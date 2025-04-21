package com.example.hrmanagementemployee;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ViewVacationRequestsController implements Initializable {

    private final int empID;

    @FXML private ScrollPane scrollPane;
    @FXML private VBox requestsContainer;

    // Constructor-based injection via your setControllerFactory
    public ViewVacationRequestsController(int empID) {
        this.empID = empID;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadRequests();
    }

    private void loadRequests() {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        String sql = "SELECT VacationID, StartDate, EndDate, Status "
                   + "FROM VacationsRequests WHERE EmployeeID = ? "
                   + "ORDER BY StartDate DESC";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id       = rs.getInt("VacationID");
                Date start   = rs.getDate("StartDate");
                Date end     = rs.getDate("EndDate");
                String stat  = rs.getString("Status");

                // build one card
                VBox card = new VBox(5);
                card.setStyle(
                    "-fx-padding: 10;"
                  + "-fx-background-color: #1e1e1e;"
                  + "-fx-border-color: #FFD700;"
                  + "-fx-border-width: 2;"
                  + "-fx-border-radius: 10;"
                  + "-fx-background-radius: 10;"
                );
                card.setPrefWidth(520);
                card.setMaxWidth(Region.USE_PREF_SIZE);

                Label lblId    = new Label("Request " + id);

                Label lblDates = new Label("From " + start.toLocalDate()
                                           + " to " + end.toLocalDate());
                Label lblStat  = new Label("Status: " + stat);

                // optional styling
                lblId.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                lblDates.setStyle("-fx-text-fill: lightgray;");
                switch (stat) {
                    case "Pending" -> lblStat.setStyle("-fx-text-fill: gold;");
                    case "Approved" -> lblStat.setStyle("-fx-text-fill: green;");
                    default -> lblStat.setStyle("-fx-text-fill: red;");
                }
                card.getChildren().addAll(lblId, lblDates, lblStat);
                requestsContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // you could show an alert here too
        }
    }
}
