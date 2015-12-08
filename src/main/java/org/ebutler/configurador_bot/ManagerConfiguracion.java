package org.ebutler.configurador_bot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.UploadedFile;

@ManagedBean(name = "configuracion")
@SessionScoped
public class ManagerConfiguracion implements Serializable{
	
	private boolean apagarOrdenador = true;
	
	private String URL;
	private String token;
	private String mensajeNoAuto;
	
	private List<String> numerosAutorizados;
	private String numeroNuevo;
	
	private String pathScripts;
	private UploadedFile file;
	
	private String pathAccesible;
	private String pathTeclado;
	
	public void save() {
		
	}
	
	public void addNumber() {
		numerosAutorizados.add(numeroNuevo);
		numeroNuevo = "";
	}
	
	public void deleteNumber() {
		numerosAutorizados.remove(numeroNuevo);
		numeroNuevo = "";
	}
	
	public ManagerConfiguracion() {
		numerosAutorizados = new ArrayList<String>();
	}
	
	public boolean isApagarOrdenador() {
		return apagarOrdenador;
	}

	public void setApagarOrdenador(boolean apagarOrdenador) {
		this.apagarOrdenador = apagarOrdenador;
	}

	public String getURL() {
		System.out.println(URL);
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

	public String getMensajeNoAuto() {
		return mensajeNoAuto;
	}

	public void setMensajeNoAuto(String mensajeNoAuto) {
		this.mensajeNoAuto = mensajeNoAuto;
	}

	public String getPathScripts() {
		return pathScripts;
	}

	public void setPathScripts(String pathScripts) {
		this.pathScripts = pathScripts;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getPathAccesible() {
		return pathAccesible;
	}

	public void setPathAccesible(String pathAccesible) {
		this.pathAccesible = pathAccesible;
	}

	public String getPathTeclado() {
		return pathTeclado;
	}

	public void setPathTeclado(String pathTeclado) {
		this.pathTeclado = pathTeclado;
	}

}
