package utils;

import ihm.Window;

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


	
	// Components
	private ConnectionHandler handler = ConnectionHandler.getInstance();
	private Window window = Window.getInstance();


	private MessageControler() {
		// do nothing
	}
	
	public static MessageControler getInstance()
    {
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
			System.out.println("msg is command and is valid\n");
			//log.info("Rentr�e dans la commande",msg.getPost(),msg.getNickName(),msg.getArgs());
			processCommand(msg);
		} else if (msg.isCommand()) { // invalid command -> so maybe a message
										// starting by # ?

			if (this.canSendMessage()) {
				try {
					//log.info("trying to message send");
					System.out.println("sending msg with #\n");
					this.send(msg);
					
					MessageFromServer answer = this.read();
					this.processServerMessage(answer);
				} catch (ConnectionHandlerException e) {
					// TODO Auto-generated catch block
					//log.error("Impossible to send the message",e);
					e.printStackTrace();
				}
			} else {
				// if no connected to server nor channel -> error, else is a
				// message
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
					MessageFromServer answer = this.read();
					this.processServerMessage(answer);
				} catch (ConnectionHandlerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if (this.handler.isConnectionOpened)
					throw new MessageControlerException(
							"You are not connected to a channel, you can't send message now. Try #JOIN CHANNEL_NAME");
				else
					throw new MessageControlerException(
							"You are not connected to a server. Try #CONNECT SERVER_IP NICKNAME");

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

	private void processServerMessage(MessageFromServer msg) {

		// check is valid ?
		boolean isValid = msg.isValid();
		if (!isValid) { // always for the moment

		}
		// server or user ?
		boolean isFromServer = msg.isFromServer();
		// case 1 Server
		if (isFromServer) {
			window.displayError(msg.getNickname() +" # "+msg.getPost());
			System.err.println(msg.getNickname() +" # "+msg.getPost());
		}
		// case 2 User
		else {
			window.displayMessage(msg.getNickname()+ " > " + msg.getPost());
			System.err.println(msg.getNickname()+ " > " + msg.getPost());
		}
	}

	// Function to call if Message is a command
	private void processCommand(MessageToServer msg)
			throws MessageControlerException {

		String command = msg.getPost();
		ArrayList<String> args = msg.getArgs();

		// Case #CONNECT : we expect 2 args (ip, nick)
		if (command.toUpperCase().equals("#CONNECT")) {
			System.out.println("command connect detectée\n");
			if (args.size() == 2) {
				String serverIP = args.get(0);
				String nickname = args.get(1);
				
				System.out.println("ip : "+serverIP);
				System.out.println("nick : " + nickname);

				// check if args are OK
				boolean isValidIP = this.isValidIpAddress(serverIP);
				boolean isValidNickname = this.isValidNickname(nickname);

				// if not maybe in wrong order?
				// No ok or not but not wrong order

				// if ok try to connect
				if (isValidIP && isValidNickname) {
					try {
						this.connectToServer(msg);
					} catch (ConnectionHandlerException e) {
						System.err.println(e);
						throw new MessageControlerException(
								"An error occured attempting to connect to IP \""
										+ serverIP + "\" with nickname \""
										+ nickname + "\".", e);
					}
				} else {
					System.err.println("ERR : nickname or ip not valid");
				}

			} else {
				throw new MessageControlerException(
						"#CONNECT expects two args, the target server ip and a nickname.");
			}

		} else if (command.toUpperCase().equals("#JOIN")) {
			System.out.println("command join detectée\n");

			if (args.size() == 1) {
				String channel = args.get(0);
				// check if args are OK

				// if ok try to connect
				try {
					this.connectToChannel(msg);
				} catch (ConnectionHandlerException e) {
					// TODO Auto-generated catch block
					throw new MessageControlerException(
							"Unabled to join channel \"" + channel + "\".", e);
				}

			} else {
				throw new MessageControlerException(
						"#JOIN expects 1 args, the target channel name.");
			}

		}
		// This case is for disconnect from server
		else if (command.toUpperCase().equals("#QUIT")) {
			try {
				// this.disconnectFromChannel(msg);
				this.disconnectFromServer(msg);
			} catch (ConnectionHandlerException e) {
				// TODO Auto-generated catch block
				throw new MessageControlerException(
						"problem while quitting the application");
			}

		}
		// This case is for leaving the app
		else if (command.toUpperCase().equals("#EXIT")) {
			try {
				// this.disconnectFromChannel(msg);
				this.disconnectFromServer(msg);
				// QUit global app;
			} catch (ConnectionHandlerException e) {
				// TODO Auto-generated catch block
				throw new MessageControlerException(
						"problem while quitting the channel");
			}
		} else {
			throw new MessageControlerException("Not a valid command. RTFM :)");
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
		
		msg.setNickName(nickname);// update the nickame
		this.nickname = nickname;
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

	private void connectToChannel(MessageToServer msg)
			throws ConnectionHandlerException {

		this.send(msg);

		// Need to check if ok
		MessageFromServer answer = this.read();
		this.processServerMessage(answer);
		// set channel name for global use
		this.currentChannel = msg.getArgs().get(0);
		
		System.out.println("Connected to channel " + this.currentChannel);

	}

	/*
	 * Disconnect
	 */

	private void disconnectFromServer(MessageToServer msg)
			throws ConnectionHandlerException {

		// write msg
		this.send(msg);

		MessageFromServer answer = this.read();
		this.processServerMessage(answer);
		// close connection
		this.handler.closeConnection();
		
		System.out.println("connection closed");

	}

	/*
	 * READ & Write
	 */
	private void send(MessageToServer msg) throws ConnectionHandlerException {
		
		msg.setNickName(this.nickname);// update the nickame

		//System.out.println("WRiting : " + msg.toString());
		this.handler.write(msg.toString());
	}

	public MessageFromServer read() throws ConnectionHandlerException {
		String s = this.handler.read();

		//System.out.println("Read string : " + s);
		MessageFromServer msg = new MessageFromServer(s);
		return msg;
	}
}
/*
 * a conserver voir si on peut de faire. // We split this message to analyze if
 * we got a "error" String[] splitted = msg.split(" "); // We check if we get a
 * message from the server if yes we create a error message if (splitted[0]==) {
 * // Here we create the windows that show up to warn us we got an error from
 * the server. } // Here we can put what we will write on the windows.
 */