import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by JUV on 11/09/2018.
 */
public class deleteCommandeController implements Initializable {
    @FXML
    AnchorPane anchorPane;
    @FXML
    TableView tableView;
    @FXML
    TableColumn commande;
    @FXML
    TableColumn etat;
    @FXML
    TableColumn operation;
    @FXML
    Button retour;

    public ObservableList<commande> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getData();
        commande.setCellValueFactory(new PropertyValueFactory<commande, String>("commandee"));
        etat.setCellValueFactory(new PropertyValueFactory<commande, String>("etat"));
        operation.setCellValueFactory(new PropertyValueFactory<commande,String>("operation"));
        tableView.setItems(list);

    }

    public void getData(){
        Connection connection = null;
        try {
            connection=Main.getConnection(Main.user,Main.getPsw());
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM commande ;");
            while (rs.next()) {
                String str0 = "";
                String str1 = "";
                if (rs.getInt("typee") == 3) str0 = "simple";
                else str0 = "doubcle";
                String str5="[";
                if (rs.getInt("epI")==1) str5=str5+"Micro Cannelure";
                else if(rs.getInt("epI")==2) str5=str5+"Petite Cannelure";
                else if (rs.getInt("epI")==3) str5=str5+"Grande Cannelure";

                if (rs.getInt("epII")==0) str5=str5+"]";
                else if(rs.getInt("epII")==1) str5=str5+", Micro Cannelure]";
                else if(rs.getInt("epII")==2) str5=str5+", Petite Cannelure]";
                else if(rs.getInt("epII")==3) str5=str5+", Grande Cannelure]";
                String str = rs.getInt("id") + " - " +rs.getString("nomCommande")+" -> "+ rs.getString("client") + " [" + str0 + "-" + rs.getString("ceType") +" "+ str5 +" "+ rs.getInt("ciG") + "g-" + rs.getInt("firstCannG") + "g-" + rs.getInt("ceG") + "g-" + rs.getInt("secCannG") + "g-" + rs.getInt("medG") + "g] [" + rs.getInt("longeur") + "mm " + rs.getInt("largeur") + "mm], " + rs.getInt("nbPlaques") + " plaques, " + rs.getString("dateCommande") + ", " + rs.getString("dureemax") + " jrs";
                if (rs.getInt("etatCommande") == 1) str1 = "En attente";
                else str1 = "Fournie";
                commande commande1=new commande(str,str1);
                commande1.operation.setId(Integer.toString(rs.getInt("id")));
                list.add(commande1);

            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public class commande{
        public String commandee;
        public Label etat;
        public Button operation;

        public String getCommandee(){
            return commandee;
        }

        public Button getOperation() {
            return operation;
        }

        public Label getEtat() {
            return etat;
        }

        public commande(String commandee, String etat) {
            this.commandee = commandee;
            this.etat=new Label();
            this.etat.setText(etat);
            this.operation=new Button("Supprmier");
            operation.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    Main main=new Main();
                    try {
                        Connection connection= main.getConnection(main.user,main.getPsw());
                        Statement stmt=connection.createStatement();
                        int delete = stmt.executeUpdate("DELETE FROM commande WHERE id="+operation.getId()+";");
                        AnchorPane pane = FXMLLoader.load(getClass().getResource("deleteC.fxml"));
                        main=new Main();
                        main.getStage().setScene(new Scene(pane));
                    } catch (Exception e1) {
                    e1.printStackTrace();
                    }
                }
            });
        }
    }
    public void retourPressed(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("first.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));


    }
}
