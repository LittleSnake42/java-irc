package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;

import serial.MessageFromServer;
import serial.MessageToServer;
import utils.MessageControler;

public class MainServer {

	public static final int SERVER_PORT = 12345;

	public boolean loopBreaker = true;

	public static void run() throws ServerException {
		ServerSocket server = null;

		try {

			server = new ServerSocket(SERVER_PORT);

			//while (true) {
				
				processSocket(server.accept());
			//}

		} catch (IOException e) {
			throw new ServerException("Error during socket connection", e);
		} finally {
			try {
				if (server != null) {
					server.close();
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
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

		OutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		try {

			in = client.getInputStream();
			isr = new InputStreamReader(in, "UTF-8");
			br = new BufferedReader(isr);
			System.out.println(client.getInetAddress().getHostAddress());

			out = client.getOutputStream();
			osw = new OutputStreamWriter(out, "UTF-8");
			bw = new BufferedWriter(osw);

			// -----------------

			while(true) {
				
				
				String string = br.readLine();

				System.out.println(string);
				
				
				MessageToServer msg = new MessageToServer(string);

				JSONObject json = new JSONObject();

				if (msg.getPost().toUpperCase().equals("#CONNECT")) {
					json.put("nickname", "server");

					json.put("post", "Vous etes connecté au serveur de test ©LittleSnake.");

					json.put("args", new JSONArray());

				} else if (msg.getPost().toUpperCase().equals("#JOIN")) {
					json.put("nickname", "server");

					json.put("post",
							"Vous etes connecté au channel " +msg.getArgs().get(0)+".");

					json.put("channels", new JSONArray());

					json.put("users", new JSONArray());

				} else if (msg.getPost().toUpperCase().equals("#QUIT")) {
					json.put("nickname", "server");

					json.put("post",
							"Au revoir");

					json.put("channels", new JSONArray());
				} else if (msg.getPost().toUpperCase().equals("#EXIT")) {
					json.put("nickname", "server");

					json.put("post", "Au revoir");

					json.put("channels", new JSONArray());
				} else {
					json.put("nickname", msg.getNickName());

					json.put("post", msg.getPost());

					json.put("channels", new JSONArray());
				}

				System.err.println("Sending :" + json);
				bw.write(json.toString());
				bw.newLine();
				bw.flush();				
				
			}
			
			


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
				
				
				if (bw != null)
					bw.close();
				if (osw != null)
					osw.close();
				if (out != null)
					out.close();
				
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

	}

	public static void main(String[] args) {

		try {
			MainServer.run();
		} catch (ServerException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
