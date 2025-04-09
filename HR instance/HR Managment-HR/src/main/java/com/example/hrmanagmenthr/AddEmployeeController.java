package com.example.hrmanagmenthr;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

@SuppressWarnings("ALL")
public class AddEmployeeController {
    URL fxmlUrl = getClass().getResource("/com/example/hrmanagmenthr/AddEmployee.fxml");



    @FXML private TextField fullName;
    @FXML private TextField email;
    @FXML private TextField phone;
    @FXML private TextField address;
    @FXML private TextField salary;
    @FXML private PasswordField password;

    private byte[] fingerprintBytes;

    @FXML
    private void handleUploadFingerprint(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Fingerprint");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                fingerprintBytes = Files.readAllBytes(file.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            String name = fullName.getText();
            String emailText = email.getText();
            String phoneText = phone.getText();
            String addressText = address.getText();
            double salaryValue = Double.parseDouble(salary.getText());
            String pwd = password.getText();

            // Insert into database
            var conn = DatabaseConnection.getConnection();
            var stmt = conn.prepareStatement("INSERT INTO employees (fullname, email, phone, address, salary, password, fingerprint) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, emailText);
            stmt.setString(3, phoneText);
            stmt.setString(4, addressText);
            stmt.setDouble(5, salaryValue);
            stmt.setString(6, pwd);
            stmt.setBytes(7, fingerprintBytes);
            stmt.executeUpdate();
            

            // Clear fields after success
            fullName.clear();
            email.clear();
            phone.clear();
            address.clear();
            salary.clear();
            password.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Employee added successfully!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding employee.");
            alert.showAndWait();
        }
    }
}
