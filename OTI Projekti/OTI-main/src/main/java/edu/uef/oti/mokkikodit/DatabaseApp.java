package edu.uef.oti.mokkikodit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class DatabaseApp extends Application {

    private static final String DB_URL = "jdbc:sqlite:contacts.db";
    private TextField nameField, emailField, ageField;
    private TextArea outputArea;

    @Override
    public void start(Stage primaryStage) {
        // Create form fields
        Label nameLabel = new Label("Name:");
        nameField = new TextField();

        Label emailLabel = new Label("Email:");
        emailField = new TextField();

        Label ageLabel = new Label("Age:");
        ageField = new TextField();

        // Create buttons
        Button addButton = new Button("Add Contact");
        Button viewButton = new Button("View Contacts");
        Button clearButton = new Button("Clear");

        // Output area
        outputArea = new TextArea();
        outputArea.setEditable(false);

        // Form layout
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));
        form.addRow(0, nameLabel, nameField);
        form.addRow(1, emailLabel, emailField);
        form.addRow(2, ageLabel, ageField);
        form.addRow(3, addButton, viewButton, clearButton);

        // Main layout
        VBox root = new VBox(10, form, outputArea);
        root.setPadding(new Insets(15));

        // Event handlers
        addButton.setOnAction(e -> addContact());
        viewButton.setOnAction(e -> viewContacts());
        clearButton.setOnAction(e -> clearFields());

        // Initialize database
        createTables();

        // Set up scene
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("Contacts Database");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void createTables() {
        String createAsiakasTable = """
            CREATE TABLE IF NOT EXISTS Asiakas (
                asiakas_id INTEGER PRIMARY KEY AUTOINCREMENT,
                nimi TEXT NOT NULL,
                email TEXT,
                puhelin TEXT
            );
        """;

        String createMokkiTable = """
            CREATE TABLE IF NOT EXISTS Mokki (
                mokki_id INTEGER PRIMARY KEY AUTOINCREMENT,
                nimi TEXT NOT NULL,
                sijainti TEXT NOT NULL,
                hinta_per_yö REAL NOT NULL,
                kuvaus TEXT
            );
        """;

        String createVarausTable = """
            CREATE TABLE IF NOT EXISTS Varaus (
                varaus_id INTEGER PRIMARY KEY AUTOINCREMENT,
                asiakas_id INTEGER,
                mokki_id INTEGER,
                alku_pvm DATE,
                loppu_pvm DATE,
                FOREIGN KEY (asiakas_id) REFERENCES Asiakas(asiakas_id),
                FOREIGN KEY (mokki_id) REFERENCES Mokki(mokki_id)
            );
        """;

        String createLaskuTable = """
            CREATE TABLE IF NOT EXISTS Lasku (
                lasku_id INTEGER PRIMARY KEY AUTOINCREMENT,
                varaus_id INTEGER,
                summa REAL,
                maksettu BOOLEAN,
                FOREIGN KEY (varaus_id) REFERENCES Varaus(varaus_id)
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            stmt.execute(createAsiakasTable);
            stmt.execute(createMokkiTable);
            stmt.execute(createVarausTable);
            stmt.execute(createLaskuTable);
            System.out.println("Tietokantataulut luotu tai olivat jo olemassa.");
        } catch (SQLException e) {
            System.out.println("Virhe tauluja luotaessa: " + e.getMessage());
        }
    }

    private void addContact() {
        String name = nameField.getText();
        String email = emailField.getText();
        String ageText = ageField.getText();

        if (name.isEmpty() || email.isEmpty()) {
            outputArea.appendText("Name and email are required!\n");
            return;
        }

        try {
            int age = ageText.isEmpty() ? 0 : Integer.parseInt(ageText);

            String sql = "INSERT INTO contacts(name, email, age) VALUES(?,?,?)";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setInt(3, age);
                pstmt.executeUpdate();
                outputArea.appendText("Contact added successfully!\n");
                clearFields();
            }
        } catch (NumberFormatException e) {
            outputArea.appendText("Age must be a number!\n");
        } catch (SQLException e) {
            outputArea.appendText("Error adding contact: " + e.getMessage() + "\n");
        }
    }

    private void viewContacts() {
        outputArea.clear();
        String sql = "SELECT id, name, email, age FROM contacts";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            outputArea.appendText(String.format("%-5s %-20s %-30s %-5s\n",
                    "ID", "Name", "Email", "Age"));
            outputArea.appendText("----------------------------------------------------\n");

            while (rs.next()) {
                outputArea.appendText(String.format("%-5d %-20s %-30s %-5d\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("age")));
            }
        } catch (SQLException e) {
            outputArea.appendText("Error viewing contacts: " + e.getMessage() + "\n");
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        ageField.clear();
    }

    public static void main(String[] args) {
        Application.launch(DatabaseApp.class, args);
    }

}
