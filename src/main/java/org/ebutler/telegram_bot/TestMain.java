package org.ebutler.telegram_bot;

import java.io.IOException;
import java.util.Properties;

public class TestMain {
	public static void main (String[] args) {
		
		Properties prop;
		try {
			prop = new LoaderConfig("sampleProperties.properties").getConfig();
			
			//String bot_token = "140378637:AAG89CTnCXArvNprK1spEYN6sm1t2-QJgqM";
			//String api_url = "https://api.telegram.org/bot";
			//ConversationManager m = new ConversationManager(api_url, bot_token);
			
			ConversationManager m = new ConversationManager(prop.getProperty("api_url"), prop.getProperty("bot_token"));
			m.runUpdateLoop();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
