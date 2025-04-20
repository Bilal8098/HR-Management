package com.example.hrmanagementmanager;

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

public class ReportsController {
    @FXML private VBox pdfListContainer;
    @FXML
    public void initialize() {
        loadPDFCards();
    }

    private void loadPDFCards() {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        String sql = """
            SELECT ReportID, ReportDate
              FROM Reports
          ORDER BY ReportDate DESC
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int reportId = rs.getInt("ReportID");
                Date reportDate = rs.getDate("ReportDate");

                HBox card = new HBox(20);
                card.setStyle(
                    "-fx-padding: 10; " +
                    "-fx-background-color: #1e1e1e; " +
                    "-fx-border-color: #FFD700; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 10; " +
                    "-fx-background-radius: 10;"
                );

                Label idLabel = new Label("Report ID: " + reportId);
                Label dateLabel = new Label("Date: " + reportDate.toString());
                Button viewButton = new Button("View PDF");
                viewButton.getStyleClass().add("loginButton");
                viewButton.setOnAction(e -> openPDF(reportId));

                card.getChildren().addAll(idLabel, dateLabel, viewButton);
                pdfListContainer.getChildren().add(card);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openPDF(int reportId) {
        String url = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
        String user = "postgres";
        String password = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

        String sql = "SELECT PDFReport FROM Reports WHERE ReportID = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reportId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    byte[] pdfData = rs.getBytes("PDFReport");
                    File tempFile = File.createTempFile("Report_" + reportId + "_", ".pdf");
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        fos.write(pdfData);
                    }
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(tempFile);
                    } else {
                        System.out.println("Desktop not supported.");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
