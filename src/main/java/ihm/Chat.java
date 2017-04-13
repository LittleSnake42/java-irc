package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ihm.Channel;
import utils.MessageControler;
import utils.MessageControlerException;

/*
 * This class is the graphical interface for displaying chat messages
 */

public class Chat extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextPane textPane;
	private JTextArea textArea;
	private JComboBox<Object> comboBox;
	private StyledDocument doc;
	private SimpleAttributeSet styleNormal;
	private SimpleAttributeSet nickStyle;

	// List of emojis
	private static final String[] EMOJIS = {":grinning:", ":grin:", ":joy:", ":rofl:", ":sweat_smile:", ":laughing:", ":wink:", ":blush:", ":sunglasses:", ":heart_eyes:", ":kissing_heart:", ":thinking:", ":smirk:", ":sleeping:", ":big_thongue:", ":drooling_face:", ":little_thongue:", ":big_thongue:", ":reverse:", ":sad:", ":triumph:", ":cry:", ":dizzy_face:", ":smiling_imp:", ":face_palm:", ":monkey:", ":smiley_cat:", ":sad_cat:", ":scream_cat:", ":8ball:", ":money:", ":snake:", ":ghost:", ":wind:", ":poop:", ":muscle:", ":ok_hand:", ":thumbsup:", ":clap:"};
	private static final ImageIcon[] EMOJIS_FILES = initEmojis();
	private static final HashMap<String, String> EMOJIS_EQUIVALENT = initEquivalentList();
	
	private static Chat INSTANCE = new Chat();

	public static ImageIcon[] initEmojis(){
		
		ImageIcon[] emos = new ImageIcon[EMOJIS.length];
		for (int i=0; i < EMOJIS.length; i++) {
			emos[i] = new ImageIcon("emojis/"+EMOJIS[i].replace(":", "")+".png");
		}
		
		return emos;
	}
	
	public static HashMap<String, String> initEquivalentList(){
		
		HashMap<String, String> equiv = new HashMap<String, String>();
		
		// Classics
		equiv.put(":)", "grin");
		equiv.put(":(", "sad");
		equiv.put(":p", "little_thongue");
		equiv.put(":P", "big_thongue");

		// Originals
		equiv.put("+1", "ok_hand");
		equiv.put("(:", "reverse");
		equiv.put("Zzz", "tired_face");

		// Kitties :D
		equiv.put(";(", "sad_cat");
		equiv.put(":O", "scream_cat");
		equiv.put(":D", "smiley_cat");
		
		// Joke
		equiv.put("Guillaume", "monkey");
		
		equiv.put("LittleSnake", "snake");


		return equiv;
	}

	
	public Chat() {
		// Define the title of the window
		this.setTitle("Client IRC");
		// Define the minimum size of the window
		this.setMinimumSize(new Dimension(300, 200));
		// Define the size of the window
		this.setSize(800, 500);
		// Close process when clicking on the red cross
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Puts the window in the center of the screen
		this.setLocationRelativeTo(null);
		
		/*
		 * contentPane is the main container, it contains the other containers :
		 * 		NORTH : panelTop
		 * 			EAST : panelTopEast
		 * 		CENTER : panelCenter
		 * 		BOTTOM : panelBottom
		 * 			CENTER : panelBottomCenter
		 * 			EAST : panelBottomEast
		 */
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		// panelTop is the top container, it contains the panel panelTopEast
		JPanel panelTop = new JPanel();
		contentPane.add(panelTop, BorderLayout.NORTH);
		panelTop.setLayout(new BorderLayout(0, 0));
		
		// panelTopEast contains the button to change channel and the button to logout
		JPanel panelTopEast = new JPanel();
		panelTop.add(panelTopEast, BorderLayout.EAST);
		
		// button to change the channel
		JButton btnChannel = new JButton("Change channel");
		btnChannel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnChannel.addActionListener(new ChannelListener());
		panelTopEast.add(btnChannel);
		
		// panelTopCenter contains the labels nickname et channel ***
		JPanel panelTopCenter = new JPanel();
		panelTop.add(panelTopCenter, BorderLayout.CENTER);
		panelTopCenter.setLayout(new BorderLayout(0, 0));
		
		// label for the nickname
		JLabel lblNickname = new JLabel("Nickname : ");
		lblNickname.setFont(new Font("Tahoma", Font.BOLD, 14));
		panelTopCenter.add(lblNickname, BorderLayout.WEST);
		
		// label for the nickname
		JLabel lblChannel = new JLabel("Channel : ");
		lblChannel.setFont(new Font("Tahoma", Font.BOLD, 14));
		panelTopCenter.add(lblChannel, BorderLayout.CENTER);
		
		// button to logout
		JButton btnLogout = new JButton("Logout");
		Icon imgLogout = new ImageIcon("image/logout.png");
		btnLogout.setIcon(imgLogout);
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogout.addActionListener(new LogoutListener());
		panelTopEast.add(btnLogout);
			
		
		// panelBottom is the bottom container, it contains panelBottomEast and panelBottomCenter
		JPanel panelBottom = new JPanel();
		contentPane.add(panelBottom, BorderLayout.SOUTH);
		panelBottom.setLayout(new BorderLayout(0, 0));
		panelBottom.setBorder(BorderFactory.createEmptyBorder(10,0,5,0));
		
		// panelBottomEast contains the dropdown list of the emojis and the button to send message
		JPanel panelBottomEast = new JPanel();
		panelBottom.add(panelBottomEast, BorderLayout.EAST);
		panelBottomEast.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		// dropdown list of the emojis
		comboBox = new JComboBox<Object>(EMOJIS_FILES);
		comboBox.setPreferredSize(new Dimension(60,30));
		comboBox.addActionListener(new ItemAction());
		panelBottomEast.add(comboBox);
		
		// button to send message
		JButton btnOk = new JButton("OK");
		btnOk.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnOk.addActionListener(new SendListener());
		panelBottomEast.add(btnOk);
		
		// panelBottomCenter contains the text area to write a message
		Panel panelBottomCenter = new Panel();
		panelBottom.add(panelBottomCenter, BorderLayout.CENTER);
		panelBottomCenter.setLayout(new BoxLayout(panelBottomCenter, BoxLayout.X_AXIS));
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.addKeyListener(new keyboardListener());
		textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		textArea.setCaretPosition(textArea.getText().length());
		panelBottomCenter.add(textArea);
		
		
		// panelCenter is the center container, it contains the text pane where messages are displayed
		JPanel panelCenter = new JPanel();
		panelCenter.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane sp = new JScrollPane(textPane);
		panelCenter.add(sp);
		
		// styles
		styleNormal = new SimpleAttributeSet();
		StyleConstants.setFontFamily(styleNormal, "Calibri");
		StyleConstants.setFontSize(styleNormal, 14);
		
		nickStyle = new SimpleAttributeSet();
		StyleConstants.setFontFamily(nickStyle, "Calibri");
		StyleConstants.setFontSize(nickStyle, 14);
	    StyleConstants.setBold(nickStyle, true);
	    StyleConstants.setForeground(nickStyle, Color.red);

		// styled document
		doc = textPane.getStyledDocument();
		
		// Add a listener to the window
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeFrame();
			}
		});
	}
	
	public static Chat getInstance() {
		return INSTANCE;
	}
	
	
	/*
	 * Methods
	 */
	
	// this method allows to display messages on the screen
	public void displayMessage(String message, String nick) {
				
		
		// bug fix :smile::smile: not ok
		if (message.contains("::"))
			message = message.replace("::", ": :");
				
		//check if need to be split
		String[] parts = null;
		if (message.contains(" ")) {
			System.out.println("Yep split");
			parts = message.split(" ");
		} else {
			System.out.println("Nop");
			parts = new String[1];
			parts[0] = message.replaceAll("[\\r\\n]+", "");
			System.out.println(parts[0].length());
		}
						
		// Displays the nick
		MessageControler mc = MessageControler.getInstance();
		try {
			if (nick.equals(mc.nickname)) {
				if(Chat.EMOJIS_EQUIVALENT.containsKey(nick)) {
					String emo_name = EMOJIS_EQUIVALENT.get(nick);
					textPane.setCaretPosition(doc.getLength());
					textPane.insertIcon(new ImageIcon("emojis/"+emo_name+".png", nick));
					doc.insertString(doc.getLength(), " # ", nickStyle);
				} else
					doc.insertString(doc.getLength(), nick + " # ", nickStyle);
			}
			else {
				if(Chat.EMOJIS_EQUIVALENT.containsKey(nick)) {
					String emo_name = EMOJIS_EQUIVALENT.get(nick);
					textPane.setCaretPosition(doc.getLength());
					textPane.insertIcon(new ImageIcon("emojis/"+emo_name+".png", nick));
					doc.insertString(doc.getLength(), "(" + nick + ") > ",styleNormal);
				} else
					doc.insertString(doc.getLength(), nick + " > ", styleNormal);
			}
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Displays the message + emos		
		for (int i = 0; i < parts.length; i++) {

			if (Arrays.asList(Chat.EMOJIS).contains(parts[i])) {
				// Il faut placer le curseur
				String emo_name = parts[i].replace(":", "");
				textPane.setCaretPosition(doc.getLength());
				textPane.insertIcon(new ImageIcon("emojis/"+emo_name+".png"));
			} 
			else {

				if(Chat.EMOJIS_EQUIVALENT.containsKey(parts[i])) {
					String emo_name = EMOJIS_EQUIVALENT.get(parts[i]);
					textPane.setCaretPosition(doc.getLength());
					textPane.insertIcon(new ImageIcon("emojis/"+emo_name+".png"));
				} else {

					if (i != 0) {
						message = " " + parts[i];//split all spaces
					} else {
						message = parts[i];
					}
					try {
						doc.insertString(doc.getLength(), message, styleNormal);
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			// si dernier mot (ou emo) \n
			if (i+1 == parts.length)
				try {
					doc.insertString(doc.getLength(), "\n", styleNormal);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
		
	}

	// This method allows to get message sent
	public String getTextField() {
		return textArea.getText();
	}

	// This method allows to display an error message
	public void displayError(String message) {
		JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	// This method allows to display an error message
	public void displayInfo(String message) {
		JOptionPane.showMessageDialog(null, message,
				"Just to let you know ...", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// This method allows to request a confirmation before closing the application
	public void closeFrame() {
		ImageIcon imageQuestion = new ImageIcon("image/question.png");
		int answer = JOptionPane.showConfirmDialog(this,
                "Are you sure you wish to close? ",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                imageQuestion);
		if(answer == JOptionPane.YES_OPTION ){
			// Close server if not already done
			MessageControler mc = MessageControler.getInstance();
			if (mc.isConnectionOpened()) {
				try {
					mc.process("#EXIT");
				} catch (MessageControlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			dispose();
		}
	}
	
	
	/*
	 * Class Listener
	 */
	
	// class listener button "Change channel", allows to change channel
	public class ChannelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			textPane.setText("");
			textArea.setText("");
			dispose();
			Channel channel = new Channel();
			channel.setVisible(true);
			
		}

	}
	
	// class listener button "Logout", allows to logout
	public class LogoutListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			textPane.setText("");
			textArea.setText("");
			dispose();
			Login login = new Login();
			login.setVisible(true);
			
		}
	}

	// class listener dropdown list of emojis
	class ItemAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			// display an emoji on the text area
			textArea.setText(textArea.getText() + EMOJIS[comboBox.getSelectedIndex()]);
			// focus
			textArea.requestFocus();
		}               
	}

	// class listener button "OK"
	class SendListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			
			// display messages on the screen
			if (textArea.getText().equals("")) {

			} else {
				
				// Process the message
				MessageControler mc = MessageControler.getInstance();
				try {
					mc.process(textArea.getText());
				} catch (MessageControlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			textArea.setText("");
			textArea.requestFocus();
		}
	}
	
	// class listener touch enter
	class keyboardListener implements KeyListener {
		  
	    public void keyTyped(KeyEvent eKey) {
	    	if (eKey.getKeyChar() == Event.ENTER) {
	    		if (textArea.getText().replaceAll("[\\r\\n]+", "").equals("")) {

				} else {
					
					// Process the message
					MessageControler mc = MessageControler.getInstance();
					try {
						mc.process(textArea.getText().replaceAll("[\\r\\n]+", ""));
					} catch (MessageControlerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				textArea.setText("");
				textArea.requestFocus();
	    	}
	    }
	 
	    public void keyPressed(KeyEvent e) {
	    	
	    }
	     
	    public void keyReleased(KeyEvent e) {

	    }
	}

	
	public void cleanDisplay() {
		this.textPane.setText("");
	}
	
}
