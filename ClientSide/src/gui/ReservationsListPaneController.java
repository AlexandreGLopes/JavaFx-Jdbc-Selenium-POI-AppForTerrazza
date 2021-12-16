package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.controlsfx.control.CheckComboBox;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.control.TextField;
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

public class ReservationsListPaneController implements Initializable, DataChangeListener {

	private CostumerService service;

	@FXML
	private CheckComboBox<String> filtrosSituacaoCheckComboBox;

	@FXML
	private CheckComboBox<String> filtrosSalaoCheckComboBox;

	@FXML
	private TextField filtroNomeSobrenomeTextField;

	// Iniciando as referências para a TableView

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
	private TableColumn<Costumer, Date> tableColumnData;

	@FXML
	private TableColumn<Costumer, Date> tableColumnHora;

	@FXML
	private TableColumn<Costumer, String> tableColumnMesa;

	@FXML
	private TableColumn<Costumer, String> tableColumnSituacao;
	
	@FXML
	private TableColumn<Costumer, Costumer> tableColumnObservacoes;

	@FXML
	private TableColumn<Costumer, Double> tableColumnPagamento;

	@FXML
	private TableColumn<Costumer, String> tableColumnIdExterno;

	public TableView<Costumer> getTableViewCostumer() {
		return tableViewCostumer;
	}

	// Final das referências do TableView

	private ObservableList<Costumer> obsList;

	public void setCostumerService(CostumerService service) {
		this.service = service;
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
		// Setando as opções do checkComboBox que vai ser o filtro pras situações das
		// reservas
		// retirado de : https://www.youtube.com/watch?v=fjkkzkk7rNc&t=420s
		final ObservableList<String> optionsSituacao = FXCollections.observableArrayList();
		optionsSituacao.add("Novo");
		optionsSituacao.add("Confirmado");
		optionsSituacao.add("Sentado");
		optionsSituacao.add("Cancelado pelo cliente");
		optionsSituacao.add("Cancelado por solicitação do cliente");
		optionsSituacao.add("Cancelado por no-show");
		optionsSituacao.add("Cancelado por erro");
		filtrosSituacaoCheckComboBox.getItems().addAll(optionsSituacao);
		// Setando as opções do checkComboBox que vai ser o filtro pros salões
		final ObservableList<String> optionsSalao = FXCollections.observableArrayList();
		optionsSalao.add("Almoço Terrazza 40");
		optionsSalao.add("Café da Tarde na Confeitaria");
		optionsSalao.add("Pôr do Sol na Confeitaria");
		optionsSalao.add("Jantar no Terrazza 40");
		optionsSalao.add("38 Floor");
		filtrosSalaoCheckComboBox.getItems().addAll(optionsSalao);
	}

	private void initializeNodes() {

		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnSalao.setCellValueFactory(new PropertyValueFactory<>("salao"));
		tableColumnPessoas.setCellValueFactory(new PropertyValueFactory<>("pessoas"));
		tableColumnHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
		Utils.formatTableColumnDate(tableColumnHora, "HH:mm");
		tableColumnMesa.setCellValueFactory(new PropertyValueFactory<>("mesa"));
		tableColumnSituacao.setCellValueFactory(new PropertyValueFactory<>("situacao"));
		tableColumnPagamento.setCellValueFactory(new PropertyValueFactory<>("pagamento"));
		Utils.formatTableColumnDouble(tableColumnPagamento, 2);

		// Arrumando largura e altura da tabela
		// Window é uma superclasse do stage então teremos que fazer um downcasting pro
		// stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCostumer.prefHeightProperty().bind(stage.heightProperty());

		// Método para customizar a tabela
		customiseFactory();

	}

	// Atualizando os nomes na lista só com os clientes com reservas feitas para a
	// data de hoje
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		// Pegando o o resultado da query do MySQL e colocando numa lista principal
		List<Costumer> masterList = service.findAllofCurrentDate();
		// A partir daqui é uma mescla de coisas retiradas de:
		// 1) https://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
		// e
		// 2)http://javadox.com/org.controlsfx/controlsfx/8.40.10/org/controlsfx/control/CheckComboBox.html#method.summary
		// e
		// 3) do updateTableView do curso de Java da Udemy
		// Por fim, mudei tudo e arrumei usando bindings que encontrei aqui:
		// 4)https://stackoverflow.com/questions/52232150/filtering-tableview-with-controlsfx-checkcombobox
		// Fazendo a observableList padrão desta classe receber a lista principal
		obsList = FXCollections.observableArrayList(masterList);

		// Criando predicados para cada checkComboBox e para o TextField
		ObjectProperty<Predicate<Costumer>> statusFilter = new SimpleObjectProperty<>();
		ObjectProperty<Predicate<Costumer>> saloonFilter = new SimpleObjectProperty<>();
		ObjectProperty<Predicate<Costumer>> nameSurnameFilter = new SimpleObjectProperty<>();

		// Fazendo o bind (enlaçando) cada um dos predicados aos valores das
		// checkComboBox e do TextField
		statusFilter.bind(Bindings.createObjectBinding(
				() -> costumer -> filtrosSituacaoCheckComboBox.getCheckModel().getCheckedItems().isEmpty()
						|| filtrosSituacaoCheckComboBox.getCheckModel().getCheckedItems()
								.contains(costumer.getSituacao()),
				filtrosSituacaoCheckComboBox.getCheckModel().getCheckedItems()));

		saloonFilter.bind(Bindings.createObjectBinding(
				() -> costumer -> filtrosSalaoCheckComboBox.getCheckModel().getCheckedItems().isEmpty()
						|| filtrosSalaoCheckComboBox.getCheckModel().getCheckedItems().contains(costumer.getSalao()),
				filtrosSalaoCheckComboBox.getCheckModel().getCheckedItems()));

		nameSurnameFilter.bind(Bindings.createObjectBinding(() -> costumer -> costumer.getNome().toLowerCase()
				.contains(filtroNomeSobrenomeTextField.getText().toLowerCase())
				|| costumer.getSobrenome().toLowerCase().contains(filtroNomeSobrenomeTextField.getText().toLowerCase()),
				filtroNomeSobrenomeTextField.textProperty()));

		// Passando a observableList para uma lista que será filtrada
		FilteredList<Costumer> filteredItems = new FilteredList<>(obsList);
		// Adicionando os dados filtrado à TableView
		tableViewCostumer.setItems(filteredItems);
		// Combinando os predicados usando Predicate.and(...) e fazendo o bind
		// (enlaçando)
		// a propriedade dos predicados da lista filtrada aos resultados
		filteredItems.predicateProperty()
				.bind(Bindings.createObjectBinding(
						() -> statusFilter.get().and(saloonFilter.get().and(nameSurnameFilter.get())), statusFilter,
						saloonFilter, nameSurnameFilter));

		// iniciando os botões nas linhas dos clientes
		initColumnWhatsButtons();
		initColumnObsButtons();
		// Utils.autoResizeColumns(tableViewCostumer);
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

	// Método que coloca os botões do whatsapp nas linhas da tableview
	private void initColumnWhatsButtons() {
		Image img = new Image(new File("res/whatsIcon.png").toURI().toString());
		tableColumnWhats.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnWhats.setCellFactory(param -> new TableCell<Costumer, Costumer>() {
			ImageView view = new ImageView(img);
			private final Button button = new Button(null, view);

			@Override
			protected void updateItem(Costumer obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				// Adicionei o código para saber se o nome do costumer está nulo porque vai ter
				// as linhas com clientes nulos para dar um espaço entre os nomes de clientes
				// duplicados
				if (obj.getNome() != null) {
					setGraphic(button);
					button.setPrefWidth(50);
					button.setPrefHeight(50);
					view.setFitHeight(20);
					view.setFitWidth(20);
					button.setOnAction(
							event -> createMessageForm(obj, "/gui/MessageForm.fxml", Utils.currentStage(event)));
				}
			}
		});
	}
	
	// Método que coloca os botões de Observações nas linhas da tableview
	private void initColumnObsButtons() {
		tableColumnObservacoes.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnObservacoes.setCellFactory(param -> new TableCell<Costumer, Costumer>() {
			private final Button button = new Button("*");

			@Override
			protected void updateItem(Costumer obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				// Adicionei o código para saber se o nome do costumer está nulo porque vai ter
				// as linhas com clientes nulos para dar um espaço entre os nomes de clientes
				// duplicados
					setGraphic(button);
					button.setPrefWidth(50);
					button.setPrefHeight(50);
					button.setOnAction(
							event -> createMessageForm(obj, "/gui/MessageForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// Método que customiza as células da tabela de dinamicamente, de acordo com os
	// valores
	private void customiseFactory() {
		// Cores
		Color corCancelado = Color.RED;
		Color corNovoConfirmado = Color.valueOf("#6e8003");
		// Talvez precise formatar hora, mas por enquanto não estou conseguindo usar.
		// deixando para depois
		//SimpleDateFormat hr = new SimpleDateFormat("HH:mm");

		// Customização de linhas inteiras
		tableViewCostumer.setRowFactory(row -> new TableRow<Costumer>() {
			@Override
			public void updateItem(Costumer item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null) {
					setStyle("");
				}
				/*
				 * else if (item.getSituacao().contains("Cancelado")) {
				 * setStyle("-fx-background-color: tomato;"); } else if
				 * (item.getSituacao().contains("Sentado")) {
				 * setStyle("-fx-background-color: #8ea604;"); }
				 */
				else {
					setStyle("");
					setPrefHeight(100);
				}
			}
		});

		// A partir daqui:
		// Customização das colunas
		tableColumnNome.setCellFactory(new Callback<TableColumn<Costumer, String>, TableCell<Costumer, String>>() {
			@Override
			public TableCell<Costumer, String> call(TableColumn<Costumer, String> param) {
				return new TableCell<Costumer, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						if (!empty) {
							int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
							String columnNome = param.getTableView().getItems().get(currentIndex).getNome();
							String columnSobrenome = param.getTableView().getItems().get(currentIndex).getSobrenome();

							setStyle("-fx-alignment:  CENTER-LEFT; -fx-font-weight: bold; -fx-font-size: 14;");
							setText(columnNome + " " + columnSobrenome);
							setPrefWidth(250);
							setWidth(250);
							setWrapText(true);
						}
						else {
							setText(null);
						}
					}
				};
			}
		});
		/*
		 * tableColumnHora.setCellFactory(new Callback<TableColumn<Costumer, Date>,
		 * TableCell<Costumer, Date>>() {
		 * 
		 * @Override public TableCell<Costumer, Date> call(TableColumn<Costumer, Date>
		 * param) { return new TableCell<Costumer, Date>() {
		 * 
		 * @Override protected void updateItem(Date item, boolean empty) { if (!empty) {
		 * int currentIndex = indexProperty().getValue() < 0 ? 0 :
		 * indexProperty().getValue(); Date columnData =
		 * param.getTableView().getItems().get(currentIndex).getData(); String
		 * columnSituacao =
		 * param.getTableView().getItems().get(currentIndex).getSituacao();
		 * 
		 * setText(hr.format(columnData)); setStyle("-fx-alignment: CENTER;"); if
		 * (columnSituacao.contains("Cancelado")) { this.setTextFill(Color.RED); } else
		 * { this.setTextFill(Color.BLACK); } } } }; } });
		 */
		tableColumnPessoas.setCellFactory(new Callback<TableColumn<Costumer, Integer>, TableCell<Costumer, Integer>>() {
			@Override
			public TableCell<Costumer, Integer> call(TableColumn<Costumer, Integer> param) {
				return new TableCell<Costumer, Integer>() {
					@Override
					protected void updateItem(Integer item, boolean empty) {
						if (!empty) {
							int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
							Integer columnPessoas = param.getTableView().getItems().get(currentIndex).getPessoas();
							String columnSituacao = param.getTableView().getItems().get(currentIndex).getSituacao();

							setText(columnPessoas.toString());
							setStyle("-fx-alignment: CENTER; -fx-font-size: 16; -fx-font-weight: bold;");
							if (columnSituacao.contains("Cancelado")) {
								this.setTextFill(corCancelado);
							} else {
								this.setTextFill(corNovoConfirmado);
							}
						}
						else {
							setText(null);
						}
					}
				};
			}
		});

		tableColumnSituacao.setCellFactory(new Callback<TableColumn<Costumer, String>, TableCell<Costumer, String>>() {
			@Override
			public TableCell<Costumer, String> call(TableColumn<Costumer, String> param) {
				return new TableCell<Costumer, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						if (!empty) {
							int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
							String columnSituacao = param.getTableView().getItems().get(currentIndex).getSituacao();

							setStyle("-fx-alignment: CENTER; -fx-text-alignment: CENTER; -fx-font-weight: bold;");
							setWrapText(true);
							setPrefWidth(100);
							setWidth(100);
							setText(columnSituacao);
							if (columnSituacao.contains("Cancelado")) {
								this.setTextFill(corCancelado);
							} else {
								this.setTextFill(corNovoConfirmado);
							}
						}
						else {
							setText(null);
						}
					}
				};
			}
		});

		tableColumnSalao.setCellFactory(new Callback<TableColumn<Costumer, String>, TableCell<Costumer, String>>() {
			@Override
			public TableCell<Costumer, String> call(TableColumn<Costumer, String> param) {
				return new TableCell<Costumer, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						if (!empty) {
							int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
							String columnSituacao = param.getTableView().getItems().get(currentIndex).getSituacao();
							String columnSalao = param.getTableView().getItems().get(currentIndex).getSalao();

							setStyle("-fx-alignment: CENTER; -fx-text-alignment: CENTER; -fx-font-weight: bold;");
							setWrapText(true);
							setPrefWidth(120);
							setWidth(120);
							setText(columnSalao);
							if (columnSituacao.contains("Cancelado")) {
								this.setTextFill(corCancelado);
							} else {
								this.setTextFill(corNovoConfirmado);
							}
						}
						else {
							setText(null);
						}
					}
				};
			}
		});
		
		tableColumnMesa.setCellFactory(new Callback<TableColumn<Costumer, String>, TableCell<Costumer, String>>() {
			@Override
			public TableCell<Costumer, String> call(TableColumn<Costumer, String> param) {
				return new TableCell<Costumer, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						if (!empty) {
							int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
							String columnMesa = param.getTableView().getItems().get(currentIndex).getMesa();

							setStyle("-fx-alignment: CENTER; -fx-text-alignment: CENTER;");
							setWrapText(true);
							setPrefWidth(120);
							setWidth(120);
							setText(columnMesa);
						}
						else {
							setText(null);
						}
					}
				};
			}
		});
		
		tableColumnPagamento.setCellFactory(new Callback<TableColumn<Costumer, Double>, TableCell<Costumer, Double>>() {
			@Override
			public TableCell<Costumer, Double> call(TableColumn<Costumer, Double> param) {
				return new TableCell<Costumer, Double>() {
					@Override
					protected void updateItem(Double item, boolean empty) {
						if (!empty) {
							int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
							Double columnPagamento = param.getTableView().getItems().get(currentIndex).getPagamento();

							setStyle("-fx-alignment: CENTER; -fx-text-alignment: CENTER;");
							if (columnPagamento == 0.00) {
								this.setText("");
							}
							else {
								this.setText(columnPagamento.toString());
							}
						}
						else {
							setText(null);
						}
					}
				};
			}
		});
	}
}
