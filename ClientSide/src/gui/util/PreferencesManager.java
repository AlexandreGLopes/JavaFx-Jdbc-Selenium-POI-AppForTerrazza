package gui.util;

import java.util.prefs.Preferences;

// Classe que controla as preferências do aplicativo. Estas preferências são definições 
// salvas para serem recuperadas toda vez que o aplicativo for fechado e iniciado novamente

public class PreferencesManager {

	// node no qual será salvo o arquivo de preferências
	private static final String PREFERENCES_NODE = "com/domine/appmesadereservas/prefs";

	// preferência que salvará o IP no qual estará funcionando o ServerSide
	public static final String IP_TO_SERVERSIDE_MACHINE = "IPOfServerSide";
	
	// preferência que salvará o separador usado para ler o arquivo csv
	public static final String SEPARADOR_DO_CSV = "SeparadorDoCsv";
	
	// preferência que salvará o tempo de espera que daremos para o servidor concluir as tarefas
	public static final String ESPERA_POR_TAREFA_DO_SERVIDOR = "EsperaServidor";
	
	// preferência que salvará quais os itens da checkcombobox de salão estão marcados
	public static final String SALOES_MARCADOS = "SaloesMarcados";
	
	// preferência que salvará quais os itens da checkcombobox de salão estão marcados
	public static final String STATUS_MARCADOS = "StatusMarcados";
	
	// preferência que salvará o IP da API do whatsapp
	public static final String IP_TO_WHATSAPP_API = "IPOfWhatsappAPI";
	
	// preferência que salvará o nome da sessão da API do whatsapp
	public static final String SESSION_NAME = "SessionName";
	
	// preferência que salvará a key da sessão da API do whatsapp
	public static final String SESSION_KEY = "SessionKey";

	// método que retorna as preferências todas. Aqui estamos usando a preferência
	// do usuário Root específico.
	public static Preferences getPreferences() {
		return Preferences.userRoot().node(PREFERENCES_NODE);
	}

}
