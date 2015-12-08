package org.ebutler.telegram_bot;

import java.util.ArrayList;
import java.util.Arrays;
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
		System.out.println(graph.toString());
		this.conversation = conversation;
		Runnable r = new Runnable(){
			@Override
			public void run() {
				execute();
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	private void execute() {
		System.out.println("Interpreter started");
		executeAction("a0"); //TODO: We shouldn't assume first state is a0, this should be in the format
	}

	private String waitForMessage(String expected_message) { //TODO: Maybe allow for regex patterns?
		List<String> l = new ArrayList<String>();
		l.add(expected_message);
		return waitForMessage(l);
	}
	
	private String waitForMessage(List<String> expected_messages) { //TODO: Maybe allow for regex patterns?
		String message = conversation.getMessage();
		while(message == null) {
			try {
				Thread.sleep(100); //TODO: Hardcoded 100
				message = conversation.getMessage();
			} catch (InterruptedException e) {}
		}
		//Ignore if message is not found
		if(!expected_messages.contains(message)) waitForMessage(expected_messages);
		return message;
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
		for(JSONObject transition : getTransitionsFor(state)) {
			//TODO: @JSF I should rethink this... Right now evaluating a condition would
			// 		stall waiting for its message.
			/*
			String destination = transition.getString("destination");
			JSONObject condition = transition.getJSONObject("condition");
			if(evaluateCondition(condition)) {
				executeAction(destination);
				break;
			}
			*/
		}
		conversation.sendMessage("I don't understand..."); // TODO: Send error phrase from config
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
