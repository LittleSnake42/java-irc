package utils;

import java.awt.Window;

import serial.MessageFromServer;

public class ClientListener implements Runnable{

	public ClientListener() {
		// TODO Auto-generated constructor stub
	}
	
	public void run() {
		
		ConnectionHandler ch = ConnectionHandler.getInstance();
		
		MessageControler mc = MessageControler.getInstance();
		
		MessageFromServer msg = null;
		
		while(true){
			
			try {
				msg = mc.read();
				mc.processServerMessage(msg);
				msg = null;
			} catch (ConnectionHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
	}

}
