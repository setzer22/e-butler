package org.ebutler.telegram_bot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

/*** This class handles polling messages and updating the right converations
 *   according to the user. Also creates new conversations when a new user starts a
 *   conversation with the bot.
 */
public class ConversationManager {
	
	private Map<Integer, Conversation> conversations_by_user;
	private Map<Integer, Interpreter> interpreters_by_user;
	private int current_offset = 0;
	private boolean stop = false;
	
	private String api_url;
	private String bot_token;
	//public String api_url = "https://api.telegram.org/bot";
    //public String bot_token = "140378637:AAG89CTnCXArvNprK1spEYN6sm1t2-QJgqM";

    //TODO: Delete this, the constructor should take the url, also remove hardcoded urls
	/*public ConversationManager() {
		conversations_by_user = new ConcurrentHashMap<Integer, Conversation>();
		interpreters_by_user = new ConcurrentHashMap<Integer, Interpreter>();
	}*/
	
	public ConversationManager(String api_url, String bot_token) {
		conversations_by_user = new ConcurrentHashMap<Integer, Conversation>();
		interpreters_by_user = new ConcurrentHashMap<Integer, Interpreter>();
		this.api_url = api_url;
		this.bot_token = bot_token;
		
	}
	
	public void notifyBot(Integer user_id, JSONObject message) {
		conversations_by_user.get(user_id).onMessage(message);
	}
	
	public void registerBot(Integer user_id, Integer chat_id) {
		Conversation bot = new Conversation(user_id, chat_id, api_url, bot_token);
		conversations_by_user.put(bot.getUser(), bot);
		
		Interpreter interp = new Interpreter(bot,this);
		interpreters_by_user.put(bot.getUser(), interp);
	}
	
	public void unregisterBot(Integer id) {
		//Delete the conversation and the interpreter with one user identified by id.
		conversations_by_user.remove(id);
		interpreters_by_user.remove(id);
	}

	public void stop() {
		this.stop = true;
	}
	
	/**Keeps polling the messages and calling events appropiately. Returns immediately
	 * and starts a new thread.*/
	public void runUpdateLoop() {
			Runnable runnable = new Runnable() {
				public void run() {
					while (!stop) {
						JSONArray updates = APIMethods.getUpdates(api_url, bot_token, current_offset);
						for(int i = 0; i < updates.length(); ++i) {
							JSONObject update = updates.getJSONObject(i);
							JSONObject message = update.getJSONObject("message");
							Integer from_id = message.getJSONObject("from").getInt("id");
							Integer chat_id = message.getJSONObject("chat").getInt("id");

							if (!conversations_by_user.containsKey(from_id)) {
								registerBot(from_id, chat_id);
							}
							notifyBot(from_id, message);
							
						}
						if(updates.length() > 0) {
							JSONObject last_update = updates.getJSONObject(updates.length() - 1);
							current_offset = last_update.getInt("update_id") + 1;
						}
					}
				}
			};

			new Thread(runnable).start();

	}
	
}
