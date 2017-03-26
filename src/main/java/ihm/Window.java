package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import utils.MessageControler;
import utils.MessageControlerException;

// graphical interface
public class Window extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel container = new JPanel();
	private JTextPane screen = new JTextPane();
	private JTextArea users = new JTextArea();
	private JTextField textField = new JTextField();
	private JComboBox<?> combo = new JComboBox<Object>();
	private JButton button = new JButton("Send");

	public ImageIcon icon = new ImageIcon("swag.jpg", "Titre");

	private static Window INSTANCE = new Window();

	private Window() {
		this.setTitle("Client IRC");
		this.setMinimumSize(new Dimension(600, 400));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		initComposant();

		this.setContentPane(container);
		ImageIcon img = new ImageIcon("images/swag.jpg");
		this.setIconImage(img.getImage());
		// this.setVisible(true);
	}

	public static Window getInstance() {
		return INSTANCE;
	}

	// add composants
	private void initComposant() {

		// screen
		screen.setEditable(false);
		// screen.setLineWrap(true);

		users.setPreferredSize(new Dimension(150, 190));
		users.setEditable(false);
		users.setLineWrap(true);

		// smiley
		String[] smiley = { "o( ><)o", "(>_<)", "�\\_(�_�)_/�", ":S", "=(" };
		combo = new JComboBox<Object>(smiley);
		combo.setForeground(Color.blue);

		// add listeners
		combo.addActionListener(new ItemAction());
		button.addActionListener(new BoutonListener());
		textField.addKeyListener(new keyboardListener());

		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());

		// boutons
		JPanel buttons = new JPanel();
		buttons.add(combo);
		buttons.add(button);

		// main container
		container.setLayout(new BorderLayout());

		// SOUTH
		bottom.add(textField, BorderLayout.CENTER);
		bottom.add(buttons, BorderLayout.EAST);
		bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		container.add(new JScrollPane(screen), BorderLayout.CENTER);
		//container.add(new JScrollPane(users), BorderLayout.EAST);
		container.add(bottom, BorderLayout.SOUTH);
	}

	// class listener smiley
	class ItemAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			textField.setText(textField.getText() + " "
					+ combo.getSelectedItem());

			// focus
			textField.requestFocus();
		}
	}

	// class listener button SEND
	class BoutonListener implements ActionListener {

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

	// method to display online users
	public void displayUsers(String user) {
		users.append(user + "\n");
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