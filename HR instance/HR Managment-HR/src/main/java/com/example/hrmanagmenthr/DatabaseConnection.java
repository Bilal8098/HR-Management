package com.example.hrmanagmenthr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway"; 
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "NqlVODXIobgaOsHmwWHqXllPtOVOZril"; 

    // This method returns a Connection object for database interaction
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // This is a void method to test if the connection can be established
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Connection successful!");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    // Main method to test the connection
    public static void main(String[] args) {
        testConnection();
    }
}
