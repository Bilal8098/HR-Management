package com.example.hrmanagmenthr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SuppressWarnings("ALL")
public class EmployeeController {

    public HBox topBar;
    @FXML private TextField searchField;
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, Integer> idCol;
    @FXML private TableColumn<Employee, String> nameCol;
    @FXML private TableColumn<Employee, String> emailCol;
    @FXML private TableColumn<Employee, String> phoneCol;
    @FXML private TableColumn<Employee, String> addressCol;
    @FXML private TableColumn<Employee, Double> salaryCol;
    @FXML private TableColumn<Employee, Void> actionCol;

    private ObservableList<Employee> employees = FXCollections.observableArrayList();

    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));

        employeeTable.setItems(employees);
        loadAllEmployees();
        addActionButtonsToTable();

        // Apply the CSS file
        Scene scene = employeeTable.getScene();
        if (scene != null) {
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        }
    }

    private void loadAllEmployees() {
        employees.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("employeeid"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getDouble("salary"),
                        rs.getBytes("cv"),
                        rs.getString("password"),
                        rs.getBytes("fingerprint")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addActionButtonsToTable() {
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button updateBtn = new Button("Update");
            private final Button deleteBtn = new Button("Delete");

            {
                updateBtn.getStyleClass().add("loginButton");
                deleteBtn.setStyle("-fx-background-color: darkred; -fx-text-fill: white;");

                updateBtn.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    updateEmployee(emp);
                });

                deleteBtn.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    deleteEmployee(emp);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, updateBtn, deleteBtn);
                    setGraphic(buttons);
                }
            }
        });
    }

    public void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim();
        employees.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM employees WHERE fullname ILIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("employeeid"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getDouble("salary"),
                        rs.getBytes("cv"),
                        rs.getString("password"),
                        rs.getBytes("fingerprint")
                ));
            }
            employeeTable.setItems(employees);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleAddEmployee(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddEmployee.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add New Employee");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(Employee emp) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this employee?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM employee WHERE employeeid = ?");
                stmt.setInt(1, emp.getId());
                stmt.executeUpdate();
                employees.remove(emp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateEmployee(Employee emp) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateEmployee.fxml"));
            Parent root = loader.load();
            UpdateEmployeeController controller = loader.getController();
            controller.setEmployee(emp);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Employee");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
