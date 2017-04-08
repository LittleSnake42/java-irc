package launcher;

import ihm.Window;

import org.apache.log4j.Logger;

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
		
		//TODO connect -> si username non valide !!

		//TODO logs fichiers

		
		// open window
		Window w = Window.getInstance();
		w.setVisible(true);
			
	}

}
