package edu.uef.oti.mokkikodit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Asiakaalle extends Application {
    private static final String DB_URL = "jdbc:sqlite:mokkikodit.db";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Database Application");
        Button connectButton = new Button("Connect to Database");
        Button createTableButton = new Button("Create Table");
        TextArea outputArea = new TextArea();

        VBox root = new VBox(10, titleLabel, connectButton, createTableButton, outputArea);
        root.setPadding(new Insets(15));

        connectButton.setOnAction(e -> connectToDatabase(outputArea));
        createTableButton.setOnAction(e -> createTable(outputArea));

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("JavaFX Database App");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void connectToDatabase(TextArea outputArea) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            outputArea.appendText("Connected to database successfully!\n");
            connection.close();
        } catch (SQLException e) {
            outputArea.appendText("Connection failed: " + e.getMessage() + "\n");
        }
    }

    private void createTable(TextArea outputArea) {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE," +
                "age INTEGER)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            outputArea.appendText("Table created successfully!\n");
        } catch (SQLException e) {
            outputArea.appendText("Error creating table: " + e.getMessage() + "\n");
        }
    }
}
