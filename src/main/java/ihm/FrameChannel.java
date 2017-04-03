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

public class FrameChannel extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel labelTitle;
	private JTextField channel;
	private JButton validate,cancel;


	public FrameChannel(){

		super();
		this.setTitle("Client IRC");
		this.setSize(new Dimension(400,200));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
	
		labelTitle = new JLabel("CHANNEL");
		
		channel = new JTextField();
		
		validate = new JButton("OK");
		cancel = new JButton("Cancel");
		
		
		Container content = this.getContentPane();
		content.setLayout(null);
		
		content.add(labelTitle);
		labelTitle.setBounds(170, 20, 100, 20);
		
		content.add(channel);
		channel.setBounds(120, 70, 160, 20);
		
		content.add(validate);
		validate.setBounds(100, 120, 87, 20);
		
		content.add(cancel);
		cancel.setBounds(210, 120, 87, 20);
		
		validate.addActionListener(new ValidateChannelListener());
		cancel.addActionListener(new CancelListener());
		
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
	
	
	public class ValidateChannelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String strChannel;
			
			strChannel = channel.getText();
			
			if (strChannel.isEmpty()) {
				
				JOptionPane.showMessageDialog(null, "Channel incorrect", "Connection error", JOptionPane.ERROR_MESSAGE);
			
			} else {
				
				JOptionPane.showMessageDialog(null, "Channel is correct", "Successfully connected", JOptionPane.INFORMATION_MESSAGE);
				dispose();
				Window window = Window.getInstance();
				window.setVisible(true);
				
			}
			
		}
	}
		
	public class CancelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			dispose();
			FrameConnection window = new FrameConnection();
			window.setVisible(true);
			
		}
	}
}