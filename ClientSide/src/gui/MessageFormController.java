package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.entities.Costumer;

public class MessageFormController implements Initializable{
	
	private Costumer entity;
	
	@FXML
	private Label labelCostumerInfo;
	
	@FXML
	private Label labelCostumerPhone;
	
	@FXML
	private TextArea textMessage;
	
	@FXML
	private Button btEnviar;
	
	@FXML
	private Button btCancelar;
	
	@FXML
	public void onBtEnviarAction() {
		
	}
	
	@FXML
	public void onBtCancelarAction() {
		
	}
	
	public void setCostumer(Costumer entity) {
		this.entity = entity;
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		labelCostumerInfo.setText("Cliente: " + entity.getNome() 
									+ " " + entity.getSobrenome());
		labelCostumerPhone.setText("Telefone: " + entity.getTelefone());
	}

}
