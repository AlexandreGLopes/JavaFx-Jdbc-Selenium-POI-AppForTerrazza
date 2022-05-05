package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
import model.entities.WaitingCostumer;
import model.services.StandardMessageService;
import model.services.WaitingCostumerService;

public class WaitingListPaneController implements Initializable, DataChangeListener {
	
	private Logger logger = LogManager.getLogger(WaitingListPaneController.class);

	private WaitingCostumerService service;

	@FXML
	private Button novaEsperaButton;

	@FXML
	private CheckComboBox<String> filtroSituacaoCheckComboBox;

	@FXML
	private TextField filtroPorNomeTextField;

	@FXML
	private DatePicker dpDataListaEspera;

	@FXML
	private TableView<WaitingCostumer> tableViewWaitingCostumer;

	@FXML
	private TableColumn<WaitingCostumer, WaitingCostumer> tableColumnWhats;

	@FXML
	private TableColumn<WaitingCostumer, WaitingCostumer> tableColumnEdit;

	@FXML
	private TableColumn<WaitingCostumer, String> tableColumnNome;

	@FXML
	private TableColumn<WaitingCostumer, Integer> tableColumnPessoas;

	// Date aqui é java.util.date
	@FXML
	private TableColumn<WaitingCostumer, Date> tableColumnHora;

	@FXML
	private TableColumn<WaitingCostumer, String> tableColumnSituacao;
	
	@FXML
	private TableColumn<WaitingCostumer, String> tableColumnMesa;

	@FXML
	private TableColumn<WaitingCostumer, String> tableColumnObservacoes;

	private ObservableList<WaitingCostumer> obsList;

	public void setWaitingCostumerService(WaitingCostumerService service) {
		this.service = service;
	}

	public TableView<WaitingCostumer> getTableViewWaitingCostumer() {
		return tableViewWaitingCostumer;
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
		initializeNodes();
		// Setando as opções do checkComboBox que vai ser o filtro pras situações das
		// reservas
		// retirado de : https://www.youtube.com/watch?v=fjkkzkk7rNc&t=420s
		final ObservableList<String> optionsSituacao = FXCollections.observableArrayList();
		optionsSituacao.add("Novo");
		optionsSituacao.add("Sentado");
		optionsSituacao.add("Cancelado pelo cliente");
		optionsSituacao.add("Cancelado por no-show");
		filtroSituacaoCheckComboBox.getItems().addAll(optionsSituacao);
	}

	private void initializeNodes() {
		// Formatando o DatePicker e passando a data de atual para ele. Pois, toda vez
		// que a gente for entrar na lista de espera queremos vera data de atual
		// automaticamente
		Utils.formatDatePicker(dpDataListaEspera, "dd/MM/yyyy");
		LocalDate today = LocalDate.now();
		dpDataListaEspera.setValue(today);
		// Inicializando colunas da tabela
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnPessoas.setCellValueFactory(new PropertyValueFactory<>("pessoas"));
		tableColumnHora.setCellValueFactory(new PropertyValueFactory<>("horaChegada"));
		Utils.formatTableColumnDate(tableColumnHora, "HH:mm");
		tableColumnSituacao.setCellValueFactory(new PropertyValueFactory<>("situacao"));
		tableColumnMesa.setCellValueFactory(new PropertyValueFactory<>("mesa"));
		tableColumnObservacoes.setCellValueFactory(new PropertyValueFactory<>("observacao"));
		// Arrumando largura e altura da tabela
		// Window é uma superclasse do stage então teremos que fazer um downcasting pro
		// stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewWaitingCostumer.prefHeightProperty().bind(stage.heightProperty());
		// Método para customizar a tabela
		customiseFactory();
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	public void updateTableView() {
		if (service == null) {
			logger.error("Service was null");
			throw new IllegalStateException("Service was null");
		}
		// Convertendo o LocalDate do Datepicker para Date para poder passar como
		// parâmetro no service
		Date datePickerConveted = Date
				.from(dpDataListaEspera.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		// Pegando o o resultado da query do MySQL e colocando numa lista principal
		List<WaitingCostumer> masterList = service.findAllofDatePickerDate(datePickerConveted);
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
		ObjectProperty<Predicate<WaitingCostumer>> statusFilter = new SimpleObjectProperty<>();
		ObjectProperty<Predicate<WaitingCostumer>> nameSurnameFilter = new SimpleObjectProperty<>();
		// Fazendo o bind (enlaçando) cada um dos predicados aos valores das
		// checkComboBox e do TextField
		statusFilter.bind(Bindings.createObjectBinding(
				() -> costumer -> filtroSituacaoCheckComboBox.getCheckModel().getCheckedItems().isEmpty()
						|| filtroSituacaoCheckComboBox.getCheckModel().getCheckedItems()
								.contains(costumer.getSituacao()),
				filtroSituacaoCheckComboBox.getCheckModel().getCheckedItems()));

		nameSurnameFilter.bind(Bindings.createObjectBinding(() -> costumer -> costumer.getNome().toLowerCase()
				.contains(filtroPorNomeTextField.getText().toLowerCase())
				|| costumer.getSobrenome().toLowerCase().contains(filtroPorNomeTextField.getText().toLowerCase()),
				filtroPorNomeTextField.textProperty()));

		// Passando a observableList para uma lista que será filtrada
		FilteredList<WaitingCostumer> filteredItems = new FilteredList<>(obsList);
		// Adicionando os dados filtrado à TableView
		tableViewWaitingCostumer.setItems(filteredItems);
		// Combinando os predicados usando Predicate.and(...) e fazendo o bind
		// (enlaçando)
		// a propriedade dos predicados da lista filtrada aos resultados
		filteredItems.predicateProperty().bind(Bindings.createObjectBinding(
				() -> statusFilter.get().and(nameSurnameFilter.get()), statusFilter, nameSurnameFilter));

		// iniciando os botões nas linhas dos clientes
		initColumnWhatsButtons();
		initColumnEditButtons();

	}

	public void onButtonNovaEsperaAction(ActionEvent event) {
		// Instanciando um WaitingCostumer vazio para passar para o Formulário
		// pratica comum no padrão MVC. Fazendo isso para usar o mesmo formulario para
		// editar e criar uma nova espera. Vamos passar uma data de hoje para ele para
		// que quando o seja reserva nova sempre abra o formulário na data atual para
		// poupar tempo para o usuário. E também porque a data não pode ser nula
		WaitingCostumer obj = new WaitingCostumer();
		// Passando a data atual do sistema para a variavel hoje
		LocalDate today = LocalDate.now();
		// Convertendo a variável hoje para outro tipo de objeto de data do java.utils
		// Iniciando ela no horário do inicio do dia e adicionando 19 horas para que
		// seja mostrado o horário de 19:00 por padrão quando clicarmos em uma nova espera
		Date data = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant().plus(Duration.ofHours(19)));
		obj.setData(data);
		obj.setHoraChegada(data);
		// Setando para duas pessoas o padrão da nova espera
		obj.setPessoas(2);
		// Setando os status para novo sempre que for uma nova espera 
		obj.setSituacao("Novo");
		//  Setando o Salão para o Jantar por padrão
		obj.setSalao("Jantar no Terrazza 40");
		// Criando formulário
		createForm("Editar Espera", obj, "/gui/EditWaitingForm.fxml", Utils.currentStage(event),
				(EditWaitingFormController controller) -> {
					controller.setWaitingCostumer(obj);
					controller.setService(new WaitingCostumerService(), new StandardMessageService());
					controller.updateFormData();
				});
	}

	public void onDatePickerChangeValue() {
		updateTableView();
	}

	// Método que cria o formulário das mensagens de whatsapp inividuais
	private <T> void createForm(String title, Costumer obj, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			// Abaixo inicializando o controller
			T controller = loader.getController();
			initializingAction.accept(controller);
			// Adicionando listener numa clausula if/else e não chamando no
			// InitializingAction na expressão lambda como os outros métodos do controller
			// porque lá o "this" estava se referindo à célula e não à classe. ENtão tive
			// que adicionar aqui para resolver o problema
			if (title.equals("Editar Espera")) {
				((EditWaitingFormController) controller).subscribeDataChangeListener(this);
			}
			// MessageFormController controller = loader.getController();
			// controller.setCostumer(obj);
			// controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(title);
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			logger.error(e.getMessage());
			Alerts.showAlert("IOException", "Erro carregando o painel", e.getMessage(), AlertType.ERROR);
		}
	}

	// Método que coloca os botões do whatsapp nas linhas da tableview
	private void initColumnWhatsButtons() {
		Image img = new Image(new File("res/whatsIcon.png").toURI().toString());
		tableColumnWhats.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnWhats.setCellFactory(param -> new TableCell<WaitingCostumer, WaitingCostumer>() {
			ImageView view = new ImageView(img);
			private final Button button = new Button(null, view);

			@Override
			protected void updateItem(WaitingCostumer obj, boolean empty) {
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
					button.setOnAction(event -> createForm("Mensagem para Whatsapp", obj, "/gui/MessageForm.fxml",
							Utils.currentStage(event), (MessageFormController controller) -> {
								controller.setCostumer(obj);
								controller.updateFormData();
							}));
				}
			}
		});
	}

	// Método que coloca os botões de Observações nas linhas da tableview
	private void initColumnEditButtons() {
		Image img = new Image(new File("res/pencil_24x24.png").toURI().toString());
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<WaitingCostumer, WaitingCostumer>() {
			ImageView view = new ImageView(img);
			private final Button button = new Button(null, view);

			@Override
			protected void updateItem(WaitingCostumer obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setPrefWidth(50);
				button.setPrefHeight(50);
				view.setFitHeight(20);
				view.setFitWidth(20);
				button.setOnAction(event -> createForm("Editar Espera", obj, "/gui/EditWaitingForm.fxml",
						Utils.currentStage(event), (EditWaitingFormController controller) -> {
							controller.setWaitingCostumer(obj);
							controller.setService(new WaitingCostumerService(), new StandardMessageService());
							controller.updateFormData();
						}));
			}
		});
	}

	private void customiseFactory() {

		SimpleDateFormat hr = new SimpleDateFormat("HH:mm");

		// Cores
		Color corCancelado = Color.RED;
		Color corNovoConfirmado = Color.valueOf("#6e8003");
		Color corSentado = Color.DARKBLUE;

		// Imagem
		Image imgWaiting = new Image(new File("res/costumer_waiting_48x53.png").toURI().toString());

		// Retirando as linhas das bordas da tabela
		tableViewWaitingCostumer.setStyle("-fx-table-cell-border-color: transparent;");

		// Customização de linhas inteiras
		tableViewWaitingCostumer.setRowFactory(row -> new TableRow<WaitingCostumer>() {
			@Override
			public void updateItem(WaitingCostumer item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null) {
					setStyle("");
				} else {
					setStyle("");
					setPrefHeight(100);
					if (isSelected()) {
						setStyle("-fx-background-color: #7bc0e8; -fx-table-cell-border-color: transparent;");
					}
				}
			}
		});

		// A partir daqui:
		// Customização das colunas
		tableColumnNome.setCellFactory(
				new Callback<TableColumn<WaitingCostumer, String>, TableCell<WaitingCostumer, String>>() {
					@Override
					public TableCell<WaitingCostumer, String> call(TableColumn<WaitingCostumer, String> param) {
						return new TableCell<WaitingCostumer, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								if (!empty) {
									int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
									String columnNome = param.getTableView().getItems().get(currentIndex).getNome();
									String columnSobrenome = param.getTableView().getItems().get(currentIndex)
											.getSobrenome();

									setStyle("-fx-alignment:  CENTER-LEFT; -fx-font-weight: bold; -fx-font-size: 14;");
									setText(columnNome + " " + columnSobrenome);
									setPrefWidth(250);
									setWidth(250);
									setWrapText(true);
								} else {
									setText(null);
								}
							}
						};
					}
				});

		tableColumnPessoas.setCellFactory(
				new Callback<TableColumn<WaitingCostumer, Integer>, TableCell<WaitingCostumer, Integer>>() {
					@Override
					public TableCell<WaitingCostumer, Integer> call(TableColumn<WaitingCostumer, Integer> param) {
						return new TableCell<WaitingCostumer, Integer>() {
							@Override
							protected void updateItem(Integer item, boolean empty) {
								if (!empty) {
									int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
									Integer columnPessoas = param.getTableView().getItems().get(currentIndex)
											.getPessoas();
									String columnSituacao = param.getTableView().getItems().get(currentIndex)
											.getSituacao();

									setText(columnPessoas.toString());
									setStyle("-fx-alignment: CENTER; -fx-font-size: 16; -fx-font-weight: bold;");
									if (columnSituacao.contains("Cancelado")) {
										this.setTextFill(corCancelado);
									} else if (columnSituacao.contains("Sentado")) {
										this.setTextFill(corSentado);
									} else {
										this.setTextFill(corNovoConfirmado);
									}
								} else {
									setText(null);
								}
							}
						};
					}
				});

		tableColumnHora
				.setCellFactory(new Callback<TableColumn<WaitingCostumer, Date>, TableCell<WaitingCostumer, Date>>() {
					@Override
					public TableCell<WaitingCostumer, Date> call(TableColumn<WaitingCostumer, Date> param) {
						return new TableCell<WaitingCostumer, Date>() {
							@Override
							protected void updateItem(Date item, boolean empty) {
								if (!empty) {
									int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
									String columnHora = hr
											.format(param.getTableView().getItems().get(currentIndex).getHoraChegada());
									String columnSituacao = param.getTableView().getItems().get(currentIndex)
											.getSituacao();

									setText(columnHora);
									setStyle("-fx-alignment: CENTER; -fx-font-size: 16; -fx-font-weight: bold;");
									if (columnSituacao.contains("Cancelado")) {
										this.setTextFill(corCancelado);
									} else if (columnSituacao.contains("Sentado")) {
										this.setTextFill(corSentado);
									} else {
										this.setTextFill(corNovoConfirmado);
									}
								} else {
									setText(null);
								}
							}
						};
					}
				});

		tableColumnSituacao.setCellFactory(
				new Callback<TableColumn<WaitingCostumer, String>, TableCell<WaitingCostumer, String>>() {
					@Override
					public TableCell<WaitingCostumer, String> call(TableColumn<WaitingCostumer, String> param) {
						return new TableCell<WaitingCostumer, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								if (!empty) {
									ImageView view = new ImageView(imgWaiting);
									int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
									String columnSituacao = param.getTableView().getItems().get(currentIndex)
											.getSituacao();
									boolean statusAguardando = param.getTableView().getItems().get(currentIndex)
											.isAguardando();

									if (statusAguardando == true || statusAguardando) {
										view.setFitHeight(24);
										view.setFitWidth(28);
										setGraphic(view);
										setText(null);
										setStyle("-fx-alignment: CENTER; -fx-text-alignment: CENTER;");
									} else {
										setGraphic(null);
										setStyle(
												"-fx-alignment: CENTER; -fx-text-alignment: CENTER; -fx-font-weight: bold;");
										setWrapText(true);
										setPrefWidth(100);
										setWidth(100);
										setText(columnSituacao);
										if (columnSituacao.contains("Cancelado")) {
											this.setTextFill(corCancelado);
										} else if (columnSituacao.contains("Sentado")) {
											this.setTextFill(corSentado);
										} else {
											this.setTextFill(corNovoConfirmado);
										}
									}
								} else {
									setText(null);
								}
							}
						};
					}
				});

		tableColumnObservacoes.setCellFactory(
				new Callback<TableColumn<WaitingCostumer, String>, TableCell<WaitingCostumer, String>>() {
					@Override
					public TableCell<WaitingCostumer, String> call(TableColumn<WaitingCostumer, String> param) {
						return new TableCell<WaitingCostumer, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								if (!empty) {
									int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
									String columnObservacao = param.getTableView().getItems().get(currentIndex)
											.getObservacao();

									setStyle("-fx-alignment:  CENTER-LEFT; -fx-font-weight: bold; -fx-font-size: 12;");
									setText(columnObservacao);
									setPrefWidth(250);
									setWidth(250);
									setWrapText(true);
								} else {
									setText(null);
								}
							}
						};
					}
				});
	}

}
