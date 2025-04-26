package com.example.hrmanagementmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class ComplaintsController {

    @FXML private ScrollPane scrollPane;
    @FXML private FlowPane cardsContainer;

    public class ComplaintCard extends VBox {
        private final int complaintId;
        private final int employeeId;
        private final String complaintText;

        public ComplaintCard(int complaintId, int employeeId, String complaintText) {
            this.complaintId = complaintId;
            this.employeeId = employeeId;
            this.complaintText = complaintText;

            this.getStyleClass().add("complaint-card");
            this.setPrefSize(280, 140);
            this.setSpacing(8);
            this.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffd700, #ffcc00);" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 10;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);" +
                    "-fx-cursor: hand;");

            Label headerLabel = new Label("Complaint #" + complaintId);
            headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: black;");

            Label idLabel = new Label("Employee ID: " + employeeId);
            idLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #111111;");

            String preview = complaintText.length() > 40 ?
                    complaintText.substring(0, 40) + "..." : complaintText;

            Label previewLabel = new Label(preview);
            previewLabel.setWrapText(true);
            previewLabel.setMaxWidth(260);
            previewLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");

            this.getChildren().addAll(headerLabel, idLabel, previewLabel);

            this.setOnMouseClicked(event -> showComplaintDetails()); // Ensures click works
        }

        private void showComplaintDetails() {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Complaint Details");
            alert.setHeaderText("Complaint #" + complaintId + " - Employee ID: " + employeeId);
            alert.setContentText(complaintText); // FULL TEXT here
            alert.getDialogPane().setMinWidth(500);
            alert.showAndWait();
        }
    }

    public void initialize() {
        loadComplaintsFromDB();
    }

    private void loadComplaintsFromDB() {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ComplaintID, EmployeeID, Complaint FROM Complaints")) {

            while (rs.next()) {
                int id = rs.getInt("ComplaintID");
                int empId = rs.getInt("EmployeeID");
                String text = rs.getString("Complaint");

                ComplaintCard card = new ComplaintCard(id, empId, text);
                cardsContainer.getChildren().add(card);
            }

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load complaints: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void closeWindow() {
        ((Stage) scrollPane.getScene().getWindow()).close();
    }

    @FXML
    private void minimizeWindow() {
        ((Stage) scrollPane.getScene().getWindow()).setIconified(true);
    }

    public static void show(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ComplaintsController.class.getResource("/com/example/demo/complaint-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(ComplaintsController.class.getResource("/com/example/demo/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Employee Complaints Dashboard");
        primaryStage.show();
    }
}
