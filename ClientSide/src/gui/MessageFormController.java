package gui;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import gui.util.Alerts;
import gui.util.MyZapHandler;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.entities.Costumer;

public class MessageFormController implements Initializable {
	
	private Logger logger = LogManager.getLogger(MessageFormController.class);
	
	// private Preferences preferences = PreferencesManager.getPreferences();

	private Costumer entity;

	@FXML
	private Label labelCostumerInfo;

	@FXML
	private Label labelCostumerPhone;

	@FXML
	private TextArea textMessage;

	@FXML
	private Button btEnviar;

	@FXML
	private Button btCancelar;

	@FXML
	public void onBtEnviarAction(ActionEvent event) {
		sendWhatsAppMessage(event);
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void setCostumer(Costumer entity) {
		this.entity = entity;
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
	}

	// Método para mostrar dados do cliente no cabeçalho
	public void updateFormData() {
		if (entity == null) {
			logger.error("Entity was null");
			throw new IllegalStateException("Entity was null");
		}
		labelCostumerInfo.setText("Cliente: " + entity.getNome() + " " + entity.getSobrenome());
		labelCostumerPhone.setText("Telefone: " + entity.getTelefone());
	}

	// Método que pega o telefone do objeto passado pela TableView e o texto do
	// formulário e manda mensagem por whatsapp usando a API que está rodando no
	// servidor VPS
	private void sendWhatsAppMessage(ActionEvent event) {
		// String que vai receber o telefone do objeto que foi passado no controller
		String telefone = entity.getTelefone();
		
		try {

			HttpResponse messageStatusCode = MyZapHandler.messageSender(telefone, textMessage.getText());

			// em caso de sucesso vamos mostrar um Alert
			if (messageStatusCode.getStatusLine().getStatusCode() >= 200 
				&& messageStatusCode.getStatusLine().getStatusCode() < 300) {
				Alerts.showAlert("Mensagem enviada com sucesso!", null, "Sua mensagem foi enviada com sucesso!",
						AlertType.INFORMATION);
			} else {
				if (messageStatusCode.getStatusLine().getStatusCode() >= 300) {
					// criando um objeto JSON e colocando dentro dele o Entity da mensagem da Api passada para string
					JSONObject album = new JSONObject(EntityUtils.toString(messageStatusCode.getEntity(), "UTF-8"));
					// Fazendo uma string com o value que estiver dentro da key "message"
					String messageJSON = album.getString("message");
					// como as mensagens estão em inglês vamos passá-las para português para mostrá-las no Alert
					if (messageJSON.equals("Error: this number is not valid")) {
						messageJSON = "O número de telefone não é válido para whatsapp.";
					} else if (messageJSON.equals("Error: the sessionkey is invalid")) {
						messageJSON = "O 'nome da sessão' cadastrado nas preferências é inválido.";
					}
					Alerts.showAlert("Erro ao enviar mensagem!", null,
							"Houve um erro ao tentar enviar a mensagem.\n\n" + messageJSON,
							AlertType.ERROR);
				}
			}
		} catch (Exception e) {
			Alerts.showAlert("Erro ao enviar mensagem!", null,
						"Houve um erro ao tentar enviar a mensagem.\nContate o desenvolvedor para saber mais.",
						AlertType.ERROR);
		}
	}
}
