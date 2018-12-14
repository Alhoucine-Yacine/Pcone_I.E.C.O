import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by JUV on 04/09/2018.
 */
public class Commande {
    public int idCommande;
    public String client;
    public String nomCommande;
    public int typeCommande;
    public String ceType;
    public int ciG;
    public int firstCannG;
    public int ceG;
    public int secCannG;
    public int medG;
    public int longueur;
    public int largeur;
    public int nbPlaques;
    public String dateCommande;
    public int dureeMax;
    public int etatCommande;
    public String epI;
    public String epII="";

    public void ajouterCommande() throws Exception {
        int nbCommandes=0;
        Connection connection=Main.getConnection(Main.user,Main.getPsw());
        Statement stmt= connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT max(id) from commande");
        if (rs!=null) rs.next();
        nbCommandes=rs.getInt("max(id)");
        nbCommandes++;
        int ep1=0;
        int ep2=0;
        if (epI.equals("Micro Cannelure")) ep1=1;
        else  if (epI.equals("Petite Cannelure")) ep1=2;
        else  if (epI.equals("Grande Cannelure")) ep1=3;

        if (epII.equals("Micro Cannelure")) ep2=1;
        else  if (epII.equals("Petite Cannelure")) ep2=2;
        else  if (epII.equals("Grande Cannelure")) ep2=3;

        String str="INSERT INTO commande (id,nomCommande,client,typee,ceType,ciG,firstCannG,ceG,secCannG,medG,longeur,largeur,nbPlaques,dateCommande,dureemax,etatCommande,epI,epII,nbPlaquesSave) VALUES("+ nbCommandes+",'"+nomCommande+"'"+",'"+client+"',"+typeCommande+",'"+ceType+"',"+ciG+","+firstCannG+","+ceG+","+secCannG+","+medG+","+longueur+","+largeur+","+nbPlaques+",'"+dateCommande+"',"+dureeMax+","+etatCommande+","+ep1+","+ep2+","+nbPlaques+")";
        System.out.println(str);
        int insert =stmt.executeUpdate(str);
        connection.close();
    }

    public static void main(String args[]) throws Exception {
        Commande c=new Commande();
        c.client="cevital";
        c.etatCommande=1;
        c.typeCommande=3;
        c.ceType="blanc";
        c.ciG=110;
        c.ceG=125;
        c.firstCannG=160;
        c.secCannG=160;
        c.medG=200;
        c.longueur=200;
        c.largeur=300;
        c.nbPlaques=50;
        c.dateCommande="05-09-2018";
        c.dureeMax=50;
        c.nomCommande="Sucre";
        c.ajouterCommande();




    }
}
