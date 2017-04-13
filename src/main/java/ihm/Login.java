package ihm;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ihm.Channel;
import utils.MessageControler;
import utils.MessageControlerException;

/*
 *  This class is the graphical interface to connect to a server
 */

public class Login extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNickname;
	private JTextField textFieldServer;
	private MessageControler mc = MessageControler.getInstance();

	public Login() {
		// Define the title of the window
		this.setTitle("Client IRC");
		// Close process when clicking on the red cross
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// The window is not resizable
		this.setResizable(false);
		// Define the size of the window
		this.setBounds(100, 100, 460, 261);
		// Puts the window in the center of the screen
		this.setLocationRelativeTo(null);
		
		// contentPane is the main container
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		this.setContentPane(contentPane);
		
		// Creation of the button to validate the nickname and the IP server
		JButton btnLogin = new JButton("Login");
		Icon imgOk = new ImageIcon("image/ok.png");
		btnLogin.setIcon(imgOk);
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		// add action listener on the button to check the nickname and the IP server
		btnLogin.addActionListener(new ValidateConnectionListener());
		btnLogin.setBounds(217, 154, 139, 30);
		contentPane.add(btnLogin);
		
		// Label for the nickname 
		JLabel lblNickname = new JLabel("Nickname");
		lblNickname.setHorizontalAlignment(SwingConstants.LEFT);
		lblNickname.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNickname.setBounds(179, 37, 86, 30);
		contentPane.add(lblNickname);
		
		// Label for the IP server 
		JLabel lblIpServer = new JLabel("IP Server");		
		lblIpServer.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIpServer.setBounds(179, 95, 86, 30);
		contentPane.add(lblIpServer);
		
		// Text field to write the nickname
		textFieldNickname = new JTextField();
		textFieldNickname.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldNickname.setBounds(286, 38, 145, 30);
		contentPane.add(textFieldNickname);
		textFieldNickname.setColumns(10);
		
		// Text field to write the IP server
		textFieldServer = new JTextField();
		textFieldServer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldServer.setBounds(286, 96, 145, 30);
		contentPane.add(textFieldServer);
		textFieldServer.setColumns(10);
		
		// Add an image to the window
		JLabel lblImage = new JLabel("");
		lblImage.setBounds(23, 11, 146, 173);
		Icon imgLogin = new ImageIcon("image/login.png");
		lblImage.setIcon(imgLogin);
		contentPane.add(lblImage);
		
		// Add a listener to the window
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeFrame();
			}
		});
	}
	
	// Check the nickname and the IP server
	public class ValidateConnectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			String strNickname, strIpServer;
			strNickname = textFieldNickname.getText();
			strIpServer = textFieldServer.getText();
			try {
				mc.process("#CONNECT " + strIpServer + " " + strNickname);
			} catch (MessageControlerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			// Is ok ? 
			if (mc.isConnectionOpened()) {
			// close frame, open next
				
				dispose();
				Channel channel = new Channel();
				channel.setVisible(true);
			
			}
						
		}
	}
	
	// This method allow to request a confirmation before closing the application
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

}
