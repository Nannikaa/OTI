package edu.uef.oti.mokkikodit.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class YhdistysTietokantaan {
    private static final String URL = "jdbc:mysql://localhost:3306/Mokkikodit";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static Connection getConnection()  throws SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL,USER,PASS);
        } catch (ClassNotFoundException e){
            throw new SQLException("MySQL JDBC Driver not found", e);
        }

    }
}
