package org.ebutler.telegram_bot;

import java.util.ArrayList;
import java.util.List;

/** This module contains all utility methods that are needed both in Interpreter and in ConversationInterpreter
 *  in order to avoid code replication.
 */
public class InterpreterUtils {

	// Waits until conversation has a message ready and returns it. The message is consumed
	public static String waitForMessageOnce(Conversation conversation) {
		String msg = waitForMessage(conversation);
		conversation.consumeMessage();
		return msg;
	}

	// Waits until conversation has a message ready and returns it. 
	public static String waitForMessage(Conversation conversation) {
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
	public static String waitForMessage(Conversation conversation, String expected_message) { //TODO: Maybe allow for regex patterns?
		List<String> l = new ArrayList<String>();
		l.add(expected_message);
		return waitForMessage(conversation, l);
	}

	// Waits until conversation receives one of the expected messages. All messages in-between
	// are ignored.
	public static String waitForMessage(Conversation conversation, List<String> expected_messages) { //TODO: Maybe allow for regex patterns?
		String message = conversation.getMessage();
		while(message == null) {
			try {
				Thread.sleep(100); //TODO: Hardcoded 100
				message = conversation.getMessage();
			} catch (InterruptedException e) {}
		}
		//Ignore if message is not found
		if(!expected_messages.contains(message)) waitForMessage(conversation, expected_messages);
		return message;
	}
}
