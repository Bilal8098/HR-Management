package com.example.hrmanagmenthr;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class DeductionController implements Initializable {

    private final String mainSal;
    private final String actualSal;
    private final int employeeID;

    public DeductionController(String mainSalary, String actualSalary, int employeeID) {
        this.mainSal = mainSalary;
        this.actualSal = actualSalary;
        this.employeeID = employeeID;
    }

    @FXML
    private TextField mainSalary;

    @FXML
    private TextField actualSalary;

    @FXML
    private TextField deductionAmount;

    @FXML
    private TextField reasonField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (mainSalary != null && actualSalary != null) {
            mainSalary.setText(mainSal);
            actualSalary.setText(actualSal);
        }
    }

    public void deduct() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            double deductionValue = Double.parseDouble(deductionAmount.getText());
            double newSalary = Double.parseDouble(actualSal) - deductionValue;
            String reason = reasonField.getText();
    
            String insertSql = """
                INSERT INTO Salary (
                    EmployeeID,
                    ActualSalary,
                    DifferenceAmount,
                    DeductionOrRaise,
                    Reason
                ) VALUES (?, ?, ?, ?, ?)
            """;
    
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, employeeID);
            insertStmt.setDouble(2, newSalary);
            insertStmt.setDouble(3, deductionValue);
            insertStmt.setString(4, "Deduction");
            insertStmt.setString(5, reason.isEmpty() ? "No reason provided" : reason);
            insertStmt.executeUpdate();
    
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Deduction recorded successfully");
            alert.show();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
