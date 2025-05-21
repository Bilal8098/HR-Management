package com.example.hrmanagementemployee;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.sql.*;

public class ViewMessage {
    @FXML
    private FlowPane cardContainer;

    @FXML
    private Button backButton;

    // Database credentials
    private static final String DB_URL = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

    private void loadMessages() {
        cardContainer.getChildren().clear();

        String query = "SELECT message_id, message_text, created_at FROM messages";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("message_id");
                String text = rs.getString("message_text");
                String date = rs.getString("created_at");
                String formattedDate = date.toString().substring(0, 16);
                // create a card
                VBox card = new VBox(5);
                card.getStyleClass().add("message-card");
                card.setStyle("-fx-background-color: #2a2a2a;");

                Label idLabel = new Label("ID: " + id);
                idLabel.setStyle("-fx-text-fill: #ffc605; -fx-font-weight: bold;");
                
                Label textLabel = new Label(text);
                textLabel.setStyle("-fx-text-fill: white;");
                textLabel.setWrapText(true);
                textLabel.setMaxWidth(230);
                
                Label dateLabel = new Label(formattedDate);
                dateLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 10px;");

                card.getChildren().addAll(idLabel, textLabel, dateLabel);
                cardContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Error loading messages: " + e.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackButtonAction() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        loadMessages();
    }
}