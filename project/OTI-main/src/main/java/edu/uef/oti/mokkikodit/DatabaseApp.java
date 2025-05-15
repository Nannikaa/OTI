package edu.uef.oti.mokkikodit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class DatabaseApp extends Application {

    // Database connection details
    private static final String DB_URL = "jdbc:sqlite:my_database.db";

    @Override
    public void start(Stage primaryStage) {
        // Create UI components
        Label titleLabel = new Label("Database Application");
        Button connectButton = new Button("Connect to Database");
        Button createTableButton = new Button("Create Table");
        TextArea outputArea = new TextArea();

        // Layout
        VBox root = new VBox(10, titleLabel, connectButton, createTableButton, outputArea);
        root.setPadding(new Insets(15));

        // Event handlers
        connectButton.setOnAction(e -> connectToDatabase(outputArea));
        createTableButton.setOnAction(e -> createTable(outputArea));

        // Set up scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("JavaFX Database App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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
