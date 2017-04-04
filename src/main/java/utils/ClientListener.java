package utils;

import serial.MessageFromServer;

public class ClientListener implements Runnable {

	public ClientListener() {
		// TODO Auto-generated constructor stub
	}

	public void run() {

		MessageControler mc = MessageControler.getInstance();

		MessageFromServer msg = null;

		while (true) {

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
