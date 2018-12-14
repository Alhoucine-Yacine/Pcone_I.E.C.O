import com.sun.deploy.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by JUV on 23/09/2018.
 */
public class statController implements Initializable{
    @FXML
    TableView tableView;
    @FXML
    TableColumn commandes;
    @FXML
    TextField dayBegin;
    @FXML
    TextField dayEnd;
    @FXML
    TextField monthBegin;
    @FXML
    TextField monthEnd;
    @FXML
    TextField yearBegin;
    @FXML
    TextField yearEnd;
    @FXML
    AnchorPane anchorPane;
    @FXML
    Button recherche;
    @FXML
    Button retour;
    @FXML
    Label warningDate;
    @FXML
    ComboBox clients;
    @FXML
    ComboBox typeCarton;
    @FXML
    ComboBox typeCE;
    @FXML
    Label total;
    @FXML
    Label surfaceTotal;
    @FXML
    Label surfaceSB;
    @FXML
    Label surfaceST;
    @FXML
    Label surfaceSK;
    @FXML
    Label surfaceDB;
    @FXML
    Label surfaceDT;
    @FXML
    Label surfaceDK;

    public ObservableList<commande> list = FXCollections.observableArrayList();

    public class commande{
        String dsc;

        public String getDsc() {
            return dsc;
        }

        public commande(String dsc) {
            this.dsc = dsc;
        }
    }

    public void retourPressed(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("first.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));


    }


    public void dayB (KeyEvent evt){
        String c=evt.getCharacter();
        char character =c.charAt(0);
        if (!Character.isDigit(character) || dayBegin.getText().length()==2 || (dayBegin.getText().isEmpty() && Integer.parseInt(c)>3) || (dayBegin.getText().equals("3") && Integer.parseInt(c)>1)) evt.consume();


    }

    public void dayE (javafx.scene.input.KeyEvent evt){
        String c=evt.getCharacter();
        char character =c.charAt(0);
        if (!Character.isDigit(character)  || dayEnd.getText().length()==2 || (dayEnd.getText().isEmpty() && Integer.parseInt(c)>3) || (dayEnd.getText().equals("3") && Integer.parseInt(c)>1)) evt.consume();


    }

    public void monthB (javafx.scene.input.KeyEvent evt){
        String c=evt.getCharacter();
        char character =c.charAt(0);
        if (!Character.isDigit(character)  || monthBegin.getText().length()==2 || (monthBegin.getText().isEmpty() && Integer.parseInt(c)>1) || (monthBegin.getText().equals("1") && Integer.parseInt(c)>2)) evt.consume();


    }

    public void monthE (javafx.scene.input.KeyEvent evt){
        String c=evt.getCharacter();
        char character =c.charAt(0);
        if (!Character.isDigit(character)  || monthEnd.getText().length()==2 || (monthEnd.getText().isEmpty() && Integer.parseInt(c)>1) || (monthEnd.getText().equals("1") && Integer.parseInt(c)>2)) evt.consume();


    }

    public void yearB (javafx.scene.input.KeyEvent evt){
        String c=evt.getCharacter();
        char character =c.charAt(0);
        if (!Character.isDigit(character)  || yearBegin.getText().length()==4) evt.consume();


    }

    public void yearE (javafx.scene.input.KeyEvent evt){
        String c=evt.getCharacter();
        char character =c.charAt(0);
        if (!Character.isDigit(character)  || yearEnd.getText().length()==4) evt.consume();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        commandes.setCellValueFactory(new PropertyValueFactory<commande,String>("dsc"));
        tableView.setItems(list);

        try {
            typeCarton.getItems().removeAll("Item 1","Item 2","Item 3");
            typeCE.getItems().removeAll("Item 1","Item 2","Item 3");
            clients.getItems().removeAll("Item 1","Item 2","Item 3");
            Main main=new Main();
            Connection connection=main.getConnection(main.user,main.getPsw());
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM client");
            clients.getItems().add("Tous les clients");
            clients.setValue("Tous les clients");
            typeCarton.getItems().addAll("Tous les types","Simple","Double");
            typeCarton.setValue("Tous les types");
            typeCE.getItems().addAll("Tous les types","blanc","TestLiner","Kraft");
            typeCE.setValue("Tous les types");
            while (rs.next()) clients.getItems().add(rs.getString("name"));

        } catch (Exception e) {

        }
    }

    public void recherchePressed () throws Exception {
        if (!dayBegin.getText().isEmpty() && !dayEnd.getText().isEmpty() && !monthBegin.getText().isEmpty() && !monthEnd.getText().isEmpty() && !yearBegin.getText().isEmpty() && !yearEnd.getText().isEmpty()){
            if (Integer.parseInt(yearEnd.getText())>=Integer.parseInt(yearBegin.getText())){
                if (Integer.parseInt(yearEnd.getText())>Integer.parseInt(yearBegin.getText()) || Integer.parseInt(yearEnd.getText())==Integer.parseInt(yearBegin.getText()) && Integer.parseInt(monthEnd.getText())>=Integer.parseInt(monthBegin.getText())){
                    if (Integer.parseInt(yearEnd.getText())>Integer.parseInt(yearBegin.getText()) || Integer.parseInt(monthEnd.getText())>Integer.parseInt(monthBegin.getText()) || Integer.parseInt(monthEnd.getText())==Integer.parseInt(monthBegin.getText()) && Integer.parseInt(dayEnd.getText())>=Integer.parseInt(dayBegin.getText())) {
                        list.removeAll(list);
                        warningDate.setText("");
                        Connection connection = Main.getConnection(Main.user, Main.getPsw());
                        Statement stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM commande");
                        int total1 = 0;
                        float surfaceTotal1 = 0;
                        float surfaceSB1 = 0;
                        float surfaceST1 = 0;
                        float surfaceSK1 = 0;
                        float surfaceDB1 = 0;
                        float surfaceDT1 = 0;
                        float surfaceDK1 = 0;
                        while (rs.next()) {
                            if (rs.getInt("etatCommande") == 0) {
                                String type = "";
                                if (rs.getInt("typee") == 3) type = "Simple";
                                else type = "Double";
                                String str0 = "";
                                String str1 = "";
                                if (rs.getInt("typee") == 3) str0 = "simple";
                                else str0 = "double";
                                String str5 = "[";
                                if (rs.getInt("epI") == 1) str5 = str5 + "Micro Cannelure";
                                else if (rs.getInt("epI") == 2) str5 = str5 + "Petite Cannelure";
                                else if (rs.getInt("epI") == 3) str5 = str5 + "Grande Cannelure";

                                if (rs.getInt("epII") == 0) str5 = str5 + "]";
                                else if (rs.getInt("epII") == 1) str5 = str5 + ", Micro Cannelure]";
                                else if (rs.getInt("epII") == 2) str5 = str5 + ", Petite Cannelure]";
                                else if (rs.getInt("epII") == 3) str5 = str5 + ", Grande Cannelure]";
                                String str10 = rs.getInt("id") + " - " + rs.getString("nomCommande") + " -> " + rs.getString("client") + " [" + str0 + "-" + rs.getString("ceType") + " " + str5 + " " + rs.getInt("ciG") + "g-" + rs.getInt("firstCannG") + "g-" + rs.getInt("ceG") + "g-" + rs.getInt("secCannG") + "g-" + rs.getInt("medG") + "g] [" + rs.getInt("longeur") + "mm " + rs.getInt("largeur") + "mm], " + rs.getInt("nbPlaquesSave") + " plaques, " + rs.getString("dateCommande") + ", " + rs.getString("dureemax") + " jrs";
                                String date = rs.getString("dateProduction");
                                String str[] = date.split("-");
                                boolean yearMaxSupYear = Integer.parseInt(str[2]) < Integer.parseInt(yearEnd.getText());
                                boolean yearMaxEqualYear = Integer.parseInt(str[2]) == Integer.parseInt(yearEnd.getText());
                                boolean yearMinInfYear = Integer.parseInt(str[2]) > Integer.parseInt(yearBegin.getText());
                                boolean yearMinEqualYear = Integer.parseInt(str[2]) == Integer.parseInt(yearBegin.getText());

                                boolean dayMaxSupDay = Integer.parseInt(str[0]) < Integer.parseInt(dayEnd.getText());
                                boolean dayMaxEqualDay = Integer.parseInt(str[0]) == Integer.parseInt(dayEnd.getText());
                                boolean dayMinInfDay = Integer.parseInt(str[0]) > Integer.parseInt(dayBegin.getText());
                                boolean dayMinEqualDay = Integer.parseInt(str[0]) == Integer.parseInt(dayBegin.getText());

                                boolean monthMaxSupMonth = Integer.parseInt(str[1]) < Integer.parseInt(monthEnd.getText());
                                boolean monthMaxEqualMonth = Integer.parseInt(str[1]) == Integer.parseInt(monthEnd.getText());
                                boolean monthMinInfMonth = Integer.parseInt(str[1]) > Integer.parseInt(monthBegin.getText());
                                boolean monthMinEqualMonth = Integer.parseInt(str[1]) == Integer.parseInt(monthBegin.getText());


                                if (Integer.parseInt(str[2]) >= Integer.parseInt(yearBegin.getText()) && Integer.parseInt(str[2]) <= Integer.parseInt(yearEnd.getText())) {
                                    if ((Integer.parseInt(str[2]) > Integer.parseInt(yearBegin.getText()) || Integer.parseInt(str[2]) == Integer.parseInt(yearBegin.getText()) && Integer.parseInt(str[1]) >= Integer.parseInt(monthBegin.getText())) && (Integer.parseInt(str[2]) < Integer.parseInt(yearEnd.getText()) || Integer.parseInt(str[2]) == Integer.parseInt(yearEnd.getText()) && Integer.parseInt(str[1]) <= Integer.parseInt(monthEnd.getText()))) {
                                        if ((yearMinInfYear && yearMaxSupYear) || (yearMinEqualYear && monthMinInfMonth && monthMaxSupMonth) || (yearMaxEqualYear && monthMaxSupMonth && monthMinInfMonth) || (yearMinEqualYear && monthMinEqualMonth && (dayMinEqualDay || dayMinInfDay)) && (dayMaxSupDay || dayMaxEqualDay || monthMaxSupMonth) || (yearMaxEqualYear && monthMaxEqualMonth && (dayMaxSupDay || dayMaxEqualDay) && (dayMinEqualDay || dayMinInfDay)) || yearMaxSupYear && (yearMinInfYear || yearMinEqualYear && monthMinInfMonth || yearMinEqualYear && monthMinEqualMonth && (dayMinEqualDay || dayMinInfDay)) || yearMinInfYear && (yearMaxSupYear || yearMaxEqualYear && monthMaxSupMonth || yearMaxEqualYear && monthMaxEqualMonth && (dayMaxSupDay || dayMaxEqualDay))) {
                                            if (rs.getString("client").equals(clients.getValue()) || clients.getValue().equals("Tous les clients")) {
                                                if (type.equals(typeCarton.getValue()) || typeCarton.getValue().equals("Tous les types")) {
                                                    if (typeCE.getValue().equals(rs.getString("ceType")) || typeCE.getValue().equals("Tous les types")) {
                                                        commande c = new commande(str10);
                                                        list.add(c);
                                                        total1=total1+rs.getInt("nbPlaquesSave");
                                                        surfaceTotal1=surfaceTotal1+rs.getInt("nbPlaquesSave")*rs.getInt("longeur")*rs.getInt("largeur")/1000000;
                                                        if (rs.getInt("typee")==3){
                                                            if (rs.getString("ceType").equals("blanc")) surfaceSB1=surfaceSB1+rs.getInt("nbPlaquesSave")*rs.getInt("longeur")*rs.getInt("largeur")/1000000;
                                                            if (rs.getString("ceType").equals("Kraft")) surfaceSK1=surfaceSK1+rs.getInt("nbPlaquesSave")*rs.getInt("longeur")*rs.getInt("largeur")/1000000;
                                                            if (rs.getString("ceType").equals("TestLiner")) surfaceST1=surfaceST1+rs.getInt("nbPlaquesSave")*rs.getInt("longeur")*rs.getInt("largeur")/1000000;



                                                        }
                                                        else {
                                                            if (rs.getString("ceType").equals("blanc")) surfaceDB1=surfaceDB1+rs.getInt("nbPlaquesSave")*rs.getInt("longeur")*rs.getInt("largeur")/1000000;
                                                            if (rs.getString("ceType").equals("Kraft")) surfaceDK1=surfaceDK1+rs.getInt("nbPlaquesSave")*rs.getInt("longeur")*rs.getInt("largeur")/1000000;
                                                            if (rs.getString("ceType").equals("TestLiner")) surfaceDT1=surfaceDT1+rs.getInt("nbPlaquesSave")*rs.getInt("longeur")*rs.getInt("largeur")/1000000;



                                                        }

                                                    }
                                                }
                                            }
                                            //
                                            //historiqueController.optima op =new historiqueController.optima(comb,date);
                                            //list.add(op);
                                        }

                                    }
                                }
                            }
                        }
                        total.setText(Integer.toString(total1));
                        surfaceTotal.setText(Float.toString(surfaceTotal1));
                        surfaceDB.setText(Float.toString(surfaceDB1));
                        surfaceDK.setText(Float.toString(surfaceDK1));
                        surfaceDT.setText(Float.toString(surfaceDT1));
                        surfaceSB.setText(Float.toString(surfaceSB1));
                        surfaceSK.setText(Float.toString(surfaceSK1));
                        surfaceST.setText(Float.toString(surfaceST1));

                    }
                    else warningDate.setText("Veuillez verifier l'intervale de temps");
                }
                else warningDate.setText("Veuillez verifier l'intervale de temps");
            }
            else warningDate.setText("Veuillez verifier l'intervale de temps");
        }
        else warningDate.setText("Veuillez remplir tous les champs !");


    }
}

