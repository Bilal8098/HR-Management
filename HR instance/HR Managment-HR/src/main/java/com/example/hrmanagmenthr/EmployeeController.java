package com.example.hrmanagmenthr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.hrmanagmenthr.PDFGeneration.SalaryReportGenerator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SuppressWarnings("ALL")
public class EmployeeController {
    @FXML
    private Pane titleBar;
    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;
    private double xOffset = 0;
    private double yOffset = 0;

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

    // Four separate search fields from FXML
    @FXML
    private TextField searchField; // For ID
    @FXML
    private TextField searchField1; // For Full Name
    @FXML
    private TextField searchField11; // For Email
    @FXML
    private TextField searchField111; // For Phone

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> idCol;

    @FXML
    private TableColumn<Employee, String> nameCol;

    @FXML
    private TableColumn<Employee, String> emailCol;

    @FXML
    private TableColumn<Employee, String> phoneCol;

    @FXML
    private TableColumn<Employee, String> addressCol;

    @FXML
    private TableColumn<Employee, Double> salaryCol;

    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    @FXML
    private Button viewAttendance;
    @FXML private Button resetSalary;

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
        // Set up cell value factories
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));

        // Set items to table and load data
        employeeTable.setItems(employees);
        loadAllEmployees();

        // If the scene is already set, add the stylesheet.
        if (employeeTable.getScene() != null) {
            employeeTable.getScene().getStylesheets().add(
                    getClass().getResource("styles.css").toExternalForm());
        }
    }
private Stage loadingStage;

private void showLoadingDialog() {
    ProgressBar progressBar = new ProgressBar();
    progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    progressBar.setPrefWidth(250);

    Label label = new Label("Processing salary reset...");
    VBox vbox = new VBox(15, label, progressBar);
    vbox.setAlignment(Pos.CENTER);
    vbox.setPadding(new Insets(20));

    Scene scene = new Scene(vbox);

    loadingStage = new Stage();
    loadingStage.initModality(Modality.APPLICATION_MODAL);
    loadingStage.setResizable(false);
    loadingStage.setTitle("Please Wait");
    loadingStage.setScene(scene);
    loadingStage.show();
}

private void hideLoadingDialog() {
    if (loadingStage != null) {
        loadingStage.close();
    }
}
@FXML
private void handleResetSalary(ActionEvent event) {
    showLoadingDialog();

    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            SalaryReportGenerator.main(new String[0]);
            return null;
        }
        @Override
        protected void succeeded() {
            super.succeeded();
            hideLoadingDialog();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Done");
                alert.setHeaderText(null);
                alert.setContentText("Salary reset completed successfully.");
                alert.showAndWait();
            });
        }

        @Override
        protected void failed() {
            super.failed();
            hideLoadingDialog();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to reset salary.");
                alert.showAndWait();
            });
        }
    };

    new Thread(task).start();
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
                        rs.getString("password"),
                        rs.getBytes("fingerprint")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The Search button action.
     * Combines search criteria from 4 text fields using OR logic.
     */
    public void handleSearch(ActionEvent event) {
        // Retrieve values from the search fields
        String idKeyword = searchField.getText().trim();
        String nameKeyword = searchField1.getText().trim();
        String emailKeyword = searchField11.getText().trim();
        String phoneKeyword = searchField111.getText().trim();

        // If a field is empty, replace with "%" which matches anything.
        // Otherwise, wrap the keyword with "%" for partial matching.
        idKeyword = idKeyword.isEmpty() ? "%" : idKeyword; // For ID, use exact match if provided
        nameKeyword = nameKeyword.isEmpty() ? "%" : "%" + nameKeyword + "%";
        emailKeyword = emailKeyword.isEmpty() ? "%" : "%" + emailKeyword + "%";
        phoneKeyword = phoneKeyword.isEmpty() ? "%" : "%" + phoneKeyword + "%";

        // Log parameters for debugging
        System.out.println("Searching with: ID: " + idKeyword + ", FullName: " + nameKeyword +
                ", Email: " + emailKeyword + ", Phone: " + phoneKeyword);

        employees.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Query construction: if ID is not "%" (i.e., user entered a specific ID), use
            // exact match
            String query;
            if (idKeyword.equals("%")) {
                query = "SELECT * FROM employees WHERE fullname LIKE ? AND email LIKE ? AND phone LIKE ?";
            } else {
                query = "SELECT * FROM employees WHERE employeeid = ? AND fullname LIKE ? AND email LIKE ? AND phone LIKE ?";
            }

            PreparedStatement stmt = conn.prepareStatement(query);

            // Set parameters based on whether we have a specific ID
            if (idKeyword.equals("%")) {
                stmt.setString(1, nameKeyword);
                stmt.setString(2, emailKeyword);
                stmt.setString(3, phoneKeyword);
            } else {
                stmt.setInt(1, Integer.parseInt(idKeyword)); // Assuming employeeid is an integer
                stmt.setString(2, nameKeyword);
                stmt.setString(3, emailKeyword);
                stmt.setString(4, phoneKeyword);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int employeeId = rs.getInt("employeeid");
                String fullName = rs.getString("fullname");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                double salary = rs.getDouble("salary");
                String password = rs.getString("password");
                byte[] fingerprint = rs.getBytes("fingerprint");
                employees.add(new Employee(employeeId, fullName, email, phone, address, salary, password, fingerprint));
            }

            // Log if no data is returned
            if (employees.isEmpty()) {
                System.out.println("No matching data found.");
            }
            // Set the table data
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

    /**
     * Called when the external Delete button is pressed.
     */
    public void deleteEmployee() {
        Employee selectedEmp = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmp == null) {
            showAlert("Please select an employee to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this employee?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);
                try (PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM salary WHERE employeeid = ?");
                        PreparedStatement stmt2 = conn
                                .prepareStatement("DELETE FROM attendance WHERE employeeid = ?")) {

                    stmt1.setInt(1, selectedEmp.getId());
                    stmt2.setInt(1, selectedEmp.getId());
                    stmt1.executeUpdate();
                    stmt2.executeUpdate();

                    try (PreparedStatement stmt3 = conn
                            .prepareStatement("DELETE FROM employees WHERE employeeid = ?")) {
                        stmt3.setInt(1, selectedEmp.getId());
                        stmt3.executeUpdate();
                    }
                    conn.commit();
                    employees.remove(selectedEmp);
                } catch (Exception e) {
                    conn.rollback();
                    e.printStackTrace();
                } finally {
                    conn.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called when the external Update button is pressed.
     */
    public void updateEmployee() {
        Employee selectedEmp = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmp == null) {
            showAlert("Please select an employee to update.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateEmployee.fxml"));
            Parent root = loader.load();
            UpdateEmployeeController controller = loader.getController();
            controller.setEmployee(selectedEmp);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Employee");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewSalaries() {
        Employee selectedEmp = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmp == null) {
            showAlert("Please select an employee to view salaries.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SalariesTable.fxml"));
            Parent root = loader.load();

            // Get controller and pass employee
            SalariesController controller = loader.getController();
            controller.setEmployee(selectedEmp);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Salaries - " + selectedEmp.getFullName());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to show alerts.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void viewAttendance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Attendance.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Attendance table");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to open attendance page.");
        }
    }

    public void openraiseValuePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("raiseValue.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setTitle("Raise Salaries for All Employees");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to open Raise All page.");
        }
    }

    @FXML
    private TextField value;

    @FXML
    public void raiseAll() {
        try {
            double raiseAmount = Double.parseDouble(value.getText().trim());

            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE employees SET salary = salary + ?");
                stmt.setDouble(1, raiseAmount);
                int rowsUpdated = stmt.executeUpdate();

                showAlert(rowsUpdated + " employees got a raise of " + raiseAmount + " successfully.");
            }

            // Close the raise window
            Stage stage = (Stage) value.getScene().getWindow();
            stage.close();

            // Reload table data
            loadAllEmployees();

        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to apply raise.");
        }
    }

}
