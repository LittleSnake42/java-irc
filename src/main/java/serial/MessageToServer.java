/**
 * @author Snake
 * Message send by the client to the server.
 */
package serial;

import java.io.Serializable;

public class MessageToServer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nickName;
	private String post;
	private String parameter1;
	private String parameter2;
	
	/**
	 * @param nickName : The nickname chosen by the user.
	 * @param post : Can be a normal message, or a command if begins by "#".
	 * 		Available commands : "#CONNECT serverIp nickName", "#JOIN channelName", "#QUIT", "#EXIT"
	 * @param parameter1 : If @param post begins by "#"
	 * @param parameter2 : If @param post begins by "#"
	 */
	public MessageToServer(String nickName, String post, String parameter1,String parameter2) {
		this.setNickName(nickName);
		this.setPost(post);
		this.setParameter1(parameter1);
		this.setParameter2(parameter2);
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @return the post
	 */
	public String getPost() {
		return post;
	}

	/**
	 * @return the parameter1
	 */
	public String getParameter1() {
		return parameter1;
	}

	/**
	 * @return the parameter2
	 */
	public String getParameter2() {
		return parameter2;
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
	 * @param parameter1 the parameter1 to set
	 */
	private void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}

	/**
	 * @param parameter2 the parameter2 to set
	 */
	private void setParameter2(String parameter2) {
		this.parameter2 = parameter2;
	}
	
}
