package com.example.hrmanagementmanager;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController {

    @FXML
    private Button viewEmps;

    @FXML
    private Button viewAttendances;

       @FXML
    private Pane titleBar;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
      private double xOffset = 0;
    private double yOffset = 0;

     public void initialize() {
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
    public void viewEmps() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewEmployees.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Employees List");
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to open Employees page.");
        }
    }

    public void viewAttendance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Attendance.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Attendance Table");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to open Attendance page.");
        }
    }

    @FXML
    private void openReportsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportsView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Salaries reports");
            stage.setScene(new Scene(loader.load()));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to open Reports page.");
        }
    }

    @FXML
    private void viewComplaints(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("complaint-view.fxml"));
            Parent complaintRoot = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(complaintRoot));
            stage.setTitle("Complaints");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to open Complaints page.");
        }
    }

    @FXML
    private void viewSuggestions() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/hrmanagementmanager/ViewSuggestion.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Suggestions");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to open Suggestions page.");
        }
    }

    @FXML
    private void goToAnnounceMessage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/hrmanagementmanager/AnnounceMessage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Announce Messages");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to open Announce Messages page.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
 @FXML
    private void closeWindow() {
        Stage stage = (Stage) viewEmps.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizeWindow() {
        Stage stage = (Stage) viewEmps.getScene().getWindow();
        stage.setIconified(true);
    }
}