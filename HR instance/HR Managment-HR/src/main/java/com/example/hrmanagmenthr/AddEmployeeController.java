package com.example.hrmanagmenthr;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.regex.Pattern;

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

    // Simple regex patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
        Pattern.CASE_INSENSITIVE
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$");

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
                showAlert(Alert.AlertType.ERROR, "Error reading fingerprint file.");
            }
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        // 1. Validate inputs
        String name = fullName.getText().trim();
        String emailText = email.getText().trim();
        String phoneText = phone.getText().trim();
        String addressText = address.getText().trim();
        String salaryText = salary.getText().trim();
        String pwd = password.getText();

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Full name must not be empty.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(emailText).matches()) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid email address.");
            return;
        }

        if (!PHONE_PATTERN.matcher(phoneText).matches()) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid phone number (7–15 digits, optional leading +).");
            return;
        }

        if (addressText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Address must not be empty.");
            return;
        }

        double salaryValue;
        try {
            salaryValue = Double.parseDouble(salaryText);
            if (salaryValue < 0) {
                showAlert(Alert.AlertType.ERROR, "Salary must be a positive number.");
                return;
            }
        } catch (NumberFormatException nfe) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid numeric salary.");
            return;
        }

        if (pwd.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Password must be at least 6 characters long.");
            return;
        }

        if (fingerprintBytes == null || fingerprintBytes.length == 0) {
            showAlert(Alert.AlertType.ERROR, "Please upload a fingerprint file.");
            return;
        }

        // 2. All validations passed — insert into database
        try {
            var conn = DatabaseConnection.getConnection();
            var stmt = conn.prepareStatement(
                "INSERT INTO employees (fullname, email, phone, address, salary, password, fingerprint) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, name);
            stmt.setString(2, emailText);
            stmt.setString(3, phoneText);
            stmt.setString(4, addressText);
            stmt.setDouble(5, salaryValue);
            stmt.setString(6, pwd);
            stmt.setBytes(7, fingerprintBytes);
            stmt.executeUpdate();

            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Employee added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error adding employee to the database.");
        }
    }

    // Utility methods

    private void clearFields() {
        fullName.clear();
        email.clear();
        phone.clear();
        address.clear();
        salary.clear();
        password.clear();
        fingerprintBytes = null;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }
}
