package com.example.hrmanagmenthr.PDFGeneration;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class SalaryReportGenerator {

    // 🔐 Your actual DB credentials
    private static final String JDBC_URL      = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
    private static final String JDBC_USER     = "postgres";
    private static final String JDBC_PASSWORD = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";
    static String currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Generate and save general report
            byte[] generalPdf = generateGeneralReport(conn);
            insertBlob(conn, "INSERT INTO Reports (PDFReport) VALUES (?)", null, generalPdf);
            System.out.println("General report generated and saved.");

            // Generate and save individual reports
            List<Integer> employeeIds = fetchEmployeeIds(conn);
            for (Integer empId : employeeIds) {
                byte[] empPdf = generateIndividualReport(conn, empId);
                insertBlob(conn, "INSERT INTO EmployeePDFs (EmployeeID, PDF) VALUES (?, ?)", empId, empPdf);
                System.out.println("Report for EmployeeID " + empId + " saved.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Done and Doneee!!");
    }

    /** Fetches all employee IDs */
    private static List<Integer> fetchEmployeeIds(Connection conn) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT EmployeeID FROM Employees";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) ids.add(rs.getInt("EmployeeID"));
        }
        return ids;
    }

    /** Generates the general report PDF */
    private static byte[] generateGeneralReport(Connection conn) throws Exception {
        String sql =
            "SELECT e.EmployeeID, e.FullName, sub.ActualSalary FROM Employees e " +
            "LEFT JOIN (SELECT DISTINCT ON (EmployeeID) EmployeeID, ActualSalary " +
            "FROM Salary ORDER BY EmployeeID, SalaryID DESC) sub " +
            "ON e.EmployeeID = sub.EmployeeID ORDER BY e.EmployeeID";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            return buildGeneralPdf(rs, "Employee Salary Report");
        }
    }

    /** Generates an individual employee report including full salary history */
    private static byte[] generateIndividualReport(Connection conn, int empId) throws Exception {
        // Fetch employee basic info
        String empSql = "SELECT FullName, Salary AS MainSalary FROM Employees WHERE EmployeeID = ?";
        String histSql =
            "SELECT SalaryID, ActualSalary, DifferenceAmount, DeductionOrRaise, Reason " +
            "FROM Salary WHERE EmployeeID = ? ORDER BY SalaryID DESC";

        try (PreparedStatement empPst = conn.prepareStatement(empSql);
             PreparedStatement histPst = conn.prepareStatement(histSql)) {
            empPst.setInt(1, empId);
            ResultSet empRs = empPst.executeQuery();
            if (!empRs.next()) {
                throw new SQLException("Employee not found: " + empId);
            }
            String fullName = empRs.getString("FullName");
            BigDecimal mainSalary = empRs.getBigDecimal("MainSalary");

            histPst.setInt(1, empId);
            ResultSet histRs = histPst.executeQuery();

            return buildIndividualPdf(empId, fullName, mainSalary, histRs);
        }
    }

    /** Builds the PDF for the general report */
    private static byte[] buildGeneralPdf(ResultSet rs, String title) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter.getInstance(document, baos);
        document.open();

        // Styling
        BaseColor gold = new BaseColor(255, 215, 0);
        BaseColor black = BaseColor.BLACK;
        Font headFont  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, black);
        Font cellFont  = FontFactory.getFont(FontFactory.HELVETICA, 11, black);
        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, gold);

        // Header
 // Header Title Row
PdfPTable header = new PdfPTable(new float[]{6, 2});
header.setWidthPercentage(100);

// Left side: Payroll report title
String monthName = new SimpleDateFormat("MMMM yyyy").format(new java.util.Date());
PdfPCell titleCell = new PdfPCell(new Phrase("Payroll Report for " + monthName, headFont));
titleCell.setBorder(Rectangle.NO_BORDER);
titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
header.addCell(titleCell);

// Right side: Current Date
PdfPCell dateCell = new PdfPCell(new Phrase(currentDate, cellFont));
dateCell.setBorder(Rectangle.NO_BORDER);
dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
header.addCell(dateCell);

document.add(header);
document.add(Chunk.NEWLINE);


        // Table
        PdfPTable table = new PdfPTable(new float[]{1, 4, 2});
        table.setWidthPercentage(100);
        for (String h : new String[]{"ID","Full Name","Actual Salary"}) {
            PdfPCell c = headerCell(h, headFont, gold);
            table.addCell(c);
        }
        BigDecimal total = BigDecimal.ZERO;
        while (rs.next()) {
            table.addCell(new PdfPCell(new Phrase(rs.getString("EmployeeID"), cellFont)));
            table.addCell(new PdfPCell(new Phrase(rs.getString("FullName"), cellFont)));
            String val = rs.getBigDecimal("ActualSalary")!=null? rs.getBigDecimal("ActualSalary").toString():"0";
            table.addCell(new PdfPCell(new Phrase(val, cellFont)));
            total = total.add(new BigDecimal(val));
        }
        document.add(table);
        document.add(Chunk.NEWLINE);
        document.add(alignedParagraph("Total: " + total, totalFont, Element.ALIGN_RIGHT));
        document.close();
        return baos.toByteArray();
    }

    /** Builds an individual employee PDF with history */
    private static byte[] buildIndividualPdf(int empId, String fullName, BigDecimal mainSalary, ResultSet histRs) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter.getInstance(document, baos);
        document.open();

        // Styling
        BaseColor gold = new BaseColor(255, 215, 0);
        BaseColor black = BaseColor.BLACK;
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, black);
        Font headFont  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, black);
        Font cellFont  = FontFactory.getFont(FontFactory.HELVETICA, 11, black);

        // Header
        PdfPTable header = new PdfPTable(new float[]{1, 6, 3});
        header.setWidthPercentage(100);
        header.addCell(emptyCell(50));
        header.addCell(noBorderCell("Salary Details for " + fullName, titleFont));
        header.addCell(noBorderCell(currentDate, cellFont));
        document.add(header);
        document.add(Chunk.NEWLINE);

        // Employee info
        document.add(new Paragraph("Employee ID: " + empId, cellFont));
        document.add(new Paragraph("Full Name: " + fullName, cellFont));
        document.add(new Paragraph("Main Salary: " + mainSalary, cellFont));
        document.add(Chunk.NEWLINE);

        // History table
        PdfPTable histTable = new PdfPTable(new float[]{1,2,2,2,4});
        histTable.setWidthPercentage(100);
        for (String h : new String[]{"SalaryID","ActualSalary","Difference","Type","Reason"}) {
            histTable.addCell(headerCell(h, headFont, gold));
        }
        while (histRs.next()) {
            histTable.addCell(new PdfPCell(new Phrase(histRs.getString("SalaryID"), cellFont)));
            histTable.addCell(new PdfPCell(new Phrase(histRs.getBigDecimal("ActualSalary").toString(), cellFont)));
            histTable.addCell(new PdfPCell(new Phrase(histRs.getBigDecimal("DifferenceAmount").toString(), cellFont)));
            histTable.addCell(new PdfPCell(new Phrase(histRs.getString("DeductionOrRaise"), cellFont)));
            histTable.addCell(new PdfPCell(new Phrase(histRs.getString("Reason"), cellFont)));
        }
        document.add(histTable);
        document.close();
        return baos.toByteArray();
    }

    /** Utility methods for cells and paragraphs **/
    private static PdfPCell emptyCell(int height) {
        PdfPCell cell = new PdfPCell(); cell.setFixedHeight(height); cell.setBorder(Rectangle.NO_BORDER); return cell;
    }
    private static PdfPCell noBorderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font)); cell.setBorder(Rectangle.NO_BORDER); cell.setVerticalAlignment(Element.ALIGN_MIDDLE); return cell;
    }
    private static PdfPCell headerCell(String text, Font font, BaseColor bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font)); cell.setBackgroundColor(bg); cell.setHorizontalAlignment(Element.ALIGN_CENTER); return cell;
    }
    private static Paragraph alignedParagraph(String text, Font font, int align) {
        Paragraph p = new Paragraph(text, font); p.setAlignment(align); return p;
    }

    /** Inserts a BLOB into the database **/
    private static void insertBlob(Connection conn, String sql, Integer empId, byte[] data) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            int idx = 1;
            if (empId != null) pst.setInt(idx++, empId);
            pst.setBytes(idx, data);
            pst.executeUpdate();
        }
    }
}