package com.example.hrmanagementmanager;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
public class MainController {
    @FXML
    private Button viewEmps;
    @FXML
    private Button viewAttendances;
    public void viewEmps(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewEmployees.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Employees List");
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void viewAttendance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Attendance.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Attendance table");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to open attendance page.");
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
private void openReportsPage() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportsView.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Employee PDFs");
        stage.setScene(new Scene(loader.load()));
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}