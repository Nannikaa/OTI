package edu.uef.oti.mokkikodit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class MokkiKoditUI extends Application {
    private static final String DB_URL = "jdbc:sqlite:mokkikodit.db";
    private TextArea tulostusAlue;


    @Override
    public void start(Stage primaryStage) {


        //Luodaan välilehti jokaiselle taululle
        TabPane valiLehti = new TabPane();

        //Asiakas taulu
        Tab asiakasTaulu = new Tab("Asiakas");
        asiakasTaulu.setContent(luoAsiakasUI());

        //Mökki taulu
        Tab mokkiTaulu = new Tab("Mökit");
        mokkiTaulu.setContent(luoMokkiUI());

        //Varaus taulu
        Tab varausTaulu = new Tab("Varaukset");
        varausTaulu.setContent(luoVarausUI());

        //Raportti taulu
        Tab raporttiTaulu = new Tab("Raportti");
        raporttiTaulu.setContent(luoRaporttiUI());

        valiLehti.getTabs().addAll(asiakasTaulu,mokkiTaulu,varausTaulu,raporttiTaulu);

        //tulostusAlue
        tulostusAlue = new TextArea();
        tulostusAlue.setEditable(false);

        //Pää asettelu
        VBox vbox = new VBox(10,valiLehti,tulostusAlue);
        vbox.setPadding(new Insets(15));

        //Kehyksen asettelu
        Scene kehys = new Scene(vbox,800,600);
        primaryStage.setTitle("Mökkikoti varausjärjestelmä");
        primaryStage.setScene(kehys);
        primaryStage.show();


    }

    private void createTables(){
        try(Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmnt = conn.createStatement()){

            //Luodaan Asiakas taulu
            String createAsiakasTable = "CREATE TABLE IF NOT EXISTS Asiakas (" +
            "asiakas_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "nimi TEXT NOT NULL,"+
            "email TEXT UNIQUE,"+
            "puhelin TEXT)";

            //Luodaan taulu mökeille
            String createMokkiTable = "CREATE TABLE IF NOT EXISTS mokit ("+
                    "mokki_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "nimi TEXT NOT NULL,"+
                    "kuvaus TEXT,"+
                    "sijainti TEXT,"+
                    "hinta TEXT," +
                    "mokkeja_vapaana INTEGER NOT NULL)";

            //Luodaan varaus taulu
            String createVarausTaulu = "CREATE TABLE IF NOT EXISTS varaus ("+
                    "varaus_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "asiakas_id INTEGER NOT NULL, "+
                    "varaus_paiva TEXT DEFAULT CURRENT_TIMESTAMP, "+
                    "kokonaistuotot REAL,"+
                    "FOREIGN KEY (asiakas_id) REFERENCES Asiakas(asiakas_id))";

            //Luodaan lasku taulu
            String createLaskuTable = "CREATE TABLE IF NOT EXISTS lasku ("+
                    "lasku_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "varaus_id INTEGER NOT NULL,"+
                    "mokki_id INTEGER NOT NULL,"+
                    "maara INTEGER NOT NULL,"+
                    "mokki_hinta REAL NOT NULL,"+
                    "FOREIGN KEY (vauraus_id) REFERENCES varaus(varaus_id),"+
                    "FOREIGN KEY (mokki_id) REFERENCES mokit(mokki_id))";

            stmnt.execute(createAsiakasTable);
            stmnt.execute(createMokkiTable);
            stmnt.execute(createVarausTaulu);
            stmnt.execute(createLaskuTable);
        }catch (SQLException e){
            naytaVirhe("Tietokantaa luominen epäonnistui: "+ e.getMessage());
        }

    }

    private GridPane luoAsiakasUI(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        //Kirjoituskenttien luominen
        TextField nimiKentta = new TextField();
        TextField emailKentta = new TextField();
        TextField puehlinKentta = new TextField();

        //Painikkeet
        Button lisaaPainike = new Button("Lisää asiakas");
        Button naytaPainike = new Button("Näytä asiakkaat");

        //Asettelu

        grid.addRow(0,new Label("Nimi:"), nimiKentta);
        grid.addRow(1,new Label("Email:"), emailKentta);
        grid.addRow(2,new Label("Puhelin:"), puehlinKentta);
        grid.addRow(3,lisaaPainike,naytaPainike);

        //Tapahtumien käsittely
        lisaaPainike.setOnAction(e ->{
            String sql = "INSERT INTO asiakas(nimi,email,puhelin) VALUES(?,?,?)";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1,nimiKentta.getText());
                pstmt.setString(2,emailKentta.getText());
                pstmt.setString(3,puehlinKentta.getText());
                pstmt.execute();
                naytaViesti("Asiakas on lisätty!");
            } catch (SQLException ex){
                naytaVirhe("Asiakas lisääminen epäonnistui: "+ ex.getMessage());
            }
        });

        naytaPainike.setOnAction(e -> {
            String sql = "SELECT * FROM Asiakas";
            try(Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery(sql)){

                StringBuilder sb = new StringBuilder();
                sb.append("ID\tEmail\tPuhelin\n");
                sb.append("--------------------------------------------------\n");

                while(rs.next()){
                    sb.append(rs.getInt("asiakas_id")).append("\t")
                            .append(rs.getString("nimi")).append("\t")
                            .append(rs.getString("email")).append("\t")
                            .append(rs.getString("puhelin")).append("\n");
                }
                tulostusAlue.setText(sb.toString());

            } catch(SQLException ex){
                naytaVirhe("Asiakkaiden näyttäminen epäonnistui" + ex.getMessage());
            }
        });

        return grid;
    }

    private GridPane luoMokkiUI(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        // Form fields
        TextField nimiKentta = new TextField();
        TextField kuvausKentta= new TextField();
        TextField hintaKentta = new TextField();
        TextField maaraKentta = new TextField();

        // Buttons
        Button lisaaPainike = new Button("Lisää Mökki");
        Button katsoPainike = new Button("Tarkastele Mökkejä");

        // Layout
        grid.addRow(0, new Label("Nimi:"), nimiKentta);
        grid.addRow(1, new Label("Kuvaus:"), kuvausKentta);
        grid.addRow(2, new Label("Hinta:"), hintaKentta);
        grid.addRow(3, new Label("Mökkien määrä:"), maaraKentta);
        grid.addRow(4,lisaaPainike, katsoPainike);

        lisaaPainike.setOnAction(e ->{
            try {
                double hinta = Double.parseDouble(hintaKentta.getText());
                int maara = Integer.parseInt(maaraKentta.getText());

                String sql = "INSERT INTO mokit(nimi, kuvaus, hinta, mokkeja_vapaana) VALUES(?,?,?,?)";
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nimiKentta.getText());
                    pstmt.setString(2, kuvausKentta.getText());
                    pstmt.setDouble(3, hinta);
                    pstmt.setInt(4, maara);
                    pstmt.executeUpdate();
                    naytaViesti("Mökki on lisätty");
                }
            } catch (NumberFormatException ex) {
                naytaVirhe("Please enter valid numbers for price and stock");
            } catch (SQLException ex) {
                naytaVirhe("Error adding product: " + ex.getMessage());
            }
        });

        katsoPainike.setOnAction(e -> {
            String sql = "SELECT * FROM mokit";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append("ID\tNimi\tKuvaus\tHinta\tMaara\n");
                sb.append("--------------------------------------------------\n");

                while (rs.next()) {
                    sb.append(rs.getInt("mokki_id")).append("\t")
                            .append(rs.getString("nimi")).append("\t")
                            .append(rs.getString("kuvaus")).append("\t")
                            .append(rs.getDouble("hinta")).append("\t")
                            .append(rs.getInt("mokkeja_vapaana")).append("\n");
                }

                tulostusAlue.setText(sb.toString());
            } catch (SQLException ex) {
                naytaVirhe("Error viewing products: " + ex.getMessage());
            }
        });

        return grid;
    }

    private GridPane luoVarausUI(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        //Kirjoituskenttien luominen
        TextField asiakasIdKentta = new TextField();
        TextField mokkiIdKentta = new TextField();
        TextField maaraKentta = new TextField();

        //Painikkeet
        Button luoVarausPainike = new Button("Luo varaus");
        Button naytaVaraukset = new Button("Näytä varaukset");

        //Asettelu

        grid.addRow(0,new Label("Asiakas ID:"), asiakasIdKentta);
        grid.addRow(1,new Label("Mökki ID:"), mokkiIdKentta);
        grid.addRow(2,new Label("Määrä:"), maaraKentta);
        grid.addRow(3,luoVarausPainike,naytaVaraukset);

        //Tapahtumienkäsittely

        luoVarausPainike.setOnAction(e ->{
            try{
                int asiakasID = Integer.parseInt(asiakasIdKentta.getText());
                int mokkiId = Integer.parseInt(mokkiIdKentta.getText());
                int maara = Integer.parseInt(maaraKentta.getText());

                //Varauksen hallinta
                Connection conn = DriverManager.getConnection(DB_URL);
                conn.setAutoCommit(false);

                try{
                    //1. Tarkasta hinta
                    double mokinHinta =0;
                    int mokkienMaara = 0;

                    String katsoHinta = "SELECT hinta, mokkeja_vapaana FROM mokit WHERE mokki_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(katsoHinta)){
                        pstmt.setInt(1,mokkiId);
                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next()){
                            mokinHinta = rs.getDouble("hinta");
                            mokkienMaara = rs.getInt("mokkeja_vapaana");
                        }

                        else {
                            throw new SQLException("Mökkiä ei löydy");
                        }
                    } if(maara > mokkienMaara) {
                        throw new SQLException("Mökkejä ei ole varattavissa");
                    }

                    //2. Luo varaus otsikko
                    String lisaaVarausSql = "INSERT INTO varaus(asiakas_id, kokonaistuotot) VALUES(?,?)";
                    double kokonaisHinta = mokinHinta * mokkienMaara;

                    int varausID;
                    try (PreparedStatement pstmt = conn.prepareStatement(lisaaVarausSql,Statement.RETURN_GENERATED_KEYS)){
                        pstmt.setInt(1,asiakasID);
                        pstmt.setDouble(2,kokonaisHinta);
                        pstmt.executeUpdate();

                        ResultSet rs = pstmt.getGeneratedKeys();
                        if(rs.next()){
                            varausID = rs.getInt(1);
                        }else{
                            throw new SQLException("Varaus ID: noutaminen epäonnistui");
                        }

                    }

                    //3. Lisätään lasku
                    String lisaaLaskuSql = "INSERT INTO lasku(lasku_id, mokki_id, maara, mokki_hinta) VALUES(?,?,?,?)";
                    try(PreparedStatement pstmt = conn.prepareStatement(lisaaLaskuSql)){
                        pstmt.setInt(1,varausID);
                        pstmt.setInt(2,mokkiId);
                        pstmt.setInt(3,maara);
                        pstmt.setDouble(4,mokinHinta);
                    }

                    // 4. Päivitä
                    String paivitaMaaraSql = "UPDATE mokit SET maara = maara - ? WHERE mokki_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(paivitaMaaraSql)) {
                        pstmt.setInt(1, maara);
                        pstmt.setInt(2, mokkiId);
                        pstmt.executeUpdate();
                    }

                    conn.commit();
                    naytaViesti("Varaus on luoto. Varaus ID" + varausID);
                } catch (SQLException ex){
                    conn.rollback();
                    naytaVirhe("Virhe varauksen luomisessa" + ex.getMessage());
                } finally {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (NumberFormatException ex){
                naytaVirhe("Syötä numero");
            } catch (SQLException ex){
                naytaVirhe("Tietokanta virhe: " + ex.getMessage());
            }
        });

        naytaVaraukset.setOnAction(e ->{
            String sql = "SELECT o.varaus_id, c.nimi AS asiakkaan_nimi, o.varaus_paiva, o.kokonaistuotot"+
                    "FROM varaus o JOIN Asiakas c ON o.asiakas_id = c.asiakas_id";

            try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery(sql)){

                StringBuilder sb = new StringBuilder();
                sb.append("Varaus ID\tAsiakas\tPäivämäärä\t\tKokonaishinta\n");
                sb.append("--------------------------------------------------\n");

                while (rs.next()){
                    sb.append(rs.getString("varaus_id")).append("\t")
                            .append(rs.getString("asiakkaan_nimi")).append("\t")
                            .append(rs.getString("varaus_paiva")).append("\t")
                            .append(rs.getDouble("kokonaistuotot")).append("\n");
                }

                tulostusAlue.setText(sb.toString());

            } catch (SQLException ex){
                naytaVirhe("Virhe varauksen katsomisessa: "+ ex.getMessage());
            }
        });
        return grid;
    }

    private VBox luoRaporttiUI(){
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        Button myyntiRaporttiPainike = new Button("Myynti Raportti");
        Button maaraRaportti = new Button("Mökkien määrä");
        Button asiakkaidenVaraukset= new Button("Asiakkaiden varaukset");

        myyntiRaporttiPainike.setOnAction(e-> {
            String sql = "SELECT p.nimi AS mökin_nimi, SUM(od.maara) AS myydyt_mokit, "+
                    "SUM(od.maara * od.mokki_hinta) AS kokonaistuotto "+
                    "FROM lasku od JOIN mokit p ON od.mokki_id = p.mokki_id " +
                    "GROUP BY p.nimi ORDER BY kokonaistuotto DESC";


            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append("Mökki\t\tMyyty\tTuotot\n");
                sb.append("--------------------------------------------------\n");

                while (rs.next()) {
                    sb.append(String.format("%-15s\t%d\t\t$%.2f\n",
                            rs.getString("mökin_nimi"),
                            rs.getInt("myydyt_mokit"),
                            rs.getDouble("kokonaistuotto")));
                }

                tulostusAlue.setText(sb.toString());
            } catch (SQLException ex) {
                naytaVirhe("Virhe kun raportoinnin generoimisessa " + ex.getMessage());
            }
        });

        maaraRaportti.setOnAction(e -> {
            String sql = "SELECT nimi, mokkeja_vapaana FROM mokit ORDER BY mokkeja_vapaana ASC";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append("Mökki\t\tMäärä\n");
                sb.append("------------------------\n");

                while (rs.next()) {
                    sb.append(String.format("%-15s\t%d\n",
                            rs.getString("nimi"),
                            rs.getInt("mokkeja_vapaana")));
                }

                tulostusAlue.setText(sb.toString());
            } catch (SQLException ex) {
                naytaVirhe("Virhe mökkien generoimisessa " + ex.getMessage());
            }
        });

        asiakkaidenVaraukset.setOnAction(e -> {
            String sql = "SELECT c.nimi AS asiakkaan_nimi, COUNT(o.varaus_id) AS varaus_rivi, "+
                    "SUM(o.kokonaistuottot) AS kokonaiskustannus" +
                    "FROM Asiakas c LEFT JOIN varaus o ON c.asiakas_id = o.asiakas_id"+
                    "GROUP BY c.nimi ORDER BY kokonaiskustannus DESC";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append("Asiakas\t\tVaraukset\tKokonaiskustannus\n");
                sb.append("--------------------------------------------\n");

                while (rs.next()) {
                    sb.append(String.format("%-15s\t%d\t$%.2f\n",
                            rs.getString("asiakkaan_nimi"),
                            rs.getInt("varaus_rivi"),
                            rs.getDouble("kokonaiskustannus")));
                }

                tulostusAlue.setText(sb.toString());
            } catch (SQLException ex) {
                naytaVirhe("Virhe asiakkaiden raportin generoimisessa: " + ex.getMessage());
            }
        });

        box.getChildren().addAll(myyntiRaporttiPainike,maaraRaportti,asiakkaidenVaraukset);
        return box;

    }

    private void naytaViesti(String viesti) {
        tulostusAlue.appendText(viesti + "\n");
    }

    private void naytaVirhe(String error) {
        tulostusAlue.appendText("ERROR: " + error + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}




































