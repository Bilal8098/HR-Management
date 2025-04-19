package com.example.hrmanagementemployee;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmployeePageController {
    private final int empID;
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
@FXML
private void openPDFWindow() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PDFview.fxml"));
        loader.setControllerFactory(param -> new EmployeePDFsController(empID));

        Stage stage = new Stage();
        stage.setTitle("Employee PDFs");
        stage.setScene(new Scene(loader.load()));
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}