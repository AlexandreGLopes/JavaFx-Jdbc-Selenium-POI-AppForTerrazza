package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import gui.util.PreferencesManager;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PreferencesPaneController implements Initializable {

	// Objeto que referencia as preferências porque este controlador será o
	// encarregado de gerenciar as preferências
	Preferences preferences = PreferencesManager.getPreferences();

	@FXML
	private TextField textFieldIPServidor;
	
	@FXML
	private TextField textFieldSeparadorCsv;
	
	@FXML
	private TextField textFieldEsperaServidor;
	
	@FXML
	private TextField textFieldIPofAPI;
	
	@FXML
	private TextField textFieldSessionName;
	
	@FXML
	private TextField textFieldSessionKey;

	@FXML
	private Button buttonSalvar;

	@FXML
	private Button buttonCancelar;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}
	
	// Método que coloca as preferências nas caixinhas de texto do painel
	public void getIPInPreferences() {
		// Colocando a preferência de IP do serverSide numa variável String
		// O primeiro parâmetro faz referência à constante key do IP
		// O segundo paraâmetro é o retorno caso a constante esteja vazia
		String ipOfServer = preferences.get(PreferencesManager.IP_TO_SERVERSIDE_MACHINE, null);
		// Jogando a variável no TextField
		textFieldIPServidor.setText(ipOfServer);
		// Colocando a preferência de separador do CSV
		String separadorCsv = preferences.get(PreferencesManager.SEPARADOR_DO_CSV, null);
		// Jogando a variável no TextField
		textFieldSeparadorCsv.setText(separadorCsv);
		// Colocando a preferência de tempo de espera pelo servidor
		String esperaServidor = preferences.get(PreferencesManager.ESPERA_POR_TAREFA_DO_SERVIDOR, null);
		// Jogando a variável no TextField
		textFieldEsperaServidor.setText(esperaServidor);
		// Colocando a preferência de IP da API de whatsapp
		String ipOfAPI = preferences.get(PreferencesManager.IP_TO_WHATSAPP_API, null);
		// Jogando a variável no TextField
		textFieldIPofAPI.setText(ipOfAPI);
		// Colocando a preferência de Nome da Sessão
		String sessionName = preferences.get(PreferencesManager.SESSION_NAME, null);
		// Jogando a variável no TextField
		textFieldSessionName.setText(sessionName);
		// Colocando a preferência de Key da Sessão
		String sessionKey = preferences.get(PreferencesManager.SESSION_KEY, null);
		// Jogando a variável no TextField
		textFieldSessionKey.setText(sessionKey);
	}

	// Método que salva o conteúdo das TextFields nas preferências
	public void onButtonSalvarAction(ActionEvent event) {
		// Colocando o IP nas preferências conforme a string dentro do TextField
		preferences.put(PreferencesManager.IP_TO_SERVERSIDE_MACHINE, textFieldIPServidor.getText());
		// Colocando o separador nas preferências conforme a string dentro do TextField
		preferences.put(PreferencesManager.SEPARADOR_DO_CSV, textFieldSeparadorCsv.getText());
		// Colocando o tempo de espera nas preferências conforme a string dentro do TextField
		preferences.put(PreferencesManager.ESPERA_POR_TAREFA_DO_SERVIDOR, textFieldEsperaServidor.getText());
		// Colocando o IP e a porta da API nas preferências conforme a string dentro do TextField
		preferences.put(PreferencesManager.IP_TO_WHATSAPP_API, textFieldIPofAPI.getText());
		// Colocando o nome da sessão nas preferências conforme a string dentro do TextField
		preferences.put(PreferencesManager.SESSION_NAME, textFieldSessionName.getText());
		// Colocando a chave da sessão nas preferências conforme a string dentro do TextField
		preferences.put(PreferencesManager.SESSION_KEY, textFieldSessionKey.getText());
		Utils.currentStage(event).close();
	}

	public void onButtonCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
}
