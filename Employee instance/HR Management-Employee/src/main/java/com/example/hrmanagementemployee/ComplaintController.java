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
import javafx.scene.control.TextArea;

public class ComplaintController implements Initializable {

    private final int empID;

    @FXML
    private TextArea complaintField;

    @FXML
    private Button postComplaint;

    public ComplaintController(int empID) {
        this.empID = empID;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postComplaint.setOnAction(this::handlePost);
    }

    private void handlePost(ActionEvent event) {
        String text = complaintField.getText();
        if (text == null || text.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Complaint", "Please enter your complaint before posting.");
            return;
        }

        insertComplaint(text.trim());
    }

    private void insertComplaint(String text) {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        String sql = "INSERT INTO complaints (employeeid, complaint) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empID);
            stmt.setString(2, text);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Posted", "Your complaint has been submitted.");
            complaintField.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not submit complaint:\n" + e.getMessage());
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
