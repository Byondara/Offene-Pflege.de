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
 */

package op.tools;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.text.JTextComponent;

/**
 *
 * @author tloehr
 */
public class GuiChecks {
    
    
    public static double checkDouble(javax.swing.event.CaretEvent evt, boolean mustBePositive){
        double dbl;
        JTextComponent txt = (JTextComponent) evt.getSource();
        Action toolTipAction = txt.getActionMap().get("hideTip");
        if (toolTipAction != null) {
            ActionEvent hideTip = new ActionEvent(txt, ActionEvent.ACTION_PERFORMED, "");
            toolTipAction.actionPerformed( hideTip );
        }
        try {
            dbl = Double.parseDouble(txt.getText().replaceAll(",","\\."));
            if (mustBePositive && dbl <= 0){
                txt.setToolTipText("<html><font color=\"red\"><b>Sie k�nnen nur Zahlen gr��er 0 eingeben</b></font></html>");
                toolTipAction = txt.getActionMap().get("postTip");
                dbl = 1d;
            } else {
                txt.setToolTipText("");
            }
            
        } catch (NumberFormatException ex) {
            if (mustBePositive){
                dbl = 1d;
            } else {
                dbl = 0d;
            }
            txt.setToolTipText("<html><font color=\"red\"><b>Sie haben eine ung�ltige Zahl eingegeben.</b></font></html>");
            toolTipAction = txt.getActionMap().get("postTip");
            if (toolTipAction != null) {
                ActionEvent postTip = new ActionEvent(txt, ActionEvent.ACTION_PERFORMED, "");
                toolTipAction.actionPerformed( postTip );
            }
        }
        return dbl;
    }
    
}
