package com.example.hrmanagementemployee;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

public class RequestVacationController implements Initializable {

    private int empID;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Button request;

    public RequestVacationController(int empID) {
        this.empID = empID;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        request.setOnAction(this::handleRequest);
    }

    /**
     * Validate fields and delegate to DB insertion.
     */
    @FXML
    private void handleRequest(ActionEvent event) {
        if (startDate.getValue() == null || endDate.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Empty Fields", "Please fill in both start and end dates.");
            return;
        }

        java.time.LocalDate start = startDate.getValue();
        java.time.LocalDate end = endDate.getValue();

        if (end.isBefore(start)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Dates", "End date cannot be before start date.");
            return;
        }

        insertVacationRequest(empID, start, end);
    }

    /**
     * Perform INSERT into PostgreSQL.
     */
    private void insertVacationRequest(int empID, java.time.LocalDate start, java.time.LocalDate end) {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        String sql = "INSERT INTO VacationsRequests (EmployeeID, StartDate, EndDate, Status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empID);
            stmt.setDate(2, Date.valueOf(start));
            stmt.setDate(3, Date.valueOf(end));
            stmt.setString(4, "Pending");

            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Vacation request submitted successfully.");

            // Optionally clear the fields
            startDate.setValue(null);
            endDate.setValue(null);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not submit vacation request:\n" + e.getMessage());
        }
    }

    /**
     * Utility to show alerts.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}