package com.example.hrmanagementmanager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AnnounceMessageController implements Initializable {
    @FXML
    private Button send;
    @FXML private TextArea message;

    private Connection connectDB() {
        // Update your DB details here
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    send.setOnAction(e -> insertMessage());
    }

    private void insertMessage() {
    Connection conn = connectDB();
    if (conn == null) {
        System.out.println("Database connection failed.");
        return;
    }

    String msgText = message.getText();
    if (msgText == null || msgText.trim().isEmpty()) {
        System.out.println("Message is empty. Not inserting.");
        return;
    }

    String query = "INSERT INTO messages (message_text, created_at) VALUES (?, CURRENT_TIMESTAMP)";

    try {
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, msgText);
        int rowsInserted = pstmt.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("Message inserted successfully!");
            message.clear(); // Clear the text area after sending
        }

        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
