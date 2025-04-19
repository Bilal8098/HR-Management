package com.example.hrmanagementemployee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmployeePageController {
    private int empID;

    @FXML private Button viewAttendance;
    public EmployeePageController(int id) {
        this.empID = id;
    }
    @FXML
private void openAttendanceWindow() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Attendance.fxml"));

        // Inject employee ID
        loader.setControllerFactory(param -> new AttendanceController(empID));

        Stage stage = new Stage();
          stage.initStyle(StageStyle.UNDECORATED); 
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Attendance Records");
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
