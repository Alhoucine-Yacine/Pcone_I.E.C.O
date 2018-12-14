import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by JUV on 04/09/2018.
 */
public class Client {
    public int ClientID;
    public int nbCommandes=0;
    public String ClientName;


    public void addCommande() throws Exception {
        Connection connection=Main.getConnection(Main.user,Main.getPsw());
        Statement stmt= connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM client WHERE name='"+ClientName+"';");
        rs.next();
        int nCommandes=rs.getInt("nbCommandes");
        nCommandes++;
        String s="UPDATE client SET nbCommandes="+nCommandes+ " WHERE name='"+ClientName+"';";
        //System.out.println(s);
        int insert =stmt.executeUpdate(s);
        connection.close();
    }

    public void ajouterClient() throws Exception {
        int nbClients=0;
        Connection connection=Main.getConnection(Main.user,Main.getPsw());
        Statement stmt= connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from client");
        while (rs.next()) {
            nbClients++;
        }
        String str="INSERT INTO client VALUES("+ nbClients+",'"+ClientName+"',"+nbCommandes+")";
        int insert =stmt.executeUpdate(str);
        connection.close();
    }
    public static void main(String args[]) throws Exception {
        Client Cevital=new Client();
        Cevital.ClientName="cevital";
        Cevital.addCommande();

    }
}
