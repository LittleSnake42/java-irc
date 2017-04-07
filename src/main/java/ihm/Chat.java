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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import utils.MessageControler;
import utils.MessageControlerException;

/*
 * This class is the graphical interface for displaying chat messages
 */

public class Chat extends JFrame {

	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Chat INSTANCE = new Chat();
	private JPanel contentPane;
	private JTextArea textArea;
	private JComboBox<Object> comboBox;
	private StyledDocument doc;
	private SimpleAttributeSet styleNormal;


	/*
	 * Create the frame.
	 */
	public Chat() {
		this.setTitle("Client IRC");
		this.setMinimumSize(new Dimension(300, 200));
		this.setSize(800, 500);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelTop = new JPanel();
		contentPane.add(panelTop, BorderLayout.NORTH);
		panelTop.setLayout(new BorderLayout(0, 0));
		
		JPanel panelTopEast = new JPanel();
		panelTop.add(panelTopEast, BorderLayout.EAST);
		
		JButton btnChannel = new JButton("Change channel");
		btnChannel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnChannel.addActionListener(new ChannelListener());
		panelTopEast.add(btnChannel);
		
		JButton btnLogout = new JButton("Logout");
		Icon imgLogout = new ImageIcon("image/logout.png");
		btnLogout.setIcon(imgLogout);
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogout.addActionListener(new LogoutListener());
		panelTopEast.add(btnLogout);
		
		JPanel panelBottom = new JPanel();
		contentPane.add(panelBottom, BorderLayout.SOUTH);
		panelBottom.setLayout(new BorderLayout(0, 0));
		panelBottom.setBorder(BorderFactory.createEmptyBorder(10,0,5,0));
		
		JPanel panelBottomEast = new JPanel();
		panelBottom.add(panelBottomEast, BorderLayout.EAST);
		panelBottomEast.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		// components for smileys		
		ImageIcon[] emoticon = {
			new ImageIcon("image/grinning.png"),
			new ImageIcon("image/grin.png"),
			new ImageIcon("image/laughing.png")				
		};
		
		comboBox = new JComboBox<Object>(emoticon);
		comboBox.setPreferredSize(new Dimension(60,30));
		comboBox.addActionListener(new ItemAction());
		panelBottomEast.add(comboBox);
		
		JButton btnOk = new JButton("OK");
		btnOk.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnOk.addActionListener(new SendListener());
		panelBottomEast.add(btnOk);
		
		Panel panelBottomCenter = new Panel();
		panelBottom.add(panelBottomCenter, BorderLayout.CENTER);
		panelBottomCenter.setLayout(new BoxLayout(panelBottomCenter, BoxLayout.X_AXIS));
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.addKeyListener(new keyboardListener());
		textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		textArea.setCaretPosition(textArea.getText().length());
		panelBottomCenter.add(textArea);
		
		JPanel panelCenter = new JPanel();
		panelCenter.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		    JScrollPane sp = new JScrollPane(textPane);
		panelCenter.add(sp);
		
		styleNormal = new SimpleAttributeSet();
		StyleConstants.setFontFamily(styleNormal, "Calibri");
		StyleConstants.setFontSize(styleNormal, 14);
		
		doc = textPane.getStyledDocument();
		
		/*try {
			
			
			doc.insertString(doc.getLength(), "N-impn test qui va fonctionnerCeci est un test qui va fonctionnerCeci est un test qui va fonctionner"+"\n\n", styleNormal);
			doc.insertString(doc.getLength(), "CecCeci est un test qui va fonctionnerCeci est un test qui va fonctionnerCeci est un test qui va fonctionnerr"+"\n", styleNormal);
			
			// The image must first be wrapped in a style
		    Style grin = doc.addStyle("StyleName", null);
		    StyleConstants.setIcon(grin, new ImageIcon("image/grin.png"));
		    
		    Style grinning = doc.addStyle("StyleName", null);
		    StyleConstants.setIcon(grinning, new ImageIcon("image/grinning.png"));
		    
		    Style laughing = doc.addStyle("StyleName", null);
		    StyleConstants.setIcon(laughing, new ImageIcon("image/laughing.png"));
		 
		    doc.insertString(doc.getLength(), "ignored text", grin);
		    doc.insertString(doc.getLength(), "ignored text", grinning);
		    doc.insertString(doc.getLength(), "ignored text", laughing);
			doc.insertString(doc.getLength(), "\nCecCeci est un test qui va fonctionnerCeci est un test qui va fonctionnerCeci est un test qui va fonctionnerr"+"\n", styleNormal);

				
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}*/
		
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
	
	// this method allow to display messages on the screen
	public void displayMessage(String message) {
		
	}

	// this method allow to get message sent
	public String getTextField() {
		return textArea.getText();
	}

	// this method allow to display an error message
	public void displayError(String message) {
		JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	// this method allow to request a confirmation before closing the application
	public void closeFrame() {
		ImageIcon imageQuestion = new ImageIcon("image/question.png");
		int answer = JOptionPane.showConfirmDialog(this,
                "Are you sure you wish to close? ",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                imageQuestion);
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
			Channel channel = new Channel();
			channel.setVisible(true);
			channel.setLocationRelativeTo(null);
			
		}

	}
	
	// class listener button "Logout"
	public class LogoutListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			dispose();
			Login login = new Login();
			login.setVisible(true);
			login.setLocationRelativeTo(null);
			
		}
	}

	// class listener smiley
	class ItemAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String smiley[] = {":grinning:", ":grin:", ":laughing:"};
			textArea.setText(textArea.getText() + smiley[comboBox.getSelectedIndex()] + " ");
			// focus
			textArea.requestFocus();
		}               
	}

	// class listener button SEND
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
				// append it to chat
				try {
					doc.insertString(doc.getLength(), textArea.getText()+"\n", styleNormal);
				} catch (BadLocationException e) {
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
					// append it to chat
					try {
						doc.insertString(doc.getLength(), textArea.getText(), styleNormal);
					} catch (BadLocationException e) {
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

}
