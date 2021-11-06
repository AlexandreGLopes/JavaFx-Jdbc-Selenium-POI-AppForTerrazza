package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alerts;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainViewController implements Initializable {

	// Socket que vai ser utilizado nos vários métodos para conversar com o servidor
	private Socket cliente;

	// PrintWriter que vai ser utilizado pelos vários métodos e vai passar o
	// argumento para o switch case
	private PrintWriter pr;

	@FXML
	private MenuItem menuItemRefreshFromWaitlist;

	@FXML
	private MenuItem menuItemRefreshFromWix;

	@FXML
	private VBox rootVBox;

	@FXML
	public void onMenuItemRefreshFromWaitlistAction() {
		try {
			makeFadeOut();
			cliente = new Socket("localhost", 3322);
			System.out.println(cliente.getPort());
			pr = new PrintWriter(cliente.getOutputStream());
			pr.println("a");
			pr.flush();

		} catch (IOException ex) {
			Alerts.showAlert("Erro", null, "Não foi possível conectar ao servidor:\n" + ex.getMessage(),
					AlertType.ERROR);
		}
	}

	@FXML
	public void onMenuItemRefreshFromWixAction() {
		try {
			makeFadeOut();
			cliente = new Socket("localhost", 3322);
			pr = new PrintWriter(cliente.getOutputStream());
			pr.println("b");
			pr.flush();

		} catch (IOException ex) {
			Alerts.showAlert("Erro", null, "Não foi possível conectar ao servidor:\n" + ex.getMessage(),
					AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	private void makeFadeOut() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.seconds(1));
		fadeTransition.setNode(rootVBox);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);

		fadeTransition.setOnFinished(event -> loadLoadingScene());
		fadeTransition.play();
	}

	private synchronized void loadLoadingScene() {
		try {
			/*
			 * Parent loadingScene; loadingScene = (VBox)
			 * FXMLLoader.load(getClass().getResource("/gui/LoadingScreen.fxml"));
			 * 
			 * Scene newScene = new Scene(loadingScene);
			 * 
			 * Stage currentStage = (Stage) rootVBox.getScene().getWindow();
			 * 
			 * currentStage.setScene(newScene);
			 */

			// Frankenstein que costurei misturando varias formas que vi para poder pegar o
			// controller e passar o socket client e injetá-lo no controllador da loading
			// screen
			FXMLLoader loadingScene = new FXMLLoader(getClass().getResource("/gui/LoadingScreen.fxml"));
			VBox newVBox = loadingScene.load();
			Scene newScene = new Scene(newVBox);
			Stage currentStage = (Stage) rootVBox.getScene().getWindow();
			currentStage.setScene(newScene);
			System.out.println(cliente.getPort());
			LoadingScreenController controller = (LoadingScreenController)loadingScene.getController();
			controller.setCliente(cliente);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
