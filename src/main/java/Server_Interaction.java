import java.util.logging.Logger;

/**
 * @author Tristan
 * @group : Rémi,Michaël,Tristan
 * @description : This class will handle the preparation to create the data that will be
 * send to the database. It will also handle the answer of the server.
 * Version 1.0 : It is a beta version right now, it handle the second word rather poorly im pretty sure
 * there is other way to handle the string but right now we care more about functionnal than pratical.
 */
public class Server_Interaction {
		// theses 2 variables are used to take the second work.
		private char[] converts;
		private StringBuilder str;
		private String nickname ;
		private String post = null;
		
		private static final Logger LOG = Logger.getLogger(Server_Interaction.class.getName());
		public Server_Interaction(String data_Send)
		{
			// Check if the data contains something
			if (data_Send.isEmpty() == false)
			{
				if (data_Send.charAt(0)=='#')
				{
					str = null;
					converts = null;
					// i is set to 1 because of the #, while j will be the size of the char[]
					int i=1,j=0;
					// Until we got a ' ' we continue to put on the char[]
					while (data_Send.charAt(i) != ' ')
					{
						converts[j] = data_Send.charAt(i);
						System.out.println("test");
					}
					// Creating a string from this
					str.append(converts,0,j);
					// Switch needs a int or  an enum to work. converted to JRE 1.7
					switch (str.toString()) {
					//Case 1 join
					case "join":
						post = "join";
						break;
						//Case 2 connect
					case "connect":
						post ="connect";
						break;
					//case 3 exit
					case "exit":
						post = "exit";
						
						break;
						// case 4 quit : We need to need to quit the server we simply 
					case "quit":
						post = "quit";
						break;
						
						//Warn the user he put a wrong command
					default:
						LOG.info("Wrong command buddy");
						
						break;
					}
				}
			}
			// I check the first letter if its a #
			
			
		}
}
