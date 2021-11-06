package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LoadingScreenController implements Initializable {
	
	@FXML
	private VBox rootVBox;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		rootVBox.setOpacity(0);
		makeFadeInTransition();
	}

	private void makeFadeInTransition() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.seconds(1));
		fadeTransition.setNode(rootVBox);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.play();
	}

}
