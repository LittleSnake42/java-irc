package utils;

import ihm.Window;
import io.MainClient;

import java.util.ArrayList;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.log4j.Logger;

import serial.MessageFromServer;
import serial.MessageToServer;

// rename MainController
// This
public class MessageControler {
	//Log variable.
	
	private static final Logger LOG = Logger.getLogger(MessageControler.class.getName());
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
	
	/*
	* This method treats the string asked as a argument and convert it to a MessageToServer Object
	* ready to get send to the Server if he pass the control. 
	*/	
	public void process(String s) throws MessageControlerException {

		// Init message
		//Create an object msg using the method initMessage.
		MessageToServer msg = this.initMessage(s);

		
		//We are checking if its a command and a valid one. If yes we call the method processCommand to make it a command ready to get send
		// to the server. 
		//If its not a command it means it's a message then.
		if (msg.isCommand() && msg.isValidCommand()) {
			LOG.info("Enter into the command "+s);
			processCommand(msg);
		} else if (msg.isCommand()) { // invalid command -> so maybe a message
										// starting by # ?

			if (this.canSendMessage()) {
				try {
					LOG.info("Command Message send ");
					this.send(msg);
					
					// We recover the Message send by the server as an object MessageFromServer
					// and we treat it to check we do not have a problem With the message we send previously.
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
				LOG.error("Unknown Command "+ msg.getPost());
				
				//Pas sur si je le laisse. Pareil pour les autres.
				//throw new MessageControlerException("Unknown command \""+ msg.getPost() + "\".");
			}
		} else { // Message

			// are we connected to a server ?
			// and is nickname set ?
			// are we connected to a channel ?
			if (this.canSendMessage()) {
				try {
					LOG.info("Simple Message send");
					this.send(msg);
					// We recover the Message send by the server as an object MessageFromServer
					// and we treat it to check we do not have a problem With the message we send previously.
					MessageFromServer answer = this.read();
					this.processServerMessage(answer);
				} catch (ConnectionHandlerException e) {
					// if we enter here it means we have an error.
					LOG.error("Error while sending the message",e);
				}
			} else {
				// We check if we are connected to a server. if yes then it means we are not connected to a channel
				// else it means we are not even connected to a server.
				if (this.handler.isConnectionOpened)
					LOG.error("You are not connected to a channel, you can't send message now. Try #JOIN CHANNEL_NAME");
					//throw new MessageControlerException("You are not connected to a channel, you can't send message now. Try #JOIN CHANNEL_NAME");
				else
					LOG.error("You are not connected to a server, Try #CONNECT SERVER_IP NICKNAME");
					//throw new MessageControlerException("You are not connected to a server. Try #CONNECT SERVER_IP NICKNAME");

			}

		}
	}

	/**
	 * Functions to process messages asking a string for argument
	 */
	private MessageToServer initMessage(String s) {
		
		//Initialize the local variable.
		ArrayList<String> args = new ArrayList<String>();
		String post = new String();
		String nickname = this.nickname;
		//We check if the string is a command by checking if it starts by #
		if (s.startsWith("#")) {
			
			//We separate the string in a array of string to recover the args and the post
			String[] splited = s.split(" ");
			//The first part of the string is put on the POST part.
			post = splited[0];
			
			// We are checking if we have args here if yes then we put the splitted parts in ARGS
			// We are limited to 2 arguments
			if (splited.length > 1 && splited[1] != null) {
				args.add(splited[1]);
			}// max 2 args
			if (splited.length > 2 && splited[2] != null) {
				args.add(splited[2]);
			}
		} else {
			//If its not a command then POST is the string put in argument
			post = s;
		}
		// We create a MessageToServer object and then we send it as a return value
		MessageToServer msg = new MessageToServer(nickname, post, args);
		return msg;
	}
	// We analyze the message send by the server
	private void processServerMessage(MessageFromServer msg) {

		// check is valid ?
		boolean isValid = msg.isValid();
		if (!isValid) { // always for the moment

		}
		// server or user ?
		boolean isFromServer = msg.isFromServer();
		// case 1 Server
		//In both case we display what we got. In the first case its the message from the server.
		//In the second case it is the message send by the user.
		if (isFromServer) {
			window.displayError(msg.getNickname() +" # "+msg.getPost());
			LOG.error(msg.getNickname() +" # "+msg.getPost());
		}
		// case 2 User
		else {
			window.displayMessage(msg.getNickname()+ " > " + msg.getPost());
			LOG.error(msg.getNickname() +" > "+msg.getPost());
		}
	}

	// Function to call if Message is a command
	private void processCommand(MessageToServer msg)
			throws MessageControlerException {

		String command = msg.getPost();
		ArrayList<String> args = msg.getArgs();

		// Case #CONNECT : we expect 2 args (ip, nick)
		if (command.toUpperCase().equals("#CONNECT")) {
			LOG.info("Command connect detected.");
			if (args.size() == 2) {
				String serverIP = args.get(0);
				String nickname = args.get(1);
				
				LOG.info("IP : " + serverIP);
				LOG.info("NICKNAME :" + nickname );


				// check if args are OK
				boolean isValidIP = this.isValidIpAddress(serverIP);
				boolean isValidNickname = this.isValidNickname(nickname);

				// if not maybe in wrong order?
				// No ok or not but not wrong order

				// if ok try to connect
				if (isValidIP && isValidNickname) {
					try {
						LOG.info("Trying to connect to the server.");
						this.connectToServer(msg);
					} catch (ConnectionHandlerException e) {
						LOG.error("An error occured attempting to connect to IP : " + serverIP 
								+ " / with nickname : " + nickname + " / Because of the error : ",e );
					}
				} else {
					LOG.error("NICKNAME OR IP ADRESS NOT VALID");
				}

			} else {
				LOG.error("#CONNECT expects two args, the target server ip and a nickname.");
			}

		} else if (command.toUpperCase().equals("#JOIN")) {
			
			LOG.info("Command JOIN detected.");
			// We need one argument to use the command JOIN
			if (args.size() == 1) {
				String channel = args.get(0);
				// check if args are OK

				// if ok try to join
				try {
					this.connectToChannel(msg);
				} catch (ConnectionHandlerException e) {
					LOG.error("Unable to join the channel : " + channel + " / Because of the error : ", e );
			
				}

			} else {
				LOG.error("#JOIN expects 1 args, the target channel name.");
			}

		}
		// This case is for disconnect from server
		else if (command.toUpperCase().equals("#QUIT")) {
			try {
				// this.disconnectFromChannel(msg);
				this.disconnectFromServer(msg);
			} catch (ConnectionHandlerException e) {
				// TODO Auto-generated catch block
				LOG.error("Problem while quitting the application because of the error : ", e);
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
				LOG.error("Problem while quitting the channel because of the error : ", e);
			}
		} else {
			LOG.error("Not a valid command. RTFM :)");
		}
	}

	/**
	 * Functions isSomething, canDoSomething 
	 */
	/*
	 *  This function check if we the message is ok to be send to the server.
	 *  Return true if there is a connection and a channel selected
	 *  Return false if one or both are missing.
	 */
	private boolean canSendMessage() {
		 
		return this.handler.isConnectionOpened && this.currentChannel != null;
	}
	/*
	 *  This function check if the IP adress is valid.
	 *  Return true if the IP adress is valid.
	 */
	private boolean isValidIpAddress(String serverIP) {
		InetAddressValidator validator = new InetAddressValidator();
		return (validator.isValidInet4Address(serverIP) /*
														 * || validator.
														 * isValidInet6Address
														 * (serverIP)
														 */);
	}
	/*
	 *  This function check if we the nickname is good.
	 *  return true if we have 3 or more characters on the nickname
	 */
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
			LOG.error("ALREADY CONNECTED !");

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
		LOG.info("Connected to channel " + this.currentChannel);

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
		
		LOG.info("Disconneted from the server.");

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
