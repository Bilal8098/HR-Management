package com.example.hrmanagementemployee;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmployeePageController {
    private final int empID;
    private final String empName;
    @FXML
    private Label hello;
    @FXML
    private Pane titleBar;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button viewAttendance;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
            hello.setText(hello.getText().replace("User", empName)); 
    }

    public EmployeePageController(int id, String empName) {
        this.empID = id;
        this.empName = empName;
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
            stage.setTitle("Salaries report");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icon.png")));
            stage.setScene(new Scene(loader.load()));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openVacationRequestWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RequestVacation.fxml"));
            loader.setControllerFactory(param -> new RequestVacationController(empID));

            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icon.png")));
            stage.setTitle("Request Vacation");
            stage.setScene(new Scene(loader.load()));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewVacationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VacationView.fxml"));
            loader.setControllerFactory(param -> new ViewVacationRequestsController(empID));

            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icon.png")));
            stage.setTitle("View vacations request");
            stage.setScene(new Scene(loader.load()));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openComplaintWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Complaints.fxml"));
            loader.setControllerFactory(p -> new ComplaintController(empID));

            Stage stage = new Stage();
            stage.setTitle("Post a Complaint");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icon.png")));
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSuggestionWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Suggestion.fxml"));
            loader.setControllerFactory(p -> new SuggestionsController(empID));

            Stage stage = new Stage();
            stage.setTitle("Post a Suggestion");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icon.png")));
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button viewMessages;

    @FXML
    private void viewMessagesAction() {
        try {
            // Corrected the path to ViewMessage.fxml
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/hrmanagementemployee/ViewMessage.fxml"));
            Pane viewMessagePage = loader.load();

            // Create a new stage (window) for the view message page
            Stage stage = new Stage();
            stage.setTitle("View Messages");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("Icon.png")));
            stage.setScene(new Scene(viewMessagePage));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}