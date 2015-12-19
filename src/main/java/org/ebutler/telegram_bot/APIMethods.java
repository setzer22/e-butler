package org.ebutler.telegram_bot;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/*** This class handles all Telegram API calls. It's just a direct translation
 *   of the telegram API. */
public class APIMethods {

	
    public static JSONArray getUpdates(String api_url, String bot_token, Integer offset) {
        try {
			return Unirest.post(api_url + bot_token + "/getUpdates")
			        .field("offset", offset)
			        .asJson().getBody().getObject().getJSONArray("result");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    public static JSONObject sendMessage(String api_url, String bot_token, Integer chatId, String text) {
        try {
			return Unirest.post(api_url + bot_token + "/sendMessage")
			        .field("chat_id", chatId)
			        .field("text", text)
			        .asJson().getBody().getObject().getJSONObject("result");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

     public static JSONObject sendMessage(String api_url, String bot_token, Integer chatId, String text, JSONObject reply_markup) {
        try {
    		 HttpResponse<JsonNode> response = Unirest.post(api_url + bot_token + "/sendMessage")
			        .field("chat_id", chatId)
			        .field("text", text)
			        .field("reply_markup", reply_markup)
			        .asJson();
    		 return response.getBody().getObject().getJSONObject("result");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
     } 
     
     private static void sendMultiPart(String api_url, String bot_token, Integer chat_id, File file, String type) {
    	 String methodName = "/send" + Character.toUpperCase(type.charAt(0)) + type.substring(1, type.length());
    	 String fieldName = type;

		 HttpClient httpClient = HttpClients.createDefault();
		 HttpPost uploadFile = new HttpPost(api_url+bot_token+methodName);
 
		 MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		 builder.addTextBody("chat_id", chat_id.toString(), ContentType.APPLICATION_JSON);
		 builder.addBinaryBody(fieldName, file);
		 HttpEntity multipart = builder.build();
 
		 uploadFile.setEntity(multipart);
 
		 try {
			 httpClient.execute(uploadFile);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
    	 
     }
     
     public static void sendPhoto(String api_url, String bot_token, Integer chat_id, File file) {
    	 sendMultiPart(api_url, bot_token, chat_id, file, "photo");
     }

     public static void sendDocument(String api_url, String bot_token, Integer chat_id, File file) {
    	 sendMultiPart(api_url, bot_token, chat_id, file, "document");
     }

     public static void sendAudio(String api_url, String bot_token, Integer chat_id, File file) {
    	 sendMultiPart(api_url, bot_token, chat_id, file, "audio");
     }

     public static void sendVideo(String api_url, String bot_token, Integer chat_id, File file) {
    	 sendMultiPart(api_url, bot_token, chat_id, file, "video");
     }

     public static void sendVoice(String api_url, String bot_token, Integer chat_id, File file) {
    	 sendMultiPart(api_url, bot_token, chat_id, file, "voice");
     }

    
    
}
