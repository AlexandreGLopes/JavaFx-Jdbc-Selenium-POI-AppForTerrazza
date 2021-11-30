package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ManualUpdaterController implements Initializable{
	
	@FXML
	private Button closeButton;

	@Override
	public void initialize(URL url, ResourceBundle rs) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	private void onCloseButtonAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

}
