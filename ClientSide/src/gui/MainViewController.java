package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.controlsfx.control.CheckComboBox;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.CheckDuplicacatesMethods;
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
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Costumer;
import model.services.CostumerService;

public class MainViewController implements Initializable, DataChangeListener {

	private CostumerService service;

	@FXML
	private VBox rootVBox;

	@FXML
	private MenuItem menuItemRefreshFromWaitlist;

	@FXML
	private MenuItem menuItemRefreshFromWix;

	@FXML
	private MenuItem menuItemManualRefresh;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	private MenuItem menuItemManualRefreshHelp;

	@FXML
	private Button tabelaPrincipalButton;
	
	@FXML
	private Button clientesDuplicadosButton;

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

	@FXML
	public void onTabelaPrincipalButtonAction() throws ParseException {
		updateTableView();
	}

	@FXML
	public void onClientesDuplicadosButtonAction() throws ParseException {
		showDuplicatedCostumersOnTableView();
	}

	@FXML
	public void onMenuItemRefreshFromWaitlistAction(ActionEvent event) {
		String option = "a";
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/LoadingScreen.fxml", currentStage, (LoadingScreenController controller) -> {
			// Setando qual foi a opção escolhida pelo usuário direto no próximo controlador
			// para que ele envie a opção para o servidor. Lá o servidor vai entrar em
			// switch/case para escolher os métodos de download usados
			controller.setOption(option);
			controller.subscribeDataChangeListener(this);
			// Mudando o Label do próximo painel de acordo com a opção escolhida pelo
			// usuário. Aprendi que não podemos chamar essas mudanças no initialize porque
			// sempre vai dar null
			if (option == "a") {
				controller.setLabel("Atualizar do Waitlist\n(Terrazza 40)");
			}
		});
	}

	@FXML
	public void onMenuItemRefreshFromWixAction() {
		String option = "b";
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/LoadingScreen.fxml", currentStage, (LoadingScreenController controller) -> {
			// Setando qual foi a opção escolhida pelo usuário direto no próximo controlador
			// para que ele envie a opção para o servidor. Lá o servidor vai entrar em
			// switch/case para escolher os métodos de download usados
			controller.setOption(option);
			controller.subscribeDataChangeListener(this);
			// Mudando o Label do próximo painel de acordo com a opção escolhida pelo
			// usuário. Aprendi que não podemos chamar essas mudanças no initialize porque
			// sempre vai dar null
			if (option == "b") {
				controller.setLabel("Atualizar do Wix\n(38 Floor)");
			}
		});
	}

	@FXML
	public void onMenuItemManualRefreshAction() {
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/ManualUpdaterScreen.fxml", currentStage, (ManualUpdaterScreenController controller) -> {
			// Setando qual foi a opção escolhida pelo usuário direto no próximo controlador
			// para que ele envie a opção para o servidor. Lá o servidor vai entrar em
			// switch/case para escolher os métodos de download usados
			controller.subscribeDataChangeListener(this);
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/About.fxml", currentStage, (x) -> {
		});
	}

	@FXML
	public void onMenuItemManualRefreshHelpAction() {
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		loadPane("/gui/ManualUpdaterHelp.fxml", currentStage, (x) -> {
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		// Setando as opções do checkComboBox que vai ser o filtro pras situações das
		// reservas
		// retirado de : https://www.youtube.com/watch?v=fjkkzkk7rNc&t=420s
		final ObservableList<String> optionsSituacao = FXCollections.observableArrayList();
		optionsSituacao.add("Novo");
		optionsSituacao.add("Confirmado");
		optionsSituacao.add("Sentado");
		optionsSituacao.add("Cancelado pelo cliente");
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
		/*
		tableColumnSituacao.setCellFactory(new Callback<TableColumn<Costumer,String>, TableCell<Costumer,String>>() {
			
			@Override
			public TableCell<Costumer, String> call(
					TableColumn<Costumer, String> param) {
				return new TableCell<Costumer, String>(){
					@Override
					protected void updateItem(String item, boolean empty) {
						if (!empty) {
                            int currentIndex = indexProperty()
                                    .getValue() < 0 ? 0
                                    : indexProperty().getValue();
                            String columnSituacao = param
                            		.getTableView().getItems()
                            		.get(currentIndex).getSituacao();
                            if (columnSituacao.contains("Cancelado")) {
                            	setTextFill(Color.RED);
                            	setText(columnSituacao);
                            }
                            else {
                            	setTextFill(Color.BLACK);
                            	setText(columnSituacao);
                            }
						}
					}
				};
			}
		});
		*/
		
		/*
		tableViewCostumer.setRowFactory(row -> new TableRow<Costumer>() {
			@Override
			public void updateItem(Costumer item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null) {
					setStyle("");
				} else if (item.getSituacao().contains("Cancelado")) {
					setTextFill(Color.RED);
						}
				else {
					setTextFill(Color.BLACK);
					}
			}
		});
		*/
		/*
		tableViewCostumer.setRowFactory(new Callback<TableView<Costumer>, TableRow<Costumer>>() {
			@Override
			public TableRow<Costumer> call(TableView<Costumer> param) {
				return new TableRow<Costumer>() {
					@Override
					protected void updateItem(Costumer item, boolean empty) {
						if (!empty) {
							int currentIndex = indexProperty()
                                    .getValue() < 0 ? 0
                                    : indexProperty().getValue();
                            String columnSituacao = param.getItems().get(currentIndex).getSituacao();
							if(columnSituacao.contains("Cancelado")) {
								setTextFill(Color.RED);
							}
							else {
								setTextFill(Color.BLACK);
							}
						}
					}
					
				};
			}
		});
		*/
		
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnSalao.setCellValueFactory(new PropertyValueFactory<>("salao"));
		tableColumnPessoas.setCellValueFactory(new PropertyValueFactory<>("pessoas"));
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("data"));
		Utils.formatTableColumnDate(tableColumnData, "dd/MM/yyyy");
		tableColumnHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
		Utils.formatTableColumnDate(tableColumnHora, "HH:mm");
		tableColumnMesa.setCellValueFactory(new PropertyValueFactory<>("mesa"));
		tableColumnSituacao.setCellValueFactory(new PropertyValueFactory<>("situacao"));
		tableColumnPagamento.setCellValueFactory(new PropertyValueFactory<>("pagamento"));
		Utils.formatTableColumnDouble(tableColumnPagamento, 2);
		tableColumnIdExterno.setCellValueFactory(new PropertyValueFactory<>("idExterno"));
	}

	// Método que carrega novos painéis
	private <T> void loadPane(String absoluteName, Stage parentStage, Consumer<T> initializingAction) {
		try {
			// Carregar o fxml
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// pane vai receber o que carregou do loader
			Pane pane = loader.load();
			// Ativando a função passada no parâmetro 4, tipo Consumer<T>
			// Todas as funções passadas no paraâmetro Consumer são funções para o
			// Controller
			// Essas funções são passadas em cada um dos métodos específicos que chamam
			// telas específicas, porque cada uma delas vai ter uma classe específica de
			// Controller
			// Abaixo inicializando o controller
			T controller = loader.getController();
			initializingAction.accept(controller);

			// Instanciar um novo stage (um palco na frente do outro)
			Stage dialogStage = new Stage();
			// Retirando a barra de título do painel de loading
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
			e.printStackTrace();
		}
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
		
		// Fazendo o bind (enlaçando) cada um dos predicados aos valores das checkComboBox e do TextField
		statusFilter.bind(Bindings.createObjectBinding(() -> costumer ->filtrosSituacaoCheckComboBox.getCheckModel().getCheckedItems().isEmpty() ||
				filtrosSituacaoCheckComboBox.getCheckModel().getCheckedItems().contains(costumer.getSituacao()),
				filtrosSituacaoCheckComboBox.getCheckModel().getCheckedItems()));
		
		saloonFilter.bind(Bindings.createObjectBinding(() -> costumer ->filtrosSalaoCheckComboBox.getCheckModel().getCheckedItems().isEmpty() ||
				filtrosSalaoCheckComboBox.getCheckModel().getCheckedItems().contains(costumer.getSalao()),
				filtrosSalaoCheckComboBox.getCheckModel().getCheckedItems()));
		
		nameSurnameFilter.bind(Bindings.createObjectBinding(() ->
				costumer -> costumer.getNome().toLowerCase().contains(filtroNomeSobrenomeTextField.getText().toLowerCase()) ||
				costumer.getSobrenome().toLowerCase().contains(filtroNomeSobrenomeTextField.getText().toLowerCase()),
				filtroNomeSobrenomeTextField.textProperty()));
		
		//Passando a observableList para uma lista que será filtrada
		FilteredList<Costumer> filteredItems = new FilteredList<>(obsList);
		// Adicionando os dados filtrado à TableView
		tableViewCostumer.setItems(filteredItems);
		//Combinando os predicados usando Predicate.and(...) e fazendo o bind (enlaçando)
		//a propriedade dos predicados da lista filtrada aos resultados
		filteredItems.predicateProperty().bind(Bindings.createObjectBinding(
				() -> statusFilter.get().and(saloonFilter.get().and(nameSurnameFilter.get())),
				statusFilter, saloonFilter, nameSurnameFilter));
		
		// iniciando os botões nas linhas dos clientes
		initColumnButtons();
		// customiseFactory();
		Utils.autoResizeColumns(tableViewCostumer);
	}

	public void showDuplicatedCostumersOnTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Costumer> duplicatedList = CheckDuplicacatesMethods.checkDuplicatesByName(service);

		obsList = FXCollections.observableArrayList(duplicatedList);
		tableViewCostumer.setItems(obsList);
		initColumnButtons();
		Utils.autoResizeColumns(tableViewCostumer);
		clientesDuplicadosButton.setStyle("-fx-effect: dropshadow(three-pass-box, #4287f5, 5, 0.0, 0, 1);");
	}

	@Override
	public void onDataChanged() {
		updateTableView();
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
	private void initColumnButtons() {
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
					button.setPrefWidth(30);
					view.setFitHeight(18);
					view.setFitWidth(18);
					button.setOnAction(
							event -> createMessageForm(obj, "/gui/MessageForm.fxml", Utils.currentStage(event)));
				}
			}
		});
	}

	private void customiseFactory() {
		tableViewCostumer.setRowFactory(row -> new TableRow<Costumer>() {
			@Override
			public void updateItem(Costumer item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setStyle("");
				} else {
					if (item.getSituacao().equals("Cancelado pelo cliente")) {
						for (int i = 0; i < getChildren().size(); i++) {
							((Labeled) getChildren().get(i)).setTextFill(Color.RED);
						}
					} else {
						for (int i = 0; i < getChildren().size(); i++) {
							((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);
							;
						}
					}
				}
			}
		});
	}
}
