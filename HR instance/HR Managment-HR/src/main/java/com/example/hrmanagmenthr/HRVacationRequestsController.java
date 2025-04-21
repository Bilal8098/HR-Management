package com.example.hrmanagmenthr;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HRVacationRequestsController implements Initializable {

    @FXML private VBox requestsContainer;

    String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
    String user = "postgres";
    String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAllRequests();
    }

    private void loadAllRequests() {
        String sql = """
            SELECT VacationID, EmployeeID, StartDate, EndDate, Status 
                     FROM VacationsRequests WHERE Status LIKE 'Pending' ORDER BY StartDate DESC
                     """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int vid = rs.getInt("VacationID");
                int eid = rs.getInt("EmployeeID");
                Date start = rs.getDate("StartDate");
                Date end   = rs.getDate("EndDate");
                String status = rs.getString("Status");

                VBox card = new VBox(5);
                card.setStyle(
                    "-fx-padding: 10;"
                  + "-fx-background-color: #1e1e1e;"
                  + "-fx-border-color: #FFD700;"
                  + "-fx-border-width: 2;"
                  + "-fx-border-radius: 10;"
                  + "-fx-background-radius: 10;"
                );
                card.setPrefWidth(590);
                card.setMaxWidth(Region.USE_PREF_SIZE);

                Label lblId    = new Label("Request #" + vid + " (Employee " + eid + ")");
                lblId.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

                Label lblDates = new Label(
                    "From " + start.toLocalDate()
                  + " to " + end.toLocalDate()
                );
                lblDates.setStyle("-fx-text-fill: lightgray;");

                Label lblStat  = new Label("Status: " + status);
                lblStat.setStyle("-fx-text-fill: gold;");

                // buttons container
                HBox btnBox = new HBox(10);
                btnBox.setStyle("-fx-padding: 5 0 0 0;");

                Button btnApprove = new Button("Approve");
                Button btnReject  = new Button("Reject");
                btnApprove.getStyleClass().add("approveButton");
                btnReject.getStyleClass().add("rejectButton");

                btnApprove.setOnAction(ev -> updateStatus(vid, "Approved"));
                btnReject.setOnAction(ev -> updateStatus(vid, "Rejected"));

                btnBox.getChildren().addAll(btnApprove, btnReject);

                card.getChildren().addAll(lblId, lblDates, lblStat, btnBox);
                requestsContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load requests.\n" + e.getMessage());
        }
    }

    private void updateStatus(int vacationId, String newStatus) {
        String sql = "UPDATE VacationsRequests SET Status = ? WHERE VacationID = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, vacationId);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Request " + vacationId + " updated to " + newStatus);

            // refresh view
            requestsContainer.getChildren().clear();
            loadAllRequests();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not update status.\n" + e.getMessage());
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