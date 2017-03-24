package utils;

import java.util.ArrayList;

import org.apache.commons.validator.routines.InetAddressValidator;

import serial.MessageFromServer;
import serial.MessageToServer;

public class MessageControler {
	
	public String nickname = "ClientApp";
	private ConnectionHandler handler = ConnectionHandler.getInstance();
	private String currentChannel = null;
		
	public MessageControler() {
		// do nothing
	}
	
	/**
	 * Point d'entrée, les autres fct sont en privé
	 * @param 
	 * @throws MessageControlerException 
	 */
	public void process(String s) throws MessageControlerException {
		
		// Init message
		MessageToServer msg = this.initMessage(s);
		
		// Command ?
		if(msg.isCommand() && msg.isValidCommand()) {
			processCommand(msg);
		} else if (msg.isCommand()) { //invalid command -> so maybe a message starting by # ?
			
			if (this.canSendMessage()) {
				try {
					this.send(msg);
				} catch (ConnectionHandlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// if no connected to server nor channel -> error, else is a
				// message.
				throw new MessageControlerException("Unknown command \""
						+ msg.getPost() + "\".");
			}
		} else { // Message
			
			// are we connected to a server ?
			// and is nickname set ?
			// are we connected to a channel ?
			if (this.canSendMessage()) {
				try {
					this.send(msg);
				} catch (ConnectionHandlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if (this.handler.isConnectionOpened)
					throw new MessageControlerException("You are not connected to a channel, you can't send message now. Try #JOIN CHANNEL_NAME");					
				else
					throw new MessageControlerException("You are not connected to a server. Try #CONNECT SERVER_IP NICKNAME");

			}
			
		}
	}

	private boolean canSendMessage() {
		// TODO Auto-generated method stub
		return this.handler.isConnectionOpened && this.currentChannel != null;
	}

	private MessageToServer initMessage(String s) {

		
		ArrayList<String> args = new ArrayList<String>();
		String post = new String();
		String nickname = this.nickname;
		
		if (s.startsWith("#")) { // command
			String[] splited = s.split(" ");
			post = splited[0];
			if (splited[1] != null) {
				args.add(splited[1]);
			}// max 2 args
			if (splited[2] != null) {
				args.add(splited[2]);
			}
		} else {
			post = s;
		}
		
		MessageToServer msg = new MessageToServer(nickname, post, args);
		return msg;
	}

	// Function to call if Message is a command
	private void processCommand(MessageToServer msg) throws MessageControlerException {
		
		String command = msg.getPost();
		ArrayList<String> args = msg.getArgs();
		
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
						this.connectToServer(msg);
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
					this.connectToChannel(msg);
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
		return (validator.isValidInet4Address(serverIP) /*|| validator.isValidInet6Address(serverIP)*/); 
	}

	private boolean isValidNickname(String nickname) {
		// String de + de 3 caractères
		return nickname.length() > 3;
	}

	private void connectToServer(MessageToServer msg) throws ConnectionHandlerException {
		
		String serverIP = msg.getArgs().get(0);
		String nickname = msg.getArgs().get(1);
		// connect to server
		if (this.handler.isConnectionOpened)
			throw new ConnectionHandlerException("Already Connected !");
		
		handler.openConnection(serverIP);
		
		// write msg
		this.send(msg);
		
		// read response -> wait x second, then timeout
		// is username ok etc...
		MessageFromServer answer = this.read();
		this.processServerMessage(answer);
		
	}

	private void diconnectFromServer(MessageToServer msg) throws ConnectionHandlerException {

		// write msg
		this.send(msg);

		// close connection
		this.handler.closeConnection();


	}
	
	private void processServerMessage(MessageFromServer msg) {
		
		// check is valid ?
		boolean isValid = msg.isValid();
		if (! isValid) { // alaways for the moment
			
			
		}
		// server or user ?
		boolean isFromServer = msg.isFromServer();
		// case 1  Server
		if (isFromServer) {
			
		}
		// case 2 User
		else {
			
		}
	}

	private void connectToChannel(MessageToServer msg) throws ConnectionHandlerException {

		this.send(msg);

		// Need to check if ok ?
		
		// set channel name for global use
		this.currentChannel = msg.getArgs().get(0);
		
	}

	private void disconnectFromChannel(MessageToServer msg) throws ConnectionHandlerException {

		this.send(msg);

		// Need to check if ok ?
		
		// set channel name for global use
		this.currentChannel = null;
		
	}
	
	private void send(MessageToServer msg) throws ConnectionHandlerException {
		this.handler.write(msg.toString());
	}
	
	private MessageFromServer read() throws ConnectionHandlerException{
		String s = this.handler.read();
		
		MessageFromServer msg = new MessageFromServer(s);
		return msg;
	}
}
