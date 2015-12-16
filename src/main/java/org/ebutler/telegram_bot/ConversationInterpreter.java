package org.ebutler.telegram_bot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ebutler.command.BashScriptCommand;
import org.ebutler.command.ScriptCommand;
import org.ebutler.command.ScriptCommandOutput;
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
	
	private boolean browse_mode = false;
	private File browse_path;
	
	private Map<String, Object> variables; //TODO: Maybe make something less polymorphic than object to support our data types?
	
	private static final JSONObject defaultKeyboard;
	static {
		defaultKeyboard = new JSONObject("{\"hide_keyboard\": true}");
	}
	
	public ConversationInterpreter(JSONObject graph, Conversation conversation) {
		this.graph = graph;
		this.conversation = conversation;
		
		variables = new HashMap<String, Object>();
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
		JSONObject keyboard = null;
		if(!action.isNull("keyboard")) {
			keyboard = findKeyboard(action.getString("keyboard"));
		}
		switch (action.getString("type")) {
			case "send-message": {
				String text = expandVariables(action.getString("text"));
				if(keyboard != null) conversation.sendMessage(text, keyboard);
				else conversation.sendMessage(text, defaultKeyboard);
				break;
			}
			case "send-photo": {
				String path = expandVariables(action.getString("path"));
				conversation.sendPhoto(new File(path));
				break;
			}
			case "send-document": {
				String path = expandVariables(action.getString("path"));
				conversation.sendDocument(new File(path));
				break;
			}
			case "send-audio": {
				String path = expandVariables(action.getString("path"));
				conversation.sendAudio(new File(path));
				break;
			}
			case "send-video": {
				String path = expandVariables(action.getString("path"));
				conversation.sendVideo(new File(path));
				break;
			}
			case "send-voice": {
				String path = expandVariables(action.getString("path"));
				conversation.sendVoice(new File(path));
				break;
			}
			case "browse-mode": {
				browse_mode = true;
				String save_in = action.getString("save-in");
				File browse_path = new File(action.getString("path"));
				File selected_file = executeBrowseMode(browse_path);
				variables.put(save_in, selected_file);
				break;
			}
			case "execute-script": {
				browse_mode = true;
				String script_path = action.getString("path");
				try {
					ScriptCommand script = new BashScriptCommand(script_path);
					JSONObject output = script.execute(variables).getJSONOutput();
					for(Object k : output.keySet()) {
						variables.put(k.toString(), output.get(k.toString()));
					}
				} catch (IOException e) {
					conversation.sendMessage("ERROR: The script at "+script_path+" wasn't found");
				}
				break;
			}
			case "no-op": {
				return;
			}
			//Add more cases later...
		}
		
	}

	private String expandVariables(String string) {
		Pattern variable_pattern = Pattern.compile("\\$[a-zA-Z_]+");
		Matcher matcher = variable_pattern.matcher(string);
		StringBuilder builder = new StringBuilder();
		int last_end = 0;
		while(matcher.find()) {
			builder.append(string.substring(last_end, matcher.start()));
			String variable = string.substring(matcher.start()+1, matcher.end());
			if(variables.containsKey(variable)) {
				builder.append(variables.get(variable.toString()));
			}
			else {
				builder.append(string.substring(matcher.start(), Math.min(matcher.end() + 1, string.length())));
			}
			last_end = matcher.end();
		}
		builder.append(string.substring(last_end, string.length()));
		System.out.println(builder.toString());
		return builder.toString();
	}

	private JSONObject buildKeyboard(List<List<String>> keyboard) {
		return buildKeyboard(keyboard, false, false, false);
	}
	
	private JSONObject buildKeyboard(List<List<String>> keyboard, boolean resize_keyboard, boolean one_time_keyboard, boolean selective) {
		JSONObject keyboard_object = new JSONObject();
		JSONArray keyboard_array = new JSONArray();
		for(List<String> l : keyboard) {
			JSONArray row = new JSONArray(l);
			keyboard_array.put(l);
		}
		keyboard_object.put("keyboard", keyboard_array);
		keyboard_object.put("resize_keyboard", resize_keyboard);
		keyboard_object.put("one_time_keyboard", one_time_keyboard);
		keyboard_object.put("selective", selective);
		
		return keyboard_object;
	}
	
	private File executeBrowseMode(File browse_path) {
		if(browse_path.isDirectory()) {
			ArrayList<List<String>> keyboard_lst = new ArrayList<List<String>>();
			keyboard_lst.add(Arrays.asList("Select folder")); //TODO: Hardcoded string
			keyboard_lst.add(Arrays.asList(".."));
			for(File f : browse_path.listFiles()) {
				keyboard_lst.add(Arrays.asList(f.getName()));
			}
			JSONObject keyboard = buildKeyboard(keyboard_lst, true, false, false);
			conversation.sendMessage(browse_path.getAbsolutePath(), keyboard);
			String response = InterpreterUtils.waitForMessageOnce(conversation);

			// Loop breaks whenever one of the three if-cases are met. Sorry for the mess...
			while (true) {
				if(response.equals("Select folder")) { // Select whole folder
					return browse_path; //TODO: Hardcoded string
				}
				else if(response.equals("..")) { // Navigate to parent folder
					return executeBrowseMode(browse_path.getParentFile());
				}
				else if(keyboard_lst.contains(Arrays.asList(response))) { // Answer is a valid folder 
					File new_browse_path = new File(browse_path.getAbsolutePath() + File.separator + response);
					return executeBrowseMode(new_browse_path);
				}
				else {
					conversation.sendMessage("Sorry, I don't understand that folder");
					conversation.sendMessage(browse_path.getAbsolutePath(), keyboard);
					response = InterpreterUtils.waitForMessageOnce(conversation);

				}
			}
			
		}
		else return browse_path;
	}

	private JSONObject findKeyboard(String name) {
		JSONArray keyboards = graph.getJSONArray("keyboards");
		for(int i = 0; i < keyboards.length(); ++i) {
			JSONObject keyboard = keyboards.getJSONObject(i);
			if(keyboard.getString("name").equals(name)) {
				return keyboard.getJSONObject("value");
			}
		}
		return null;
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
