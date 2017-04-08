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
import utils.MessageControler;
import utils.MessageControlerException;

public class Login extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNickname;
	private JTextField textFieldServer;
	private MessageControler msg = MessageControler.getInstance();

	/**
	 * Create the frame to login
	 */
	public Login() {
	
		this.setTitle("Client IRC");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setBounds(100, 100, 460, 261);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		this.setContentPane(contentPane);
		
		JButton btnLogin = new JButton("Login");
		Icon imgOk = new ImageIcon("image/ok.png");
		btnLogin.setIcon(imgOk);
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogin.addActionListener(new ValidateConnectionListener());
		btnLogin.setBounds(217, 154, 139, 30);
		contentPane.add(btnLogin);
		
		JLabel lblNickname = new JLabel("Nickname");
		lblNickname.setHorizontalAlignment(SwingConstants.LEFT);
		lblNickname.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNickname.setBounds(179, 37, 86, 30);
		contentPane.add(lblNickname);
		
		JLabel lblIpServer = new JLabel("IP Server");		
		lblIpServer.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIpServer.setBounds(179, 95, 86, 30);
		contentPane.add(lblIpServer);
		
		textFieldNickname = new JTextField();
		textFieldNickname.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldNickname.setBounds(286, 38, 145, 30);
		contentPane.add(textFieldNickname);
		textFieldNickname.setColumns(10);
		
		textFieldServer = new JTextField();
		textFieldServer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldServer.setBounds(286, 96, 145, 30);
		contentPane.add(textFieldServer);
		textFieldServer.setColumns(10);
		
		JLabel lblImage = new JLabel("");
		lblImage.setBounds(23, 11, 146, 173);
		Icon imgLogin = new ImageIcon("image/login.png");
		lblImage.setIcon(imgLogin);
		contentPane.add(lblImage);
		
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeFrame();
			}
		});
	}
	
	
	public class ValidateConnectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			String strNickname, strIpServer;
			strNickname = textFieldNickname.getText();
			strIpServer = textFieldServer.getText();
			try {
				msg.process("#connect " + strIpServer + " " + strNickname);
			} catch (MessageControlerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			/*
			if (strNickname.isEmpty() || strIpServer.isEmpty()) {
				
				ImageIcon imgError = new ImageIcon("image/error.png");
			    JOptionPane.showMessageDialog(null, "Nickname or IP server incorrect", "Erreur", JOptionPane.ERROR_MESSAGE, imgError);
			
			} else {
				
				ImageIcon imgInfo = new ImageIcon("image/info.png");
				JOptionPane.showMessageDialog(null, "Nickname or IP server is correct", "Successfully connected", JOptionPane.INFORMATION_MESSAGE, imgInfo);
				*/
				dispose();
				Channel channel = new Channel();
				channel.setVisible(true);
				channel.setLocationRelativeTo(null);
			
			
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
