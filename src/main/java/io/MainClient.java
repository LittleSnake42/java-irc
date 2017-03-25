/**
 * 
 */
package io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author remi
 * 
 */
public class MainClient {

	private static final Logger LOG = Logger.getLogger(MainClient.class
			.getName());
	private static final String NICKNAME = "LittleSnake";
	private static final int SERVER_PORT = 12345;
	private static final String SERVER_HOST = "127.0.0.1";
	private static final String ENCODING = "UTF-8";

	public static void run() throws ClientException {

		Scanner sc = null;
		Socket s = null;

		OutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		sc = new Scanner(System.in);

		while (true) {

			try {

				final String msg = sc.nextLine();

				s = new Socket(InetAddress.getByName(SERVER_HOST), SERVER_PORT);

				out = s.getOutputStream();
				osw = new OutputStreamWriter(out, ENCODING);
				bw = new BufferedWriter(osw);

				bw.write(NICKNAME + " > " + msg);
				bw.flush();

			} catch (IOException e) {
				throw new ClientException("Error sending message.", e);
			} finally {
				try {

					if (bw != null)
						bw.close();
					if (osw != null)
						osw.close();
					if (out != null)
						out.close();
					if (s != null)
						s.close();
				} catch (IOException e) {
					//LOG.error("error closing ...", e);
				}
			}
		}


	}

	private MainClient() {
		// useless
	}

	public static void main(String[] args) {

		try {
			MainClient.run();
			System.exit(0);
		} catch (ClientException e) {
			//LOG.error("Error runing client", e);
			System.exit(-1);
		}

	}

}
