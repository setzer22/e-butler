package org.ebutler.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

public class PythonScriptCommand extends ScriptCommand {
	
	private static String python_path = "/usr/bin/python";

	public PythonScriptCommand(String path) throws IOException {
		super(path);
	}

	@Override
	public ScriptCommandOutput execute() throws ExecuteException, IOException {
		CommandLine commandLine = new CommandLine(python_path);
		commandLine.addArgument(getFile().getAbsolutePath());

		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(0);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
		
		executor.setStreamHandler(streamHandler);

		int exitValue = executor.execute(commandLine);
		
		return new ScriptCommandOutput(exitValue, outputStream.toString());
	}
	
	public static void setPythonPath(String path) {
		python_path = path;
	}
	
}
