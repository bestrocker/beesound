package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import Controller.ViewObserver;

import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Font;
import javax.swing.JMenuItem;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer.UIResource;

public class GUI implements ViewInterface{
        
    private static final double PERC_HALF = 0.5;
    private static final double PERC_QUATER = 0.25;
    private ViewObserver controller;   
    private final JFrame frame;
    private final JScrollPane scrollPane = new JScrollPane();
    private JFileChooser chooser = new JFileChooser(); 
    private JList<String> list;
    
        /*GUI constructor*/
        public GUI() {
        	
	            //controller.addSong(System.getProperty("user.home" + "rubber_bullets.mp3"));
	            
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

	            /*left buttons*/
	            
	            final JButton button = new JButton("All Songs");
	            button.setBorder(null);
	            button.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
	            button.setBackground(new Color(255, 255, 204));              
	            button.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
			            list = new JList<>(new Vector<>(controller.showAllSong()));
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
	//		            list = new JList<>(controller.getNamesFromLibrary(controller.getAlbumTitles()));
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
//			            list = new JList<>(controller.getNamesFromLibrary(controller.getArtistNames()));
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
	//		            list = new JList<>(controller.getNamesFromLibrary(controller.getPlaylistNames()));
			            createSelectableList();
					}
            	});
            	
	            final JButton button_4 = new JButton("Music Genre");
	            button_4.setBorder(null);
	            button_4.setFont(new Font("Trajan Pro", Font.PLAIN, 11));
	            button_4.setBackground(new Color(204, 255, 255));
            	button_4.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
//			            list = new JList<>(controller.getNamesFromLibrary(controller.getGenreNames()));
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
	//		            list = new JList<>(controller.getNamesFromLibrary(controller.getSongTitles())); //this button needs a method on library
			            createSelectableList();
					}
            	});
	            
	            leftButtonsPanel.add(button);
	            leftButtonsPanel.add(button_1);
	            leftButtonsPanel.add(button_2);
	            leftButtonsPanel.add(button_3);
	            leftButtonsPanel.add(button_4);
	            leftButtonsPanel.add(button_5);
	            
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
	            rightPanel.setFont(new Font("SansSerif", Font.PLAIN, 12));
	            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));	   
	            
	            /*album image*/
	            
	            final URL ImgURL = UIResource.class.getResource("/zutons.jpg");
	            final JLabel imageLabel = new JLabel(new ImageIcon(ImgURL));            
	            imageLabel.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.5), 0));
	            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	            
	            /*label with current song's info*/
	            
	            final JLabel currentSongInfo = new JLabel("Current Song's Info");
	            currentSongInfo.setFont(new Font("Dialog", Font.PLAIN, 11));
	            currentSongInfo.setBackground(Color.WHITE);
	            currentSongInfo.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.5), (int)(frame.getHeight() * 0.3)));
	            currentSongInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
	            
	            rightPanel.add(imageLabel);	
	            rightPanel.add(currentSongInfo);   
                
                /*SOUTH PANEL: CONTROL PLAYER'S BUTTONS*/
	            
                final FlowLayout playerButtonsLayout = new FlowLayout();
                final JPanel playerButtonsPanel = new JPanel(playerButtonsLayout);
                
                final JButton button_6 = new JButton(" ► ");
                button_6.setForeground(new Color(0, 128, 0));
                button_6.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println(getSelectedIndex());
						controller.playButton();					
					}
				}); 
                
                final JButton button_7 = new JButton(" ■ ");
                button_7.setForeground(new Color(0, 0, 0));              
                final JButton button_8 = new JButton(" << ");
                button_8.setFont(new Font("Tahoma", Font.BOLD, 11));                         
                final JButton button_9 = new JButton(" >> ");
                button_9.setFont(new Font("Tahoma", Font.BOLD, 11));
                
                playerButtonsPanel.add(button_6);
                playerButtonsPanel.add(button_7);
                playerButtonsPanel.add(button_8);
                playerButtonsPanel.add(button_9);
 
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
                
                final JMenuItem menuChoiceImport = new JMenuItem("Import");
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
                
                final JMenuItem menuChoiceOpen = new JMenuItem("Open");
                menuChoiceOpen.setFont(new Font("Dialog", Font.PLAIN, 11));
                menuFile.add(menuChoiceOpen);    
                
                final JMenuItem menuChoiceExit = new JMenuItem("Exit");
                menuChoiceExit.setFont(new Font("Dialog", Font.PLAIN, 11));
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
        //	list.addListSelectionListener(new MyListSelectionHandler());
            list.setFont(new Font("DialogInput", Font.ITALIC, 11));
            scrollPane.setViewportView(list);
        }
        
        public int getSelectedIndex() {
        	return this.list.getMaxSelectionIndex();
        }
        
        @Override
        public void setObserver(ViewObserver observer) {
            this.controller = observer;
        }
        
        /*valuechanged is activated by making a selection on the list, and return the index of the selected element to update() method*/
        /*class MyListSelectionHandler implements ListSelectionListener {        	
            @Override
            public void valueChanged(ListSelectionEvent e) {
                controller.updatePath(list.getMaxSelectionIndex());
            }           
        }*/
        
        /*main*/
        public static void main(String[] args) throws IOException {
                               
                new GUI();
        }
 
}