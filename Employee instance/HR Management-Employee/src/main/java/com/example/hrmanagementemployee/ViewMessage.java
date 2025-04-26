package com.example.hrmanagementemployee;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.sql.*;

public class ViewMessage {

    @FXML
    private TableView<Message> messageTable;
    @FXML
    private TableColumn<Message, Integer> messageIdColumn;
    @FXML
    private TableColumn<Message, String> messageTextColumn;
    @FXML
    private TableColumn<Message, String> createdAtColumn;
    @FXML
    private Button backButton;

    // Database credentials
    private static final String DB_URL = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway"; // Update with the correct URL
    private static final String DB_USER = "postgres"; // Username
    private static final String DB_PASSWORD = "NqlVODXIobgaOsHmwWHqXllPtOVOZril"; // Password

    private void loadMessages() {
        ObservableList<Message> messages = FXCollections.observableArrayList();

        // Query to fetch messages from the database
        String query = "SELECT message_id, message_text, created_at FROM messages";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Check if there are any messages
            if (!rs.isBeforeFirst()) {
                System.out.println("No messages found in the database.");
            }

            // Loop through the result set and create Message objects
            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                String messageText = rs.getString("message_text");
                String createdDate = rs.getString("created_at");

                // Debugging print
                System.out.println("Message ID: " + messageId + ", Text: " + messageText + ", Date: " + createdDate);

                // Add the message to the ObservableList
                messages.add(new Message(messageId, messageText, createdDate));
            }

            // Set the items of the TableView
            messageTable.setItems(messages);
        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace for more details
            showErrorDialog("Error loading messages from the database: " + e.getMessage());
        }
    }


    // Method to show an error dialog
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    // Back button action handler to close the window
    @FXML
    private void handleBackButtonAction() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    // Initialize method to bind the columns
    @FXML
    public void initialize() {
        messageIdColumn.setCellValueFactory(cellData -> cellData.getValue().messageIdProperty().asObject());
        messageTextColumn.setCellValueFactory(cellData -> cellData.getValue().messageTextProperty());
        createdAtColumn.setCellValueFactory(cellData -> cellData.getValue().createdAtProperty());

        loadMessages();
    }
}