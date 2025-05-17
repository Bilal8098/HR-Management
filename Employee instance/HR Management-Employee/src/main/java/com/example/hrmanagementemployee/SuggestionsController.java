package com.example.hrmanagementemployee;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SuggestionsController implements Initializable {

    private final int empID;

    @FXML
    private TextField SuggestionField;

    @FXML
    private Button postSuggestion;

    public SuggestionsController(int empID) {
        this.empID = empID;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postSuggestion.setOnAction(this::handlePost);
    }

    private void handlePost(ActionEvent event) {
        String text = SuggestionField.getText();
        if (text == null || text.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Suggestion", "Please enter your Suggestion before posting.");
            return;
        }

        insertSuggestion(text.trim());
    }

    private void insertSuggestion(String text) {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        String sql = "INSERT INTO Suggestions (employeeid, suggestion) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empID);
            stmt.setString(2, text);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Posted", "Your Suggestion has been submitted.");
            SuggestionField.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not submit Suggestion:\n" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}