package launcher;

import java.util.ArrayList;

import serial.MessageFromServer;
import serial.MessageToServer;

public class Launcher {

	private Launcher() {
		// Useless
	}

	public static void main(String[] args) {

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
	

	}
}
