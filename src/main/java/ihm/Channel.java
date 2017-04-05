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

import ihm.Login.ValidateConnectionListener;

public class Channel extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldChannel;


	/**
	 * Create the frame to choose the channel
	 */
	public Channel() {
		setTitle("Chat IRC");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 460, 261);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnOk = new JButton("OK");
		Icon imgOk = new ImageIcon("image/ok.png");
		btnOk.setIcon(imgOk);
		btnOk.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnOk.addActionListener(new ChannelListener());
		btnOk.setBounds(164, 157, 104, 30);
		contentPane.add(btnOk);
		
		JLabel lblChannel = new JLabel("Channel");
		lblChannel.setHorizontalAlignment(SwingConstants.LEFT);
		lblChannel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblChannel.setBounds(186, 41, 72, 30);
		contentPane.add(lblChannel);
		
		textFieldChannel = new JTextField();
		textFieldChannel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldChannel.setBounds(115, 94, 203, 30);
		contentPane.add(textFieldChannel);
		textFieldChannel.setColumns(10);
		
		JButton btnLogout = new JButton("Logout");
		Icon imgLogout = new ImageIcon("image/logout.png");
		btnLogout.setIcon(imgLogout);
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogout.addActionListener(new LogoutListener());
		btnLogout.setBounds(346, 11, 98, 30);
		contentPane.add(btnLogout);
		
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeFrame();
			}
		});
	}
	
	
	public class ChannelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String strChannel;
			
			strChannel = textFieldChannel.getText();
			
			if (strChannel.isEmpty()) {
				
				ImageIcon imgError = new ImageIcon("image/error.png");
			    JOptionPane.showMessageDialog(null, "Channel incorrect", "Erreur", JOptionPane.ERROR_MESSAGE, imgError);
			
			} else {
				
				ImageIcon imgInfo = new ImageIcon("image/info.png");				
				JOptionPane.showMessageDialog(null, "Channel is correct", "Successfully connected", JOptionPane.INFORMATION_MESSAGE, imgInfo);
				
				dispose();
				Window window = Window.getInstance();
				window.setVisible(true);
				window.setLocationRelativeTo(null);
				
			}
			
		}
	}
	
	
	public class LogoutListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			dispose();
			Login login = new Login();
			login.setVisible(true);
			login.setLocationRelativeTo(null);
			
		}
	}
	
	
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
