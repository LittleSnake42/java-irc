package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// graphical interface
public class Window extends JFrame {
	
  private JPanel container = new JPanel();
  private JTextArea screen = new JTextArea();
  private JTextArea users = new JTextArea();
  private JTextField jtf = new JTextField();
  private JComboBox combo = new JComboBox();
  private JButton button = new JButton("Send");

  
  public Window() {
	this.setTitle("Client IRC");
	this.setSize(800, 600);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLocationRelativeTo(null);
	
	initComposant();
	
	this.setContentPane(container);
	this.setVisible(true);            
  }
  
  
 // add composants
  private void initComposant(){
	
	// screen
  	screen.setEditable(false);
  	screen.setLineWrap(true);
       
    users.setPreferredSize(new Dimension(150, 190));
    users.setEditable(false);
    users.setLineWrap(true);
    
    // smiley
    String[] smiley = {"o( ><)o", "(>_<)", "¯\\_(°_°)_/¯", ":S", "=("};
    combo = new JComboBox(smiley);
    combo.setForeground(Color.blue);
    
    
    // add listeners
    combo.addActionListener(new ItemAction());
    button.addActionListener(new BoutonListener());

    
    JPanel bottom = new JPanel();
    bottom.setLayout(new BorderLayout());
    
    // boutons
    JPanel buttons = new JPanel();
    buttons.add(combo);
    buttons.add(button);

    
    // main container
   	container.setLayout(new BorderLayout());
    
    // SOUTH
    bottom.add(jtf, BorderLayout.CENTER);
    bottom.add(buttons, BorderLayout.EAST);
    bottom.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    
    container.add(new JScrollPane(screen), BorderLayout.CENTER);
    container.add(new JScrollPane(users), BorderLayout.EAST);
    container.add(bottom, BorderLayout.SOUTH);
  }
  
  class ItemAction implements ActionListener{
    public void actionPerformed(ActionEvent e) {
    	jtf.setText(jtf.getText() + " " + combo.getSelectedItem());
    	
    	// focus
    	jtf.requestFocus();
    }               
  }
  
  // Classe écoutant le bouton ENVOYER
  class BoutonListener implements ActionListener{
    // Redéfinition de la méthode actionPerformed()
    public void actionPerformed(ActionEvent arg0) {
    	// affichage du texte sur l'écran
    	if (jtf.getText().equals("")) {
    		
    	} else {
    		screen.append(jtf.getText() + "\n");
    	}
    	
    	jtf.setText("");
    	jtf.requestFocus();
    }
  }
}


