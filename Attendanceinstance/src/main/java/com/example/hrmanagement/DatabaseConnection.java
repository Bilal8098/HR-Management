package com.example.hrmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres"; // Replace with your database username
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril"; // Replace with your database password
        
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Establish connection
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found: " + e.getMessage());
        }
    }
}
