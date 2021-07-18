/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	
    	this.txtGiorni.clear();
    	this.txtResult.clear();
    	
    	Actor a = this.boxAttore.getValue();
    	
    	if(a==null) {
    		this.txtResult.appendText("selezionare un attore");
    		return;
    	}
    	
    	List<Actor> attori = this.model.calcolaRaggiungibili(a);
    	
    	if(attori.size()==0) {
    		this.txtResult.appendText("vertice isolato");
    		return;
    	}
    	
    	this.txtResult.appendText("ATTORI SIMILI A: "+a.toString()+"\n");
    	for(Actor actor : attori) {
    		this.txtResult.appendText(actor.toString()+"\n");
    	}

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtGiorni.clear();
    	this.txtResult.clear();
    	
    	String genere = this.boxGenere.getValue();
    	
    	if(genere == null) {
    		this.txtResult.appendText("seleziona un genere");
    		return;
    	}
    	
    	this.model.creaGrafo(genere);
    	
    	this.txtResult.appendText("Grafo creato!\n#Vertici: "+this.model.getNVertici()+"\nArchi: "
    			+this.model.getNArchi());
    	
    	this.boxAttore.getItems().clear();
    	this.boxAttore.getItems().addAll(this.model.getVertici());
    	
    	this.btnSimili.setDisable(false);
    	this.btnSimulazione.setDisable(false);
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	this.txtResult.clear();
    	
    	try {
    		Integer n = Integer.parseInt(this.txtGiorni.getText());
    		
    		if(n<1) {
    			this.txtResult.setText("inserire un valore numerico positivo");
        		return;
    		}
    		
    		this.model.simula(n);
    		
    		Collection<Actor> intervistati = this.model.getIntervistati();
    		
    		if(intervistati.size()==0) {
    			this.txtResult.appendText("impossibile simulare");
    			return;
    			
    		}
    		
    		this.txtResult.appendText("pause: "+ this.model.getPause()+"\n");
    		
    		for(Actor a : intervistati) {
    			this.txtResult.appendText(a.toString()+"\n");
    		}
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("inserire un valore numerico");
    		return;
    		
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxGenere.getItems().addAll(this.model.getGeneri());
    }
}
