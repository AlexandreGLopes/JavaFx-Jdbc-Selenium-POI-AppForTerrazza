package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alerts;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
	public void onMenuItemRefreshFromWaitlistAction(ActionEvent event) {
		String option = "a";
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		createLoadingPane(option, "/gui/LoadingScreen.fxml", currentStage);
	}

	@FXML
	public void onMenuItemRefreshFromWixAction() {
		String option = "b";
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		createLoadingPane(option, "/gui/LoadingScreen.fxml", currentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	private void createLoadingPane(String option, String absoluteName, Stage parentStage) {
		try {
			// Carregar o fxml
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// pane vai receber o que carregou do loader
			Pane pane = loader.load();
			// Vamos injetar o departamento client no controlador da tela de loading
			// Pegando referencia para o controlador
			LoadingScreenController controller = loader.getController();
			controller.setOption(option);
			if (option == "a") {
				controller.setLabel("Atualizar do Waitlist\n(Terrazza 40)");
			}
			if (option == "b") {
				controller.setLabel("Atualizar do Wix\n(38 Floor)");
			}
			

			// Instanciar um novo stage (um palco na frente do outro)
			Stage dialogStage = new Stage();
			//Retirando a barra de título do painel de loading
			dialogStage.initStyle(StageStyle.UNDECORATED);
			// Além do stage precisamos de uma Scene
			dialogStage.setScene(new Scene(pane));
			// Não poderá ser redimensionada
			dialogStage.setResizable(false);
			// Passando o Stage "pai" dessa janela, que passamos como segundo parâmetro
			// neste método
			dialogStage.initOwner(parentStage);
			// Ela será modal, enquanto você não fechar ela não poderá acessar a janela
			// anterior
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
