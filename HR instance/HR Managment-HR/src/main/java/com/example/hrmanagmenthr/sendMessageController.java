package com.example.hrmanagmenthr;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class sendMessageController {

    @FXML
    private TextArea messageField;

    @FXML
    private Button sendForAllBtn;

    private Connection connectDB() {
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

    @FXML
    private void handleSendForAll() {
        String messageText = messageField.getText();

        if (messageText == null || messageText.trim().isEmpty()) {
            System.out.println("Message is empty, cannot send.");
            return;
        }

        Connection conn = connectDB();
        if (conn == null) {
            System.out.println("Failed to connect to database.");
            return;
        }

        String sql = "INSERT INTO messages (message_text, created_at) VALUES (?, CURRENT_TIMESTAMP)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, messageText);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Message sent successfully!");
                messageField.clear();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) { }
        }
    }
}
