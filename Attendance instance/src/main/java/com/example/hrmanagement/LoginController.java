package com.example.hrmanagement;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {
      @FXML private HBox titleBar;
    @FXML private Button closeButton;
    @FXML private Button minimizeButton;
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

    @FXML
    private TextField employeeIdField; // TextField for Employee ID input

    @FXML
    private Button loginButton; // Button for fingerprint verification

    @FXML
    private Label idLabel;

    // Path to the provided fingerprint BMP file that will be compared
    private static final String PROVIDED_FINGERPRINT_PATH = "C:/tmp/finger1.bmp";

    @FXML
    public void initialize() {
        // Ensure that employee ID field accepts only numeric characters
        employeeIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                employeeIdField.setText(newValue.replaceAll("[^\\d]", "")); // Remove non-numeric characters
            }
        });
          // Enable dragging the window
          titleBar.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    public void verifyFingerPrint() {
        String employeeId = employeeIdField.getText();
        if (employeeId.isEmpty()){
            Alert empty = new Alert(AlertType.ERROR, 
                    "Please enter your ID", ButtonType.OK);
            empty.setTitle("Empty Data!!");
            empty.setHeaderText("You didn't enter data");
            // Set the dialog pane background and base font size
            empty.getDialogPane().setStyle("-fx-background-color: #121212; -fx-font-size: 14px;");
            empty.getDialogPane().setPrefSize(400, 250);
            styleAlert(empty);
            empty.show();
            return;
        }
        // Create a progress indicator for the button
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(24, 24);
        progressIndicator.setStyle("-fx-progress-color: black;");
        
        // Replace the button text with the progress indicator
        String originalText = loginButton.getText();
        loginButton.setGraphic(progressIndicator);
        loginButton.setText("");

        // Run the authentication in a background task using Employee ID only
        Task<Boolean> authTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return authenticateUser(employeeId);
            }
        };

        authTask.setOnSucceeded(workerStateEvent -> {
            Platform.runLater(() -> {
                // Restore the button text and graphic
                loginButton.setGraphic(null);
                loginButton.setText(originalText);

                boolean authSuccess = authTask.getValue();
                if (authSuccess) {
                    // Proceed to fingerprint verification...
                    Alert fingerprintAlert = new Alert(AlertType.NONE);
                    fingerprintAlert.setTitle("Fingerprint Verification");
                    fingerprintAlert.setHeaderText("Scanning Fingerprint...");
                    
                    // Set the dialog pane background and base font size
                    fingerprintAlert.getDialogPane().setStyle("-fx-background-color: #121212; -fx-font-size: 14px;");

                    ProgressIndicator fpIndicator = new ProgressIndicator();
                    fpIndicator.setPrefSize(40, 40);
                    fpIndicator.setStyle("-fx-progress-color: gold;"); // Make it golden

                    VBox fpContent = new VBox(10, fpIndicator);
                    fpContent.setStyle("-fx-padding: 20px; -fx-alignment: center;");
                    fingerprintAlert.getDialogPane().setContent(fpContent);
                    fingerprintAlert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                    fingerprintAlert.getDialogPane().setPrefSize(400, 250);
                    fingerprintAlert.show();
                    
                    // Style the header and content of the alert
                    styleAlert(fingerprintAlert);

                    PauseTransition delay = new PauseTransition(Duration.seconds(3));
                    delay.setOnFinished(event -> {
                        if (verifyFingerprint(employeeId)) {
                            fingerprintAlert.close();
                            recordAttendance(employeeId);

                            Alert successAlert = new Alert(AlertType.INFORMATION, 
                                    "Authentication successful! Attendance recorded.", ButtonType.OK);
                            successAlert.getDialogPane().setStyle("-fx-background-color: #121212; -fx-font-size: 14px;");
                            successAlert.show();
                            
                            styleAlert(successAlert);
                        } else {
                            fingerprintAlert.close();
                            Alert failureAlert = new Alert(AlertType.ERROR, 
                                    "Fingerprint verification failed!", ButtonType.OK);
                            failureAlert.getDialogPane().setStyle("-fx-background-color: #121212; -fx-font-size: 14px;");
                            failureAlert.show();
                            
                            styleAlert(failureAlert);
                        }
                    });
                    delay.play();
                } else {
                    Alert invalidAlert = new Alert(AlertType.ERROR, 
                            "Invalid Employee ID!", ButtonType.OK);
                    invalidAlert.getDialogPane().setStyle("-fx-background-color: #121212; -fx-font-size: 14px;");
                    invalidAlert.show();
                    
                    styleAlert(invalidAlert);
                }
            });
        });

        authTask.setOnFailed(workerStateEvent -> {
            Platform.runLater(() -> {
                // Restore the button text and graphic on error
                loginButton.setGraphic(null);
                loginButton.setText(originalText);
                
                Alert errorAlert = new Alert(AlertType.ERROR, 
                        "An error occurred during authentication!", ButtonType.OK);
                errorAlert.getDialogPane().setStyle("-fx-background-color: #121212; -fx-font-size: 14px;");
                errorAlert.show();
                
                styleAlert(errorAlert);
            });
        });
        new Thread(authTask).start();
    }

    /**
     * Styles an alert such that its header text is black and bold,
     * and its content text is golden and bold.
     */
    private void styleAlert(Alert alert) {
        Platform.runLater(() -> {
            // Lookup the header region and set its style (if present)
            Node header = alert.getDialogPane().lookup(".header-panel");
            if (header != null) {
                header.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
            }
            // Lookup content labels and apply golden color and bold weight
            for (Node node : alert.getDialogPane().lookupAll(".content.label")) {
                if (node instanceof Label) {
                    node.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold;");
                }
            }
        });
    }

    // Function to authenticate the user using only the EmployeeID.
    private boolean authenticateUser(String employeeId) {
        // Database connection details
        String dbUrl = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String dbUser = "postgres";
        String dbPassword = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";
        String sql = "SELECT EmployeeID FROM Employees WHERE EmployeeID = ?";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set EmployeeID parameter
            statement.setInt(1, Integer.parseInt(employeeId));
            ResultSet resultSet = statement.executeQuery();

            // Authentication is successful if an employee record is found.
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Function to verify the fingerprint by comparing the provided BMP file with the stored fingerprint
    private boolean verifyFingerprint(String employeeId) {
        // Database connection details
        String dbUrl = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String dbUser = "postgres";
        String dbPassword = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";
        String sql = "SELECT FingerPrint FROM Employees WHERE EmployeeID = ?";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set EmployeeID parameter
            statement.setInt(1, Integer.parseInt(employeeId));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the fingerprint data stored in the database
                byte[] storedFingerprint = resultSet.getBytes("FingerPrint");

                // Load the provided fingerprint BMP file from the file system
                byte[] providedFingerprint = Files.readAllBytes(Paths.get(PROVIDED_FINGERPRINT_PATH));

                // Compare the two byte arrays; in this simple example, they must be exactly equal.
                return storedFingerprint != null && java.util.Arrays.equals(storedFingerprint, providedFingerprint);
            } else {
                return false; // Employee not found
            }
        } catch (Exception e) { // Catch both SQLException and IOException
            e.printStackTrace();
            return false;
        }
    }

    // Function to record attendance in the Attendance table
    private void recordAttendance(String employeeId) {
        // Database connection details
        String dbUrl = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String dbUser = "postgres";
        String dbPassword = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";
        String sql = "INSERT INTO Attendance (EmployeeID) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(employeeId));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
