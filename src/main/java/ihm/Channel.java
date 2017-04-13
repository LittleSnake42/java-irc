package ihm;

import java.awt.EventQueue;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import utils.MessageControler;
import utils.MessageControlerException;
import ihm.Login.ValidateConnectionListener;

/*
 *  This class is the graphical interface to choose a channel
 */

public class Channel extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldChannel;
	private MessageControler msg = MessageControler.getInstance();

	public Channel() {
		// Define the title of the window
		this.setTitle("Chat IRC");
		// The window is not resizable
		this.setResizable(false);
		// Close process when clicking on the red cross
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Define the size of the window
		this.setBounds(100, 100, 460, 261);
		// Puts the window in the center of the screen
		this.setLocationRelativeTo(null);
		
		// contentPane is the main container
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Creation of the button to validate the channel
		JButton btnOk = new JButton("OK");
		Icon imgOk = new ImageIcon("image/ok.png");
		btnOk.setIcon(imgOk);
		btnOk.setFont(new Font("Tahoma", Font.BOLD, 14));
		// add action listener on the button
		btnOk.addActionListener(new ChannelListener());
		btnOk.setBounds(164, 152, 104, 30);
		contentPane.add(btnOk);
		
		// Label for the channel
		JLabel lblChannel = new JLabel("Channel");
		lblChannel.setHorizontalAlignment(SwingConstants.LEFT);
		lblChannel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblChannel.setBounds(187, 30, 72, 30);
		contentPane.add(lblChannel);
		
		// Text field to write the channel
		textFieldChannel = new JTextField();
		textFieldChannel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldChannel.setBounds(115, 85, 203, 30);
		textFieldChannel.setColumns(10);
		contentPane.add(textFieldChannel);
		
		// Add a listener to the window
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeFrame();
			}
		});
	}
	
	// Check the channel
	public class ChannelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String strChannel;
		
			strChannel = textFieldChannel.getText();
			try {
				msg.process("#JOIN " + strChannel);
			} catch (MessageControlerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
			dispose();
			Chat chat = Chat.getInstance();
			chat.setVisible(true);
			
		}
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
			dispose();
		}
	}
}
