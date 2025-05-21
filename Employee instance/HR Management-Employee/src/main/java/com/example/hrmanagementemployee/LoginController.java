package com.example.hrmanagementemployee;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("EmployeeLogin.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Employee Login");
stage.getIcons().add(new Image(LoginController.class.getResourceAsStream("Icon.png")));

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Validates login credentials.
     * Returns the employee ID if valid, otherwise returns 0 or -1 for failure.
     */
    public static int validateLogin(String employeeIdStr, String password) {
        String sql = "SELECT employeeid FROM employees WHERE employeeid = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int employeeId = Integer.parseInt(employeeIdStr);
            stmt.setInt(1, employeeId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("employeeid");  // Success: return valid employee ID
            } else {
                return 0; // No match
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error
        }
    }

    /**
     * Opens the Employee Page and passes the employee ID to the controller.
     */
    public static void openEmployeePage(Stage stage, int empId) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("EmployeePage.fxml"));
            loader.setControllerFactory(param -> new EmployeePageController(empId));

            Parent root = loader.load();
            stage.getIcons().add(new Image(LoginController.class.getResourceAsStream("Icon.png")));

            stage.setScene(new Scene(root));
            stage.setTitle("Employee Page");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
