package gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.StandardMessage;
import model.entities.WaitingCostumer;
import model.services.StandardMessageService;
import model.services.WaitingCostumerService;

public class EditWaitingFormController implements Initializable {

	private WaitingCostumer entity;

	private WaitingCostumerService service;
	
	private StandardMessageService messageService;

	// Essa classe emite o evento e ela vai guardar uma lista de objetos
	// interessados em receber o evento
	// Com esse atributo vamos poder permitir que outros objetos se inscrevam nessa
	// lista
	// para isso faremos uma método abaixo
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	SimpleDateFormat hr = new SimpleDateFormat("HH:mm");

	@FXML
	private TextField textFieldNome;

	@FXML
	private TextField textFieldSobrenome;

	@FXML
	private TextField textFieldTelefone;

	@FXML
	private ComboBox<Integer> comboBoxPessoas;

	@FXML
	private ComboBox<String> comboBoxSalao;

	@FXML
	private DatePicker datePickerData;

	@FXML
	private ComboBox<String> comboBoxHorario;

	@FXML
	private TextField textFieldMesa;

	@FXML
	private ComboBox<String> comboBoxSituacao;

	@FXML
	private CheckBox checkBoxAguardando;

	@FXML
	private TextField textFieldObservacao;

	@FXML
	private Label labelErrorEmptyFields;

	@FXML
	private Button buttonSalvar;

	@FXML
	private Button buttonCancelar;

	public void setWaitingCostumer(WaitingCostumer entity) {
		this.entity = entity;
	}

	public void setService(WaitingCostumerService service, StandardMessageService messageService) {
		this.service = service;
		this.messageService = messageService;
	}

	// com este método, outros objetos, desde que implementem a interface
	// DataChangeListener, podem se inscrever pra receber o evento da classe
	// adicionamos um método (notifyDataChangeListeners) a mais e colocamos ele
	// dentro do onBtSaveAction abaixo
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Formatando o datePicker
		Utils.formatDatePicker(datePickerData, "dd/MM/yyyy");
		// Setando opções das comboBoxes
		final ObservableList<Integer> optionsPessoas = FXCollections.observableArrayList();
		for (int i = 1; i < 13; i++) {
			optionsPessoas.add(i);
		}
		comboBoxPessoas.getItems().addAll(optionsPessoas);
		final ObservableList<String> optionsSalao = FXCollections.observableArrayList();
		optionsSalao.add("Almoço Terrazza 40");
		optionsSalao.add("Confeitaria");
		optionsSalao.add("Jantar no Terrazza 40");
		optionsSalao.add("38 Floor");
		comboBoxSalao.getItems().addAll(optionsSalao);
		final ObservableList<String> optionsHorario = FXCollections.observableArrayList();
		optionsHorario.add("19:00");
		optionsHorario.add("19:30");
		optionsHorario.add("20:00");
		optionsHorario.add("20:30");
		optionsHorario.add("21:00");
		optionsHorario.add("21:30");
		optionsHorario.add("22:00");
		optionsHorario.add("22:30");
		comboBoxHorario.getItems().addAll(optionsHorario);
		final ObservableList<String> optionsSituacao = FXCollections.observableArrayList();
		optionsSituacao.add("Novo");
		optionsSituacao.add("Sentado");
		optionsSituacao.add("Cancelado pelo cliente");
		optionsSituacao.add("Cancelado por no-show");
		comboBoxSituacao.getItems().addAll(optionsSituacao);
		// Chamando metodo que vai inicializar as regras das caixinhas
		initializeNodes();
	}

	// Regras ficarão dentro desta função
	private void initializeNodes() {
		Constraints.setTextFieldInteger(textFieldTelefone);
		Constraints.setTextFieldMaxLength(textFieldTelefone, 13);
		Constraints.setTextFieldMaxLength(textFieldNome, 60);
		Constraints.setTextFieldMaxLength(textFieldSobrenome, 80);
		Constraints.setTextFieldMaxLength(textFieldMesa, 50);
	}

	// Método para mudar as opções da comboBox de Horário quando o usuário escolher
	// outra opção na vomboBoxSalão
	public void onComboBoxSalaoChange() {
		comboBoxHorario.getItems().clear();
		final ObservableList<String> optionsHorario = FXCollections.observableArrayList();
		switch (comboBoxSalao.getValue()) {
		case "Almoço Terrazza 40":
			optionsHorario.add("12:00");
			optionsHorario.add("12:30");
			optionsHorario.add("13:00");
			optionsHorario.add("13:30");
			optionsHorario.add("14:00");
			optionsHorario.add("14:30");
			break;
		case "Confeitaria":
			optionsHorario.add("15:00");
			optionsHorario.add("17:00");
			break;
		case "Jantar no Terrazza 40":
			optionsHorario.add("19:00");
			optionsHorario.add("19:30");
			optionsHorario.add("20:00");
			optionsHorario.add("20:30");
			optionsHorario.add("21:00");
			optionsHorario.add("21:30");
			optionsHorario.add("22:00");
			optionsHorario.add("22:30");
			break;
		case "38 Floor":
			optionsHorario.add("19:00");
			optionsHorario.add("19:30");
			optionsHorario.add("20:00");
			optionsHorario.add("21:30");
			optionsHorario.add("22:00");
			break;
		default:
			break;
		}
		comboBoxHorario.getItems().addAll(optionsHorario);
		// Setando a opção padrão da combobox para o primeiro item
		comboBoxHorario.setValue(comboBoxHorario.getItems().get(0));
	}

	// Método que pega os dados do objeto que foi passado para o formulário e
	// adiciona os paraâmetros dele nos TextFields e comboBoxes
	public void updateFormData() {
		// Programação defensiva
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		// Setando os elementos
		textFieldNome.setText(entity.getNome());
		textFieldSobrenome.setText(entity.getSobrenome());
		textFieldTelefone.setText(entity.getTelefone());
		comboBoxPessoas.setValue(entity.getPessoas());
		comboBoxSalao.setValue(entity.getSalao());
		if (entity.getData() != null) {
			// Passando o parâmetro de data do cliente para uma variável que vamos tratar
			// para passar ela para o dataPicker. Usamos o getTime() e depois passamos para
			// Instant()
			Date data = new java.util.Date(entity.getData().getTime());
			datePickerData.setValue(LocalDate.ofInstant(data.toInstant(), ZoneId.systemDefault()));
		}
		comboBoxHorario.setValue(hr.format(entity.getHoraChegada()).toString());
		textFieldMesa.setText(entity.getMesa());
		comboBoxSituacao.setValue(entity.getSituacao());
		if (entity.isAguardando()) {
			checkBoxAguardando.setSelected(true);
		}
		textFieldObservacao.setText(entity.getObservacao());
	}

	// método para pegar os dados de dentro das caixinhas de texto e passando para
	// um novo objeto que será usado para adicionar ou fazer o update do banco de
	// dados
	private WaitingCostumer getFormData() throws ParseException {
		WaitingCostumer obj = new WaitingCostumer();
		obj.setId(entity.getId());
		obj.setNome(textFieldNome.getText());
		obj.setSobrenome(textFieldSobrenome.getText());
		// tratando o conteúdo da textFieldTelefone para que fique extamemnte como um
		// número de telefone possível de mandar mensagem
		String telefone = textFieldTelefone.getText();
		if (telefone.length() == 8) {
			telefone = "55419" + telefone;
		} else if (telefone.length() == 9) {
			telefone = "5541" + telefone;
		} else if (telefone.length() == 11) {
			telefone = "55" + telefone;
		}
		obj.setTelefone(telefone);
		obj.setPessoas(comboBoxPessoas.getValue());
		obj.setSalao(comboBoxSalao.getValue());
		// Trabalhando com a data para converter do datePicker para o Date.util.java
		Instant instant = Instant.from(datePickerData.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setData(Date.from(instant));
		obj.setHoraChegada(hr.parse(comboBoxHorario.getValue()));
		obj.setSituacao(comboBoxSituacao.getValue());
		obj.setAguardando(checkBoxAguardando.isSelected());
		obj.setObservacao(textFieldObservacao.getText());
		// Se o cliente estiver sentado vamos adicionar os parêmtros de mesa e horário
		// que sentou
		if (obj.getSituacao() == "Sentado") {
			obj.setHoraSentada(new Date());
			obj.setMesa(textFieldMesa.getText());
		}
		return obj;

	}

	// Método chamado quando clicar no botão salvar
	public void onButtonSalvarAction(ActionEvent event) {
		// Programação defensiva
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		// Temos campos que não poderão estar vazios: Nome, sobrenome e telefone. Vamos
		// setar o Label de Erro para uma mensagem caso o usuário deixe qualquer um
		// deles vazio
		if (textFieldNome.getText() == null || textFieldNome.getText().trim().equals("")
				|| textFieldSobrenome.getText() == null || textFieldSobrenome.getText().trim().equals("")
				|| textFieldTelefone.getText() == null || textFieldTelefone.getText().trim().equals("")
				|| textFieldTelefone.getText().length() < 13) {
			labelErrorEmptyFields.setText("Campos Nome, sobrenome e telefone não podem estar vazios");
		}
		// Caso todos os campos estejam corretos:
		else {
			try {
				// Entity recebendo os dados das caixinhas
				entity = getFormData();
				// Vamos salvar os dados no banco de dados no banco de dados
				service.saveOrUpdate(entity);
				// notificando os listeners
				notifyDataChangeListeners();
				// Fechando a janela. Pegando o palco atual por meio do evento ocorrido
				Utils.currentStage(event).close();
			} catch (ParseException e) {
				Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
			}
			catch (DbException e) {
				Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
			}
			finally {
				StandardMessage message = messageService.findByTitle("Cliente adicionado à espera");
				System.out.println(String.format(message.getMensagem(), entity.getNome(), hr.format(entity.getHoraChegada()).toString()));
			}
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	public void onButtonCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

}
