package launcher;

import ihm.Chat;
import ihm.Login;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Launcher {

	//private static final Logger LOG = Logger.getLogger(Launcher.class.getName());
	
	private Launcher() {
		// Useless
	}
	
	public static void main(String[] args) {
		
		//LOG.info("Init Cient");
		//LOG.info("Enjoy boys");

		//TODO penser à fermer thread qui lit on disconnect
		
		// TODO SI on ferme la fenetre channel, comme on est connecté, on ferme la connection
		
		// TODO #QUIT on quitte channel
		// #EXIT ON QUITTE LE SERVEUR 

		//TODO check si le serveur est ok (fermeture intempestive) -> thread qui lit

		
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Chat.getInstance();
		
		Login login = new Login();
		login.setVisible(true);
		
//		// read ? not tested
//		ClientListener cl = new ClientListener();
//
//		Thread t = new Thread( cl );
//		t.start();
			
	}

}
