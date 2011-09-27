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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import op.OPDE;
import tablerenderer.RNDHTML;
import op.tools.SYSConst;

/**
 *
 * @author tloehr
 */
public class RNDUbergabe
        extends RNDHTML {
    Color color;
    Font font;
    
    public RNDUbergabe() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                
        TableModel tm = table.getModel();
        //long qsuid = ((Long) tm.getValueAt(row, TMUebergabe.COL_QSUID)).longValue();
        
        if (row % 2 == 0 && !isSelected) {
            this.color = Color.WHITE;
            // cell is selected, use the highlight color
        } else if (isSelected) {
            this.color = Color.LIGHT_GRAY;
        } else {
            this.color = SYSConst.khaki1;
        }

        OPDE.getLogger().debug(value);
        
        // Das hier f�r das Zeichen zur Kenntnisnahme
        if (column == TMUebergabe.COL_ACKN){
            JLabel j;
            if ((Boolean) value){
                j = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/artwork/16x16/apply.png")));
            } else {
                j = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/artwork/16x16/help.png")));
            }
            j.setOpaque(true);
            j.setBackground(color);
            return j;
        }

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    @Override
    public Color getBackground(){
        return color;
    }
    
} // RNDUbergabe
