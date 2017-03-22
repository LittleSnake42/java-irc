package utils;

import java.util.ArrayList;

import org.apache.commons.validator.routines.InetAddressValidator;

import serial.MessageFromServer;
import serial.MessageToServer;

public class MessageControler {
		
	public MessageControler() {
		// do nothing
	}
	
	public void process(String s) {
		MessageToServer msg = new MessageToServer(s);
	}

	// Function to call if Message is a command
	public void processCommand(String command, ArrayList<String> args) throws MessageControlerException {
		// Case #CONNECT : we expect 2 args (ip, nick)
		if (command.toUpperCase() == "#CONNECT") {
			
			if (args.size() == 2) {
				String serverIP = args.get(0);
				String nickname = args.get(1);
				
				//check if args are OK
				boolean isValidIP = this.isValidIpAddress(serverIP);
				boolean isValidNickname = this.isValidNickname(nickname);
				
				// if not maybe in wrong order?
				
				//if ok try to connect
				if (isValidIP && isValidNickname) {
					try {
						this.connectToServer(serverIP, nickname);
					} catch (ConnectionHandlerException e) {
						throw new MessageControlerException(
								"An error occured attempting to connect to IP \""
										+ serverIP + "\" with nickname \""
										+ nickname + "\".", e);
					}
				}
				
			} else {
				throw new MessageControlerException("#CONNECT expects two args, the target server ip and a nickname.");
			}
			
		} else if (command.toUpperCase() == "#JOIN") {

			if (args.size() == 1) {
				String channel = args.get(0);				
				//check if args are OK
				
				//if ok try to connect
				try {
					this.connectToChannel(channel);
				} catch (ConnectionHandlerException e) {
					// TODO Auto-generated catch block
					throw new MessageControlerException(
							"Unabled to join channel \"" + channel + "\".", e);
				}
				
			} else {
				throw new MessageControlerException("#JOIN expects 1 args, the target channel name.");
			}
			
		} else if (command.toUpperCase() == "#QUIT") {

		} else if (command.toUpperCase() == "#EXIT") {

		} else {
			throw new MessageControlerException("Not a valid command. RTFM :)");
		}
	}

	// TODO nothing to do here
	private boolean isValidIpAddress(String serverIP) {
		InetAddressValidator validator = new InetAddressValidator();	
		return (validator.isValidInet4Address(serverIP) || validator.isValidInet6Address(serverIP)); 
	}

	private boolean isValidNickname(String nickname) {
		// String de + de 3 caractères
		return nickname.length() > 3;
	}

	private void connectToServer(String serverIP, String nickname) throws ConnectionHandlerException {
		// connect to server
		// TODO singleton getInstance + check isOpened connection
		ConnectionHandler handler = new ConnectionHandler();
		handler.openConnection(serverIP);
		
		// init MSG
		ArrayList<String> args = new ArrayList<String>();
		args.add(serverIP);
		args.add(nickname);
		MessageToServer msg = new MessageToServer(nickname, "#connect", args);
		// write msg
		handler.write(msg.toString());
		
		// read response -> wait x second, then timeout
		// is username ok etc...
		String s = handler.read();
		this.processServerMessage(s);
		
	}
	
	private void processServerMessage(String s) {

		// init server message
		MessageFromServer msgFromServer = new MessageFromServer(s);
		
		// check is valid ?
		boolean isValid = msgFromServer.isValid();
		if (! isValid) {
			
		}
		// server or user ?
		boolean isFromServer = msgFromServer.isFromServer();
		// case 1  Server
		if (isFromServer) {
			
		}
		// case 2 User
		else {
			
		}
	}

	private void connectToChannel(String channel) throws ConnectionHandlerException {
		// TODO Auto-generated method stub
		ConnectionHandler handler = new ConnectionHandler();
	}
}
