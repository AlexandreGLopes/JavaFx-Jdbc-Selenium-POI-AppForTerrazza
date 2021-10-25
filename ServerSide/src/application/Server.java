package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Server {

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(3322);
			System.out.println("Servidor iniciado na porta 3322");

			Socket cliente = server.accept();
			System.out.println("Cliente conectado do IP " + cliente.getInetAddress().getHostAddress());
			// Configurando o webdriver
			System.setProperty("webdriver.chrome.driver", "res/chromedriver.exe");
			// Instanciando e abrindo o browser
			WebDriver browser = new ChromeDriver();
			// Maximizando a página para padronizar os elementos expostos e não perder
			// nenhum deles por conta de uma apresentação dinâmica da página
			browser.manage().window().maximize();
			// Estabelecendo o tempo de espera
			browser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

			// Entrando na página de relatório
			browser.get("https://waitlist.tagme.com.br/admin/reports");

			// Logando
			browser.findElement(By.name("email")).sendKeys("reserva.terrazza");
			browser.findElement(By.name("pass")).sendKeys("387576");
			browser.findElement(By.className("btn-primary")).click();

			//Escolhendo o reltaório e apertando os botões para baixar
			browser.findElement(By.id("dropdownMenu1")).click();
			browser.findElement(By.linkText("Lista Geral de Reservas")).click();
			browser.findElement(By.className("btn-group")).click();
			browser.findElement(By.xpath("/html/body/div[1]/app-admin/div/div[2]/div/div[1]/div[2]/div[3]/div/button[2]")).click();
			browser.findElement(By.xpath("/html/body/div[1]/app-admin/div/div[2]/div/div[1]/div[2]/div[3]/div/ul/li[3]/a")).click();
			Scanner entrada = new Scanner(cliente.getInputStream());
			while (entrada.hasNextLine()) {
				System.out.println(entrada.nextLine());
			}

			entrada.close();
			server.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
