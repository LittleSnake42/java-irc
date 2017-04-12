package utils;

import ihm.Chat;

import java.util.ArrayList;

import org.apache.commons.validator.routines.InetAddressValidator;

import serial.MessageFromServer;
import serial.MessageToServer;

// rename MainController

public class MessageControler {

	// Global variables
	public String nickname = "ClientApp";
	private String currentChannel = null;
	private static MessageControler INSTANCE = new MessageControler();
	private ClientListener listener = null;

	// Components
	private ConnectionHandler handler = ConnectionHandler.getInstance();
	private Chat window = Chat.getInstance();

	// Singleton
	private MessageControler() {
		// do nothing
	}

	public static MessageControler getInstance() {
		return INSTANCE;
	}

	/**
	 * Point d'entrée, les autres fct sont en privé
	 * 
	 * @param
	 * @throws MessageControlerException
	 */
	public void process(String s) throws MessageControlerException {

		// Init message
		MessageToServer msg = this.initMessage(s);

		// Command ?
		if (msg.isCommand() && msg.isValidCommand()) {
			processCommand(msg);
		} else if (msg.isCommand()) { // invalid command -> so maybe a message
										// starting by # ?

			if (this.canSendMessage()) {
				try {

					this.send(msg);

				} catch (ConnectionHandlerException e) {

					e.printStackTrace();
				}
			} else {
				// if no connected to server nor channel -> error, else is a
				// message
				String message = "Unknown command \""+ msg.getPost() + "\".";
				//throw new MessageControlerException(message);
				this.window.displayError(message);
			}
		} else { // Message

			// are we connected to a server ?
			// and is nickname set ?
			// are we connected to a channel ?
			if (this.canSendMessage()) {
				try {
					this.send(msg);
				} catch (ConnectionHandlerException e) {
					e.printStackTrace();
				}
			} else {
				if (this.handler.isConnectionOpened) {
					String message = "You are not connected to a channel, you can't send message now. Try #JOIN CHANNEL_NAME";
					this.window.displayError(message);
				} else {
					String message = "You are not connected to a server. Try #CONNECT SERVER_IP NICKNAME";
					this.window.displayError(message);
				}

			}

		}
	}

	/**
	 * Functions to process messages
	 */
	private MessageToServer initMessage(String s) {

		ArrayList<String> args = new ArrayList<String>();
		String post = new String();
		String nickname = this.nickname;

		if (s.startsWith("#")) { // command
			String[] splited = s.split(" ");
			post = splited[0];
			if (splited.length > 1 && splited[1] != null) {
				args.add(splited[1]);
			}// max 2 args
			if (splited.length > 2 && splited[2] != null) {
				args.add(splited[2]);
			}
		} else {
			post = s;
		}

		MessageToServer msg = new MessageToServer(nickname, post, args);
		return msg;
	}

	protected void processServerMessage(MessageFromServer msg) {

		// check is valid ?
		boolean isValid = msg.isValid();
		if (!isValid) { // always for the moment

		}
		// server , error or user ?
		boolean isFromServer = msg.isFromServer();
		boolean isError = msg.isError();
		
		// case 1 Server
		if (isFromServer) {
			window.displayInfo(msg.getPost());
		} else if (isError) {
			//case 2 error, return a error to block processing (eg already connected to a channel or nick already in use)
		}
		// case 3 User
		else {
			window.displayMessage(msg.getPost(), msg.getNickname());
		}
	}

	// Function to call if Message is a command
	private void processCommand(MessageToServer msg)
			throws MessageControlerException {

		String command = msg.getPost();
		ArrayList<String> args = msg.getArgs();

		// Case #CONNECT : we expect 2 args (ip, nick)
		if (command.toUpperCase().equals("#CONNECT")) {

			if (args.size() == 2) {
				String serverIP = args.get(0);
				String nickname = args.get(1);

				// check if args are OK
				boolean isValidIP = this.isValidIpAddress(serverIP);
				boolean isValidNickname = this.isValidNickname(nickname);

				// if ok try to connect
				if (isValidIP && isValidNickname) {
					try {
						this.connectToServer(msg);
					} catch (ConnectionHandlerException e) {
						// System.err.println(e);
						String message = "An error occured attempting to connect to IP \""
								+ serverIP
								+ "\" with nickname \""
								+ nickname
								+ "\".";
						this.window.displayError(message);
						// Throw execption to stop execution
						throw new MessageControlerException(message);
					}
				} else {
					this.window.displayError("ERROR : nickname or ip not valid");
				}

			} else {
				String message = "#CONNECT expects two args, the target server ip and a nickname.";
				//throw new MessageControlerException(message);
				this.window.displayError(message);
			}

		} else if (command.toUpperCase().equals("#JOIN")) {

			if (args.size() == 1) {
				String channel = args.get(0);

				try {
					this.connectToChannel(msg);
				} catch (ConnectionHandlerException e) {
					// TODO Auto-generated catch block
					String message = "Unabled to join channel \"" + channel + "\".";
					this.window.displayError(message);
					System.err.println(e.getMessage());
					//throw new MessageControlerException(message, e);
				}

			} else {
				String message = "#JOIN expects 1 args, the target channel name.";
				this.window.displayError(message);
				//throw new MessageControlerException(message);
			}

		}
		// This case is for disconnect from channel
		else if (command.toUpperCase().equals("#QUIT")) {
			try {
				this.disconnectFromChannel(msg);
			} catch (ConnectionHandlerException e) {
				String message = "Error attempting to #QUIT";
				this.window.displayError(message);
				//throw new MessageControlerException(message, e);
			}

		}
		// This case is for leaving the server
		else if (command.toUpperCase().equals("#EXIT")) {
			try {
				this.disconnectFromServer(msg);
			} catch (ConnectionHandlerException e) {
				String message = "Error attempting to #EXIT";
				this.window.displayError(message);
				//throw new MessageControlerException(message, e);
			}
		} else {
			window.displayError("WTF !? RTMF Bitch, unsupported command!");
			//throw new MessageControlerException("Not a valid command. RTFM :)");
		}
	}

	/**
	 * Functions isSomething, canDoSomething
	 */

	private boolean canSendMessage() {
		// TODO Auto-generated method stub
		return this.handler.isConnectionOpened && this.currentChannel != null;
	}

	private boolean isValidIpAddress(String serverIP) {
		InetAddressValidator validator = new InetAddressValidator();
		return (validator.isValidInet4Address(serverIP) /*
														 * || validator.
														 * isValidInet6Address
														 * (serverIP)
														 */);
	}

	private boolean isValidNickname(String nickname) {
		// String de + de 3 caractères
		return nickname.length() > 3;
	}

	/*
	 * - Functions to interact with the Connection handler
	 */

	/*
	 * Connect
	 */

	private void connectToServer(MessageToServer msg)
			throws ConnectionHandlerException {

		String serverIP = msg.getArgs().get(0);
		String nickname = msg.getArgs().get(1);

		// connect to server
		if (this.handler.isConnectionOpened) {
			this.window
					.displayError("Ooops, seems you're already connected ....");
			// throw new ConnectionHandlerException("Already Connected !");
		} else {
			handler.openConnection(serverIP);
			msg.setNickName(nickname);// update the nickame
			this.nickname = nickname;
			// write msg
			this.send(msg);

			System.out.println("sending : " + msg.toString());
			// For this specifique case the server can send an error (nick
			// already used)
			// So we treat directly the message

			MessageFromServer answer = this.read();

			if (answer.isError()) {
				this.handler.isConnectionOpened = false;
				this.window.displayError(answer.getPost());
			} else {
				this.handler.isConnectionOpened = true;
				this.window.displayInfo(answer.getPost());
			
			}

		}
	}

	private void connectToChannel(MessageToServer msg)
			throws ConnectionHandlerException {

		// ALready conneted to a channel ? quit the previous one
		if (this.currentChannel != null) {
			// Init message so that server knows we're going to quit the channel
			MessageToServer m = new MessageToServer(this.nickname, "#quit", new ArrayList<String>());
			disconnectFromChannel(m);
			// empty the textArea
			window.cleanDisplay();
		}
		// Now connect to new channel
		this.send(msg);
		this.currentChannel = msg.getArgs().get(0);

		if (this.listener == null) {
			this.listener = new ClientListener();
			Thread t = new Thread(this.listener);
			t.start();
		}
	}

	/*
	 * Disconnect
	 */

	private void disconnectFromServer(MessageToServer msg)
			throws ConnectionHandlerException {

		
		if (this.currentChannel != null) {
			// Init message so that server knows we're going to quit the channel
			MessageToServer m = new MessageToServer(this.nickname, "#QUIT", new ArrayList<String>());
			disconnectFromChannel(m);
		}
		
		if(this.listener != null)
			this.listener.stop();
		this.listener = null;
		
		// this will cause the server to close the socket
		this.send(msg);
		
		this.currentChannel = "";
		
		// close connection
		this.handler.closeConnection();

		this.window.displayInfo("Connection with server closed.");
		
	}

	private void disconnectFromChannel(MessageToServer msg) throws ConnectionHandlerException {
		// write msg
		this.send(msg);
		String channel = this.currentChannel;
		// close connection
		this.currentChannel = null;

	}

	/*
	 * READ & Write
	 */
	private void send(MessageToServer msg) throws ConnectionHandlerException {

		msg.setNickName(this.nickname);// update the nickame

		this.handler.write(msg.toString());
	}

	public MessageFromServer read() throws ConnectionHandlerException {
		String s = this.handler.read();

		System.out.println("Read string : " + s);
		MessageFromServer msg = new MessageFromServer(s);
		return msg;
	}


	public boolean isConnectionOpened() {
		return this.handler.isConnectionOpened;
		
	}



}
