package utils;

import ihm.Chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;


public class ConnectionHandler {
	private static final Logger LOG = Logger.getRootLogger();
	private static final int SERVER_PORT = 12345;
	private static final String ENCODING = "UTF-8";
	private static ConnectionHandler INSTANCE = new ConnectionHandler();
	
	private Socket socket = null;
	// Write
	private OutputStream out       = null;
	private OutputStreamWriter osw = null;
	private BufferedWriter bw      = null;
	// Read
	private InputStream in        = null;
	private InputStreamReader isr = null;
	protected BufferedReader br     = null;
	
	public boolean isConnectionOpened = false;

	private ConnectionHandler() {
		// TODO Auto-generated constructor stub
	}
	// This allow allow us to get the INSTANCE of the application to not duplicate it.
	public static ConnectionHandler getInstance()
    {
        return INSTANCE;
    }
	
	// This method will take care of the connection to the server. It asks a ip address.
	public void openConnection(String ip) throws ConnectionHandlerException {
		// check if a socket is already opened
		if (this.isConnectionOpened) {
			LOG.error("You are already connected,close the current connection before opening a new one.");
			throw new ConnectionHandlerException("You must close current connection before opening a new one.");
			//APP . window .display error
		}
		try {
			this.socket = new Socket(InetAddress.getByName(ip), SERVER_PORT);
			
			//if socket not ok do something : msg err
			//Initializing the write attribute.
			this.out = socket.getOutputStream();
			this.osw = new OutputStreamWriter(out, ENCODING);
			this.bw  = new BufferedWriter(osw);
			
			// Initializing the read attribute.
			this.in  = this.socket.getInputStream();
			this.isr = new InputStreamReader(in, ENCODING);
			this.br  = new BufferedReader(isr);
			LOG.info("Connected to the server : "+ip);
			this.isConnectionOpened = true; // APP 
			// while checking for an eventual error.
		} catch (IOException e) {
			LOG.error("Error when trying to open connection...");
			LOG.error("Here is the error message : " + e.getMessage());
			throw new ConnectionHandlerException("Error opening connection.", e);
		}
	}
	// This Method is used when the quit the application.
	public void closeConnection() throws ConnectionHandlerException {
		try {
			//Check if the bufferedwritter is open if yes we close it.
			if (this.bw != null) {
				this.bw.close();
			}
			//Check if the outputstreamwritter is open if yes we close it.
			if (this.osw != null) {
				this.osw.close();
			}
			//Check if the ouputstream is open if yes we close it.
			if (this.out != null) {
				this.out.close();
			}
			// read
			//Check if the bufferedreader is open if yes we close it.
			if (this.br != null) {
				this.br.close();
			}
			//Check if the Inputstreamreader is open if yes we close it.
			if (this.isr != null) {
				this.isr.close();
			}
			//Check if the InputStream is open if yes we close it.
			if (this.in != null) {
				this.in.close();
			}
			// socket
			if (this.socket != null) {
				this.socket.close();
			}
			//while checking for a fatal error like the poor hard drive dying.
		} catch (IOException e) {
			LOG.fatal("NOT GOOD. HARD DRIVE DEADED. PLEASE SAVE YOUR GODDESS AND YOUR PORN ON THE HARD DRIVE !!!!!!");
			throw new ConnectionHandlerException("Error closing connection.", e);
		} finally {
			this.isConnectionOpened = false;
		}
	}
	//this method allows to send a message to server, be it a command or a simply a message to the chat
	public void write(String s) throws ConnectionHandlerException {
		// We check if we have a connection before doing anything else
		if (this.isConnectionOpened) {
				try {
					// Writting on the buffer
					bw.write(s);
					// Add a end line caracter for the server to not block him.
					bw.newLine();
					// Send everything to the server
					bw.flush();
					//while checking for an eventual error
					LOG.info("Message send to the server.");
					LOG.info("Here is the message : " + s);
					// while checking for an error.
				} catch (IOException e) {
					// IO error, close everything
					LOG.error("Error when sending this message to the server : " + s);
					this.closeConnection();
					throw new ConnectionHandlerException("Unabled to write string \"" + s + "\".", e);
				}
			// If not we tell the user to connect to a server before sending message.
		} else {
			LOG.error("No connection opened. Please connect to a server before sending a message.");
			throw new ConnectionHandlerException("No connection opened.");
		}
	}
	// This method allows to get message from the server and analyzing this message
	public String read() throws ConnectionHandlerException {
		
		String msg = null;
		// We are checking if we are connected to a server before reading.
		if (this.isConnectionOpened) {
			while (msg == null) {
				try {
					// We recover the message send to us by the server
					msg = this.br.readLine();
					// while checking for an error.
				} catch (IOException e) {
					// IO error, close everything
					this.closeConnection();
					Chat chat = Chat.getInstance();
					LOG.error("We don't manage to read from the server which means...");
					LOG.error("An unexpected error appened. Server is not reachable. Quitting the APP.");
					chat.displayError("An unexpected error appened. Server is not reachable. Quitting the APP.");
					chat.dispose();
					throw new ConnectionHandlerException("Unabled to read from Buffer.", e);
				}

			}
			LOG.info("Message send by the server.");
			LOG.info("Displaying the message received : "+msg);
			
			return msg;
		// If we aren't connected then we tell the user there is no connection...
		} else {
			LOG.error("No connection opened. Please connect to a server before thinking you will receive a message.");
			throw new ConnectionHandlerException("No connection opened.");
		}
	}
	//LOG is private so we have an accessor for the class MessageControler.
	public Logger getLogger()
	{
		return LOG;
		
	}

}
