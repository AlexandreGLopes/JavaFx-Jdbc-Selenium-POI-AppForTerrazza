package gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.PreferencesManager;
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
	
	Preferences preferences = PreferencesManager.getPreferences();

	private String option;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private Button refreshButton;
	
	@FXML
	private Button cancelButton;

	@FXML
	private ImageView loadingGif;

	@FXML
	private Label txtLabel;
	
	@FXML
	private Label txtCarregando;

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
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
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
		txtCarregando.setVisible(true);
		makeFadeInTransition();
		sendOptionToServer(event);
	}

	private void sendOptionToServer(ActionEvent event) {

		// Running server communication in a Task, in a background thread to unblock the
		// UI and show all the Transitions
		Task<String> serverCommunicationTask = new Task<>() {
			@Override
			protected String call() throws Exception {
				String ipOfServer = preferences.get(PreferencesManager.IP_TO_SERVERSIDE_MACHINE, null);
				try (Socket cliente = new Socket(ipOfServer, 3322);
						PrintWriter pr = new PrintWriter(cliente.getOutputStream());
						BufferedReader bf = new BufferedReader(new InputStreamReader(cliente.getInputStream()));) {
					pr.println(option);
					pr.flush();
					cliente.setSoTimeout(1000);
					return bf.readLine();
				}
			}
		};

		serverCommunicationTask.setOnSucceeded((e) -> {
			//Se o servidor concluir tudo corretamente
			if ("close".equals(serverCommunicationTask.getValue())) {
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
			//Se der erro na parte do selenium
			if ("erro".equals(serverCommunicationTask.getValue())) {
				Alerts.showAlert("Erro", null, "Informe o desenvolvedor:\nAlgo deu errado ao acessar o site de reservas terceiro.\n\nVocê pode contornar o problema adicionando as reservas manualmente.\nPara saber mais clique em Ajuda", AlertType.ERROR);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
			//Se der erro em algum processo interno no servidor
			if ("erroServer".equals(serverCommunicationTask.getValue())) {
				Alerts.showAlert("Erro", null, "Informe o desenvolvedor:\nAlgo deu errado com nosso servidor interno.\n\nVocê pode contornar o problema adicionando as reservas manualmente.\nPara saber mais clique em Ajuda", AlertType.ERROR);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
			//Se der erro em algum processo interno no banco de dados
			if ("erroDB".equals(serverCommunicationTask.getValue())) {
				Alerts.showAlert("Erro", null, "Informe o desenvolvedor:\nAlgo deu errado com nosso Banco de Dados.\n\nVocê pode contornar o problema adicionando as reservas manualmente.\nPara saber mais clique em Ajuda", AlertType.ERROR);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
		});

		serverCommunicationTask.setOnFailed((e) -> {
			//Se não conseguirmos nos conectar com o servidor pelo Socket
			serverCommunicationTask.getException().printStackTrace();
			Alerts.showAlert("Demora na resposta do servidor", null, "O servidor está demorando para responder. Pode ser que ele esteja trabalhando com lentidão. Pressione o botão RESERVAS para verificar se a lista será atualizada.\n\nCaso contrário, tente utilizar a opção: ATUALIZAR MANUALMENTE.\n\nContate o desenvolvedor para tentar resolver o problema.", AlertType.ERROR);
			Utils.currentStage(event).close();
		});

		Thread thread = new Thread(serverCommunicationTask);
		thread.setDaemon(true);
		thread.start();

	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	//Botão de cancelamento para fechar o painel, pois ele não tem barra superior
	@FXML
	private void onCancelButtonAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
}