package com.example.hrmanagmenthr;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SendMessageController {

    @FXML
    private TextField messageField;

    // Send for All button action
    @FXML
    private void handleSendForAll() {
        String message = messageField.getText().trim();
        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Message cannot be empty!");
            return;
        }

        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        String sql = "INSERT INTO messages (message_text, created_at) VALUES (?, CURRENT_DATE)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, message);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Message sent to all employees successfully!");
                messageField.clear();
            } else {
                JOptionPane.showMessageDialog(null, "Message could not be sent.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Identify Employees button action
    @FXML
    private void handleIdentifyEmployees() {
        // Get the message from the TextField to send to selected employees
        String message = messageField.getText().trim();
        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a message first.");
            return;
        }

        try {
            // Load the IdentifyEmployee FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hrmanagmenthr/IdentifyEmployee.fxml"));
            AnchorPane root = loader.load();

            // Get the controller of IdentifyEmployee
            IdentifyEmployeeController controller = loader.getController();

            // Set the message in IdentifyEmployeeController
            controller.setMessage(message);

            // Show the scene with the IdentifyEmployeeController
            Stage stage = new Stage();
            stage.setTitle("Identify Employees");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
