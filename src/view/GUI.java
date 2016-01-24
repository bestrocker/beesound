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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Controller.Audio.MpegInfo.*;
import Controller.Controller.REPRODUCE;
import Controller.ViewObserver;

import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Font;
import javax.swing.JMenuItem;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer.UIResource;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(200, 100));

        final JPanel landingPanel = new JPanel();
        final BorderLayout landingLayout = new BorderLayout();
        landingPanel.setLayout(landingLayout);
        
        /* RIGHT PANEL FOR IMAGE AND INFO CURRENT SONG */

        final JPanel rightPanel = new JPanel();
        rightPanel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(seek);

        /* beesound logo */

        final URL ImgURL = UIResource.class.getResource("/zutons.jpg");
        final JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.46), 250));
        imageLabel.setIcon(new ImageIcon(ImgURL));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel infoTitle = new JLabel("Info Title");
        infoTitle.setFont(new Font("Dialog", Font.PLAIN, 11));
        infoTitle.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.46), (int)(frame.getHeight() * 0.1)));
        infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(imageLabel);     
        rightPanel.add(infoTitle);

        /* CENTER PANEL: LIST SELECTION & INFO LABEL */

        final JPanel listSelectionPanel = new JPanel();
        listSelectionPanel.setLayout(new BoxLayout(listSelectionPanel, BoxLayout.Y_AXIS));

        /* information panel about list selection */

        final JPanel counterPanel = new JPanel();
        counterPanel.setMaximumSize(new Dimension(32767, 30));
        final JLabel counterLabel = new JLabel("Numero brani + minutaggio: ");
        counterPanel.setBackground(new Color(100, 100, 230));
        //setLibraryInfo(counterLabel, controller.showInfoLibrary());
        
        //scrollPane.setBorder(null);
        listSelectionPanel.add(scrollPane);
        listSelectionPanel.add(counterPanel);                               
        counterPanel.add(counterLabel);

        /* SOUTH PANEL: CONTROL PLAYER'S BUTTONS */

        final FlowLayout playerButtonsLayout = new FlowLayout();
        final JPanel playerButtonsPanel = new JPanel(playerButtonsLayout);

        final JButton button_6 = new JButton(" ▶ ");
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

        final JButton button_7 = new JButton(" ■ ");
        button_7.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller.stopButton();              
                stopped = true;
                playing = false;
                updatePlayButton(button_6);
            }
        });

        final JButton button_8 = new JButton(" << ");
        button_8.setFont(new Font("Droid Sans", Font.BOLD, 11));
        button_8.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller.prevTrack();               
            }
        });

        final JButton button_9 = new JButton(" >> ");
        button_9.setFont(new Font("Droid Sans", Font.BOLD, 11));
        button_9.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller.nextTrack();           
            }
        });

        final JLabel volumeLabel = new JLabel(" volume ");
        final JSlider volume = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 100);
        volume.setPreferredSize(new Dimension(150, 20));
        volumeLabel.setFont(new Font("Droid Sans", Font.PLAIN, 11));
        volume.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                
                controller.setVolumeButton((double)volume.getValue() / 100);                
            }
        });
        
        /*linear and shuffle mode buttons*/
        
        final JButton bShuffle = new JButton("Shuffle");
        bShuffle.setFont(new Font("Droid Sans", Font.PLAIN, 9));
        bShuffle.setEnabled(false);;
        bShuffle.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller.setShuffleMode();
            }
        });
  
        final JButton bLinear = new JButton("Linear");
        bLinear.setFont(new Font("Droid Sans", Font.PLAIN, 9));
        bLinear.setEnabled(true);
        bLinear.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller.linearMode();
                bShuffle.setEnabled(true);

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

        /*left buttons*/

        final JButton button = new JButton("All Songs");
        setLeftButtons(button);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                list = new JList<>(new Vector<>(controller.showAllSong()));
                list.setSelectedIndex(0);
                songName = list.getModel().getElementAt(list.getMaxSelectionIndex());

                list.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {}                    

                    @Override
                    public void mousePressed(MouseEvent e) {

                        JPopupMenu menu = buildStandardPopup(button);
                        if(e.isPopupTrigger()) {
                            menu.show(e.getComponent(), e.getX(), e.getY());
                            songName = list.getModel().getElementAt(list.getMaxSelectionIndex());
                        }                       
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {}

                    @Override
                    public void mouseEntered(MouseEvent e) {}

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if(e.getClickCount() == 2) {
                            
                            controller.addSongInReproductionPlaylist(list.getModel()
                                    .getElementAt(list.getMaxSelectionIndex()), REPRODUCE.NOW);
                            Agent agent = new Agent(seek);
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
        });

        final JButton button_1 = new JButton("Albums");
        setLeftButtons(button_1);
        button_1.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                list = new JList<>(new Vector<>(controller.showAllAlbum()));
                createSelectableList();
            }
        });

        final JButton button_2 = new JButton("Artists");
        setLeftButtons(button_2);
        button_2.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                list = new JList<>(new Vector<>(controller.showAllArtist()));
                createSelectableList();
            }
        });

        final JButton button_3 = new JButton("Yuor Playlists");
        setLeftButtons(button_3);
        
        button_3.addActionListener(new ActionListener() {           
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                list = new JList<>(new Vector<>(controller.showAllPlaylist()));
                createSelectableList();
                
                list.addMouseListener(new MouseListener() {
                    
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                        
                        JPopupMenu menu = buildStandardPopup(button_3);
                        if(e.isPopupTrigger()) {
                            menu.show(e.getComponent(), e.getX(), e.getY());
                            songName = list.getModel().getElementAt(list.getMaxSelectionIndex());
                        }               
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {}
                    
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        
                        if(e.getClickCount() == 2) {
                            
                            list = new JList<>(new Vector<>(controller.showPlaylistSong(list.getModel()
                                    .getElementAt(list.getMaxSelectionIndex()))));
                            createSelectableList();
                            
                            list.addMouseListener(new MouseListener() {

                                @Override
                                public void mouseReleased(MouseEvent e) {}                    

                                @Override
                                public void mousePressed(MouseEvent e) {

                                    JPopupMenu menu = buildStandardPopup(button);
                                    if(e.isPopupTrigger()) {
                                        menu.show(e.getComponent(), e.getX(), e.getY());
                                    }                       
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {}

                                @Override
                                public void mouseEntered(MouseEvent e) {}

                                @Override
                                public void mouseClicked(MouseEvent e) {

                                    if(e.getClickCount() == 2) {
                                        
                                        controller.addSongInReproductionPlaylist(list.getModel()
                                                .getElementAt(list.getMaxSelectionIndex()), REPRODUCE.NOW);
                                        Agent agent = new Agent(seek);
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

        final JButton button_4 = new JButton("Music Genre");
        setLeftButtons(button_4);
        button_4.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                list = new JList<>(new Vector<>(controller.showAllGenre()));
                createSelectableList();
            }
        });

        final JButton button_5 = new JButton("Più ascoltati");
        setLeftButtons(button_5);
        button_5.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                //list = new JList<>(new Vector<>(controller.showFavorites()));
                //createSelectableList();
            }
        });

        final JButton buttonQueue = new JButton("In riproduzione");
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

                        JPopupMenu menu = buildStandardPopup(buttonQueue);
                        if(e.isPopupTrigger()) {
                            menu.show(e.getComponent(), e.getX(), e.getY());
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
        menuFile.setFont(new Font("SansSerif", Font.PLAIN, 11));
        menuBar.add(menuFile);
        final JMenu menuHelp = new JMenu("Info");
        menuHelp.setFont(new Font("SansSerif", Font.PLAIN, 11));
        menuBar.add(menuHelp);

        /* choice menu(JMenuItem) */

        final JMenuItem menuChoiceImport = new JMenuItem("Add file to Library");
        menuChoiceImport.setFont(new Font("Dialog", Font.PLAIN, 11));
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

        final JMenuItem menuChoiceOpen = new JMenuItem("Add to reproduction list");
        menuChoiceOpen.setFont(new Font("Dialog", Font.PLAIN, 11));
        menuChoiceOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller.addSongInReproductionPlaylist(list.getModel()
                        .getElementAt(list.getMaxSelectionIndex()), REPRODUCE.AFTER);
            }
        });
        menuFile.add(menuChoiceOpen);
        
        final JMenuItem menuCreatePlaylist = new JMenuItem("Create new Playlist");
        menuCreatePlaylist.setFont(new Font("Dialog", Font.PLAIN, 11));
        menuCreatePlaylist.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                final JFrame frame2 = new JFrame("Your Playlist");
                final JPanel panel = new JPanel();
                final JLabel label = new JLabel("Insert playlist name  ");
                label.setFont(new Font("Dialog", Font.PLAIN, 12));
                final JTextArea area = new JTextArea(1, 10);
                final JButton button = new JButton("ok");
                button.setBackground(new Color(200, 200, 255));
                button.setFont(new Font("Dialog", Font.BOLD, 12));
                button.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                        controller.newPlaylistFile(area.getText());
                        frame2.dispose();
                    }
                });
                
                panel.add(label);
                panel.add(area);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(button);
                frame2.add(panel);
                frame2.setSize(400, 40);
                frame2.setVisible(true);
            }
        });
        menuFile.add(menuCreatePlaylist);

        final JMenuItem menuChoiceExit = new JMenuItem("Exit Program");
        menuChoiceExit.setFont(new Font("Dialog", Font.PLAIN, 11));
        menuChoiceExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuFile.add(menuChoiceExit);

        final JMenuItem menuChoiceInfo = new JMenuItem("Info Beesound");
        menuChoiceInfo.setFont(new Font("Dialog", Font.PLAIN, 11));
        menuHelp.add(menuChoiceInfo);
        menuChoiceInfo.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.out.println("aosimdcoasdmioc");
                final JFrame infoFrame = new JFrame("Beesound members");
                JTextArea textArea = new JTextArea("\nThis Program has been realized by: \n"
                        + "\nTiziano De Cristofaro : model\n"
                        + "Carlo Alberto Scola: controller\n"
                        + "Gianluca Cincinelli: view\n");
                textArea.setFont(new Font("Dialog", Font.BOLD, 13));
                textArea.setForeground(new Color(20, 40, 150));
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                infoFrame.add(textArea);
                infoFrame.setSize(300, 120);
                infoFrame.setVisible(true);
            }
        });

        /* adding components */
        
        landingPanel.add(listSelectionPanel, BorderLayout.CENTER);                  
        landingPanel.add(leftButtonsPanel, BorderLayout.WEST);
        landingPanel.add(playerButtonsPanel, BorderLayout.SOUTH);
        landingPanel.add(rightPanel, BorderLayout.EAST);
        
        frame.getContentPane().add(landingPanel);
        frame.setVisible(true);        
    }
    
///////////////////////////  PRIVATE METHODS  ///////////////////////////////////
    
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
        button.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
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
    private JPopupMenu buildStandardPopup(JButton button) {
        
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
                
                final JFrame frame2 = new JFrame("Your Playlist");
                final JPanel panel = new JPanel();
                final JLabel label = new JLabel("Select a playlist");
                label.setFont(new Font("Dialog", Font.PLAIN, 12));
                final JComboBox<String> combo = new JComboBox<>(array);
                combo.setPreferredSize(new Dimension(100, 20));
                final JButton button = new JButton("ok");
                button.setBackground(new Color(200, 200, 255));
                button.setFont(new Font("Dialog", Font.BOLD, 12));
                button.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                        controller.addSongInPlaylist(songName, combo.getSelectedItem().toString());
                        frame2.dispose();
                    }
                });
                
                panel.add(label);
                panel.add(combo);
                panel.add(button);
                frame2.add(panel);
                frame2.setSize(350, 65);
                frame2.setVisible(true);                
            }
        });
        
        menu.add(itemAddToPlaylist);
        menu.add(itemRemovePlaylist);
        menu.add(itemRemoveFromLibrary);
        menu.add(itemAddToReproductionList);
        menu.add(itemRemoveFromReproductionList);

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

}