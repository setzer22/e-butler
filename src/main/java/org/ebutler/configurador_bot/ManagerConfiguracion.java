package org.ebutler.configurador_bot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.ebutler.telegram_bot.LoaderConfig;
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
	
	public void runBot() {
		
	}
	
	public void save() {
		Properties prop = new Properties();
		OutputStream out = null;
		
		try {
			File file = new File("config.properties");
			if(!file.exists())
				file.createNewFile();
			out = new FileOutputStream(file);
			
			String numeros = "";
			if(numerosAutorizados.size() > 0) {
				numeros = numerosAutorizados.get(0);
				numerosAutorizados.remove(0);
			}
			for(String str : numerosAutorizados)
				numeros += "," + str;
			
			prop.setProperty("api_url", URL);
			prop.setProperty("bot_token", token);
			prop.setProperty("authorized_folder", pathAccesible);
			prop.setProperty("authorized_users", numeros);
			
			
			
			prop.store(out, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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
		try {
			Properties prop = new LoaderConfig("config.properties").getConfig();
			URL = prop.getProperty("api_url");
			token = prop.getProperty("bot_token");
			pathAccesible = prop.getProperty("authorized_folder");
			String numeros = prop.getProperty("authorized_users");
			List<String> auxnumerosAutorizados = Arrays.asList(numeros.split(","));
			numerosAutorizados = new ArrayList<String>();
			
			for(String s : auxnumerosAutorizados)
				numerosAutorizados.add(s);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
