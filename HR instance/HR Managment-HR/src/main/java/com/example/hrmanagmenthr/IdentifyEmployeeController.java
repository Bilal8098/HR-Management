package com.example.hrmanagmenthr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class IdentifyEmployeeController implements Initializable {

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn, emailColumn, phoneColumn, addressColumn, passwordColumn;
    @FXML
    private TableColumn<Employee, Double> salaryColumn;

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Enable multi-selection
        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        loadEmployees();
    }

    private void loadEmployees() {
        ObservableList<Employee> list = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {

            while (rs.next()) {
                byte[] fingerprint = rs.getBytes("fingerprint");

                list.add(new Employee(
                        rs.getInt("employeeid"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getDouble("salary"),
                        rs.getString("password"),
                        fingerprint
                ));
            }

            employeeTable.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendMessage() {
        ObservableList<Employee> selectedEmployees = employeeTable.getSelectionModel().getSelectedItems();

        // Check if any employees are selected
        if (selectedEmployees.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No employees selected!");
            alert.show();
            return;
        }

        // Prepare the SQL query for inserting the message
        String sql = "INSERT INTO messages (employeeid, message_text) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Loop through selected employees and add them to the batch for sending messages
            for (Employee emp : selectedEmployees) {
                ps.setInt(1, emp.getId());  // Set employeeid
                ps.setString(2, message);   // Set message text
                ps.addBatch();  // Add to batch for batch execution
            }

            // Execute the batch insert
            int[] result = ps.executeBatch();

            // Check if the message was successfully sent to the selected employees
            if (result.length > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Message sent to selected employees.");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to send message.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error sending message: " + e.getMessage());
            alert.show();
        }
    }

}
