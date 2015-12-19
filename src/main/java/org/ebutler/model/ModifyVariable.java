package org.ebutler.model;

import org.json.JSONObject;

public class ModifyVariable extends Accion {
	
	private String variable;
	private String new_value;

	public ModifyVariable() {
		this.type = "modify-variable";
	}

	@Override
	public String toJSON() {
		return JSONObject.wrap(this).toString();
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getNew_value() {
		return new_value;
	}

	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}


}
