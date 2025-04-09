package com.example.hrmanagmenthr;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class RaiseAllController {
    @FXML
    private TextField value;
    @FXML
      public void raiseAll(){
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE salary SET actualSalary = actualSalary + ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, Double.parseDouble(value.getText()));
            stmt.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Salaries raised successfully");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
