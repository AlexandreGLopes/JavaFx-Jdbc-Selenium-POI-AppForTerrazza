package gui;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.entities.Costumer;

public class WaitingListPaneController implements Initializable {
	
	@FXML
	private Button novaEsperaButton;
	
	@FXML
	private CheckComboBox<String> filtroSituacaoCheckComboBox;
	
	@FXML
	private TextField filtroPorNomeTextField;
	
	@FXML
	private TableView<Costumer> tableViewWaitingCostumer;

	@FXML
	private TableColumn<Costumer, Costumer> tableColumnWhats;
	
	@FXML
	private TableColumn<Costumer, Costumer> tableColumnEdit;
	
	@FXML
	private TableColumn<Costumer, String> tableColumnNome;

	@FXML
	private TableColumn<Costumer, String> tableColumnSobrenome;
	
	// Date aqui é java.util.date
	@FXML
	private TableColumn<Costumer, Date> tableColumnHora;
	
	@FXML
	private TableColumn<Costumer, String> tableColumnSituacao;
	
	@FXML
	private TableColumn<Costumer, String> tableColumnObservacoes;

	@Override
	public void initialize(URL url, ResourceBundle rs) {
	}

}
