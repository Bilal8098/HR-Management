package com.example.hrmanagementemployee;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EmployeePDFsController {
    @FXML private VBox pdfListContainer;

    private int empID;

    public EmployeePDFsController(int id) {
        this.empID = id;
    }

    @FXML
    public void initialize() {
        loadPDFCards();
    }
    // @FXML
    // private void closeWindow() {
    //     Stage stage = (Stage) closeButton.getScene().getWindow();
    //     stage.close();
    // }

    // @FXML
    // private void minimizeWindow() {
    //     Stage stage = (Stage) minimizeButton.getScene().getWindow();
    //     stage.setIconified(true);
    // }


    private void loadPDFCards() {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT PDFID, PDFDate FROM EmployeePDFs WHERE EmployeeID = ? ORDER BY PDFDate DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int pdfId = rs.getInt("PDFID");
                Date pdfDate = rs.getDate("PDFDate");

                HBox card = new HBox(20);
                card.setStyle("-fx-padding: 10; -fx-background-color: #1e1e1e; -fx-border-color: #FFD700; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; ");

                Label idLabel = new Label("PDF ID: " + pdfId);
                Label dateLabel = new Label("Date: " + pdfDate.toString());
                Button viewButton = new Button("View PDF");
                viewButton.getStyleClass().add("loginButton");
                viewButton.setOnAction(e -> openPDF(pdfId));

                card.getChildren().addAll(idLabel, dateLabel, viewButton);

                pdfListContainer.getChildren().add(card);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openPDF(int pdfId) {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT PDF FROM EmployeePDFs WHERE PDFID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pdfId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                byte[] pdfData = rs.getBytes("PDF");
                File tempFile = File.createTempFile("EmployeePDF_" + pdfId + "_", ".pdf");
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(pdfData);
                }
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(tempFile);
                } else {
                    System.out.println("Desktop not supported.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
