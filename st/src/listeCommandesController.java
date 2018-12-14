import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Created by JUV on 09/09/2018.
 */
public class listeCommandesController implements Initializable {
    @FXML
    AnchorPane anchorPane;
    @FXML
    TableView tableView;
    @FXML
    TableColumn commandes;
    @FXML
    TableColumn etat;
    @FXML
    Button retour;
    public ObservableList<commandee> list = FXCollections.observableArrayList();


    public static class commandee {
        public String data;
        public String etat;
        public CheckBox checkBox;


        public void setData(String data) {
            this.data = data;
        }

        public void setEtat(String etat) {
            this.etat = etat;
        }

        public String getData() {
            return data;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public String getEtat() {
            return etat;
        }

        public commandee(String d, String e) {
            data = d;
            etat = e;
            checkBox = new CheckBox();
            checkBox.setVisible(true);
            checkBox.setText("Valide");
            if (e.equals("En attente")) checkBox.setSelected(true);
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    Connection connection = null;
                    int etat=0;
                    if(newValue==true) etat=1;
                    try {int year = Calendar.getInstance().get(Calendar.YEAR);
                        int month = ZonedDateTime.now(  ZoneId.of( "Africa/Algiers" )  ).getMonthValue();
                        int day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                        String strDate = day + "-" + month + "-" + year;
                        connection=Main.getConnection(Main.user,Main.getPsw());
                        Statement stmt = connection.createStatement();
                        String s="UPDATE commande SET etatCommande="+etat+ " WHERE id="+checkBox.getId()+";";
                        String s2="UPDATE commande SET dateProduction='"+strDate+ "' WHERE id="+checkBox.getId()+";";
                        int insert =stmt.executeUpdate(s);
                        if(newValue==false) insert=stmt.executeUpdate(s2);
                        connection.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
            });
        }


    }



    public void getData() throws Exception {
        Connection connection=Main.getConnection(Main.user,Main.getPsw());
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM commande ;");
        while (rs.next()) {
            String str0 = "";
            String str1 = "";
            if (rs.getInt("typee") == 3) str0 = "simple";
            else str0 = "double";
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
            else str1 = "Fournie/Annulée";
            commandee commande = new commandee(str, str1);
            list.add(commande);
            commande.checkBox.setId(Integer.toString(rs.getInt("id")));
        }

        connection.close();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            getData();
            commandes.setCellValueFactory(new PropertyValueFactory<commandee, String>("data"));
            etat.setCellValueFactory(new PropertyValueFactory<commandee, String>("checkBox"));
            tableView.setItems(list);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void retourPressed(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("first.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));


    }
}
