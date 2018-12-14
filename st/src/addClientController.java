import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by JUV on 04/09/2018.
 */
public class addClientController {
    @FXML
    private Button ajouter;
    @FXML
    private  Button retour;
    @FXML
    AnchorPane anchorPane2;
    @FXML
    TextField textField;
    @FXML
    Label label;

    public void addPressed(ActionEvent event) throws Exception {
        if(!textField.getText().isEmpty())
        {
            try {Client client=new Client();
            client.ClientName=textField.getText();
            client.ajouterClient();
                AnchorPane pane = FXMLLoader.load(getClass().getResource("first.fxml"));
                Main main=new Main();
                main.getStage().setScene(new Scene(pane));
            }
            catch (SQLIntegrityConstraintViolationException e){
                label.setText("Client déja existant !");

        }


        }
        else label.setText("Entrer le nom du client !");


    }
    public void cancelPressed(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("first.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));

    }
}
