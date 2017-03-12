/**
 * @author Snake
 * Message send by the server to the client.
 * Can be an error message, or a message from another user.
 */
package serial;

import java.io.Serializable;
import java.util.ArrayList;

public class MessageFromServer implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nickName; 
	private String post;
	private ArrayList<String> channels;
	private ArrayList<String> users;

	/**
	 * Constructor
	 * @param nickName : Can be "server" if the message to display is a notice or an error.
	 * @param post : The message 
	 * @param channels : A list of available channels
	 * @param users : A list of connected users (to the current channel)
	 */
	public MessageFromServer(String nickName, String post, ArrayList<String> channels, ArrayList<String> users) {
		this.setNickName(nickName);
		this.setPost(post);
		this.setChannels(channels);
		this.setUsers(users);
	}


	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return this.nickName;
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
	 * @param nickName the nickName to set
	 */
	private void setNickName(String nickName) {
		this.nickName = nickName;
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

}
