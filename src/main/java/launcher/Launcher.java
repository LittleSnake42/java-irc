package launcher;

import ihm.Window;

import java.util.Scanner;

import utils.ConnectionHandlerException;
import utils.MessageControler;
import utils.MessageControlerException;

public class Launcher {

	//private static final Logger LOG = Logger.getLogger(Launcher.class.getName());
	
	private Launcher() {
		// Useless
	}

	/*public static void main(String[] args) {

		ArrayList<String> args1 = new ArrayList<String>();
		MessageToServer msgToServer = new MessageToServer("LittleSnake", "Mon message", args1);

		System.out.println(msgToServer);
		
		String json = "{\"nickname\":\"server\", \"post\":\"vous êtes connecté au chat IRC. Bravo.\"}"; 
				
		try {
			
			MessageFromServer msgFromServer = new MessageFromServer(json);
			//To write in writer -> msgFromServer.write(writer);
			
		
			System.out.println(json);
			System.out.println(msgFromServer.getNickname());
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

	}*/
	
	public static void main(String[] args) {
		
		//LOG.info("Init Cient");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		MessageControler mc = new MessageControler();
		
		
		// open window
		Window w = Window.getInstance();
		w.setVisible(true);
		
		// lancer deuxieme thread pour lire?

		while (true) {

			try {
				//String msg = sc.nextLine();

				//mc.process(msg);
				
				// bloquant
				//
				mc.read();
				
			//} catch (MessageControlerException e) {
				//LOG.error("error ...", e);
				//System.err.println(e);
			} catch (ConnectionHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				
			}
		}
		
	}

}
