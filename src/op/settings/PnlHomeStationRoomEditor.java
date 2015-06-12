/*
 * Created by JFormDesigner on Thu Apr 30 14:43:18 CEST 2015
 */

package op.settings;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideButton;
import entity.EntityTools;
import entity.building.*;
import gui.PnlBeanEditor;
import gui.events.ContentRequestedEventListener;
import gui.events.DataChangeEvent;
import gui.events.DataChangeListener;
import gui.events.JPADataChangeListener;
import gui.interfaces.CleanablePanel;
import gui.interfaces.DefaultCPTitle;
import gui.interfaces.DefaultCollapsiblePane;
import gui.interfaces.DefaultCollapsiblePanes;
import op.OPDE;
import op.tools.GUITools;
import op.tools.SYSConst;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @author Torsten Löhr
 */
public class PnlHomeStationRoomEditor extends CleanablePanel implements Runnable {

    //    private final DefaultMutableTreeNode root;
    //    private String keyToRefresh;
    private Object objectToRefresh;

    private boolean refresh = false;
    private final Thread thread;

    //    private HashMap<String, CollapsiblePanes> parentCPS;
//    private HashMap<String, DefaultCollapsiblePane> indexView;
    //    private HashMap<String, DefaultMutableTreeNode> indexMoadel;
    private Logger logger = Logger.getLogger(this.getClass());
    private JScrollPane scrollPane1;
    private DefaultCollapsiblePanes cpsHomes;

    public PnlHomeStationRoomEditor() {
        cpsHomes = new DefaultCollapsiblePanes();
        scrollPane1 = new JScrollPane();
        setLayout(new FormLayout(
                "default, $lcgap, default:grow, $lcgap, default",
                "default, $lgap, default:grow, $lgap, default"));

        scrollPane1.setViewportView(cpsHomes);
        add(scrollPane1, CC.xy(3, 3, CC.FILL, CC.FILL));

//        indexView = new HashMap<>();
//        parentCPS = new HashMap<>();
        objectToRefresh = null;

        logger.setLevel(Level.DEBUG);

        thread = new Thread(this);
        thread.start();
        reload();
    }


    @Override
    public void reload() {
//        initDataModel(null);
        refresh = true;
    }


    @Override
    public void cleanup() {
        thread.interrupt();
//        indexView.clear();
        cpsHomes.removeAll();
    }


    @Override
    public String getInternalClassID() {
        return null;
    }

    @Override
    public void run() {
        while (!thread.isInterrupted()) {
            try {
                if (refresh) {
                    refresh = false;
                    refreshDisplay();
                }

                Thread.sleep(OPDE.DEFAULT_SCREEN_RESFRESH_MILLIS);
            } catch (InterruptedException e) {
                logger.debug("InterruptedException");
                return;
            }
        }
    }

    synchronized void refreshDisplay() {

        if (objectToRefresh == null) {

            cpsHomes.removeAll();
            cpsHomes.setLayout(new JideBoxLayout(cpsHomes, JideBoxLayout.Y_AXIS));

            for (final Homes home : HomesTools.getAll()) {
                cpsHomes.add(createCP(home));
            }
            cpsHomes.addExpansion();
        }


//        if (objectToRefresh == null) {
//            indexView.clear();
//            cpsHomes.removeAll();
//            cpsHomes.setLayout(new JideBoxLayout(cpsHomes, JideBoxLayout.Y_AXIS));
//            cpsHomes.add(createAddHomeButton());
//
//            ArrayList<Homes> listHomes = HomesTools.getAll();
//            for (final Homes home : listHomes) {
//                parentCPS.put(getKey(home), cpsHomes);
//                cpsHomes.add(createCP(home));
//            }
//
//            cpsHomes.addExpansion();
//        } else {
//            if (indexView.containsKey(getKey(objectToRefresh))) {
//                final String key = getKey(objectToRefresh);
//
//                SwingUtilities.invokeLater(() -> {
//                    indexView.get(key).reload();
//                    indexView.get(key).revalidate();
//                    indexView.get(key).repaint();
//                });
//            } else {
//
//                if (objectToRefresh instanceof Homes) {
//                    cpsHomes.remove(cpsHomes.getComponentCount() - 1); // remove old Expansion
//                    cpsHomes.add(createCP(objectToRefresh));
//                    cpsHomes.addExpansion();
//
//                    parentCPS.put(getKey(objectToRefresh), cpsHomes);
//
//                    SwingUtilities.invokeLater(() -> {
//                        cpsHomes.revalidate();
//                        cpsHomes.repaint();
//                    });
//
//                } else if (objectToRefresh instanceof Floors) {
//
//                    CollapsiblePanes cps = parentCPS.get("fl#" + getKey(((Floors) objectToRefresh).getHome()));
//
//                    cps.remove(cpsHomes.getComponentCount() - 1); // remove old Expansion
//                    cps.add(createCP(objectToRefresh));
//                    cps.addExpansion();
//
//                    indexView.get(getKey(objectToRefresh)).reload();
//
//                    SwingUtilities.invokeLater(() -> {
//                        cps.revalidate();
//                        cps.repaint();
//                    });
//                }
//            }
//            objectToRefresh = null;
//        }
    }

    private DefaultCollapsiblePane createCP(final Homes home) {

        ContentRequestedEventListener<DefaultCollapsiblePane> headerUpdate = cre -> {
            DefaultCollapsiblePane dcp = (DefaultCollapsiblePane) cre.getSource();
            Homes myHome = EntityTools.find(Homes.class, home.getEID());
            dcp.setTitleButtonText(myHome.getName());
            dcp.getTitleButton().setFont(SYSConst.ARIAL24);
        };

        ContentRequestedEventListener<DefaultCollapsiblePane> contentUpdate = cre -> {
            DefaultCollapsiblePane dcp = (DefaultCollapsiblePane) cre.getSource();
            Homes myHome = EntityTools.find(Homes.class, home.getEID());
            dcp.setContentPane(createContent(myHome, (DefaultCollapsiblePane<Homes>) cre.getSource()));
        };

        DefaultCollapsiblePane<Homes> cp = new DefaultCollapsiblePane(headerUpdate, contentUpdate);
        return cp;
    }

    private DefaultCollapsiblePanes createContent(final Homes home, DataChangeListener<Homes> dcl) {

        DefaultCollapsiblePanes dcps = new DefaultCollapsiblePanes();

        try {
            PnlBeanEditor<Homes> pnlBeanEditor = new PnlBeanEditor<>(() -> EntityTools.find(Homes.class, home.getEID()), Homes.class, PnlBeanEditor.SAVE_MODE_IMMEDIATE);
            pnlBeanEditor.addDataChangeListener(new JPADataChangeListener<>(editedHome -> {
                pnlBeanEditor.reload(editedHome);
                dcl.dataChanged(new DataChangeEvent<>(pnlBeanEditor, editedHome));
            }));
            dcps.add(pnlBeanEditor);
//                                    .add(createCPFloorsFor((Homes) userObject));
            dcps.addExpansion();
        } catch (Exception e) {
            OPDE.fatal(logger, e);
        }


        return dcps;
    }


//    private DefaultCollapsiblePane createCP(Object userObject) {
//        if (userObject == null) return null;
//        String key = getKey(userObject);
//
//        logger.debug("neuer CP erstellt: " + userObject);
//
//        ContentRequestedEventListener<DefaultCollapsiblePane> contentRequestedEventListener = null;
//
//        if (userObject instanceof Homes) {
//            contentRequestedEventListener = cre -> {
//                DefaultCollapsiblePane dcp = (DefaultCollapsiblePane) cre.getSource();
//                Homes home = EntityTools.find(Homes.class, ((Homes) userObject).getEID());
//                dcp.setTitleButtonText(home.getName());
//
//                if (!indexView.containsKey(key)) {
//                    dcp.getTitleButton().setFont(SYSConst.ARIAL24);
//                    dcp.setContentPane(createContent(home));
//                }
//
//            };
//
//        } else if (userObject instanceof Floors) {
//
//            contentRequestedEventListener = cre -> {
//                DefaultCollapsiblePane dcp = (DefaultCollapsiblePane) cre.getSource();
//                Floors floor = EntityTools.find(Floors.class, ((Floors) userObject).getFloorid());
//                dcp.setTitleButtonText(floor.getName());
//                if (!indexView.containsKey(key)) {
//                    dcp.setContentPane(createContent(userObject));
//                }
//            };
//
//        } else if (userObject instanceof Rooms) {
//            contentRequestedEventListener = cre -> {
//                DefaultCollapsiblePane dcp = (DefaultCollapsiblePane) cre.getSource();
//                dcp.setTitleButtonText(((Rooms) userObject).getText());
//                if (!indexView.containsKey(key)) {
//                    dcp.setContentPane(createContent(userObject));
//                }
//            };
//        }
////        else if (userObject.equals(Station.class)) {
////
////            contentRequestedEventListener = new ContentRequestedEventListener<DefaultCollapsiblePane>() {
////                @Override
////                public void contentRequested(ContentRequestedEvent<DefaultCollapsiblePane> cre) {
////                    DefaultCollapsiblePane dcp = (DefaultCollapsiblePane) cre.getSource();
////                    dcp.setTitleButtonText("misc.msg.station");
////                    dcp.getTitleButton().setFont(SYSConst.ARIAL20);
////                    dcp.setContentPane(createContent(userObject));
////                }
////            };
////
////
////        }
//
//
//        DefaultCollapsiblePane cp = new DefaultCollapsiblePane(contentRequestedEventListener);
//
////        cp.setBackground(Color.white);
//
//
//        indexView.put(getKey(userObject), cp);
//
//
//        return cp;
//    }
//
//
//    private DefaultCollapsiblePane createCPFloorsFor(Homes home) {
//        ContentRequestedEventListener<DefaultCollapsiblePane> contentRequestedEventListener = new ContentRequestedEventListener<DefaultCollapsiblePane>() {
//            @Override
//            public void contentRequested(ContentRequestedEvent<DefaultCollapsiblePane> cre) {
//                DefaultCollapsiblePane dcp = (DefaultCollapsiblePane) cre.getSource();
//                dcp.setTitleButtonText("misc.msg.floor");
//                dcp.getTitleButton().setFont(SYSConst.ARIAL20);
//
//                CollapsiblePanes cps = new CollapsiblePanes();
//                cps.setLayout(new JideBoxLayout(cps, JideBoxLayout.Y_AXIS));
//
//                for (final Floors floor : home.getFloors()) {
//                    cps.add(createCP(floor));
//                    parentCPS.put(getKey(floor), cps);
//                    parentCPS.put("fl#" + getKey(home), cps);
//                }
//                cps.addExpansion();
//                dcp.setContentPane(cps);
//            }
//        };
//        DefaultCollapsiblePane cp = new DefaultCollapsiblePane(contentRequestedEventListener);
//        indexView.put(getKey(home), cp);
//
//        return cp;
//    }

//    private JPanel createContent(Object userObject) {
//
//        CollapsiblePanes cps = new CollapsiblePanes();
//        cps.setLayout(new JideBoxLayout(cps, JideBoxLayout.Y_AXIS));
//
//        Object id;
//
//        if (userObject instanceof Homes) {
//
//            try {
//                PnlBeanEditor<Homes> pnlBeanEditor = new PnlBeanEditor<>(new JPADataChangeListener<>(o -> {
//                    objectToRefresh = o;
//                    refresh = true;
//                }), () -> EntityTools.find(Homes.class, ((Homes) userObject).getEID()), Homes.class, PnlBeanEditor.SAVE_MODE_IMMEDIATE);
//
//                cps.add(pnlBeanEditor);
//                cps.add(createCPFloorsFor((Homes) userObject));
//
//
//            } catch (Exception e) {
//                OPDE.fatal(logger, e);
//            }
//
////            for (final Floors floor : ((Homes) userObject).getFloors()) {
////                createCP(floor);
////            }
//
////            for (final Station station : ((Homes) userObject).getStations()) {
////
////
////            }
//
//
//        } else if (userObject instanceof Rooms) {
////            cps.add(new PnlRooms(new DataChangeListener<Rooms>() {
////                @Override
////                public void dataChanged(DataChangeEvent evt) {
////                    EntityManager em = OPDE.createEM();
////                    try {
////                        em.getTransaction().begin();
////                        Rooms myRoom = em.merge((Rooms) evt.getData());
////                        em.getTransaction().commit();
////
////
////                    } catch (OptimisticLockException ole) {
////                        if (em.getTransaction().isActive()) {
////                            em.getTransaction().rollback();
////                        }
////                        OPDE.warn(logger, ole);
////                        OPDE.getDisplayManager().addSubMessage(DisplayManager.getLockMessage(ole));
////                    } catch (Exception e) {
////                        em.getTransaction().rollback();
////                        OPDE.fatal(logger, e);
////                    } finally {
////                        em.close();
////                        ((PnlRooms) evt.getSource()).cleanup(); // this is a one time use panel. will be replaced with the next refresh
////                        refresh = true;
////                    }
////                }
////            }, () -> EntityTools.find(Rooms.class, ((Rooms) userObject).getRoomID())));
//        } else if (userObject instanceof Floors) {
//            try {
//                PnlBeanEditor<Floors> pbe = new PnlBeanEditor<>(new JPADataChangeListener<>(o -> {
//                    objectToRefresh = o;
//                    refresh = true;
//                }), () -> EntityTools.find(Floors.class, ((Floors) userObject).getFloorid()), Floors.class, PnlBeanEditor.SAVE_MODE_IMMEDIATE);
//                cps.add(pbe);
//
//            } catch (Exception e) {
//                OPDE.fatal(logger, e);
//            }
//        }
//
//
//        cps.addExpansion();
//
//        parentCPS.put(getKey(userObject), cps);
//
//
//        return cps;
//    }


//    private JPanel createContent4Floors(DefaultMutableTreeNode node) {
////        JPanel pnl = new JPanel();
////        pnl.setLayout(new BoxLayout(pnl, BoxLayout.PAGE_AXIS));
//////        pnl.add(new JPanel().add(createAddRoomButton()));
//
//
//        CollapsiblePanes cps = new CollapsiblePanes();
//        Enumeration en = node.children();
//
//        while (en.hasMoreElements()) {
//            DefaultMutableTreeNode child = (DefaultMutableTreeNode) en.nextElement();
//            cps.add(createCP(child));
//        }
//
//        return cps;
//
//    }


//    private JPanel createContent4Floor(DefaultMutableTreeNode node) {
//
//            CollapsiblePanes cps = new CollapsiblePanes();
//            Enumeration en = node.children();
//
//            while (en.hasMoreElements()) {
//                DefaultMutableTreeNode child = (DefaultMutableTreeNode) en.nextElement();
//                cps.add(createCP(child));
//            }
//
//            return cps;
//        }


//    private JPanel createContent4Rooms(DefaultMutableTreeNode node) {
//        JPanel pnl = new JPanel();
//        pnl.setLayout(new BoxLayout(pnl, BoxLayout.PAGE_AXIS));
////        pnl.add(new JPanel().add(createAddRoomButton()));
//
//        Enumeration en = node.children();
//
//        while (en.hasMoreElements()) {
//            DefaultMutableTreeNode child = (DefaultMutableTreeNode) en.nextElement();
//            Rooms room = (Rooms) child.getUserObject();
//            DefaultCPTitle cptitle = new DefaultCPTitle(room.toString(), null);
//            pnl.add(cptitle.getMain());
//        }
//
//        return pnl;
//    }

    private JPanel createContent4Stations(DefaultMutableTreeNode node) {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.PAGE_AXIS));
        pnl.add(createAddStationButton());

        Enumeration en = node.children();

        while (en.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) en.nextElement();
            Station station = (Station) child.getUserObject();
            DefaultCPTitle cptitle = new DefaultCPTitle(station.getName(), null);
            pnl.add(cptitle.getMain());
        }

        return pnl;
    }
//
//
//    private DefaultMutableTreeNode find(Object userObject) {
//        DefaultMutableTreeNode found = null;
//
//        Enumeration en = root.breadthFirstEnumeration();
//        while (en.hasMoreElements()) {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
//            if (node.getUserObject() != null && node.getUserObject().equals(userObject)) {
//                found = node;
//                break;
//            }
//        }
//        return found;
//    }


    private void create(Homes home) {

//            pnlContentH.add(cpsInsideHome);
//
//            CollapsiblePane cpRooms = new CollapsiblePane(SYSTools.xx("misc.msg.room"));
//            cpsInsideHome.add(cpRooms);


//        DefaultCPTitle cptitle = new DefaultCPTitle(home.getName(), null);
//        CollapsiblePane cpHome = new CollapsiblePane();
//        cpHome.setCollapsible(false);
//        cpHome.setTitleLabelComponent(cptitle.getMain());
//        cpHome.setSlidingDirection(SwingConstants.SOUTH);


//        final JideButton btnAddRoom = GUITools.createHyperlinkButton("opde.settings.btnAddRoom", SYSConst.icon22add, null);
//        btnAddRoom.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JidePopup popup = GUITools.getTextEditor(null, 1, 40, new Closure() {
//                    @Override
//                    public void execute(Object o) {
//                        //                            if (o != null && !o.toString().trim().isEmpty()) {
//                        //                                EntityManager em = OPDE.createEM();
//                        //                                try {
//                        //                                    em.getTransaction().begin();
//                        //                                    em.merge(new Station(o.toString(), em.merge(home)));
//                        //                                    em.getTransaction().commit();
//                        //                                    createHomesList();
//                        //                                    OPDE.getMainframe().emptySearchArea();
//                        //                                    OPDE.getMainframe().prepareSearchArea();
//                        //                                } catch (Exception e) {
//                        //                                    em.getTransaction().rollback();
//                        //                                    OPDE.fatal(e);
//                        //                                } finally {
//                        //                                    em.close();
//                        //                                }
//                        //                            }
//                    }
//                }, btnAddRoom);
//                GUITools.showPopup(popup, SwingConstants.EAST);
//            }
//        });
//        subtree.add(new DefaultMutableTreeNode(btnAddRoom));


//                String titleR = "<html><font size=+1>" + room.toString() + "</font></html>";
//                DefaultCPTitle cpTitleR = new DefaultCPTitle(titleR, null);
//
//                //                            final JButton btnEditStation = new JButton(SYSConst.icon22edit);
//                //                            btnEditStation.setPressedIcon(SYSConst.icon22Pressed);
//                //                            btnEditStation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//                //                            btnEditStation.setContentAreaFilled(false);
//                //                            btnEditStation.setBorder(null);
//                //
//                //                            btnEditStation.addActionListener(new ActionListener() {
//                //                                @Override
//                //                                public void actionPerformed(ActionEvent e) {
//                //
//                //                                    final JidePopup popup = GUITools.getTextEditor(station.getName(), 1, 40, new Closure() {
//                //                                        @Override
//                //                                        public void execute(Object o) {
//                //                                            if (o != null && !o.toString().trim().isEmpty()) {
//                //                                                EntityManager em = OPDE.createEM();
//                //                                                try {
//                //                                                    em.getTransaction().begin();
//                //                                                    Station myStation = em.merge(station);
//                //                                                    myStation.setName(o.toString().trim());
//                //                                                    em.getTransaction().commit();
//                //                                                    createHomesList();
//                //                                                    OPDE.getMainframe().emptySearchArea();
//                //                                                    OPDE.getMainframe().prepareSearchArea();
//                //                                                } catch (Exception e) {
//                //                                                    em.getTransaction().rollback();
//                //                                                    OPDE.fatal(e);
//                //                                                } finally {
//                //                                                    em.close();
//                //                                                }
//                //                                            }
//                //                                        }
//                //                                    }, btnEditStation);
//                //                                    GUITools.showPopup(popup, SwingConstants.EAST);
//                //                                }
//                //                            });
//                //
//                //                            cpTitleS.getRight().add(btnEditStation);
//
//
//                //                            if (station.getResidents().isEmpty()) {
//                //                                /***
//                //                                 *          _      _      _             _        _   _
//                //                                 *       __| | ___| | ___| |_ ___   ___| |_ __ _| |_(_) ___  _ __
//                //                                 *      / _` |/ _ \ |/ _ \ __/ _ \ / __| __/ _` | __| |/ _ \| '_ \
//                //                                 *     | (_| |  __/ |  __/ ||  __/ \__ \ || (_| | |_| | (_) | | | |
//                //                                 *      \__,_|\___|_|\___|\__\___| |___/\__\__,_|\__|_|\___/|_| |_|
//                //                                 *
//                //                                 */
//                //                                final JButton btnDeleteStation = new JButton(SYSConst.icon22delete);
//                //                                btnDeleteStation.setPressedIcon(SYSConst.icon22Pressed);
//                //                                btnDeleteStation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//                //                                btnDeleteStation.setContentAreaFilled(false);
//                //                                btnDeleteStation.setBorder(null);
//                //
//                //                                btnDeleteStation.addActionListener(new ActionListener() {
//                //                                    @Override
//                //                                    public void actionPerformed(ActionEvent e) {
//                //                                        new DlgYesNo(SYSTools.xx("misc.questions.delete1") + "<br/><i>" + station.getName() + "</i><br/>" + SYSTools.xx("misc.questions.delete2"), SYSConst.icon48delete, new Closure() {
//                //                                            @Override
//                //                                            public void execute(Object answer) {
//                //                                                if (answer.equals(JOptionPane.YES_OPTION)) {
//                //                                                    EntityManager em = OPDE.createEM();
//                //                                                    try {
//                //                                                        em.getTransaction().begin();
//                //                                                        Station myStation = em.merge(station);
//                //                                                        em.lock(myStation, LockModeType.OPTIMISTIC);
//                //                                                        em.remove(myStation);
//                //                                                        em.getTransaction().commit();
//                //                                                        createHomesList();
//                //                                                        OPDE.getMainframe().emptySearchArea();
//                //                                                        OPDE.getMainframe().prepareSearchArea();
//                //                                                    } catch (RollbackException ole) {
//                //                                                        if (em.getTransaction().isActive()) {
//                //                                                            em.getTransaction().rollback();
//                //                                                        }
//                //                                                        if (ole.getMessage().indexOf("Class> entity.info.Resident") > -1) {
//                //                                                            OPDE.getMainframe().completeRefresh();
//                //                                                        }
//                //                                                        OPDE.getDisplayManager().addSubMessage(DisplayManager.getLockMessage());
//                //                                                    } catch (Exception e) {
//                //                                                        if (em.getTransaction().isActive()) {
//                //                                                            em.getTransaction().rollback();
//                //                                                        }
//                //                                                        OPDE.fatal(e);
//                //                                                    } finally {
//                //                                                        em.close();
//                //                                                    }
//                //                                                }
//                //                                            }
//                //                                        });
//                //                                    }
//                //                                });
//                //                                cpTitleS.getRight().add(btnDeleteStation);
//
//
//                pnlContentH.add(cpTitleR.getMain());
//
//            }

//        Collections.sort(home.getStations());
//        for (final Station station : home.getStations()) {
//            String titleS = "<html><font size=+1>" + station.getName() + "</font></html>";
//            DefaultCPTitle cpTitleS = new DefaultCPTitle(titleS, null);
//
//            /***
//             *               _ _ _         _        _   _
//             *       ___  __| (_) |_   ___| |_ __ _| |_(_) ___  _ __
//             *      / _ \/ _` | | __| / __| __/ _` | __| |/ _ \| '_ \
//             *     |  __/ (_| | | |_  \__ \ || (_| | |_| | (_) | | | |
//             *      \___|\__,_|_|\__| |___/\__\__,_|\__|_|\___/|_| |_|
//             *
//             */
//            final JButton btnEditStation = new JButton(SYSConst.icon22edit);
//            btnEditStation.setPressedIcon(SYSConst.icon22Pressed);
//            btnEditStation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//            btnEditStation.setContentAreaFilled(false);
//            btnEditStation.setBorder(null);
//
//            btnEditStation.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//
//                    final JidePopup popup = GUITools.getTextEditor(station.getName(), 1, 40, new Closure() {
//                        @Override
//                        public void execute(Object o) {
//                            if (o != null && !o.toString().trim().isEmpty()) {
//                                EntityManager em = OPDE.createEM();
//                                try {
//                                    em.getTransaction().begin();
//                                    Station myStation = em.merge(station);
//                                    myStation.setName(o.toString().trim());
//                                    em.getTransaction().commit();
//                                    createHomesList();
//                                    OPDE.getMainframe().emptySearchArea();
//                                    OPDE.getMainframe().prepareSearchArea();
//                                } catch (Exception e) {
//                                    em.getTransaction().rollback();
//                                    OPDE.fatal(e);
//                                } finally {
//                                    em.close();
//                                }
//                            }
//                        }
//                    }, btnEditStation);
//                    GUITools.showPopup(popup, SwingConstants.EAST);
//                }
//            });
//
//            cpTitleS.getRight().add(btnEditStation);
//
//
//            if (station.getResidents().isEmpty()) {
//                /***
//                 *          _      _      _             _        _   _
//                 *       __| | ___| | ___| |_ ___   ___| |_ __ _| |_(_) ___  _ __
//                 *      / _` |/ _ \ |/ _ \ __/ _ \ / __| __/ _` | __| |/ _ \| '_ \
//                 *     | (_| |  __/ |  __/ ||  __/ \__ \ || (_| | |_| | (_) | | | |
//                 *      \__,_|\___|_|\___|\__\___| |___/\__\__,_|\__|_|\___/|_| |_|
//                 *
//                 */
//                final JButton btnDeleteStation = new JButton(SYSConst.icon22delete);
//                btnDeleteStation.setPressedIcon(SYSConst.icon22Pressed);
//                btnDeleteStation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//                btnDeleteStation.setContentAreaFilled(false);
//                btnDeleteStation.setBorder(null);
//
//                btnDeleteStation.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        new DlgYesNo(SYSTools.xx("misc.questions.delete1") + "<br/><i>" + station.getName() + "</i><br/>" + SYSTools.xx("misc.questions.delete2"), SYSConst.icon48delete, new Closure() {
//                            @Override
//                            public void execute(Object answer) {
//                                if (answer.equals(JOptionPane.YES_OPTION)) {
//                                    EntityManager em = OPDE.createEM();
//                                    try {
//                                        em.getTransaction().begin();
//                                        Station myStation = em.merge(station);
//                                        em.lock(myStation, LockModeType.OPTIMISTIC);
//                                        em.remove(myStation);
//                                        em.getTransaction().commit();
//                                        createHomesList();
//                                        OPDE.getMainframe().emptySearchArea();
//                                        OPDE.getMainframe().prepareSearchArea();
//                                    } catch (RollbackException ole) {
//                                        if (em.getTransaction().isActive()) {
//                                            em.getTransaction().rollback();
//                                        }
//                                        if (ole.getMessage().indexOf("Class> entity.info.Resident") > -1) {
//                                            OPDE.getMainframe().completeRefresh();
//                                        }
//                                        OPDE.getDisplayManager().addSubMessage(DisplayManager.getLockMessage());
//                                    } catch (Exception e) {
//                                        if (em.getTransaction().isActive()) {
//                                            em.getTransaction().rollback();
//                                        }
//                                        OPDE.fatal(e);
//                                    } finally {
//                                        em.close();
//                                    }
//                                }
//                            }
//                        });
//                    }
//                });
//                cpTitleS.getRight().add(btnDeleteStation);
//            }
//
//            pnlContentH.add(cpTitleS.getMain());
//
//        }
//        String titleH = "<html><font size=+1><b>" + home.getName() + "</b></font></html>";
//        DefaultCPTitle cpTitleH = new DefaultCPTitle(titleH, null);
//
//        CollapsiblePane cpH = new CollapsiblePane();
//        cpH.setSlidingDirection(SwingConstants.SOUTH);
//        cpH.setHorizontalAlignment(SwingConstants.LEADING);
//        cpH.setOpaque(false);
//        cpH.setTitleLabelComponent(cpTitleH.getMain());
//
//        /***
//         *               _ _ _     _
//         *       ___  __| (_) |_  | |__   ___  _ __ ___   ___
//         *      / _ \/ _` | | __| | '_ \ / _ \| '_ ` _ \ / _ \
//         *     |  __/ (_| | | |_  | | | | (_) | | | | | |  __/
//         *      \___|\__,_|_|\__| |_| |_|\___/|_| |_| |_|\___|
//         *
//         */
//        final JButton btnEditHome = new JButton(SYSConst.icon22edit);
//        btnEditHome.setPressedIcon(SYSConst.icon22Pressed);
//        btnEditHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnEditHome.setContentAreaFilled(false);
//        btnEditHome.setBorder(null);
//        btnEditHome.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                final PnlHomes pnlHomes = new PnlHomes(home);
//                GUITools.showPopup(GUITools.createPanelPopup(pnlHomes, new Closure() {
//                    @Override
//                    public void execute(Object o) {
//                        if (o != null) {
//                            EntityManager em = OPDE.createEM();
//                            try {
//                                em.getTransaction().begin();
//                                Homes myHome = em.merge((Homes) o);
//                                em.getTransaction().commit();
//                                createHomesList();
//                                OPDE.getMainframe().emptySearchArea();
//                                OPDE.getMainframe().prepareSearchArea();
//                            } catch (Exception e) {
//                                em.getTransaction().rollback();
//                                OPDE.fatal(e);
//                            } finally {
//                                em.close();
//                            }
//                        }
//                    }
//                }, btnEditHome), SwingConstants.SOUTH_WEST);
//
//            }
//        });
//        cpTitleH.getRight().add(btnEditHome);
//
//        if (home.getStations().isEmpty()) {
//            final JButton btnDeleteHome = new JButton(SYSConst.icon22delete);
//            btnDeleteHome.setPressedIcon(SYSConst.icon22Pressed);
//            btnDeleteHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//            btnDeleteHome.setContentAreaFilled(false);
//            btnDeleteHome.setBorder(null);
//
//            btnDeleteHome.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    new DlgYesNo(SYSTools.xx("misc.questions.delete1") + "<br/><i>" + HomesTools.getAsText(home) + "</i><br/>" + SYSTools.xx("misc.questions.delete2"), SYSConst.icon48delete, new Closure() {
//                        @Override
//                        public void execute(Object answer) {
//                            if (answer.equals(JOptionPane.YES_OPTION)) {
//                                EntityManager em = OPDE.createEM();
//                                try {
//                                    em.getTransaction().begin();
//                                    Homes myHome = em.merge(home);
//                                    em.lock(myHome, LockModeType.OPTIMISTIC);
//                                    em.remove(myHome);
//                                    em.getTransaction().commit();
//                                    createHomesList();
//                                    OPDE.getMainframe().emptySearchArea();
//                                    OPDE.getMainframe().prepareSearchArea();
//                                } catch (RollbackException ole) {
//                                    if (em.getTransaction().isActive()) {
//                                        em.getTransaction().rollback();
//                                    }
//                                    if (ole.getMessage().indexOf("Class> entity.info.Resident") > -1) {
//                                        OPDE.getMainframe().completeRefresh();
//                                    }
//                                    OPDE.getDisplayManager().addSubMessage(DisplayManager.getLockMessage());
//                                } catch (Exception e) {
//                                    if (em.getTransaction().isActive()) {
//                                        em.getTransaction().rollback();
//                                    }
//                                    OPDE.fatal(e);
//                                } finally {
//                                    em.close();
//                                }
//                            }
//                        }
//                    });
//                }
//            });
//            cpTitleH.getRight().add(btnDeleteHome);
//        }
//
//        cpH.setContentPane(pnlContentH);
//        cpsHomes.add(cpH);
//        root.add(subtree);
    }


//    private JPanel createContentPane4Stations(Homes home) {
//        JPanel pnlContent = new JPanel(new VerticalLayout());
//
//        final JideButton btnAddStation = GUITools.createHyperlinkButton("opde.settings.btnAddStation", SYSConst.icon22add, null);
//        btnAddStation.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JidePopup popup = GUITools.getTextEditor(null, 1, 40, new Closure() {
//                    @Override
//                    public void execute(Object o) {
//                        if (o != null && !o.toString().trim().isEmpty()) {
//                            EntityManager em = OPDE.createEM();
//                            try {
//                                em.getTransaction().begin();
//                                em.merge(new Station(o.toString(), em.merge(home)));
//                                em.getTransaction().commit();
//                                createHomesList();
//                                OPDE.getMainframe().emptySearchArea();
//                                OPDE.getMainframe().prepareSearchArea();
//                            } catch (Exception e) {
//                                em.getTransaction().rollback();
//                                OPDE.fatal(e);
//                            } finally {
//                                em.close();
//                            }
//                        }
//                    }
//                }, btnAddStation);
//                GUITools.showPopup(popup, SwingConstants.EAST);
//            }
//        });
//
//
//        pnlContent.add(btnAddStation);
//
//        return pnlContent;
//    }


//    private JideButton createAddRoomButton(Floors floor) {
//        final JideButton btnAddRoom = GUITools.createHyperlinkButton("opde.settings.btnAddRoom", SYSConst.icon22add, null);
//        btnAddRoom.addActionListener(e -> {
//            final PnlRooms pnlRooms = new PnlRooms(new Rooms("", false, false, floor));
//            JidePopup popup = GUITools.createPanelPopup(pnlRooms, new Closure() {
//                @Override
//                public void execute(Object o) {
//                    if (o != null) {
////                        EntityManager em = OPDE.createEM();
////                        try {
////                            em.getTransaction().begin();
////                            home = em.merge((Homes) o);
////                            create(home);
////                            em.getTransaction().commit();
////
////                        } catch (IllegalStateException ise) {
////                            OPDE.error(ise);
////                        } catch (Exception e) {
////                            em.getTransaction().rollback();
////                            OPDE.fatal(e);
////                        } finally {
////                            em.close();
////                            setRefresh(true);
////                        }
//                    }
//                }
//            }, btnAddRoom);
//            GUITools.showPopup(popup, SwingConstants.EAST);
//            pnlRooms.setStartFocus();
//        });
//
//        btnAddRoom.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        return btnAddRoom;
//
//    }

    private JideButton createAddHomeButton() {
        final JideButton btnAddHome = GUITools.createHyperlinkButton("opde.settings.btnAddHome", SYSConst.icon22add, null);
        btnAddHome.addActionListener(e -> {

            EntityManager em = OPDE.createEM();
            try {
                em.getTransaction().begin();
                Homes home = em.merge(new Homes(UUID.randomUUID().toString().substring(0, 15)));
                em.getTransaction().commit();
                objectToRefresh = home;
            } catch (IllegalStateException ise) {
                logger.error(ise);
            } catch (Exception ex) {
                em.getTransaction().rollback();
                logger.fatal(ex);
            } finally {
                em.close();
                refresh = true;
            }
        });

        return btnAddHome;
    }


    private JideButton createAddStationButton() {
        final JideButton btnAddHome = GUITools.createHyperlinkButton("opde.settings.btnAddStation", SYSConst.icon22add, null);
//        btnAddHome.addActionListener(e -> {
//            final PnlHomes pnlHomes = new PnlHomes(new Homes(UUID.randomUUID().toString().substring(0, 15)));
//            JidePopup popup = GUITools.createPanelPopup(pnlHomes, new Closure() {
//                @Override
//                public void execute(Object o) {
//                    if (o != null && !o.toString().trim().isEmpty()) {
////                            EntityManager em = OPDE.createEM();
////                            try {
////                                em.getTransaction().begin();
////                                em.merge(new Station(o.toString(), em.merge(home)));
////                                em.getTransaction().commit();
////                                createHomesList();
////                                OPDE.getMainframe().emptySearchArea();
////                                OPDE.getMainframe().prepareSearchArea();
////                            } catch (Exception e) {
////                                em.getTransaction().rollback();
////                                OPDE.fatal(e);
////                            } finally {
////                                em.close();
////                            }
//                    }
//                }
//            }, btnAddHome);
//            GUITools.showPopup(popup, SwingConstants.EAST);
//            pnlHomes.setStartFocus();
//        });

        return btnAddHome;

    }

    private String getKey(Object object) {
        if (object instanceof DefaultMutableTreeNode)
            object = ((DefaultMutableTreeNode) object).getUserObject();
        if (object instanceof Rooms)
            return "room:" + ((Rooms) object).getRoomID();
        if (object instanceof Homes)
            return "home:" + ((Homes) object).getEID();
        if (object instanceof Floors)
            return "floor:" + ((Floors) object).getFloorid();
        if (object instanceof Station)
            return "station:" + ((Station) object).getStatID();

        return null;
    }


}
