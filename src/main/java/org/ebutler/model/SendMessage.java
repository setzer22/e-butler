package org.ebutler.model;

import org.json.JSONObject;

public class SendMessage extends Accion {
	
	private String text;
	private String keyboard;
	
	public SendMessage() {
		this.type = "send-message";
	}

	@Override
	public String toJSON() {
		return JSONObject.wrap(this).toString();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(String keyboard) {
		this.keyboard = keyboard;
	}

}
