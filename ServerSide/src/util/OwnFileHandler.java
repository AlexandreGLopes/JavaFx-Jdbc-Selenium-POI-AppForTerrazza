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
		// SImpleDateFormat para formatar os padrões das datas que vão entrar nas
		// strings
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
		// Definição do path do arquivo a ser lido
		String path = definePath();
		String file = defineFile(option);
		File tempFile = new File(path + file);
		// Lista de Costumers que vai receber os objetos para ser retornada no final do
		// método
		List<Costumer> list = new ArrayList<>();
		// Inicio do buffered reader
		try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {

			// Lendo a primeira linha e não usando porque ela é cabeçalho
			br.readLine();
			// Lendo a próxima linha e jogando na String com a qual vamos trabalhar
			String line = br.readLine();
			// O array abaixo vai receber o final do split e sempre vai ter que conter 21
			// elementos
			String[] array = new String[21];
			// Loop que vai ler até o final
			while (line != null) {
				// Removendo aspas indesejadas da linha
				line = line.replace("\"", "");
				// Formando um array dividindo a linha nos ";"
				String[] fields = line.split(";");
				// Se o array acima, lido de uma linha específica no meio do loop,
				// ficar menor que 21 elementos é porque temos uma quebra de linha não esperada
				// e temos que trabalhar ela
				if (fields.length < 21) {
					// Caso verdadeiro o array acima vai receber o resultado do método que junta as
					// linhas, no caso de uma quebra de linha inesperada
					array = ifFieldsLengthMinorThan21(line, fields, br);
				}
				// Se não, só vamos proceder adicionando o array fields no array que vamos usar
				// para instanciar os Costumers
				else {
					array = fields;
				}
				list.add(
						new Costumer(null, array[0], array[1], array[4], array[5], array[7], Integer.parseInt(array[8]),
								dt.parse(array[9]), hr.parse(array[10]), array[17], array[13], 0.00, array[20]));
				line = br.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		return list;
	}

	// Método para deixar menos verboso o waitlistReaderInstantiator()
	// Este método será utilizado caso o tamanho do array dividido seja menor que 21
	// elementos
	// Isso vai ocorrer se tiver alguma quebra de linha indesejada no arquivo csv
	// Neste caso a linha seguinte vai conter o restante dos elementos e vai somar
	// 21 no total.
	// Neste sentido, vaos juntar os elementos de uma leitura de linha na próxima e
	// fazer um array com elas
	public static String[] ifFieldsLengthMinorThan21(String line, String[] fields, BufferedReader br)
			throws IOException {
		String[] array = new String[21];
		line = br.readLine();
		line = line.replace("\"", "");
		String[] fields2 = line.split(";");
		for (int i = 0; i < fields.length; i++) {
			array[i] = fields[i];
		}
		for (int i = 1; i < fields2.length; i++) {
			array[fields.length - 1 + i] = fields2[i];
		}
		return array;
	}
}