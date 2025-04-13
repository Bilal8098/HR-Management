package com.example.hrmanagmenthr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class SalariesController {
    @FXML
    private TableView<EmployeeSalary> salaryTable;

    private Employee selectedEmployee; // holds the selected employee

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goToRaiseAll() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("raiseValue.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Raise all salaries");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Automatically use the latest salary record (the first record in the table) for deduction.
    public void goToDeduction() {
        try {
            ObservableList<EmployeeSalary> items = salaryTable.getItems();
            if (items.isEmpty()) {
                showAlert("No salary record available.");
                return;
            }
            // Automatically take the latest record (ordered descending in SQL)
            EmployeeSalary record = items.get(0);

            String mainSalary = record.getMainSalary();
            String actualSalary = record.getActualSalary();
            int employeeID = record.getEmployeeID();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("deduction.fxml"));
            DeductionController controller = new DeductionController(mainSalary, actualSalary, employeeID);
            loader.setController(controller);

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Deduct employee");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Automatically use the latest salary record (the first record in the table) for raise.
    public void goToRaise() {
        try {
            ObservableList<EmployeeSalary> items = salaryTable.getItems();
            if (items.isEmpty()) {
                showAlert("No salary record available.");
                return;
            }
            // Automatically take the latest record (ordered descending in SQL)
            EmployeeSalary record = items.get(0);

            String mainSalary = record.getMainSalary();
            String actualSalary = record.getActualSalary();
            int employeeID = record.getEmployeeID();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("raise.fxml"));
            raiseController controller = new raiseController(mainSalary, actualSalary, employeeID);
            loader.setController(controller);

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Raise employee");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Bind columns assuming the column order matches your FXML design.
        TableColumn<EmployeeSalary, String> colEmployeeID = (TableColumn<EmployeeSalary, String>) salaryTable.getColumns().get(0);
        TableColumn<EmployeeSalary, String> colMainSalary = (TableColumn<EmployeeSalary, String>) salaryTable.getColumns().get(1);
        TableColumn<EmployeeSalary, String> colActualSalary = (TableColumn<EmployeeSalary, String>) salaryTable.getColumns().get(2);
        TableColumn<EmployeeSalary, String> colDifference = (TableColumn<EmployeeSalary, String>) salaryTable.getColumns().get(3);
        TableColumn<EmployeeSalary, String> colType = (TableColumn<EmployeeSalary, String>) salaryTable.getColumns().get(4);
        TableColumn<EmployeeSalary, String> colReason = (TableColumn<EmployeeSalary, String>) salaryTable.getColumns().get(5);

        colEmployeeID.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getEmployeeID())));
        colMainSalary.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMainSalary()));
        colActualSalary.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getActualSalary()));
        colDifference.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDifferenceAmount()));
        colType.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        colReason.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getReason()));
    }

    // Called from the main controller to set the current employee and load all their salary records.
    public void setEmployee(Employee employee) {
        this.selectedEmployee = employee;
        loadSalaryData(employee.getId());
    }

    // Load all salary records for the given employee from PostgreSQL.
    private void loadSalaryData(int employeeId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            // Remove the LIMIT clause to fetch all records for the employee.
            // The records are ordered with the latest (by SalaryID descending or a date column) at the top.
            String sql = "SELECT s.EmployeeID, e.Salary AS MainSalary, s.ActualSalary, s.DifferenceAmount, " +
                         "s.DeductionOrRaise, s.Reason " +
                         "FROM Salary s " +
                         "JOIN Employees e ON s.EmployeeID = e.EmployeeID " +
                         "WHERE s.EmployeeID = ? " +
                         "ORDER BY s.SalaryID DESC";  // You can change this to a date column if available.

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            ObservableList<EmployeeSalary> list = FXCollections.observableArrayList();
            while (rs.next()) {
                int id = rs.getInt("EmployeeID");
                String mainSalary = rs.getBigDecimal("MainSalary").toString();
                String actual = rs.getBigDecimal("ActualSalary").toString();
                String diff = rs.getBigDecimal("DifferenceAmount").toString();
                String type = rs.getString("DeductionOrRaise");
                String reason = rs.getString("Reason");

                list.add(new EmployeeSalary(id, mainSalary, actual, diff, type, reason));
            }
            salaryTable.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
