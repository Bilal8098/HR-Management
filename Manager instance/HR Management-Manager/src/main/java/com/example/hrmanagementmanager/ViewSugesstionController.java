package com.example.hrmanagementmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.*;

public class ViewSugesstionController {

    @FXML
    private VBox suggestionContainer;

    public void initialize() {
        loadSuggestionsFromDB();
    }

    private void loadSuggestionsFromDB() {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        String query = "SELECT * FROM suggestion";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("suggestionid");
                int employeeId = rs.getInt("employeeid");
                String suggestion = rs.getString("suggestion");

                VBox card = new VBox();
                card.setSpacing(5);
                card.setStyle("-fx-background-color: #DAA520; -fx-padding: 10; -fx-background-radius: 10;");
                card.setPrefWidth(600);

                Text title = new Text("Suggestion ID #: " + id);
                title.setStyle("-fx-font-weight: bold; -fx-fill: black;");

                Text emp = new Text("Employee ID: " + employeeId);
                emp.setStyle("-fx-fill: black;");

                Text text = new Text(suggestion);
                text.setWrappingWidth(580);
                text.setStyle("-fx-fill: black;");

                card.getChildren().addAll(title, emp, text);

                suggestionContainer.getChildren().add(card);
            }

        } catch (SQLException e) {
            showError("Error loading suggestions: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
