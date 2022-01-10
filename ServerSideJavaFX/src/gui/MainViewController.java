package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import application.Main;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Costumer;
import model.services.CostumerService;
import util.OwnFileHandler;
import util.SeleniumUtils;

public class MainViewController implements Initializable {

	private Logger logger = LogManager.getLogger(MainViewController.class);
	
	@FXML
	private VBox rootVBox;

	@FXML
	private Label statusLabel;

	@FXML
	private Button iniciarButton;

	@FXML
	private GridPane gridPane;

	@FXML
	private MenuItem menuItemAbout;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	public void initializeNodes() {
		Stage stage = (Stage) Main.getMainScene().getWindow();
		gridPane.prefHeightProperty().bind(stage.heightProperty());
		gridPane.prefWidthProperty().bind(stage.widthProperty());
	}

	public void onIniciarButtonAction() {
		statusLabel.setText("Servidor Iniciado");
		iniciarButton.setDisable(true);
		initializeServer();
	}

	public void initializeServer() {
		// Construindo uma Task que vai rodar em outro Thread para deixar a UI livre
		// para mostrar os FadeTransitions
		Task<String> task = new Task<>() {
			@Override
			protected String call() throws Exception {

				CostumerService service = new CostumerService();

				// Instanciando o socket para receber a conexão
				ServerSocket server = new ServerSocket(3322);
				System.out.println("Servidor iniciado na porta 3322");

				// Abrindo um navegador ao iniciar o servidor. Usaremos este para abrir o Wix
				// Pois o wix tem Recaptcha e vamos deixar aberto para não cair na verificação
				// do robô
				WebDriver browser2 = SeleniumUtils.openToWIx();
				// Se precisar saber qual é o identificador da janela do browser acima
				// descomentar abaixo
				// System.out.println(browser2.getWindowHandle());

				// Iniciando um loop para aceitar uma nova conexão toda vez que a conexão
				// anterior for fechada
				int continuar = 1;
				do {
					Socket cliente = server.accept();
					System.out.println("Cliente conectado do IP " + cliente.getInetAddress().getHostAddress());

					// Instâncias que recebem o stream do cliente e vão convertê-lo em string para
					// usar no switch/case
					InputStreamReader in = new InputStreamReader(cliente.getInputStream());
					BufferedReader bf = new BufferedReader(in);
					String option = bf.readLine();

					// Printwriter que será utilzado dentro do switch/case
					// PrintWriter pr;
					// Chamando o método estático da classe que faz os downloads via Selenium
					// Usando switch case para escolher qual método usar
					// Lista que vai receber as reservas/clientes
					List<Costumer> list = null;
					switch (option) {
					case "a":
						// verificando se ficou algum arquivo antigo baixado para trás e deletando se
						// existir
						OwnFileHandler.verifyAndDeleteFile("a");
						// Fazendo o download do arquivo do Terrazza 40
						SeleniumUtils.DownloadFromWaitlist(cliente);
						// Lista que vai receber as reservas/clientes
						// List<Costumer> list = null;
						// Chamando o método que lê e intancia uma lista de objetos do tipo Costumer
						// para depois serem colocados no banco de dados
						try {
							list = OwnFileHandler.waitlistReaderInstantiator("a");
						} catch (NumberFormatException e) {
							logger.error(e.getMessage());
							e.printStackTrace();
							sendToClient(cliente, "erroServer");
						} catch (ParseException e) {
							logger.error(e.getMessage());
							e.printStackTrace();
							sendToClient(cliente, "erroServer");
						}
						for (Costumer obj : list) {
							try {
								service.insertIfExternalIdNotExists(obj);
							} catch (Exception e) {
								logger.error(e.getMessage());
								e.printStackTrace();
								sendToClient(cliente, "erroDB");
							}
						}
						// verificando e excluindo o arquivo de download
						OwnFileHandler.verifyAndDeleteFile("a");
						// Comunicando com o cliente para mostrar que as funções aqui finalizaram
						sendToClient(cliente, "close");
						break;
					case "b":
						// verificando se ficou algum arquivo antigo baixado para trás e deletando se
						// existir
						OwnFileHandler.verifyAndDeleteFile("b");
						// Fazendo o download do arquivo do 38 Floor
						SeleniumUtils.useWix(browser2, cliente);
						// Lista que vai receber as reservas/clientes
						// List<Costumer> list = null;
						// Chamando o método que lê e intancia uma lista de objetos do tipo Costumer
						// para depois serem colocados no banco de dados
						try {
							list = OwnFileHandler.wixReaderInstantiator("b");
						} catch (NumberFormatException e) {
							logger.error(e.getMessage());
							e.printStackTrace();
							sendToClient(cliente, "erroServer");
						}
						for (Costumer obj : list) {
							try {
								service.insertIfExternalIdNotExists(obj);
							} catch (Exception e) {
								logger.error(e.getMessage());
								e.printStackTrace();
								sendToClient(cliente, "erroDB");
							}
						}
						// verificando e excluindo o arquivo de download
						OwnFileHandler.verifyAndDeleteFile("b");
						// Comunicando com o cliente para mostrar que as funções aqui finalizaram
						sendToClient(cliente, "close");
						break;
					default:
						sendToClient(cliente, "erroServer");
					}

					// Fechando conexão
					cliente.close();
				} while (continuar == 1);
				return null;
			}
		};

		task.setOnSucceeded(null);

		task.setOnFailed((e) -> {
			task.getException().printStackTrace();
			logger.error(task.getException());
		});

		// Iniciando subthread para fazer a Task
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

// Método que vai enviar a mensagem do servidor para o cliente para que o
// programa não fique parado e que o usuário saiba se aconteceu algum erro ou se
// deu tudo certo
	public static void sendToClient(Socket cliente, String message) throws IOException {
		PrintWriter pr = new PrintWriter(cliente.getOutputStream());
		pr.println(message);
		pr.flush();
	}

	public void onMenuItemAboutAction() {
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/About.fxml", currentStage, false, (x) -> {
		});
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
			// Não poderá ser redimensionada
			dialogStage.setResizable(false);
			// verificando se a tela terá que terá a regra de não tem a barra de título
			if (staticScreen) {
				// Retirando a barra de título do painel de loading
				dialogStage.initStyle(StageStyle.UNDECORATED);
			}
			dialogStage.showAndWait();

		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
