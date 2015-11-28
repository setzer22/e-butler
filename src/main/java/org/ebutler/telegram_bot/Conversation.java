package org.ebutler.telegram_bot;

import java.io.File;

import org.json.JSONObject;

/*** This class represents a conversation between a bot and a user. It uses the
 *   telegram API. All telegram functionality should be handled at this level.
 *   Replacing this interface should essentially change the backend network.
 */
public class Conversation {
	
	private Integer user;
	private String api_url;
	private String bot_token;
	private Integer chat_id;

	public Integer getUser() {return user;}
	
	public Conversation(Integer user_id, Integer chat_id, String api_url, String bot_token) {
		this.user = user_id;
		this.chat_id = chat_id;
		this.api_url = api_url;
		this.bot_token = bot_token;
	}
	
	public void sendMessage(String text) {
		APIMethods.sendMessage(api_url, bot_token, chat_id, text);
	}
	
	public void sendPhoto(File file) {
		APIMethods.sendPhoto(api_url, bot_token, chat_id, file);
	}

	public void sendDocument(File file) {
		APIMethods.sendDocument(api_url, bot_token, chat_id, file);
	}

	public void sendAudio(File file) {
		APIMethods.sendAudio(api_url, bot_token, chat_id, file);
	}

	public void sendVideo(File file) {
		APIMethods.sendVideo(api_url, bot_token, chat_id, file);
	}

	public void sendVoice(File file) {
		APIMethods.sendVoice(api_url, bot_token, chat_id, file);
	}
	
	/** This gets called whenever a new message is sent to this conversation*/
	public void onMessage(JSONObject message) {
		System.out.println("[chat"+user+"]: "+ message.getString("text"));
		sendMessage(message.getString("text"));
	}
}
