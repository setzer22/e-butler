package org.ebutler.model;

import org.json.JSONObject;

public class NoOp extends Accion {
	
	public NoOp() {
		this.type = "no-op";
	}

	@Override
	public String toJSON() {
		return JSONObject.wrap(this).toString();
	}

}
