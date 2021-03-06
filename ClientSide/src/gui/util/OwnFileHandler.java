package gui.util;

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
import java.util.prefs.Preferences;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.scene.control.Alert.AlertType;
import model.entities.Costumer;

public class OwnFileHandler {

	private static Logger logger = LogManager.getLogger(OwnFileHandler.class);

	static Preferences preferences = PreferencesManager.getPreferences();

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

	// Método que lê o arquivo XLS baixado do waitlist e instancia e adiciona uma
	// lista
	// de objetos do tipo Costumer (clientes). Depois esta lista será retornada para
	// o programa principal para que este popule o banco de dados com ela
	public static List<Costumer> waitlistXlsReaderInstantiator(String absolutePath) throws Exception {
		// SImpleDateFormat para formatar os padrões das datas que vão entrar nas
		// strings
		SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		// Definição do path do arquivo a ser lido
		File tempFile = new File(absolutePath);
		// Lista de Costumers que vamos retornar
		List<Costumer> list = new ArrayList<>();

		try {
			// O primeiro passo é abrir o arquivo, com a classe FileInputStream, passando o
			// PATH completo do arquivo.
			FileInputStream arquivo = new FileInputStream(tempFile);
			// Ajustando a configuração para contornar o Zip Bomb
			ZipSecureFile.setMinInflateRatio(0);
			// Depois utilizando a classe HSSFWorkbook, o arquivo é validado se é ou não um
			// arquivo Excel
			HSSFWorkbook workbook = new HSSFWorkbook(arquivo);
			// A classe HSSFSheet abre uma planilha específica do arquivo
			HSSFSheet sheetReservations = workbook.getSheetAt(0);
			// Depois de aberto o arquivo, e com a planilha que será processado aberta, é
			// necessário ler célula a célula do arquivo, para isso, é recuperado um
			// iterator sobre todas as linhas do arquivo excel.
			Iterator<Row> rowIterator = sheetReservations.iterator();
			// Lendo a primeira linha porque ela é cabeçalho
			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				// Dentro de cada linha, é recuperado outro iterator, agora para iterar sobre as
				// colunas de cada linha. Para ler as linhas do arquivo, é utilizada a classe
				// Row, e para a célula especifica é utilizada a classe Cell.
				Iterator<Cell> cellIterator = row.cellIterator();
				// Instanciando um costumer para receber os valores das células
				Costumer costumer = new Costumer();
				// adicionando o costumer vazio na lista
				list.add(costumer);
				costumer.setAguardando(false);
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// A classe Cell possui diversos métodos para a manipulação dos dados do
					// arquivo, por exemplo, é possível recuperar os dados que estão na célula com o
					// tipo Java correto, por isso existem métodos para recuperar String, Números,
					// Booleans entre outros.
					// Switch/case para verificar qual o número da célula que o iterador está e usar
					// o método set do Costumer para adicionar os parâmetros
					switch (cell.getColumnIndex()) {
					case 0:
						costumer.setNome(cell.getStringCellValue());
						break;
					case 1:
						costumer.setSobrenome(cell.getStringCellValue());
						break;
					case 4:
						costumer.setTelefone(String.valueOf(cell.getNumericCellValue()));
						break;
					case 5:
						costumer.setEmail(cell.getStringCellValue());
						break;
					case 7:
						costumer.setSalao(cell.getStringCellValue());
						break;
					case 8:
						int pessoas = (int) cell.getNumericCellValue();
						costumer.setPessoas(pessoas);
						break;
					case 9:
						costumer.setData(dt.parse(cell.getStringCellValue()));
						break;
					case 10:
						costumer.setHora(hr.parse(cell.getStringCellValue()));
						break;
					case 13:
						costumer.setSituacao(cell.getStringCellValue());
						break;
					case 15:
						costumer.setObservacao(cell.getStringCellValue());
						break;
					case 17:
						costumer.setMesa(cell.getStringCellValue());
						;
						break;
					case 20:
						costumer.setIdExterno(cell.getStringCellValue());
						costumer.setPagamento(0.00);
						break;
					default:
						break;
					}
				}
			}
			workbook.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
		// retornando a lista
		return list;
	}

	// Método que lê o arquivo CSV baixado do waitlist e instancia e adiciona uma
	// lista
	// de objetos do tipo Costumer (clientes). Depois esta lista será retornada para
	// o programa principal para que este popule o banco de dados com ela
	public static List<Costumer> waitlistCsvReaderInstantiator(String absolutePath)
			throws NumberFormatException, ParseException {
		// SImpleDateFormat para formatar os padrões das datas que vão entrar nas
		// strings
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
		// Definição do path do arquivo a ser lido
		File tempFile = new File(absolutePath);
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
				// Formando um array dividindo a linha de acordo com o sepradaor definido nas
				// prefereências
				String separador = preferences.get(PreferencesManager.SEPARADOR_DO_CSV, null);
				String[] fields = line.split(separador);
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
				list.add(new Costumer(null, array[0], array[1], array[4], array[5], array[7],
						Integer.parseInt(array[8]), dt.parse(array[9]), hr.parse(array[10]), array[17], array[13],
						array[15], false, 0.00, array[20]));
				line = br.readLine();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	// Método para deixar menos verboso o waitlistReaderInstantiator()
	// Este método será utilizado caso o tamanho do array dividido seja menor que 21
	// elementos
	// Isso vai ocorrer se tiver alguma quebra de linha indesejada no arquivo csv
	// Neste caso a linha seguinte vai conter o restante dos elementos e vai somar
	// 21 no total.
	// Neste sentido, vamos juntar os elementos de uma leitura de linha na próxima e
	// fazer um array com elas
	public static String[] ifFieldsLengthMinorThan21(String line, String[] fields, BufferedReader br)
			throws IOException {
		// Fazendo um array de 21 posições que será retornado após receber a junção das
		// leituras de linhas. Teremos que juntar as leituras de linha em diferentes
		// vetores e listas para poder trabalhar com eles e ir montando este Array final
		String[] array = new String[21];
		// pegando qual é o separador definido nas preferências
		String separador = preferences.get(PreferencesManager.SEPARADOR_DO_CSV, null);
		// A lista abaixo vai receber os valores de um vetor temporário (temp).
		// Utilizamos ela para juntar todas as leituras de linha da observação da
		// reserva que tem as quebras de linha, e que, a princípio, entrarão no vetor
		// temp.
		List<String> fields2 = new ArrayList<String>();
		// Esse booleano vai marcar se o loop deve continuar
		boolean loopDeLeitura = true;
		// loop que vai ler a linha seguinte e jogar no vetor temporário e depois na
		// lista
		while (loopDeLeitura) {
			// Marcando o bufferedReader para poder voltar à mesma posição. Teremos que
			// voltar porque quando o loop for marcado para finalizar teremos que ler a
			// linha novamente e joga-la em outro vetor
			br.mark(0);
			// lendo a próxima linha
			line = br.readLine();
			line = line.replace("\"", "");
			// vetor temporário que vai receber a linha dividida pelo separador
			String[] temp = line.split(separador);
			// Utilizamos um vetor temporário porque usando o método .length sabemos que a
			// divisão resultou em apenas um ou vários termos. Se, neste caso, resultar em
			// apenas 1 termo ainda há mais quebras de linha para serem lidas, e o loop deve
			// continuar (iremos para o else). Se resultar em MAIS de um 1 termo quer dizer
			// que atingimos a última linha da reserva no CSV.
			if (temp.length > 1) {
				// neste caso setamos o loop para terminar
				loopDeLeitura = false;
				// E resetamos o BufferedReader. Ou seja, voltamos à linha marcada no br.mark()
				br.reset();
			} else {
				// No caso de ser apenas 1 termo (ou menos, mas sempre será um termo, a
				// principio) vamos adicionar os termos do temp na lista fields2
				for (String str : temp) {
					fields2.add(str);
				}
			}
		}
		// Em seguida vamos ler novamente a última linha da reserva no CSV e colocá-la
		// num outro vetor chamado de fields3 e vamos dividir a linha de acordo com o
		// separador das preferências
		line = br.readLine();
		line = line.replace("\"", "");
		String[] fields3 = line.split(separador);
		// Agora vamos percorrer cada termo da lista fields2 e vamos adicionar cada um
		// desses termos na última posição do array fields (que foi passado como
		// parâmetro para este método)
		for (String str : fields2) {
			// No caso aqui "fields.length - 1" significa a última posição pois o termo
			// começa no zero, mas o tamanho do array é contado a partir do 1.
			fields[fields.length - 1] = fields[fields.length - 1] + ". " + str;
		}
		// O Array que será retornado vai receber agora cada um dos termos do fields
		for (int i = 0; i < fields.length; i++) {
			array[i] = fields[i];
		}
		// Juntando o conteúdo da primeira posição do Fields3 no conteúdo da última
		// posião do Array. Ou seja, aqui estamos juntando o final da observação que
		// será lido no último br.readLine que vai dar conta de ler o final da
		// observação e o restante dos dados da reserva
		array[fields.length - 1] = array[fields.length - 1] + fields3[0];
		// depois de adicionar o primeiro termo do fields3, para cada termo no fields3
		// (após o primeiro) vamos adicionar este termo na próxima posição vazia do
		// array. Sabemos qual é a próxima posição vazia do array porque ela é a última
		// posição do fields mais 1, e depois que esta estiver ocupada será a próxima
		for (int i = 1; i < fields3.length; i++) {
			array[fields.length - 1 + i] = fields3[i];
		}
		return array;
	}

	public static List<Costumer> wixReaderInstantiator(String absolutePath) {
		// SImpleDateFormat para formatar os padrões das datas que vão entrar nas
		// strings
		SimpleDateFormat hr = new SimpleDateFormat("HH:mm");
		// Definição do path do arquivo a ser lido
		File tempFile = new File(absolutePath);
		// Lista de Costumers que vamos retornar
		List<Costumer> list = new ArrayList<>();

		try {
			// O primeiro passo é abrir o arquivo, com a classe FileInputStream, passando o
			// PATH completo do arquivo.
			FileInputStream arquivo = new FileInputStream(tempFile);
			// Ajustando a configuração para contornar o Zip Bomb
			ZipSecureFile.setMinInflateRatio(0);
			// Depois utilizando a classe XSSFWorkbook, o arquivo é validado se é ou não um
			// arquivo Excel
			XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
			// A classe XSSFSheet abre uma planilha específica do arquivo
			XSSFSheet sheetReservations = workbook.getSheetAt(0);
			// Depois de aberto o arquivo, e com a planilha que será processado aberta, é
			// necessário ler célula a célula do arquivo, para isso, é recuperado um
			// iterator sobre todas as linhas do arquivo excel.
			Iterator<Row> rowIterator = sheetReservations.iterator();
			// Lendo a primeira linha porque ela é cabeçalho
			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				// Dentro de cada linha, é recuperado outro iterator, agora para iterar sobre as
				// colunas de cada linha. Para ler as linhas do arquivo, é utilizada a classe
				// Row, e para a célula especifica é utilizada a classe Cell.
				Iterator<Cell> cellIterator = row.cellIterator();
				// Instanciando um costumer para receber os valores das células
				Costumer costumer = new Costumer();
				// adicionando o costumer vazio na lista
				list.add(costumer);
				costumer.setAguardando(false);
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// A classe Cell possui diversos métodos para a manipulação dos dados do
					// arquivo, por exemplo, é possível recuperar os dados que estão na célula com o
					// tipo Java correto, por isso existem métodos para recuperar String, Números,
					// Booleans entre outros.
					// Switch/case para verificar qual o número da célula que o iterador está e usar
					// o método set do Costumer para adicionar os parâmetros
					switch (cell.getColumnIndex()) {
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
						// Vários praâmetros que não temos na planilha por padrão serão adicionados no
						// hardcod aqui
						costumer.setTelefone("Wix não exporta");
						costumer.setSalao("38 Floor");
						costumer.setPessoas(2);
						costumer.setSituacao("Novo");
						costumer.setObservacao("");
						String strInicial = cell.getStringCellValue()
								.substring(cell.getStringCellValue().lastIndexOf("(") + 1);
						String strFinal = strInicial.substring(0, 5);
						try {
							costumer.setHora(hr.parse(strFinal));
						} catch (Exception e) {
							// System.out.println("não deu");
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
			workbook.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			Alerts.showAlert("Erro ao ler arquivo", null,
					"Erro inesperado ao tentar ler aquivo XLSX.\n\nCódigo do erro: " + e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
		// Pegando a data atual do sistema e colocando na variável today
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		Date currDate = new Date();
		Date today = null;
		try {
			today = dt.parse(dt.format(currDate));
		} catch (ParseException e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		}
		// Instanciando um iterator para poder percorrer a lista e poder remover os
		// costumers com data diferente da atual
		Iterator<Costumer> i = list.iterator();
		while (i.hasNext()) {
			// Lendo o próximo costumer
			Costumer c = i.next();
			// Método para comparar as data do costumer com a atual
			if (c.getData().compareTo(today) != 0) {
				// Se for diferente remover
				i.remove();
			}
		}
		// retornando a lista
		return list;
	}
}