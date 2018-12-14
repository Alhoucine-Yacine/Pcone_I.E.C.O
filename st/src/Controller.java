import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

public class Controller {
    @FXML
    private Button client;
    @FXML
    private Button commande;
    @FXML
    private Button optimal;
    @FXML
    private Button combiner;
    @FXML
    private Button liste;
    @FXML
    private Button deleteC;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button historique;

    @FXML
    private AnchorPane anchorPane1;
    @FXML
    private Button retour;
    @FXML
    private Button lggin;
    @FXML
    private TextField user;
    @FXML
    private PasswordField psw;
    @FXML
    private Label label;

    public void clientPressed(ActionEvent event) throws IOException {
        AnchorPane pane =FXMLLoader.load(getClass().getResource("newclient.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));


    }

    public void commandePressed(ActionEvent event) throws IOException {
        AnchorPane pane =FXMLLoader.load(getClass().getResource("commande.fxml"));
        Main main= new Main();
        main.getStage().setScene(new Scene(pane));


    }

    public void statPressed(ActionEvent event) throws IOException {
        AnchorPane pane =FXMLLoader.load(getClass().getResource("stat.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));


    }

    public void optimPressed(ActionEvent event) throws IOException {
        AnchorPane pane =FXMLLoader.load(getClass().getResource("optim.fxml"));
        //anchorPane.getChildren().setAll(pane);
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));


    }

    public void listePressed(ActionEvent event) throws IOException {
        AnchorPane pane =FXMLLoader.load(getClass().getResource("listeCommandes.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));


    }

    public void deletePressed(ActionEvent event) throws IOException {
        AnchorPane pane =FXMLLoader.load(getClass().getResource("deleteC.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));


    }

    public  void combPressed (ActionEvent event) throws  IOException{
        AnchorPane pane= FXMLLoader.load(getClass().getResource("comb1.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));
    }

    public void historiquePressed (ActionEvent event) throws IOException{
        AnchorPane pane= FXMLLoader.load(getClass().getResource("historique.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));
    }

    public void loginPressed (ActionEvent event) throws IOException {
        if (!user.getText().isEmpty()) {
            try {
                Main main= new Main();
                main.user=user.getText();
                main.setPsw(psw.getText());
                Connection connection=Main.getConnection(Main.user,Main.getPsw());
                AnchorPane pane = FXMLLoader.load(getClass().getResource("first.fxml"));
                main.getStage().setScene(new Scene(pane));
            } catch (java.sql.SQLNonTransientConnectionException e) {
                label.setText("Nom d'utilisateur ou mot de passe non valide !");

            }
            catch (java.sql.SQLSyntaxErrorException e){
                label.setText("Base de données non trouvée !");

            }
            catch (Exception e){
                e.printStackTrace();
                label.setText("Problème de connexion avec la base de données !");
                PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
                writer.println((e.getMessage()));
                writer.close();

            }
        }
    }

    public void quitPressed (ActionEvent event) throws IOException {
        Main main=new Main();
        main.close(main.stage);
    }
}
