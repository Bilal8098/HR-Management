package com.example.hrmanagmenthr.PDFGeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class AllEmployeePDFsDownloader {

    private static final String JDBC_URL = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";

    public static void main(String[] args) {
        // Folder where we'll save PDFs
        String folderName = "EmployeePDFs";

        // Create the folder if it doesn't exist
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("📁 Folder created: " + folderName);
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Download General Report PDF
            downloadGeneralReport(conn, folderName);

            // Download Employee PDFs
            downloadEmployeePDFs(conn, folderName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadGeneralReport(Connection conn, String folderName) throws SQLException, FileNotFoundException, IOException {
        String sql = "SELECT PDFReport FROM Reports ORDER BY ReportID DESC LIMIT 1"; // Assuming the most recent report is what you want

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                byte[] pdfBytes = rs.getBytes("PDFReport");
                if (pdfBytes != null) {
                    String fileName = folderName + "/General_Report.pdf";
                    try (FileOutputStream fos = new FileOutputStream(fileName)) {
                        fos.write(pdfBytes);
                        System.out.println("✅ Saved General Report as: " + fileName);
                    }
                }
            } else {
                System.out.println("❌ No general report found in the database.");
            }
        }
    }

    private static void downloadEmployeePDFs(Connection conn, String folderName) throws SQLException, IOException {
        String sql = "SELECT PDFID, EmployeeID, PDF FROM EmployeePDFs";

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int pdfId = rs.getInt("PDFID");
                int employeeId = rs.getInt("EmployeeID");
                byte[] pdfBytes = rs.getBytes("PDF");

                if (pdfBytes != null) {
                    String fileName = folderName + "/Employee_" + employeeId + "_PDF_" + pdfId + ".pdf";
                    try (FileOutputStream fos = new FileOutputStream(fileName)) {
                        fos.write(pdfBytes);
                        System.out.println("✅ Saved PDF for Employee ID " + employeeId + " as: " + fileName);
                    }
                }
            }
        }
    }
}
