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

	@FXML
	private Button refreshButton;

	@FXML
	private ImageView loadingGif;

	@FXML
	private Label txtLabel;

	private String option;

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@FXML
	private AnchorPane rootPane;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	private void makeFadeInTransition() {
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), loadingGif);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);
		fadeTransition.play();

	}

	@FXML
	private void onRefreshButtonAction(ActionEvent event) {
		if (option == null) {
			throw new IllegalStateException("Entity was null");
		}
		refreshButton.setVisible(false);
		refreshButton.setDisable(true);
		txtLabel.setVisible(false);
		makeFadeInTransition();
		switch (option) {
		case "a":
			try {
				cliente = new Socket("localhost", 3322);
				pr = new PrintWriter(cliente.getOutputStream());
				pr.println("a");
				pr.flush();
				waitForReponse(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "b":
			try {
				cliente = new Socket("localhost", 3322);
				pr = new PrintWriter(cliente.getOutputStream());
				pr.println("b");
				pr.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			System.out.println("no valid option");
		}
		//waitForReponse(event);
	}

	private void waitForReponse(ActionEvent event) {
		try {
			int continuar = 1;
			do {
			InputStreamReader in = new InputStreamReader(cliente.getInputStream());
			BufferedReader bf = new BufferedReader(in);
			String response = bf.readLine();
			switch (response) {
			case "a":
				Utils.currentStage(event).close();
				continuar = 2;
				break;

			default:
				break;
			}
			} while (continuar == 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
