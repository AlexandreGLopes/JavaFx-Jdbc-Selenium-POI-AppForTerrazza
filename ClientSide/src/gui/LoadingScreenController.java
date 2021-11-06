package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LoadingScreenController implements Initializable {

	// Socket que vai ser utilizado nos vários métodos para conversar com o servidor
	private Socket cliente;

	@FXML
	private VBox rootVBox;

	public Socket getCliente() {
		return cliente;
	}

	public void setCliente(Socket cliente) {
		this.cliente = cliente;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		rootVBox.setOpacity(0);
		makeFadeInTransition();
	}

	private void makeFadeInTransition() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.seconds(1));
		fadeTransition.setNode(rootVBox);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.play();
		if (this.cliente != null) {
			try {
				InputStreamReader in = new InputStreamReader(cliente.getInputStream());
				BufferedReader bf = new BufferedReader(in);
				String option = bf.readLine();
				switch (option) {
				case "a":
					makeFadeOut();
					break;
				default:
					System.out.println("no valid option");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void makeFadeOut() {
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setDuration(Duration.seconds(1));
		fadeTransition.setNode(rootVBox);
		fadeTransition.setFromValue(1);
		fadeTransition.setToValue(0);

		fadeTransition.play();
	}

}
