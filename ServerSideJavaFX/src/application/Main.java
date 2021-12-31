package application;

import java.io.IOException;

import gui.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	// Vamos expor uma referencia para a cena principal (tela principal)
	// Assim vamos poder fazer um método para passar ela para o MainViewController
	// Passando ela poderemos carregar novas views dentro da tela principal
	private static Scene mainScene;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();

			// Ajustando o ScrollPane para ficar ajustado a janela por meio de código
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);

			mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Servidor da Mesa de Trabalho de Reservas");

			// Pegando o controller da MainView para poder chamar o método que inicializa o
			// trabalho do servidor
			//MainViewController controller = loader.getController();
			// iniciando o loop do servidor
			//controller.initializeServer();

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
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
