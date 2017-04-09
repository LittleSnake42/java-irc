package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;


public class ConnectionHandler {
	
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
	
	public static ConnectionHandler getInstance()
    {
        return INSTANCE;
    }
	
	
	public void openConnection(String ip) throws ConnectionHandlerException {
		// check if a socket is already opened
		if (this.isConnectionOpened) {
			throw new ConnectionHandlerException("You must close current connection before opening a new one.");
			//APP . window .display error
		}
		try {
			this.socket = new Socket(InetAddress.getByName(ip), SERVER_PORT);
			
			//if socket not ok do something : msg err
			// write
			this.out = socket.getOutputStream();
			this.osw = new OutputStreamWriter(out, ENCODING);
			this.bw  = new BufferedWriter(osw);
			
			// read
			this.in  = this.socket.getInputStream();
			this.isr = new InputStreamReader(in, ENCODING);
			this.br  = new BufferedReader(isr);

			this.isConnectionOpened = true; // APP 

		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new ConnectionHandlerException("Error opening connection.", e);
		}
	}
	// This Method will be used with the pretty white cross on the red square when we have implemented this
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
		} catch (IOException e) {
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
					
					System.out.println("message send test: \"" + s + "\".");
					
				} catch (IOException e) {
					throw new ConnectionHandlerException("Unabled to write string \"" + s + "\".", e);
				}
			
		} else {
			throw new ConnectionHandlerException("No connection opened.");
		}
	}
	// This method allows to get message from the server and analyzing this message
	public String read() throws ConnectionHandlerException {
		
		String msg = null;
		if (this.isConnectionOpened) {
			while (msg == null) {
				try {
					// We recover the message send to us by the server
					msg = this.br.readLine();

				} catch (IOException e) {
					throw new ConnectionHandlerException("Unabled to read from Buffer.", e);
				}

			}

			System.out.println(msg);
			
			return msg;
		
		} else {
			throw new ConnectionHandlerException("No connection opened.");
		}
	}


}
