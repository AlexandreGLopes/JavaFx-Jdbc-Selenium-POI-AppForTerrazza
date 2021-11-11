package util;

import java.io.File;

public class OwnFileHandler {

	// Método que verifica o sistema operacional (com a auda de OSValidator) e monta
	// um String com o início do path até o arquivo do sistema das reservas
	public static String definePath() {
		String os = OSValidator.checkOSName();
		String user = System.getProperty("user.name");
		String path = null;
		if (os == "windows") {
			path = "C:\\Users\\" + user + "\\Downloads\\";
		}
		if (os == "linux") {
			path = "/home/" + user + "/Downloads/";
		}
		return path;
	}

	// Método que define qual o nome do arquivo de acordo com a opção escolhida pelo
	// usuário
	public static String defineFile(String option) {
		String file = null;
		if (option == "a") {
			file = "Relatorio.csv";
		}
		if (option == "b") {
			file = "reservations.xlsx";
		}
		return file;
	}

	// Método que verifica se o arquivo existe e deleta se ele existir. Será
	// utilizado para limpar a pasta downloads antes e depois do download e de
	// popular o banco de dados
	public static void verifyAndDeleteFile(String option) {
		String path = definePath();
		String file = defineFile(option);
		File tempFile = new File(path + file);
		if (tempFile.exists()) {
			tempFile.delete();
		}
	}

}
