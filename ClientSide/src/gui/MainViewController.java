package gui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
	private Button testButton;
	
	//Iniciando as referências para a TableView

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
	
	//Date aqui é java.util.date
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
	
	//Final das referências do TableView
	
	private ObservableList<Costumer> obsList;
	
	public void setCostumerService(CostumerService service) {
		this.service = service;
	}
	
	public void onTestButtonAction() throws ParseException {
		
	}

	@FXML
	public void onMenuItemRefreshFromWaitlistAction(ActionEvent event) {
		String option = "a";
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		createLoadingPane(option, "/gui/LoadingScreen.fxml", currentStage);
	}

	@FXML
	public void onMenuItemRefreshFromWixAction() {
		String option = "b";
		// parâmetro dentro de um objeto Stage que receberá o método currentStage do
		// Utils
		Stage currentStage = (Stage) rootVBox.getScene().getWindow();
		createLoadingPane(option, "/gui/LoadingScreen.fxml", currentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
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

	private void createLoadingPane(String option, String absoluteName, Stage parentStage) {
		try {
			// Carregar o fxml
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// pane vai receber o que carregou do loader
			Pane pane = loader.load();
			// Vamos injetar o departamento client no controlador da tela de loading
			// Pegando referencia para o controlador
			LoadingScreenController controller = loader.getController();
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
			if (option == "b") {
				controller.setLabel("Atualizar do Wix\n(38 Floor)");
			}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Atualizando os nomes na lista só com os clientes com reservas feitas para a data de hoje
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Costumer> list = service.findAllofCurrentDate();
		obsList = FXCollections.observableArrayList(list);
		tableViewCostumer.setItems(obsList);
		initColumnButtons();
		Utils.autoResizeColumns(tableViewCostumer);
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	private void createDialogForm(Costumer obj, String absoluteName, Stage parentStage) {
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
		}
		catch (IOException e) {
			Alerts.showAlert("IOException", "Erro carregando o painel", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void initColumnButtons() { 
		  tableColumnWhats.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		  tableColumnWhats.setCellFactory(param -> new TableCell<Costumer, Costumer>() { 
		    private final Button button = new Button("WhatsApp");
		 
		    @Override 
		    protected void updateItem(Costumer obj, boolean empty) { 
		      super.updateItem(obj, empty); 
		 
		      if (obj == null) { 
		        setGraphic(null); 
		        return;
		      }
		      
		      setGraphic(button);
		      button.setOnAction( 
		      event -> createDialogForm( 
		        obj, "/gui/MessageForm.fxml", Utils.currentStage(event))); 
		    } 
		  });
		}
}
