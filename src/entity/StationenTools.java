/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.util.ArrayList;
import java.util.Vector;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import op.OPDE;

/**
 *
 * @author tloehr
 */
public class StationenTools {

    /**
     * Setzt eine ComboBox mit der Liste der Stationen. Wenn m�glich wird direkt die gew�nschte Standard Station eingestellt.
     * @param cmb
     */
    public static void setComboBox(JComboBox cmb){
        Query query = OPDE.getEM().createNamedQuery("Stationen.findAllSorted");
        cmb.setModel(new DefaultComboBoxModel(new Vector<Stationen>(query.getResultList())));

        //TODO: Kandidat f�r SYSProps
        long statid = OPDE.getLocalProps().containsKey("station") ? Long.parseLong(OPDE.getLocalProps().getProperty("station")) : 1l;

        Query query2 = OPDE.getEM().createNamedQuery("Stationen.findByStatID");
        query2.setParameter("statID", statid);
        Stationen station = (Stationen) query2.getSingleResult();
        cmb.setSelectedItem(station);
    }

}
