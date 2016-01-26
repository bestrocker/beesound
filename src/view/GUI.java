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
import java.lang.reflect.InvocationTargetException;
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
import javax.swing.SwingUtilities;
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
    private String songName;
    private JSlider seek = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);
    private Integer colorVal = 0;

    public GUI() {

        /* FRAME AND PANEL */

        frame = new JFrame("BeeSound Player");
        frame.setFont(new Font("Trajan Pro", Font.PLAIN, 12));
        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = (int)(dimension.getWidth() * PERC_HALF - dimension.getWidth() * PERC_QUATER);
        final int y = (int)(dimension.getHeight() * PERC_HALF - dimension.getHeight() * PERC_QUATER);
        frame.setSize((int) (dimension.getWidth() * 0.5), (int) (dimension.getHeight() * 0.5));
        frame.setLocation(x, y);                
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           
        frame.setMinimumSize(new Dimension(200, 100));

        final JPanel landingPanel = new JPanel();
        final BorderLayout landingLayout = new BorderLayout();
        landingPanel.setLayout(landingLayout);

        /* RIGHT PANEL FOR IMAGE AND INFO CURRENT SONG */

        final JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(seek);

        final URL ImgURL = UIResource.class.getResource("/zutons.jpg");
        final JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.46), 250));
        imageLabel.setIcon(new ImageIcon(ImgURL));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel infoTitle = new JLabel("Info Title");
        infoTitle.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.46), (int)(frame.getHeight() * 0.1)));
        infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(imageLabel);     
        rightPanel.add(infoTitle);

        /* CENTER PANEL: LIST SELECTION & INFO LABEL */

        final JPanel listSelectionPanel = new JPanel();
        listSelectionPanel.setLayout(new BoxLayout(listSelectionPanel, BoxLayout.Y_AXIS));

        final JPanel counterPanel = new JPanel();
        counterPanel.setMaximumSize(new Dimension(32767, 30));
        final JLabel counterLabel = new JLabel("Numero brani + minutaggio: ");
        counterPanel.setBackground(new Color(100, 100, 255));
        //setLibraryInfo(counterLabel, controller.showInfoLibrary());

        listSelectionPanel.add(scrollPane);
        listSelectionPanel.add(counterPanel);                               
        counterPanel.add(counterLabel);

        /* SOUTH PANEL: CONTROL PLAYER'S BUTTONS */

        final FlowLayout playerButtonsLayout = new FlowLayout();
        final JPanel playerButtonsPanel = new JPanel(playerButtonsLayout);
        
        final JButton button_6 = new JButton(" ▶ ");
        final JButton button_7 = new JButton(" ■ ");
        final JButton button_8 = new JButton(" << ");
        final JButton button_9 = new JButton(" >> ");
        final JButton bShuffle = new JButton("Shuffle");
        final JButton bLinear = new JButton("Linear");
        
        final JSlider volume = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 100);
        final JLabel volumeLabel = new JLabel(" volume ");

        button_6.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (stopped) {                   

                    controller.addSongInReproductionPlaylist(list.getModel()
                            .getElementAt(list.getMaxSelectionIndex()), REPRODUCE.NOW);
                    Agent agent = new Agent(seek);
                    //agent.start();
                    playing = true;
                    setInfoLabel(infoTitle, controller.getCurrentSongInfo());
                }
                else {

                    controller.pauseButton();
                    playing = !playing;
                }
                stopped = false;
                updatePlayButton(button_6);
            }
        }); 

        button_7.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.stopButton();              
                stopped = true;
                playing = false;
                updatePlayButton(button_6);
            }
        });

        button_8.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.prevTrack();               
            }
        });

        button_9.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.nextTrack();           
            }
        });

        volume.setPreferredSize(new Dimension(150, 20));
        volume.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                controller.setVolumeButton((double)volume.getValue() / 100);                
            }
        });

        bShuffle.setEnabled(true);;
        bShuffle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.setShuffleMode();
                bShuffle.setEnabled(false);
                bLinear.setEnabled(true);
            }
        });

        bLinear.setEnabled(false);
        bLinear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.linearMode();
                bShuffle.setEnabled(true);
                bLinear.setEnabled(false);
            }
        });

        playerButtonsPanel.add(bShuffle);
        playerButtonsPanel.add(Box.createRigidArea(new Dimension()));
        playerButtonsPanel.add(bLinear);
        playerButtonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        playerButtonsPanel.add(volumeLabel);
        playerButtonsPanel.add(volume);
        playerButtonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        playerButtonsPanel.add(button_8);
        playerButtonsPanel.add(button_7);
        playerButtonsPanel.add(button_6);
        playerButtonsPanel.add(button_9);

        /* LEFT PANEL & BUTTONS */

        final JPanel leftButtonsPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        leftButtonsPanel.setPreferredSize(new Dimension(85, 0));
        
        final JButton button = new JButton("All Songs");
        final JButton button_1 = new JButton("Albums");
        final JButton button_2 = new JButton("Artists");
        final JButton button_3 = new JButton("Yuor Playlists");
        final JButton button_4 = new JButton("Music Genre");
        final JButton button_5 = new JButton("Più ascoltati");
        final JButton buttonQueue = new JButton("In riproduzione");

        setLeftButtons(button);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllSong()));

                if(list.getModel().getSize() > 0) {
                    list.setSelectedIndex(0);
                    songName = list.getModel().getElementAt(list.getMaxSelectionIndex());
                }
                createSelectableList();

                list.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {}                    

                    @Override
                    public void mousePressed(MouseEvent e) {

                        if(list.getModel().getSize() > 0) {

                            JPopupMenu menu = buildStandardPopup(button, true, true, true, false, true);
                            if(e.isPopupTrigger()) {

                                menu.show(e.getComponent(), e.getX(), e.getY());
                                songName = list.getModel().getElementAt(list.getMaxSelectionIndex());
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
                            updatePlayButton(button_6);
                            setInfoLabel(infoTitle, controller.getCurrentSongInfo());
                        }                        
                    }
                });                

            }
        });

        setLeftButtons(button_1);
        button_1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllAlbum()));
                createSelectableList();
            }
        });

        setLeftButtons(button_2);
        button_2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllArtist()));
                createSelectableList();
            }
        });

        setLeftButtons(button_3);
        button_3.addActionListener(new ActionListener() {           

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

                            JPopupMenu menu = buildStandardPopup(button_3, false, false, false, true, false);
                            if(e.isPopupTrigger()) {

                                menu.show(e.getComponent(), e.getX(), e.getY());
                                songName = list.getModel().getElementAt(list.getMaxSelectionIndex());
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

                            list = new JList<>(new Vector<>(controller.showPlaylistSong(list.getModel()
                                    .getElementAt(list.getMaxSelectionIndex()))));
                            createSelectableList();

                            list.addMouseListener(new MouseListener() {

                                @Override
                                public void mouseReleased(MouseEvent e) {}                    

                                @Override
                                public void mousePressed(MouseEvent e) {

                                    if(list.getModel().getSize() > 0) {

                                        JPopupMenu menu = buildStandardPopup(buttonQueue, true, true, true, false, false);
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
                                        updatePlayButton(button_6);
                                        setInfoLabel(infoTitle, controller.getCurrentSongInfo());
                                    }                        
                                }
                            }); 
                            createSelectableList();
                        }

                    }
                });
            }
        });

        setLeftButtons(button_4);
        button_4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllGenre()));
                createSelectableList();
            }
        });

        setLeftButtons(button_5);
        button_5.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showFavorites()));
                createSelectableList();
            }
        });

        setLeftButtons(buttonQueue);
        buttonQueue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showReproductionPlaylist()));
                list.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {}                    

                    @Override
                    public void mousePressed(MouseEvent e) {

                        if(list.getModel().getSize() > 0) {

                            JPopupMenu menu = buildStandardPopup(buttonQueue, true, true, false, false, false);
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

        leftButtonsPanel.add(button);
        leftButtonsPanel.add(button_1);
        leftButtonsPanel.add(button_2);
        leftButtonsPanel.add(button_3);
        leftButtonsPanel.add(button_4);
        leftButtonsPanel.add(button_5);
        leftButtonsPanel.add(buttonQueue);

        /* TOP MENU */

        final JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(null);
        frame.setJMenuBar(menuBar);  

        /* jmenu buttons: file, help */

        final JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);
        final JMenu menuInfo = new JMenu("Info");
        menuBar.add(menuInfo);

        /* choice menu(JMenuItem) */

        final JMenuItem menuChoiceImport = new JMenuItem("Add file to Library");
        menuChoiceImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = chooser.showOpenDialog(menuFile);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    controller.addSong(chooser.getSelectedFile().getAbsolutePath());
                    button.doClick();
                }
            }
        });
        menuFile.add(menuChoiceImport);
 
        final JMenuItem menuCreatePlaylist = new JMenuItem("Create new Playlist");
        menuCreatePlaylist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                final JFrame frameChoosePlaylist = new JFrame("Your Playlist");
                final JPanel panel = new JPanel();
                final JLabel label = new JLabel("Insert playlist name  ");
                final JTextArea area = new JTextArea(1, 10);
                final JButton button = new JButton("ok");
                button.setBackground(new Color(200, 200, 255));
                button.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        controller.newPlaylistFile(area.getText());
                        frameChoosePlaylist.dispose();
                    }
                });

                panel.add(label);
                panel.add(area);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(button);
                frameChoosePlaylist.add(panel);
                frameChoosePlaylist.setSize(400, 40);
                frameChoosePlaylist.setLocationRelativeTo(frame);
                frameChoosePlaylist.setVisible(true);
            }
        });
        menuFile.add(menuCreatePlaylist);

        final JMenuItem menuChoiceExit = new JMenuItem("Exit Program");
        menuChoiceExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuFile.add(menuChoiceExit);

        final JMenuItem menuChoiceInfo = new JMenuItem("Info Beesound");
        menuInfo.add(menuChoiceInfo);
        menuChoiceInfo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                final JFrame infoFrame = new JFrame("Beesound members");
                JTextArea textArea = new JTextArea("\nThis Program has been realized by: \n"
                        + "\nTiziano De Cristofaro : model\n"
                        + "Carlo Alberto Scola: controller\n"
                        + "Gianluca Cincinelli: view\n");
                textArea.setForeground(new Color(20, 40, 150));
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                infoFrame.add(textArea);
                infoFrame.setSize(300, 120);
                infoFrame.setLocationRelativeTo(frame);
                infoFrame.setVisible(true);
            }
        });

        /* array whit all components */

        final Component[] compArray = new Component[]{menuChoiceExit, menuChoiceImport, menuCreatePlaylist
                , menuChoiceInfo, menuFile, menuInfo, menuBar, bLinear, bShuffle, button, button_1, button_2
                , button_3, button_4, button_5, button_8, button_9, buttonQueue, counterLabel, counterPanel, infoTitle};
        setComponentFont(compArray);

        landingPanel.add(listSelectionPanel, BorderLayout.CENTER);                  
        landingPanel.add(leftButtonsPanel, BorderLayout.WEST);
        landingPanel.add(playerButtonsPanel, BorderLayout.SOUTH);
        landingPanel.add(rightPanel, BorderLayout.EAST);

        frame.getContentPane().add(landingPanel);
        /*
         * Decide il controller quando far vedere la view.
         */
        // frame.setVisible(true);
    }   

    ///////////////////////////  PRIVATE METHODS  ///////////////////////////////////

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

        int a = 30, b = 255, c = b - (a * colorVal);
        if(c > 0) {
            button.setBackground(new Color(255, 255, c));
        } else {
            button.setBackground(new Color(255 + c, 255, 255));
        }
        colorVal++;
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
            , boolean remPlay, boolean addPlay) {

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
                controller.removePlaylit(list.getModel().getElementAt(list.getMaxSelectionIndex()));
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

                        controller.addSongInPlaylist(songName, combo.getSelectedItem().toString());
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

        return menu;
    }


    /**
     * This class create and start a new thread for running the seekbar media
     * Seekbar listens and works as a new thread until the song is playing.
     *
     */
    private class Agent extends Thread {

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
    }


    @Override
    public void refreshView() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setVisible(final boolean visible) {
        this.frame.setVisible(visible);
    }

}