package utils;

import java.util.ArrayList;

public class MessageControler {
	
	public MessageControler() {
		// do nothing
	}

	// Function to call if Message is a command
	public void processCommand(String command, ArrayList<String> args) throws MessageControlerException {
		// Case #CONNECT : we expect 2 args (ip, nick)
		if (command.toUpperCase() == "#CONNECT") {
			
			if (args.size() == 2) {
				String serverIP = args.get(0);
				String nickname = args.get(1);
				
				//check if args are OK
				this.isIpAddress(serverIP);
				this.isNickname(nickname);
				
				//if ok try to connect
				this.connectToServer(serverIP, nickname);
				
			} else {
				throw new MessageControlerException("#CONNECT expects two args, the target server ip and a nickname.");
			}
			
		} else if (command.toUpperCase() == "#JOIN") {

			if (args.size() == 1) {
				String channel = args.get(0);				
				//check if args are OK
				
				//if ok try to connect
				this.connectToChannel(channel);
				
			} else {
				throw new MessageControlerException("#JOIN expects 1 args, the target channel name.");
			}
			
		} else if (command.toUpperCase() == "#QUIT") {

		} else if (command.toUpperCase() == "#EXIT") {

		} else {
			throw new MessageControlerException("Not a valid command. RTFM :)");
		}
	}

	private void isIpAddress(String serverIP) throws MessageControlerException {
		// Mettre en place une regexp pour checker que c'est une IP
		
	}

	private void isNickname(String nickname) throws MessageControlerException {
		// String de + de 3 caractères
		
	}

	private void connectToServer(String serverIP, String nickname) throws MessageControlerException {
		// gère la connexion au serveur
		// -> appel une methode du package io ?
		
	}
	
	private void connectToChannel(String channel) {
		// TODO Auto-generated method stub
		
	}
}
