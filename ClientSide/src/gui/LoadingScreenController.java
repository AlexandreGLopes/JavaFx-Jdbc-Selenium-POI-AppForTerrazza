package gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alerts;
import gui.util.Utils;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class LoadingScreenController implements Initializable {

	private String option;

	@FXML
	private Button refreshButton;
	
	@FXML
	private Button cancelButton;

	@FXML
	private ImageView loadingGif;

	@FXML
	private Label txtLabel;

	@FXML
	private GridPane rootPane;
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}
	
	public void setLabel(String option) {
		this.txtLabel.setText(option);
	}

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
			Alerts.showAlert("Erro", null, "Informe o desenvolvedor:\nOpção (option) estava nula", AlertType.ERROR);
			throw new IllegalStateException("Option was null");
		}
		refreshButton.setVisible(false);
		refreshButton.setDisable(true);
		cancelButton.setVisible(false);
		cancelButton.setDisable(true);
		txtLabel.setVisible(false);
		makeFadeInTransition();
		sendOptionToServer(event);
	}

	private void sendOptionToServer(ActionEvent event) {

		// Running server communication in a Task, in a background thread to unblock the
		// UI and show all the Transitions
		Task<String> serverCommunicationTask = new Task<>() {
			@Override
			protected String call() throws Exception {
				try (Socket cliente = new Socket("localhost", 3322);
						PrintWriter pr = new PrintWriter(cliente.getOutputStream());
						BufferedReader bf = new BufferedReader(new InputStreamReader(cliente.getInputStream()));) {
					pr.println(option);
					pr.flush();
					return bf.readLine();
				}
			}
		};

		serverCommunicationTask.setOnSucceeded((e) -> {
			if ("c".equals(serverCommunicationTask.getValue())) {
				Utils.currentStage(event).close();
			}
		});

		serverCommunicationTask.setOnFailed((e) -> {
			serverCommunicationTask.getException().printStackTrace();
			// handle exception...
		});

		Thread thread = new Thread(serverCommunicationTask);
		thread.setDaemon(true);
		thread.start();

	}
	
	//Botão de cancelamento para fechar o painel, pois ele não tem barra superior
	@FXML
	private void onCancelButtonAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
}