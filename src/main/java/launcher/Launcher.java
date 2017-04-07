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

		//TODO penser à fermer socket

		//TODO check si le serveur est ok (fermeture intempestive)
		
		//TODO connect -> si username non valide !!

		//TODO logs fichiers

		
		// open window
		Window w = Window.getInstance();
		w.setVisible(true);
			
	}

}
