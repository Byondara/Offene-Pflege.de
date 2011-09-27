/*
 * OffenePflege
 * Copyright (C) 2008 Torsten L�hr
 * This program is free software; you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License V2 as published by the Free Software Foundation
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to 
 * the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 * www.offene-pflege.de
 * ------------------------ 
 * Auf deutsch (freie �bersetzung. Rechtlich gilt die englische Version)
 * Dieses Programm ist freie Software. Sie k�nnen es unter den Bedingungen der GNU General Public License, 
 * wie von der Free Software Foundation ver�ffentlicht, weitergeben und/oder modifizieren, gem�� Version 2 der Lizenz.
 *
 * Die Ver�ffentlichung dieses Programms erfolgt in der Hoffnung, da� es Ihnen von Nutzen sein wird, aber 
 * OHNE IRGENDEINE GARANTIE, sogar ohne die implizite Garantie der MARKTREIFE oder der VERWENDBARKEIT F�R EINEN 
 * BESTIMMTEN ZWECK. Details finden Sie in der GNU General Public License.
 *
 * Sie sollten ein Exemplar der GNU General Public License zusammen mit diesem Programm erhalten haben. Falls nicht, 
 * schreiben Sie an die Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA.
 * 
 */
package op.care.uebergabe;

import entity.Einrichtungen;
import entity.Pflegeberichte;
import entity.PflegeberichteTools;
import entity.Uebergabebuch;
import entity.UebergabebuchTools;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Query;
import javax.swing.table.AbstractTableModel;
import op.OPDE;
import op.tools.SYSCalendar;

/**
 * @author tloehr
 */
public class TMUebergabe
        extends AbstractTableModel {

    public static final int COL_PIT = 0;
    public static final int COL_INFO = 1;
    public static final int COL_HTML = 2;
    public static final int COL_ACKN = 3;
    public static final int COL_BERICHT = 99;
    public static final int SORT_NAME = 0;
    public static final int SORT_UHRZEIT = 1;
    public static final int LIST_BERICHT = 0;
    public static final int LIST_ACKNOWLEDGED = 1;

    protected ArrayList berichte;

    public TMUebergabe(Date datum, Einrichtungen einrichtung) {
        super();

        Query queryUB = OPDE.getEM().createNamedQuery("Uebergabebuch.findByEinrichtungAndDatumAndAckUser");
        queryUB.setParameter(1, OPDE.getLogin().getUser().getUKennung());
        queryUB.setParameter(2, einrichtung.getEKennung());
        queryUB.setParameter(3, new Date(SYSCalendar.startOfDay(datum)));
        queryUB.setParameter(4, new Date(SYSCalendar.endOfDay(datum)));

        Query queryPB = OPDE.getEM().createNamedQuery("Pflegeberichte.findByEinrichtungAndDatumAndAckUser");
        queryPB.setParameter(1, OPDE.getLogin().getUser().getUKennung());
        queryPB.setParameter(2, einrichtung.getEKennung());
        queryPB.setParameter(3, new Date(SYSCalendar.startOfDay(datum)));
        queryPB.setParameter(4, new Date(SYSCalendar.endOfDay(datum)));

        berichte = new ArrayList();
        berichte.addAll(queryUB.getResultList());
        berichte.addAll(queryPB.getResultList());

    }

    @Override
    public int getRowCount() {
        return berichte.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    public ArrayList getBerichte(){
        return berichte;
    }

    @Override
    public Class getColumnClass(int c) {
        return String.class;
//            if (1 >= c+1 && c+1 <= 4) return String.class;
//            return Boolean.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object result = null;
        OPDE.getLogger().debug(berichte);
        Object[] bericht = (Object[]) berichte.get(row);
        // Zur Info
        // bericht[LIST_BERICHT] enth�lt immer das Berichte Objekt (entweder Ubergabebuch oder Pflegeberichte)
        // bericht[1] ist 1, wenn der aktuelle User den Bericht best�tigt hat. 0 sonst.

        if (bericht[LIST_BERICHT] instanceof Uebergabebuch) {
            switch (col) {
                case COL_PIT: {
                    result = UebergabebuchTools.getDatumUndUser((Uebergabebuch) bericht[LIST_BERICHT], false);
                    break;
                }
                case COL_INFO: {
                    result = UebergabebuchTools.getEinrichtungAsHTML((Uebergabebuch) bericht[LIST_BERICHT]);
                    break;
                }
                case COL_HTML: {
                    result = UebergabebuchTools.getAsHTML((Uebergabebuch) bericht[LIST_BERICHT]);
                    break;
                }
                case COL_ACKN: { // Vom aktuellen User bereits gesehen ?
                    result = ((Long) bericht[LIST_ACKNOWLEDGED]).longValue() > 0;
                    break;
                }
                default: {
                    result = bericht[LIST_BERICHT];
                    break;
                }
            }
        } else {
            switch (col) {
                case COL_PIT: {
                    result = PflegeberichteTools.getDatumUndUser((Pflegeberichte) bericht[LIST_BERICHT], false);
                    break;
                }
                case COL_INFO: {
                    result = PflegeberichteTools.getBewohnerName((Pflegeberichte) bericht[LIST_BERICHT]);
                    break;
                }
                case COL_HTML: {
                    result = PflegeberichteTools.getAsHTML((Pflegeberichte) bericht[LIST_BERICHT]);
                    break;
                }
                case COL_ACKN: { // Vom aktuellen User bereits gesehen ?
                    result = ((Long) bericht[LIST_ACKNOWLEDGED]).longValue() > 0;
                    break;
                }
                default: {
                    result = bericht[LIST_BERICHT];
                    break;
                }
            }
        }

        return result;
    }
}
