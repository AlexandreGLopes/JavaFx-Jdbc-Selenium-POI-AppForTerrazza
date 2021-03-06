package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class LoadingScreenController implements Initializable {

	private Logger logger = LogManager.getLogger(LoadingScreenController.class);

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
		Image gif = new Image(new File("res/loading.gif").toURI().toString());
		loadingGif.setImage(gif);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);
		fadeTransition.play();
	}

	@FXML
	private void onRefreshButtonAction(ActionEvent event) {
		if (option == null) {
			Alerts.showAlert("Erro", null, "Informe o desenvolvedor:\nOp????o (option) estava nula", AlertType.ERROR);
			logger.error("Option was null");
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
				// Buscado a prefer??ncia que guarda o IP e colocando ela numa vari??vel string
				// que ser?? utilizada para iniciar a comunica????o
				String ipOfServer = preferences.get(PreferencesManager.IP_TO_SERVERSIDE_MACHINE, null);
				try (Socket cliente = new Socket(ipOfServer, 3322);
						PrintWriter pr = new PrintWriter(cliente.getOutputStream());
						BufferedReader bf = new BufferedReader(new InputStreamReader(cliente.getInputStream()));) {
					pr.println(option);
					pr.flush();
					// Estabelecendo um timeout de acordo com as prefer??ncias 
					String esperaServidor = preferences.get(PreferencesManager.ESPERA_POR_TAREFA_DO_SERVIDOR, null);
					Integer timeout = Integer.parseInt(esperaServidor);
					cliente.setSoTimeout(timeout);
					return bf.readLine();
				}
			}
		};

		serverCommunicationTask.setOnSucceeded((e) -> {
			// Se o servidor concluir tudo corretamente
			if ("close".equals(serverCommunicationTask.getValue())) {
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
			// Se der erro na parte do selenium
			if ("erro".equals(serverCommunicationTask.getValue())) {
				Alerts.showAlert("Erro", null,
						"Informe o desenvolvedor:\nAlgo deu errado ao acessar o site de reservas terceiro.\n\nVoc?? pode contornar o problema adicionando as reservas manualmente.\nPara saber mais clique em Ajuda",
						AlertType.ERROR);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
			// Se der erro em algum processo interno no servidor
			if ("erroServer".equals(serverCommunicationTask.getValue())) {
				Alerts.showAlert("Erro", null,
						"Informe o desenvolvedor:\nAlgo deu errado com nosso servidor interno.\n\nVoc?? pode contornar o problema adicionando as reservas manualmente.\nPara saber mais clique em Ajuda",
						AlertType.ERROR);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
			// Se der erro em algum processo interno no banco de dados
			if ("erroDB".equals(serverCommunicationTask.getValue())) {
				Alerts.showAlert("Erro", null,
						"Informe o desenvolvedor:\nAlgo deu errado com nosso Banco de Dados.\n\nVoc?? pode contornar o problema adicionando as reservas manualmente.\nPara saber mais clique em Ajuda",
						AlertType.ERROR);
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
		});

		serverCommunicationTask.setOnFailed((e) -> {
			// Se n??o conseguirmos nos conectar com o servidor pelo Socket
			serverCommunicationTask.getException().printStackTrace();
			Alerts.showAlert("Demora na resposta do servidor", null,
					"O servidor est?? demorando para responder. Pode ser que ele esteja trabalhando com lentid??o. Aguarde poucos segundos e pressione o bot??o RESERVAS para verificar se a lista ser?? atualizada.\n\nCaso contr??rio, tente utilizar a op????o: ATUALIZAR MANUALMENTE.\n\nContate o desenvolvedor para tentar resolver o problema.",
					AlertType.WARNING);
			logger.error(serverCommunicationTask.getException());
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

	// Bot??o de cancelamento para fechar o painel, pois ele n??o tem barra superior
	@FXML
	private void onCancelButtonAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
}