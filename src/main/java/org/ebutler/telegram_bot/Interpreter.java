package org.ebutler.telegram_bot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Interpreter {
	
	Conversation conversation;
	Map<String, JSONObject> conversations_by_trigger;
	ConversationManager CM;
	
	public Interpreter(Conversation conversation, ConversationManager CM) {
		this.CM = CM;
		conversations_by_trigger = new HashMap<String, JSONObject>();
		this.conversation = conversation;

		//TODO: Hardcoded initialization code 
			File f = new File("C:/Users/Teku/repos/e-butler/Conversations/simple-2.json");
			JSONObject conv_json = null;
			try {
				byte[] encoded = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
				String json = new String(encoded, "UTF-8");
				conv_json = new JSONObject(json);
			}
			catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Java can sometimes be a frustrating experience");
			}
			conversations_by_trigger.put("/therapist", conv_json);
		// ^ Hardcoded initialization code

		Runnable r = new Runnable() {
			@Override
			public void run() {
				execute();
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public void execute() {
		
		//Wait for /start
		String message = InterpreterUtils.waitForMessageOnce(conversation);
		while(!message.equals("/start")) {
			conversation.sendMessage("Type /start to begin"); //TODO: Add this to the config
			message = InterpreterUtils.waitForMessageOnce(conversation);
		}

		//TODO: Maybe add a default /exit action?
		String trigger_phrase = " ";
		//while (!trigger_phrase.equals("/exit")) { 
		while (true) { 
			String available = "";
			/*scope*/ {
				available = "Welcome, please select one of the available actions:"; //TODO: Add this to the config
				int  count = 0; 
				for(String trigger : conversations_by_trigger.keySet()) {
					available += "\n  "+count+". "+trigger;
					++count;
				}
				available += "\n "+count+". /exit";
			}
			conversation.sendMessage(available); 

			//Wait for trigger phrase
			trigger_phrase = InterpreterUtils.waitForMessageOnce(conversation);
			
			while (!conversations_by_trigger.containsKey(trigger_phrase)) {
				if(trigger_phrase.equals("/exit")) {
					CM.unregisterBot(conversation.getUser());
					return;
				}
				conversation.sendMessage(available);
				trigger_phrase = InterpreterUtils.waitForMessageOnce(conversation);
			}
			
			JSONObject conversation_json = conversations_by_trigger.get(trigger_phrase);
			ConversationInterpreter interp = new ConversationInterpreter(conversation_json, conversation);
			interp.execute();
		}
	}
}
