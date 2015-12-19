package org.ebutler.model;

import org.json.JSONObject;

public class SendVideo extends Accion {
	
	private String path;
	private String keyboard;
	
	public SendVideo() {
		this.type = "send-video";
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
