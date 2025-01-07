package Components;

import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Rectangle;
import javax.swing.JLabel;

import Components.Playlist.SongListModel;
import Entities.Song;
import FileInfo.FileInfoPanel;
import Player.Player;
import Utility.ArhiveLoader;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;

import javazoom.jlgui.player.amp.tag.MpegInfo;
import javazoom.jlgui.player.amp.tag.TagInfoFactory;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ComponentSampleModel;

import javax.swing.JDialog;

import Player.Player;

public abstract class PlayerDialog extends JDialog{

	
	protected Player parent;
	
	public PlayerDialog(Player owner){
		super(owner);
		this.parent = owner;
	}
	
	protected void SetLocation(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                 (screenSize.height - this.getHeight()) / 2);
	}
	
	protected void SetClosing(){
		AddListenerToComponent(getContentPane());
	}
	
	private void AddListenerToComponent(Component component){
		component.addKeyListener(this.closer);
		if ( ! (component instanceof ItemSelectable)) {
			Component [] members = ( (Container) component).getComponents();
			for (Component component2 : members) {
				AddListenerToComponent(component2);
			}
		} 
	}
	
	private KeyListener closer = new KeyListener() {
		public void keyTyped(KeyEvent arg0) {
			
		}

		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				setVisible(false);
				parent.setVisible(true);
			}
		}

		public void keyReleased(KeyEvent arg0) {
			
		}
		
	};

	protected abstract void initialize();
}

public class ArhiveDialog extends PlayerDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3669300564878331816L;
	// private Player parent = null;
	private JPanel contentPanel = null;
	private JPanel pnlSettings = null;  //  @jve:decl-index=0:visual-constraint="450,125"
	private JPanel pnlButtons = null;
	private JLabel lblArhiveFolder = null;
	private JLabel arhiveFolderV = null;
	private JLabel lblSongPath = null;
	private JLabel lblSongPathV = null;
	private JLabel lblFoldersCut = null;
	private JTextField txtFoldersCut = null;
	private JRadioButton radioMove = null;
	private JRadioButton radioCopy = null;
	private JScrollPane scrollPnlSongs = null;
	private JList jListSongs = null;
	private JLabel lblSongName = null;
	private JTextField txtSongName = null;
	private JButton btnChangeA = null;
	private JButton btnChangeP = null;
	private JButton btnFirsUpper = null;
	private JPanel pnlArhiveStatus = null;
	private JFileChooser fileChooser = null;
	private JButton btnRemove = null;
	private JButton btnViewInfo = null;
	private JButton btnViewArhive = null;
	private JButton btnArhive = null;
	private JButton btnExit = null;
	private JButton btnMove = null;
	
	private Vector <Song> songs = null;
	private JProgressBar prgArhive = null; 
	private ArhiveLoader arhiveLoader = null;
	private JTabbedPane jTabbedPaneContent = null;
	private JPanel jPanelAction = null;
	private JPanel jPanelFileInfo = null;
	private JPanel jPanelArhiveOverview = null;
	private Song lastLoadedInfo = null;
	private FileInfoPanel fileInfoPanel = null;
	
	public void SetSongs(Vector <Song> songs){
		this.songs = songs;
		getJListSongs().setListData(songs);
		getJListSongs().setSelectedIndex(0);
	}

	/**
	 * This method initializes 
	 * 
	 */
	public ArhiveDialog(Player owner) {
		super(owner);
	    initialize();
		//this.parent = owner;
		
//
	}

	/**
	 * This method initializes this
	 * 
	 */
	protected void initialize() {
        this.setSize(new java.awt.Dimension(596,542));
        this.setContentPane(getContentPanel());
        this.setTitle("Arhive songs");
        this.arhiveLoader = new ArhiveLoader(parent.settings.arhivePath, this);
        SetLocation();
        SetClosing();
	}
	
	/**
	 * Sets the appropriate tab to be sellected
	 */
	public void SetSellectedTab(int index){
		getJTabbedPaneContent().setSelectedIndex(index);
	}
	

	/**
	 * This method initializes contentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(null);
			contentPanel.add(getJTabbedPaneContent(), null);
			contentPanel.add(getPnlButtons(), null);
			contentPanel.add(getPnlArhiveStatus(), null);
		}
		return contentPanel;
	}

	/**
	 * This method initializes pnlSettings	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlSettings() {
		if (pnlSettings == null) {
			lblSongName = new JLabel();
			lblSongName.setBounds(new Rectangle(13, 167, 122, 25));
			lblSongName.setText("Song name");
			lblFoldersCut = new JLabel();
			lblFoldersCut.setBounds(new Rectangle(13, 231, 158, 26));
			lblFoldersCut.setText("Folders to cut");
			lblSongPathV = new JLabel();
			lblSongPathV.setBounds(new Rectangle(12, 133, 245, 27));
			lblSongPathV.setText("JLabel");
			lblSongPath = new JLabel();
			lblSongPath.setBounds(new Rectangle(13, 99, 123, 26));
			lblSongPath.setText("Song path");
			arhiveFolderV = new JLabel();
			arhiveFolderV.setBounds(new Rectangle(11, 59, 250, 28));
			arhiveFolderV.setText(parent.settings.arhivePath);
			arhiveFolderV.setToolTipText(parent.settings.arhivePath);
			lblArhiveFolder = new JLabel();
			lblArhiveFolder.setBounds(new Rectangle(9, 28, 126, 26));
			lblArhiveFolder.setText("Arhive Folder");
			pnlSettings = new JPanel();
			pnlSettings.setBounds(new java.awt.Rectangle(302,13,262,306));
			pnlSettings.setLayout(null);
			pnlSettings.setBorder(BorderFactory.createTitledBorder(null, "Arhive properties", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			pnlSettings.add(lblArhiveFolder, null);
			pnlSettings.add(arhiveFolderV, null);
			pnlSettings.add(lblSongPath, null);
			pnlSettings.add(lblSongPathV, null);
			pnlSettings.add(lblFoldersCut, null);
			pnlSettings.add(getTxtFoldersCut(), null);
			pnlSettings.add(getRadioMove(), null);
			pnlSettings.add(getRadioCopy(), null);
			pnlSettings.add(lblSongName, null);
			pnlSettings.add(getTxtSongName(), null);
			pnlSettings.add(getBtnChangeA(), null);
			pnlSettings.add(getBtnChangeP(), null);
			pnlSettings.add(getBtnFirsUpper(), null);
		}
		return pnlSettings;
	}

	/**
	 * This method initializes pnlButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlButtons() {
		if (pnlButtons == null) {
			pnlButtons = new JPanel();
			pnlButtons.setLayout(null);
			pnlButtons.setBounds(new Rectangle(9, 4, 572, 59));
			pnlButtons.add(getBtnRemove(), null);
			pnlButtons.add(getBtnViewInfo(), null);
			pnlButtons.add(getBtnViewArhive(), null);
			pnlButtons.add(getBtnArhive(), null);
			pnlButtons.add(getBtnExit(), null);
			pnlButtons.add(getBtnMove(), null);
		}
		return pnlButtons;
	}

	/**
	 * This method initializes txtFoldersCut	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtFoldersCut() {
		if (txtFoldersCut == null) {
			txtFoldersCut = new JTextField();
			txtFoldersCut.setBounds(new Rectangle(188, 231, 55, 25));
			txtFoldersCut.setText(parent.settings.foldersToCut + "");
		}
		return txtFoldersCut;
	}

	/**
	 * This method initializes radioMove	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRadioMove() {
		if (radioMove == null) {
			radioMove = new JRadioButton();
			radioMove.setBounds(new Rectangle(13, 266, 84, 28));
			radioMove.setText("Move");
		}
		return radioMove;
	}

	/**
	 * This method initializes radioCopy	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRadioCopy() {
		if (radioCopy == null) {
			radioCopy = new JRadioButton();
			radioCopy.setBounds(new Rectangle(119, 268, 118, 27));
			radioCopy.setText("Copy");
		}
		return radioCopy;
	}

	/**
	 * This method initializes scrollPnlSongs	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPnlSongs() {
		if (scrollPnlSongs == null) {
			scrollPnlSongs = new JScrollPane();
			scrollPnlSongs.setBounds(new java.awt.Rectangle(5,5,292,317));
			scrollPnlSongs.setBorder(BorderFactory.createTitledBorder(null, "Song to arhive", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			scrollPnlSongs.setViewportView(getJListSongs());
		}
		return scrollPnlSongs;
	}

	/**
	 * This method initializes jListSongs	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJListSongs() {
		if (jListSongs == null) {
			jListSongs = new JList();
			jListSongs.setListData(parent.getPlayList().getSelectedSongs());
			jListSongs.setSelectedIndex(0);
			jListSongs
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(javax.swing.event.ListSelectionEvent e) {
							writeSelectedInfo();
						}
					});
		}
		// jListSongs.setListData(arhive);
		//jListSongs.setSelectedIndex(0);
		return jListSongs;
	}
	
	private void writeSelectedInfo(){
		Song s = (Song)jListSongs.getSelectedValue();
		if (s != null){
			String dir = getDirectory(s.getFilePath());
			lblSongPathV.setText(dir);
			lblSongPathV.setToolTipText(dir);
			txtSongName.setText(removeExstension(s.getSongName()));
		}
	}
	
	private String getDirectory(String path){
		int index = path.lastIndexOf("\\");
		return path.substring(0, index);
	}
	
	private String removeExstension(String fileName){
		int index = fileName.lastIndexOf(".");
		return fileName.substring(0, index);
	}

	/**
	 * This method initializes txtSongName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtSongName() {
		if (txtSongName == null) {
			txtSongName = new JTextField();
			txtSongName.setBounds(new Rectangle(12, 202, 235, 24));
		}
		return txtSongName;
	}

	/**
	 * This method initializes btnChangeA	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnChangeA() {
		if (btnChangeA == null) {
			btnChangeA = new JButton();
			btnChangeA.setBounds(new Rectangle(148, 28, 106, 23));
			btnChangeA.setText("Change");
			btnChangeA.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showChangeDirDialog(arhiveFolderV);
				}
			});
		}
		return btnChangeA;
	}

	/**
	 * This method initializes btnChangeP	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnChangeP() {
		if (btnChangeP == null) {
			btnChangeP = new JButton();
			btnChangeP.setBounds(new Rectangle(143, 97, 111, 29));
			btnChangeP.setText("Change");
			btnChangeP.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showChangeDirDialog(lblSongPathV);
				}
			});
		}
		return btnChangeP;
	}

	/**
	 * This method initializes btnFirsUpper	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnFirsUpper() {
		if (btnFirsUpper == null) {
			btnFirsUpper = new JButton();
			btnFirsUpper.setBounds(new Rectangle(149, 166, 103, 28));
			btnFirsUpper.setText("First Upper");
			btnFirsUpper.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					txtSongName.setText(FirstUpper(txtSongName.getText()));
				}
			});
		}
		return btnFirsUpper;
	}

	private String FirstUpper(String text){
		String res = "";
		String [] words = text.split(" ");
		for (String string : words) {
			string = string.trim();
			if (string.length() > 0){
				res += string.substring(0, 1).toUpperCase();
				res += string.substring(1).toLowerCase();
				res += " ";
			}
		}
		return res.substring(0, res.length() - 1);
	}
	/**
	 * This method initializes pnlArhiveStatus	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlArhiveStatus() {
		if (pnlArhiveStatus == null) {
			pnlArhiveStatus = new JPanel();
			pnlArhiveStatus.setLayout(null);
			pnlArhiveStatus.setBounds(new java.awt.Rectangle(13,436,568,63));
			pnlArhiveStatus.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Arhive status", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			pnlArhiveStatus.add(getPrgArhive(), null);
		}
		return pnlArhiveStatus;
	}
	
	
	/**
	 * Shows dialog for adding folders in list.
	 *
	 */
	public void showChangeDirDialog(JLabel destination){
		JFileChooser fch = getFileChooser();
		fch.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fch.setDragEnabled(false);
		fch.setCurrentDirectory(new File(destination.getText()));
	    int returnVal = fch.showDialog(this, "OK");
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	String path;
	    	if (fch.getSelectedFile().isFile()){
	    		path = fch.getSelectedFile().getParent();
	    	} else {
	    		path = fch.getSelectedFile().getAbsolutePath();
	    	}
	    	destination.setText(path);
	    	destination.setToolTipText(path);
	    }
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

	/**
	 * This method initializes btnRemove	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton();
			btnRemove.setBounds(new Rectangle(4, 7, 63, 46));
		}
		return btnRemove;
	}

	/**
	 * This method initializes btnViewInfo	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnViewInfo() {
		if (btnViewInfo == null) {
			btnViewInfo = new JButton();
			btnViewInfo.setBounds(new Rectangle(75, 6, 70, 48));
		}
		return btnViewInfo;
	}

	/**
	 * This method initializes btnViewArhive	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnViewArhive() {
		if (btnViewArhive == null) {
			btnViewArhive = new JButton();
			btnViewArhive.setBounds(new Rectangle(154, 6, 71, 47));
		}
		return btnViewArhive;
	}

	/**
	 * This method initializes btnArhive	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnArhive() {
		if (btnArhive == null) {
			btnArhive = new JButton();
			btnArhive.setBounds(new Rectangle(237, 6, 64, 48));
			btnArhive.setText("Arhive");
			btnArhive.setToolTipText("Arhive");
			btnArhive.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Arhive();
				}
			});
		}
		return btnArhive;
	}
	
	/*
	 * Method that moves a song to arhive folder
	 */
	private void Arhive(){
		Song arhiveSong = (Song)getJListSongs().getSelectedValue();
		
		SongListModel songListModel = (SongListModel)parent.getPlayList().getListSongs().getModel();
		
		int playingIndex = parent.getPlayList().playingIndex;
		
		Song playingSong = null;
		
		if (playingIndex != -1){
			playingSong = (Song)songListModel.getElementAt(playingIndex);
		}
		
		if (arhiveSong.equals(playingSong)){
			parent.playNext();
		}
		boolean sortFirst = !arhiveSong.getFilePath().startsWith(lblSongPathV.getText());
		if (sortFirst){
			MoveTo(arhiveSong);
		}
		arhiveSong = (Song)getJListSongs().getSelectedValue();
		int depth = Integer.parseInt(txtFoldersCut.getText());
		String cutPath = CutFolder(lblSongPathV.getText(), depth);
		File source = new File(arhiveSong.getFilePath());
		File dest = new File(arhiveFolderV.getText() + "\\" + cutPath + "\\" + txtSongName.getText() + ".mp3");
		dest.getParentFile().mkdirs();
		if (dest.exists()){
			int retVal = JOptionPane.showConfirmDialog(parent, "File exists! \nOverwrite?" , "Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
			if (retVal == JOptionPane.CANCEL_OPTION){
				return;
			} else {
				dest.delete();
			}
		}
		if (!source.renameTo(dest)){
			JOptionPane.showConfirmDialog(parent, "Error! \nFile not moved!" , "Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.OK_OPTION);
			return;
		}
		
		songListModel.removeElement(arhiveSong);
		songs.removeElement(arhiveSong);
		jListSongs.setSelectedIndex(songs.size() - 1);
		jListSongs.repaint();
		
	}

	private String CutFolder(String path, int depth){
		int lastIndex = path.indexOf("\\");
		for (int i = 0; i < depth; i++){
			lastIndex = path.indexOf("\\", lastIndex + 1);
		}
		// if last index is -1 then count substring from first index
		if (lastIndex == -1){
			lastIndex = path.indexOf("\\");
		}
		return path.substring(lastIndex, path.length());
	}
	/*
	 * Method that moves song to new location
	 */
	private void MoveTo(Song moveSong){
		SongListModel songListModel = (SongListModel)parent.getPlayList().getListSongs().getModel(); 
		Song playingSong = null;
		boolean movingPlaying = false;
		
		File source = new File(moveSong.getFilePath());
		
		int playingIndex = parent.getPlayList().playingIndex;
		int movingIndex = songListModel.indexOf(moveSong);
		
		if (playingIndex != -1){
			playingSong = (Song)songListModel.getElementAt(playingIndex);
		}
		
		if (moveSong.equals(playingSong)){
			parent.getMp3().stop();
			movingPlaying = true;
		}
		File dest = new File(lblSongPathV.getText() + "\\" + txtSongName.getText() + ".mp3");
		dest.getParentFile().mkdirs();
		if (dest.exists()){
			int retVal = JOptionPane.showConfirmDialog(parent, "File exists! \nOverwrite?" , "Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
			if (retVal == JOptionPane.CANCEL_OPTION){
				return;
			} else {
				dest.delete();
			}
		}
		if (!source.renameTo(dest)){
			JOptionPane.showConfirmDialog(parent, "Error! \nFile not moved!" , "Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.OK_OPTION);
			return;
		}
		
		Song newSong = new Song(dest.getName(), dest.getAbsolutePath());
		this.songs.setElementAt(newSong, jListSongs.getSelectedIndex());
		this.jListSongs.repaint();
		songListModel.setElementAt(newSong, movingIndex);
		if (movingPlaying){
			parent.getMp3().resume();
		}
		
		/*
		 *  sets the lasLoaded song to null because the info of
		 *  this song is changed
		 */
		lastLoadedInfo = null;
	}
	
	/**
	 * This method initializes btnExit	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnExit() {
		if (btnExit == null) {
			btnExit = new JButton();
			btnExit.setBounds(new Rectangle(380, 4, 63, 49));
			btnExit.setText("Exit");
		}
		return btnExit;
	}

	/**
	 * This method initializes btnMove	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnMove() {
		if (btnMove == null) {
			btnMove = new JButton();
			btnMove.setBounds(new java.awt.Rectangle(311,5,60,47));
			btnMove.setText("Move");
			btnMove.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Song arhiveSong = (Song)getJListSongs().getSelectedValue();
					MoveTo(arhiveSong);
				}
			});
		}
		return btnMove;
	}

	/**
	 * This method initializes prgArhive	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getPrgArhive() {
		if (prgArhive == null) {
			prgArhive = new JProgressBar();
			prgArhive.setBounds(new java.awt.Rectangle(14,20,550,28));
			prgArhive.setMaximum(parent.settings.arhiveSize);
			prgArhive.setBorderPainted(false);
			prgArhive.setStringPainted(true);
		}
		return prgArhive;
	}

	/**
	 * This method initializes jTabbedPaneContent	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPaneContent() {
		if (jTabbedPaneContent == null) {
			jTabbedPaneContent = new JTabbedPane();
			jTabbedPaneContent.setBounds(new java.awt.Rectangle(10,74,568,355));
			jTabbedPaneContent.addTab("Arhive", null, getJPanelAction(), null);
			jTabbedPaneContent.addTab("File Info", null, getJPanelFileInfo(), null);
			jTabbedPaneContent.addTab("Arhive Overview", null, getJPanelArhiveOverview(), null);
			jTabbedPaneContent.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					// change this 
					if(getJTabbedPaneContent().getSelectedIndex() == 1){
						Song s = (Song)getJListSongs().getSelectedValue();
						if (s != lastLoadedInfo){
							// LoadBasicInfo(s);
							getFileInfoPanel().LoadBasicInfo(s);
							lastLoadedInfo = s;
						}
						
					}
				}
			});
		}
		return jTabbedPaneContent;
	}
	
	/**
	 * This method initializes jPanelAction	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelAction() {
		if (jPanelAction == null) {
			jPanelAction = new JPanel();
			jPanelAction.setLayout(null);
			jPanelAction.add(getPnlSettings(), null);
			jPanelAction.add(getScrollPnlSongs(), null);
			
			
		}
		return jPanelAction;
	}

	/**
	 * This method initializes jPanelFileInfo	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelFileInfo() {
		if (jPanelFileInfo == null) {
			jPanelFileInfo = new JPanel();
			jPanelFileInfo.setLayout(null);
			jPanelFileInfo.add(getFileInfoPanel(), null);
			// jPanelFileInfo.add(getJTabbedPaneTAGS(), null);
		}
		return jPanelFileInfo;
	}

	/**
	 * This method initializes jPanelArhiveOverview	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelArhiveOverview() {
		if (jPanelArhiveOverview == null) {
			jPanelArhiveOverview = new JPanel();
			jPanelArhiveOverview.setLayout(null);
			// jPanelArhiveOverview.add(new FileInfoPanel(), null);
			// jPanelArhiveOverview.add(getFileInfoPanel(), null);
		}
		return jPanelArhiveOverview;
	}
	

	/**
	 * This method initializes fileInfoPanel	
	 * 	
	 * @return Components.FileInfoPanel	
	 */
	private FileInfoPanel getFileInfoPanel() {
		if (fileInfoPanel == null) {
			fileInfoPanel = new FileInfoPanel();
			fileInfoPanel.setBounds(new java.awt.Rectangle(1,5,564,329));
		}
		return fileInfoPanel;
	}


}  //  @jve:decl-index=0:visual-constraint="131,-8"
