package edu.uef.oti.mokkikodit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import java.util.List;

public class MokkiKoditUI extends Application {
    private TextArea tulostusAlue;


    @Override
    public void start(Stage primaryStage) {
        Database.createTables();

        //Luodaan välilehti jokaiselle taululle
        TabPane valiLehti = new TabPane();
        valiLehti.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        //Asiakas taulu
        Tab asiakasTaulu = new Tab("Asiakas");
        asiakasTaulu.setContent(luoAsiakasUI());

        //Mökki taulu
        Tab mokkiTaulu = new Tab("Mökit");
        mokkiTaulu.setContent(luoMokkiUI());

        //Varaus taulu
        Tab varausTaulu = new Tab("Varaukset");
        varausTaulu.setContent(luoVarausUI());

        //Lasku Taulu
        Tab laskuTaulu = new Tab("Laskut");
        laskuTaulu.setContent(luoLaskuUI());

        //Raportti taulu
        Tab raporttiTaulu = new Tab("Raportti");
        raporttiTaulu.setContent(luoRaporttiUI());

        valiLehti.getTabs().addAll(asiakasTaulu,mokkiTaulu,varausTaulu,laskuTaulu,raporttiTaulu);

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
            if (nimiKentta.getText().isEmpty() || emailKentta.getText().isEmpty() || puehlinKentta.getText().isEmpty()){
                naytaVirhe("Kaikki kentät ovat pakollisia");
                return;
            }
            Database.addAsiakas(nimiKentta.getText(), emailKentta.getText(), puehlinKentta.getText());
            naytaAsiakkaat();
        });

        naytaPainike.setOnAction(e -> {
            naytaAsiakkaat();
        });

        return grid;
    }

    private void naytaAsiakkaat() {
        tulostusAlue.clear();
        Database.getAllAsiakkaat().forEach(asiakas -> tulostusAlue.appendText(asiakas + "\n"));
    }

    private GridPane luoMokkiUI(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        // Form fields
        TextField nimiKentta = new TextField();
        TextField sijaintiKentta= new TextField();
        TextField hintaKentta = new TextField();
        TextField kuvausKentta = new TextField();

        // Buttons
        Button lisaaPainike = new Button("Lisää Mökki");
        Button katsoPainike = new Button("Tarkastele Mökkejä");

        // Layout
        grid.addRow(0, new Label("Nimi:"), nimiKentta);
        grid.addRow(1, new Label("Sijainti:"), sijaintiKentta);
        grid.addRow(2, new Label("Hinta per yö:"), hintaKentta);
        grid.addRow(3, new Label("Kuvaus:"), kuvausKentta);
        grid.addRow(4,lisaaPainike, katsoPainike);

        lisaaPainike.setOnAction(e ->{
            if (nimiKentta.getText().isEmpty() || sijaintiKentta.getText().isEmpty() || hintaKentta.getText().isEmpty() || kuvausKentta.getText().isEmpty()){
                naytaVirhe("Kaikki kentät ovat pakollisia");
                return;
            }
            Database.addMokki(nimiKentta.getText(), sijaintiKentta.getText(), Double.parseDouble(hintaKentta.getText()), kuvausKentta.getText());
            naytaMokit();
        });

        katsoPainike.setOnAction(e -> {
            naytaMokit();
        });

        return grid;
    }

    private void naytaMokit() {
        tulostusAlue.clear();
        Database.getAllMokit().forEach(mokki -> tulostusAlue.appendText(mokki + "\n"));
    }

    private GridPane luoVarausUI(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        //Kirjoituskenttien luominen
        TextField asiakasIdKentta = new TextField();
        TextField mokkiIdKentta = new TextField();
        TextField alkuPvmKentta = new TextField();
        TextField loppuPvmKentta = new TextField();

        //Painikkeet
        Button luoVarausPainike = new Button("Luo varaus");
        Button naytaVaraukset = new Button("Näytä varaukset");

        //Asettelu

        grid.addRow(0,new Label("Asiakas ID:"), asiakasIdKentta);
        grid.addRow(1,new Label("Mökki ID:"), mokkiIdKentta);
        grid.addRow(2,new Label("Alku Pvm:"), alkuPvmKentta);
        grid.addRow(3,new Label("Loppu Pvm:"), loppuPvmKentta);
        grid.addRow(4,luoVarausPainike,naytaVaraukset);

        //Tapahtumienkäsittely

        luoVarausPainike.setOnAction(e -> {
            try {
                if (asiakasIdKentta.getText().isEmpty() || mokkiIdKentta.getText()
                    .isEmpty() || alkuPvmKentta.getText().isEmpty() || loppuPvmKentta.getText()
                    .isEmpty()) {
                    naytaVirhe("Kaikki kentät ovat pakollisia");
                    return;
                }
                String result = Database.addVaraus(Integer.parseInt(asiakasIdKentta.getText()),
                    Integer.parseInt(mokkiIdKentta.getText()),
                    java.sql.Date.valueOf(alkuPvmKentta.getText()),
                    java.sql.Date.valueOf(loppuPvmKentta.getText()));
                if (result != null) {
                    naytaVirhe(result);
                } else {
                    naytaVaraus();
                }
            } catch (NumberFormatException ex) {
                naytaVirhe("Asiakas ID ja Mökki ID pitää olla numeroita");
            } catch (IllegalArgumentException ex) {
                naytaVirhe("Syötä numero muodossa YYYY-MM-DD");
            }
        });

        naytaVaraukset.setOnAction(e ->{
            naytaVaraus();
        });
        return grid;
    }

    private void naytaVaraus() {
        tulostusAlue.clear();
        Database.getAllVaraukset().forEach(varaus -> tulostusAlue.appendText(varaus + "\n"));
    }

    private GridPane luoLaskuUI(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        //Kirjoituskenttien luominen
        TextField varausIdKentta = new TextField();
        TextField summaKentta = new TextField();
        TextField maksettuKentta = new TextField();
        TextField MaksuPvmKentta = new TextField();

        //Painikkeet
        Button luoLaskuPainike = new Button("Luo lasku");
        Button naytaLaskut = new Button("Näytä laskut");

        //Asettelu

        grid.addRow(0,new Label("Varaus ID:"), varausIdKentta);
        grid.addRow(1,new Label("Summa:"), summaKentta);
        grid.addRow(2,new Label("Maksettu:"), maksettuKentta);
        grid.addRow(3,new Label("Maksu Pvm:"), MaksuPvmKentta);
        grid.addRow(4,luoLaskuPainike,naytaLaskut);

        //Tapahtumienkäsittely

        luoLaskuPainike.setOnAction(e -> {
            try {
            if (varausIdKentta.getText().isEmpty() || summaKentta.getText().isEmpty() || maksettuKentta.getText().isEmpty()){
                naytaVirhe("Kaikki kentät ovat pakollisia");
                return;
            }

            String maksettuInput = maksettuKentta.getText().trim().toLowerCase();
            boolean maksettu;
           if (maksettuInput.equals("true") || maksettuInput.equals("kyllä")) {
                maksettu = true;
            } else if (maksettuInput.equals("false") || maksettuInput.equals("ei")) {
                maksettu = false;
            } else {
                naytaVirhe("Maksettu kenttä pitää olla true/false tai kyllä/ei");
                return;
            }

           Date maksuPvm;
           try {
               maksuPvm = Date.valueOf(MaksuPvmKentta.getText());
              } catch (IllegalArgumentException ex) {
                naytaVirhe("Syötä päivämäärä muodossa YYYY-MM-DD");
                return;
           }

            boolean success = Database.addLasku(Integer.parseInt(varausIdKentta.getText()), Double.parseDouble(summaKentta.getText()),
                maksettu, maksuPvm);
            if (!success) {
                naytaVirhe("Varausta ei löydy");
                return;
            }
            naytaLaskut();

            } catch (NumberFormatException ex) {
                naytaVirhe("Varaus ID ja summa pitää olla numeroita");
            }


        });

        naytaLaskut.setOnAction(e -> {
            naytaLaskut();
        });

        return grid;
    }

    private void naytaLaskut() {
        tulostusAlue.clear();
        Database.getAllLaskut().forEach(lasku -> tulostusAlue.appendText(lasku + "\n"));
    }

    private GridPane luoRaporttiUI(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        TextField alkuPvmKentta = new TextField();
        TextField loppuPvmKentta = new TextField();
        Button tulostaRaporttiPainike = new Button("Tulosta raportti");

        grid.addRow(0,new Label("Alku Pvm:"), alkuPvmKentta);
        grid.addRow(1,new Label("Loppu pvm:"), loppuPvmKentta);
        grid.addRow(3,tulostaRaporttiPainike);

        tulostaRaporttiPainike.setOnAction(e-> {
            try {
                if (alkuPvmKentta.getText().isEmpty() || loppuPvmKentta.getText().isEmpty()) {
                    naytaVirhe("Kaikki kentät ovat pakollisia");
                    return;
                }

                Date alkuPvm = Date.valueOf(alkuPvmKentta.getText());
                Date loppuPvm = Date.valueOf(loppuPvmKentta.getText());

                List<String> raportti = Database.getVarauksetRaportti(alkuPvm, loppuPvm);
                tulostusAlue.clear();
                if (raportti.isEmpty()) {
                    naytaViesti("Ei varauksia tälle aikavälille");
                } else {
                    raportti.forEach(rivi -> tulostusAlue.appendText(rivi + "\n"));
                }
            } catch (IllegalArgumentException ex) {
                naytaVirhe("Syötä päivämäärä muodossa YYYY-MM-DD");
            }
        });

        return grid;

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




































