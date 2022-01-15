package gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ManualUpdaterHelpController implements Initializable{
	
	@FXML
	private Button closeButton;
	
	@FXML
	private ImageView imageViewWaitlistGif;
	
	@FXML
	private ImageView imageViewWixGif;

	@Override
	public void initialize(URL url, ResourceBundle rs) {
		// TODO Auto-generated method stub
	}
	
	public void setGifs() {
		Image gifWaitlist = new Image(new File("res/waitlisthelp.gif").toURI().toString());
		Image gifWix = new Image(new File("res/wixhelp.gif").toURI().toString());
		imageViewWaitlistGif.setImage(gifWaitlist);
		imageViewWixGif.setImage(gifWix);
	}
	
	@FXML
	private void onCloseButtonAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

}
