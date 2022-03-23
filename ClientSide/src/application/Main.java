package application;

import java.io.IOException;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.DbException;
import gui.MainViewController;
import gui.util.Alerts;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.services.CostumerService;

public class Main extends Application {

	// Vamos expor uma referencia para a cena principal (tela principal)
	// Assim vamos poder fazer um método para passar ela para o MainViewController
	// Passando ela poderemos carregar novas views dentro da tela principal
	private static Scene mainScene;
	
	private Logger logger = LogManager.getLogger(MainViewController.class);

	@Override
	public void start(Stage primaryStage) throws ParseException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			
			// Ajustando o ScrollPane para ficar ajustado a janela por meio de código
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);

			mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Mesa de Trabalho de Reservas");
			
			//Pegando o controller da MainView para poder chamar o método que inicializa o Painel separado da tabela
			MainViewController controller = loader.getController();
			//Atualizando os valores da TableView
			controller.onTabelaPrincipalButtonAction();
			
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DbException e) {
			logger.error(e.getMessage());
			Alerts.showAlert("Erro ao carregar Aplicativo",
					null,
					"Houve um erro ao acessar o banco de dados do aplicativo."
					+ "\n Verifique se o servidor está ligado e"
					+ "\nse a instância do banco de dados está iniciada."
					+ "\n\nMensagem de erro: " + e.getMessage(),
					AlertType.ERROR);
		}
	}

	// método que pega a tela principal colocada como atributo
	public static Scene getMainScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}