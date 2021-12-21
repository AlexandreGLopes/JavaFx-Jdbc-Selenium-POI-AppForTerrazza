package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
								dt.parse(array[9]), hr.parse(array[10]), array[17], array[13], array[15], false, 0.00, array[20]));
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

	public static List<Costumer> wixReaderInstantiator(String option) {
		// SImpleDateFormat para formatar os padrões das datas que vão entrar nas
		// strings
		SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
		// Definição do path do arquivo a ser lido
		String path = definePath();
		String file = defineFile(option);
		File tempFile = new File(path + file);
		//Lista de Costumers que vamos retornar
		List<Costumer> list = new ArrayList<>();
		
		try {
			//O primeiro passo é abrir o arquivo, com a classe FileInputStream, passando o PATH completo do arquivo.
			FileInputStream arquivo = new FileInputStream(tempFile);
			//Ajustando a configuração para contornar o Zip Bomb
			ZipSecureFile.setMinInflateRatio(0);
			//Depois utilizando a classe XSSFWorkbook, o arquivo é validado se é ou não um arquivo Excel
			XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
			//A classe XSSFSheet abre uma planilha específica do arquivo
			XSSFSheet sheetReservations = workbook.getSheetAt(0);
			//Depois de aberto o arquivo, e com a planilha que será processado aberta, é necessário ler célula a célula do arquivo, para isso, é recuperado um iterator sobre todas as linhas do arquivo excel. 
			Iterator<Row> rowIterator = sheetReservations.iterator();
			//Lendo a primeira linha porque ela é cabeçalho
			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				//Dentro de cada linha, é recuperado outro iterator, agora para iterar sobre as colunas de cada linha. Para ler as linhas do arquivo, é utilizada a classe Row, e para a célula especifica é utilizada a classe Cell. 
				Iterator<Cell> cellIterator = row.cellIterator();
				//Instanciando um costumer para receber os valores das células
				Costumer costumer = new Costumer();
				//adicionando o costumer vazio na lista
				list.add(costumer);
				costumer.setAguardando(false);
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					//A classe Cell possui diversos métodos para a manipulação dos dados do arquivo, por exemplo, é possível recuperar os dados que estão na célula com o tipo Java correto, por isso existem métodos para recuperar String, Números, Booleans entre outros.
					//Switch/case para verificar qual o número da célula que o iterador está e usar o método set do Costumer para adicionar os parâmetros
					switch(cell.getColumnIndex()) {
					case 0:
						costumer.setIdExterno(cell.getStringCellValue());
						break;
					case 1:
						costumer.setNome(cell.getStringCellValue());
						break;
					case 2:
						costumer.setSobrenome(cell.getStringCellValue());
						break;
					case 3:
						costumer.setEmail(cell.getStringCellValue());
						break;
					case 5:
						costumer.setData(cell.getDateCellValue());
						break;
					case 7:
						costumer.setMesa(cell.getStringCellValue());
						//Vários praâmetros que não temos na planilha por padrão serão adicionados no hardcod aqui
						costumer.setTelefone("Wix não exporta");;
						costumer.setSalao("38 Floor");
						costumer.setPessoas(2);
						costumer.setSituacao("Novo");
						costumer.setObservacao("");
						String strInicial = cell.getStringCellValue().substring(cell.getStringCellValue().lastIndexOf("(") + 1);
						String strFinal = strInicial.substring(0, 5);
						try {
						costumer.setHora(hr.parse(strFinal));
						}catch (Exception e) {
							//System.out.println("não deu");
						}
						break;
					case 10:
						costumer.setPagamento(cell.getNumericCellValue());
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
		//Pegando a data atual do sistema e colocando na variável today
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		Date currDate = new Date();
		Date today = null;
		try {
			today = dt.parse(dt.format(currDate));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		//Instanciando um iterator para poder percorrer a lista e poder remover os costumers com data diferente da atual
		Iterator<Costumer> i = list.iterator();
		while (i.hasNext()) {
			//Lendo o próximo costumer
			Costumer c = i.next();
			//Método para comparar as data do costumer com a atual
			if (c.getData().compareTo(today) != 0) {
				//Se for diferente remover
				i.remove();
			}
		}
		//retornando a lista
		return list;
	}
}