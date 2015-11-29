package org.ebutler.editor_bot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;

@ManagedBean
public class DiagramaManager implements Serializable{

	private MindmapNode root;
    
    private MindmapNode selectedNode;
    
    private List<String> listOptions;
    
    private boolean primerNodo = false;
    private String fraseEntrada;
    private String color = "FFCC00";
    
    private String identificador;
    private String texto;
    private UploadedFile file;
    
    @PostConstruct
    private void initOptions() {
    	if(!primerNodo) {
	        RequestContext context = RequestContext.getCurrentInstance();
	        context.execute("firstConver.show();");
    	}
    	root = new DefaultMindmapNode("Inicio", "Inicio", color, true);
    	MindmapNode prueba = new DefaultMindmapNode("hola", "hola", color, true);
    	root.addNode(prueba);
    	MindmapNode prueba2 = new DefaultMindmapNode("adios", "adios", color, true);
    	prueba.addNode(prueba2);
    }
    
    public void crearEnviarMensaje() {
    	MindmapNode nuevoMensaje = new DefaultMindmapNode(identificador, texto, color, true);
    	selectedNode.addNode(nuevoMensaje);
    }
 
    public MindmapNode getRoot() {
        return root;
    }
    
    public void setRoot(MindmapNode root) {
        this.root = root;
    }
 
    public MindmapNode getSelectedNode() {
        return selectedNode;
    }
    public void setSelectedNode(MindmapNode selectedNode) {
        this.selectedNode = selectedNode;
    }
 
    public void onNodeSelect(SelectEvent event) {
    	selectedNode = (MindmapNode) event.getObject();
        
    }

	public List<String> getListOptions() {
		return listOptions;
	}

	public void setListOptions(List<String> listOptions) {
		this.listOptions = listOptions;
	}

	public boolean isPrimerNodo() {
		return primerNodo;
	}

	public void setPrimerNodo(boolean primerNodo) {
		this.primerNodo = primerNodo;
	}

	public String getFraseEntrada() {
		return fraseEntrada;
	}

	public void setFraseEntrada(String fraseEntrada) {
		this.fraseEntrada = fraseEntrada;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}
