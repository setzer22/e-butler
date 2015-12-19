package org.ebutler.model;

public abstract class Accion {

	protected String name;
	protected String identificador;
	protected String type;
		
	public abstract String toJSON();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
