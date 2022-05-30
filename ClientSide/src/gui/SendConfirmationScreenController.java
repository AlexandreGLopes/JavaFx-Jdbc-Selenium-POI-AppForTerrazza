package gui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.MyZapHandler;
import gui.util.Utils;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import model.entities.Costumer;
import model.entities.CostumerXStandardMessage;
import model.entities.NonExistentPhone;
import model.entities.StandardMessage;
import model.services.CostumerXStandardMessageService;
import model.services.NonExistentPhoneService;
import model.services.StandardMessageService;

public class SendConfirmationScreenController {

	private Logger logger = LogManager.getLogger(SendConfirmationScreenController.class);

	private CostumerXStandardMessageService costumerXmessageService;

	private StandardMessageService messageService;

	private NonExistentPhoneService nonExistentPhoneService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	private ObservableList<Costumer> obsList;

	@FXML
	private CheckBox checkBoxAlmoco;

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

	@FXML
	private ImageView loadingGif;

	public void setCostumerXmessageService(CostumerXStandardMessageService costumerXmessageService) {
		this.costumerXmessageService = costumerXmessageService;
	}

	public void setMessageService(StandardMessageService messageService) {
		this.messageService = messageService;
	}

	public void setNonExistentPhoneService(NonExistentPhoneService nonExistentPhoneService) {
		this.nonExistentPhoneService = nonExistentPhoneService;
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

	// variável que recebe os ids do Costumer para inserir no Banco de dados e pular na próxima vez
	// que fizer o loop de enviar as mensagens de confirmação
	private Integer idCostumerSeDerErro;

	// vetor que vai receber as quantidades de mensagens enviadas, de erros no envio de mensagens
	// pela Api e de telefones que não passarem pela checagem de numeração desta classe, mais abaixo
	private String[] quantidadesRepostaAlert = new String[3];

	@FXML
	public void onButtonConfirmarAction(ActionEvent event) {

		// retirando os checkbox da tela e mostrando o gif de loading
		checkBoxAlmoco.setVisible(false);
		checkBoxCafeDaTarde.setVisible(false);
		checkBoxPorDoSol.setVisible(false);
		checkBoxJantar.setVisible(false);
		checkBox38Floor.setVisible(false);
		buttonConfirmar.setVisible(false);
		buttonCancelar.setVisible(false);
		buttonConfirmar.setDisable(true);
		buttonCancelar.setDisable(true);
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), loadingGif);
		Image gif = new Image(new File("res/loading.gif").toURI().toString());
		loadingGif.setImage(gif);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);
		fadeTransition.play();
		
		// Construindo uma Task que vai rodar em outro Thread para deixar a UI livre
		// para mostrar os FadeTransitions
		Task<String> task = new Task<>() {
			@Override
			protected String call() throws Exception {
				// chamando o loop que comunica com a Api e manda as confirmações
                messageValidation();
				return "close";
            }
        };
        // Ao final taskando se a string tem o valor close para notificar os listener e
		// fechar o painel e também tratar erros e mostrar alerts
		task.setOnSucceeded((e) -> {
			if ("close".equals(task.getValue())) {
				Alerts.showAlert("Processo finalizado!", null, "O processo de envio das mensagens foi finalizado!\n"
					+ quantidadesRepostaAlert[0] + " mensagens enviadas.\n"
					+ quantidadesRepostaAlert[1] + " mensagens NÃO enviadas.\n"
					+ quantidadesRepostaAlert[2] + " telefones com dígitos a mais ou códigos de área errados.", AlertType.INFORMATION);
				Utils.currentStage(event).close();
			}
		});
		task.setOnFailed((e) -> {
			notifyDataChangeListeners();
			logger.error(task.getException());
			Utils.currentStage(event).close();
			Alerts.showAlert("Erro", null, "Para mais informações verificar o arquivo de log da aplicação", AlertType.ERROR);
		});
		// Iniciando subthread para fazer a Task
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();

	}

	public void messageValidation() {
		// Preparando a lista que será filtrada para passar para a parte do método que
		// vai verificar o banco de dados e mandar as mensagens logo abaixo
		List<Costumer> filteredList = new ArrayList<>();
		// checando as checkboxes escolhidas pelo usuário para montar a lista
		if (checkBoxAlmoco.isSelected()) {
			List<Costumer> tempList = obsList.stream()
					.filter(costumer -> checkBoxAlmoco.getText().contains(costumer.getSalao()))
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
			// Puxando a lista dos ids de clientes que têm o número inexistente. Essa lista
			// é composta por dados do banco que são inseridos quando o loop abaixo de
			// confirmações é iniciado e recebe uma exception de falha de conexão. Ao
			// iniciar o loop de novo este select vai verificar os ids que deram erro de
			// conexão na hora de mandar as mensagens e vai pular eles ali abaixo
			List<NonExistentPhone> todayNonExistentPhonesList = nonExistentPhoneService.findAllTodayNonExistentPhones();
			// variável que vai contar quantas mensagens foram enviadas a partir do
			// recebimento do código 200 do servidor
			int quantidadeMensagensEnviadas = 0;
			// Duas variáveis abaxio vão ser incrementadas se deralgum erro. A primeira vai ser incrementada com
			// uma resposta de erro do servidor. A segunda será incrementada quando os ifs de verificação dos
			// números de celular forem falsos
			int quantidadeMensagensNaoEnviadas = 0;
			int quantidadeTelefonesForaPadrao = 0;
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
						// Verificando se o número possui extamente 13 dígitos
						if (costumer.getTelefone().length() == 13) {
							// verificando se o número do cliente é um número possível no Brasil
							if (costumer.getTelefone().charAt(0) == '5' && costumer.getTelefone().charAt(1) == '5'
									&& costumer.getTelefone().charAt(4) == '9'
									|| costumer.getTelefone().charAt(4) == '8'
									|| costumer.getTelefone().charAt(4) == '7') {
								// objeto temporário para usar o equals para comparar e ver se a lista contém o
								// id do cliente atual do loop
								NonExistentPhone temp = new NonExistentPhone(null, costumer.getId(), null);
								// Para que o processo continue, o id do cliente atual tem que ser diferente dos
								// ids dentro da lista, se não, vai pular
								if (!todayNonExistentPhonesList.contains(temp)) {
									// Colocando o id do cliente numa varável para se der errado colocar no banco de
									// dados e pular na próxima confirmação. Será usado no catch expection se for
									// necessário
									idCostumerSeDerErro = costumer.getId();
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
									HttpResponse messageStatusCode = MyZapHandler.messageSender(costumer.getTelefone(),
											textMessage);
									// Se o código de status confirmar o envio vamos criar uma linha no banco de
									// dados para relacionar a mensagem com o cliente. Assim, nas próximas vezes não
									// vamos mandar a mesma mensagem por conta da primeira decisão desse método
									if (messageStatusCode.getStatusLine().getStatusCode() >= 200 
										&& messageStatusCode.getStatusLine().getStatusCode() < 300) {
										costumerXmessageService
												.createRelationship(new CostumerXStandardMessage(costumer.getId(), 2));
										// incrementando a quantidade de mensagens enviadas para mostrar ao usuário no
										// Alert do "Processo finalizado"
										quantidadeMensagensEnviadas++;
										// Alerts.showAlert("Mensagem enviada com sucesso!", null, "Sua mensagem foi
										// enviada com sucesso!", AlertType.INFORMATION);
									} else if (messageStatusCode.getStatusLine().getStatusCode() >= 300) {
										quantidadeMensagensNaoEnviadas++;
									}
									// Colocando um tempo de delay forçado para evitar que o sistema tente mandar
									// mais mensagens do que o servidor consegue processar
									Thread.sleep(500);
								}
							} else {
								quantidadeTelefonesForaPadrao++;
							}
						} else {
							quantidadeTelefonesForaPadrao++;
						}
					}
				}
			}
			notifyDataChangeListeners();
			quantidadesRepostaAlert[0] = String.valueOf(quantidadeMensagensEnviadas);
			quantidadesRepostaAlert[1] = String.valueOf(quantidadeMensagensNaoEnviadas);
			quantidadesRepostaAlert[2] = String.valueOf(quantidadeTelefonesForaPadrao);
		} catch (Exception e) {
			logger.error(e.getMessage() + e.getStackTrace());
			// inserindo o id do cliente atual no banco de dados caso o telefone tenha ocasioado uma exception no processo de envio
			nonExistentPhoneService.insertNonExistentPhone(idCostumerSeDerErro);
			e.printStackTrace();  
			notifyDataChangeListeners();
		}
	}

	public void onButtonCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

}
