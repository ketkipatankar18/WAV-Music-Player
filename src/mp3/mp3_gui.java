package mp3;
package Components.Playlist;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import javax.swing.BorderFactory;
import javax.swing.plaf.metal.MetalLookAndFeel;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;

import Components.ArhiveDialog;
import Components.SearchDialog;
import Entities.Song;
import Player.Player;
import Utility.BackgroundLoader;
import Utility.ExampleFileFilter;
import Utility.MusicPlayer;
import Utility.SongTransferHandler;
import FileInfo.FileInfoDialog;

/**
 * This class manages song list.
 * 
 * @author Vojislav Pankovic
 *
 */
public class PLEditor extends JWindow {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JScrollPane spnpList = null;

	private JList listSongs = null;

	private volatile ArrayList<Song> songs = new ArrayList<Song>();  //  @jve:decl-index=0:

    private ExampleFileFilter eff = new ExampleFileFilter();
  
    private MusicPlayer mp3 = null;  //  @jve:decl-index=0:
  
    private Player parent = null;
    
	private JPanel pnlButtons = null;

	public JButton btnAddFile = null;

	private JButton btnRemove = null;

	public JButton btnManage = null;

	private JFileChooser fileChooser = null;

	private JPopupMenu popupAdd = null;
	
	private JMenuItem file = null;
	
	private JMenuItem addDir = null;
	
	private JPopupMenu popupRem = null;
	
	private JMenuItem remAll = null;
	
	private JMenuItem remSelected = null;
	
	private JPopupMenu popupManage = null;
	
	private JMenuItem save = null;
	
	private JMenuItem load = null;
	
	private int oldSel = -1, newSel;
	
	public SongListModel songListModel = new SongListModel(getSongs());

	private SearchDialog searchDialog = null;  //  @jve:decl-index=0:visual-constraint="192,2166"
	
	private ArhiveDialog arhiveDialog = null;

	public int playingIndex = -1;
	
	public ArrayList<String> FilesForLoad = null;  //  @jve:decl-index=0:
	
	public BackgroundLoader bckgLoader = null;
	
	private int rCornerX;
	private int rCornerY;
	
	private boolean resizing = false;

	private FileInfoDialog fileInfoDialog = null;  //  @jve:decl-index=0:visual-constraint="393,208"

	/**
     * Sets music player
     * 
     * @param p MusicPlayer
     */
    public void setPlayer(MusicPlayer p){
    	this.mp3 = p;
    }
    /**
     * Sets parent player
     * @param p Player 
     */
    public void setParent(Player p){
    	this.parent = p;
    }
	/**
	 * This is the default constructor
	 */
	public PLEditor() {
		super();
		initialize();
	}
	
	/** 
	 * Reference to itself. This metod is used from
	 * ActionListener, for calling private metods
	 * 
	 */ 
	private JWindow me(){
		return this;
	}
	
	/**
	 *Initialization. 
	 *
	 */
	private void initialize() {
		this.setSize(360, 275);
		this.setContentPane(getJContentPane());
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentMoved(java.awt.event.ComponentEvent e) {
				rCornerX = getWidth();
				rCornerY = getHeight();
			}
		});
		this.FilesForLoad = new ArrayList<String>();
		this.bckgLoader = new BackgroundLoader(this);
		LoadTmpList();
	}

	public synchronized ArrayList<String> getFilesForLoad() {
		if (FilesForLoad.size() == 0){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return FilesForLoad;
	}
	/**
	 * Main panel initialization is public so it
	 * would be posible to apply new skin on it.
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setBorder(BorderFactory.createLineBorder(MetalLookAndFeel.getPrimaryControlDarkShadow(), 5));
			jContentPane.add(getSpnpList(), BorderLayout.CENTER);
			jContentPane.add(getPnlButtons(), BorderLayout.SOUTH);
			rCornerX = getWidth();
			rCornerY = getHeight();
			jContentPane.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {   
				public void mouseDragged(java.awt.event.MouseEvent e) {    
					if ((resizing) && (e.getPoint().x > 355) && (e.getPoint().y > 275)){
						setSize(e.getPoint().x, e.getPoint().y);
						rCornerX = getWidth();
						rCornerY = getHeight();
						
					}
				}
				public void mouseMoved(java.awt.event.MouseEvent e) {
					if ((Math.abs(e.getPoint().x - rCornerX) < 10) && (Math.abs(e.getPoint().y - rCornerY) < 10)){
						setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
					} else {
						setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				}
			});
			jContentPane.addMouseListener(new java.awt.event.MouseAdapter() {   
				public void mousePressed(java.awt.event.MouseEvent e) {
					if ((Math.abs(e.getPoint().x - rCornerX) < 10) && (Math.abs(e.getPoint().y - rCornerY) < 10)){
						resizing = true;
					}
				}
				public void mouseReleased(java.awt.event.MouseEvent e) {    
					resizing = false;
					rearangeComponents();
				}
			});
			
		    getListSongs().setBackground(MetalLookAndFeel.getPrimaryControlDarkShadow());
		    getListSongs().setForeground(MetalLookAndFeel.getControlHighlight());

		}
		return jContentPane;
	}
	/**
	 * Rearanges components on a playlist after it is 
	 * resized
	 *
	 */
	private void rearangeComponents(){
		pnlButtons.setLocation(pnlButtons.getLocation().x, getHeight() - pnlButtons.getHeight() - 5);
		pnlButtons.setSize(getWidth() - 10, pnlButtons.getHeight());
		spnpList.setSize(getWidth() - 20, getHeight() - 65);
		setVisible(false);
		setVisible(true);
		
	}
	/*
	 * ScrollPanel initialization for song list	
	 */
	private JScrollPane getSpnpList() {
		if (spnpList == null) {
			spnpList = new JScrollPane();
			spnpList.setBounds(new Rectangle(10, 10, 340, 211));
			spnpList.setFont(new Font("Dialog", Font.PLAIN, 12));
			spnpList.setViewportView(getListSongs());
			spnpList.setBorder(BorderFactory.createLoweredBevelBorder());
		}
		return spnpList;
	}
	
	/**
	 * Returns song list
	 * 
	 */
	public JList getListSongs() {
		if (listSongs == null) {
			listSongs = new JList(songListModel);
			listSongs.setFixedCellHeight(15);
			listSongs.setCellRenderer(new SongListCellRenderer(this));
			listSongs.setFont(new Font("DialogInput", Font.BOLD, 12));
			listSongs.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if(e.getClickCount() == 2){
						// ukoliko nije postavljen plejer pismeo poruku
						if (mp3 == null){
							System.out.print("Nije inicijalozovano");
						} else {
							mp3.resume();
						}
					}
				}
				public void mouseReleased(java.awt.event.MouseEvent e) {
					// mis je otpusten, nista nije selektovano
					oldSel = -1;
				}
			});
			listSongs.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
				public void mouseDragged(java.awt.event.MouseEvent e) {
					
					// biramo selektovani element
					newSel = listSongs.getSelectedIndex();
					// ukoliko jos nije doslo do razmene, zamenimo ga sa starim
					if (oldSel == -1){
						oldSel = newSel;
					}
					// ako su razliciti, menjamo ih
					if (newSel != oldSel){
						// menjamo
						songListModel.swapElements(newSel, oldSel);
						// i obavestimo da je doslo do razmene
						oldSel = newSel;
					}
				}
			});
			listSongs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(javax.swing.event.ListSelectionEvent e) {
							listSongs.repaint();
						}
					});
			listSongs.setDragEnabled(true);
			listSongs.setTransferHandler(new SongTransferHandler(listSongs));
		}
		return listSongs;
	}

	/**
	 * Returns song array.
	 * 
	 */
	public ArrayList<Song> getSongs(){
		if (this.songs == null){
			this.songs = new ArrayList<Song>();
		}
		return this.songs;
	}
	/**
	 * Adds song to list.
	 * 
	 */
	public void addSong(Song s){
		songListModel.addElement(s);
	}
	/**
	 * Removes song from list.
	 * 
	 */
	private void remSong(int index){
		songListModel.removeElementAt(index);
	}
	/**
	 * Returns panel buttons.
	 * 
	 */
	private JPanel getPnlButtons() {
		if (pnlButtons == null) {
			pnlButtons = new JPanel();
			pnlButtons.setLayout(new GridLayout());
			pnlButtons.setBounds(new Rectangle(5, 220, 350, 50));
			pnlButtons.add(getBtnAdd());
			pnlButtons.add(getBtnRemove());
			pnlButtons.add(getBtnManagePl());
		}
		return pnlButtons;
	}
	/**
	 * Returns add button, for adding file or folder
	 * 
	 */
	private JButton getBtnAdd() {
		if (btnAddFile == null) {
			btnAddFile = new JButton();
			btnAddFile.setIcon(new ImageIcon("resources/images/add.gif"));
			btnAddFile.setRolloverIcon(new ImageIcon("resources/images/addFocus.gif"));
			btnAddFile.setFocusPainted(false);
			btnAddFile.setBorderPainted(false);
			btnAddFile.setBounds(new Rectangle(10, 7, 40, 35));
			btnAddFile.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				    SwingUtilities.updateComponentTreeUI(getPopupAdd());     
					getPopupAdd().show(getPnlButtons(), btnAddFile.getLocation().x + 50, btnAddFile.getLocation().y - 30);
				}
			});
		}
		return btnAddFile;
	}
	/*
	 * Returns popup menu for selecting files and folders.
	 */
	private JPopupMenu getPopupAdd() {
		if (this.popupAdd == null){
			this.popupAdd = new JPopupMenu();
			this.popupAdd.add(getFile());
			this.popupAdd.add(getAddDir());
		}
		return popupAdd;
	}
	/*
	 * Initializes add file menu.
	 */
	private JMenuItem getFile() {
		if (this.file == null){
			file = new JMenuItem ("Add files                   ");
			file.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showAddFileDialog();
				}
			});
		}
		return file;
	}
	/*
	 * Initializes add folder menu.
	 */
	private JMenuItem getAddDir() {
		if (this.addDir == null){
			addDir = new JMenuItem ("Add folder          ");
			addDir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showAddDirDialog();
				}
			});			
		}
		return addDir;
	}
	/**
	 * Shows dialog for adding files in list.
	 *
	 */
	public synchronized void showAddFileDialog(){
		JFileChooser fch = getFileChooser();
		String [] filters = {"mp3"};
		eff = InitiateFileFilter(filters, "MP3 music");
		fch.setFileFilter(eff);
		fch.setMultiSelectionEnabled(true);
		fch.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fch.setDragEnabled(true);
	    int returnVal = fch.showDialog(me(), "Add File");
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	File[] res = fch.getSelectedFiles();
	    	for (int i = 0; i < res.length; i++){
				FilesForLoad.add(res[i].getAbsolutePath());
				FilesForLoad.add(res[i].getName());
//	    		Song s = new Song(res[i].getName(), res[i].getAbsolutePath());
//	    		addSong(s);
	    	}
	    	notify();
	    }	
	}
	/**
	 * Shows dialog for adding folders in list.
	 *
	 */
	public void showAddDirDialog(){
		JFileChooser fch = getFileChooser();
		fch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fch.setDragEnabled(false);
	    int returnVal = fch.showDialog(me(), "Add Directory");
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	AddAllFiles(fch.getSelectedFile());
	    }
	}
	/*
	 * Adds files in list recursivly, based on root directory
	 * 
	 */
	private synchronized void AddAllFiles(File f) {
		File [] files = f.listFiles();
		for (int i = 0; i < files.length; i++){
			// if it is folder, add its files
			if(files[i].isDirectory()){
				AddAllFiles(files[i]);
			// checking MP3 files
			} else if (files[i].getName().toLowerCase().endsWith(".mp3")) {
				FilesForLoad.add(files[i].getAbsolutePath());
				FilesForLoad.add(files[i].getName());
				//Song s = new Song(files[i].getName(), files[i].getAbsolutePath());
				//addSong(s);
			}
		}
		notify();
	}
	/*
	 * Sets file filter.
	 * 
	 */
	private ExampleFileFilter InitiateFileFilter(String[] exts, String desc) {
		for (int i = 0; i < exts.length; i++){
			eff.addExtension(exts[i]);
		}
		eff.setDescription(desc);
		return eff;
	}
	/*
	 * Returns remove button.
	 */
	private JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton();
			btnRemove.setIcon(new ImageIcon("resources/images/rem.gif"));
			btnRemove.setRolloverIcon(new ImageIcon("resources/images/remFocus.gif"));
			btnRemove.setFocusPainted(false);
			btnRemove.setBorderPainted(false);
			btnRemove.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				    SwingUtilities.updateComponentTreeUI(getPopupRem());     
					getPopupRem().show(getPnlButtons(), btnRemove.getLocation().x + 50, btnRemove.getLocation().y - 30);
				} 
			});
		} 
		return btnRemove;
	}
	/*
	 * Returns popup menu for removing songs.
	 * 
	 */
	private JPopupMenu getPopupRem() {
		if (this.popupRem == null){
			this.popupRem = new JPopupMenu();
			this.popupRem.add(getRemAll());
			this.popupRem.add(getRemSelected());
		}
		return popupRem;
	}
	/*
	 * Returns menu for entire list removing.
	 */
	private JMenuItem getRemAll() {
		if (this.remAll == null){
			this.remAll = new JMenuItem ("Remove all                   ");
			this.remAll.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					clearList();
				}
			});	
		}
		return remAll;
	}
	/*
	 * Returns menu for selected song removing.
	 */
	private JMenuItem getRemSelected() {
		if (this.remSelected == null){
			remSelected = new JMenuItem("Remove selected");
			remSelected.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int index = getListSongs().getSelectedIndex(); 
					if(index != -1){
						remSong(index);
					}
				}	
			});	
		}
		return remSelected;
	}
	/*
	 * Clears entire list.
	 */
	private void clearList(){
		songListModel.removeAllElements();
		FilesForLoad.clear();
		getSongs().clear();
	}
	/*
	 * Returns manage playlist button.
	 */
	private JButton getBtnManagePl() {
		if (btnManage == null) {
			btnManage = new JButton();
			btnManage.setIcon(new ImageIcon("resources/images/plist.gif"));
			btnManage.setRolloverIcon(new ImageIcon("resources/images/plistFocus.gif"));
			btnManage.setFocusPainted(false);
			btnManage.setBorderPainted(false);
			btnManage.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				    SwingUtilities.updateComponentTreeUI(getPopupManage());     
					getPopupManage().show(getPnlButtons(), btnManage.getLocation().x + 50, btnManage.getLocation().y - 30);
				}
			});
		}
		return btnManage;
	}
	/*
	 *  Returns popup menu for playlist managing.
	 */
	private JPopupMenu getPopupManage() {
		if (this.popupManage == null){
			this.popupManage = new JPopupMenu();
			this.popupManage.add(getSave());
			this.popupManage.add(getLoad());
		}
		return popupManage;
	}
	/*
	 * Returns load playlist menu.
	 */
	private JMenuItem getLoad() {
		if (this.load == null){
			this.load = new JMenuItem ("Load playlist                   ");
			this.load.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showLoadListDialog();
				} 
			});
		}
		return load;
	}
	/*
	 * Returns save playlist menu.
	 */
	private JMenuItem getSave() {
		if (this.save == null){
			this.save = new JMenuItem ("Save playlist                   ");
			this.save.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showSaveListDialog();
				}	
			});
		}
		return save;
	}
	/**
	 * Shows dialog for loading playlist.
	 *
	 */
	public void showLoadListDialog(){
		JFileChooser fch = getFileChooser();
		String [] filters = {"pls"};
		eff = InitiateFileFilter(filters, "MP3 music");
		fch.setFileFilter(eff);
		fch.setMultiSelectionEnabled(false);
		fch.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fch.setDragEnabled(false);
		int returnVal = fch.showOpenDialog(me());
		if (returnVal == JFileChooser.APPROVE_OPTION){
			LoadPlayList(fch.getSelectedFile(), true);
		}					
	}
	/**
	 * Shows dialog for saving playlist.
	 *
	 */
	public void showSaveListDialog(){
		JFileChooser fch = getFileChooser();
		String [] filters = {"pls"};
		eff = InitiateFileFilter(filters, "MP3 music");
		fch.setFileFilter(eff);
		fch.setMultiSelectionEnabled(false);
		fch.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fch.setDragEnabled(false);
		int returnVal = fch.showSaveDialog(me());
		if (returnVal == JFileChooser.APPROVE_OPTION){
			SavePlayList(fch.getSelectedFile(), true);
		}
	}
	/*
	 * metod za cuvanje liste
	 * parametar show sluzi za prikazivanje pourke
	 * ukoliko korisnik cuva listu ili ne 
	 * ukoliko se radi o automatskom cuvanju
	 */
	private void SavePlayList(File f, boolean show){
		String fileName = f.getAbsolutePath();
		if (! fileName.endsWith(".pls")){
			fileName += ".pls";
		}
		File sav = new File(fileName);
		if (show && sav.exists()){
			int retVal = JOptionPane.showConfirmDialog(me(), "File exists! \nOverwrite?" , "Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
			if (retVal == JOptionPane.CANCEL_OPTION){
				return;
			}
		}
		try {
			PrintWriter pw = new PrintWriter(fileName);
			Song s;
			ArrayList songs = getSongs();
			for (int i=0; i < songs.size(); i++){
				s = (Song)songs.get(i);
				pw.println(s.getFilePath());
				pw.println(s.getSongName());
			}
			pw.close();
			if (show){
				JOptionPane.showMessageDialog(me(), "Play list saved!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Loads playlist from specified file
	 * show - whether loading is automatic or not
	 */
	private synchronized void LoadPlayList(File f, boolean show){
		clearList();
		String filePath, songName;
		if (!f.getName().endsWith(".pls")) {
			JOptionPane.showMessageDialog(null, "Error loading list", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			while ((filePath = br.readLine()) != null){
				songName = br.readLine();
				FilesForLoad.add(filePath);
				FilesForLoad.add(songName);
				//addSong(new Song(songName, filePath));
			}
			br.close();
			if (show){
				JOptionPane.showMessageDialog(me(), "Play list loaded!");
			}
			notify();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getListSongs().setSelectedIndex(0);
	}
	/*
	 * Returns file chooser.
	 */
	private JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setSize(new Dimension(256, 187));
		}
		SwingUtilities.updateComponentTreeUI(fileChooser);
		return fileChooser;
	}
	/*
	 * Returns panel for options.
	
	private JOptionPane getJOptionPane() {
		if (jOptionPane == null) {
			jOptionPane = new JOptionPane();
			jOptionPane.setMessage("File exists! \nAre you sure you want to overwrite it?");
			jOptionPane.setOptionType(JOptionPane.YES_NO_OPTION);
			jOptionPane.setMessageType(JOptionPane.WARNING_MESSAGE);
		}
		return jOptionPane;
	}
	 */
	/**
	 * Returns currently selected song.
	 */
	public Song getSelectedSong(){
		int index = getListSongs().getSelectedIndex();
		playingIndex = index;
		if (index != -1){
			return getSongs().get(index);
		}
		return null;
	}
	
	public Vector<Song> getSelectedSongs(){
		Vector <Song> res = new Vector<Song>();
		int [] selIndices = getListSongs().getSelectedIndices();
		for (int i = 0; i < selIndices.length; i++){
			res.add(getSongs().get(selIndices[i]));
		}
		return res;
	}
	/**
	 * Saves current state in temp list.
	 *
	 */
	public void saveTmpList(){
		File f = new File("tmp.pls");
		SavePlayList(f, false);
	}
	/**
	 * Loads state from temp list.
	 *
	 */
	private void LoadTmpList(){
		File f = new File("tmp.pls");
		if (f.exists()){
			LoadPlayList(f, false);
		}
	}
	/**
	 * Selects next song. 
	 * 
	 * @return true if that choice is possible
	 */
	public boolean selectNext(){
		String song = songListModel.dequeue();
		if (song != null){
			int songIndex = songListModel.indexOfSong(song);
			getListSongs().setSelectedIndex(songIndex);
			getListSongs().ensureIndexIsVisible(songIndex);
			return true;
		}
		getListSongs().setSelectedIndex(playingIndex);
		int current =  playingIndex; // getListSongs().getSelectedIndex();
		// if we repeat one song,
		// next is same as previous
		if (parent.repeatMode == Player.REPEAT_ONE){
			return true;
		}
		if (parent.playingMode == Player.PLAY_NORMAL){
			// last song
			if (current == getSongs().size() - 1){
				if (parent.repeatMode == Player.REPEAT_OFF){
					getListSongs().setSelectedIndex(-1);
					return false;
				// or begining of list
				} else { 
					getListSongs().setSelectedIndex(0);
					getListSongs().ensureIndexIsVisible(0);
					//getSpnpList().getViewport().setViewPosition(new Point(0, calculateScrollPos(0)));
					return true;
				}
			// not last song
			} else {
				getListSongs().setSelectedIndex(current + 1);
				// getSpnpList().getViewport().setViewPosition(new Point(0, calculateScrollPos(current + 1)));
				getListSongs().ensureIndexIsVisible(current + 1);
				return true;
			} 
		// SHUFFLE is on
		} else {
			int next = (int)(getSongs().size() * Math.random());
			getListSongs().setSelectedIndex(next);
			getListSongs().ensureIndexIsVisible(next);
			//getSpnpList().getViewport().setViewPosition(new Point(0, calculateScrollPos(next)));
			return true;
		}
	}
	/**
	 * Returns previous song. 
	 * 
	 * @return true if possible.
	 */
	public boolean selectPrew(){
		getListSongs().setSelectedIndex(playingIndex);
		int current = playingIndex;  // getListSongs().getSelectedIndex();
		if (parent.repeatMode == Player.REPEAT_ONE){
			return true;
		}
		if (parent.playingMode == Player.PLAY_NORMAL){
			// first song
			if (current == 0){
				getListSongs().setSelectedIndex(getSongs().size() - 1);
				getListSongs().ensureIndexIsVisible(getSongs().size() - 1);
				//getSpnpList().getViewport().setViewPosition(new Point(0, calculateScrollPos(getSongs().size() - 1)));
				return true;
			} else {
				getListSongs().setSelectedIndex(current - 1);
				getListSongs().ensureIndexIsVisible(current - 1);
				//getSpnpList().getViewport().setViewPosition(new Point(0, calculateScrollPos(current - 1)));
				return true;
			} 
		// SHUFFLE is on
		} else {
			int next = (int)(getSongs().size() * Math.random());
			getListSongs().setSelectedIndex(next);
			getListSongs().ensureIndexIsVisible(next);
			//getSpnpList().getViewport().setViewPosition(new Point(0, calculateScrollPos(next)));
			return true;
		}
	}
	/*
	 * Calculates scrolling possition for current song.
	private int calculateScrollPos(int current){
		// normal element scrolling
		if (getSongs().size() - 9 > current) {
			return (getListSongs().getHeight() / getSongs().size()) * current; 
		// last screen element scrolling
		} else {
			current -= 9;
			if (current < 0){
				current = 0;
			}
			return (getListSongs().getHeight() / getSongs().size()) * current;
		}
	}
	 */
	/**
	 * This method initializes searchDialog	
	 * 	
	 * @return SearchDialog	
	 */
	private SearchDialog getSearchDialog() {
		if (searchDialog == null) {
			searchDialog = new SearchDialog(parent);
		}
		SwingUtilities.updateComponentTreeUI(searchDialog);
		return searchDialog;
	}
	/**
	 * This method initializes arhiveDialog	
	 * 	
	 * @return SearchDialog	
	 */
	private ArhiveDialog getArhiveDialog() {
		if (arhiveDialog == null) {
			arhiveDialog = new ArhiveDialog(parent);
		}
		SwingUtilities.updateComponentTreeUI(arhiveDialog);
		return arhiveDialog;
	}
	/**
	 * Show the search dialog
	 *
	 */
	public void showSearchDialog(){
		getSearchDialog().setVisible(true);
	}
	
	public void showFilwInfoDialog(){
		getFileInfoDialog().LoadBasicInfo(getSelectedSong());
		getFileInfoDialog().setVisible(true);
	}
	/**
	 * Show the arhive dialog
	 *
	 */
	public void showArhiveDialog(){
		getArhiveDialog().SetSongs(getSelectedSongs());
		getArhiveDialog().setModal(true);
		getArhiveDialog().SetSellectedTab(0);
		getArhiveDialog().setVisible(true);
	}
	
	/**
	 * plays song at selected index
	 * @param index
	 */
	public void playSongAt(int index){
		System.out.println("Prosledjena vredonst = " + index);
		getListSongs().setSelectedIndex(index);
		getListSongs().ensureIndexIsVisible(index);
		this.mp3.resume();
	}
	/**
	 * Put sellected song to quee
	 *
	 */
	public void enqueueSelSong(){
		Song s = getSelectedSong();
		if (s != null){
			
			songListModel.queue.add(s.getSongName());
			songListModel.queueIndex.add(listSongs.getSelectedIndex());
			
			
			listSongs.setSelectedIndex(listSongs.getSelectedIndex() + 1);
			listSongs.repaint();
		}
	}
	/**
	 * This method initializes fileInfoDialog	
	 * 	
	 * @return FileInfo.FileInfoDialog	
	 */
	private FileInfoDialog getFileInfoDialog() {
		if (fileInfoDialog == null) {
			fileInfoDialog = new FileInfoDialog(parent);
			fileInfoDialog.setSize(new java.awt.Dimension(579,374));
		}
		return fileInfoDialog;
	}
}  
