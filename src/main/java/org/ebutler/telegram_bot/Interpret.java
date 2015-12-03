package org.ebutler.telegram_bot;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

/***
 * This class is responsible for executing the conversation between the bot and the user
 * by sending the appropiate messages and waiting for using input according to a JSON-
 * specified graph.
 *
 */


//TODO: We'll have to make it so this class is a bigger state that checks for the trigger
//		phrase of each conversation and launches that conversation once it gets the message.
//		For now we'll be using it to execute a single conversation.

//NOTE: Avoid navigating the JSON in this class' code and always use the helper functions. If
//		there's a functionality not covered by those, create a new one.
public class Interpret {
	
	private JSONObject graph;
	private Conversation conversation;
	
	public Interpret(JSONObject graph, Conversation conversation) {
		this.graph = graph;
		this.conversation = conversation;
		execute(); //TODO: This should go in a new thread or will block the caller.
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
	
	
	private JSONObject findAction(String state) {
		JSONArray actions = graph.getJSONArray("actions");
		for(int i = 0; i < actions.length(); ++i) {
			if(actions.getJSONObject(i).getString("name").equals(state)) {
				return actions.getJSONObject(i);
			}
		}
		return null;
	}
	
	private void executeAction(String state) {
		JSONObject action = findAction(state); 
		switch (action.getString("type")) {
			case "send-message": {
				conversation.sendMessage(action.getString("text"));
			}
			//Add more cases later...
		}
	}
	
	private List<JSONObject> getTransitionsFor(String state) {
		List<JSONObject> result = new ArrayList<JSONObject>();
		JSONArray transitions = graph.getJSONArray("transitions");
		for(int i = 0; i < transitions.length(); ++i) {
			if (transitions.getJSONObject(i).getString("origin").equals(state)) {
				result.add(transitions.getJSONObject(i));
			}
		}
		return result;
	}
}
