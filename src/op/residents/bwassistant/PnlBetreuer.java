/*
 * Created by JFormDesigner on Mon Jul 09 15:51:58 CEST 2012
 */

package op.residents.bwassistant;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import entity.info.LCustodian;
import entity.info.LCustodianTools;
import op.OPDE;
import op.tools.PnlEditBetreuer;
import op.tools.SYSConst;
import op.tools.SYSTools;
import org.apache.commons.collections.Closure;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Torsten Löhr
 */
public class PnlBetreuer extends JPanel {
    public static final String internalClassID = "opde.admin.bw.wizard.page5";
    private double split1Pos;
    private Closure validate;
    private PnlEditBetreuer pnlEditBetreuer;


    public PnlBetreuer(Closure validate) {
        this.validate = validate;
        initComponents();
        initPanel();
    }

    private void initPanel() {
        EntityManager em = OPDE.createEM();
        Query query = em.createQuery("SELECT b FROM LCustodian b WHERE b.status >= 0 ORDER BY b.name, b.vorname");
        java.util.List<LCustodian> listLCustodian = query.getResultList();
        em.close();
        listLCustodian.add(0, null);

        pnlEditBetreuer = new PnlEditBetreuer(new LCustodian());
        pnlRight.add(pnlEditBetreuer, 0);

        cmbBetreuer.setModel(new DefaultComboBoxModel(listLCustodian.toArray()));
        cmbBetreuer.setRenderer(LCustodianTools.getBetreuerRenderer());

    }

    public void initSplitPanel() {
        split1Pos = SYSTools.showSide(split1, SYSTools.LEFT_UPPER_SIDE);
    }

    private void btnCancelActionPerformed(ActionEvent e) {
        split1Pos = SYSTools.showSide(split1, SYSTools.LEFT_UPPER_SIDE, SYSConst.SCROLL_TIME_FAST);
    }

    private void btnOKActionPerformed(ActionEvent e) {
        LCustodian newLCustodian = pnlEditBetreuer.getLCustodian();
        if (newLCustodian != null) {
            cmbBetreuer.setModel(new DefaultComboBoxModel(new LCustodian[]{newLCustodian}));
            validate.execute(newLCustodian);
        }
        split1Pos = SYSTools.showSide(split1, SYSTools.LEFT_UPPER_SIDE, SYSConst.SCROLL_TIME_FAST);
    }

    private void btnAddActionPerformed(ActionEvent e) {
        split1Pos = SYSTools.showSide(split1, SYSTools.RIGHT_LOWER_SIDE, SYSConst.SCROLL_TIME_FAST);
    }

    private void cmbBetreuerItemStateChanged(ItemEvent e) {
        validate.execute(cmbBetreuer.getSelectedItem());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        split1 = new JSplitPane();
        panel1 = new JPanel();
        cmbBetreuer = new JComboBox();
        btnAdd = new JButton();
        pnlRight = new JPanel();
        panel2 = new JPanel();
        btnCancel = new JButton();
        btnOK = new JButton();

        //======== this ========
        setLayout(new FormLayout(
            "default, $lcgap, default:grow, $lcgap, default",
            "default, $lgap, default:grow, $lgap, default"));

        //======== split1 ========
        {
            split1.setDividerLocation(400);
            split1.setDividerSize(1);
            split1.setDoubleBuffered(true);
            split1.setEnabled(false);

            //======== panel1 ========
            {
                panel1.setLayout(new FormLayout(
                    "default:grow, $lcgap, default",
                    "2*(default, $lgap), default"));

                //---- cmbBetreuer ----
                cmbBetreuer.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        cmbBetreuerItemStateChanged(e);
                    }
                });
                panel1.add(cmbBetreuer, CC.xy(1, 3));

                //---- btnAdd ----
                btnAdd.setText(null);
                btnAdd.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/bw/add.png")));
                btnAdd.setContentAreaFilled(false);
                btnAdd.setBorderPainted(false);
                btnAdd.setBorder(null);
                btnAdd.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnAddActionPerformed(e);
                    }
                });
                panel1.add(btnAdd, CC.xy(3, 3));
            }
            split1.setLeftComponent(panel1);

            //======== pnlRight ========
            {
                pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.PAGE_AXIS));

                //======== panel2 ========
                {
                    panel2.setLayout(new BoxLayout(panel2, BoxLayout.LINE_AXIS));

                    //---- btnCancel ----
                    btnCancel.setText(null);
                    btnCancel.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/cancel.png")));
                    btnCancel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            btnCancelActionPerformed(e);
                        }
                    });
                    panel2.add(btnCancel);

                    //---- btnOK ----
                    btnOK.setText(null);
                    btnOK.setIcon(new ImageIcon(getClass().getResource("/artwork/22x22/apply.png")));
                    btnOK.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            btnOKActionPerformed(e);
                        }
                    });
                    panel2.add(btnOK);
                }
                pnlRight.add(panel2);
            }
            split1.setRightComponent(pnlRight);
        }
        add(split1, CC.xy(3, 3, CC.DEFAULT, CC.FILL));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JSplitPane split1;
    private JPanel panel1;
    private JComboBox cmbBetreuer;
    private JButton btnAdd;
    private JPanel pnlRight;
    private JPanel panel2;
    private JButton btnCancel;
    private JButton btnOK;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
