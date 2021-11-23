package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DataChangeListener;
import gui.util.Utils;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class ManualUpdaterScreenController implements Initializable {

	private String option;

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
	private GridPane rootPane;

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
	}

	private void makeFadeInTransition() {
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), loadingGif);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);
		fadeTransition.play();
	}

	@FXML
	private void onRefreshButtonAction(ActionEvent event) {
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
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
