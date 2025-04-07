package com.example.hrmanagmenthr;

// UpdateEmployeeController.java
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

@SuppressWarnings("ALL")
public class UpdateEmployeeController {
    @FXML private TextField fullName, email, phone, address, salary;
    @FXML private PasswordField password;

    private byte[] cvBytes;
    private byte[] fingerprintBytes;
    private int employeeId;

    public void setEmployee(Employee emp) {
        this.employeeId = emp.getId();
        fullName.setText(emp.getFullName());
        email.setText(emp.getEmail());
        phone.setText(emp.getPhone());
        address.setText(emp.getAddress());
        salary.setText(String.valueOf(emp.getSalary()));
        password.setText(emp.getPassword());
    }

    public void handleUploadCV() {
        cvBytes = pickFile();
    }

    public void handleUploadFingerprint() {
        fingerprintBytes = pickFile();
    }

    private byte[] pickFile() {
        try {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(null);
            if (file != null) {
                FileInputStream fis = new FileInputStream(file);
                return fis.readAllBytes();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void handleUpdate() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE employees SET fullname=?, email=?, phone=?, address=?, salary=?, password=?, cv=?, fingerprint=? WHERE employeeid=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fullName.getText());
            stmt.setString(2, email.getText());
            stmt.setString(3, phone.getText());
            stmt.setString(4, address.getText());
            stmt.setDouble(5, Double.parseDouble(salary.getText()));
            stmt.setString(6, password.getText());
            stmt.setBytes(7, cvBytes);
            stmt.setBytes(8, fingerprintBytes);
            stmt.setInt(9, employeeId);
            stmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

