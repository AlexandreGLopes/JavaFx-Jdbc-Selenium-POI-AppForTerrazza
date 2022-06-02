package gui.util;

import java.util.prefs.Preferences;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyZapHandler {
	
	private static Logger logger = LogManager.getLogger(MyZapHandler.class);
	
	private static Preferences preferences = PreferencesManager.getPreferences();

	public static HttpResponse connect () {
		// Iniciando o uso do HttpClient da Apache
		HttpClient httpClient = HttpClientBuilder.create().build();

		// Buscado a preferência que guarda o IP e a Porta da API, o nome da sessão e a Key da sessão
		// e colocando elas em variaveis string
		String ipOfAPI = preferences.get(PreferencesManager.IP_TO_WHATSAPP_API, null);
		// pegando a api configurada das preferências
		String wichApi = preferences.get(PreferencesManager.WICH_API, null);

		try {
			if(wichApi.equals("ApiWppPropria")) {
				// Montando um resquest do tipo POST e passando o servidor, a porta e ométodo
			// usado para mandar mensagem
			HttpGet request = new HttpGet("http://" + ipOfAPI + "/status");
			// Headers do JSON segundo a API do MyZAP 2.0
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			// Executando a resquisição e pegando as resposta e colocando na variável
			// response
			HttpResponse response = httpClient.execute(request);

			return response;
			}
		} catch (Exception e) {
			logger.error(e.getMessage() + e);
		}
		return null;
	}
	
	public static HttpResponse messageSender (String telefone, String message) throws Exception {

		// pegando a api configurada das preferências
		String wichApi = preferences.get(PreferencesManager.WICH_API, null);
		// escolhendo qual api será utilizada para enviar as mensagens
		switch (wichApi) {
			case "MyZAP 2.0":
				return myZapSender(telefone, message);
			case "ApiWppPropria":
				return ApiPropriaSender(telefone, message);
			default:
				throw new Exception("Nenhuma API selecionada.");
		}
	}

	public static HttpResponse myZapSender(String telefone, String message) {
		// Iniciando o uso do HttpClient da Apache
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		// Buscado a preferência que guarda o IP e a Porta da API, o nome da sessão e a Key da sessão
		// e colocando elas em variaveis string
		String ipOfAPI = preferences.get(PreferencesManager.IP_TO_WHATSAPP_API, null);
		String sessionName = preferences.get(PreferencesManager.SESSION_NAME, null);
		String sessionKey = preferences.get(PreferencesManager.SESSION_KEY, null);

		try {
			// Montando um resquest do tipo POST e passando o servidor, a porta e ométodo
			// usado para mandar mensagem
			HttpPost request = new HttpPost("http://" + ipOfAPI + "/sendText");
			// Construindo Body do JSON segundo a API do MyZAP 2.0
			// Passamos o telefone conforme o Costumer passado e a mensagem pegada do
			// textArea. Usamos o .replace para formatar toda vez que tiver uma quebra de
			// linha "\n" adicionar uma barra a mais. Se não ela vai se tornar uma nova
			// linha no JSON (e não um dado que é passado como String no JSON).
			StringEntity params = new StringEntity("{\"session\":\"" + sessionName + "\",\"number\":\"" + telefone
					+ "\",\"text\":\"" + message.replace("\n", "\\n") + "\"}", "UTF-8");
			// Headers do JSON segundo a API do MyZAP 2.0
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			request.setHeader("sessionkey", sessionKey);
			// Passando o Body para a requisição
			request.setEntity(params);
			// Executando a resquisição e pegando as resposta e colocando na variável
			// response
			HttpResponse response = httpClient.execute(request);

			return response;
		} catch (Exception e) {
			logger.error(e.getMessage() + e);
		}
		return null;
	}

	public static HttpResponse ApiPropriaSender(String telefone, String message) {
		// Iniciando o uso do HttpClient da Apache
		HttpClient httpClient = HttpClientBuilder.create().build();

		// Buscado a preferência que guarda o IP e a Porta da API, o nome da sessão e a Key da sessão
		// e colocando elas em variaveis string
		String ipOfAPI = preferences.get(PreferencesManager.IP_TO_WHATSAPP_API, null);
		String sessionKey = preferences.get(PreferencesManager.SESSION_KEY, null);

		try {
			// Montando um resquest do tipo POST e passando o servidor, a porta e ométodo
			// usado para mandar mensagem
			HttpPost request = new HttpPost("http://" + ipOfAPI + "/send");
			// Construindo Body do JSON segundo a APIWppPropria
			// Passamos o telefone conforme o Costumer passado e a mensagem pegada do
			// textArea. Usamos o .replace para formatar toda vez que tiver uma quebra de
			// linha "\n" adicionar uma barra a mais. Se não ela vai se tornar uma nova
			// linha no JSON (e não um dado que é passado como String no JSON).
			StringEntity params = new StringEntity("{\"number\":\"" + telefone
					+ "\",\"message\":\"" + message.replace("\n", "\\n") + "\",\"sessionkey\":\"" + sessionKey + "\"}", "UTF-8");
			// Headers do JSON segundo a API do APIWppPropria
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			// Passando o Body para a requisição
			request.setEntity(params);
			// Executando a resquisição e pegando as resposta e colocando na variável
			// response
			HttpResponse response = httpClient.execute(request);

			return response;
		} catch (Exception e) {
			logger.error(e.getMessage() + e);
		}
		return null;
	}
}
