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
		// String que vai receber o telefone do objeto que foi passado no controller
		String telefone = entity.getTelefone();
		// Iniciando o uso do HttpClient da Apache
		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			// Montando um resquest do tipo POST e passando o servidor, a porta e ométodo
			// usado para mandar mensagem
			HttpPost request = new HttpPost("http://51.222.146.252:3333/sendText");
			// Construindo Body do JSON segundo a API do MyZAP 2.0
			// Passamos o telefone conforme o Costumer passado e a mensagem pegada do
			// textArea. Usamos o .replace para formatar toda vez que tiver uma quebra de
			// linha "\n" adicionar uma barra a mais. Se não ela vai se tornar uma nova
			// linha no JSON (e não um dado que é passado como String no JSON).
			StringEntity params = new StringEntity("{\"session\":\"terrazzaobscwb\",\"number\":\"" + telefone
					+ "\",\"text\":\"" + textMessage.getText().replace("\n", "\\n") + "\"}", "UTF-8");
			// Headers do JSON segundo a API do MyZAP 2.0
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			request.setHeader("sessionkey", "terrazza@A8K9hD");
			// Passando o Body para a requisição
			request.setEntity(params);
			// Executando a resquisição e pegando as resposta e colocando na variável
			// response
			HttpResponse response = httpClient.execute(request);
			// Passando o código da resposta da conecção http para uma variável ainda mais
			// restrita que vai ter apenas o integer do código
			int statusCode = response.getStatusLine().getStatusCode();
			// Verificando se o código está dentro da faixa de conclusão bem sucedida
			// Dependendo do código o Alert é montado para mostrar se deu certo ou não
			if (statusCode >= 200 && statusCode < 300) {
				Alerts.showAlert("Mensagem enviada com sucesso!", null, "Sua mensagem foi enviada com sucesso!",
						AlertType.INFORMATION);
			} else {
				Alerts.showAlert("Erro ao enviar mensagem!", null,
						"Houve um erro ao tentar enviar a mensagem.\nContate o desenvolvedor para saber mais.",
						AlertType.ERROR);
			}
		} catch (Exception e) {
			Alerts.showAlert("Erro ao enviar mensagem!", null,
					"Houve um erro ao tentar enviar a mensagem.\nContate o desenvolvedor para saber mais.\nCódigo do erro: "
							+ e.getMessage(),
					AlertType.ERROR);
		}
	}
}
