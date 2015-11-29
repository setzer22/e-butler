package org.ebutler.command;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.ExecuteException;

public abstract class ScriptCommand {
	
	private File scriptFile;
	
	public ScriptCommand(String path) throws IOException {
		this.scriptFile = new File(path);
		if(!this.scriptFile.exists()) {
			throw new IOException("File not found");
		}
	}
	
	public abstract ScriptCommandOutput execute() throws ExecuteException, IOException;
	
	public File getFile() { return scriptFile; }
	
}
