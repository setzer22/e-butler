package org.ebutler.command;

import org.json.JSONObject;

/**Encapsulates the output for a ScriptCommand*/
public class ScriptCommandOutput {

	private int exit_value;
	private String stdout;
	public JSONObject stdout_json = null;
	
	public ScriptCommandOutput(int exit_value, String stdout) {
		this.exit_value = exit_value;
		this.stdout = stdout;
	}
	
	/**Returns the output as a string*/
	public String getStringOutput() {return stdout;}

	/**Returns the output as a JSON structure*/
	public JSONObject getJSONOutput() {
		if(stdout_json == null)
			stdout_json = new JSONObject(stdout);
		return stdout_json;
	}
	
	/**Returns the exit value*/
	public int getExitValue() { return exit_value; }

	@Override
	public String toString() {
		return stdout;
	}
	
	
}
