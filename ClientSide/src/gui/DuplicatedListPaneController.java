package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.CheckDuplicacatesMethods;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.Costumer;
import model.services.CostumerService;

public class DuplicatedListPaneController implements Initializable {

	SimpleDateFormat hr = new SimpleDateFormat("HH:mm");

	private CostumerService service;

	@FXML
	private TableView<Costumer> tableViewCostumer;

	@FXML
	private TableColumn<Costumer, Costumer> tableColumnWhats;

	@FXML
	private TableColumn<Costumer, Integer> tableColumnId;

	@FXML
	private TableColumn<Costumer, String> tableColumnNome;

	@FXML
	private TableColumn<Costumer, String> tableColumnSobrenome;

	@FXML
	private TableColumn<Costumer, String> tableColumnTelefone;

	@FXML
	private TableColumn<Costumer, String> tableColumnEmail;

	@FXML
	private TableColumn<Costumer, String> tableColumnSalao;

	@FXML
	private TableColumn<Costumer, Integer> tableColumnPessoas;

	// Date aqui é java.util.date
	@FXML
	private TableColumn<Costumer, Date> tableColumnHora;

	@FXML
	private TableColumn<Costumer, String> tableColumnMesa;

	@FXML
	private TableColumn<Costumer, String> tableColumnSituacao;

	@FXML
	private TableColumn<Costumer, String> tableColumnIdExterno;

	public TableView<Costumer> getTableViewCostumer() {
		return tableViewCostumer;
	}

	private ObservableList<Costumer> obsList;

	public void setCostumerService(CostumerService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnSalao.setCellValueFactory(new PropertyValueFactory<>("salao"));
		tableColumnPessoas.setCellValueFactory(new PropertyValueFactory<>("pessoas"));
		tableColumnHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
		Utils.formatTableColumnDate(tableColumnHora, "HH:mm");
		tableColumnMesa.setCellValueFactory(new PropertyValueFactory<>("mesa"));
		tableColumnSituacao.setCellValueFactory(new PropertyValueFactory<>("situacao"));
		tableColumnIdExterno.setCellValueFactory(new PropertyValueFactory<>("idExterno"));

		// Arrumando largura e altura da tabela
		// Window é uma superclasse do stage então teremos que fazer um downcasting pro
		// stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCostumer.prefHeightProperty().bind(stage.heightProperty());

		// Customização das células e colunas
		customizeFactory();

	}

	private void customizeFactory() {

		// Customização de linhas inteiras
		tableViewCostumer.setRowFactory(row -> new TableRow<Costumer>() {
			@Override
			public void updateItem(Costumer item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null) {
					setStyle("");
				} else {
					setStyle("");
					if (isSelected()) {
						setStyle("-fx-background-color: #7bc0e8; ");
					}
				}
			}
		});

		// Customização das colunas
		tableColumnHora.setCellFactory(new Callback<TableColumn<Costumer, Date>, TableCell<Costumer, Date>>() {
			@Override
			public TableCell<Costumer, Date> call(TableColumn<Costumer, Date> param) {
				return new TableCell<Costumer, Date>() {
					@Override
					protected void updateItem(Date item, boolean empty) {
						if (!empty) {
							int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
							String columnNome = param.getTableView().getItems().get(currentIndex).getNome();
							Date columnHora = param.getTableView().getItems().get(currentIndex).getHora();
							String data = hr.format(columnHora);

							if (columnNome != null) {
								setStyle("-fx-alignment: CENTER-LEFT;");
								setText(data);
							} else if (columnNome == null) {
								setText(null);
							}
						} else {
							setText(null);
						}
					}
				};
			}
		});

	}

	// O updatetableview aqui vai ter um switch/case para que possamos usar os três
	// tipos de comparações: por nome, por telefone e por email. cada uma vai usar
	// um método para pegar os dados do Banco já filtrados para ficar melhor de trabalhar
	public void updateTableView(String option) {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Costumer> duplicatedList = new ArrayList<>();

		switch (option) {
		case "name":
			duplicatedList = CheckDuplicacatesMethods.checkDuplicatesByName(service);
			break;

		case "telefone":
			duplicatedList = CheckDuplicacatesMethods.checkDuplicatesByTelephone(service);
			break;

		case "email":
			duplicatedList = CheckDuplicacatesMethods.checkDuplicatesByEmail(service);
			break;

		default:
			break;
		}

		obsList = FXCollections.observableArrayList(duplicatedList);
		tableViewCostumer.setItems(obsList);
		initColumnButtons();
		Utils.autoResizeColumns(tableViewCostumer);
	}

	// Método que coloca os botões do whatsapp nas linhas da tableview
	private void initColumnButtons() {
		Image img = new Image(new File("res/whatsIcon.png").toURI().toString());
		tableColumnWhats.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnWhats.setCellFactory(param -> new TableCell<Costumer, Costumer>() {
			ImageView view = new ImageView(img);
			private final Button button = new Button(null, view);

			@Override
			protected void updateItem(Costumer obj, boolean empty) {
				super.updateItem(obj, empty);

				// Ao rolar para baixo apareciam mais bitões. aí adicionei mais uma checagem:
				// "obj.getNome() == null" porque não é só o objeto que tem que estar nulo. Isso
				// parece ter resolvido
				if (obj == null || obj.getNome() == null) {
					setGraphic(null);
					return;
				}

				// Adicionei o código para saber se o nome do costumer está nulo porque vai ter
				// as linhas com clientes nulos para dar um espaço entre os nomes de clientes
				// duplicados
				if (obj.getNome() != null) {
					setGraphic(button);
					button.setPrefWidth(30);
					view.setFitHeight(18);
					view.setFitWidth(18);
					button.setOnAction(
							event -> createMessageForm(obj, "/gui/MessageForm.fxml", Utils.currentStage(event)));
				}
			}
		});
	}

	// Método que cria o formulário das mensagens de whatsapp inividuais
	private void createMessageForm(Costumer obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			MessageFormController controller = loader.getController();
			controller.setCostumer(obj);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Mensagem para Whatsapp");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Erro carregando o painel", e.getMessage(), AlertType.ERROR);
		}
	}

}
