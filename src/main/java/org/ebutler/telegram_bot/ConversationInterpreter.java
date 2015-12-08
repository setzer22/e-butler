package org.ebutler.telegram_bot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

/***
 * This class is responsible for executing the conversation between the bot and the user
 * by sending the appropiate messages and waiting for using input according to a JSON-
 * specified graph.
 */
public class ConversationInterpreter {
	
	private JSONObject graph;
	private Conversation conversation;
	
	public ConversationInterpreter(JSONObject graph, Conversation conversation) {
		this.graph = graph;
		System.out.println(graph.toString());
		this.conversation = conversation;
	}
	
	public void execute() {
		System.out.println("Interpreter started");
		executeState(initialState());
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
				break;
			}
			case "send-photo": {
				conversation.sendPhoto(new File(action.getString("path")));
				break;
			}
			case "send-document": {
				conversation.sendDocument(new File(action.getString("path")));
				break;
			}
			case "send-audio": {
				conversation.sendAudio(new File(action.getString("path")));
				break;
			}
			case "send-video": {
				conversation.sendVideo(new File(action.getString("path")));
				break;
			}
			case "send-voice": {
				conversation.sendVoice(new File(action.getString("path")));
				break;
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
				String last_message = InterpreterUtils.waitForMessage(conversation);
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
