package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Utils;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class LoadingScreenController implements Initializable {

	// Socket que vai ser utilizado nos vários métodos para conversar com o servidor
	private Socket cliente;

	// PrintWriter que vai ser utilizado pelos vários métodos e vai passar o
	// argumento para o switch case
	private PrintWriter pr;
	
	private InputStreamReader in;
	
	private BufferedReader bf;
	
	private String option;
	
	private String response;

	@FXML
	private Button refreshButton;

	@FXML
	private ImageView loadingGif;

	@FXML
	private Label txtLabel;
	
	@FXML
	private AnchorPane rootPane;

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
	
	@FXML
	private void onRefreshButtonAction(ActionEvent event) {
		if (option == null) {
			throw new IllegalStateException("Entity was null");
		}
		refreshButton.setVisible(false);
		refreshButton.setDisable(true);
		txtLabel.setVisible(false);
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), loadingGif);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);
		fadeTransition.play();
		fadeTransition.setOnFinished((e) -> {sendOptionToServer(event);});
	}
	
	@FXML
	private void sendOptionToServer(ActionEvent event) {
		
			try {
				cliente = new Socket("localhost", 3322);
				pr = new PrintWriter(cliente.getOutputStream());
				in = new InputStreamReader(cliente.getInputStream());
				bf = new BufferedReader(in);
				pr.println(option);
				pr.flush();
				waitForReponse(event, bf);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
	
	private void waitForReponse(ActionEvent event, BufferedReader bf) throws IOException {
		response = bf.readLine();
		switch (response) {
		case "a":
			Utils.currentStage(event).close();
			break;
		}
	}
}
