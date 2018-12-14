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
import java.util.ResourceBundle;

/**
 * Created by JUV on 16/09/2018.
 */
public class historiqueController implements Initializable {
    @FXML
    TableView tableView;
    @FXML
    TableColumn optims;
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
    TableColumn date;
    @FXML
    Label label;

    public ObservableList<optima> list = FXCollections.observableArrayList();

    public class optima {
        String comb;
        String date;

        public String getDate() {
            return date;
        }

        public String getComb() {

            return comb;
        }

        public optima(String comb, String date) {
            this.comb = comb;
            this.date = date;
        }
    }

    public void dayB (javafx.scene.input.KeyEvent evt){
        String c=evt.getCharacter();
        char character =c.charAt(0);
        if (!Character.isDigit(character)  || dayBegin.getText().length()==2 || (dayBegin.getText().isEmpty() && Integer.parseInt(c)>3) || (dayBegin.getText().equals("3") && Integer.parseInt(c)>1)) evt.consume();


    }

    public void dayE (javafx.scene.input.KeyEvent evt){
        String c=evt.getCharacter();
        char character =c.charAt(0);
        if (!Character.isDigit(character)  || dayEnd.getText().length()==2 || (dayEnd.getText().isEmpty() && Integer.parseInt(c)>3) || (dayEnd.getText().equals("3") && Integer.parseInt(c)>1)) evt.consume();


    }

    public void monthB (javafx.scene.input.KeyEvent evt){
        String c=evt.getCharacter();
        char character =c.charAt(0);
        if (!Character.isDigit(character) || monthBegin.getText().length()==2 || (monthBegin.getText().isEmpty() && Integer.parseInt(c)>1) || (monthBegin.getText().equals("1") && Integer.parseInt(c)>2)) evt.consume();


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
        try {
            getData();
            optims.setCellValueFactory(new PropertyValueFactory<optima,String>("comb"));
            date.setCellValueFactory(new PropertyValueFactory<optima,String>("date"));
            tableView.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getData() throws Exception {
        Connection connection=Main.getConnection(Main.user,Main.getPsw());
        Statement stmt=connection.createStatement();
        ResultSet rs=stmt.executeQuery("SELECT * FROM optimisation;");
        while (rs.next()){
            String combin=rs.getString("commandes");
            String date=rs.getString("dateOptim");
            optima op=new optima(combin,date);
            list.add(op);


        }


    }
    public void retourPressed(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("first.fxml"));
        Main main=new Main();
        main.getStage().setScene(new Scene(pane));


    }

    public void recherchePressed () throws Exception {
        if (!dayBegin.getText().isEmpty() && !dayEnd.getText().isEmpty() && !monthBegin.getText().isEmpty() && !monthEnd.getText().isEmpty() && !yearBegin.getText().isEmpty() && !yearEnd.getText().isEmpty()){
        if (Integer.parseInt(yearEnd.getText())>=Integer.parseInt(yearBegin.getText())){
            if (Integer.parseInt(yearEnd.getText())>Integer.parseInt(yearBegin.getText()) || Integer.parseInt(yearEnd.getText())==Integer.parseInt(yearBegin.getText()) && Integer.parseInt(monthEnd.getText())>=Integer.parseInt(monthBegin.getText())){
                if (Integer.parseInt(yearEnd.getText())>Integer.parseInt(yearBegin.getText()) || Integer.parseInt(monthEnd.getText())>Integer.parseInt(monthBegin.getText()) || Integer.parseInt(monthEnd.getText())==Integer.parseInt(monthBegin.getText()) && Integer.parseInt(dayEnd.getText())>=Integer.parseInt(dayBegin.getText())){
                   list.removeAll(list);
                   label.setText("");
                    Connection connection=Main.getConnection(Main.user,Main.getPsw());
                   Statement stmt =connection.createStatement();
                   ResultSet rs=stmt.executeQuery("SELECT * FROM optimisation");
                   while (rs.next()){
                       String comb=rs.getString("commandes");
                       String date=rs.getString("dateOptim");
                       String str[]=date.split("-");
                       System.out.println(str[0]+" "+str[1]+" "+str[2]);
                       boolean yearMaxSupYear=Integer.parseInt(str[2])<Integer.parseInt(yearEnd.getText());
                       boolean yearMaxEqualYear=Integer.parseInt(str[2])==Integer.parseInt(yearEnd.getText());
                       boolean yearMinInfYear=Integer.parseInt(str[2])>Integer.parseInt(yearBegin.getText());
                       boolean yearMinEqualYear=Integer.parseInt(str[2])==Integer.parseInt(yearBegin.getText());

                       boolean dayMaxSupDay=Integer.parseInt(str[0])<Integer.parseInt(dayEnd.getText());
                       boolean dayMaxEqualDay=Integer.parseInt(str[0])==Integer.parseInt(dayEnd.getText());
                       boolean dayMinInfDay=Integer.parseInt(str[0])>Integer.parseInt(dayBegin.getText());
                       boolean dayMinEqualDay=Integer.parseInt(str[0])==Integer.parseInt(dayBegin.getText());

                       boolean monthMaxSupMonth=Integer.parseInt(str[1])<Integer.parseInt(monthEnd.getText());
                       boolean monthMaxEqualMonth=Integer.parseInt(str[1])==Integer.parseInt(monthEnd.getText());
                       boolean monthMinInfMonth=Integer.parseInt(str[1])>Integer.parseInt(monthBegin.getText());
                       boolean monthMinEqualMonth=Integer.parseInt(str[1])==Integer.parseInt(monthBegin.getText());


                       if(Integer.parseInt(str[2])>=Integer.parseInt(yearBegin.getText()) && Integer.parseInt(str[2])<=Integer.parseInt(yearEnd.getText())){
                           if ( (Integer.parseInt(str[2])>Integer.parseInt(yearBegin.getText()) || Integer.parseInt(str[2])==Integer.parseInt(yearBegin.getText()) && Integer.parseInt(str[1])>=Integer.parseInt(monthBegin.getText()))&& (Integer.parseInt(str[2])<Integer.parseInt(yearEnd.getText()) || Integer.parseInt(str[2])==Integer.parseInt(yearEnd.getText()) && Integer.parseInt(str[1])<=Integer.parseInt(monthEnd.getText()))){
                               if((yearMinInfYear && yearMaxSupYear) || (yearMinEqualYear && monthMinInfMonth && monthMaxSupMonth) || (yearMaxEqualYear && monthMaxSupMonth && monthMinInfMonth) || (yearMinEqualYear && monthMinEqualMonth && (dayMinEqualDay || dayMinInfDay)) && (dayMaxSupDay || dayMaxEqualDay || monthMaxSupMonth) || (yearMaxEqualYear && monthMaxEqualMonth  && (dayMaxSupDay || dayMaxEqualDay) && (dayMinEqualDay || dayMinInfDay)) || yearMaxSupYear && (yearMinInfYear || yearMinEqualYear && monthMinInfMonth || yearMinEqualYear && monthMinEqualMonth && (dayMinEqualDay || dayMinInfDay) ) ||  yearMinInfYear && (yearMaxSupYear || yearMaxEqualYear && monthMaxSupMonth || yearMaxEqualYear && monthMaxEqualMonth && (dayMaxSupDay || dayMaxEqualDay) )){
                                   optima op =new optima(comb,date);
                                   list.add(op);
                               }

                           }
                       }
                   }
                }
                else label.setText("Veuillez verifier l'intervale de temps");
            }
            else label.setText("Veuillez verifier l'intervale de temps");
        }
        else label.setText("Veuillez verifier l'intervale de temps");
        }
        else label.setText("Veuillez remplir tous les champs !");


    }
}
