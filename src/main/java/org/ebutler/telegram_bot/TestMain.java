package org.ebutler.telegram_bot;

public class TestMain {
	public static void main (String[] args) {
		ConversationManager m = new ConversationManager();
		m.runUpdateLoop();
	}
}
