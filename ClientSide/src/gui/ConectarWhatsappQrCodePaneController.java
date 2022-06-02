package gui;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import gui.util.Alerts;
import gui.util.MyZapHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ConectarWhatsappQrCodePaneController implements Initializable {

    private Logger logger = LogManager.getLogger(LoadingScreenController.class);

    @FXML
	private Button conectarButton;

    @FXML
	private ImageView qrCode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void onConectarButtonAction () {
        HttpResponse connectResponse = MyZapHandler.connect();
        // criando um objeto JSON e colocando dentro dele o Entity da mensagem da Api passada para string
        JSONObject album = null;
        try {
            album  = new JSONObject(EntityUtils.toString(connectResponse.getEntity(), "UTF-8"));
            // Fazendo uma objeto com as keys e valores que estiverem dentro da key "qr_code"
            JSONObject messageJSON = album.getJSONObject("qr_code");
            // Dentro do objeto pegando o valor da string que tiver dentro da key base64Qr
            String qrCodeBase64 = messageJSON.getString("base64Qr");
            // retirando o que tiver antes da vírgula da string e deixando só o código em base 64
            String[] base64Split = qrCodeBase64.split(",");
            qrCodeBase64 = base64Split[1];
            // transformando a string num array de bytes
            byte[] imageByte = Base64.getDecoder().decode(qrCodeBase64);
            // criando uma nova imagem usando o inputstream do array de bytes e passando a variavel de array de bytes criada acima
            Image img = new Image(new ByteArrayInputStream(imageByte));
            // setando a imagem do qrCode na view
            qrCode.setImage(img);
            
        } catch (Exception e) {
            //Se o erro for ocasionado porque a resposta da API foi de que já tem um número conectado à sessão
            if (album.getBoolean("connected") == true) {
                Alerts.showAlert("Whatsapp já conectado", null,
                        "Já existe um número de Whatsapp conectado a essa sessão da API.",
                        AlertType.INFORMATION);
            } else {
                // Se o erro for outro mandar mensagem padrão e imprimir o erro
                Alerts.showAlert("Erro ao tentar Conectar", null,
                        "Houve um erro ao tentar conectar com a Api do WhatsApp.\nContate o desenvolvedor para saber mais.\n\n"
                        + e.getMessage(),
                        AlertType.ERROR);
            }
            logger.error(e.getMessage());
        }
    }
}
