package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import utils.MessageControler;
import utils.MessageControlerException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


/*
 * This class is the graphical interface for displaying chat messages
 */

public class Window extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel, panelTop, panelButtons, panelCenter, panelRight, panelBottom;
	private JTextPane screen, textField;
	private JComboBox<?> listSmileys;
	private JButton buttonSend, buttonChannel, buttonLogout;
	private StyledDocument sDoc;
	private Style defaut, styleCyan, styleRed;
	private int pos = 0;
	
	public ImageIcon icon = new ImageIcon("swag.jpg", "Titre");

	private static Window INSTANCE = new Window();

	private Window() {
	
		this.setTitle("Client IRC");

		this.setMinimumSize(new Dimension(500, 300));
		this.setSize(800, 500);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);

		initComponents();

		this.setContentPane(mainPanel);    

	}
	

	public static Window getInstance() {
		return INSTANCE;
	}
	
	// This method allow to init components
	private void initComponents(){
		
		// create panels
		mainPanel = new JPanel();
		panelTop = new JPanel();
		panelBottom = new JPanel();
		panelCenter = new JPanel();
		panelRight = new JPanel();
		
		// layout
		mainPanel.setLayout(new BorderLayout());
		panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.LINE_AXIS));
		panelBottom.setLayout(new BorderLayout());
		panelCenter.setLayout(new BorderLayout());
		panelRight.setLayout(new BorderLayout());
		
		
		/*
		 * TOP panel components
		 */
		buttonChannel = new JButton("Change channel");
		buttonLogout = new JButton("Logout");


		panelTop.add(buttonChannel);
		panelTop.add(buttonLogout);
		
		
		/*
		 * BOTTOM panel components
		 */
		textField  = new JTextPane();
		
		// components for smileys		
		ImageIcon[] emoticon = {
			new ImageIcon("image/grinning.png"),
			new ImageIcon("image/grin.png"),
			new ImageIcon("image/laughing.png")				
		};
		
		listSmileys = new JComboBox<Object>(emoticon);
		listSmileys.setPreferredSize(new Dimension(60,30));
		
		buttonSend = new JButton("SEND");
		
		// add combo and button "SEND" to the panel buttons
		panelButtons = new JPanel();
		panelButtons.add(listSmileys);
		panelButtons.add(buttonSend);
		
		// add components to the panel bottom
		panelBottom.add(textField, BorderLayout.CENTER);
		panelBottom.add(panelButtons, BorderLayout.EAST);
		panelBottom.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		
		
		/*
		 * CENTER panel components
		 */
		screen = new JTextPane();
		screen.setSize(100, 100);
		screen.setEditable(false);
		
		defaut = screen.getStyle("default");
		
		styleCyan = screen.addStyle("style1", defaut);
		StyleConstants.setForeground(styleCyan, Color.CYAN);
	    StyleConstants.setFontFamily(styleCyan, "Comic sans MS");
	    
	    styleRed = screen.addStyle("style2", styleCyan);
	    StyleConstants.setForeground(styleRed, Color.RED);
	   	    
	    sDoc = (StyledDocument)screen.getDocument();
	    
	    panelCenter.add(screen);
		
	    
		/*
		 * Main panel
		 */
	    // add panels to main panel
		mainPanel.add(new JScrollPane(panelTop), BorderLayout.NORTH);
	    mainPanel.add(new JScrollPane(panelCenter), BorderLayout.CENTER);
	    mainPanel.add(new JScrollPane(panelRight), BorderLayout.EAST);
	    mainPanel.add(panelBottom, BorderLayout.SOUTH);
		
	    
	   /*
	    *  add listeners
	    */
	    buttonChannel.addActionListener(new ChannelListener());
		buttonLogout.addActionListener(new LogoutListener());
	 	listSmileys.addActionListener(new ItemAction());
	 	buttonSend.addActionListener(new SendListener());
	 	textField.addKeyListener(new keyboardListener());
	 	
		
	    this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeFrame();
			}
		});
	}	
	
	/*
	 * Methods
	 */
	

	public void closeFrame() {
		int answer = JOptionPane.showConfirmDialog(this,
                "Are you sure you wish to close? ",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
		if(answer == JOptionPane.YES_OPTION ){
			dispose();
		}
	}

	
	/*
	 * Class Listener
	 */
	
	// class listener button "Change channel"
	public class ChannelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			dispose();
			FrameChannel window = new FrameChannel();
			window.setVisible(true);
			
		}

	}
	
	// class listener button "Logout"
	public class LogoutListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			dispose();
			FrameConnection window = new FrameConnection();
			window.setVisible(true);
			
		}
	}


	// class listener smiley
	class ItemAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			//textField.setText(textField.getText() + " " + listSmileys.getSelectedItem());
			String smiley[] = {":grinning:", ":grin:", ":laughing:"};
			textField.setText(textField.getText() + smiley[listSmileys.getSelectedIndex()] + " ");
			// focus
			textField.requestFocus();
		}
	}

	// class listener button SEND
	class SendListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			// display messages on the screen
			if (textField.getText().equals("")) {

			} else {

				// Process the message
				MessageControler mc = MessageControler.getInstance();
				try {
					mc.process(textField.getText());
				} catch (MessageControlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// append it to chat
				// screen.append(textField.getText() + "\n");

			}
			
	        try {
				sDoc.insertString(pos, textField.getText(), defaut); pos+=textField.getText().length();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			textField.setText("");
			textField.requestFocus();
		}
	}

	// method to display messages on the screen
	public void displayMessage(String message) {
		StyledDocument document = (StyledDocument) screen.getDocument();

		screen.insertIcon(new ImageIcon("/java-irc/src/main/java/ihm/swag.jpg"));

		try {
			document.insertString(document.getLength(), message + "\n", null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// screen.append(message + "\n");
	}

	// method to get message sent
	public String getTextField() {
		return textField.getText();
	}

	// method to display an error message
	public void displayError(String message) {

		JOptionPane.showMessageDialog(null, message, "Erreur",
				JOptionPane.ERROR_MESSAGE);
	}

	public void displayInfo(String message) {
		JOptionPane.showMessageDialog(null, message,
				"Just to let you know ...", JOptionPane.INFORMATION_MESSAGE);
	}
	
	class keyboardListener implements KeyListener {

		public void keyTyped(KeyEvent eKey) {
			if (eKey.getKeyChar() == Event.ENTER) {
				if (!textField.getText().equals("")) {

					// Process the message
					MessageControler mc = MessageControler.getInstance();
					try {
						mc.process(textField.getText());
					} catch (MessageControlerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				textField.setText("");
				textField.requestFocus();
			}
		}

		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {

		}
	}

}