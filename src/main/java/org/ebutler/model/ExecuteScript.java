package org.ebutler.model;

import org.json.JSONObject;

public class ExecuteScript extends Accion {
	
	private String path;

	public ExecuteScript() {
		this.type = "execute-script";
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

}
