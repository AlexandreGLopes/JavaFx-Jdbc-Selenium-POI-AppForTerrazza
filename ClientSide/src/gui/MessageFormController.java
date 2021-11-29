package gui;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import gui.util.Alerts;
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
			throw new IllegalStateException("Entity was null");
		}
		labelCostumerInfo.setText("Cliente: " + entity.getNome() + " " + entity.getSobrenome());
		labelCostumerPhone.setText("Telefone: " + entity.getTelefone());
	}

	// Método que pega o telefone do objeto passado pela TableView e o texto do
	// formulário e manda mensagem por whatsapp usando a API que está rodando no
	// servidor VPS
	private void sendWhatsAppMessage(ActionEvent event) {
		String telefone = entity.getTelefone();

		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpPost request = new HttpPost("http://51.222.146.252:3333/sendText");
			StringEntity params = new StringEntity("{\"session\":\"TESTE\",\"number\":\"" + telefone + "\",\"text\":\""
					+ textMessage.getText() + "\"}", "UTF-8");
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			request.setHeader("sessionkey", "CHAVE1234");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode >= 200 && statusCode < 300) {
				Alerts.showAlert("Mensagem enviada com sucesso!", null, "Sua mensagem foi enviada com sucesso!",
						AlertType.INFORMATION);
			}
			else {
				Alerts.showAlert("Erro ao enviar mensagem!", null,
						"Houve um erro ao tentar enviar a mensagem.\nContate o desenvolvedor para saber mais.", AlertType.ERROR);
			}
		} catch (Exception e) {
			Alerts.showAlert("Erro ao enviar mensagem!", null,
					"Houve um erro ao tentar enviar a mensagem.\nContate o desenvolvedor para saber mais.\nCódigo do erro: "
							+ e.getMessage(),
					AlertType.ERROR);
		}
	}
}
