package org.ebutler.telegram_bot;

import java.io.IOException;
import java.util.Properties;

public class TestMain {
	public static void main (String[] args) {
		
		Properties prop;
		try {
			prop = new LoaderConfig("config.properties").getConfig();
			
			ConversationManager m = new ConversationManager(prop.getProperty("api_url"), prop.getProperty("bot_token"));
			m.runUpdateLoop();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
