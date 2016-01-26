package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListCellRenderer.UIResource;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Font;
import javax.swing.JMenuItem;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Controller.Audio.MpegInfo.Duration;
import Controller.Controller.REPRODUCE;

public class GUI implements ViewInterface{

    private static final double PERC_HALF = 0.5;
    private static final double PERC_QUATER = 0.25;
    private ViewObserver controller;   
    private final JFrame frame;
    private final JScrollPane scrollPane = new JScrollPane();
    private JFileChooser chooser = new JFileChooser(); 
    private JList<String> list;
    private boolean playing = false;
    private boolean stopped = true;
    private String selectedSongName;
    private String selectedPlaylistName;
    private JSlider seekBar = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);
    private Integer deltaColor = 0;

    public GUI() {
        
        frame = new JFrame("BeeSound Player");
        final JPanel pnLanding = new JPanel(new BorderLayout());
        frame.setFont(new Font("Trajan Pro", Font.PLAIN, 12));
        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = (int)(dimension.getWidth() * PERC_HALF - dimension.getWidth() * PERC_QUATER);
        final int y = (int)(dimension.getHeight() * PERC_HALF - dimension.getHeight() * PERC_QUATER);
        frame.setSize((int) (dimension.getWidth() * 0.5), (int) (dimension.getHeight() * 0.5));
        frame.setLocation(x, y);                
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           
        frame.setMinimumSize(new Dimension(200, 100));
        
        /* RIGHT PANEL FOR IMAGE AND INFO CURRENT SONG */

        final JPanel pnRight = new JPanel();
        pnRight.setLayout(new BoxLayout(pnRight, BoxLayout.Y_AXIS));
        final URL ImgURL = UIResource.class.getResource("/zutons.jpg");
        final JLabel lbImage = new JLabel();
        lbImage.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.46), 250));
        lbImage.setIcon(new ImageIcon(ImgURL));
        lbImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        final JLabel lbInfoCurrent = new JLabel("Info Current");
        lbInfoCurrent.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.46), (int)(frame.getHeight() * 0.1)));
        lbInfoCurrent.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pnRight.add(seekBar);
        pnRight.add(lbImage);     
        pnRight.add(lbInfoCurrent);

        /* CENTER PANEL: LIST SELECTION & INFO LABEL */

        final JPanel pnListView = new JPanel();
        pnListView.setLayout(new BoxLayout(pnListView, BoxLayout.Y_AXIS));
        final JPanel pnInfoLibrary = new JPanel();
        pnInfoLibrary.setMaximumSize(new Dimension(32767, 30));
        final JLabel lbInfoLibrary = new JLabel("Numero brani + minutaggio: ");
        pnInfoLibrary.setBackground(new Color(100, 100, 255));
        //setLibraryInfo(lbInfoLibrary, controller.showInfoLibrary());

        pnListView.add(scrollPane);
        pnListView.add(pnInfoLibrary);                               
        pnInfoLibrary.add(lbInfoLibrary);

        /* SOUTH PANEL: CONTROL PLAYER'S BUTTONS */

        final JPanel pnPlayerButtons = new JPanel(new FlowLayout());        
        final JButton btPlay = new JButton(" ▶ ");
        final JButton btStop = new JButton(" ■ ");
        final JButton btPrev = new JButton(" << ");
        final JButton btNext = new JButton(" >> ");
        final JButton btShuffle = new JButton("Shuffle");
        final JButton btLinear = new JButton("Linear");       
        final JSlider slVol = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 100);
        final JLabel lbVol = new JLabel(" volume ");
        
        // PLAY
        btPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (stopped) {                   

                    controller.addSongInReproductionPlaylist(list.getModel()
                            .getElementAt(list.getMaxSelectionIndex()), REPRODUCE.NOW);
                    //Agent agent = new Agent(seekBar);
                    //agent.start();
                    playing = true;
                    setInfoLabel(lbInfoCurrent, controller.getCurrentSongInfo());
                }
                else {

                    controller.pauseButton();
                    playing = !playing;
                }
                stopped = false;
                updatePlayButton(btPlay);
            }
        }); 
        
        //STOP
        btStop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.stopButton();              
                stopped = true;
                playing = false;
                updatePlayButton(btPlay);
            }
        });
        
        //PREVIOUS
        btPrev.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.prevTrack();               
            }
        });
        
        //NEXT
        btNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.nextTrack();           
            }
        });
        
        //VOLUME
        slVol.setPreferredSize(new Dimension(150, 20));
        slVol.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                controller.setVolumeButton((double)slVol.getValue() / 100);                
            }
        });
        
        //SHUFFLE MODE
        btShuffle.setEnabled(true);;
        btShuffle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.setShuffleMode();
                btShuffle.setEnabled(false);
                btLinear.setEnabled(true);
            }
        });
        
        //LINEAR MODE
        btLinear.setEnabled(false);
        btLinear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.linearMode();
                btShuffle.setEnabled(true);
                btLinear.setEnabled(false);
            }
        });

        pnPlayerButtons.add(btShuffle);
        pnPlayerButtons.add(Box.createRigidArea(new Dimension()));
        pnPlayerButtons.add(btLinear);
        pnPlayerButtons.add(Box.createRigidArea(new Dimension(10, 0)));
        pnPlayerButtons.add(lbVol);
        pnPlayerButtons.add(slVol);
        pnPlayerButtons.add(Box.createRigidArea(new Dimension(10, 0)));
        pnPlayerButtons.add(btPrev);
        pnPlayerButtons.add(btStop);
        pnPlayerButtons.add(btPlay);
        pnPlayerButtons.add(btNext);

        /////////////////// LEFT PANEL & BUTTONS ////////////////////////

        final JPanel pnLeftButtons = new JPanel(new GridLayout(0, 1, 0, 0));
        pnLeftButtons.setPreferredSize(new Dimension(85, 0));       
        final JButton btAll = new JButton("All Songs");
        final JButton btAlbum = new JButton("Albums");
        final JButton btArtist = new JButton("Artists");
        final JButton btPlaylist = new JButton("Yuor Playlists");
        final JButton btGenre = new JButton("Music Genre");
        final JButton btFavorites = new JButton("Più ascoltati");
        final JButton btReproduction = new JButton("In riproduzione");
        
        //ALL SONGS
        setLeftButtons(btAll);
        btAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllSong()));

                if(list.getModel().getSize() > 0) {
                    list.setSelectedIndex(0);
                    selectedSongName = list.getModel().getElementAt(list.getMaxSelectionIndex());
                }
                createSelectableList();

                list.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {}                    

                    @Override
                    public void mousePressed(MouseEvent e) {

                        if(list.getModel().getSize() > 0) {

                            JPopupMenu menu = buildStandardPopup(btAll, true, false, true, false, true, false, false);
                            if(e.isPopupTrigger()) {

                                menu.show(e.getComponent(), e.getX(), e.getY());
                                selectedSongName = list.getModel().getElementAt(list.getMaxSelectionIndex());
                            }
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {}

                    @Override
                    public void mouseEntered(MouseEvent e) {}

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if(e.getClickCount() == 2 && list.getModel().getSize() > 0) {

                            controller.addSongInReproductionPlaylist(list.getModel()
                                    .getElementAt(list.getMaxSelectionIndex()), REPRODUCE.NOW);
                            //Agent agent = new Agent(seek);
                            //agent.start();
                            playing = true;
                            stopped = false;
                            updatePlayButton(btPlay);
                            setInfoLabel(lbInfoCurrent, controller.getCurrentSongInfo());
                        }                        
                    }
                });                

            }
        });
        
        //ALBUMS
        setLeftButtons(btAlbum);
        btAlbum.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllAlbum()));
                createSelectableList();
            }
        });

        setLeftButtons(btArtist);
        btArtist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllArtist()));
                createSelectableList();
            }
        });
        
        //PLAYLIST
        setLeftButtons(btPlaylist);
        btPlaylist.addActionListener(new ActionListener() {           

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllPlaylist()));
                if(list.getModel().getSize() > 0) {
                    list.setSelectedIndex(0);
                }
                createSelectableList();

                list.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {}

                    @Override
                    public void mousePressed(MouseEvent e) {

                        if(list.getModel().getSize() > 0) {

                            JPopupMenu menu = buildStandardPopup(btPlaylist, false, false, false, true, false, true, false);
                            if(e.isPopupTrigger()) {

                                menu.show(e.getComponent(), e.getX(), e.getY());
                                selectedSongName = list.getModel().getElementAt(list.getMaxSelectionIndex());
                            }
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {}

                    @Override
                    public void mouseEntered(MouseEvent e) {}

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if(e.getClickCount() == 2 && list.getModel().getSize() > 0) {
                            
                            selectedPlaylistName = list.getModel().getElementAt(list.getMaxSelectionIndex());
                            System.out.println(list.getModel().getElementAt(list.getMaxSelectionIndex()));
                            list = new JList<>(new Vector<>(controller.showPlaylistSong(list.getModel().getElementAt(list.getMaxSelectionIndex()))));
                            createSelectableList();

                            list.addMouseListener(new MouseListener() {

                                @Override
                                public void mouseReleased(MouseEvent e) {}                    

                                @Override
                                public void mousePressed(MouseEvent e) {

                                    if(list.getModel().getSize() > 0) {

                                        JPopupMenu menu = buildStandardPopup(btPlaylist, true, false, true, false, false, false, true);
                                        if(e.isPopupTrigger()) {

                                            menu.show(e.getComponent(), e.getX(), e.getY());
                                        }
                                    }
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {}

                                @Override
                                public void mouseEntered(MouseEvent e) {}

                                @Override
                                public void mouseClicked(MouseEvent e) {

                                    if(e.getClickCount() == 2 && list.getModel().getSize() > 0) {

                                        controller.addSongInReproductionPlaylist(list.getModel()
                                                .getElementAt(list.getMaxSelectionIndex()), REPRODUCE.NOW);
                                        //Agent agent = new Agent(seek);
                                        //agent.start();
                                        playing = true;
                                        stopped = false;
                                        updatePlayButton(btPlay);
                                        setInfoLabel(lbInfoCurrent, controller.getCurrentSongInfo());
                                    }                        
                                }
                            }); 
                            createSelectableList();
                        }

                    }
                });
            }
        });
        
        //GENRE
        setLeftButtons(btGenre);
        btGenre.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllGenre()));
                createSelectableList();
            }
        });
        
        //FAVORITES
        setLeftButtons(btFavorites);
        btFavorites.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showFavorites()));
                createSelectableList();
            }
        });
        
        //REPRODUCTION QUEUE
        setLeftButtons(btReproduction);
        btReproduction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showReproductionPlaylist()));
                list.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {}                    

                    @Override
                    public void mousePressed(MouseEvent e) {

                        if(list.getModel().getSize() > 0) {

                            JPopupMenu menu = buildStandardPopup(btReproduction, false, true, false, false, false, false, false);
                            if(e.isPopupTrigger()) {

                                menu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {}

                    @Override
                    public void mouseEntered(MouseEvent e) {}

                    @Override
                    public void mouseClicked(MouseEvent e) {}                        

                });                
                createSelectableList();
            }
        });

        pnLeftButtons.add(btAll);
        pnLeftButtons.add(btAlbum);
        pnLeftButtons.add(btArtist);
        pnLeftButtons.add(btPlaylist);
        pnLeftButtons.add(btGenre);
        pnLeftButtons.add(btFavorites);
        pnLeftButtons.add(btReproduction);

        ////////////////   TOP MENU ////////////////////

        final JMenuBar mnBar = new JMenuBar();
        final JMenu mnFile = new JMenu("File");
        final JMenu mnInfo = new JMenu("Info");
        final JMenuItem mniAddToLib = new JMenuItem("Add file to Library");
        final JMenuItem mniCreatePlaylist = new JMenuItem("Create new Playlist");
        final JMenuItem mniExit = new JMenuItem("Exit Program");
        final JMenuItem mniBeeInfo = new JMenuItem("Info Beesound");
        mnBar.setBorder(null);
        
        //ADD MP3 LIBRARY
        mniAddToLib.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = chooser.showOpenDialog(mnFile);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    controller.addSong(chooser.getSelectedFile().getAbsolutePath());
                    btAll.doClick();
                }
            }
        });
        
        //CREATE PLAYLIST
        mniCreatePlaylist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                final JFrame frChoosePlaylist = new JFrame("Your Playlist");
                final JPanel pnChoosePlaylist = new JPanel();
                final JLabel lbChoosePlaylist = new JLabel("Insert playlist name  ");
                final JTextArea taChoosePlaylist = new JTextArea(1, 10);
                final JButton btChoosePlaylist = new JButton("ok");
                btChoosePlaylist.setBackground(new Color(200, 200, 255));
                btChoosePlaylist.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        controller.newPlaylistFile(taChoosePlaylist.getText());
                        frChoosePlaylist.dispose();
                    }
                });

                pnChoosePlaylist.add(lbChoosePlaylist);
                pnChoosePlaylist.add(taChoosePlaylist);
                pnChoosePlaylist.add(Box.createRigidArea(new Dimension(5, 0)));
                pnChoosePlaylist.add(btChoosePlaylist);
                frChoosePlaylist.add(pnChoosePlaylist);
                frChoosePlaylist.setSize(400, 40);
                frChoosePlaylist.setLocationRelativeTo(frame);
                frChoosePlaylist.setVisible(true);
            }
        });
        
        //EXIT PROGRAM
        mniExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        //INFO BEESOUND
        mniBeeInfo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                final JFrame frBeeInfo = new JFrame("Beesound members");
                final JTextArea taBeeInfo = new JTextArea("\nThis Program has been realized by: \n"
                        + "\nTiziano De Cristofaro : model\n"
                        + "Carlo Alberto Scola: controller\n"
                        + "Gianluca Cincinelli: view\n");
                taBeeInfo.setForeground(new Color(20, 40, 150));
                taBeeInfo.setEditable(false);
                taBeeInfo.setLineWrap(true);
                frBeeInfo.add(taBeeInfo);
                frBeeInfo.setSize(300, 120);
                frBeeInfo.setLocationRelativeTo(frame);
                frBeeInfo.setVisible(true);
            }
        });

        //ARRAY WITH ALL COMPONENTS
        final Component[] compArray = new Component[]{mniExit, mniAddToLib, mniCreatePlaylist
                , mniBeeInfo, mnFile, mnInfo, mnBar, btLinear, btShuffle, btAll, btAlbum, btArtist
                , btPlaylist, btGenre, btFavorites, btPrev, btNext, btReproduction, lbInfoLibrary, pnInfoLibrary, lbInfoCurrent};
        setComponentFont(compArray);
        
        mnFile.add(mniAddToLib);
        mnFile.add(mniCreatePlaylist);
        mnFile.add(mniExit);
        mnInfo.add(mniBeeInfo);
        mnBar.add(mnFile);
        mnBar.add(mnInfo);
        pnLanding.add(pnListView, BorderLayout.CENTER);                  
        pnLanding.add(pnLeftButtons, BorderLayout.WEST);
        pnLanding.add(pnPlayerButtons, BorderLayout.SOUTH);
        pnLanding.add(pnRight, BorderLayout.EAST);
        frame.setJMenuBar(mnBar);  
        frame.getContentPane().add(pnLanding);
    }   

    ///////////////////////////  PRIVATE METHODS  ///////////////////////////////////

    @Override
    public void setVisible(final boolean visible) {
        this.frame.setVisible(visible);
    }

    /**
     * Refresh all components
     */
    @Override
    public void refreshView() {
        
    }

    /**
     * Sets font for a component
     * @param comp[]
     */
    private void setComponentFont(Component[] comp){

        for(Component var: comp) {
            var.setFont(new Font("Droid Sans", Font.PLAIN, 11));
        }
    }

    /**
     * Set information into a label about all songs in the library and their total time duration in minutes
     * @param map
     * @param label
     */
    private void setLibraryInfo(JLabel label, Map<String, Integer> map) {

        label.setText("Songs in library: " + map.get("nSongs") + " - Minuti totali: " + map.get("min"));
        label.setFont(new Font("Dialog", Font.PLAIN, 11));
        label.setBackground(new Color(20, 20, 150));
        label.setForeground(new Color(51, 204, 51));
    }

    /**
     * Set lable's text to show title and song duration
     * @param label
     * @param Map
     */
    private void setInfoLabel(JLabel label, Map<String, Object> map) {

        Duration duration = (Duration)(map.get("Duration"));
        label.setText(map.get("Title") + " - " + duration.getMin() + ":" + duration.getSec());
    }

    /**
     * Set left button Layout. Set a value from 0 to 86 to show a different range of colors
     * Set 0 < a < 41 for monocromatic look
     * Set 42 < a < 86 for a bicromatic look
     * @param button
     */
    private void setLeftButtons(final JButton button) {

        int a = 30, b = 255, c = b - (a * deltaColor);
        if(c > 0) {
            button.setBackground(new Color(255, 255, c));
        } else {
            button.setBackground(new Color(255 + c, 255, 255));
        }
        deltaColor++;
        button.setBorder(null);
    }

    /**
     * Create a selectable list to be shown into GUI
     */
    private void createSelectableList() {

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Droid Sans", Font.PLAIN, 11));
        scrollPane.setViewportView(list);
    }

    /**
     * Return the actual list selection index
     * @return
     */
    public int getSelectedIndex() {

        return this.list.getMaxSelectionIndex();
    }

    /**
     * Set the controller as the observer
     * @param observer
     */
    @Override
    public void setObserver(ViewObserver observer) {

        this.controller = observer;
    }

    /**
     * Switch between play/pause button
     * @param button
     */
    private void updatePlayButton(JButton button) {

        if (playing) {
            button.setText(" || ");
        }
        else {
            button.setText(" ▶ ");
        }        
    }

    /**
     * Build a popup menu on the right mouse click, with options to choose 
     * @return JPopupMenu
     */
    private JPopupMenu buildStandardPopup(JButton button, boolean addQueue, boolean remQueue, boolean rem
            , boolean remPlay, boolean addPlay, boolean playPlay, boolean remFromPlay) {

        final JPopupMenu menu = new JPopupMenu();
        final JMenuItem itemAddToReproductionList = new JMenuItem("Add to reproduction Playlist");

        itemAddToReproductionList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.addSongInReproductionPlaylist(list.getModel()
                        .getElementAt(list.getMaxSelectionIndex()), REPRODUCE.AFTER);
                button.doClick();
            }
        });
        
        final JMenuItem itemRemoveFromReproductionList = new JMenuItem("Remove from reproduction Playlist");
        itemRemoveFromReproductionList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeSongFromQueue(list.getModel()
                        .getElementAt(list.getMaxSelectionIndex()));              
                button.doClick();
            }

        });
        
        final JMenuItem itemRemoveFromLibrary = new JMenuItem("Remove from Library");
        itemRemoveFromLibrary.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeSong(list.getModel().getElementAt(list.getMaxSelectionIndex()));
                button.doClick();
            }
        });

        final JMenuItem itemRemovePlaylist = new JMenuItem("Remove playlist");
        itemRemovePlaylist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removePlaylist(list.getModel().getElementAt(list.getMaxSelectionIndex()));
                button.doClick();
            }
        });

        final JMenuItem itemAddToPlaylist = new JMenuItem("Add song to Playlist");
        
        
        itemAddToPlaylist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllPlaylist()));
                String[] array = new String[list.getModel().getSize()];

                for(int i = 0; i < array.length; i++ ) {

                    array[i] = (String)(list.getModel().getElementAt(i));
                }

                final JFrame frameChoosePlaylist = new JFrame("Your Playlist");
                final JPanel panelChoosePlaylist = new JPanel();
                final JLabel labelChoosePlaylist = new JLabel("Select a playlist");

                labelChoosePlaylist.setFont(new Font("Dialog", Font.PLAIN, 12));
                final JComboBox<String> combo = new JComboBox<>(array);
                combo.setPreferredSize(new Dimension(100, 20));
                final JButton buttonChoosePlaylist = new JButton("ok");
                buttonChoosePlaylist.setBackground(new Color(200, 200, 255));
                buttonChoosePlaylist.setFont(new Font("Dialog", Font.BOLD, 12));
                buttonChoosePlaylist.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        controller.addSongInPlaylist(selectedSongName, combo.getSelectedItem().toString());
                        button.doClick();
                        frameChoosePlaylist.dispose();
                    }
                });

                panelChoosePlaylist.add(labelChoosePlaylist);
                panelChoosePlaylist.add(combo);
                panelChoosePlaylist.add(buttonChoosePlaylist);
                frameChoosePlaylist.add(panelChoosePlaylist);
                frameChoosePlaylist.setSize(350, 65);
                frameChoosePlaylist.setLocationRelativeTo(frame);
                frameChoosePlaylist.setVisible(true);                
            }                       
        });
        
        final JMenuItem itemPlayPlaylist = new JMenuItem("Play Playlist");
        itemPlayPlaylist.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                //controller.playPlaylist(list.getModel().getElementAt(list.getMaxSelectionIndex()));
                button.doClick();
            }
        });
        
        final JMenuItem itemRemFromPlaylist = new JMenuItem("Remove from this playplist");
        itemRemFromPlaylist.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeSongFromPlaylist(list.getModel().getElementAt(list.getMaxSelectionIndex()), selectedPlaylistName);
                button.doClick();
            }
        });
        
        if(controller.showAllPlaylist().isEmpty()) {
            itemAddToPlaylist.setEnabled(false);
        }       
        if(addQueue) {
            menu.add(itemAddToReproductionList);
        }
        if(remQueue) {
            menu.add(itemRemoveFromReproductionList);
        }
        if(rem) {
            menu.add(itemRemoveFromLibrary);
        }
        if(remPlay) {
            menu.add(itemRemovePlaylist);
        }
        if(addPlay) {
            menu.add(itemAddToPlaylist);
        }
        if(playPlay) {
            menu.add(itemPlayPlaylist);
        }
        if(remFromPlay) {
            menu.add(itemRemFromPlaylist);
        }

        return menu;
    }

    /**
     * This class create and start a new thread for running the seekbar media
     * Seekbar listens and works as a new thread until the song is playing.
     *
     */
    /*private class Agent extends Thread {

        private JSlider seek;
        private Duration duration;
        private int totSec;

        public Agent(JSlider seek) {

            duration = (Duration) controller.getCurrentSongInfo().get("Duration"); 
            this.totSec = (duration.getSec() + duration.getMin() * 60);
            this.seek = seek;
            this.seek.setValue(0);                      
            seek.setMaximum(totSec);                     
        }

        @Override
        public void run() {            

            while(seek.getValue() < totSec) {

                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            seek.setValue(seek.getValue() + 1);
                        }
                    });
                } catch (InvocationTargetException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

}