package org.ebutler.model;

public class EnviarMensaje extends Accion {
	
	private String texto;
	private String teclado;

	@Override
	public String toJSON() {
		String result = "{";
		result += "'name': '" + this.name + "', ";
		result += "'type': '" + this.type + "', ";
		result += "'text': '" + this.texto + "', ";
		if(teclado != null && !teclado.equals(""))
			result += "'keyboard': '" + this.teclado + "', ";
		else
			result += "'keyboard': 'none', ";	
		result += "}";
		return result;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTeclado() {
		return teclado;
	}

	public void setTeclado(String teclado) {
		this.teclado = teclado;
	}

}
