package org.ebutler.configurador_bot;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "configuracion")
public class ManagerConfiguracion {
	
	private boolean apagarOrdenador = true;

	public boolean isApagarOrdenador() {
		return apagarOrdenador;
	}

	public void setApagarOrdenador(boolean apagarOrdenador) {
		this.apagarOrdenador = apagarOrdenador;
	}

}
