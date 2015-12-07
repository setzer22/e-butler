package org.ebutler.configurador_bot;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "configuracion")
public class ManagerConfiguracion {
	
	private boolean apagarOrdenador = true;
	private String URL;
	private String token;

	private List<String> numerosAutorizados;
	private String numeroNuevo = "";
	
	public void addNumber() {
		numerosAutorizados.add(numeroNuevo);
		numeroNuevo = "";
	}
	
	public ManagerConfiguracion() {
		numerosAutorizados = new ArrayList<String>();
		numerosAutorizados.add("hola");
	}
	
	public boolean isApagarOrdenador() {
		return apagarOrdenador;
	}

	public void setApagarOrdenador(boolean apagarOrdenador) {
		this.apagarOrdenador = apagarOrdenador;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getNumerosAutorizados() {
		return numerosAutorizados;
	}

	public void setNumerosAutorizados(List<String> numerosAutorizados) {
		this.numerosAutorizados = numerosAutorizados;
	}

	public String getNumeroNuevo() {
		return numeroNuevo;
	}

	public void setNumeroNuevo(String numeroNuevo) {
		this.numeroNuevo = numeroNuevo;
	}

}
