package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.prefs.Preferences;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.CheckComboBox;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.PreferencesManager;
import gui.util.Utils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
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
import model.entities.CostumerXStandardMessage;
import model.services.CostumerService;
import model.services.CostumerXStandardMessageService;
import model.services.NonExistentPhoneService;
import model.services.StandardMessageService;

public class ReservationsListPaneController implements Initializable, DataChangeListener {

	private Logger logger = LogManager.getLogger(ReservationsListPaneController.class);

	Preferences preferences = PreferencesManager.getPreferences();

	private CostumerService service;

	@FXML
	private CheckComboBox<String> filtrosSituacaoCheckComboBox;

	@FXML
	private CheckComboBox<String> filtrosSalaoCheckComboBox;

	@FXML
	private TextField filtroNomeSobrenomeTextField;

	@FXML
	private Button mandarConfirmacoesButton;

	// Iniciando as refer??ncias para a TableView

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

	// Date aqui ?? java.util.date
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
	private TableColumn<Costumer, Costumer> tableColumnConfirmacoes;

	@FXML
	private TableColumn<Costumer, Costumer> tableColumnEdit;

	@FXML
	private TableColumn<Costumer, String> tableColumnIdExterno;

	public TableView<Costumer> getTableViewCostumer() {
		return tableViewCostumer;
	}

	// Final das refer??ncias do TableView

	private ObservableList<Costumer> obsList;

	private List<Integer> idsComConfirmacoes = new ArrayList<Integer>();

	public void setCostumerService(CostumerService service) {
		this.service = service;
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		// Setando as op????es do checkComboBox que vai ser o filtro pras situa????es das
		// reservas
		// retirado de : https://www.youtube.com/watch?v=fjkkzkk7rNc&t=420s
		final ObservableList<String> optionsSituacao = FXCollections.observableArrayList();
		optionsSituacao.add("Novo");
		optionsSituacao.add("Confirmado");
		optionsSituacao.add("Sentado");
		optionsSituacao.add("Cancelado pelo cliente");
		optionsSituacao.add("Cancelado por solicita????o do cliente");
		optionsSituacao.add("Cancelado por no-show do cliente");
		optionsSituacao.add("Cancelado por erro de cadastro");
		filtrosSituacaoCheckComboBox.getItems().addAll(optionsSituacao);
		// Setando as op????es do checkComboBox que vai ser o filtro pros sal??es
		final ObservableList<String> optionsSalao = FXCollections.observableArrayList();
		optionsSalao.add("Almo??o Terrazza 40");
		optionsSalao.add("Caf?? da Tarde na Confeitaria");
		optionsSalao.add("P??r do Sol na Confeitaria");
		optionsSalao.add("Jantar no Terrazza 40");
		optionsSalao.add("38 Floor");
		filtrosSalaoCheckComboBox.getItems().addAll(optionsSalao);

		// pegando a prefer??ncia gravada anteriormente que lista os filtros prveiamente
		// selecionados para o checkcombobox das situa????es
		String statusGravados = preferences.get(PreferencesManager.STATUS_MARCADOS, null);
		// Verificando se a prefer??ncia est?? ou n??o nula. Se n??o estiver nula vai entrar
		// na parte que pega as pref??ncias do filtro e marca esses filtros previamente
		// salvos
		if (statusGravados != null) {
			// Como ?? uma observablelist quando usamos o toString ele vir?? com colchetes no
			// in??cio e no fim, aqui vamos retirar esses colchetes
			statusGravados = statusGravados.replace("[", "");
			statusGravados = statusGravados.replace("]", "");
			// Vamos dividir o String usando os caracteres ", " e colocaremos dentro de um
			// array
			String[] statusGravadosArray = statusGravados.split(", ");
			// loop que vai pegar os valores dentro do array e vai marcar como checked os
			// devidos campos previamente marcados na checkComboBox
			for (int i = 0; i < statusGravadosArray.length; i++) {
				filtrosSituacaoCheckComboBox.getCheckModel().check(statusGravadosArray[i]);
			}
		}

		// Adicionando um listener ao checkComboBox que vai verificar e fazer uma a????o
		// toda vez que um campo for marcado ou desmarcado nele
		filtrosSituacaoCheckComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> arg0) {
				// montando uma String com os campos da observableList dos filtrosSitua????o
				String checkedOptions = filtrosSituacaoCheckComboBox.getCheckModel().getCheckedItems().toString();
				// Passando a String para as prefer??ncias e salvando esses filtros. Essa a????o ??
				// chamado aqui no listener toda vez que o usu??rio mudar as op????es selecionadas
				// no checkComboBox
				preferences.put(PreferencesManager.STATUS_MARCADOS, checkedOptions);
			}
		});

		// pegando a prefer??ncia gravada anteriormente que lista os filtros prveiamente
		// selecionados para o checkcombobox dos sal??es
		String saloesGravados = preferences.get(PreferencesManager.SALOES_MARCADOS, null);
		// Verificando se a prefer??ncia est?? ou n??o nula. Se n??o estiver nula vai entrar
		// na parte que pega as pref??ncias do filtro e marca esses filtros previamente
		// salvos
		if (saloesGravados != null) {
			// Como ?? uma observablelist quando usamos o toString ele vir?? com colchetes no
			// in??cio e no fim, aqui vamos retirar esses colchetes
			saloesGravados = saloesGravados.replace("[", "");
			saloesGravados = saloesGravados.replace("]", "");
			// Vamos dividir o String usando os caracteres ", " e colocaremos dentro de um
			// array
			String[] saloesGravadosArray = saloesGravados.split(", ");
			// loop que vai pegar os valores dentro do array e vai marcar como checked os
			// devidos campos previamente marcados na checkComboBox
			for (int i = 0; i < saloesGravadosArray.length; i++) {
				filtrosSalaoCheckComboBox.getCheckModel().check(saloesGravadosArray[i]);
			}
		}

		// Adicionando um listener ao checkComboBox que vai verificar e fazer uma a????o
		// toda vez que um campo for marcado ou desmarcado nele
		filtrosSalaoCheckComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> arg0) {
				// montando uma String com os campos da observableList dos filtrosSalao
				String checkedOptions = filtrosSalaoCheckComboBox.getCheckModel().getCheckedItems().toString();
				// Passando a String para as prefer??ncias e salvando esses filtros. Essa a????o ??
				// chamado aqui no listener toda vez que o usu??rio mudar as op????es selecionadas
				// no checkComboBox
				preferences.put(PreferencesManager.SALOES_MARCADOS, checkedOptions);
			}
		});

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
		// Window ?? uma superclasse do stage ent??o teremos que fazer um downcasting pro
		// stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCostumer.prefHeightProperty().bind(stage.heightProperty());

		// M??todo para customizar a tabela
		customiseFactory();

	}

	// Atualizando os nomes na lista s?? com os clientes com reservas feitas para a
	// data de hoje
	public void updateTableView() {
		if (service == null) {
			logger.error("Service was null");
			throw new IllegalStateException("Service was null");
		}
		// Pegando o o resultado da query do MySQL e colocando numa lista principal
		List<Costumer> masterList = service.findAllofCurrentDate();
		// A partir daqui ?? uma mescla de coisas retiradas de:
		// 1) https://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
		// e
		// 2)http://javadox.com/org.controlsfx/controlsfx/8.40.10/org/controlsfx/control/CheckComboBox.html#method.summary
		// e
		// 3) do updateTableView do curso de Java da Udemy
		// Por fim, mudei tudo e arrumei usando bindings que encontrei aqui:
		// 4)https://stackoverflow.com/questions/52232150/filtering-tableview-with-controlsfx-checkcombobox
		// Fazendo a observableList padr??o desta classe receber a lista principal
		obsList = FXCollections.observableArrayList(masterList);

		// Criando predicados para cada checkComboBox e para o TextField
		ObjectProperty<Predicate<Costumer>> statusFilter = new SimpleObjectProperty<>();
		ObjectProperty<Predicate<Costumer>> saloonFilter = new SimpleObjectProperty<>();
		ObjectProperty<Predicate<Costumer>> nameSurnameFilter = new SimpleObjectProperty<>();

		// Fazendo o bind (enla??ando) cada um dos predicados aos valores das
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

		// Passando a observableList para uma lista que ser?? filtrada
		FilteredList<Costumer> filteredItems = new FilteredList<>(obsList);
		// Adicionando os dados filtrado ?? TableView
		tableViewCostumer.setItems(filteredItems);
		// Combinando os predicados usando Predicate.and(...) e fazendo o bind
		// (enla??ando)
		// a propriedade dos predicados da lista filtrada aos resultados
		filteredItems.predicateProperty()
				.bind(Bindings.createObjectBinding(
						() -> statusFilter.get().and(saloonFilter.get().and(nameSurnameFilter.get())), statusFilter,
						saloonFilter, nameSurnameFilter));

		// Verificando se as mensagens de confirma????o foram enviadas para poder colocar
		// na coluna das confirma????es
		CostumerXStandardMessageService costumerXmessageService = new CostumerXStandardMessageService();
		for (Costumer costumer : masterList) {
			CostumerXStandardMessage costumerXmessage = costumerXmessageService
					.findIfRelationshipExists(costumer.getId(), 2);
			if (costumerXmessage != null) {
				// Se o id do cliente na masterList estiver associado a um envio de mensagem de
				// confirma????o no banco de dados esse id vai ser adicionado na lista
				// idsComConfirma????o. Essa lista ser?? utilizada para comparar com o id do
				// cliente no momento em que as linhas da tabelas estiverem sendo constru??das
				// para poder mostrar ao usu??rio se a mensagem foi enviada ou n??o
				idsComConfirmacoes.add(costumer.getId());
			}
		}

		// iniciando os bot??es nas linhas dos clientes
		initColumnWhatsButtons();
		initColumnObsButtons();
		initColumnEditButtons();
		initConfirmacoesColumn();
		// Utils.autoResizeColumns(tableViewCostumer);
	}

	// M??todo que cria o formul??rio das mensagens de whatsapp inividuais
	private <T> void createForm(String title, Costumer obj, String absoluteName, Stage parentStage,
			Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			// Abaixo inicializando o controller
			T controller = loader.getController();
			initializingAction.accept(controller);
			// Adicionando listener numa clausula if/else e n??o chamando no
			// InitializingAction na express??o lambda como os outros m??todos do controller
			// porque l?? o "this" estava se referindo ?? c??lula e n??o ?? classe. ENt??o tive
			// que adicionar aqui para resolver o problema
			if (title.equals("Editar Reserva")) {
				((EditReservationFormController) controller).subscribeDataChangeListener(this);
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

	// M??todo do bot??o que utilizamos para mandar as mensagens padr??o de confirma????o
	// da reserva. Vamos mandar apenas uma vez por cliente. Por isso vamos ter que
	// adicionar no banco de dados a rela????o entre cliente e a mensagem para saber
	// se ela foi ou n??o mandada.
	public void onMandarConfirmacoesButtonAction(ActionEvent event) {
		createForm("Mandar Confirma????es", null, "/gui/SendConfirmationScreen.fxml", Utils.currentStage(event),
				(SendConfirmationScreenController controller) -> {
					controller.setCostumerXmessageService(new CostumerXStandardMessageService());
					controller.setMessageService(new StandardMessageService());
					controller.setNonExistentPhoneService(new NonExistentPhoneService());
					controller.setObsList(obsList);
					controller.subscribeDataChangeListener(this);
				});
	}

	// M??todo que coloca os bot??es do whatsapp nas linhas da tableview
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

				// Adicionei o c??digo para saber se o nome do costumer est?? nulo porque vai ter
				// as linhas com clientes nulos para dar um espa??o entre os nomes de clientes
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

	// M??todo que coloca os bot??es de Observa????es nas linhas da tableview
	private void initColumnObsButtons() {
		tableColumnObservacoes.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnObservacoes.setCellFactory(param -> new TableCell<Costumer, Costumer>() {
			private final Button button = new Button("*");

			@Override
			protected void updateItem(Costumer obj, boolean empty) {
				super.updateItem(obj, empty);

				// Adicionei o c??digo para saber se a observa????o vai estar em branco. Se estiver
				// n??o vai adicionar o bot??o. Se tiver observa????o vai aparecer um bot??o para
				// acessar o texto
				if (obj == null || obj.getObservacao().isBlank()) {
					setGraphic(null);
					return;
				}

				if (!obj.getObservacao().isBlank()) {
					setGraphic(button);
					button.setPrefWidth(50);
					button.setPrefHeight(50);
					button.setOnAction(event -> createForm("Observa????es", obj, "/gui/NotesForm.fxml",
							Utils.currentStage(event), (NotesFormController controller) -> {
								controller.setCostumer(obj);
								controller.updateFormData();
							}));
				}
			}
		});
	}

	// M??todo que coloca os bot??es de Observa????es nas linhas da tableview
	private void initColumnEditButtons() {
		Image img = new Image(new File("res/pencil_24x24.png").toURI().toString());
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Costumer, Costumer>() {
			ImageView view = new ImageView(img);
			private final Button button = new Button(null, view);

			@Override
			protected void updateItem(Costumer obj, boolean empty) {
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
				button.setOnAction(event -> createForm("Editar Reserva", obj, "/gui/EditReservationForm.fxml",
						Utils.currentStage(event), (EditReservationFormController controller) -> {
							controller.setCostumer(obj);
							controller.updateFormData();
						}));
			}
		});
	}

	// M??todo que compara a lista de idsComConfirmacoes com cada linha de cliente e
	// mostra se a mensagem foi enviada para este cliente ou n??o
	private void initConfirmacoesColumn() {
		tableColumnConfirmacoes.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnConfirmacoes.setCellFactory(param -> new TableCell<Costumer, Costumer>() {

			@Override
			protected void updateItem(Costumer obj, boolean empty) {
				super.updateItem(obj, empty);

				if (!empty) {
					setText("N??o");
					setStyle("-fx-alignment: CENTER; -fx-text-alignment: CENTER; -fx-font-weight: bold;");
					setTextFill(Color.DARKRED);
					for (Integer id : idsComConfirmacoes) {
						if (obj.getId().equals(id)) {
							setText("Enviada");
							setTextFill(Color.valueOf("#6e8003"));
						}
					}
				} else {
					setText(null);
				}
			}
		});
	}

	// M??todo que customiza as c??lulas da tabela de dinamicamente, de acordo com os
	// valores
	private void customiseFactory() {
		// Cores
		Color corCancelado = Color.RED;
		Color corNovoConfirmado = Color.valueOf("#6e8003");
		Color corSentado = Color.DARKBLUE;

		// Imagem
		Image imgWaiting = new Image(new File("res/costumer_waiting_48x53.png").toURI().toString());

		// Retirando as linhas das bordas da tabela
		tableViewCostumer.setStyle("-fx-table-cell-border-color: transparent;");

		// Customiza????o de linhas inteiras
		tableViewCostumer.setRowFactory(row -> new TableRow<Costumer>() {
			@Override
			public void updateItem(Costumer item, boolean empty) {
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
		// Customiza????o das colunas
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
						} else {
							setText(null);
						}
					}
				};
			}
		});

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

		tableColumnSituacao.setCellFactory(new Callback<TableColumn<Costumer, String>, TableCell<Costumer, String>>() {
			@Override
			public TableCell<Costumer, String> call(TableColumn<Costumer, String> param) {
				return new TableCell<Costumer, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						if (!empty) {
							ImageView view = new ImageView(imgWaiting);
							int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
							String columnSituacao = param.getTableView().getItems().get(currentIndex).getSituacao();
							boolean statusAguardando = param.getTableView().getItems().get(currentIndex).isAguardando();

							if (statusAguardando == true || statusAguardando) {
								view.setFitHeight(24);
								view.setFitWidth(28);
								setGraphic(view);
								setText(null);
								setStyle("-fx-alignment: CENTER; -fx-text-alignment: CENTER;");
							} else {
								setGraphic(null);
								setStyle("-fx-alignment: CENTER; -fx-text-alignment: CENTER; -fx-font-weight: bold;");
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
							setGraphic(null);
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

		tableColumnMesa.setCellFactory(new Callback<TableColumn<Costumer, String>, TableCell<Costumer, String>>() {
			@Override
			public TableCell<Costumer, String> call(TableColumn<Costumer, String> param) {
				return new TableCell<Costumer, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						if (!empty) {
							int currentIndex = indexProperty().getValue() < 0 ? 0 : indexProperty().getValue();
							String columnMesa = param.getTableView().getItems().get(currentIndex).getMesa();

							setStyle("-fx-alignment: CENTER; -fx-text-alignment: CENTER; -fx-font-weight: bold;");
							setWrapText(true);
							setPrefWidth(120);
							setWidth(120);
							setText(columnMesa);
						} else {
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
							} else {
								this.setText(columnPagamento.toString());
							}
						} else {
							setText(null);
						}
					}
				};
			}
		});
	}

}
