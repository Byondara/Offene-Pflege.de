/*
 * OffenePflege.de (OPDE)
 * Copyright (C) 2011 Torsten Löhr
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
 */
package op.users;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.pane.event.CollapsiblePaneAdapter;
import com.jidesoft.pane.event.CollapsiblePaneEvent;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideButton;
import entity.system.*;
import op.OPDE;
import op.system.InternalClass;
import op.threads.DisplayManager;
import op.threads.DisplayMessage;
import op.tools.*;
import org.apache.commons.collections.Closure;
import org.jdesktop.swingx.VerticalLayout;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * @author tloehr
 */
public class PnlUser extends CleanablePanel {
    public static final String internalClassID = "opde.users";

    private final int TAB_USER = 0;
    private final int TAB_GROUPS = 1;

    private boolean initPhase = false;
    private JToggleButton tbOldUsers;//, tbShowUsers, tbShowGroups;
    private ButtonGroup bg1;
    private JScrollPane jspSearch;
    private CollapsiblePanes searchPanes;

    private ArrayList<Users> lstUsers;
    private ArrayList<Groups> lstGroups;

    private HashMap<String, JPanel> contentMap;
    private HashMap<String, CollapsiblePane> cpMap;

    private HashMap<String, Users> usermap;

//    private HashMap<Groups, Pair<ArrayList<Users>, ArrayList<InternalClass>>> groupMap2;


    private Color fg, bg;

    /**
     * Creates new form PnlUser
     */
    public PnlUser(JScrollPane jspSearch) {
        this.jspSearch = jspSearch;
        initComponents();
//        btnEnableUser.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/artwork/22x22/user_active.png")));
//        btnEnableUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/artwork/22x22/user_inactive.png")));

//        lstAllGroups.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//
//            @Override
//            public void valueChanged(ListSelectionEvent lse) {
//                if (!lse.getValueIsAdjusting()) {
//
//                    if (lstAllGroups.getSelectedIndex() >= 0) {
//                        selectedGroup = (Groups) lstAllGroups.getSelectedValue();
//                        EntityManager em1 = OPDE.createEM();
//                        em1.refresh(selectedGroup);
//                        em1.close();
//                    } else {
//                        selectedGroup = null;
//                    }
//                    setLeftSideOnGroupTab();
//                    setRightSideOnGroupTab();
//                }
//            }
//        });

        initPhase = true;
        initPanel();
        reloadDisplay();
    }

    private void initPanel() {
        fg = SYSTools.getColor(OPDE.getProps().getProperty("EARLY_FGBHP"));
        bg = SYSTools.getColor(OPDE.getProps().getProperty("EARLY_BGBHP"));
        contentMap = new HashMap<String, JPanel>();
        cpMap = new HashMap<String, CollapsiblePane>();
        usermap = new HashMap<String, Users>();
        OPDE.getDisplayManager().setMainMessage(OPDE.getAppInfo().getInternalClasses().get(internalClassID).getShortDescription());
        prepareSearchArea();
        tabMain.setTitleAt(TAB_USER, OPDE.lang.getString(internalClassID + ".tab.users"));
        tabMain.setTitleAt(TAB_GROUPS, OPDE.lang.getString(internalClassID + ".tab.groups"));
        tabMain.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                buildPanel();
            }
        });
    }


    private void reloadDisplay() {
        /***
         *               _                 _ ____  _           _
         *      _ __ ___| | ___   __ _  __| |  _ \(_)___ _ __ | | __ _ _   _
         *     | '__/ _ \ |/ _ \ / _` |/ _` | | | | / __| '_ \| |/ _` | | | |
         *     | | |  __/ | (_) | (_| | (_| | |_| | \__ \ |_) | | (_| | |_| |
         *     |_|  \___|_|\___/ \__,_|\__,_|____/|_|___/ .__/|_|\__,_|\__, |
         *                                              |_|            |___/
         */


        final boolean withworker = false;
        if (withworker) {

//            OPDE.getMainframe().setBlocked(true);
//            OPDE.getDisplayManager().setProgressBarMessage(new DisplayMessage(OPDE.lang.getString("misc.msg.wait"), -1, 100));
//
//            cpDFN.removeAll();
//
//            SwingWorker worker = new SwingWorker() {
//
//                @Override
//                protected Object doInBackground() throws Exception {
//
//                    int progress = 0;
//                    OPDE.getDisplayManager().setProgressBarMessage(new DisplayMessage(OPDE.lang.getString("misc.msg.wait"), progress, 100));
//
//                    for (DFN dfn : DFNTools.getDFNs(resident, jdcDatum.getDate())) {
//                        shiftMAPDFN.get(dfn.getShift()).add(dfn);
//                    }
//
//                    for (Byte shift : new Byte[]{DFNTools.SHIFT_ON_DEMAND, DFNTools.SHIFT_VERY_EARLY, DFNTools.SHIFT_EARLY, DFNTools.SHIFT_LATE, DFNTools.SHIFT_VERY_LATE}) {
//                        shiftMAPpane.put(shift, createCP4(shift));
//                        try {
//                            shiftMAPpane.get(shift).setCollapsed(shift == DFNTools.SHIFT_ON_DEMAND || shift != SYSCalendar.whatShiftIs(new Date()));
//                        } catch (PropertyVetoException e) {
//                            OPDE.debug(e);
//                        }
//                        progress += 20;
//                        OPDE.getDisplayManager().setProgressBarMessage(new DisplayMessage(OPDE.lang.getString("misc.msg.wait"), progress, 100));
//                    }
//
//                    return null;
//                }
//
//                @Override
//                protected void done() {
//                    buildPanel(true);
//                    initPhase = false;
//                    OPDE.getDisplayManager().setProgressBarMessage(null);
//                    OPDE.getMainframe().setBlocked(false);
//                }
//            };
//            worker.execute();

        } else {
            lstUsers = UsersTools.getUsers(true);
            lstGroups = GroupsTools.getGroups();
            for (Users user : lstUsers) {
                usermap.put(user.getUID(), user);
                createCP4(user);
            }
            for (Groups group : lstGroups) {
                createCP4(group);
            }
            buildPanel();
        }
        initPhase = false;
    }

    @Override
    public String getInternalClassID() {
        return internalClassID;
    }


    @Override
    public void cleanup() {
        lstGroups.clear();
        lstUsers.clear();
        usermap.clear();
        contentMap.clear();
        cpMap.clear();
        cpsGroups.removeAll();
        cpsUsers.removeAll();
        searchPanes.removeAll();
    }

    private void tabMainStateChanged(ChangeEvent e) {

    }

    @Override
    public void reload() {
        reloadDisplay();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the PrinterForm Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        tabMain = new JTabbedPane();
        jspUsers = new JScrollPane();
        cpsUsers = new CollapsiblePanes();
        jspGroups = new JScrollPane();
        cpsGroups = new CollapsiblePanes();

        //======== this ========
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        //======== tabMain ========
        {
            tabMain.setFont(new Font("Arial", Font.PLAIN, 18));

            //======== jspUsers ========
            {

                //======== cpsUsers ========
                {
                    cpsUsers.setLayout(new BoxLayout(cpsUsers, BoxLayout.X_AXIS));
                }
                jspUsers.setViewportView(cpsUsers);
            }
            tabMain.addTab("text", jspUsers);


            //======== jspGroups ========
            {

                //======== cpsGroups ========
                {
                    cpsGroups.setLayout(new FormLayout(
                            "default, $lcgap, default",
                            "2*(default, $lgap), default"));
                }
                jspGroups.setViewportView(cpsGroups);
            }
            tabMain.addTab("text", jspGroups);

        }
        add(tabMain);
    }// </editor-fold>//GEN-END:initComponents


    private String generatePassword(String firstname, String lastname) {
        Random generator = new Random(System.currentTimeMillis());
        return lastname.substring(0, 1).toLowerCase() + firstname.substring(0, 1).toLowerCase() + SYSTools.padL(Integer.toString(generator.nextInt(9999)), 4, "0");
//        selectedUser.setMd5pw(SYSTools.hashword(pw));
//        if (JOptionPane.showConfirmDialog(this, "Das Passwort wurde auf '" + pw + "' gesetzt.\nMöchten Sie einen Beleg ausdrucken ?", "Passwort", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//            print(pw);
//        }
    }


    private void prepareSearchArea() {
        searchPanes = new CollapsiblePanes();
        searchPanes.setLayout(new JideBoxLayout(searchPanes, JideBoxLayout.Y_AXIS));
        jspSearch.setViewportView(searchPanes);

        JPanel mypanel = new JPanel();
        mypanel.setLayout(new VerticalLayout(3));
        mypanel.setBackground(Color.WHITE);

        CollapsiblePane searchPane = new CollapsiblePane(OPDE.lang.getString(internalClassID));
        searchPane.setStyle(CollapsiblePane.PLAIN_STYLE);
        searchPane.setCollapsible(false);

        try {
            searchPane.setCollapsed(false);
        } catch (PropertyVetoException e) {
            OPDE.error(e);
        }

        GUITools.addAllComponents(mypanel, addCommands());
        GUITools.addAllComponents(mypanel, addFilters());

        searchPane.setContentPane(mypanel);

        searchPanes.add(searchPane);
        searchPanes.addExpansion();

    }

    private java.util.List<Component> addFilters() {
        java.util.List<Component> list = new ArrayList<Component>();

        /***
         *      _   _      ___  _     _ _   _
         *     | |_| |__  / _ \| | __| | | | |___  ___ _ __ ___
         *     | __| '_ \| | | | |/ _` | | | / __|/ _ \ '__/ __|
         *     | |_| |_) | |_| | | (_| | |_| \__ \  __/ |  \__ \
         *      \__|_.__/ \___/|_|\__,_|\___/|___/\___|_|  |___/
         *
         */
        tbOldUsers = GUITools.getNiceToggleButton(OPDE.lang.getString("misc.filters.showclosed"));
        tbOldUsers.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (initPhase) return;
                buildPanel();
            }
        });
        tbOldUsers.setHorizontalAlignment(SwingConstants.LEFT);
        list.add(tbOldUsers);


//        /***
//         *      _   _    ____  _                   _   _                      ______
//         *     | |_| |__/ ___|| |__   _____      _| | | |___  ___ _ __ ___   / / ___|_ __ ___  _   _ _ __  ___
//         *     | __| '_ \___ \| '_ \ / _ \ \ /\ / / | | / __|/ _ \ '__/ __| / / |  _| '__/ _ \| | | | '_ \/ __|
//         *     | |_| |_) |__) | | | | (_) \ V  V /| |_| \__ \  __/ |  \__ \/ /| |_| | | | (_) | |_| | |_) \__ \
//         *      \__|_.__/____/|_| |_|\___/ \_/\_/  \___/|___/\___|_|  |___/_/  \____|_|  \___/ \__,_| .__/|___/
//         *                                                                                          |_|
//         */
//        tbShowUsers = GUITools.getNiceToggleButton(OPDE.lang.getString(internalClassID + ".filter.showusers"));
//        tbShowUsers.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent itemEvent) {
//                if (initPhase || itemEvent.getStateChange() == ItemEvent.DESELECTED) return;
//                buildPanel();
//            }
//        });
//        tbShowUsers.setHorizontalAlignment(SwingConstants.LEFT);
//        list.add(tbShowUsers);
//
//        tbShowGroups = GUITools.getNiceToggleButton(OPDE.lang.getString(internalClassID + ".filter.showgroups"));
//        tbShowGroups.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent itemEvent) {
//                if (initPhase || itemEvent.getStateChange() == ItemEvent.DESELECTED) return;
//                buildPanel();
//            }
//        });
//        tbShowGroups.setHorizontalAlignment(SwingConstants.LEFT);
//        list.add(tbShowGroups);
//
//        bg1 = new ButtonGroup();
//        bg1.add(tbShowGroups);
//        bg1.add(tbShowUsers);
//
//        tbShowUsers.setSelected(true);


        return list;
    }

    private java.util.List<Component> addCommands() {

        java.util.List<Component> list = new ArrayList<Component>();

        /***
         *         _       _     _ _   _
         *        / \   __| | __| | | | |___  ___ _ __
         *       / _ \ / _` |/ _` | | | / __|/ _ \ '__|
         *      / ___ \ (_| | (_| | |_| \__ \  __/ |
         *     /_/   \_\__,_|\__,_|\___/|___/\___|_|
         *
         */
        final JideButton btnAddUser = GUITools.createHyperlinkButton(OPDE.lang.getString(internalClassID + ".btnAddUser"), SYSConst.icon22addUser, null);
        btnAddUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (tabMain.getSelectedIndex() != TAB_USER) {
                    tabMain.setSelectedIndex(TAB_USER);
                }
                new DlgUser(new Users(), new Closure() {
                    @Override
                    public void execute(Object o) {
                        if (o != null) {
                            EntityManager em = OPDE.createEM();
                            try {
                                em.getTransaction().begin();
                                Users user = em.merge((Users) o);
                                // Put everyone into >>everyone<<
                                Groups everyone = em.find(Groups.class, "everyone");
                                em.lock(everyone, LockModeType.OPTIMISTIC);
                                user.getGroups().add(everyone);
                                everyone.getMembers().add(user);

                                em.getTransaction().commit();
                                lstUsers.add(user);
                                reloadDisplay();
                            } catch (Exception e) {
                                em.getTransaction().rollback();
                            } finally {
                                em.close();
                            }
                        }
                    }
                });

            }
        });
        list.add(btnAddUser);

        /***
         *         _       _     _  ____
         *        / \   __| | __| |/ ___|_ __ ___  _   _ _ __
         *       / _ \ / _` |/ _` | |  _| '__/ _ \| | | | '_ \
         *      / ___ \ (_| | (_| | |_| | | | (_) | |_| | |_) |
         *     /_/   \_\__,_|\__,_|\____|_|  \___/ \__,_| .__/
         *                                              |_|
         */
        final JideButton btnAddGroup = GUITools.createHyperlinkButton(OPDE.lang.getString(internalClassID + ".btnAddGroup"), SYSConst.icon22addGroup, null);
        btnAddGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (tabMain.getSelectedIndex() != TAB_GROUPS) {
                    tabMain.setSelectedIndex(TAB_GROUPS);
                }
                new DlgGroup(new Groups(), new Closure() {
                    @Override
                    public void execute(Object o) {
                        if (o != null) {
                            EntityManager em = OPDE.createEM();
                            try {
                                em.getTransaction().begin();
                                Groups myGroup = em.merge((Groups) o);
                                em.getTransaction().commit();
                                createCP4(myGroup);
                                lstGroups.add(myGroup);
                                buildPanel();
                            } catch (Exception e) {
//                                em.getTransaction().rollback();
                                OPDE.fatal(e);
                            } finally {
                                em.close();
                            }
                        }
                    }
                });

            }
        });
        list.add(btnAddGroup);

        /***
         *      _     _         ____       _       _
         *     | |__ | |_ _ __ |  _ \ _ __(_)_ __ | |_
         *     | '_ \| __| '_ \| |_) | '__| | '_ \| __|
         *     | |_) | |_| | | |  __/| |  | | | | | |_
         *     |_.__/ \__|_| |_|_|   |_|  |_|_| |_|\__|
         *
         */
//        JideButton btnPrint = GUITools.createHyperlinkButton(OPDE.lang.getString("misc.commands.print"), SYSConst.icon22print2, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//
//            }
//        });
//        list.add(btnPrint);


        return list;
    }

    private CollapsiblePane createCP4(final Users user) {
        final String key = user.getUID() + ".xusers";
        if (!cpMap.containsKey(key)) {
            cpMap.put(key, new CollapsiblePane());
            try {
                cpMap.get(key).setCollapsed(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        final CollapsiblePane cp = cpMap.get(key);
        DefaultCPTitle cptitle = new DefaultCPTitle("<html><font size=+1>" +
                user.toString() +
                (UsersTools.isQualified(user) ?
                        ", " + OPDE.lang.getString(internalClassID + ".qualifiedNurse") : "") +
                "</font></html>", null);


        /***
         *       ____ _                            ______        __
         *      / ___| |__   __ _ _ __   __ _  ___|  _ \ \      / /
         *     | |   | '_ \ / _` | '_ \ / _` |/ _ \ |_) \ \ /\ / /
         *     | |___| | | | (_| | | | | (_| |  __/  __/ \ V  V /
         *      \____|_| |_|\__,_|_| |_|\__, |\___|_|     \_/\_/
         *                              |___/
         */
        final JButton btnChangePW = new JButton(SYSConst.icon22password);
        btnChangePW.setPressedIcon(SYSConst.icon22passwordPressed);
        btnChangePW.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnChangePW.setContentAreaFilled(false);
        btnChangePW.setBorder(null);
        btnChangePW.setToolTipText(OPDE.lang.getString(internalClassID + ".btnChangePW.tooltip"));
        btnChangePW.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                EntityManager em = OPDE.createEM();
                try {
                    em.getTransaction().begin();
                    Users myUser = em.merge(usermap.get(user.getUID()));
                    String newpw = SYSTools.generatePassword(myUser.getVorname(), myUser.getName());
                    em.lock(myUser, LockModeType.OPTIMISTIC);
                    myUser.setMd5pw(SYSTools.hashword(newpw));
                    em.getTransaction().commit();

                    lstUsers.remove(user);
                    lstUsers.add(myUser);
                    usermap.put(key, myUser);
                    Collections.sort(lstUsers);

                    SYSTools.printpw(newpw, myUser);

                    OPDE.getDisplayManager().addSubMessage(new DisplayMessage(OPDE.lang.getString(internalClassID + ".pwchanged")));
                } catch (OptimisticLockException ole) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }

                    OPDE.getDisplayManager().addSubMessage(DisplayManager.getLockMessage());
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    OPDE.fatal(e);
                } finally {
                    em.close();
                }

            }
        });
        btnChangePW.setEnabled(user.isActive());
        cptitle.getRight().add(btnChangePW);

        /***
         *      _     _            _        _   _           ___                  _   _
         *     | |__ | |_ _ __    / \   ___| |_(_)_   _____|_ _|_ __   __ _  ___| |_(_)_   _____
         *     | '_ \| __| '_ \  / _ \ / __| __| \ \ / / _ \| || '_ \ / _` |/ __| __| \ \ / / _ \
         *     | |_) | |_| | | |/ ___ \ (__| |_| |\ V /  __/| || | | | (_| | (__| |_| |\ V /  __/
         *     |_.__/ \__|_| |_/_/   \_\___|\__|_| \_/ \___|___|_| |_|\__,_|\___|\__|_| \_/ \___|
         *
         */
        final JButton btnActiveInactive = new JButton(user.isActive() ? SYSConst.icon22stop : SYSConst.icon22playerPlay);
        btnActiveInactive.setPressedIcon(user.isActive() ? SYSConst.icon22stopPressed : SYSConst.icon22playerPlayPressed);
        btnActiveInactive.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnActiveInactive.setContentAreaFilled(false);
        btnActiveInactive.setBorder(null);
        btnActiveInactive.setToolTipText(OPDE.lang.getString(internalClassID + (user.isActive() ? ".btnActiveInactive.stop" : ".btnActiveInactive.play")));
        btnActiveInactive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                EntityManager em = OPDE.createEM();
                try {
                    em.getTransaction().begin();
                    Users myUser = em.merge(usermap.get(user.getUID()));
                    em.lock(myUser, LockModeType.OPTIMISTIC);

                    myUser.setStatus(myUser.isActive() ? UsersTools.STATUS_INACTIVE : UsersTools.STATUS_ACTIVE);
                    em.getTransaction().commit();
                    lstUsers.remove(user);
                    lstUsers.add(myUser);
                    usermap.put(myUser.getUID(), myUser);
                    Collections.sort(lstUsers);
                    CollapsiblePane cp = createCP4(myUser);
                    boolean wasCollapsed = cpMap.get(key).isCollapsed();
                    cpMap.put(key, cp);

                    cp.setCollapsed(myUser.isActive() ? wasCollapsed : true);
                    buildPanel();
                } catch (OptimisticLockException ole) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    if (ole.getMessage().indexOf("Class> entity.info.Bewohner") > -1) {
                        OPDE.getMainframe().emptyFrame();
                        OPDE.getMainframe().afterLogin();
                    }
                    OPDE.getDisplayManager().addSubMessage(DisplayManager.getLockMessage());
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    OPDE.fatal(e);
                } finally {
                    em.close();
                }
            }


        });
        cptitle.getRight().add(btnActiveInactive);

        /***
         *               _ _ _
         *       ___  __| (_) |_
         *      / _ \/ _` | | __|
         *     |  __/ (_| | | |_
         *      \___|\__,_|_|\__|
         *
         */
        final JButton btnEdit = new JButton(SYSConst.icon22edit3);
        btnEdit.setPressedIcon(SYSConst.icon22edit3Pressed);
        btnEdit.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnEdit.setContentAreaFilled(false);
        btnEdit.setBorder(null);
        btnEdit.setToolTipText(OPDE.lang.getString(internalClassID + ".btnEdit"));
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                new DlgUser(user, new Closure() {
                    @Override
                    public void execute(Object o) {
                        if (o != null) {
                            EntityManager em = OPDE.createEM();
                            try {
                                em.getTransaction().begin();
                                Users myUser = em.merge((Users) o);
                                em.lock(myUser, LockModeType.OPTIMISTIC);
                                em.getTransaction().commit();
                                lstUsers.remove(user);
                                lstUsers.add(myUser);
                                usermap.put(myUser.getUID(), myUser);
                                Collections.sort(lstUsers);
                                CollapsiblePane cp = createCP4(myUser);
                                boolean wasCollapsed = cpMap.get(key).isCollapsed();
                                cpMap.put(key, cp);

                                cp.setCollapsed(myUser.isActive() ? wasCollapsed : true);
                                buildPanel();
                            } catch (OptimisticLockException ole) {
                                if (em.getTransaction().isActive()) {
                                    em.getTransaction().rollback();
                                }
                                if (ole.getMessage().indexOf("Class> entity.info.Bewohner") > -1) {
                                    OPDE.getMainframe().emptyFrame();
                                    OPDE.getMainframe().afterLogin();
                                }
                                OPDE.getDisplayManager().addSubMessage(DisplayManager.getLockMessage());
                            } catch (Exception e) {
                                if (em.getTransaction().isActive()) {
                                    em.getTransaction().rollback();
                                }
                                OPDE.fatal(e);
                            } finally {
                                em.close();
                            }
                        }
                    }
                });
            }


        });
        cptitle.getRight().add(btnEdit);

        cp.setTitleLabelComponent(cptitle.getMain());
        cp.setSlidingDirection(SwingConstants.SOUTH);


        /***
         *       ___ ___  _  _ _____ ___ _  _ _____
         *      / __/ _ \| \| |_   _| __| \| |_   _|
         *     | (_| (_) | .` | | | | _|| .` | | |
         *      \___\___/|_|\_| |_| |___|_|\_| |_|
         *
         */

        cp.addCollapsiblePaneListener(new CollapsiblePaneAdapter() {
            @Override
            public void paneExpanded(CollapsiblePaneEvent collapsiblePaneEvent) {
                cp.setContentPane(new PnlEditMemberships(user, lstGroups));
                cp.setOpaque(false);
            }
        }

        );
        cp.setBackground(UsersTools.getBG1(user));
        cp.setCollapsible(user.isActive());

        cp.setHorizontalAlignment(SwingConstants.LEADING);
        cp.setOpaque(false);

        return cp;
    }

    private CollapsiblePane createCP4(final Groups group) {
        final String key = group.getGID() + ".xgroups";
        if (!cpMap.containsKey(key)) {
            cpMap.put(key, new CollapsiblePane());
            cpMap.get(key).setSlidingDirection(SwingConstants.SOUTH);

            cpMap.get(key).setBackground(bg);
            cpMap.get(key).setForeground(fg);

            cpMap.get(key).addCollapsiblePaneListener(new CollapsiblePaneAdapter() {
                @Override
                public void paneExpanded(CollapsiblePaneEvent collapsiblePaneEvent) {
                    if (!contentMap.containsKey(key)) {
                        contentMap.put(key, createContentPanel4(group));
                    }
                    cpMap.get(key).setContentPane(contentMap.get(key));
                }
            });

            cpMap.get(key).setHorizontalAlignment(SwingConstants.LEADING);
            cpMap.get(key).setOpaque(false);
            try {
                cpMap.get(key).setCollapsed(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        final CollapsiblePane cp = cpMap.get(key);

        /***
         *      _   _ _____    _    ____  _____ ____
         *     | | | | ____|  / \  |  _ \| ____|  _ \
         *     | |_| |  _|   / _ \ | | | |  _| | |_) |
         *     |  _  | |___ / ___ \| |_| | |___|  _ <
         *     |_| |_|_____/_/   \_\____/|_____|_| \_\
         *
         */

        JPanel titlePanelleft = new JPanel();
        titlePanelleft.setLayout(new BoxLayout(titlePanelleft, BoxLayout.LINE_AXIS));


        /***
         *      _     _       _    _           _   _                _   _                _
         *     | |   (_)_ __ | | _| |__  _   _| |_| |_ ___  _ __   | | | | ___  __ _  __| | ___ _ __
         *     | |   | | '_ \| |/ / '_ \| | | | __| __/ _ \| '_ \  | |_| |/ _ \/ _` |/ _` |/ _ \ '__|
         *     | |___| | | | |   <| |_) | |_| | |_| || (_) | | | | |  _  |  __/ (_| | (_| |  __/ |
         *     |_____|_|_| |_|_|\_\_.__/ \__,_|\__|\__\___/|_| |_| |_| |_|\___|\__,_|\__,_|\___|_|
         *
         */
        JideButton btnGroup = GUITools.createHyperlinkButton("<html><font size=+1>" +
                group.toString() +
                (group.isQualified() ?
                        ", " + OPDE.lang.getString(internalClassID + ".qualifiedGroup") : "") +
                "</font></html>", null, null);

        btnGroup.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    cp.setCollapsed(!cp.isCollapsed());
                } catch (PropertyVetoException e) {
                    OPDE.error(e);
                }
            }
        });
        titlePanelleft.add(btnGroup);


        JPanel titlePanelright = new JPanel();
        titlePanelright.setLayout(new BoxLayout(titlePanelright, BoxLayout.LINE_AXIS));

        titlePanelleft.setOpaque(false);
        titlePanelright.setOpaque(false);
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);

        titlePanel.setLayout(new GridBagLayout());
        ((GridBagLayout) titlePanel.getLayout()).columnWidths = new int[]{0, 80};
        ((GridBagLayout) titlePanel.getLayout()).columnWeights = new double[]{1.0, 1.0};

        titlePanel.add(titlePanelleft, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));

        titlePanel.add(titlePanelright, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 0), 0, 0));

        cp.setTitleLabelComponent(titlePanel);

        if (!cp.isCollapsed()) {
            if (!contentMap.containsKey(key)) {
                contentMap.put(key, createContentPanel4(group));
            }
            cp.setContentPane(contentMap.get(key));
        }

        return cp;
    }

    private JPanel createContentPanel4(final Groups group) {
        JPanel contentPanel = new JPanel(new VerticalLayout());
        if (!group.isEveryone()) { // everyone does not need a membership.
            contentPanel.add(createMemberPanel4(group));
        }

        if (!group.isAdmin()) { // admin does not need further acls. he is in godmode anyways
            contentPanel.add(createClassesPanel4(group));
        }
        return contentPanel;
    }

    private JPanel createMemberPanel4(final Groups group) {

        CollapsiblePane cpMember = new CollapsiblePane(OPDE.lang.getString(internalClassID + ".members"));
        cpMember.setBackground(bg.darker()); // a little darker
        cpMember.setForeground(Color.WHITE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new VerticalLayout());
        contentPanel.setOpaque(false);

        cpMember.setContentPane(new PnlEditMembers(group, lstUsers));

        contentPanel.add(cpMember);
        cpMember.setFont(SYSConst.ARIAL14BOLD);


        return contentPanel;
    }

    private JPanel createClassesPanel4(final Groups group) {

        HashMap<String, SYSGROUPS2ACL> lookup = SYSGROUPS2ACLTools.getIntClassesMap(group);
        CollapsiblePane cpClasses = new CollapsiblePane(OPDE.lang.getString(internalClassID + ".modules"));
        try {
            cpClasses.setCollapsed(true);
        } catch (PropertyVetoException e) {
        }

        JPanel contentClasses = new JPanel();
        contentClasses.setLayout(new VerticalLayout());

        ArrayList<InternalClass> listClasses = new ArrayList<InternalClass>(OPDE.getAppInfo().getInternalClasses().values());
        Collections.sort(listClasses);
        for (final InternalClass ic : listClasses) {
            CollapsiblePane cpClass = new CollapsiblePane(ic.getShortDescription());
            cpClass.setToolTipText(ic.getLongDescription());
            SYSGROUPS2ACL mySYSGROUPS2ACL = lookup.containsKey(ic.getInternalClassID()) ? lookup.get(ic.getInternalClassID()) : new SYSGROUPS2ACL(ic.getInternalClassID(), group);
            cpClass.setContentPane(new PnlEditACL(mySYSGROUPS2ACL));
            cpClass.setStyle(CollapsiblePane.TREE_STYLE);
            try {
                cpClass.setCollapsed(true);
            } catch (PropertyVetoException e) {
            }
            contentClasses.add(cpClass);
        }

        cpClasses.setContentPane(contentClasses);
        cpClasses.setFont(SYSConst.ARIAL14BOLD);

        return cpClasses;
    }

    private void buildPanel() {

        if (tabMain.getSelectedIndex() == TAB_USER) {
            cpsUsers.removeAll();
            cpsUsers.setLayout(new JideBoxLayout(cpsUsers, JideBoxLayout.Y_AXIS));
            Collections.sort(lstUsers);
            for (Users user : lstUsers) {
                if (tbOldUsers.isSelected() || user.isActive()) {
                    cpsUsers.add(cpMap.get(user.getUID() + ".xusers"));
                }
            }
            cpsUsers.addExpansion();
        } else {
            cpsGroups.removeAll();
            cpsGroups.setLayout(new JideBoxLayout(cpsGroups, JideBoxLayout.Y_AXIS));
            Collections.sort(lstGroups);
            for (Groups group : lstGroups) {
                cpsGroups.add(cpMap.get(group.getGID() + ".xgroups"));
            }
            cpsGroups.addExpansion();
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTabbedPane tabMain;
    private JScrollPane jspUsers;
    private CollapsiblePanes cpsUsers;
    private JScrollPane jspGroups;
    private CollapsiblePanes cpsGroups;
    // End of variables declaration//GEN-END:variables
}
