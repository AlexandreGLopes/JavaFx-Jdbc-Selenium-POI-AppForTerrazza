package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entities.Costumer;
import model.services.CostumerService;

public class EditReservationFormController implements Initializable {

	CostumerService service = new CostumerService();

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	private Costumer entity;

	@FXML
	private ComboBox<String> comboBoxSituacao;

	@FXML
	private CheckBox checkBoxAguardando;

	@FXML
	private TextField textFieldSentar;

	@FXML
	private TextArea textAreaObservacoes;

	@FXML
	private Button buttonSalvar;

	@FXML
	private Button buttonCancelar;

	// método para setar o cliente que vamos editar neste formulário
	public void setCostumer(Costumer entity) {
		this.entity = entity;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Inicializando as opções da comboBox
		final ObservableList<String> optionsSituacao = FXCollections.observableArrayList();
		optionsSituacao.add("Novo");
		optionsSituacao.add("Confirmado");
		optionsSituacao.add("Sentado");
		optionsSituacao.add("Cancelado pelo cliente");
		optionsSituacao.add("Cancelado por solicitação do cliente");
		optionsSituacao.add("Cancelado por no-show");
		optionsSituacao.add("Cancelado por erro");
		comboBoxSituacao.getItems().addAll(optionsSituacao);
	}

	// método que coloca os dados editáveis do cliente nos respectivos campos quando
	// abrimos o formulário
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		comboBoxSituacao.setValue(entity.getSituacao());
		if (entity.isAguardando()) {
			checkBoxAguardando.setSelected(true);
		} else {
			checkBoxAguardando.setSelected(false);
		}
		textFieldSentar.setText(entity.getMesa());
		textAreaObservacoes.setText(entity.getObservacao());
	}

	// método para pegar os dados de dentro das caixinhas de texto e outros
	// elementos e retornar um objeto cliente. Este cliente será igualado ao entity
	// para puxar alguns dos atributos dele e modificar os que nós quisermos com
	// base no que tiver dentro das caixinhas
	private Costumer getFormData() {
		Costumer costumer = entity;

		costumer.setSituacao(comboBoxSituacao.getValue());
		costumer.setAguardando(checkBoxAguardando.isSelected());
		costumer.setMesa(textFieldSentar.getText());
		costumer.setObservacao(textAreaObservacoes.getText());

		return costumer;
	}

	// Botão de salvar
	@FXML
	public void onButtonSalvarAction(ActionEvent event) {
		// programação defensiva. Se o programador tiver esquecido de injetar
		// vamos fazer isso pois estamos fazendo uma injeção de dependecias manual
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		// Entity recebendo os dados das caixinhas
		entity = getFormData();
		if (entity.getMesa() == null || entity.getMesa().trim().equals("") && entity.getSituacao().equals("Sentado")) {
			Alerts.showAlert("Número da mesa não descrito", null,
					"Por favor, ao sentar o cliente coloque o número da mesa", AlertType.WARNING);
		} else {
			// Vamos salvar os dados no banco de dados no banco de dados
			service.updateCostumerFromEditForm(entity);
			// notificando os listeners
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
	}

	// Botão de cancelar
	@FXML
	public void onButtonCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
}
