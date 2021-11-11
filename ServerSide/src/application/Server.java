package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.openqa.selenium.WebDriver;

import util.OwnFileHandler;
import util.SeleniumUtils;

public class Server {

	public static void main(String[] args) {
		try {
			// Instanciando o socket para receber a conexão
			ServerSocket server = new ServerSocket(3322);
			System.out.println("Servidor iniciado na porta 3322");

			// Abrindo um navegador ao iniciar o servidor. Usaremos este para abrir o Wix
			// Pois o wix tem Recaptcha e vamos deixar aberto para não cair na verificação
			// do robô
			WebDriver browser2 = SeleniumUtils.openToWIx();
			// Se precisar saber qual é o identificador da janela do browser acima
			// descomentar abaixo
			// System.out.println(browser2.getWindowHandle());

			// Iniciando um loop para aceitar uma nova conexão toda vez que a conexão
			// anterior for fechada
			int continuar = 1;
			do {
				Socket cliente = server.accept();
				System.out.println("Cliente conectado do IP " + cliente.getInetAddress().getHostAddress());

				// Instâncias que recebem o stream do cliente e vão convertê-lo em string para
				// usar no switch/case
				InputStreamReader in = new InputStreamReader(cliente.getInputStream());
				BufferedReader bf = new BufferedReader(in);
				String option = bf.readLine();

				// Printwriter que será utilzado dentro do switch/case
				PrintWriter pr;
				// Chamando o método estático da classe que faz os downloads via Selenium
				// Usando switch case para escolher qual método usar
				switch (option) {
				case "a":
					// verificando se ficou algum arquivo antigo baixado para trás e deletando se
					// existir
					OwnFileHandler.verifyAndDeleteFile("a");
					//Fazendo o download do arquivo do Terrazza 40
					SeleniumUtils.DownloadFromWaitlist();
					
					//verificando e excluindo o arquivo de download
					OwnFileHandler.verifyAndDeleteFile("a");
					//Comunicando com o cliente para mostrar que as funções aqui finalizaram
					pr = new PrintWriter(cliente.getOutputStream());
					pr.println("c");
					pr.flush();
					break;
				case "b":
					// verificando se ficou algum arquivo antigo baixado para trás e deletando se
					// existir
					OwnFileHandler.verifyAndDeleteFile("b");
					//Fazendo o download do arquivo do 38 Floor
					SeleniumUtils.useWix(browser2);
					
					//verificando e excluindo o arquivo de download
					OwnFileHandler.verifyAndDeleteFile("b");
					//Comunicando com o cliente para mostrar que as funções aqui finalizaram
					pr = new PrintWriter(cliente.getOutputStream());
					pr.println("c");
					pr.flush();
					break;
				default:
					System.out.println("no valid option");
				}

				// Fechando conexão
				cliente.close();
			} while (continuar == 1);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
