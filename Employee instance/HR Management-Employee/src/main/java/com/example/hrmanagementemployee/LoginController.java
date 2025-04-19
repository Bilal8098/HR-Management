package com.example.hrmanagementemployee;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("EmployeeLogin.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Employee Login");
          stage.initStyle(StageStyle.UNDECORATED); 
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static boolean validateLogin(String employeeId, String password) {
        String sql = "SELECT * FROM employees WHERE employeeid = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(employeeId));
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void openEmployeePage(Stage stage, int empId) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("EmployeePage.fxml"));
    
            // Set a controller factory to inject the empId
            loader.setControllerFactory(param -> new EmployeePageController(empId));
    
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Employee Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
