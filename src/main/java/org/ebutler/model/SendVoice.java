package org.ebutler.model;

import org.json.JSONObject;

public class SendVoice extends Accion {
	
	private String path;
	private String keyboard;
	
	public SendVoice() {
		this.type = "send-voice";
	}

	@Override
	public String toJSON() {
		return JSONObject.wrap(this).toString();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(String keyboard) {
		this.keyboard = keyboard;
	}

}

