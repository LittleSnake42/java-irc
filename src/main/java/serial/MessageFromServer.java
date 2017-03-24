/**
 * @author Snake
 * Message send by the server to the client.
 * Can be an error message, or a message from another user.
 */
package serial;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;


public class MessageFromServer extends JSONObject{

	private String nickname; 
	private String post;
	private ArrayList<String> channels;
	private ArrayList<String> users;

	/**
	 * Constructor
	 * @param nickName : Can be "server" if the message to display is a notice or an error.
	 * @param post     : The message 
	 * @param channels : A list of available channels
	 * @param users    : A list of connected users (to the current channel)
	 */
	public MessageFromServer(String json) {
		super(json);

		try {
			String nickname = this.getString("nickname");
			String post = this.getString("post");
			
			this.setNickname(nickname);
			this.setPost(post);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}


	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return this.nickname;
	}


	/**
	 * @return the post
	 */
	public String getPost() {
		return this.post;
	}


	/**
	 * @return the channels
	 */
	public ArrayList<String> getChannels() {
		return this.channels;
	}


	/**
	 * @return the users
	 */
	public ArrayList<String> getUsers() {
		return this.users;
	}


	/**
	 * @param nickname the nickname to set
	 */
	private void setNickname(String nickname) {
		this.nickname = nickname;
	}


	/**
	 * @param post the post to set
	 */
	private void setPost(String post) {
		this.post = post;
	}


	/**
	 * @param channels the channels to set
	 */
	private void setChannels(ArrayList<String> channels) {
		this.channels = channels;
	}


	/**
	 * @param users the users to set
	 */
	private void setUsers(ArrayList<String> users) {
		this.users = users;
	}

	/**
	 * check if the message is from a user or from the server
	 * if from server, needs to be treated differently
	 * @return boolean
	 */
	public boolean isFromServer() {
		return this.nickname.toLowerCase() == "server";
	}


	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}
}
