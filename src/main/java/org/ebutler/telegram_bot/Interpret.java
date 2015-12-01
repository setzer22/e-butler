package org.ebutler.telegram_bot;

import org.json.JSONObject;

/***
 * This class execute one conversation between bot and user in Telegram chat specified by JSON graph.
 *
 */

public class Interpret {
	
	private JSONObject graph;
	private Conversation conversation;
	
	public Interpret(JSONObject graph, Conversation conversation) {
		this.graph = graph;
		this.conversation = conversation;
		execute();
	}
	
	private void execute() {
		//TODO: this method execute the JSON conversation.
				
		/* Example wait a message:
		 * lastmessage = conversation.getMessage();
		 * while(lastmessage == null){
		 * 	sleep(0.5);
		 * 	lastmessage = conversation.getMessage(); 
		 * }
		 * do something with this message.
		 */
		
		
	}
}
