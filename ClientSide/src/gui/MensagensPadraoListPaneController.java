package gui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.listeners.DataChangeListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.entities.StandardMessage;
import model.services.StandardMessageService;

public class MensagensPadraoListPaneController implements Initializable, DataChangeListener {
	
	private Logger logger = LogManager.getLogger(MensagensPadraoListPaneController.class);
	
	private StandardMessageService standardMessageService;
	
	private ObservableList<StandardMessage> obsList;
	
	@FXML
	private AnchorPane rootAnchorPane;
	
	@FXML
	private VBox rootVBox;
	
	@FXML
	private Button buttonNovaMensagem;
	
	@FXML
	private TableView<StandardMessage> tableViewStandardMessages;
	
	@FXML
	private TableColumn<StandardMessage, Integer> tableColumnId;
	
	@FXML
	private TableColumn<StandardMessage, String> tableColumnTitulo;
	
	@FXML
	private TableColumn<StandardMessage, String> tableColumnMensagem;
	
	@FXML
	private TableColumn<StandardMessage, StandardMessage> tableColumnEdit;
	
	public void setStandardMessageService (StandardMessageService standardMessageService) {
		this.standardMessageService = standardMessageService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		initColumnEditButtons();
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
		tableColumnMensagem.setCellValueFactory(new PropertyValueFactory<>("mensagem"));
		
		//Stage stage = (Stage) rootVBox.getScene().getWindow();
		rootVBox.prefHeightProperty().bind(rootAnchorPane.heightProperty());
		rootVBox.prefWidthProperty().bind(rootAnchorPane.widthProperty());
		tableViewStandardMessages.prefHeightProperty().bind(rootVBox.heightProperty());
		tableViewStandardMessages.prefWidthProperty().bind(rootVBox.widthProperty());
	}

	public void onButtonNovaMensagemAction() {
		
	}
	
	public void updateTableView() {
		if (standardMessageService == null) {
			logger.error("Service was null");
			throw new IllegalStateException("Service was null");
		}
		List<StandardMessage> list = standardMessageService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewStandardMessages.setItems(obsList);
		
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	// Método que coloca os botões de Observações nas linhas da tableview
		private void initColumnEditButtons() {
			Image img = new Image(new File("res/pencil_24x24.png").toURI().toString());
			tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnEdit.setCellFactory(param -> new TableCell<StandardMessage, StandardMessage>() {
				ImageView view = new ImageView(img);
				private final Button button = new Button(null, view);

				@Override
				protected void updateItem(StandardMessage obj, boolean empty) {
					super.updateItem(obj, empty);

					if (obj == null) {
						setGraphic(null);
						return;
					}

					setGraphic(button);
					button.setPrefWidth(20);
					button.setPrefHeight(20);
					view.setFitHeight(20);
					view.setFitWidth(20);
					button.setOnAction(event -> {
							});
				}
			});
		}

}
