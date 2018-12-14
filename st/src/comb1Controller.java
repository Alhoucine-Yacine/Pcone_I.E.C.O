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
import java.util.Comparator;
import java.util.ResourceBundle;

/**
 * Created by JUV on 15/09/2018.
 */
public class comb1Controller implements Initializable {
    @FXML
    AnchorPane anchorPane;
    @FXML
    ComboBox comboBox ;
    @FXML
    Button retour;
    @FXML
    TableColumn commandes;
    @FXML
    TableColumn laise;
    @FXML
    TableColumn dechet;
    @FXML
    TableColumn choix;
    @FXML
    TableView tableView;

    public ObservableList<combinaison> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        commandes.setCellValueFactory(new PropertyValueFactory<optimController.combinaison, String>("commandes"));
        laise.setCellValueFactory(new PropertyValueFactory<optimController.combinaison, String>("laise"));
        dechet.setCellValueFactory(new PropertyValueFactory<optimController.combinaison, String>("dechet"));
        choix.setCellValueFactory(new PropertyValueFactory<optimController.combinaison, String>("checkBox"));
        tableView.setItems(list);
        try {comboBox.getItems().remove(0,3);
            Connection connection=Main.getConnection(Main.user,Main.getPsw());
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM commande;");
            while (rs.next()) {
                String str0 = "";
                String str1 = "";
                if (rs.getInt("typee") == 3) str0 = "simple";
                else str0 = "double";
                String str = rs.getInt("id") + " - " +rs.getString("nomCommande")+" -> "+ rs.getString("client") + " [" + str0 + "-" + rs.getString("ceType") + ", " + rs.getInt("ciG") + "g-" + rs.getInt("firstCannG") + "g-" + rs.getInt("ceG") + "g-" + rs.getInt("secCannG") + "g-" + rs.getInt("medG") + "g] [" + rs.getInt("longeur") + "mm " + rs.getInt("largeur") + "mm], " + rs.getInt("nbPlaques") + " plaques, " + rs.getString("dateCommande") + ", " + rs.getString("dureemax") + " jrs";
                if (rs.getInt("etatCommande") == 1)
                    comboBox.getItems().add(str);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public class combinaison{
        public String commandes;
        public int laise;
        public int dechet;
        public CheckBox checkBox;

        public String getCommandes(){
            return commandes;
        }
        public int getLaise(){
            return laise;
        }
        public int getDechet(){
            return dechet;
        }

        public CheckBox getCheckBox(){
            return checkBox;
        }
        public combinaison(String c,int l,int d){
            this.laise=l;
            this.dechet=d;
            this.commandes=c;
            checkBox = new CheckBox();
            checkBox.setVisible(true);
            checkBox.setText("Validé");
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    Connection connection = null;
                    try {
                        int nbOptims=0;
                        connection=Main.getConnection(Main.user,Main.getPsw());
                        Statement stmt = connection.createStatement();
                        String str[]=checkBox.getId().split("/");
                        ResultSet rs = stmt.executeQuery("SELECT max(id) from optimisation");
                        if (rs!=null) rs.next();
                        nbOptims=rs.getInt("max(id)");
                        nbOptims++;
                        String s="INSERT INTO optimisation (id,commandes,dateOptim) VALUES("+nbOptims+",'"+str[4]+"','"+str[5]+"');";
                        int insert =stmt.executeUpdate(s);
                        String s0="UPDATE commande SET nbPlaques="+str[2]+ " WHERE id="+str[0]+";";
                        String s2="UPDATE commande SET nbPlaques="+str[3]+ " WHERE id="+str[1]+";";
                        String s1="UPDATE commande SET etatCommande="+0+ " WHERE id="+str[0]+";";
                        String s3="UPDATE commande SET etatCommande="+0+ " WHERE id="+str[1]+";";
                        String s4="UPDATE commande SET dateProduction='"+str[5]+ "' WHERE id="+str[0]+";";
                        String s5="UPDATE commande SET dateProduction='"+str[5]+ "' WHERE id="+str[1]+";";
                        insert =stmt.executeUpdate(s0);
                        insert =stmt.executeUpdate(s2);
                        if (Integer.parseInt(str[2])==0) {insert =stmt.executeUpdate(s1); insert=stmt.executeUpdate(s4);}
                        if (Integer.parseInt(str[3])==0) {insert =stmt.executeUpdate(s3);stmt.executeUpdate(s5);}
                        connection.close();
                        getData();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
            });

        }

    }

    public int[] getOptimalLaise(int a, int b){
        int tab[];
        int min=0;
        int minL=1300;
        int mini=0;
        int minj=0;
        for (int i=1;i<7;i++){
            for (int j=1;j<=7-i;j++) {
                for (int k = 1300; k <= 2500; k += 100) {
                    int l = k - a * i - b * j;
                    if (l >= 30 && (l <= min || min == 0)) {
                        min = l;
                        minL=k;
                        mini=i;
                        minj=j;


                    }
                }

            }
        }
        return new int[]{minL,min,mini,minj};
    }


    public  void getData() throws Exception {
        list.removeAll(list);
        Connection connection=Main.getConnection(Main.user,Main.getPsw());
        Statement stmt = connection.createStatement();
        String arr[]=((String)comboBox.getValue()).split(" ");
        int wantedID=Integer.parseInt(arr[0]);
        ResultSet rs = stmt.executeQuery("SELECT * FROM commande WHERE id="+wantedID+";");
        rs.next();
        Statement stmt2 = connection.createStatement();
        ResultSet rs2 = stmt2.executeQuery("SELECT * FROM commande ;");
        while (rs2.next()) {
            if (rs.getInt("id") != rs2.getInt("id") &&rs.getInt("etatCommande") == 1 && rs2.getInt("etatCommande") == 1) {
                if (rs.getInt("typee") == rs2.getInt("typee") && rs.getString("ceType").equals(rs2.getString("ceType"))) {
                    if (rs.getInt("ciG") == rs2.getInt("ciG") && rs.getInt("medG") == rs2.getInt("medG") && rs.getInt("firstCannG") == rs2.getInt("firstCannG") && rs.getInt("secCannG") == rs2.getInt("secCannG") && rs.getInt("ceG") == rs2.getInt("ceG") && rs.getInt("epI")==rs2.getInt("epI") && rs.getInt("epII")==rs2.getInt("epII")) {
                        if (rs.getInt("largeur")+rs2.getInt("largeur")<2500) {
                                String str0 = "";
                                if (rs.getInt("typee") == 3) str0 = "simple";
                                else str0 = "double";
                                int tab[]=getOptimalLaise(rs.getInt("largeur"),rs2.getInt("largeur"));
                                int nbi=tab[2];
                                int nbj=tab[3];
                                int longA=rs.getInt("longeur")*rs.getInt("nbPlaques")/nbi;
                                if (rs.getInt("longeur")*rs.getInt("nbPlaques")%nbi !=0 ) longA=longA+rs.getInt("longeur");
                                int longB=rs2.getInt("longeur")*rs2.getInt("nbPlaques")/nbj;
                                if (rs2.getInt("longeur")*rs2.getInt("nbPlaques")%nbj !=0 ) longB=longB+rs2.getInt("longeur");
                                int minLong=Math.min(longA,longB);

                                int nbres1=rs.getInt("nbPlaques")-minLong*nbi/rs.getInt("longeur");
                                int nbres2=rs2.getInt("nbPlaques")-minLong*nbj/rs2.getInt("longeur");
                                if (nbres1<0) nbres1=0;
                                if(nbres2<0) nbres2=0;
                            String str5="[";
                            if (rs.getInt("epI")==1) str5=str5+"Micro Cannelure";
                            else if(rs.getInt("epI")==2) str5=str5+"Petite Cannelure";
                            else if (rs.getInt("epI")==3) str5=str5+"Grande Cannelure";

                            if (rs.getInt("epII")==0) str5=str5+"]";
                            else if(rs.getInt("epII")==1) str5=str5+", Micro Cannelure]";
                            else if(rs.getInt("epII")==2) str5=str5+", Petite Cannelure]";
                            else if(rs.getInt("epII")==3) str5=str5+", Grande Cannelure]";

                            String str6="[";
                            if (rs2.getInt("epI")==1) str6=str6+"Micro Cannelure";
                            else if(rs2.getInt("epI")==2) str6=str6+"Petite Cannelure";
                            else if (rs2.getInt("epI")==3) str6=str6+"Grande Cannelure";

                            if (rs2.getInt("epII")==0) str6=str6+"]";
                            else if(rs2.getInt("epII")==1) str6=str6+", Micro Cannelure]";
                            else if(rs2.getInt("epII")==2) str6=str6+", Petite Cannelure]";
                            else if(rs2.getInt("epII")==3) str6=str6+", Grande Cannelure]";
                            String str = rs.getInt("id") + " - "+rs.getString("nomCommande")+" -> " + rs.getString("client") + " [" + str0 + "-" + rs.getString("ceType") +" "+ str5+" "+ rs.getInt("ciG") + "g-" + rs.getInt("firstCannG") + "g-" + rs.getInt("ceG") + "g-" + rs.getInt("secCannG") + "g-" + rs.getInt("medG") + "g] [" + rs.getInt("longeur") + "mm " + rs.getInt("largeur") + "mm], " + rs.getInt("nbPlaques") + " plaques, " + rs.getString("dateCommande") + ", " + rs.getString("dureemax") + " jrs *** "+nbi+ " par groupe | reste : "+nbres1+" plaque(s) ";
                            String str2 = rs2.getInt("id") +" - "+rs2.getString("nomCommande")+ " -> " + rs2.getString("client") + " [" + str0 + "-" + rs2.getString("ceType") +" "+ str6+" "+ rs2.getInt("ciG") + "g-" + rs2.getInt("firstCannG") + "g-" + rs2.getInt("ceG") + "g-" + rs2.getInt("secCannG") + "g-" + rs2.getInt("medG") + "g] [" + rs2.getInt("longeur") + "mm " + rs2.getInt("largeur") + "mm], " + rs2.getInt("nbPlaques") + " plaques, " + rs2.getString("dateCommande") + ", " + rs2.getString("dureemax") + " jrs *** "+nbj+ " par groupe | reste : "+nbres2+" plaque(s) ";
                                String str3 = str + "\n" + str2;
                                int year = Calendar.getInstance().get(Calendar.YEAR);
                                int month = ZonedDateTime.now(  ZoneId.of( "Africa/Algiers" )  ).getMonthValue();
                                //int month=Calendar.getInstance().get(Calendar.MONTH);
                                int day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                                String strDate = day + "-" + month + "-" + year;

                                combinaison comb=new combinaison(str3,getOptimalLaise(rs.getInt("largeur"),rs2.getInt("largeur"))[0],getOptimalLaise(rs.getInt("largeur"),rs2.getInt("largeur"))[1]);
                                comb.checkBox.setId(Integer.toString(rs.getInt("id"))+"/"+Integer.toString(rs2.getInt("id"))+"/"+nbres1+"/"+nbres2+"/"+str3+"/"+strDate);
                                list.add(comb);
                                Comparator<combinaison> comparator=Comparator.comparingInt(combinaison::getDechet);
                                list.sort(comparator);
                            }
                        }
                    }
                }
            }

        connection.close();



    }

    public void cancelPressed(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("first.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));

    }
}
