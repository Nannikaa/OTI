package edu.uef.oti.mokkikodit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class MultipleDatabaseApp extends Application {

    private static final String DB_URL = "jdbc:sqlite:store_management.db";
    private TextArea outputArea;

    @Override
    public void start(Stage primaryStage) {
        // Initialize database
        initializeDatabase();

        // Create tabs for each table
        TabPane tabPane = new TabPane();

        // Customer Tab
        Tab customerTab = new Tab("Customers");
        customerTab.setContent(createCustomerUI());

        // Product Tab
        Tab productTab = new Tab("Products");
        productTab.setContent(createProductUI());

        // Order Tab
        Tab orderTab = new Tab("Orders");
        orderTab.setContent(createOrderUI());

        // Reports Tab
        Tab reportTab = new Tab("Reports");
        reportTab.setContent(createReportUI());

        tabPane.getTabs().addAll(customerTab, productTab, orderTab, reportTab);

        // Output area
        outputArea = new TextArea();
        outputArea.setEditable(false);

        // Main layout
        VBox root = new VBox(10, tabPane, outputArea);
        root.setPadding(new Insets(15));

        // Set up scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Store Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Create customers table
            String createCustomers = "CREATE TABLE IF NOT EXISTS customers (" +
                    "customer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT UNIQUE," +
                    "phone TEXT," +
                    "address TEXT)";

            // Create products table
            String createProducts = "CREATE TABLE IF NOT EXISTS products (" +
                    "product_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT," +
                    "price REAL NOT NULL," +
                    "stock_quantity INTEGER NOT NULL)";

            // Create orders table
            String createOrders = "CREATE TABLE IF NOT EXISTS orders (" +
                    "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customer_id INTEGER NOT NULL," +
                    "order_date TEXT DEFAULT CURRENT_TIMESTAMP," +
                    "total_amount REAL," +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))";

            // Create order_details table
            String createOrderDetails = "CREATE TABLE IF NOT EXISTS order_details (" +
                    "detail_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id INTEGER NOT NULL," +
                    "product_id INTEGER NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "unit_price REAL NOT NULL," +
                    "FOREIGN KEY (order_id) REFERENCES orders(order_id)," +
                    "FOREIGN KEY (product_id) REFERENCES products(product_id))";

            stmt.execute(createCustomers);
            stmt.execute(createProducts);
            stmt.execute(createOrders);
            stmt.execute(createOrderDetails);

        } catch (SQLException e) {
            showError("Database initialization failed: " + e.getMessage());
        }
    }

    private GridPane createCustomerUI() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        // Form fields
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();

        // Buttons
        Button addBtn = new Button("Add Customer");
        Button viewBtn = new Button("View Customers");

        // Layout
        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Email:"), emailField);
        grid.addRow(2, new Label("Phone:"), phoneField);
        grid.addRow(3, new Label("Address:"), addressField);
        grid.addRow(4, addBtn, viewBtn);

        // Event handlers
        addBtn.setOnAction(e -> {
            String sql = "INSERT INTO customers(name, email, phone, address) VALUES(?,?,?,?)";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, emailField.getText());
                pstmt.setString(3, phoneField.getText());
                pstmt.setString(4, addressField.getText());
                pstmt.executeUpdate();
                showMessage("Customer added successfully!");
            } catch (SQLException ex) {
                showError("Error adding customer: " + ex.getMessage());
            }
        });

        viewBtn.setOnAction(e -> {
            String sql = "SELECT * FROM customers";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append("ID\tName\tEmail\tPhone\tAddress\n");
                sb.append("--------------------------------------------------\n");

                while (rs.next()) {
                    sb.append(rs.getInt("customer_id")).append("\t")
                            .append(rs.getString("name")).append("\t")
                            .append(rs.getString("email")).append("\t")
                            .append(rs.getString("phone")).append("\t")
                            .append(rs.getString("address")).append("\n");
                }

                outputArea.setText(sb.toString());
            } catch (SQLException ex) {
                showError("Error viewing customers: " + ex.getMessage());
            }
        });

        return grid;
    }

    private GridPane createProductUI() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        // Form fields
        TextField nameField = new TextField();
        TextField descField = new TextField();
        TextField priceField = new TextField();
        TextField stockField = new TextField();

        // Buttons
        Button addBtn = new Button("Add Product");
        Button viewBtn = new Button("View Products");

        // Layout
        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Description:"), descField);
        grid.addRow(2, new Label("Price:"), priceField);
        grid.addRow(3, new Label("Stock Quantity:"), stockField);
        grid.addRow(4, addBtn, viewBtn);

        // Event handlers
        addBtn.setOnAction(e -> {
            try {
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());

                String sql = "INSERT INTO products(name, description, price, stock_quantity) VALUES(?,?,?,?)";
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nameField.getText());
                    pstmt.setString(2, descField.getText());
                    pstmt.setDouble(3, price);
                    pstmt.setInt(4, stock);
                    pstmt.executeUpdate();
                    showMessage("Product added successfully!");
                }
            } catch (NumberFormatException ex) {
                showError("Please enter valid numbers for price and stock");
            } catch (SQLException ex) {
                showError("Error adding product: " + ex.getMessage());
            }
        });

        viewBtn.setOnAction(e -> {
            String sql = "SELECT * FROM products";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append("ID\tName\tDescription\tPrice\tStock\n");
                sb.append("--------------------------------------------------\n");

                while (rs.next()) {
                    sb.append(rs.getInt("product_id")).append("\t")
                            .append(rs.getString("name")).append("\t")
                            .append(rs.getString("description")).append("\t")
                            .append(rs.getDouble("price")).append("\t")
                            .append(rs.getInt("stock_quantity")).append("\n");
                }

                outputArea.setText(sb.toString());
            } catch (SQLException ex) {
                showError("Error viewing products: " + ex.getMessage());
            }
        });

        return grid;
    }

    private GridPane createOrderUI() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        // Form fields
        TextField customerIdField = new TextField();
        TextField productIdField = new TextField();
        TextField quantityField = new TextField();

        // Buttons
        Button createOrderBtn = new Button("Create Order");
        Button viewOrdersBtn = new Button("View Orders");

        // Layout
        grid.addRow(0, new Label("Customer ID:"), customerIdField);
        grid.addRow(1, new Label("Product ID:"), productIdField);
        grid.addRow(2, new Label("Quantity:"), quantityField);
        grid.addRow(3, createOrderBtn, viewOrdersBtn);

        // Event handlers
        createOrderBtn.setOnAction(e -> {
            try {
                int customerId = Integer.parseInt(customerIdField.getText());
                int productId = Integer.parseInt(productIdField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                // Start transaction
                Connection conn = DriverManager.getConnection(DB_URL);
                conn.setAutoCommit(false);

                try {
                    // 1. Get product price and check stock
                    double unitPrice = 0;
                    int currentStock = 0;

                    String checkStockSql = "SELECT price, stock_quantity FROM products WHERE product_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(checkStockSql)) {
                        pstmt.setInt(1, productId);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            unitPrice = rs.getDouble("price");
                            currentStock = rs.getInt("stock_quantity");
                        } else {
                            throw new SQLException("Product not found");
                        }
                    }

                    if (quantity > currentStock) {
                        throw new SQLException("Not enough stock available");
                    }

                    // 2. Create order header
                    String insertOrderSql = "INSERT INTO orders(customer_id, total_amount) VALUES(?, ?)";
                    double totalAmount = unitPrice * quantity;

                    int orderId;
                    try (PreparedStatement pstmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                        pstmt.setInt(1, customerId);
                        pstmt.setDouble(2, totalAmount);
                        pstmt.executeUpdate();

                        ResultSet rs = pstmt.getGeneratedKeys();
                        if (rs.next()) {
                            orderId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to get order ID");
                        }
                    }

                    // 3. Add order details
                    String insertDetailSql = "INSERT INTO order_details(order_id, product_id, quantity, unit_price) VALUES(?,?,?,?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(insertDetailSql)) {
                        pstmt.setInt(1, orderId);
                        pstmt.setInt(2, productId);
                        pstmt.setInt(3, quantity);
                        pstmt.setDouble(4, unitPrice);
                        pstmt.executeUpdate();
                    }

                    // 4. Update product stock
                    String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
                        pstmt.setInt(1, quantity);
                        pstmt.setInt(2, productId);
                        pstmt.executeUpdate();
                    }

                    conn.commit();
                    showMessage("Order created successfully! Order ID: " + orderId);

                } catch (SQLException ex) {
                    conn.rollback();
                    showError("Error creating order: " + ex.getMessage());
                } finally {
                    conn.setAutoCommit(true);
                    conn.close();
                }

            } catch (NumberFormatException ex) {
                showError("Please enter valid numbers for IDs and quantity");
            } catch (SQLException ex) {
                showError("Database error: " + ex.getMessage());
            }
        });

        viewOrdersBtn.setOnAction(e -> {
            String sql = "SELECT o.order_id, c.name AS customer_name, o.order_date, o.total_amount " +
                    "FROM orders o JOIN customers c ON o.customer_id = c.customer_id";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append("Order ID\tCustomer\tDate\t\tTotal\n");
                sb.append("--------------------------------------------------\n");

                while (rs.next()) {
                    sb.append(rs.getInt("order_id")).append("\t")
                            .append(rs.getString("customer_name")).append("\t")
                            .append(rs.getString("order_date")).append("\t")
                            .append(rs.getDouble("total_amount")).append("\n");
                }

                outputArea.setText(sb.toString());
            } catch (SQLException ex) {
                showError("Error viewing orders: " + ex.getMessage());
            }
        });

        return grid;
    }

    private VBox createReportUI() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        Button salesReportBtn = new Button("Sales Report");
        Button stockReportBtn = new Button("Stock Report");
        Button customerOrdersBtn = new Button("Customer Orders");

        salesReportBtn.setOnAction(e -> {
            String sql = "SELECT p.name AS product_name, SUM(od.quantity) AS total_sold, " +
                    "SUM(od.quantity * od.unit_price) AS total_revenue " +
                    "FROM order_details od JOIN products p ON od.product_id = p.product_id " +
                    "GROUP BY p.name ORDER BY total_revenue DESC";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append("Product\t\tTotal Sold\tRevenue\n");
                sb.append("--------------------------------------------------\n");

                while (rs.next()) {
                    sb.append(String.format("%-15s\t%d\t\t$%.2f\n",
                            rs.getString("product_name"),
                            rs.getInt("total_sold"),
                            rs.getDouble("total_revenue")));
                }

                outputArea.setText(sb.toString());
            } catch (SQLException ex) {
                showError("Error generating sales report: " + ex.getMessage());
            }
        });

        stockReportBtn.setOnAction(e -> {
            String sql = "SELECT name, stock_quantity FROM products ORDER BY stock_quantity ASC";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append("Product\t\tStock\n");
                sb.append("------------------------\n");

                while (rs.next()) {
                    sb.append(String.format("%-15s\t%d\n",
                            rs.getString("name"),
                            rs.getInt("stock_quantity")));
                }

                outputArea.setText(sb.toString());
            } catch (SQLException ex) {
                showError("Error generating stock report: " + ex.getMessage());
            }
        });

        customerOrdersBtn.setOnAction(e -> {
            String sql = "SELECT c.name AS customer_name, COUNT(o.order_id) AS order_count, " +
                    "SUM(o.total_amount) AS total_spent " +
                    "FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id " +
                    "GROUP BY c.name ORDER BY total_spent DESC";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append("Customer\t\tOrders\tTotal Spent\n");
                sb.append("--------------------------------------------\n");

                while (rs.next()) {
                    sb.append(String.format("%-15s\t%d\t$%.2f\n",
                            rs.getString("customer_name"),
                            rs.getInt("order_count"),
                            rs.getDouble("total_spent")));
                }

                outputArea.setText(sb.toString());
            } catch (SQLException ex) {
                showError("Error generating customer report: " + ex.getMessage());
            }
        });

        box.getChildren().addAll(salesReportBtn, stockReportBtn, customerOrdersBtn);
        return box;
    }

    private void showMessage(String message) {
        outputArea.appendText(message + "\n");
    }

    private void showError(String error) {
        outputArea.appendText("ERROR: " + error + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
