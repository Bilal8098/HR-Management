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

    private Employee selectedEmployee; // 👈 to hold selected employee

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

    public void goToDeduction() {
        try {
            EmployeeSalary selectedItem = salaryTable.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                showAlert("Select an employee");
                return;
            }

            String mainSalary = selectedItem.getMainSalary();
            String actualSalary = selectedItem.getActualSalary();
            int employeeID = selectedItem.getEmployeeID();
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

    public void goToRaise() {
        try {
            EmployeeSalary selectedItem = salaryTable.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                showAlert("Select an employee");
                return;
            }

            String mainSalary = selectedItem.getMainSalary();
            String actualSalary = selectedItem.getActualSalary();
            int employeeID = selectedItem.getEmployeeID();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("raise.fxml"));
            raiseController controller = new raiseController(mainSalary, actualSalary, employeeID);
            loader.setController(controller);

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("raise employee");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Bind columns here
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

    // ✅ NEW: This method will be called from the main controller
    public void setEmployee(Employee employee) {
        this.selectedEmployee = employee;
        loadSalaryData(employee.getId());
    }

    // ✅ NEW: Load only salary data for the selected employee
    private void loadSalaryData(int employeeId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT s.EmployeeID, e.Salary AS MainSalary, s.ActualSalary, s.DifferenceAmount, s.DeductionOrRaise, s.Reason " +
                    "FROM Salary s JOIN Employees e ON s.EmployeeID = e.EmployeeID WHERE s.EmployeeID = ?";
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
