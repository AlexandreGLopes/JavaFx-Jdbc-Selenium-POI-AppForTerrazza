package gui;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import model.entities.Costumer;

public class NotesFormController implements Initializable {
	
	private Logger logger = LogManager.getLogger(NotesFormController.class);
	
	private Costumer entity;
	
	@FXML
	private VBox rootVBox;
	
	@FXML
	private Label labelCostumerName;
	
	@FXML
	private Label labelReservationHour;
	
	@FXML
	private TextArea textAreaNotes;
	
	public void setCostumer(Costumer entity) {
		this.entity = entity;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
	}
	
	public void updateFormData() {
		if (entity == null) {
			logger.error("Entity was null");
			throw new IllegalStateException("Entity was null");
		}
		labelCostumerName.setText("Cliente: " + entity.getNome() + " " + entity.getSobrenome());
		labelReservationHour.setText("Horário: " + entity.getHora());
		textAreaNotes.setText(entity.getObservacao());
	}

}
