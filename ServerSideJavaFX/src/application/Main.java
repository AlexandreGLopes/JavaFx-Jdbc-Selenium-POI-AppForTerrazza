package application;

import java.io.IOException;

import gui.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
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
			VBox vbox = loader.load();

			mainScene = new Scene(vbox);
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Servidor da Mesa de Trabalho de Reservas");

			// Pegando o controller da MainView para poder chamar o método que faz o bind
			// das propriedades de height e width do gridPane
			MainViewController controller = loader.getController();
			controller.initializeNodes();

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
