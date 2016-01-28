package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.List;
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
import javax.swing.JTextField;
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
    private JList<String> songList;
    private JList<Integer> nFavorites;
    private JList<Object> songInfo;
    private boolean playing = false;
    private boolean stopped = true;
    private String selectedSongName;
    private String selectedPlaylistName;
    private JSlider seekBar = new JSlider(SwingConstants.HORIZONTAL, 0,100,0);
    private Integer deltaColor = 0;
    private Integer[] infoLibraryArray = new Integer[3];
    final JLabel lbInfoLibrary = new JLabel("Numero brani + minutaggio: ");
    final JButton btAllSongs = new JButton("All Songs");
    final JSlider slVol = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
    final List searchList = new List();
    private Agent agent;
    
    public GUI() {
        
        seekBar.setDoubleBuffered(true);
        seekBar.setValueIsAdjusting(true);
        
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
        
        ////////////// RIGHT PANEL FOR IMAGE AND INFO CURRENT SONG ///////////////////

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
        pnRight.add(this.seekBar);
        
        //SeekBar
        this.seekBar.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JSlider source = (JSlider)e.getSource();
                seekBar.setValueIsAdjusting(false);
                seek((int)source.getValue());
            }
            @Override
            public void mousePressed(MouseEvent e) {
                seekBar.setValueIsAdjusting(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {               
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                JSlider source = (JSlider)e.getSource();
                seekBar.setValueIsAdjusting(false);
                seekBar.setValue((int)source.getValue());
                seek((int)source.getValue());
            }
        });
        
        //pnRight.add(seekBar);
        pnRight.add(lbImage);     
        pnRight.add(lbInfoCurrent);

        ////////////// CENTER PANEL: LIST SELECTION & INFO LABEL ////////////////////////
        
        final JPanel pnListView = new JPanel();
        pnListView.setLayout(new BoxLayout(pnListView, BoxLayout.Y_AXIS));
        final JPanel pnInfoLibrary = new JPanel();
        pnInfoLibrary.setMaximumSize(new Dimension(32767, 30));
        pnInfoLibrary.setBackground(new Color(100, 100, 255));                

        pnListView.add(scrollPane);
        pnListView.add(pnInfoLibrary);                               
        pnInfoLibrary.add(lbInfoLibrary, FlowLayout.LEFT);

        ////////////// SOUTH PANEL: CONTROL PLAYER'S BUTTONS ////////////////////////

        final JPanel pnPlayerButtons = new JPanel(new FlowLayout());        
        final JButton btPlay = new JButton(" ▶ ");
        final JButton btStop = new JButton(" ■ ");
        final JButton btPrev = new JButton(" << ");
        final JButton btNext = new JButton(" >> ");
        final JButton btShuffle = new JButton("Shuffle");
        final JButton btLinear = new JButton("Linear");       
        final JLabel lbVol = new JLabel(" volume ");
        
        // PLAY
        btPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (stopped) {
                    
                   /* agent.interrupt();
                    agent.start();*/
                    
                    if(songList.getModel().getSize()==0)return;
                    controller.addSongInReproductionPlaylist(songList.getModel()
                            .getElementAt(songList.getMaxSelectionIndex()), REPRODUCE.NOW);
                    playing = true;
                    setVolume();

                    updateProgressBar(PROGRESS_BAR.ACTIVE);
                    
                    setInfoLabel(lbInfoCurrent, controller.getCurrentSongInfo());
                }
                else {
                    controller.pauseButton();
                   /* agent.interrupt();*/
                    updateProgressBar(PROGRESS_BAR.PAUSE);
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
                
                updateProgressBar(PROGRESS_BAR.PAUSE);
            }
        });
        
        //PREVIOUS
        btPrev.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.prevTrack();
                setVolume();
                setInfoLabel(lbInfoCurrent, controller.getCurrentSongInfo());
            }
        });
        
        //NEXT
        btNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.nextTrack();
                setVolume();
                setInfoLabel(lbInfoCurrent, controller.getCurrentSongInfo());
            }
        });
        
        //VOLUME
        slVol.setPreferredSize(new Dimension(150, 20));
        slVol.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                setVolume();
            }
        });
        
        //SHUFFLE MODE
        btShuffle.setEnabled(true);
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
        final JButton btAlbum = new JButton("Albums");
        final JButton btArtist = new JButton("Artists");
        final JButton btPlaylist = new JButton("Yuor Playlists");
        final JButton btGenre = new JButton("Music Genre");
        final JButton btFavorites = new JButton("Più ascoltati");
        final JButton btReproduction = new JButton("In riproduzione");
        
        //ALL SONGS
        setLeftButtons(btAllSongs);
        btAllSongs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                songList = new JList<>(new Vector<>(controller.showAllSong()));
                if(songList.getModel().getSize() > 0) {
                    songList.setSelectedIndex(0);
                    selectedSongName = songList.getModel().getElementAt(songList.getMaxSelectionIndex());
                }
                createSelectableList();
                songList.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseReleased(MouseEvent e) {}                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(songList.getModel().getSize() > 0) {
                            JPopupMenu menu = buildStandardPopup(btAllSongs, true, false, true, false, true, false, false, true);
                            if(e.isPopupTrigger()) {
                                menu.show(e.getComponent(), e.getX(), e.getY());
                                selectedSongName = songList.getModel().getElementAt(songList.getMaxSelectionIndex());
                            }
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(e.getClickCount() == 2 && songList.getModel().getSize() > 0) {
                            controller.addSongInReproductionPlaylist(songList.getModel()
                                    .getElementAt(songList.getMaxSelectionIndex()), REPRODUCE.NOW);
                            setVolume();
                            playing = true;
                            stopped = false;
                            updatePlayButton(btPlay);
                            setInfoLabel(lbInfoCurrent, controller.getCurrentSongInfo());
                            System.out.println("MAX: "+seekBar.getMaximum());    
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
                songList = new JList<>(new Vector<>(controller.showAllAlbum()));
                createSelectableList();
            }
        });
        
        //ARTISTS
        setLeftButtons(btArtist);
        btArtist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                songList = new JList<>(new Vector<>(controller.showAllArtist()));
                createSelectableList();
            }
        });
        
        //PLAYLIST
        setLeftButtons(btPlaylist);
        btPlaylist.addActionListener(new ActionListener() {           

            @Override
            public void actionPerformed(ActionEvent e) {
                songList = new JList<>(new Vector<>(controller.showAllPlaylist()));
                if(songList.getModel().getSize() > 0) {
                    songList.setSelectedIndex(0);
                }
                createSelectableList();
                songList.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mousePressed(MouseEvent e) {

                        if(songList.getModel().getSize() > 0) {
                            JPopupMenu menu = buildStandardPopup(btPlaylist, false, false, false, true, false, true, false, false);
                            menu.show(e.getComponent(), e.getX(), e.getY());
                            selectedSongName = songList.getModel().getElementAt(songList.getMaxSelectionIndex());
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(e.getClickCount() == 2 && songList.getModel().getSize() > 0) {                        
                            selectedPlaylistName = songList.getModel().getElementAt(songList.getMaxSelectionIndex());
                            System.out.println(songList.getModel().getElementAt(songList.getMaxSelectionIndex()));
                            songList = new JList<>(new Vector<>(controller.showPlaylistSong(songList.getModel().getElementAt(songList.getMaxSelectionIndex()))));
                            createSelectableList();
                            songList.addMouseListener(new MouseListener() {

                                @Override
                                public void mouseReleased(MouseEvent e) {}
                                
                                @Override
                                public void mousePressed(MouseEvent e) {
                                    if(songList.getModel().getSize() > 0) {
                                        JPopupMenu menu = buildStandardPopup(btPlaylist, true, false, false, false, false, false, true, true);
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
                                    if(e.getClickCount() == 2 && songList.getModel().getSize() > 0) {
                                        controller.addSongInReproductionPlaylist(songList.getModel()
                                                .getElementAt(songList.getMaxSelectionIndex()), REPRODUCE.NOW);
                                        setVolume();
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
                songList = new JList<>(new Vector<>(controller.showAllGenre()));
                createSelectableList();
            }
        });
        
        //FAVORITES
        setLeftButtons(btFavorites);
        btFavorites.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                songList = new JList<>(new Vector<>(controller.showFavorites().keySet()));
                nFavorites = new JList<>(new Vector<>(controller.showFavorites().values()));
                createFavoritesList();
            }
        });
        
        //REPRODUCTION QUEUE
        setLeftButtons(btReproduction);
        btReproduction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                songList = new JList<>(new Vector<>(controller.showReproductionPlaylist()));
                songList.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(songList.getModel().getSize() > 0) {
                            JPopupMenu menu = buildStandardPopup(btReproduction, false, true, false, false, false, false, false, false);
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

        pnLeftButtons.add(btAllSongs);
        pnLeftButtons.add(btAlbum);
        pnLeftButtons.add(btArtist);
        pnLeftButtons.add(btPlaylist);
        pnLeftButtons.add(btGenre);
        pnLeftButtons.add(btFavorites);
        pnLeftButtons.add(btReproduction);

        /////////////////////  TOP MENU ////////////////////

        final JMenuBar mnBar = new JMenuBar();
        mnBar.setBorder(null);
        final JMenu mnFile = new JMenu("File");
        final JMenu mnInfo = new JMenu("Info");
        final JMenuItem mniAddToLib = new JMenuItem("Add file to Library");
        final JMenuItem mniCreatePlaylist = new JMenuItem("Create new Playlist");
        final JMenuItem mniExit = new JMenuItem("Exit Program");
        final JMenuItem mniBeeInfo = new JMenuItem("Info Beesound");
        final JTextField tfSearchBar = new JTextField("search", 5);
        
        //ADD MP3 LIBRARY
        mniAddToLib.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = chooser.showOpenDialog(mnFile);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    controller.addSong(chooser.getSelectedFile().getAbsolutePath());
                    btAllSongs.doClick();
                }
            }
        });
        
        //CREATE PLAYLIST
        mniCreatePlaylist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                final JFrame frChoosePlaylist = new JFrame("Create your Playlist");
                final JPanel pnChoosePlaylist = new JPanel();
                final JLabel lbChoosePlaylist = new JLabel("Insert playlist name  ");
                final JTextField tfChoosePlaylist = new JTextField(10);
                final JButton btChoosePlaylist = new JButton("ok");
                btChoosePlaylist.setBackground(new Color(200, 200, 255));
                btChoosePlaylist.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.newPlaylistFile(tfChoosePlaylist.getText());
                        frChoosePlaylist.dispose();
                    }
                });

                pnChoosePlaylist.add(lbChoosePlaylist);
                pnChoosePlaylist.add(tfChoosePlaylist);
                pnChoosePlaylist.add(Box.createRigidArea(new Dimension(5, 0)));
                pnChoosePlaylist.add(btChoosePlaylist);
                frChoosePlaylist.add(pnChoosePlaylist);
                frChoosePlaylist.setSize(new Dimension(400, 60));
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
        
        //SEARCH BAR
        final JButton btSearch = new JButton(" Go ");
        tfSearchBar.setBackground(new Color(230, 255, 230));
        tfSearchBar.select(0, tfSearchBar.getText().length());
        btSearch.setBackground(new Color(100, 220, 100));
        btSearch.setBorder(null);
        btSearch.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                songList = new JList<>(new Vector<>(controller.searchSong(tfSearchBar.getText())));                
                if(songList.getModel().getSize() > 0) {
                    songList.setSelectedIndex(0);
                    selectedSongName = songList.getModel().getElementAt(songList.getMaxSelectionIndex());
                }
                createSelectableList();
                songList.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseReleased(MouseEvent e) {}                    
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(songList.getModel().getSize() > 0) {
                            JPopupMenu menu = buildStandardPopup(btAllSongs, true, false, true, false, true, false, false, true);
                            if(e.isPopupTrigger()) {
                                menu.show(e.getComponent(), e.getX(), e.getY());
                                selectedSongName = songList.getModel().getElementAt(songList.getMaxSelectionIndex());
                            }
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(e.getClickCount() == 2 && songList.getModel().getSize() > 0) {
                            controller.addSongInReproductionPlaylist(songList.getModel()
                                    .getElementAt(songList.getMaxSelectionIndex()), REPRODUCE.NOW);
                            setVolume();
                            playing = true;
                            stopped = false;
                            updatePlayButton(btPlay);
                            setInfoLabel(lbInfoCurrent, controller.getCurrentSongInfo());
                        }                        
                    }
                });                

            }
        });

        //ARRAY WITH ALL COMPONENTS
        final Component[] compArray = new Component[]{mniExit, mniAddToLib, mniCreatePlaylist, lbVol, btSearch
                , mniBeeInfo, mnFile, mnInfo, mnBar, btLinear, btShuffle, btAllSongs, btAlbum, btArtist
                , btPlaylist, btGenre, btFavorites, btPrev, btNext, btReproduction, lbInfoLibrary, pnInfoLibrary, lbInfoCurrent};
        setComponentFont(compArray);
        
        mnFile.add(mniAddToLib);
        mnFile.add(mniCreatePlaylist);
        mnFile.add(mniExit);
        mnInfo.add(mniBeeInfo);
        mnBar.add(mnFile);
        mnBar.add(mnInfo);
        mnBar.add(Box.createRigidArea(new Dimension((int)(frame.getWidth() * 0.5), 0)));
        mnBar.add(tfSearchBar);
        mnBar.add(Box.createRigidArea(new Dimension(10, 0)));
        mnBar.add(btSearch);
        mnBar.add(Box.createRigidArea(new Dimension(10, 0)));
        pnLanding.add(pnListView, BorderLayout.CENTER);                  
        pnLanding.add(pnLeftButtons, BorderLayout.WEST);
        pnLanding.add(pnPlayerButtons, BorderLayout.SOUTH);
        pnLanding.add(pnRight, BorderLayout.EAST);
        frame.setJMenuBar(mnBar);  
        frame.getContentPane().add(pnLanding);
    }   

    ///////////////////////////  PRIVATE METHODS  ///////////////////////////////////
    
    /**
     * Sets volume for all song's reproduction
     */
    private void setVolume() {       
        controller.setVolumeButton((double)slVol.getValue() / 100);
    }

    private void seek(int n){
        controller.skipTo(n);
    }
    
    @Override
    public void setVisible(final boolean visible) {        
        this.frame.setVisible(visible);
    }

    /**
     * Refresh all components
     */
    @Override
    public void refreshView() {      
        btAllSongs.doClick();
        controller.showLibraryInfo().toArray(infoLibraryArray);
        lbInfoLibrary.setText("Total song: " + infoLibraryArray[0] + "          Total time: " + infoLibraryArray[1] + ":"
                + infoLibraryArray[2] % 60);
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
     * Set lable's text to show title and song duration
     * @param label
     * @param Map
     */
    private void setInfoLabel(JLabel label, Map<String, Object> map) {
        Duration duration = (Duration)(map.get("duration"));
        label.setText(map.get("title") + " - " + duration.getMin() + ":" + duration.getSec());
        this.seekBar.setMaximum((Integer)(this.controller.getCurrentSongInfo().get("size")));
        this.seekBar.setValue(0);
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
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.setFont(new Font("Droid Sans", Font.PLAIN, 11));
        songList.setSelectionInterval(0, 0);
        scrollPane.setViewportView(songList);
    }
    
    private void createFavoritesList() {
        scrollPane.setViewportView(songList); 
        scrollPane.setViewportView(nFavorites);

    }
    
    public void setHighlighted(int index) {
        songList.setSelectionInterval(index, index);
    }

    /**
     * Return the actual list selection index
     * @return
     */
    public int getSelectedIndex() {
        return this.songList.getMaxSelectionIndex();
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
            , boolean remPlay, boolean addPlay, boolean playPlay, boolean remFromPlay, boolean songDet) {

        final JPopupMenu menu = new JPopupMenu();
        final JMenuItem itemAddToReproductionList = new JMenuItem("Add to reproduction Playlist");
        itemAddToReproductionList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                controller.addSongInReproductionPlaylist(songList.getModel()
                        .getElementAt(songList.getMaxSelectionIndex()), REPRODUCE.AFTER);
                setVolume();
                button.doClick();
            }
        });
        
        final JMenuItem itemRemoveFromReproductionList = new JMenuItem("Remove from reproduction Playlist");
        itemRemoveFromReproductionList.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeSongFromQueue(songList.getModel()
                        .getElementAt(songList.getMaxSelectionIndex()));              
                button.doClick();
            }

        });
        
        final JMenuItem itemRemoveFromLibrary = new JMenuItem("Remove from Library");
        itemRemoveFromLibrary.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeSong(songList.getModel().getElementAt(songList.getMaxSelectionIndex()));
                button.doClick();
            }
        });

        final JMenuItem itemRemovePlaylist = new JMenuItem("Remove playlist");
        itemRemovePlaylist.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removePlaylist(songList.getModel().getElementAt(songList.getMaxSelectionIndex()));
                button.doClick();
            }
        });

        final JMenuItem itemAddToPlaylist = new JMenuItem("Add song to Playlist");     
        itemAddToPlaylist.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                songList = new JList<>(new Vector<>(controller.showAllPlaylist()));
                String[] array = new String[songList.getModel().getSize()];
                for(int i = 0; i < array.length; i++ ) {
                    array[i] = (String)(songList.getModel().getElementAt(i));
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
                controller.playPlaylist(songList.getModel().getElementAt(songList.getMaxSelectionIndex()));
                setVolume();
                button.doClick();
            }
        });
        
        final JMenuItem itemRemFromPlaylist = new JMenuItem("Remove from this playplist");
        itemRemFromPlaylist.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeSongFromPlaylist(songList.getModel().getElementAt(songList.getMaxSelectionIndex()), selectedPlaylistName);
                button.doClick();
            }
        });
        
        final JMenuItem itemSongInfo = new JMenuItem("Show song info");
        itemSongInfo.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFrame frSongInfo = new JFrame("Song details ");
                final JPanel pnSongInfo = new JPanel(new GridLayout(controller.showSongInfo(0).size(), 0, 0, 1));
                System.out.println(controller.showSongInfo(0).size());
                for (Map.Entry<String, Object> entry : controller.showSongInfo(songList.getSelectedIndex()).entrySet()) {
                    pnSongInfo.add(new JTextField(" " + entry.getKey() + ": " + entry.getValue()));
                }
                frSongInfo.add(pnSongInfo);
                frSongInfo.setSize(500, 320);
                frSongInfo.setResizable(false);
                frSongInfo.setLocationRelativeTo(frame);
                frSongInfo.setVisible(true);
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
        if(songDet) {
            menu.add(itemSongInfo);
        }
        
        return menu;
    }    
    
    class Agent extends Thread{
        private volatile boolean stopped = false;

        public void run(){
           this.stopped=false;
           System.out.println("CIAO STRONZO sono entrato!!");
                while(!stopped){
                    try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
                                public void run() {
                                    seekBar.setValue(controller.getPos());  
                                    frame.repaint();
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                }
                System.out.println("ESCO DAL THREAD");
        }
        
        public void setStopped(final boolean value){
            this.stopped = value;
        }
    }
    
    @Override
    public void updateProgressBar(PROGRESS_BAR val){
        if(val==PROGRESS_BAR.ACTIVE){
        if(agent!=null){
            agent.setStopped(true);
            agent.interrupt();
        }
        agent = new Agent();
        agent.start();
        } else if (val==PROGRESS_BAR.PAUSE) {
            agent.setStopped(true);
            agent.interrupt();
        }
    }
    public enum PROGRESS_BAR{
        PAUSE,ACTIVE;
    }
}