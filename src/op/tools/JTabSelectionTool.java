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

import java.awt.image.IndexColorModel;
import java.util.HashMap;
import javax.swing.JTabbedPane;

/**
 *
 * @author root
 */
public class JTabSelectionTool {
    private HashMap index;
    private JTabbedPane input;
    /** Creates a new instance of JTabSelectionTool */
    public JTabSelectionTool(JTabbedPane input) {
        this.input = input;
        index = new HashMap();
        for(int i=0; i < input.getTabCount(); i++){
            index.put(input.getTitleAt(i), i);
        }
    }
    
    public int getIndex(String title){
        Integer i = (Integer) index.get(title);
        int ind;
        if (i != null){
            ind = i.intValue();
        } else {
            ind = -1;
        }
        return ind;
    }
    
    public void setTab(String title){
        input.setSelectedIndex(getIndex(title));
    }
}
