package gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.services.CostumerService;
import model.services.StandardMessageService;
import model.services.WaitingCostumerService;

public class MainViewController implements Initializable, DataChangeListener {

	private Logger logger = LogManager.getLogger(MainViewController.class);

	@FXML
	private VBox rootVBox;

	@FXML
	private MenuItem menuItemRefreshFromWaitlist;

	@FXML
	private MenuItem menuItemRefreshFromWix;

	@FXML
	private MenuItem menuItemManualRefresh;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	private MenuItem menuItemManualRefreshHelp;

	@FXML
	private MenuItem menuItemMensagensPadrao;

	@FXML
	private MenuItem menuItemConectarWhatsapp;

	@FXML
	private MenuItem menuItemPreferences;

	@FXML
	private Button tabelaPrincipalButton;

	@FXML
	private Button tabelaEsperaButton;

	@FXML
	private Button clientesDuplicadosPorNomeButton;

	@FXML
	private Button clientesDuplicadosPorTelefoneButton;

	@FXML
	private Button clientesDuplicadosPorEmailButton;

	@FXML
	public void onTabelaPrincipalButtonAction() throws ParseException {
		loadView("/gui/ReservationsListPane.fxml", (ReservationsListPaneController controller) -> {
			controller.setCostumerService(new CostumerService());
			// Setando a tabela para deixar as colunas passarem al??m dos limites horizontais
			// da pr??pria tabela
			controller.getTableViewCostumer().setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
			controller.updateTableView();
		});
	}

	@FXML
	public void onTabelaEsperaButtonAction() throws ParseException {
		loadView("/gui/WaitingListPane.fxml", (WaitingListPaneController controller) -> {
			controller.setWaitingCostumerService(new WaitingCostumerService());
			// Setando a tabela para deixar as colunas passarem al??m dos limites horizontais
			// da pr??pria tabela
			controller.getTableViewWaitingCostumer().setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
			controller.updateTableView();
		});
	}

	@FXML
	public void onClientesDuplicadosPorNomeButtonAction() throws ParseException {
		loadView("/gui/DuplicatedListPane.fxml", (DuplicatedListPaneController controller) -> {
			controller.setCostumerService(new CostumerService());
			controller.updateTableView("name");
		});
	}

	@FXML
	public void onClientesDuplicadosPorTelefoneButtonAction() throws ParseException {
		loadView("/gui/DuplicatedListPane.fxml", (DuplicatedListPaneController controller) -> {
			controller.setCostumerService(new CostumerService());
			controller.updateTableView("telefone");
		});
	}

	@FXML
	public void onClientesDuplicadosPorEmailButtonAction() throws ParseException {
		loadView("/gui/DuplicatedListPane.fxml", (DuplicatedListPaneController controller) -> {
			controller.setCostumerService(new CostumerService());
			controller.updateTableView("email");
		});
	}

	@FXML
	public void onMenuItemRefreshFromWaitlistAction(ActionEvent event) {
		String option = "a";
		// par??metro dentro de um objeto Stage que receber?? o m??todo currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/LoadingScreen.fxml", currentStage, true, null, (LoadingScreenController controller) -> {
			// Setando qual foi a op????o escolhida pelo usu??rio direto no pr??ximo controlador
			// para que ele envie a op????o para o servidor. L?? o servidor vai entrar em
			// switch/case para escolher os m??todos de download usados
			controller.setOption(option);
			controller.subscribeDataChangeListener(this);
			// Mudando o Label do pr??ximo painel de acordo com a op????o escolhida pelo
			// usu??rio. Aprendi que n??o podemos chamar essas mudan??as no initialize porque
			// sempre vai dar null
			if (option == "a") {
				controller.setLabel("Atualizar do Waitlist\n(Terrazza 40)");
			}
		});
	}

	@FXML
	public void onMenuItemRefreshFromWixAction() {
		String option = "b";
		// par??metro dentro de um objeto Stage que receber?? o m??todo currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/LoadingScreen.fxml", currentStage, true, null, (LoadingScreenController controller) -> {
			// Setando qual foi a op????o escolhida pelo usu??rio direto no pr??ximo controlador
			// para que ele envie a op????o para o servidor. L?? o servidor vai entrar em
			// switch/case para escolher os m??todos de download usados
			controller.setOption(option);
			controller.subscribeDataChangeListener(this);
			// Mudando o Label do pr??ximo painel de acordo com a op????o escolhida pelo
			// usu??rio. Aprendi que n??o podemos chamar essas mudan??as no initialize porque
			// sempre vai dar null
			if (option == "b") {
				controller.setLabel("Atualizar do Wix\n(38 Floor)");
			}
		});
	}

	@FXML
	public void onMenuItemManualRefreshAction() {
		// par??metro dentro de um objeto Stage que receber?? o m??todo currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/ManualUpdaterScreen.fxml", currentStage, true, null, (ManualUpdaterScreenController controller) -> {
			// Setando qual foi a op????o escolhida pelo usu??rio direto no pr??ximo controlador
			// para que ele envie a op????o para o servidor. L?? o servidor vai entrar em
			// switch/case para escolher os m??todos de download usados
			controller.subscribeDataChangeListener(this);
		});
	}

	public void onMenuItemMensagensPadraoAction() {
		// par??metro dentro de um objeto Stage que receber?? o m??todo currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/MensagensPadraoListPane.fxml", currentStage, false, null, (MensagensPadraoListPaneController controller) -> {
			controller.setStandardMessageService(new StandardMessageService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemConectarWhatsapp() {
		// par??metro dentro de um objeto Stage que receber?? o m??todo currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/ConectarWhatsappQrCodePane.fxml", currentStage, false, "Conectar via QR Code", (ConectarWhatsappQrCodePaneController controller) -> {
		});
	}

	public void onMenuItemPreferencesAction() {
		// par??metro dentro de um objeto Stage que receber?? o m??todo currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/PreferencesPane.fxml", currentStage, false, null, (PreferencesPaneController controller) -> {
			controller.getIPInPreferences();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		// par??metro dentro de um objeto Stage que receber?? o m??todo currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/About.fxml", currentStage, false, null, (x) -> {
		});
	}

	@FXML
	public void onMenuItemManualRefreshHelpAction() {
		// par??metro dentro de um objeto Stage que receber?? o m??todo currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/ManualUpdaterHelp.fxml", currentStage, true, null, (ManualUpdaterHelpController controller) -> {
			controller.setGifs();
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	// M??todo que carrega novos pain??is
	private <T> void loadPane(String absoluteName, Stage parentStage, boolean staticScreen, String title,
			Consumer<T> initializingAction) {
		try {
			// Carregar o fxml
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// pane vai receber o que carregou do loader
			Pane pane = loader.load();
			// Ativando a fun????o passada no par??metro 4, tipo Consumer<T>
			// Todas as fun????es passadas no para??metro Consumer s??o fun????es para o
			// Controller
			// Essas fun????es s??o passadas em cada um dos m??todos espec??ficos que chamam
			// telas espec??ficas, porque cada uma delas vai ter uma classe espec??fica de
			// Controller
			// Abaixo inicializando o controller
			T controller = loader.getController();
			initializingAction.accept(controller);

			// Instanciar um novo stage (um palco na frente do outro)
			Stage dialogStage = new Stage();
			// Al??m do stage precisamos de uma Scene
			dialogStage.setScene(new Scene(pane));
			// Passando o Stage "pai" dessa janela, que passamos como segundo par??metro
			// neste m??todo
			dialogStage.initOwner(parentStage);
			// Ela ser?? modal, enquanto voc?? n??o fechar ela n??o poder?? acessar a janela
			// anterior
			dialogStage.initModality(Modality.WINDOW_MODAL);
			// N??o poder?? ser redimensionada
			dialogStage.setResizable(false);
			//setando o t??tulo do painel
			if (title != null) {
				dialogStage.setTitle(title);
			}
			// verificando se a tela ter?? que ter?? a regra de n??o tem a barra de t??tulo
			if (staticScreen) {
				// Retirando a barra de t??tulo do painel de loading
				dialogStage.initStyle(StageStyle.UNDECORATED);
			}
			dialogStage.showAndWait();

		} catch (IOException e) {
			logger.error(e.getMessage());
			Alerts.showAlert("Erro ao carregar tela", null, "Erro ao carregar: " + absoluteName, AlertType.ERROR);
			e.printStackTrace();
		}
	}

	// Fun????o para abrir outra tela (Private porque vamos cham??-la aqui mesmo)
	// Adicionamos "synchronized" para s?? mostrar o resultado ap??s carregar tudo.
	// Assim o processamento n??o ser?? interrompido durante o multithreading
	// Parametro 1 ?? o caminho do fxml
	// Parametro 2 define a fun????o como gen??rica para receber um tipo qualquer
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// carregar a view
			VBox newVBox = loader.load();

			// Mostrar a view dentro da janela principal
			Scene mainScene = Main.getMainScene();
			// vamos pegar os "filhos" da VBox About e carreg??-los nos filhos da VBox da
			// MainView (ver a hierarquia das tags de fxml para entender)
			// Pegando o primeiro elemento da minha view (fazendo um casting de ScrollPane
			// para o compilador entender que ?? issoq ue eu quero)
			// getContent() Pegando o filho de ScrollPane
			// Finalizando com um casting para Vbox antes de tudo para que o compilador
			// entenda que eu quero o Vbox
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			// A partir daqui teremos que preservar o MenuBar, excluir tudo o que tiver nos
			// filhos do VBox, incluir o MenuBar e depois os filhos do VBox de About
			// Guardar referencia para o menu
			Node mainMenu = mainVBox.getChildren().get(0);
			Node mainToolBar = mainVBox.getChildren().get(1);
			// excluindo da tela os filhos de mainVbox
			mainVBox.getChildren().clear();
			// Adicionar o mainMenu e depis os filhos do newVBox (About)
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().add(mainToolBar);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			// Ativando a fun????o passada no par??metro 2, tipo Consumer<T>
			T controller = loader.getController();
			initializingAction.accept(controller);
		} catch (IOException e) {
			logger.error(e.getMessage());
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	@Override
	public void onDataChanged() {
		// Toda vez que a gente atualizar os dados do banco vamos voltar para a tabela
		// principal, mesmo que o usuario esteja em outra tabela
		loadView("/gui/ReservationsListPane.fxml", (ReservationsListPaneController controller) -> {
			controller.setCostumerService(new CostumerService());
			controller.updateTableView();
		});
	}
}
