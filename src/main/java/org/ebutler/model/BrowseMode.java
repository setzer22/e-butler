package org.ebutler.model;

import org.json.JSONObject;

public class BrowseMode extends Accion {
	
	private String path;
	private String save_in;

	public BrowseMode() {
		this.type = "browse-mode";
	}

	@Override
	public String toJSON() {
		return JSONObject.wrap(this).toString();
	}

	//@Auto-generated
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSaveIn() {
		return save_in;
	}

	public void setSaveIn(String save_in) {
		this.save_in = save_in;
	}

}
