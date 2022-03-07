package gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.MyZapHandler;
import gui.util.Utils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import model.entities.Costumer;
import model.entities.CostumerXStandardMessage;
import model.entities.StandardMessage;
import model.services.CostumerXStandardMessageService;
import model.services.StandardMessageService;

public class SendConfirmationScreenController {

	private Logger logger = LogManager.getLogger(SendConfirmationScreenController.class);

	private CostumerXStandardMessageService costumerXmessageService;

	private StandardMessageService messageService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	private ObservableList<Costumer> obsList;

	@FXML
	private CheckBox checkBoxAlmoço;

	@FXML
	private CheckBox checkBoxCafeDaTarde;

	@FXML
	private CheckBox checkBoxPorDoSol;

	@FXML
	private CheckBox checkBoxJantar;

	@FXML
	private CheckBox checkBox38Floor;

	@FXML
	private Button buttonConfirmar;

	@FXML
	private Button buttonCancelar;

	public void setCostumerXmessageService(CostumerXStandardMessageService costumerXmessageService) {
		this.costumerXmessageService = costumerXmessageService;
	}

	public void setMessageService(StandardMessageService messageService) {
		this.messageService = messageService;
	}

	public void setObsList(ObservableList<Costumer> obsList) {
		this.obsList = obsList;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	public void onButtonConfirmarAction(ActionEvent event) {
		// Preparando a lista que será filtrada para passar para a parte do método que
		// vai verificar o banco de dados e mandar as mensagens logo abaixo
		List<Costumer> filteredList = new ArrayList<>();
		// checando as checkboxes escolhidas pelo usuário para montar a lista
		if (checkBoxAlmoço.isSelected()) {
			List<Costumer> tempList = obsList.stream()
					.filter(costumer -> checkBoxAlmoço.getText().contains(costumer.getSalao()))
					.collect(Collectors.toList());
			filteredList = Stream.of(filteredList, tempList).flatMap(x -> x.stream()).collect(Collectors.toList());
		}
		if (checkBoxCafeDaTarde.isSelected()) {
			List<Costumer> tempList = obsList.stream()
					.filter(costumer -> checkBoxCafeDaTarde.getText().contains(costumer.getSalao()))
					.collect(Collectors.toList());
			filteredList = Stream.of(filteredList, tempList).flatMap(x -> x.stream()).collect(Collectors.toList());
		}
		if (checkBoxPorDoSol.isSelected()) {
			List<Costumer> tempList = obsList.stream()
					.filter(costumer -> checkBoxPorDoSol.getText().contains(costumer.getSalao()))
					.collect(Collectors.toList());
			filteredList = Stream.of(filteredList, tempList).flatMap(x -> x.stream()).collect(Collectors.toList());
		}
		if (checkBoxJantar.isSelected()) {
			List<Costumer> tempList = obsList.stream()
					.filter(costumer -> checkBoxJantar.getText().contains(costumer.getSalao()))
					.collect(Collectors.toList());
			filteredList = Stream.of(filteredList, tempList).flatMap(x -> x.stream()).collect(Collectors.toList());
		}
		if (checkBox38Floor.isSelected()) {
			List<Costumer> tempList = obsList.stream()
					.filter(costumer -> checkBox38Floor.getText().contains(costumer.getSalao()))
					.collect(Collectors.toList());
			filteredList = Stream.of(filteredList, tempList).flatMap(x -> x.stream()).collect(Collectors.toList());
		}
		// Começando a parte de verificar no banco de dados e mandar as mensagens
		try {
			// variável que vai contar quantas mensagens foram enviadas a partir do
			// recebimento do código 200 do servidor
			int quantidadeMensagensEnviadas = 0;
			// Instanciando um novo objeto de relação entre o cliente e a mensagem padrão
			CostumerXStandardMessage costumerXmessage = new CostumerXStandardMessage();
			// SimpleDateFormat para formatar o campo de horário que vai para mensagem em
			// string
			SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
			// loop para percorrer cada cliente da lista da tabela
			for (Costumer costumer : filteredList) {
				// variável vai receber o retorno da relação entre o cliente e a mesangem padrão
				// número 2, que será sempre a mesma mensagem de confirmação
				costumerXmessage = costumerXmessageService.findIfRelationshipExists(costumer.getId(), 2);
				// se o retorno do banco de dados sobre a relação for nulo:
				if (costumerXmessage == null) {
					// se for nulo e a situação for "Novo" ou "Confirmado" vamos mandar a mensagem
					// no whats. Só nesses casos porque não vamos perguntar aos clientes se eles
					// confirmam reservas já canceladas ou sentadas. Como o "confirmado" vem do
					// sistema de fora vamos confirmar mais uma vez
					if (costumer.getSituacao().equals("Novo") || costumer.getSituacao().equals("Confirmado")) {
						// Pegando a mensagm pelo título e passando para um objeto mensagem
						StandardMessage message = messageService.findByTitle("Confirmação de reserva");
						// Variável que vai juntar o nome e sobrenome do cliente numa unica string
						String nomeESobrenome = costumer.getNome() + " " + costumer.getSobrenome();
						// Fazendo uma string dinamicamente com o nome, o horário e número de pessoas do
						// cliente para ser passada como parâmetro para o método que vai mandar a
						// mensagem
						String textMessage = String.format(message.getMensagem(), nomeESobrenome,
								hr.format(costumer.getHora()).toString(), costumer.getPessoas());
						// Chamando o método que envia mensagem e retorna o código de status
						Integer messageStatusCode = MyZapHandler.messageSender(costumer.getTelefone(), textMessage);
						// Se o código de status confirmar o envio vamos criar uma linha no banco de
						// dados para relacionar a mensagem com o cliente. Assim, nas próximas vezes não
						// vamos mandar a mesma mensagem por conta da primeira decisão desse método
						if (messageStatusCode >= 200 && messageStatusCode < 300) {
							costumerXmessageService
									.createRelationship(new CostumerXStandardMessage(costumer.getId(), 2));
							// incrementando a quantidade de mensagens enviadas para mostrar ao usuário no
							// Alert do "Processo finalizado"
							quantidadeMensagensEnviadas++;
							// Alerts.showAlert("Mensagem enviada com sucesso!", null, "Sua mensagem foi
							// enviada com sucesso!", AlertType.INFORMATION);
						}
						// Colocando um tempo de delay forçado para evitar que o sistema tente mandar
						// mais mensagens do que o servidor consegue processar
						Thread.sleep(500);
					}
				}
			}
			notifyDataChangeListeners();
			Alerts.showAlert("Processo finalizado!", null, "O processo de envio das mensagens foi finalizado!\n"
					+ quantidadeMensagensEnviadas + " mensagens enviadas.", AlertType.INFORMATION);
		} catch (Exception e) {
			logger.error(e.getMessage() + e);
			e.printStackTrace();
			notifyDataChangeListeners();
		}
	}

	public void onButtonCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

}
