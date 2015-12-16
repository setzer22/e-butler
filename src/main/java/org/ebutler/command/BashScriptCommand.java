package org.ebutler.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

public class BashScriptCommand extends ScriptCommand {
	private static String bash_path = "/bin/bash";

	public BashScriptCommand(String path) throws IOException {
		super(path);
	}

	@Override
	public ScriptCommandOutput execute(Map<String, Object> variables) throws ExecuteException, IOException {
		
		CommandLine commandLine = new CommandLine(bash_path);
		commandLine.addArgument(getFile().getAbsolutePath());

		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(0);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		
		executor.setStreamHandler(streamHandler);

		int exitValue = executor.execute(commandLine, scriptVariablesMap(variables));
		
		return new ScriptCommandOutput(exitValue, outputStream.toString());
	}
	
	public static void setBashPath(String path) {
		bash_path = path;
	}
}
