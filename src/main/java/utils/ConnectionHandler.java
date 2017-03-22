package utils;

import io.ClientException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;

import serial.MessageFromServer;


// TODO SINGLETON !!!
public class ConnectionHandler {
	
	private static final int SERVER_PORT = 12345;
	private static final String ENCODING = "UTF-8";
	
	private Socket socket = null;
	// Write
	private OutputStream out       = null;
	private OutputStreamWriter osw = null;
	private BufferedWriter bw      = null;
	// Read
	private InputStream in        = null;
	private InputStreamReader isr = null;
	private BufferedReader br     = null;
	
	private boolean isConnectionOpened = false;

	public ConnectionHandler() {
		// TODO Auto-generated constructor stub
	}
	
	public void openConnection(String ip) throws ConnectionHandlerException {
		// check if a socket is already opened
		if (this.isConnectionOpened) {
			throw new ConnectionHandlerException("You must close current connection before opening a new one.");
		}
		try {
			//
			this.socket = new Socket(InetAddress.getByName(ip), SERVER_PORT);
			
			// write
			this.out = socket.getOutputStream();
			this.osw = new OutputStreamWriter(out, ENCODING);
			this.bw  = new BufferedWriter(osw);
			
			// read
			this.in  = this.socket.getInputStream();
			this.isr = new InputStreamReader(in, ENCODING);
			this.br  = new BufferedReader(isr);
			
			this.isConnectionOpened = true;

		} catch (IOException e) {
			throw new ConnectionHandlerException("Error opening connection.", e);
		}
	}
	
	public void closeConnection() throws ConnectionHandlerException {
		try {
			// write
			if (this.bw != null) {
				this.bw.close();
			}
			if (this.osw != null) {
				this.osw.close();
			}
			if (this.out != null) {
				this.out.close();
			}
			// read
			if (this.br != null) {
				this.br.close();
			}
			if (this.isr != null) {
				this.isr.close();
			}
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
	
	public void write(String s) throws ConnectionHandlerException {
	
		if (this.isConnectionOpened) {
				try {
					bw.write(s);
					bw.flush();
				} catch (IOException e) {
					throw new ConnectionHandlerException("Unabled to write string \"" + s + "\".", e);
				}
			
		} else {
			throw new ConnectionHandlerException("No connection opened.");
		}
	}
	
	public String read() throws ConnectionHandlerException {
		
		if (this.isConnectionOpened) {
				try {
					String msg = this.br.readLine();
					return msg;
				} catch (IOException e) {
					throw new ConnectionHandlerException("Unabled to read from Buffer.", e);
				}
			
		} else {
			throw new ConnectionHandlerException("No connection opened.");
		}
	}


}
