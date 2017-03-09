package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MainServer {

	public static final int SERVER_PORT = 12345;
	private static final Logger LOG = Logger.getLogger(MainServer.class
			.getName());
	public boolean loopBreaker = true;

	public static void run() throws ServerException {
		ServerSocket server = null;

		try {

			server = new ServerSocket(SERVER_PORT);

			while (true) {
				processSocket(server.accept());
			}

		} catch (IOException e) {
			throw new ServerException("Error during socket connection", e);
		} finally {
			try {
				if (server != null) {
					server.close();
				}
			} catch (IOException e) {
				LOG.error("Error closing server", e);
			}
		}

	}

	private MainServer() {
		// useless
	}

	private static void processSocket(Socket client) throws ServerException {
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			in = client.getInputStream();
			isr = new InputStreamReader(in, "UTF-8");
			br = new BufferedReader(isr);
			System.out.println(client.getInetAddress().getHostAddress());
			System.out.println(br.readLine());

		} catch (IOException e) {
			throw new ServerException("Error during writing data on SYSO", e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				LOG.error("Err closing streams", e);
			}
		}

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		
		LOG.info("Start server on port " + SERVER_PORT + ".");
		try {
			MainServer.run();
		} catch (ServerException e) {
			LOG.error("Error in runing server", e);
		}
		System.exit(0);
	}

}
