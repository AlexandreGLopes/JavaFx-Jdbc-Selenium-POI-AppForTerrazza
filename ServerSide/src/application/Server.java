package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import util.SeleniumUtils;

public class Server {

	public static void main(String[] args) {
		try {
			//Instanciando o socket para receber a conexão
			ServerSocket server = new ServerSocket(3322);
			System.out.println("Servidor iniciado na porta 3322");
			
			//Iniciando um loop para aceitar uma nova conexão toda vez que a conexão anterior for fechada
			int continuar = 1;
			do {
			Socket cliente = server.accept();
			System.out.println("Cliente conectado do IP " + cliente.getInetAddress().getHostAddress());
			//Chamando o método estático da classe que faz os downloads via Selenium
			SeleniumUtils.DownloadFromWiatlist();
			//Fechando conexão
			cliente.close();
			} while (continuar == 1);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
