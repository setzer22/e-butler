package org.ebutler.telegram_bot;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/*** This class handles all Telegram API calls. It's just a direct translation
 *   of the telegram API. */
//TODO: API URL and Token should be configurable
//TODO: Static is probably not a good idea, but OK for now
public class APIMethods {

	private static String api_url = "https://api.telegram.org/bot";
    private static String bot_token = "140378637:AAG89CTnCXArvNprK1spEYN6sm1t2-QJgqM";
	
    public static HttpResponse<JsonNode> getUpdates(Integer offset) {
        try {
			return Unirest.post(api_url + bot_token + "/getUpdates")
			        .field("offset", offset)
			        .asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    public static HttpResponse<JsonNode> sendMessage(Integer chatId, String text) {
        try {
			return Unirest.post(api_url + bot_token + "/sendMessage")
			        .field("chat_id", chatId)
			        .field("text", text)
			        .asJson();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    } 

    
    
}
