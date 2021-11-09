package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	// Função para retornar o Stage atual onde o controller que recebeu o evento
	// está
	public static Stage currentStage(ActionEvent event) {
		// Dois downcastings feitos no mesmo argumento
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

}
