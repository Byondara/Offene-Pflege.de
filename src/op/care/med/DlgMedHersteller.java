/*
 * OffenePflege
 * Copyright (C) 2008 Torsten Löhr
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
 * Auf deutsch (freie Übersetzung. Rechtlich gilt die englische Version)
 * Dieses Programm ist freie Software. Sie können es unter den Bedingungen der GNU General Public License, 
 * wie von der Free Software Foundation veröffentlicht, weitergeben und/oder modifizieren, gemäß Version 2 der Lizenz.
 *
 * Die Veröffentlichung dieses Programms erfolgt in der Hoffnung, daß es Ihnen von Nutzen sein wird, aber 
 * OHNE IRGENDEINE GARANTIE, sogar ohne die implizite Garantie der MARKTREIFE oder der VERWENDBARKEIT FÜR EINEN 
 * BESTIMMTEN ZWECK. Details finden Sie in der GNU General Public License.
 *
 * Sie sollten ein Exemplar der GNU General Public License zusammen mit diesem Programm erhalten haben. Falls nicht, 
 * schreiben Sie an die Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA.
 * 
 */

package op.care.med;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import entity.EntityTools;
import entity.verordnungen.MedHersteller;
import op.OPDE;
import op.tools.SYSTools;

import javax.swing.*;
import java.util.HashMap;

/**
 * @author tloehr
 */
public class DlgMedHersteller extends javax.swing.JDialog {

    /**
     * Creates new form DlgMedHersteller
     */
//    public DlgMedHersteller(java.awt.Frame parent) {
//        super(parent, true);
//        initDialog();
//        SYSTools.centerOnParent(this, parent);
//        setVisible(true);
//    }

    /**
     * Creates new form DlgMedHersteller
     */

    private MedHersteller hersteller;

    public DlgMedHersteller(JDialog parent, MedHersteller hersteller) {
        super(parent, true);
        initDialog();
        this.hersteller = hersteller;
        SYSTools.centerOnParent(parent, this);
        setVisible(true);
    }

    private void initDialog() {
        initComponents();
        txtFirma.setText("");
        txtFax.setText("");
        txtOrt.setText("");
        txtPLZ.setText("");
        txtStrasse.setText("");
        txtTel.setText("");
        txtWWW.setText("");

    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Erzeugter Quelltext ">//GEN-BEGIN:initComponents
    private void initComponents() {
        panel1 = new JPanel();
        jLabel1 = new JLabel();
        jSeparator1 = new JSeparator();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        txtPLZ = new JTextField();
        txtFirma = new JTextField();
        txtStrasse = new JTextField();
        txtTel = new JTextField();
        txtFax = new JTextField();
        txtWWW = new JTextField();
        jSeparator2 = new JSeparator();
        btnCancel = new JButton();
        btnOK = new JButton();
        txtOrt = new JTextField();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "default",
            "fill:default"));

        //======== panel1 ========
        {
            panel1.setLayout(new FormLayout(
                "4*(default, $lcgap), default",
                "9*(fill:default, $lgap), fill:default"));

            //---- jLabel1 ----
            jLabel1.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabel1.setText("Neuen Hersteller eingeben");
            panel1.add(jLabel1, CC.xywh(1, 1, 5, 1));
            panel1.add(jSeparator1, CC.xywh(1, 3, 9, 1));

            //---- jLabel2 ----
            jLabel2.setText("Firma:");
            panel1.add(jLabel2, CC.xy(1, 5));

            //---- jLabel3 ----
            jLabel3.setText("Strasse:");
            panel1.add(jLabel3, CC.xy(1, 7));

            //---- jLabel4 ----
            jLabel4.setText("PLZ, Ort:");
            panel1.add(jLabel4, CC.xy(1, 9));

            //---- jLabel5 ----
            jLabel5.setText("Telefon:");
            panel1.add(jLabel5, CC.xy(1, 11));

            //---- jLabel6 ----
            jLabel6.setText("Fax:");
            panel1.add(jLabel6, CC.xy(1, 13));

            //---- jLabel7 ----
            jLabel7.setText("WWW:");
            panel1.add(jLabel7, CC.xy(1, 15));

            //---- txtPLZ ----
            txtPLZ.setText("jTextField1");
            panel1.add(txtPLZ, CC.xy(3, 9));

            //---- txtFirma ----
            txtFirma.setText("jTextField2");
            txtFirma.addCaretListener(new CaretListener() {
                @Override
                public void caretUpdate(CaretEvent e) {
                    txtFirmaCaretUpdate(e);
                }
            });
            panel1.add(txtFirma, CC.xywh(3, 5, 7, 1));

            //---- txtStrasse ----
            txtStrasse.setText("jTextField3");
            panel1.add(txtStrasse, CC.xywh(3, 7, 7, 1));

            //---- txtTel ----
            txtTel.setText("jTextField4");
            panel1.add(txtTel, CC.xywh(3, 11, 7, 1));

            //---- txtFax ----
            txtFax.setText("jTextField5");
            panel1.add(txtFax, CC.xywh(3, 13, 7, 1));

            //---- txtWWW ----
            txtWWW.setText("jTextField6");
            panel1.add(txtWWW, CC.xywh(3, 15, 7, 1));
            panel1.add(jSeparator2, CC.xywh(1, 17, 9, 1));

            //---- btnCancel ----
            btnCancel.setIcon(new ImageIcon(getClass().getResource("/artwork/16x16/cancel.png")));
            btnCancel.setText("Abbrechen");
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnCancelActionPerformed(e);
                }
            });
            panel1.add(btnCancel, CC.xy(9, 19));

            //---- btnOK ----
            btnOK.setIcon(new ImageIcon(getClass().getResource("/artwork/16x16/apply.png")));
            btnOK.setText("OK");
            btnOK.setEnabled(false);
            btnOK.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnOKActionPerformed(e);
                }
            });
            panel1.add(btnOK, CC.xy(7, 19));

            //---- txtOrt ----
            txtOrt.setText("jTextField2");
            panel1.add(txtOrt, CC.xywh(5, 9, 5, 1));
        }
        contentPane.add(panel1, CC.xy(1, 1));
        pack();
        setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents

    private void txtFirmaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtFirmaCaretUpdate
        btnOK.setEnabled(!txtFirma.getText().equals(""));
    }//GEN-LAST:event_txtFirmaCaretUpdate

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        hersteller = new MedHersteller(txtFirma.getText(), txtStrasse.getText(), txtPLZ.getText(), txtOrt.getText(), txtTel.getText(), txtFax.getText(), txtWWW.getText());
        EntityTools.persist(hersteller);
        dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    public void dispose() {
        SYSTools.unregisterListeners(this);
        super.dispose();
    }


    // Variablendeklaration - nicht modifizieren//GEN-BEGIN:variables
    private JPanel panel1;
    private JLabel jLabel1;
    private JSeparator jSeparator1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JTextField txtPLZ;
    private JTextField txtFirma;
    private JTextField txtStrasse;
    private JTextField txtTel;
    private JTextField txtFax;
    private JTextField txtWWW;
    private JSeparator jSeparator2;
    private JButton btnCancel;
    private JButton btnOK;
    private JTextField txtOrt;
    // Ende der Variablendeklaration//GEN-END:variables

}
