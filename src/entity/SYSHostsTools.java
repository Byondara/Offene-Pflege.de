/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Query;
import op.OPDE;
import op.tools.SYSCalendar;
import op.tools.SYSTools;

/**
 *
 * @author tloehr
 */
public class SYSHostsTools {

    public static SYSHosts getHost(String hostkey) {
        SYSHosts host = null;
        InetAddress localMachine = null;

        try {
            localMachine = InetAddress.getLocalHost();
        } catch (java.net.UnknownHostException uhe) {
            OPDE.fatal(uhe);
            System.exit(0);
        }

        Query query = OPDE.getEM().createNamedQuery("SYSHosts.findByHostKey");
        query.setParameter("hostKey", hostkey);
        boolean mainhost = SYSTools.catchNull(OPDE.getLocalProps().getProperty("mainhost")).equalsIgnoreCase("true");

        try {
            // doppelte Hostkeys k�nnen nicht auftreten. Die sind unique.
            host = (SYSHosts) query.getSingleResult();

            // Das bedeutet, dass der Host sich entweder korrekt abgemeldet hat oder sich mindestens 2 Minuten lang nicht gemeldet hat. Dann gehen wir davon aus, dass der Host abgest�rzt ist.
            // Der Host scheint noch zu leben. Dann k�nnen wir nich nochmal starten. G�b sonst Durcheinander.
            if (host.getLpol() != null && SYSCalendar.earlyEnough(host.getLpol().getTime(), 2)) {
                OPDE.fatal("Es gibt bereits einen aktiven Host mit demselben Hostkey.");
                host = null;
            } else {
                // ===================== REPARATUR DEFEKTER HOST EINTR�GE ======================
                // Ein fr�here Sitzung ist zusammengebrochen und nicht sauber beendet worden.
                // Da m�ssen wir erst aufr�umen.
                if (host.getLpol() != null && !SYSCalendar.earlyEnough(host.getLpol().getTime(), 2)) {
                    OPDE.warn("Host wurde beim letzten mal nicht korrekt beendet. Wird jetzt behoben.");
                    OPDE.getEM().getTransaction().begin();
                    try {
                        // Welche Logins h�ngen an diesem besch�digten Host ?
                        Query queryLogin = OPDE.getEM().createNamedQuery("SYSLogin.findByHost");
                        queryLogin.setParameter("host", host);
                        List<SYSLogin> logins = queryLogin.getResultList();
                        if (!logins.isEmpty()) {
                            Iterator<SYSLogin> itLogin = logins.iterator();
                            while (itLogin.hasNext()) {
                                SYSLogin login = itLogin.next();
                                login.setLogout(host.getLpol());

                                Query queryRC = OPDE.getEM().createNamedQuery("SYSRunningClasses.findByLogin");
                                queryRC.setParameter("login", login);
                                List<SYSRunningClasses> rc = queryRC.getResultList();
                                Iterator<SYSRunningClasses> itRC = rc.iterator();
                                while (itRC.hasNext()) {
                                    OPDE.getEM().remove(itRC.next());
                                }
                            }
                        }
                        host.setLpol(null);
                        OPDE.getEM().getTransaction().commit();
                    } catch (Exception e) {
                        OPDE.getEM().getTransaction().rollback();
                    }

                    OPDE.getLogger().debug("Wir m�ssten eigentlcih aufr�umen");
                }

                if (mainhost) {
                    Query query2 = OPDE.getEM().createNamedQuery("SYSHosts.findOtherRunningMainHosts");
                    query2.setParameter("hostKey", hostkey);

                    if (!query2.getResultList().isEmpty()) {
                        SYSHosts alreadyRunningHost = (SYSHosts) query2.getResultList().get(0);
                        OPDE.warn("Es gibt bereits einen laufenden MainHost mit der Adresse: " + alreadyRunningHost.getIp());
                        OPDE.warn("Unsere Maschine l�uft entgegen des Wunsches nun als normaler Host. Bitte local.properties reparieren.");
                        mainhost = false;
                    }
                }
                if (host.getMainHost() != mainhost) {
                    host.setMainHost(mainhost);
                    OPDE.getEM().getTransaction().begin();
                    OPDE.getEM().merge(host);
                    OPDE.getEM().getTransaction().commit();
                }
            }
        } catch (Exception e) {
            host = new SYSHosts(hostkey, localMachine.getHostName(), localMachine.getHostAddress(), mainhost);
            OPDE.getEM().getTransaction().begin();
            try {
                OPDE.getEM().persist(host);
                OPDE.getEM().getTransaction().commit();
            } catch (Exception e1) {
                OPDE.fatal(e1);
                System.exit(0);
            }
        }
        return host;
    }

    /**
     * Meldet den �bergebenen Host ab, indem das Last Proof of Life auf NULL gesetzt wird.
     * @param host
     */
    protected static void shutdown(SYSHosts host) {

        OPDE.getEM().getTransaction().begin();
        host.setLpol(null);
        OPDE.getEM().merge(host);
        OPDE.getEM().getTransaction().commit();

    }

    /**
     * Meldet den aktuellen Host ab.
     */
    public static void shutdown() {
        shutdown(OPDE.getHost());
    }
}
