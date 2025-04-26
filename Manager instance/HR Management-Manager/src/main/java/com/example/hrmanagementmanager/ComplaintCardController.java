package com.example.hrmanagementmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ComplaintCardController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label shortDescriptionLabel;

    private String fullComplaint;

    public void setComplaint(String title, String shortDescription, String fullComplaint) {
        titleLabel.setText(title);
        shortDescriptionLabel.setText(shortDescription);
        this.fullComplaint = fullComplaint;
    }

    @FXML
    private void showDetails(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Complaint Details");
        alert.setHeaderText(titleLabel.getText());
        alert.setContentText(fullComplaint);
        alert.showAndWait();
    }
}
