/**
 * @author Snake
 * Message send by the client to the server.
 */
package serial;

import java.util.ArrayList;

import org.json.JSONObject;

public class MessageToServer extends JSONObject {

	private String nickname;
	private String post;
	private ArrayList<String> args;
	
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
	
}
