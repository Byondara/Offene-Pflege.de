/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.persistence.Query;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import op.OPDE;

/**
 *
 * @author tloehr
 */
public class PBerichtTAGSTools {

    /**
     * Erstellt ein JMenu bestehend aus Checkboxen. F�r jede aktive PBerichtTag jeweils eine.
     * Wenn man die anklickt, wird eine Markierung zum Bericht hinzugef�gt. Dieses Men�
     * wird in PnlBerichte verwendet. Als Kontextmen� f�r die einzelnen Berichtszeilen.
     * @param bericht Der Bericht, f�r den das Men� erzeugt werden soll.
     * Je nachdem, welche Tags diesem Bericht schon zugewiesen sind, werden die Checkboxen bereits angeklickt oder auch nicht.
     * F�r das Men� wird ein Listener definiert, der weitere Tags setzt oder entfernt.
     * @return das vorbereitete Men�
     */
    public static JMenu createMenuForTags(Pflegeberichte bericht) {
        final Pflegeberichte finalbericht = bericht;
        Query query = OPDE.getEM().createNamedQuery("PBerichtTAGS.findAllActive");
        ArrayList<PBerichtTAGS> tags = new ArrayList(query.getResultList());

        JMenu menu = new JMenu("Text-Markierungen");
        Iterator<PBerichtTAGS> itTags = tags.iterator();
        while (itTags.hasNext()) {
            final PBerichtTAGS tag = itTags.next();
            JCheckBox cb = new JCheckBox(tag.getBezeichnung());
            cb.setForeground(tag.getColor());
            if (tag.isBesonders()) {
                cb.setFont(new Font("Lucida Grande", Font.BOLD, 13));
            }
            cb.setSelected(bericht.getTags().contains(tag));


            cb.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.DESELECTED) {
                        finalbericht.getTags().remove(tag);
                    } else {
                        finalbericht.getTags().add(tag);
                    }
                }
            });
            menu.add(cb);

        }

        menu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                OPDE.getEM().getTransaction().begin();
                OPDE.getEM().merge(finalbericht);
                OPDE.getEM().getTransaction().commit();
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        return menu;
    }


    /**
     * Erstellt eine JPanel, die mit Checkboxen gef�llt ist. Pro aktive PBerichtTag jeweils eine.
     *
     * @param listener Ein ItemListener, der sagt, was geschehen soll, wenn man auf die Checkboxen klickt.
     * @param preselect Eine Collection aus Tags besteht. Damit kann man einstellen, welche Boxen schon vorher angeklickt sein sollen.
     * @param layout Ein Layoutmanager f�r das Panel.
     * @return das Panel zur weiteren Verwendung.
     */
    public static JPanel createCheckBoxPanelForTags(ItemListener listener, Collection<PBerichtTAGS> preselect, LayoutManager layout) {
        Query query = OPDE.getEM().createNamedQuery("PBerichtTAGS.findAllActive");
        ArrayList<PBerichtTAGS> tags = new ArrayList(query.getResultList());
        JPanel panel = new JPanel(layout);
        Iterator<PBerichtTAGS> itTags = tags.iterator();
        while (itTags.hasNext()) {
            PBerichtTAGS tag = itTags.next();
            JCheckBox cb = new JCheckBox(tag.getBezeichnung());
            cb.setForeground(tag.getColor());
            if (tag.isBesonders()) {
                cb.setFont(new Font("Lucida Grande", Font.BOLD, 13));
            }
            cb.putClientProperty("UserObject", tag);
            
            cb.setSelected(preselect.contains(tag));
            cb.addItemListener(listener);

            panel.add(cb);
        }
        return panel;

    }
    
    /**
     * Kleine Hilfsmethode, die ich brauche um festzustellen ob ein bestimmter bericht
     * ein Sozial Bericht ist.
     * @param collection
     * @return 
     */
    public static boolean isSozial(Pflegeberichte bericht){
        Iterator<PBerichtTAGS> itTags = bericht.getTags().iterator();
        boolean yes = false;
        while (!yes && itTags.hasNext()) {
            yes = itTags.next().getKurzbezeichnung().equalsIgnoreCase("soz");
        }
        return yes;
    }

}
