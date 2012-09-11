/*
 * OffenePflege
 * Copyright (C) 2006-2012 Torsten Löhr
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

import entity.prescription.*;
import op.OPDE;
import op.tools.SYSTools;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author root
 */
public class DlgDAF extends javax.swing.JDialog {
    private TradeForm darreichung;
    private boolean editMode;


    public DlgDAF(JFrame parent, String title, TradeForm darreichung) {
        super(parent, true);
        initComponents();
        this.darreichung = darreichung;
        EntityManager em = OPDE.createEM();
        Query query = em.createNamedQuery("MedFormen.findAll");
        cmbForm.setModel(new DefaultComboBoxModel(query.getResultList().toArray(new DosageForm[]{})));
        cmbForm.setRenderer(DosageFormTools.getRenderer(0));
        em.close();
        editMode = darreichung.getDafID() != null;

        if (editMode) {

//            //HashMap daf = DBRetrieve.getSingleRecord("MPDarreichung", new String[]{"Zusatz", "FormID"}, "DafID", dafid);
////            double apv = op.care.med.DBHandling.getAPV4(dafid, "");
////            long thisFormID = ((BigInteger) daf.get("FormID")).longValue();
//
//            apv = APVTools.getAPV4(darreichung);
//            if (apv == null) {
//                apv = new APV(BigDecimal.ONE, false, null, darreichung);
//            }
//
//            if (darreichung.getDosageForm().getState() == MedFormenTools.APV1) {
//                txtAPV.setText("1");
//                txtAPV.setEnabled(false);
//            } else {
//                txtAPV.setText(apv.getAPV4().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//                txtAPV.setEnabled(true);
//            }

            cmbForm.setSelectedItem(darreichung.getDosageForm());
            txtZusatz.setText(SYSTools.catchNull(darreichung.getZusatz()));

        } else {
//            apv = new APV(BigDecimal.ONE, false, null, darreichung);
            cmbForm.setSelectedIndex(1);
        }

        setTitle(title);
        SYSTools.centerOnParent(parent, this);
        setVisible(true);
    }


    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jSeparator1 = new JSeparator();
        jLabel2 = new JLabel();
        txtZusatz = new JTextField();
        jLabel3 = new JLabel();
        cmbForm = new JComboBox();
        jSeparator2 = new JSeparator();
        btnCancel = new JButton();
        btnOK = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        Container contentPane = getContentPane();

        //======== jPanel1 ========
        {

            //---- jLabel1 ----
            jLabel1.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabel1.setText("Darreichungsform");

            //---- jLabel2 ----
            jLabel2.setText("Zusatzbezeichnung:");

            //---- jLabel3 ----
            jLabel3.setText("Form:");

            //---- cmbForm ----
            cmbForm.setModel(new DefaultComboBoxModel(new String[] {
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4"
            }));
            cmbForm.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    cmbFormItemStateChanged(e);
                }
            });

            //---- btnCancel ----
            btnCancel.setIcon(new ImageIcon(getClass().getResource("/artwork/16x16/cancel.png")));
            btnCancel.setText("Abbrechen");
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnCancelActionPerformed(e);
                }
            });

            //---- btnOK ----
            btnOK.setIcon(new ImageIcon(getClass().getResource("/artwork/16x16/apply.png")));
            btnOK.setText("OK");
            btnOK.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnOKActionPerformed(e);
                }
            });

            GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup()
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup()
                            .addComponent(jSeparator1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                            .addComponent(jLabel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                                    .addComponent(jLabel3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup()
                                    .addComponent(cmbForm, 0, 258, Short.MAX_VALUE)
                                    .addComponent(txtZusatz, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)))
                            .addComponent(jSeparator2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnOK)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancel)))
                        .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup()
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtZusatz, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbForm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(52, 52, 52)
                        .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancel)
                            .addComponent(btnOK))
                        .addContainerGap(11, Short.MAX_VALUE))
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed

        darreichung.setZusatz(txtZusatz.getText());
        darreichung.setDosageForm((DosageForm) cmbForm.getSelectedItem());


        EntityManager em = OPDE.createEM();
        try {
            em.getTransaction().begin();
            if (editMode) {
                darreichung = em.merge(darreichung);
//                apv = em.merge(apv);
            } else {
                em.persist(darreichung);
//                apv.setTauschen(darreichung.getDosageForm().getState() == MedFormenTools.APV_PER_DAF);
//                em.persist(apv);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            OPDE.fatal(e);
        } finally {
            em.close();
        }
        dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void cmbFormItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFormItemStateChanged
        DosageForm form = (DosageForm) evt.getItem();
//        lblAnw.setText(MedFormenTools.EINHEIT[form.getAnwEinheit()]);
//        lblPack.setText(MedFormenTools.EINHEIT[form.getPackEinheit()]);
//        txtAPV.setText("1");
//        txtAPV.setEnabled(form.getState() != MedFormenTools.APV1);
//        apv.setAPV(BigDecimal.ONE);
    }//GEN-LAST:event_cmbFormItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel jPanel1;
    private JLabel jLabel1;
    private JSeparator jSeparator1;
    private JLabel jLabel2;
    private JTextField txtZusatz;
    private JLabel jLabel3;
    private JComboBox cmbForm;
    private JSeparator jSeparator2;
    private JButton btnCancel;
    private JButton btnOK;
    // End of variables declaration//GEN-END:variables

}
