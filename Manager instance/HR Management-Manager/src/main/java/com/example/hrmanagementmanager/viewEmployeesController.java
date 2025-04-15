package com.example.hrmanagementmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.*;

public class viewEmployeesController {
    private static final String URL = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
    private static final String USER = "postgres";
    private static final String PASSWORD = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, Integer> idCol;
    @FXML private TableColumn<Employee, String> nameCol;
    @FXML private TableColumn<Employee, String> emailCol;
    @FXML private TableColumn<Employee, String> phoneCol;
    @FXML private TableColumn<Employee, String> addressCol;
    @FXML private TableColumn<Employee, Double> salaryCol;

    @FXML private TextField idSearchField;
    @FXML private TextField nameSearchField;
    @FXML private TextField emailSearchField;
    @FXML private TextField phoneSearchField;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    public void initialize() {
        // Set up column bindings
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getEmployeeID()));
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));
        emailCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        phoneCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone()));
        addressCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));
        salaryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getSalary()));

        loadEmployees();
        idSearchField.setOnKeyPressed(event -> handleKeyPressed(event));
        nameSearchField.setOnKeyPressed(event -> handleKeyPressed(event));
        emailSearchField.setOnKeyPressed(event -> handleKeyPressed(event));
        phoneSearchField.setOnKeyPressed(event -> handleKeyPressed(event));
    }
private void handleKeyPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
        searchEmployees();
        }
    }
    public void loadEmployees() {
        employeeList.clear();
        try (Connection conn = DriverManager.getConnection(
        URL, USER, PASSWORD
        );
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT EmployeeID, FullName, Email, Phone, Address, Salary FROM Employees")) {

            while (rs.next()) {
                Employee emp = new Employee(
                        rs.getInt("EmployeeID"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Address"),
                        rs.getDouble("Salary")
                );
                employeeList.add(emp);
            }
            employeeTable.setItems(employeeList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void searchEmployees() {
        String idText = idSearchField.getText();
        String nameText = nameSearchField.getText();
        String emailText = emailSearchField.getText();
        String phoneText = phoneSearchField.getText();

        ObservableList<Employee> filteredList = FXCollections.observableArrayList();

        for (Employee emp : employeeList) {
            boolean matches = true;

            if (!idText.isEmpty() && !String.valueOf(emp.getEmployeeID()).contains(idText))
                matches = false;
            if (!nameText.isEmpty() && !emp.getFullName().toLowerCase().contains(nameText.toLowerCase()))
                matches = false;
            if (!emailText.isEmpty() && !emp.getEmail().toLowerCase().contains(emailText.toLowerCase()))
                matches = false;
            if (!phoneText.isEmpty() && !emp.getPhone().contains(phoneText))
                matches = false;

            if (matches)
                filteredList.add(emp);
        }

        employeeTable.setItems(filteredList);
    }
}
