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
		//TODO: No while true, when the execution is done this should return to the upper interpreter.
		//		But that doesn't exist yet.
		while(true) {
			executeState(initialState());
		}
	}

	// Waits until conversation has a message ready and returns it. 
	private String waitForMessage() {
		//@CopyPasted
		String message = conversation.getMessage();
		while(message == null) {
			try {
				Thread.sleep(100); //TODO: Hardcoded 100
				message = conversation.getMessage();
			} catch (InterruptedException e) {}
		}
		return message;
	}

	// Waits until conversation receives the expected message. All messages in-between
	// are ignored.
	private String waitForMessage(String expected_message) { //TODO: Maybe allow for regex patterns?
		List<String> l = new ArrayList<String>();
		l.add(expected_message);
		return waitForMessage(l);
	}

	// Waits until conversation receives one of the expected messages. All messages in-between
	// are ignored.
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

	
	private String initialState() {
		return graph.getString("initial-state");
	}

	private boolean isFinal(String state) {
		JSONArray final_states = graph.getJSONArray("final-states");
		for(int i = 0; i < final_states.length(); ++i) {
			String s = final_states.getString(i);
			if (s.equals(state)) return true;
		}
		return false;
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
	
	private void executeState(String state) {
		//Execute this state's action
		JSONObject action = findAction(state); 
		executeAction(action);
		
		if (isFinal(state)) {
			return;
		}
		
		//Select which branch to take
		//NOTE: converstaion.getMessage() keeps the last given value util you call consumeMessage. 
		//		This ensures that, for an action, all its branches will be evaluated against the
		//		same message.
		String next_state = null;
		boolean consumes_message = false; //Does any of the evaluated transitions wait for a message?
		for(JSONObject transition : getTransitionsFor(state)) {
			String destination = transition.getString("destination");
			JSONObject condition = transition.getJSONObject("condition");
			consumes_message = consumes_message || consumesMessage(condition);
			if(evaluateCondition(condition)) {
				next_state = destination;
				break;
			}
		}
		
		if(consumes_message) conversation.consumeMessage();

		if(next_state != null) executeState(next_state);
		else {
			conversation.sendMessage("I don't understand..."); // TODO: Send error phrase from config
			executeState(state);
		}
		
	}
	
	private void executeAction(JSONObject action) {
		switch (action.getString("type")) {
			case "send-message": {
				conversation.sendMessage(action.getString("text"));
			}
			case "no-op": {
				return;
			}
			//Add more cases later...
		}
		
	}
	
	private boolean evaluateCondition(JSONObject condition) {
		String type = condition.getString("type");
		switch(type) {
			case "text": {
				String last_message = waitForMessage();
				return condition.getString("text").equals(last_message);
			}
			case "otherwise": {
				return true;
			}
		}
		return false;
	}
	
	private boolean consumesMessage(JSONObject condition) {
		return condition.getString("type").equals("text");
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
