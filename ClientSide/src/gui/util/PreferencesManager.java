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

	// método que retorna as preferências todas. Aqui estamos usando a preferência
	// do usuário Root específico.
	public static Preferences getPreferences() {
		return Preferences.userRoot().node(PREFERENCES_NODE);
	}

}