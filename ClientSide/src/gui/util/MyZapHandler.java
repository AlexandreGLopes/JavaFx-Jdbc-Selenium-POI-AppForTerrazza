package gui.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Alert.AlertType;

public class MyZapHandler {
	
	private static Logger logger = LogManager.getLogger(MyZapHandler.class);

	public static Integer messageSender(String telefone, String message) {
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
					+ "\",\"text\":\"" + message.replace("\n", "\\n") + "\"}", "UTF-8");
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
			Integer statusCode = response.getStatusLine().getStatusCode();
			return statusCode;
		} catch (Exception e) {
			logger.error(e.getMessage());
			Alerts.showAlert("Erro ao enviar mensagem!", null,
					"Houve um erro ao tentar enviar a mensagem.\nContate o desenvolvedor para saber mais.\nCódigo do erro: "
							+ e.getMessage(),
					AlertType.ERROR);
		}
		return null;
	}

}
