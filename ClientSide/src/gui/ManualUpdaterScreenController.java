package gui;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.OwnFileHandler;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import model.entities.Costumer;
import model.services.CostumerService;

public class ManualUpdaterScreenController implements Initializable {

	CostumerService service = new CostumerService();

	private File selectedFile = null;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private Button filePickerButton;

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

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
	}

	// Método para clicar no botão de escolher arquivo para selecionar o arquivo
	// manualmente
	@FXML
	private void onFilePickerButtonAction(ActionEvent event) {
		// Instanciando o Filechooser
		FileChooser fc = new FileChooser();
		// Definindo a pasta padrão para iniciar a caixa de diálogo como a pasta de
		// Downloads do usuário
		fc.setInitialDirectory(new File(OwnFileHandler.definePath()));
		// Adicionando a restrição para apenas arquivos excel e csv
		fc.getExtensionFilters().addAll(new ExtensionFilter("Excel and CSV Files", "*.csv", "*.xls", "*.xlsx"));
		// Mostrando a caixa de dialogo para escolher o arquivo
		selectedFile = fc.showOpenDialog(null);

		if (selectedFile != null) {
			// Mostrando o nome do arquivo no Label do painel no momento da seleção
			txtLabel.setText(selectedFile.getName());
		} else {
			Alerts.showAlert("Alerta", null, "Nenhum arquivo selecionado", AlertType.WARNING);
		}
	}
	
	//Método que configura o FadeTransition da imagem de carregamento do painel
	private void makeFadeInTransition() {
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), loadingGif);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);
		fadeTransition.play();
	}
	
	// Método que trata do clique do botão de iniciar o carregamento do arquivo
	@FXML
	private void onRefreshButtonAction(ActionEvent event) {

		filePickerButton.setVisible(false);
		filePickerButton.setDisable(false);
		refreshButton.setVisible(false);
		refreshButton.setDisable(true);
		cancelButton.setVisible(false);
		cancelButton.setDisable(true);
		txtLabel.setVisible(false);
		txtCarregando.setVisible(true);
		makeFadeInTransition();

		// Construindo uma Task que vai rodar em outro Thread para deixar a UI livre
		// para mostrar os FadeTransitions
		Task<String> test = new Task<>() {
			@Override
			protected String call() throws Exception {
				// String que receberá o valor a ser retornado para verificar se é para fechar o
				// painel
				String retorno = null;

				if (selectedFile != null) {
					// Lista que vai receber as reservas/clientes
					List<Costumer> list = null;
					// String com o começo do nome do arquivo até o nono caractere porque achei a
					// melhor forma de decidir se o arquivo é do waitlist ou do wix
					String str = selectedFile.getName().substring(0, 9);
					switch (str) {
					// Caso aquivo do waitlist
					case "Relatorio":
						// String para pegar os três caracteres finais do arquivo
						String fileExtension = selectedFile.getName().substring(selectedFile.getName().length() - 3);
						// Testando se for csv ou xls e chamando o método específico
						switch (fileExtension) {
						case "csv":
							try {
								// Colocando costumers na lista com o método que le o arquivo csv
								list = OwnFileHandler.waitlistCsvReaderInstantiator(selectedFile.getAbsolutePath());
							} catch (NumberFormatException | ParseException e) {
								Alerts.showAlert("Erro", null, e.getMessage(), AlertType.ERROR);
								e.printStackTrace();
							}
							break;
						case "xls":
							try {
								// Colocando costumers na lista com o método que le o arquivo xls
								list = OwnFileHandler.waitlistXlsReaderInstantiator(selectedFile.getAbsolutePath());
							} catch (NumberFormatException e) {
								return "erroxls";
							} catch (Exception e) {
								return "erroxls";
							}
							break;
						default:
							// TODO
							break;
						}
						// Método que insere a lista no banco de dados. Foi feito em método separado
						// para economizar linhas de código e deixar mais fácil de ler
						// Os parâmetros list e event seguem aqui mas estão com sua utilização comentada
						// no método, pois ainda preciso testar se não vamos mesmo precisar mais deles
						takeListInsertInDBAndClose(list, event);
						retorno = "close";
						break;

					// Caso seja arquivo do Wix
					case "reservati":
						try {
							// Colocando costumers na lista com o método que le o arquivo xlsx
							list = OwnFileHandler.wixReaderInstantiator(selectedFile.getAbsolutePath());
						} catch (NumberFormatException e) {
							Alerts.showAlert("Erro", null, e.getMessage(), AlertType.ERROR);
							e.printStackTrace();
						}
						// Método que insere a lista no banco de dados. Foi feito em método separado
						// para economizar linhas de código e deixar mais fácil de ler
						// Os parâmetros list e event seguem aqui mas estão com sua utilização comentada
						// no método, pois ainda preciso testar se não vamos mesmo precisar mais deles
						takeListInsertInDBAndClose(list, event);
						retorno = "close";
						break;
					default:
						// TODO
						break;
					}
				}
				return retorno;
			}
		};

		// Ao final testando se a string tem o valor close para notificar os listener e
		// fechar o painel e também tratar erros e mostrar alerts
		test.setOnSucceeded((e) -> {
			if ("close".equals(test.getValue())) {
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
			}
			if ("erroxls".equals(test.getValue())) {
				notifyDataChangeListeners();
				Utils.currentStage(event).close();
				Alerts.showAlert("Problema no arquivo", "O arquivo pode estar corrompido",
						"Você tentou usar o arquivo Excel do Waitlist, mas ele  geralmente vem corrompido."
								+ "\n\nRecomendamos baixar e utilizar o arquivo CSV.\nOu abrir o arquivo corrompido no Excel e ir em 'Salvar como' e salvar o arquivo novamente como xls e depois tentar carregá-lo aqui novamente"
								+ "\n\nCódigo do erro: Erro Xls",
						AlertType.ERROR);
			}
		});

		// Iniciando subthread para fazer a Task
		Thread thread = new Thread(test);
		thread.setDaemon(true);
		thread.start();
	}

	// Método que insere a lista no banco de dados, chama onotifychangelisteners e
	// depois fecha o painel
	private void takeListInsertInDBAndClose(List<Costumer> list, ActionEvent event) {
		for (Costumer obj : list) {
			try {
				service.insertIfExternalIdNotExists(obj);
			} catch (Exception e) {
				Alerts.showAlert("Erro", null, e.getMessage(), AlertType.ERROR);
			}
		}
		// Fechamento e notifyDataChangelisteners passados para o final do
		// task.setonsucceded. Deixei aqui para se der algum problema lembrar de voltar
		// com eles aqui mesmo
		// notifyDataChangeListeners();
		// Utils.currentStage(event).close();
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	// Botão de cancelamento para fechar o painel, pois ele não tem barra superior
	@FXML
	private void onCancelButtonAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
}
