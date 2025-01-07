package mp3;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import javazoom.jl.player.Player;
import java.util.ArrayList;
import java.util.List;

public class MP3 {

	private JFrame frame;
	private JFrame Music;
	//private Player player;
    static File sound;
    static AudioInputStream ais;
     Clip clip;
    int d;
    static int index = 0;
    static int a;
    static JProgressBar progressBar = new JProgressBar();
    String data="";
    static int count=0;
	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		getConnection();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MP3 window = new MP3();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public static Connection getConnection() throws Exception{
		String driver="com.mysql.jdbc.Driver";
		String url="jdbc:mysql://localhost:3306/video";
		String username="root";
		String password="ketki1807";
		Class.forName(driver);
		try {
		Connection con=DriverManager.getConnection(url, username, password);
			return con;
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return null;
		}
	

	/**
	 * Create the application.
	 */
	public MP3() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton_1 = new JButton("Pause");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
		btnNewButton_1.setForeground(new Color(0, 0, 128));
		btnNewButton_1.setBackground(SystemColor.activeCaption);
		btnNewButton_1.setBounds(31, 218, 87, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Resume");
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
		btnNewButton_2.setForeground(new Color(0, 0, 128));
		btnNewButton_2.setBackground(SystemColor.activeCaption);
		btnNewButton_2.setBounds(320, 218, 87, 23);
		frame.getContentPane().add(btnNewButton_2);
		
//		JProgressBar progressBar = new JProgressBar();
		progressBar.setBackground(new Color(240, 255, 255));
		progressBar.setBounds(10, 192, 416, 16);
	    progressBar.setValue(0); 
		progressBar.setStringPainted(true);
		frame.getContentPane().add(progressBar);
		
		JSlider slider = new JSlider();
		slider.setBackground(new Color(176, 196, 222));
		slider.setBounds(108, 159, 194, 23);
		frame.getContentPane().add(slider);
		
		
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setForeground(new Color(0, 0, 128));
		btnPlay.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
		btnPlay.setBackground(SystemColor.activeCaption);
		btnPlay.setBounds(170, 218, 87, 23);
		frame.getContentPane().add(btnPlay);
		
		//Volume Low Button
		JLabel lblNewLabel_1 = new JLabel();
		lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\ketki_laptop\\Downloads\\drive-download-20200316T052619Z-001\\volumel.png"));
		lblNewLabel_1.setBounds(56, 141, 42, 53);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblVolumeHigh = new JLabel();
		lblVolumeHigh.setIcon(new ImageIcon("C:\\Users\\ketki_laptop\\Downloads\\drive-download-20200316T052619Z-001\\volumeu.png"));
		//JLabel lblVolumeHigh = new JLabel("Volume High");
		lblVolumeHigh.setBounds(308, 141, 42, 53);
		frame.getContentPane().add(lblVolumeHigh);
		
		 List<String> myList = new ArrayList<>(10);
	      for (int index = 1; index <= 25; index++) {
	  		try {

	  			Connection con = getConnection();
	  			String query="select Name from music where mp3_id=?;";
	  			PreparedStatement st=con.prepareStatement(query);
				st.setLong(1,index);
	  		ResultSet rs=st.executeQuery();
	  		while(rs.next())
	  		{
	  			myList.add(rs.getString(1));
	  		}
	  		}catch (Exception e) {
					e.printStackTrace();
				}
	      }
	      JList<String> list = new JList<String>(myList.toArray(new String[myList.size()]));
	      JScrollPane scrollPane = new JScrollPane();
	      scrollPane.setBounds(10,10,416,70);
	      scrollPane.setViewportView(list);
	      list.setLayoutOrientation(JList.VERTICAL);
	      frame.getContentPane().add(scrollPane);
	      
	      ListSelectionListener listSelectionListener = new ListSelectionListener() {
	          public void valueChanged(ListSelectionEvent listSelectionEvent) {
	            boolean adjust = listSelectionEvent.getValueIsAdjusting();
	            if (!adjust) {
	              JList list = (JList) listSelectionEvent.getSource();
	              data=(String) list.getSelectedValue();
             	 System.out.println(data);
	            }
	          }
	        };
	        list.addListSelectionListener(listSelectionListener);
		//play button action listener
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae2) 
			{
			 if(ae2.getSource()==btnPlay)
			 {
				 try
				 {
					 if(count!=0)
					 {
						 if (clip.isRunning()) 
							 clip.stop();
					 }
					  
					 count++;
				        		 String audioFilePath= data+".wav";
				        		 File audioFile = new File(audioFilePath);
				                   AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

								 clip= AudioSystem.getClip();
								 clip.open(audioStream);
								 FloatControl gainControl = 
										    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
										gainControl.setValue(-10.0f);
										slider.addChangeListener( new ChangeListener(){
									        public void stateChanged(ChangeEvent ce) {
									          gainControl.setValue( slider.getValue() );
									        }
									      } );
								 clip.start();
//								 progressBar.addChangeListener(new BoundedChangeListener());
//								 for(int n=1;clip.isRunning();n++)
//                                 {
//                                	 for (int i = 0; i < n; i++) {
//   								      progressBar.setValue(i);
////   								      Thread.sleep(100);
//   								    } 
//                                 }
                                
								 btnNewButton_1.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent ae1) 
										{
										 if(ae1.getSource()==btnNewButton_1)
										 {
											 try
											 {
													 if (clip.isRunning()) 
													 clip.stop();
													 d=clip.getFramePosition();
											 }
											 catch(Exception e)
											 {
												JOptionPane.showMessageDialog(null, e); 
											 }
										 }
										}
									});
									btnNewButton_2.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent ae1) 
										{
										 if(ae1.getSource()==btnNewButton_2)
										 {
											 try
											 {
													  if (clip.getFramePosition()!=0)   
											         {
											        	 clip.setFramePosition(d); // rewind to the beginning
												         clip.start();
											         }
											 }
											 catch(Exception e)
											 {
												JOptionPane.showMessageDialog(null, e); 
											 }
										 }
										}
									});
				 }
				 catch(Exception e)
				 {
					JOptionPane.showMessageDialog(null, e); 
				 }
			 }
			}
		});
		
	}
	

	class BoundedChangeListener implements ChangeListener {
	  public void stateChanged(ChangeEvent changeEvent) {
	    Object source = changeEvent.getSource();
	    if (source instanceof JProgressBar) {
	      JProgressBar theJProgressBar = (JProgressBar) source;
	     
	  }
	}
	}
}

	   
	   
	    

	 
