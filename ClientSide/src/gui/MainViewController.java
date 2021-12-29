package gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.CostumerXStandardMessageService;
import model.services.StandardMessageService;
import model.services.WaitingCostumerService;

public class MainViewController implements Initializable, DataChangeListener {

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
			// Setando a tabela para deixar as colunas passarem além dos limites horizontais
			// da própria tabela
			controller.getTableViewCostumer().setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
			controller.updateTableView();
		});
	}

	@FXML
	public void onTabelaEsperaButtonAction() throws ParseException {
		loadView("/gui/WaitingListPane.fxml", (WaitingListPaneController controller) -> {
			controller.setWaitingCostumerService(new WaitingCostumerService());
			// Setando a tabela para deixar as colunas passarem além dos limites horizontais
			// da própria tabela
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
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/LoadingScreen.fxml", currentStage, true, (LoadingScreenController controller) -> {
			// Setando qual foi a opção escolhida pelo usuário direto no próximo controlador
			// para que ele envie a opção para o servidor. Lá o servidor vai entrar em
			// switch/case para escolher os métodos de download usados
			controller.setOption(option);
			controller.subscribeDataChangeListener(this);
			// Mudando o Label do próximo painel de acordo com a opção escolhida pelo
			// usuário. Aprendi que não podemos chamar essas mudanças no initialize porque
			// sempre vai dar null
			if (option == "a") {
				controller.setLabel("Atualizar do Waitlist\n(Terrazza 40)");
			}
		});
	}

	@FXML
	public void onMenuItemRefreshFromWixAction() {
		String option = "b";
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/LoadingScreen.fxml", currentStage, true, (LoadingScreenController controller) -> {
			// Setando qual foi a opção escolhida pelo usuário direto no próximo controlador
			// para que ele envie a opção para o servidor. Lá o servidor vai entrar em
			// switch/case para escolher os métodos de download usados
			controller.setOption(option);
			controller.subscribeDataChangeListener(this);
			// Mudando o Label do próximo painel de acordo com a opção escolhida pelo
			// usuário. Aprendi que não podemos chamar essas mudanças no initialize porque
			// sempre vai dar null
			if (option == "b") {
				controller.setLabel("Atualizar do Wix\n(38 Floor)");
			}
		});
	}

	@FXML
	public void onMenuItemManualRefreshAction() {
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/ManualUpdaterScreen.fxml", currentStage, true, (ManualUpdaterScreenController controller) -> {
			// Setando qual foi a opção escolhida pelo usuário direto no próximo controlador
			// para que ele envie a opção para o servidor. Lá o servidor vai entrar em
			// switch/case para escolher os métodos de download usados
			controller.subscribeDataChangeListener(this);
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/About.fxml", currentStage, true, (x) -> {
		});
	}

	@FXML
	public void onMenuItemManualRefreshHelpAction() {
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/ManualUpdaterHelp.fxml", currentStage, true, (x) -> {
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

	// Método que carrega novos painéis
	private <T> void loadPane(String absoluteName, Stage parentStage, boolean staticScreen,
			Consumer<T> initializingAction) {
		try {
			// Carregar o fxml
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// pane vai receber o que carregou do loader
			Pane pane = loader.load();
			// Ativando a função passada no parâmetro 4, tipo Consumer<T>
			// Todas as funções passadas no paraâmetro Consumer são funções para o
			// Controller
			// Essas funções são passadas em cada um dos métodos específicos que chamam
			// telas específicas, porque cada uma delas vai ter uma classe específica de
			// Controller
			// Abaixo inicializando o controller
			T controller = loader.getController();
			initializingAction.accept(controller);

			// Instanciar um novo stage (um palco na frente do outro)
			Stage dialogStage = new Stage();
			// Além do stage precisamos de uma Scene
			dialogStage.setScene(new Scene(pane));
			// Passando o Stage "pai" dessa janela, que passamos como segundo parâmetro
			// neste método
			dialogStage.initOwner(parentStage);
			// Ela será modal, enquanto você não fechar ela não poderá acessar a janela
			// anterior
			dialogStage.initModality(Modality.WINDOW_MODAL);
			// verificando se a tela terá que ter regras para não aumentar e nao ter barra
			// de título
			if (staticScreen) {
				// Retirando a barra de título do painel de loading
				dialogStage.initStyle(StageStyle.UNDECORATED);
				// Não poderá ser redimensionada
				dialogStage.setResizable(false);
			}
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("Erro ao carregar tela", null, "Erro ao carregar: " + absoluteName, AlertType.ERROR);
			e.printStackTrace();
		}
	}

	// Função para abrir outra tela (Private porque vamos chamá-la aqui mesmo)
	// Adicionamos "synchronized" para só mostrar o resultado após carregar tudo.
	// Assim o processamento não será interrompido durante o multithreading
	// Parametro 1 é o caminho do fxml
	// Parametro 2 define a função como genérica para receber um tipo qualquer
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// carregar a view
			VBox newVBox = loader.load();

			// Mostrar a view dentro da janela principal
			Scene mainScene = Main.getMainScene();
			// vamos pegar os "filhos" da VBox About e carregá-los nos filhos da VBox da
			// MainView (ver a hierarquia das tags de fxml para entender)
			// Pegando o primeiro elemento da minha view (fazendo um casting de ScrollPane
			// para o compilador entender que é issoq ue eu quero)
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

			// Ativando a função passada no parâmetro 2, tipo Consumer<T>
			T controller = loader.getController();
			initializingAction.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
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
