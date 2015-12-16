package org.ebutler.command;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.ExecuteException;

public abstract class ScriptCommand {
	
	private File scriptFile;
	
	public ScriptCommand(String path) throws IOException {
		this.scriptFile = new File(path);
		if(!this.scriptFile.exists()) {
			throw new IOException("File not found");
		}
	}
	
	public abstract ScriptCommandOutput execute(Map<String, Object> variables) throws ExecuteException, IOException;

	public File getFile() { return scriptFile; }
	
	// map: \[x,y] -> [x, y.toString()]
	public HashMap<String, String> scriptVariablesMap (Map<String, Object> variables) {
		HashMap<String, String> env = new HashMap<>();
		for (String key : variables.keySet()) {
			env.put(key, variables.get(key).toString());
		}
		return env;
	}
}
