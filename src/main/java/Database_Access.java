import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import io.ClientException;



/**
 * 
 */

/**
 * @author Tristan
 * @group : Rémi,Michaël,Tristan
 * @description : This class allows the connection to the database and the sending of data.
 * 
 * Version 1.0 :Right now there is a lot of thing to improve. We must see together what we can do for getting the message 
 * of the server and how do we treat it. the method isn't yet perfect. It is a rough sketch of what will become 
 * the class. We have to synchronize with the graphical part to understand how works the JSON. 
 */
public class Database_Access {
	private static final Logger LOG = Logger.getLogger(Database_Access.class.getName());
	// This method will manage the acces to the database
	// using Json converted to string and send to the server we try
	// to connect
	public  Database_Access() throws Database_Access_Exception 
	{
		//Here is the creation of the variable 
		JSONObject jo = null;
		Socket s = null;
		Scanner sc = null;
		OutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		// End of the creation to the server
		while(true)
		
		{
		try {
			
			jo = new JSONObject();
			sc = new Scanner(System.in);
			//This part is the connection to the server : this part is useless right now
			s = new Socket(InetAddress.getByName("128.0.0.1"),12345);
			// This part take care of getting a stream from the server 
			out = s.getOutputStream();
			osw = new OutputStreamWriter(out,"UTF-8");
			bw = new BufferedWriter(osw);
			//This part send the message to the server.
			bw.write(jo.toString());
			bw.newLine();
			bw.flush();
			
		} catch (IOException  e) {
			//If there is a error when it send the message it will tell us what happened.
			throw new Database_Access_Exception("error during message sending.",e);
		}finally{
		
		// This part is taking care of closing all the stream that have been open in the program
			// In case that doesn't work it close the program and write an error on the log
			// The error is most unlikely to happen but we have to treat it in case it does happen
			try {
				if (sc!=null)
					sc.close();
				if (bw!=null)
					bw.close();
				if (osw != null)
					osw.close();
				if (out!=null)
					out.close();
				if (s!=null)
				s.close();
			} catch (IOException  e) {
				LOG.log(Level.SEVERE,"error during closing",e);
				// This error happen if we have a problem in the hard drive. Most likely to never happen.
			}
		}
		}
		
	}
}
