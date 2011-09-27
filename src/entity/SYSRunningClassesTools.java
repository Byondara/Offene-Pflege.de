/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Query;
import op.OPDE;

/**
 *
 * @author tloehr
 */
public class SYSRunningClassesTools {

    /**
     * Wenn man ein Modul starten will, dann stellt sich dabei immer die Frage, ob die Verwendung dieses Moduls
     * zu einem Konflikt f�hren k�nnte. Sagen wir z.B. der User will eine Verordnung f�r einen bestimmten Bewohner
     * �ndern, w�hrend ein anderer User die BHPs f�r denselben Bewohner abhakt. Das soll verhindert werden.
     *
     * Au�erdem soll es nicht m�glich sein, dass der BHPImport l�uft, wenn das PnlVerordnung oder PnlBHP f�r irgendeinen Bewohner offen ist.
     *
     * In der Datei <code>/internalclasses.xml</code> werden die Klassen markiert, die zueinander in Konflikt stehen k�nnten.
     *
     * In unserem Beispiel kollidieren die Klassen PnlVerordnung (nachfolgend A) und PnlBHP (B) dann, wenn sie f�r <u>denselben</u> Bewohner aufgerufen
     * werden (also dieselbe <b>Signatur</b> besitzen. PnlBHP (B), PnlVerordnung (A) und BHPImport (C) kollidieren dann wenn,
     * <ol>
     *  <li>(C) starten will und bereits irgendeine Instanz von (A), (B) oder (C) l�uft. Unabh�ngig von der Signatur.</li>
     *  <li>(A) oder (B) starten wollen und (C) bereits l�uft.</li>
     * </ol>
     *
     * Die Klasse (C) aus unserem Beispiel ist dann innerhalb der Kollisionsdom�ne die MainClass. Eine MainClass startet nur, wenn keine andere Klasse
     * (signaturunabh�ngig) aus derselben Kollisionsdom�ne l�uft. Normale Klassen (also keine MainClass) laufen nur dann, wenn keine MainClass aus derselben
     * Kollisionsdom�ne l�uft.
     *
     * Die vorliegende Methode wird aufgerufen um die Verwendung einer bestimmten Klasse anzuzeigen. Sie pr�ft m�gliche Konflikte und gibt eine Liste als Antwort zur�ck.
     *
     * @return Das <b>erste</b> Objekt der Liste enth�lt das neu erstellte EntityObjekt, dass die Aktivit�t der gew�nschten Klasse anzeigt. Sollte es nicht m�glich sein, dass
     * die Klasse starten darf, dann ist das erste Objekt <code>null</code>. Das zweite (und alle weiteren folgenden) Objekt(e) enthalten die SYSRunningClasses der Module, die
     * die gew�nschte Ausf�hrung verhindert haben. Das kann bedeuten, dass (im Falle einer MainClass) die Ausf�hrung generell verhindert wurde, oder, (bei signaturabh�ngigen) Konflikten,
     * statt des gew�nschten Schreibzugriffs (RW) nur ein Lesezugriff (RO) erm�glicht wurde. Welche Status m�glich ist, steht im ersten Objekt.
     */
    public static SYSRunningClasses[] moduleStarted(String internalClassID, Object signature, short status) {
        boolean signed = signature != null;
        SYSRunningClasses runningClass = null;

        ArrayList<SYSRunningClasses> result = new ArrayList();
        result.add(null); // Ergebnis immer an erster Stelle. Auch wenn sie null bleibt.

        List unsignedConflicts = new ArrayList();
        String classesWithUnsignedConflicts = OPDE.getInternalClasses().getClassesWithUnsignedCollisions(internalClassID);

        if (!classesWithUnsignedConflicts.equals("")) {
            // Gibt es Klassen (signaturUNabh�ngig), die mit mir kollidieren, weil sie schon laufen ?
            // Konflikte OHNE Signatur muss man immer pr�fen. Egal ob eine signatur benutzt wurde oder nicht.
            String qWO = ""
                    + " SELECT s FROM SYSRunningClasses s "
                    + " WHERE s.classname IN (" + classesWithUnsignedConflicts + ") ";
            Query queryWO = OPDE.getEM().createQuery(qWO);
            unsignedConflicts = queryWO.getResultList();
        }

        // Wenn bis hier KEINE Konflikte existieren UND eine signatur vorliegt, dann m�ssen wir weiter suchen.
        if (unsignedConflicts.isEmpty()) {
            if (signed) {
                String classesWithSignedConflicts = OPDE.getInternalClasses().getClassesWithSignedCollisions(internalClassID);
                // Suchen lohnt sich nur, wenn die Klasse schreiben will und es auch potenzielle Konflikte gibt.
                if (status == SYSRunningClasses.STATUS_RW && !classesWithSignedConflicts.equals("")) {
                    String qWITH = ""
                            + " SELECT s FROM SYSRunningClasses s "
                            + " WHERE s.status = :status "
                            + " AND s.classname IN (" + classesWithSignedConflicts + ") "
                            + " AND s.signature = :signature";
                    Query queryWITH = OPDE.getEM().createQuery(qWITH);
                    queryWITH.setParameter("status", status);
                    queryWITH.setParameter("signature", signature.toString());

                    // Wenn es mindestens einen Konflikt gibt, dann wird der Status auf RO gesetzt.
                    if (!queryWITH.getResultList().isEmpty()) {
                        status = SYSRunningClasses.STATUS_RO;
                        result.addAll(queryWITH.getResultList());
                    }
                }
                runningClass = new SYSRunningClasses(internalClassID, signature.toString(), status);
            } else {
                runningClass = new SYSRunningClasses(internalClassID, null, status);
            }
            result.set(0, runningClass);
            OPDE.getEM().getTransaction().begin();
            OPDE.getEM().persist(runningClass);
            OPDE.getEM().getTransaction().commit();
        } else { // Ansonsten sind wir schon fertig.
            result.addAll(unsignedConflicts);
        }

        return result.toArray(new SYSRunningClasses[]{});
    }

    public static void moduleEnded(SYSRunningClasses runningClass) {
        OPDE.getEM().getTransaction().begin();
        OPDE.getEM().remove(runningClass);
        OPDE.getEM().getTransaction().commit();
    }

    public static void endAllModules(SYSLogin login) {
        Query query = OPDE.getEM().createNamedQuery("SYSRunningClasses.findByLogin");
        query.setParameter("login", login);
        ArrayList<SYSRunningClasses> list = new ArrayList(query.getResultList());

        if (!list.isEmpty()) {
            Iterator<SYSRunningClasses> it = list.iterator();
            while (it.hasNext()) {
                OPDE.getEM().remove(it.next());
            }
        }
    }
}
