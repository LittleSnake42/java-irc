package launcher;

import io.ClientException;
import io.MainClient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import serial.MessageFromServer;
import serial.MessageToServer;
import utils.MessageControler;
import utils.MessageControlerException;

public class Launcher {

	private static final Logger LOG = Logger.getLogger(MainClient.class
			.getName());
	
	private Scanner sc = null;

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
		Launcher l = new Launcher();
		
		LOG.info("Init Cient");
		l.sc = new Scanner(System.in);
		MessageControler mc = new MessageControler();

		while (true) {

			//try {

				final String msg = l.sc.nextLine();
				
				mc.process(msg);
				
			//} catch (MessageControlerException e) {
				//LOG.error("error ...", e);
			//} finally {
				
			//}
		}
	}

}
