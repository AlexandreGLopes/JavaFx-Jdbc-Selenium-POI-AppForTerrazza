package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.entities.Costumer;

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

	// Método que lê o arquivo baixado do waitlist e instancia e adiciona uma lista
	// de objetos do tipo Costumer (clientes). Depois esta lista será retornada para
	// o programa principal para que este popule o banco de dados com ela
	public static List<Costumer> waitlistReaderInstantiator(String option)
			throws NumberFormatException, ParseException {
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
		String path = definePath();
		String file = defineFile(option);
		File tempFile = new File(path + file);
		List<Costumer> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {

			br.readLine();
			String line = br.readLine();

			while (line != null) {
				line = line.replace("\"", "");
				String[] fields = line.split(";");
				list.add(new Costumer(null, fields[0], fields[1], fields[4], fields[5], fields[7],
						Integer.parseInt(fields[8]), dt.parse(fields[9]), hr.parse(fields[10]), null, fields[13],
						null, fields[21]));
				line = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return list;
	}
}