package util;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumUtils {
	
	//Método para ser utilizado para economizar linhas de código.
	//Sempre que estivermos usando um sistema operacional diferente vir aqui e
	//modificar o chromedriver específico. Assim também só temos que modificar em um lugar
	public static WebDriver setPropertyByOs() {
		// Configurando o webdriver
		// Montando a String parâmetro do WebDriver de acordo com o sistema operacional utilizado
		// Chamando a classe estática que reconhece o sistema operacional e define qual o arquivo de driver a ser usado 
		String os = OSValidator.defineChromedriver();
		//Try cacth para definir o webdriver porque pode ser que dê erro se estiver sendo utilizado em um sistema para o qual não estamos destinando esta aplicação
		//Mas parece não estar funcionando o catch: ARRUMAR
		try {
			System.setProperty("webdriver.chrome.driver", "rs/" + os);
		} catch (IllegalStateException e) {
			System.out.println(os + "\n" + e);
		}
		
		// Instanciando e abrindo o browser
		WebDriver browser = new ChromeDriver();
		return browser;
	}
	
	//Método que faz o download do csv Waitlist
	public static void DownloadFromWaitlist() {
		//Configurando o WebDriver
		WebDriver browser = setPropertyByOs();
		
		//Descomentar abaixo no caso de precisar usar o método de abrir em uma nova tela
		//browser.switchTo().newWindow(WindowType.WINDOW);
		
		// Maximizando a p�gina para padronizar os elementos expostos e n�o perder
		// nenhum deles por conta de uma apresenta��o din�mica da p�gina
		browser.manage().window().maximize();
		// Estabelecendo o tempo de espera
		browser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		// Entrando na p�gina de relat�rio
		browser.get("https://waitlist.tagme.com.br/admin/reports");

		// Logando
		browser.findElement(By.name("email")).sendKeys("reserva.terrazza");
		browser.findElement(By.name("pass")).sendKeys("387576");
		browser.findElement(By.className("btn-primary")).click();

		// Escolhendo o relta�rio e apertando os bot�es para baixar
		browser.findElement(By.id("dropdownMenu1")).click();
		browser.findElement(By.linkText("Lista Geral de Reservas")).click();
		browser.findElement(By.className("btn-group")).click();
		browser.findElement(By.xpath("/html/body/div[1]/app-admin/div/div[2]/div/div[1]/div[2]/div[3]/div/button[2]"))
				.click();
		browser.findElement(By.xpath("/html/body/div[1]/app-admin/div/div[2]/div/div[1]/div[2]/div[3]/div/ul/li[3]/a"))
				.click();
		try {
			Thread.sleep(5000);
			browser.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// Método para instanciar e retornar um objeto webdriver que será o navegador.
	// Este navegador ficará aberto para poder passar manualmente pelo reCaptcha e
	// poderemos pegar o objeto novamente no futuro e usar o outro método de apertar
	// os botões
	public static WebDriver openToWIx() {
		//Configurando o WebDriver
		WebDriver browser2 = setPropertyByOs();
		// Maximizando a p�gina para padronizar os elementos expostos e n�o perder
		// nenhum deles por conta de uma apresenta��o din�mica da p�gina
		browser2.manage().window().maximize();
		// Estabelecendo o tempo de espera
		browser2.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		browser2.get("https://manage.wix.com");

		return browser2;
	}
	
	//Método que pega a página já aberta do navegador e procede com o download do xlsx do Wix
	public static void useWix(WebDriver browser2) {
		
		//Configurando a espera explícita para agauradr os elementos aparecerem
		WebDriverWait wait = new WebDriverWait(browser2, 20);
		//Verificando com um booleando se o elemento de botão "Hotéis" se encontra na tela
		if (browser2.findElement(By.xpath("/html/body/div[1]/div/div/div[1]/span/span/span/div/div/div[2]/div/div[1]/div[3]/a/button/span/span")).isDisplayed()) {
			//Evento de clique em hotéis
			browser2.findElement(By.xpath(
					"/html/body/div[1]/div/div/div[1]/span/span/span/div/div/div[2]/div/div[1]/div[3]/a/button/span/span"))
					.click();
			//Verificando se o iframe dos hotéis já foi carregado 
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"/html/body/div[1]/div/div/div[2]/div/div[1]/div/div/div/div/span/span/div[2]/div/iframe")));
			//Mudando para o iframe
			browser2.switchTo().frame("etpaContainer");
		}
		
		//eventos de clique até o download
		browser2.findElement(By.xpath("/html/body/div[2]/div/div[1]/ul/li[2]/a/span")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"/html/body/div[2]/div/div[2]/div[1]/div/div[2]/div[2]/div/md-input-container/md-select")))
				.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"/html/body/div[4]/md-select-menu/md-content/md-option[2]/div[1]/span")))
				.click();
		//Voltando para a página principal
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"/html/body/div[2]/div/div[1]/wix-hotels-logo/div/a/span")))
				.click();
		//Na página principal saindo do iframe e voltando ao html principal
		browser2.switchTo().defaultContent();
		
	}

}
