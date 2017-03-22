/**
 * @author Snake
 * Message send by the client to the server.
 */
package serial;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageToServer extends JSONObject {

	private String nickname;
	private String post;
	private ArrayList<String> args;
	
	// Hum need to ask how to init an ArrayList
	private static final String[] AVAILABLE_COMMANDS = {"#CONNECT", "#JOIN", "#QUIT", "#EXIT"};
	
	/**
	 * @param nickname : The nickname chosen by the user.
	 * @param post : Can be a normal message, or a command if begins by "#".
	 * 		  Available commands : "#CONNECT serverIp nickname", "#JOIN channelName", "#QUIT", "#EXIT"
	 * @param args : If @param post begins by "#"
	 */
	public MessageToServer(String nickname, String post, ArrayList<String> args) {
		this.setNickName(nickname);
		this.setPost(post);
		this.setArgs(args);
	}
	
	public MessageToServer(String json) {
		
		super(json);

		try {
			String nickname = this.getString("nickname");
			String post = this.getString("post");
			JSONArray args = this.getJSONArray("args");
			
			this.setNickName(nickname);
			this.setPost(post);
			//this.setArgs(args.toList());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the nickname
	 */
	public String getNickName() {
		return nickname;
	}

	/**
	 * @return the post
	 */
	public String getPost() {
		return post;
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	/**
	 * @param nickname the nickname to set
	 */
	private void setNickName(String nickname) {
		this.nickname = nickname;
		this.put("nickname", this.nickname);
	}

	/**
	 * @param post the post to set
	 */
	private void setPost(String post) {
		this.post = post;
		this.put("post", this.post);
	}

	/**
	 * @param args, array of args
	 */
	private void setArgs(ArrayList<String> args) {
		this.args = args;
		this.put("args", this.args);
	}
	
	/**
	 * Check whether the message is a command or a simple message
	 * @return boolean
	 */
	public boolean isCommand() {
		return this.post.startsWith("#");
	}
	
	public boolean isValidCommand() {
		// Method 1
		// Not possible to switch "String" type in Java before java7
		if (this.post.toUpperCase() == "#CONNECT") {
		
		} else if(this.post.toUpperCase() == "#JOIN"){
			
		}
		// Method 2
		return Arrays.asList(this.AVAILABLE_COMMANDS).contains(this.post.toUpperCase());
		
	}
}
