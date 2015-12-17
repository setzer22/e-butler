package org.ebutler.telegram_bot;

import java.io.IOException;
import java.util.Properties;

public class TestMain {
	
	//TODO: This is worse than a singleton. But the properties should be accessible
	//		for later use somehow. For now this works
	public static Properties prop;
	
	public static void main (String[] args) {
		
		try {
			prop = new LoaderConfig("config.properties").getConfig();
			ConversationManager m = new ConversationManager(prop.getProperty("api_url"), prop.getProperty("bot_token"));
			m.runUpdateLoop();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
