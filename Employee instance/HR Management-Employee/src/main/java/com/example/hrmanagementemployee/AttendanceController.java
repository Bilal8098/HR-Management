package com.example.hrmanagementemployee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class AttendanceController {

    @FXML private TableView<AttendanceModel> attendanceTable;
    @FXML private TableColumn<AttendanceModel, String> attendanceIdCol;
    @FXML private TableColumn<AttendanceModel, String> attendDateCol;
    private int empID;
    
       @FXML
    private Pane titleBar;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
      private double xOffset = 0;
    private double yOffset = 0;
    public AttendanceController(int empID) {
        this.empID = empID;
    }
    public void initialize() {
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        attendanceIdCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAttendanceId())));
        attendDateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAttendDate().toString()));

        attendanceTable.setItems(loadAttendanceFromDB());
    }

    private ObservableList<AttendanceModel> loadAttendanceFromDB() {
        ObservableList<AttendanceModel> list = FXCollections.observableArrayList();

        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";
        String sql = "SELECT * FROM Attendance WHERE EmployeeID = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.empID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AttendanceModel attendance = new AttendanceModel();
                    attendance.setAttendanceId(rs.getInt("AttendanceID"));
                    attendance.setAttendDate(rs.getTimestamp("AttendDate"));

                    list.add(attendance);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load attendance data: " + e.getMessage());
        }

        return list;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) attendanceTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizeWindow() {
        Stage stage = (Stage) attendanceTable.getScene().getWindow();
        stage.setIconified(true);
    }
}