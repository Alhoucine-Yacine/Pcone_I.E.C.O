import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static Stage stage;
    private static final String CONFIG_FILE = "config.txt";

    public static String user;
    private static String psw;


    public static void setPsw(String psw) {
        Main.psw = psw;
    }

    public static String getPsw() {
        return psw;
    }

    public static String getServerIp() throws IOException {
        FileReader fr = new FileReader(CONFIG_FILE);
        BufferedReader br = new BufferedReader(fr);
        String ip=br.readLine();
        String port=br.readLine();
        return ip+","+port;

    }


    public static Connection getConnection(String name, String pass) throws Exception {
        String currentLine[] = getServerIp().split(",");


        String driver = "com.mysql.cj.jdbc.Driver";
        System.out.println(currentLine[0]+","+currentLine[1]);
        String url = "jdbc:mysql://" + currentLine[0] + ":"+currentLine[1]+"/myapp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
        String username = name;
        String passwd = pass;
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, username, passwd);
        return conn;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("PC one");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setX(0);
        primaryStage.setY(0);

        primaryStage.show();

        stage = primaryStage;
    }

    public void close(Stage primaryStage) {
        stage.close();
    }

    public Stage getStage() {

        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
