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

    public GUI() {

        /*THE FRAME*/

        frame = new JFrame("BeeSound Player");
        frame.setFont(new Font("Trajan Pro", Font.PLAIN, 12));
        final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = (int)(dimension.getWidth() * PERC_HALF - dimension.getWidth() * PERC_QUATER);
        final int y = (int)(dimension.getHeight() * PERC_HALF - dimension.getHeight() * PERC_QUATER);
        frame.setSize((int) (dimension.getWidth() * 0.5), (int) (dimension.getHeight() * 0.5));
        frame.setLocation(x, y);                
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           
        frame.setResizable(false);
        frame.setMinimumSize(new Dimension(200, 100));

        /*THE PRINCIPAL BORDERLAYOUT PANEL*/

        final JPanel landingPanel = new JPanel();
        final BorderLayout landingLayout = new BorderLayout();
        landingPanel.setLayout(landingLayout);

        /*LEFT PANEL & BUTTONS*/
        /*panel for left buttons*/

        final JPanel leftButtonsPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        leftButtonsPanel.setPreferredSize(new Dimension(85, 0));

        /*SOUTH PANEL: CONTROL PLAYER'S BUTTONS*/

        final FlowLayout playerButtonsLayout = new FlowLayout();
        final JPanel playerButtonsPanel = new JPanel(playerButtonsLayout);

        final JButton button_6 = new JButton(" ▶ ");
        button_6.setForeground(new Color(0, 128, 0));
        button_6.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (stopped) {
                    controller.addSongInReproductionPlaylist(list.getModel()
                            .getElementAt(list.getMaxSelectionIndex()), REPRODUCE.NOW);
                    Agent agent = new Agent(seek);
                    agent.start();
                    playing = true;
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
        button_7.setForeground(new Color(0, 0, 0));
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
        button_8.setFont(new Font("Tahoma", Font.BOLD, 11));
        button_8.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller.prevTrack();               
            }
        });

        final JButton button_9 = new JButton(" >> ");
        button_9.setFont(new Font("Tahoma", Font.BOLD, 11));
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
        bShuffle.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller.setShuffleMode();                
            }
        });
        
        final JButton bLinear = new JButton("Linear");
        bLinear.setFont(new Font("Droid Sans", Font.PLAIN, 9));
        bLinear.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                controller.linearMode();                
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

        /*left buttons*/

        final JButton button = new JButton("All Songs");
        button.setBorder(null);
        button.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
        button.setBackground(new Color(255, 255, 204));              
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

                        JPopupMenu menu = buildStandardPopup();
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
                            agent.start();
                            playing = true;
                            stopped = false;
                            updatePlayButton(button_6);
                        }                        
                    }
                });                
                createSelectableList();
            }
        });

        final JButton button_1 = new JButton("Albums");
        button_1.setBorder(null);
        button_1.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
        button_1.setBackground(new Color(255, 255, 153));
        button_1.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                list = new JList<>(new Vector<>(controller.showAllAlbum()));
                createSelectableList();
            }
        });

        final JButton button_2 = new JButton("Artists");
        button_2.setBorder(null);
        button_2.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
        button_2.setBackground(new Color(255, 255, 102));
        button_2.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                list = new JList<>(new Vector<>(controller.showAllArtist()));
                createSelectableList();
            }
        });

        final JButton button_3 = new JButton("Yuor Playlists");
        button_3.setBorder(null);
        button_3.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
        button_3.setBackground(new Color(255, 255, 51));
        button_3.addActionListener(new ActionListener() {                                       
            @Override
            public void actionPerformed(ActionEvent e) {
                
                list = new JList<>(new Vector<>(controller.showAllPlaylist()));
                createSelectableList();
                
                list.addMouseListener(new MouseListener() {
                    
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    
                    @Override
                    public void mousePressed(MouseEvent e) {}
                    
                    @Override
                    public void mouseExited(MouseEvent e) {}
                    
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(e.getClickCount() == 2) {
                            list = new JList<>(new Vector<>(controller.showAllSong()));
                            list.addMouseListener(new MouseListener() {

                                @Override
                                public void mouseReleased(MouseEvent e) {}                    

                                @Override
                                public void mousePressed(MouseEvent e) {

                                    JPopupMenu menu = buildStandardPopup();
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
                                        agent.start();
                                        playing = true;
                                        stopped = false;
                                        updatePlayButton(button_6);
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
        button_4.setBorder(null);
        button_4.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
        button_4.setBackground(new Color(204, 255, 255));
        button_4.addActionListener(new ActionListener() {                                       
            @Override
            public void actionPerformed(ActionEvent e) {
                list = new JList<>(new Vector<>(controller.showAllGenre()));
                createSelectableList();
            }
        });

        final JButton button_5 = new JButton("Più ascoltati");
        button_5.setBorder(null);
        button_5.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
        button_5.setBackground(new Color(153, 255, 255));
        button_5.addActionListener(new ActionListener() {                                       
            @Override
            public void actionPerformed(ActionEvent e) {
                //list = new JList<>(new Vector<>(controller.showCurrentPlaylist()));
                createSelectableList();
            }
        });

        final JButton buttonQueue = new JButton("In riproduzione");
        buttonQueue.setBorder(null);
        buttonQueue.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
        buttonQueue.setBackground(new Color(102, 255, 255));
        buttonQueue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                list = new JList<>(new Vector<>(controller.showReproductionPlaylist()));
                list.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {}                    

                    @Override
                    public void mousePressed(MouseEvent e) {

                        JPopupMenu menu = buildStandardPopup();
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

        /*CENTER PANEL: LIST SELECTION & INFO LABEL*/

        final JPanel listSelectionPanel = new JPanel();
        listSelectionPanel.setLayout(new BoxLayout(listSelectionPanel, BoxLayout.Y_AXIS));                  

        /*information panel about list selection*/

        final JPanel counterPanel = new JPanel();
        counterPanel.setBackground(Color.DARK_GRAY);
        counterPanel.setMaximumSize(new Dimension(32767, 30));
        final JLabel counterLabel = new JLabel("Numero brani + minutaggio: ");
        counterLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
        counterLabel.setForeground(new Color(51, 204, 51));

        listSelectionPanel.add(scrollPane);
        listSelectionPanel.add(counterPanel);                               
        counterPanel.add(counterLabel);

        /*RIGHT PANEL FOR IMAGE AND INFO CURRENT SONG*/

        final JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(153, 204, 102));
        rightPanel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        seek.setMaximumSize(new Dimension(280, 20));
        rightPanel.add(seek);

        /*album image*/

        final URL ImgURL = UIResource.class.getResource("/zutons.jpg");
        final JLabel imageLabel = new JLabel(new ImageIcon(ImgURL));            
        imageLabel.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.46), 0));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        /*label with current song's info*/

        final JLabel currentSongInfo = new JLabel("Current Song's Info");
        currentSongInfo.setFont(new Font("Dialog", Font.PLAIN, 11));
        currentSongInfo.setBackground(Color.WHITE);
        currentSongInfo.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.46), (int)(frame.getHeight() * 0.3)));
        currentSongInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(imageLabel);     
        rightPanel.add(currentSongInfo);

        /*TOP MENU*/

        final JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(null);
        frame.setJMenuBar(menuBar);  

        /*jmenu buttons: file, help. Jmenu can contains jmenu items*/

        final JMenu menuFile = new JMenu("File");
        menuFile.setFont(new Font("SansSerif", Font.PLAIN, 11));
        menuBar.add(menuFile);
        final JMenu menuHelp = new JMenu("Info");
        menuHelp.setFont(new Font("SansSerif", Font.PLAIN, 11));
        menuBar.add(menuHelp);

        /*add choice menu(JMenuItem)*/

        final JMenuItem menuChoiceImport = new JMenuItem("Add file to Library");
        menuChoiceImport.setFont(new Font("Dialog", Font.PLAIN, 11));
        menuChoiceImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = chooser.showOpenDialog(menuFile);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    controller.addSong(chooser.getSelectedFile().getAbsolutePath());
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

        /*adding components*/
        landingPanel.add(listSelectionPanel, BorderLayout.CENTER);                  
        landingPanel.add(leftButtonsPanel, BorderLayout.WEST);
        landingPanel.add(playerButtonsPanel, BorderLayout.SOUTH);
        landingPanel.add(rightPanel, BorderLayout.EAST);
        frame.getContentPane().add(landingPanel);
        frame.setVisible(true);        
    }

    /*creation of the list of strings to show into listSelectionPanel*/
    private void createSelectableList() {
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Droid Sans", Font.PLAIN, 11));
        scrollPane.setViewportView(list);
    }

    public int getSelectedIndex() {
        return this.list.getMaxSelectionIndex();
    }

    @Override
    public void setObserver(ViewObserver observer) {
        this.controller = observer;
    }

    private void updatePlayButton(JButton button) {
        if (playing) {
            button.setText(" || ");
        }
        else {
            button.setText(" ▶ ");
        }        
    }

    private JPopupMenu buildStandardPopup() {
        final JPopupMenu menu = new JPopupMenu();
        final JMenuItem itemAddToReproductionList = new JMenuItem("Add to reproduction Playlist");
        itemAddToReproductionList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addSongInReproductionPlaylist(list.getModel()
                        .getElementAt(list.getMaxSelectionIndex()), REPRODUCE.AFTER);
            }
        });
        final JMenuItem itemRemoveFromReproductionList = new JMenuItem("Remove from reproduction Playlist");
        itemRemoveFromReproductionList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeSongFromQueue(list.getModel()
                        .getElementAt(list.getMaxSelectionIndex()));                       
            }
        });   
        final JMenuItem itemRemoveFromLibrary = new JMenuItem("Remove from Library");
        itemRemoveFromLibrary.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeSong(list.getModel().getElementAt(list.getMaxSelectionIndex()));
            }
        });
        
        final JMenuItem itemAddToPlaylist = new JMenuItem("Add song to Playlist");
        
        itemAddToPlaylist.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                list = new JList<>(new Vector<>(controller.showAllPlaylist()));
                createSelectableList();
                String[] array = new String[list.getModel().getSize()];
                
                for(int i = 0; i < array.length; i++ ) {
                    
                    array[i] = (String)(list.getModel().getElementAt(i));
                    //System.out.println("array: " + array[i]);
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
                        //System.out.println(songName);
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
        menu.add(itemRemoveFromLibrary);
        menu.add(itemAddToReproductionList);
        menu.add(itemRemoveFromReproductionList);

        return menu;
    }
    
    private class Agent extends Thread {
        
        private JSlider seek;
        private volatile boolean running = true;
        
        public Agent(JSlider seek) {
            this.seek = seek;
            this.seek.setValue(0);
        }
            
        @Override
        public void run() {
                        
            while(seek.getValue() < 100 && running == true) {
                
                try {
                   SwingUtilities.invokeAndWait(new Runnable() {
                       
                       @Override
                       public void run() {
                           
                           try {
                               Thread.sleep(200);
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