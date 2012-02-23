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
package op.care.med.vorrat;

import entity.verordnungen.*;
import op.OPDE;

import op.tools.SYSConst;
import op.tools.SYSTools;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.*;
import javax.swing.border.SoftBevelBorder;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * @author tloehr
 */
public class DlgBestandAbschliessen extends javax.swing.JDialog {

    private MedBestand bestand;
    private java.awt.Frame parent;

    /**
     * Creates new form DlgBestandAnbruch
     */
    public DlgBestandAbschliessen(java.awt.Frame parent, MedBestand bestand) {
        super(parent, true);
        this.parent = parent;
        this.bestand = bestand;
        initComponents();
        initDialog();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new JLabel();
        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        txtInfo = new JTextPane();
        rbLeer = new JRadioButton();
        rbStellen = new JRadioButton();
        txtLetzte = new JTextField();
        lblEinheiten = new JLabel();
        rbAbgelaufen = new JRadioButton();
        jSeparator1 = new JSeparator();
        jLabel2 = new JLabel();
        cmbBestID = new JComboBox();
        jLabel3 = new JLabel();
        rbGefallen = new JRadioButton();
        btnClose = new JButton();
        btnOk = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        Container contentPane = getContentPane();

        //---- jLabel1 ----
        jLabel1.setFont(new Font("Dialog", Font.BOLD, 16));
        jLabel1.setText("Bestand abschlie\u00dfen");

        //======== jPanel1 ========
        {
            jPanel1.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));

            //======== jScrollPane1 ========
            {

                //---- txtInfo ----
                txtInfo.setEditable(false);
                jScrollPane1.setViewportView(txtInfo);
            }

            //---- rbLeer ----
            rbLeer.setSelected(true);
            rbLeer.setText("Die Packung ist nun leer");
            rbLeer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rbLeerActionPerformed(e);
                }
            });

            //---- rbStellen ----
            rbStellen.setText("Beim Vorab Stellen haben Sie die letzten ");
            rbStellen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rbStellenActionPerformed(e);
                }
            });

            //---- txtLetzte ----
            txtLetzte.setText("jTextField1");
            txtLetzte.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    txtLetzteFocusLost(e);
                }
            });

            //---- lblEinheiten ----
            lblEinheiten.setText("Einheiten verbraucht.");

            //---- rbAbgelaufen ----
            rbAbgelaufen.setText("Die Packung ist abgelaufen oder wird nicht mehr ben\u00f6tigt. Bereit zur Entsorgung.");
            rbAbgelaufen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rbAbgelaufenActionPerformed(e);
                }
            });

            //---- jLabel2 ----
            jLabel2.setText("Als n\u00e4chstes Packung soll die Nummer:");

            //---- cmbBestID ----
            cmbBestID.setModel(new DefaultComboBoxModel(new String[]{
                    "Item 1",
                    "Item 2",
                    "Item 3",
                    "Item 4"
            }));
            cmbBestID.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    cmbBestIDItemStateChanged(e);
                }
            });

            //---- jLabel3 ----
            jLabel3.setText("angebrochen werden.");

            //---- rbGefallen ----
            rbGefallen.setText("<html>Die Packung ist <font color=\"red\">runter gefallen</font> oder <font color=\"red\">verschwunden</font> und muss ausgebucht werden.</html>");
            rbGefallen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rbGefallenActionPerformed(e);
                }
            });

            GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup()
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel1Layout.createParallelGroup()
                                            .addComponent(rbGefallen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(rbAbgelaufen)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(rbStellen)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(txtLetzte, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(lblEinheiten))
                                            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE)
                                            .addComponent(rbLeer)
                                            .addComponent(jSeparator1, GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel2)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(cmbBestID, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel3)))
                                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup()
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(rbLeer)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(rbStellen)
                                            .addComponent(lblEinheiten)
                                            .addComponent(txtLetzte, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(rbAbgelaufen)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(rbGefallen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(cmbBestID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3))
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
        }

        //---- btnClose ----
        btnClose.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/cancel.png")));
        btnClose.setText("Schlie\u00dfen");
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCloseActionPerformed(e);
            }
        });

        //---- btnOk ----
        btnOk.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/apply.png")));
        btnOk.setText("\u00dcbernehmen");
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnOkActionPerformed(e);
            }
        });

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(jPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1)
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                .addComponent(btnOk)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnClose)))
                                .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnClose)
                                        .addComponent(btnOk))
                                .addContainerGap(30, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(rbLeer);
        buttonGroup1.add(rbStellen);
        buttonGroup1.add(rbAbgelaufen);
        buttonGroup1.add(rbGefallen);
    }// </editor-fold>//GEN-END:initComponents

    private void rbAbgelaufenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAbgelaufenActionPerformed
        txtLetzte.setEnabled(rbStellen.isSelected());
    }//GEN-LAST:event_rbAbgelaufenActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void initDialog() {
        this.setTitle(SYSTools.getWindowTitle("Bestand abschließen"));
        String text = "Sie möchten den Bestand mit der Nummer <font color=\"red\"><b>" + bestand.getBestID() + "</b></font> abschließen.";
        text += "<br/>" + MedBestandTools.getBestandTextAsHTML(bestand) + "</br>";
        text += "<br/>Bitte wählen Sie einen der drei folgenden Gründe für den Abschluss:";
        txtInfo.setContentType("text/html");
        txtInfo.setText(SYSTools.toHTML(text));

        EntityManager em = OPDE.createEM();

        Query query = em.createQuery(" " +
                " SELECT b FROM MedBestand b " +
                " WHERE b.vorrat = :vorrat AND b.aus = :aus AND b.anbruch = :anbruch " +
                " ORDER BY b.ein, b.bestID "); // Geht davon aus, dass die PKs immer fortlaufend, automatisch vergeben werden.
        query.setParameter("vorrat", bestand.getVorrat());
        query.setParameter("aus", SYSConst.DATE_BIS_AUF_WEITERES);
        query.setParameter("anbruch", SYSConst.DATE_BIS_AUF_WEITERES);
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel(query.getResultList().toArray());
        dcbm.insertElementAt("keine", 0);
        cmbBestID.setModel(dcbm);
        cmbBestID.setRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
                String text = o instanceof MedBestand ? ((MedBestand) o).getBestID().toString() : o.toString();
                return new JLabel(text);
            }
        });
        em.close();

        int index = Math.min(2, cmbBestID.getItemCount());
        cmbBestID.setSelectedIndex(index - 1);

        lblEinheiten.setText(MedFormenTools.EINHEIT[bestand.getDarreichung().getMedForm().getPackEinheit()] + " verbraucht");
        txtLetzte.setText("");
        txtLetzte.setEnabled(false);
        SYSTools.centerOnParent(parent, this);
        // Das mit dem Vorabstellen nur bei Formen, die auf Stück basieren also APV = 1
        rbStellen.setEnabled(bestand.getDarreichung().getMedForm().getStatus() == MedFormenTools.APV1);
        setVisible(true);
    }

    @Override
    public void dispose() {
        SYSTools.unregisterListeners(this);
        super.dispose();
    }

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        save();//GEN-LAST:event_btnOkActionPerformed
        dispose();
    }

    private void rbStellenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbStellenActionPerformed
        txtLetzte.setEnabled(true);
        txtLetzte.requestFocus();
    }//GEN-LAST:event_rbStellenActionPerformed

    private void rbLeerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLeerActionPerformed
        txtLetzte.setEnabled(rbStellen.isSelected());
    }//GEN-LAST:event_rbLeerActionPerformed

    private void txtLetzteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLetzteFocusLost
        try {
            double inhalt = Double.parseDouble(txtLetzte.getText().replace(",", "."));
            if (inhalt <= 0d) {
                txtLetzte.setText("1");
            }
        } catch (NumberFormatException ex) {
            txtLetzte.setText("1");
        }
    }//GEN-LAST:event_txtLetzteFocusLost

    private void cmbBestIDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBestIDItemStateChanged
        if (cmbBestID.getSelectedIndex() == 0) {
            cmbBestID.setToolTipText(null);
        } else {
            MedBestand myBestand = (MedBestand) cmbBestID.getSelectedItem();
            cmbBestID.setToolTipText(SYSTools.toHTML(MedBestandTools.getBestandTextAsHTML(myBestand)));
        }
    }//GEN-LAST:event_cmbBestIDItemStateChanged

    private void rbGefallenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbGefallenActionPerformed
        txtLetzte.setEnabled(rbStellen.isSelected());
    }//GEN-LAST:event_rbGefallenActionPerformed

    private void save() {
        String classname = this.getClass().getName() + ".save()";
        EntityManager em = OPDE.createEM();
        try {
            em.getTransaction().begin();

            OPDE.info("Bestands Nr. " + bestand.getBestID() + " wird abgeschlossen");
            OPDE.info("UKennung: " + OPDE.getLogin().getUser().getUKennung());

            MedBestand nextBest = null;
            if (cmbBestID.getSelectedIndex() > 0) {
                nextBest = (MedBestand) cmbBestID.getSelectedItem();
            }

            if (rbStellen.isSelected()) {
                bestand.setNaechsterBestand(nextBest);
                BigDecimal inhalt = new BigDecimal(Double.parseDouble(txtLetzte.getText().replace(",", ".")));
                MedBestandTools.setzeBestandAuf(em, bestand, inhalt, "Korrekturbuchung zum Packungsabschluss", MedBuchungenTools.STATUS_KORREKTUR_AUTO_VORAB);
                //DBHandling.setzeBestand(bestid, inhalt, "Korrekturbuchung zum Packungsabschluss", DBHandling.STATUS_KORREKTUR_AUTO_VORAB);
                //op.tools.DBHandling.updateRecord("MPBestand", hm, "BestID", bestid);
                bestand.getVorrat().getBestaende().remove(bestand);
                bestand = em.merge(bestand);
                bestand.getVorrat().getBestaende().add(bestand);

                OPDE.info(classname + ": Vorabstellen angeklickt. Sind noch " + inhalt + " in der Packung.");
                OPDE.info(classname + ": Nächste Packung im Anbruch wird die Bestands Nr.: " + nextBest.getBestID() + " sein.");

            } else {
                BigDecimal apv = bestand.getApv();

                if (rbGefallen.isSelected()) {
                    MedBestandTools.abschliessen(em, bestand, "Packung ist runtergefallen.", MedBuchungenTools.STATUS_KORREKTUR_AUTO_RUNTERGEFALLEN);
                    //DBHandling.closeBestand(bestid, "Packung ist runtergefallen.", false, DBHandling.STATUS_KORREKTUR_AUTO_RUNTERGEFALLEN);
                    OPDE.info(classname + ": Runtergefallen angeklickt.");
                } else if (rbAbgelaufen.isSelected()) {
                    MedBestandTools.abschliessen(em, bestand, "Packung ist abgelaufen.", MedBuchungenTools.STATUS_KORREKTUR_AUTO_ABGELAUFEN);
                    //DBHandling.closeBestand(bestid, "Packung ist abgelaufen.", false, DBHandling.STATUS_KORREKTUR_AUTO_ABGELAUFEN);
                    OPDE.info(classname + ": Abgelaufen angeklickt.");
                } else {
                    MedBestandTools.abschliessen(em, bestand, "Korrekturbuchung zum Packungsabschluss", MedBuchungenTools.STATUS_KORREKTUR_AUTO_LEER);
                    apv = MedBestandTools.berechneAPV(bestand);
                    //DBHandling.closeBestand(bestid, "Korrekturbuchung zum Packungsabschluss", true, DBHandling.STATUS_KORREKTUR_AUTO_LEER);
                    OPDE.info(classname + ": Packung ist nun leer angeklickt.");
                }
                if (nextBest != null) {
                    MedBestandTools.anbrechen(em, nextBest, apv);
                    OPDE.info(classname + ": Nächste Packung mit Bestands Nr.: " + nextBest.getBestID() + " wird nun angebrochen.");
                } else {
                    // es wurde kein nächster angebrochen ?
                    // könnte es sein, dass dieser Vorrat keine Packungen mehr hat ?
                    if (MedVorratTools.getNaechsteNochUngeoeffnete(bestand.getVorrat()) == null && MedVorratTools.getNaechsteNochUngeoeffnete(bestand.getVorrat()) == null) {
                        // Dann prüfen, ob dieser Vorrat zu Verordnungen gehört, die nur bis Packungs Ende laufen sollen
                        // Die müssen dann jetzt nämlich abgeschlossen werden.
                        Iterator<Verordnung> itVerordnung = VerordnungTools.getVerordnungenByVorrat(em, bestand.getVorrat(), true).iterator();
                        while (itVerordnung.hasNext()){
                            Verordnung verordnung = itVerordnung.next();
                            VerordnungTools.absetzen(em, verordnung, verordnung.getAnArzt(), verordnung.getAnKH());
                        }
                    }
                }
//                if (!DBHandling.hasAnbruch(vorid)) { // Keine mehr im Anbruch ?
//                    // Dann alles absetzen, was zu diesem Vorrat gehörte und bis PackEnde lief.
//                    op.care.verordnung.DBHandling.absetzenBisPackEnde2Vorrat(vorid);
//                }
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            OPDE.fatal(e);
        } finally {
            em.close();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextPane txtInfo;
    private JRadioButton rbLeer;
    private JRadioButton rbStellen;
    private JTextField txtLetzte;
    private JLabel lblEinheiten;
    private JRadioButton rbAbgelaufen;
    private JSeparator jSeparator1;
    private JLabel jLabel2;
    private JComboBox cmbBestID;
    private JLabel jLabel3;
    private JRadioButton rbGefallen;
    private JButton btnClose;
    private JButton btnOk;
    // End of variables declaration//GEN-END:variables
}
