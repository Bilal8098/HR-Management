package com.example.hrmanagementemployee;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class EmployeeLoginController {

    @FXML
    private Pane titleBar;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> handleLogin());

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

   private void handleLogin() {
    String empId = employeeIdField.getText();
    String pass = passwordField.getText();

    if (empId.isEmpty() || pass.isEmpty()) {
        showAlert("Please enter Employee ID and Password.");
        return;
    }

    Employee employee = LoginController.validateLogin(empId, pass);

    if (employee != null) {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        LoginController.openEmployeePage(stage, employee);
    } else {
        showAlert("Invalid Employee ID or Password.");
    }
}

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizeWindow() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }
}
