import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Created by JUV on 05/09/2018.
 */
public class addCommandeController implements Initializable{
@FXML
    AnchorPane anchorPane;
@FXML
    Button ajouter ;
@FXML
    Button annuler;
@FXML
    TextField client;
@FXML
    RadioButton simple;
@FXML
    RadioButton doublee;
@FXML
    RadioButton blanc;

@FXML
    RadioButton tl;

@FXML
    RadioButton kraft;
@FXML
    TextField  ciG;
@FXML
    TextField firstCanG;
@FXML
    TextField ceG;
@FXML
    TextField secCanG;
@FXML
    TextField medG;
@FXML
    TextField longueur;
@FXML
    TextField largeur;
@FXML
    TextField nbPlaues;
@FXML
    TextField delai;
@FXML
    Label label;
@FXML
    TextField nomCommande;
@FXML
    ComboBox epI;
@FXML
    ComboBox epII;


    public void cancelPressed(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("first.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));

    }

    public void num (javafx.scene.input.KeyEvent evt){
        String c=evt.getCharacter();
        char character =c.charAt(0);
        if (!Character.isDigit(character)) evt.consume();


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        simple.setSelected(true);
        blanc.setSelected(true);
        secCanG.setDisable(true);
        medG.setDisable(true);
        epII.setDisable(true);
        epI.getItems().removeAll("Item 1","Item 2","Item 3");
        epII.getItems().removeAll("Item 1","Item 2","Item 3");
        epI.getItems().addAll("Micro Cannelure","Petite Cannelure","Grande Cannelure");
        epII.getItems().addAll("Micro Cannelure","Petite Cannelure","Grande Cannelure");
        epI.setValue("Petite Cannelure");
        epII.setValue("Petite Cannelure");
            }

    public void uncheckTypeForsimple(){

        doublee.setSelected(false);
        simple.setSelected(true);
        secCanG.setDisable(true);
        medG.setDisable(true);
        epII.setDisable(true);

    }

    public void uncheckTypeForDouble(){
        doublee.setSelected(true);
        simple.setSelected(false);
        secCanG.setDisable(false);
        medG.setDisable(false);
        epII.setDisable(false);
    }
    public void uncheckTypeForBlanc(){
        tl.setSelected(false);
        kraft.setSelected(false);
        blanc.setSelected(true);
    }

    public void uncheckTypeForTl(){
        tl.setSelected(true);
        kraft.setSelected(false);
        blanc.setSelected(false);

    }

    public void uncheckTypeForKraft(){
        tl.setSelected(false);
        kraft.setSelected(true);
        blanc.setSelected(false);

    }

    public void ajouterPressed() throws Exception {
        if (!client.getText().isEmpty()){
            if(!longueur.getText().isEmpty() && !largeur.getText().isEmpty()){
                if (!nbPlaues.getText().isEmpty()){
                        if (!ciG.getText().isEmpty() && !ceG.getText().isEmpty() && !firstCanG.getText().isEmpty() && (simple.isSelected() || !secCanG.getText().isEmpty() && !medG.getText().isEmpty())) {
                            if (!nomCommande.getText().isEmpty()) {


                                Commande commande = new Commande();
                                commande.client = client.getText();
                                if (simple.isSelected()) commande.typeCommande = 3;
                                else commande.typeCommande = 5;
                                if (blanc.isSelected()) commande.ceType = "blanc";
                                else if (tl.isSelected()) commande.ceType = "TestLiner";
                                else commande.ceType = "Kraft";
                                commande.ciG = Integer.parseInt(ciG.getText());
                                commande.firstCannG = Integer.parseInt(firstCanG.getText());
                                commande.ceG = Integer.parseInt(ceG.getText());
                                commande.epI= (String) epI.getValue();
                                if (doublee.isSelected()) {
                                    commande.epII= (String) epII.getValue();
                                    commande.secCannG = Integer.parseInt(secCanG.getText());
                                    commande.medG = Integer.parseInt(medG.getText());
                                }
                                commande.longueur = Integer.parseInt(longueur.getText());
                                commande.largeur = Integer.parseInt(largeur.getText());
                                commande.nbPlaques = Integer.parseInt(nbPlaues.getText());
                                int year = Calendar.getInstance().get(Calendar.YEAR);
                                int month = ZonedDateTime.now(  ZoneId.of( "Africa/Algiers" )  ).getMonthValue();
                                //int month=Calendar.getInstance().get(Calendar.MONTH);
                                int day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                                String strDate = day + "-" + month + "-" + year;
                                commande.dateCommande = strDate;
                                commande.nomCommande=nomCommande.getText();
                                if (!delai.getText().isEmpty()) commande.dureeMax = Integer.parseInt(delai.getText());
                                else commande.dureeMax = 0;
                                commande.etatCommande = 1;
                                Client client1 = new Client();
                                client1.ClientName = client.getText();
                                client1.nbCommandes = 1;
                                try {
                                    client1.ajouterClient();
                                } catch (Exception e) {
                                    client1.addCommande();
                                }

                                commande.ajouterCommande();

                                AnchorPane pane = FXMLLoader.load(getClass().getResource("first.fxml"));
                                Main main=new Main();
                                main.getStage().setScene(new Scene(pane));
                            }
                            else label.setText("Entrer le nom du produit !");
                        }// end of grammages
                        else label.setText("Entrer les grammages !");
                }// end of is empty nbplaquses
                else label.setText("Entrer le nombre de plaques !");
            }// end of is empty long + larg
            else label.setText("Entrer les dimensions du produit !");
        }
        else label.setText("Entrer le nom du client !");
        // end if is empty client

    }
}
