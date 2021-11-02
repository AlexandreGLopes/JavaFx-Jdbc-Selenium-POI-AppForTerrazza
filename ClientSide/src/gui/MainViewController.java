package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {
	
	//Socket que vai ser utilizado nos vários métodos para conversar com o servidor
	private Socket cliente;
	
	//PrintWriter que vai ser utilizado pelos vários métodos  e vai passar o argumento para o switch case
	private PrintWriter pr;
	
	@FXML
	private MenuItem menuItemRefreshFromWaitlist;
	
	@FXML
	private MenuItem menuItemRefreshFromWix;
	
	@FXML
	public void onMenuItemRefreshFromWaitlistAction() {
		try {
            cliente = new Socket("localhost",3322);
            pr = new PrintWriter(cliente.getOutputStream());
            pr.println("a");
            pr.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	@FXML
	public void onMenuItemRefreshFromWixAction() {
		try {
            cliente = new Socket("localhost",3322);
            pr = new PrintWriter(cliente.getOutputStream());
            pr.println("b");
            pr.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}

}
