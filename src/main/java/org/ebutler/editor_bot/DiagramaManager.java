package org.ebutler.editor_bot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.ebutler.model.SendMessage;
import org.primefaces.context.RequestContext;
import org.primefaces.event.diagram.ConnectEvent;
import org.primefaces.event.diagram.ConnectionChangeEvent;
import org.primefaces.event.diagram.DisconnectEvent;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.DiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.endpoint.RectangleEndPoint;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;

@ManagedBean
@ViewScoped
public class DiagramaManager implements Serializable{

	private DefaultDiagramModel model;
    
    private String identificador;
    private String texto;
    private List<String> teclados;
    private int count = 0;
 
    @PostConstruct
    public void init() {
        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);
        
        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{strokeStyle:'#98AFC7', lineWidth:3}");
        connector.setHoverPaintStyle("{strokeStyle:'#5C738B'}");
        model.setDefaultConnector(connector);
    }
     
    public void save() {
    	
    }
   
    
    public DiagramModel getModel() {
        return model;
    }
    
    public void crearEM() {
    	SendMessage accioObj = new SendMessage();
    	accioObj.setIdentificador(identificador);
    	accioObj.setText(texto);
    	accioObj.setName("a"+ count);
    	++count;
    	
    	Element accion = new Element(accioObj);
    	accion.setDraggable(true);
        EndPoint endPointCA = createRectangleEndPoint(EndPointAnchor.AUTO_DEFAULT);
        endPointCA.setSource(true);
        endPointCA.setTarget(true);
        accion.addEndPoint(endPointCA);
        
        model.addElement(accion);
    }
     
    public void onConnect(ConnectEvent event) {
            Connection conn = new Connection(event.getSourceElement().getEndPoints().get(0), event.getTargetElement().getEndPoints().get(0));
            conn.getOverlays().add(new LabelOverlay("hola", "flow-label", 0.5));
            conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));
            model.connect(conn);
    }
     
    public void onDisconnect(DisconnectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Disconnected", 
                    "From " + event.getSourceElement().getData()+ " To " + event.getTargetElement().getData());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
         
        RequestContext.getCurrentInstance().update("form:msgs");
    }
     
    public void onConnectionChange(ConnectionChangeEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Connection Changed", 
                    "Original Source:" + event.getOriginalSourceElement().getData() + 
                    ", New Source: " + event.getNewSourceElement().getData() + 
                    ", Original Target: " + event.getOriginalTargetElement().getData() + 
                    ", New Target: " + event.getNewTargetElement().getData());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
         
        RequestContext.getCurrentInstance().update("form:msgs");
    }
     
    private EndPoint createRectangleEndPoint(EndPointAnchor anchor) {
        RectangleEndPoint endPoint = new RectangleEndPoint(anchor);
        endPoint.setScope("network");
        endPoint.setSource(true);
        endPoint.setStyle("{fillStyle:'#98AFC7'}");
        endPoint.setHoverStyle("{fillStyle:'#5C738B'}");
         
        return endPoint;
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

	public List<String> getTeclados() {
		return teclados;
	}

	public void setTeclados(List<String> teclados) {
		this.teclados = teclados;
	}
}
