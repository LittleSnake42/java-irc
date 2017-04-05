package ihm;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FrameConnection extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel labelNickname,labelIpServer;
	private JTextField nickname,ipServer;
	private JButton validate;


	public FrameConnection(){

		super();
		this.setTitle("Client IRC");
		this.setSize(new Dimension(400,200));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		
		labelNickname = new JLabel("Nickname");
		nickname = new JTextField();
		
		labelIpServer = new JLabel("IP Server");
		ipServer = new JTextField();
		
		validate = new JButton("Login");
		
		
		Container content = this.getContentPane();
		content.setLayout(null);
		
		content.add(labelNickname);
		labelNickname.setBounds(30, 50, 100, 20);
		
		content.add(nickname);
		nickname.setBounds(150, 50, 160, 20);
		
		content.add(labelIpServer);
		labelIpServer.setBounds(30, 85, 100, 20);
		
		content.add(ipServer);
		ipServer.setBounds(150, 85, 160, 20);
		
		content.add(validate);
		validate.setBounds(150, 130, 87, 20 );
		
		validate.addActionListener(new ValidateConnectionListener());
		
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeFrame();
			}
		});
	
	}
	
	
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
	
	
	public class ValidateConnectionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String strNickname, strIpServer;
			
			strNickname = nickname.getText();
			strIpServer = ipServer.getText();
			
			if (strNickname.isEmpty() || strIpServer.isEmpty()) {
				
				JOptionPane.showMessageDialog(null, "Nickname or IP server incorrect", "Connection error", JOptionPane.ERROR_MESSAGE);
			
			} else {
				
				JOptionPane.showMessageDialog(null, "Nickname or IP server is correct", "Successfully connected", JOptionPane.INFORMATION_MESSAGE);
				dispose();
				FrameChannel channel = new FrameChannel();
				channel.setVisible(true);
				
			}
			
		}
	}

}