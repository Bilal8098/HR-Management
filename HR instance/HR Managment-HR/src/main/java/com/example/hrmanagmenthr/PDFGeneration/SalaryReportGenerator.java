package com.example.hrmanagmenthr.PDFGeneration;

import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class SalaryReportGenerator {

    // 🔐 Your actual DB credentials
    private static final String JDBC_URL = "jdbc:postgresql://shinkansen.proxy.rlwy.net:58078/railway";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "NqlVODXIobgaOsHmwWHqXllPtOVOZril";
    static String currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

    private static final String OUTPUT_PDF = "EmployeeSalaryReport" + "(" + currentDate + ")" + ".pdf";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // 1. Fetch each employee with their latest ActualSalary
            String sql = "SELECT e.EmployeeID, e.FullName, sub.ActualSalary " +
                    "FROM Employees e " +
                    "LEFT JOIN ( " +
                    "  SELECT DISTINCT ON (EmployeeID) EmployeeID, ActualSalary " +
                    "  FROM Salary " +
                    "  ORDER BY EmployeeID, SalaryID DESC " +
                    ") sub ON e.EmployeeID = sub.EmployeeID " +
                    "ORDER BY e.EmployeeID";

            try (PreparedStatement pst = conn.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery()) {

                // 2. Build the PDF in memory
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Document document = new Document(PageSize.A4, 36, 36, 54, 36);
                PdfWriter.getInstance(document, baos);
                document.open();

                // Styling colors
                BaseColor gold = new BaseColor(255, 215, 0);
                BaseColor black = BaseColor.BLACK;

                // Fonts
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, black);
                Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, black);
                Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11, black);
                Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, black);

                // Header with icon space
                PdfPTable headerTable = new PdfPTable(new float[] { 1, 6, 3 });
                headerTable.setWidthPercentage(100);
                PdfPCell iconCell = new PdfPCell();
                iconCell.setFixedHeight(50);
                iconCell.setBorder(Rectangle.NO_BORDER);
                // leave blank or add image later
                headerTable.addCell(iconCell);

                PdfPCell titleCell = new PdfPCell(new Phrase("Employees Salary Report", titleFont));
                titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                titleCell.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(titleCell);
                PdfPCell dateCell = new PdfPCell(new Phrase(currentDate, cellFont));
                dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                dateCell.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(dateCell);

                document.add(headerTable);
                document.add(Chunk.NEWLINE);

                // Table: ID | Name | ActualSalary
                PdfPTable table = new PdfPTable(new float[] { 1, 4, 2 });
                table.setWidthPercentage(100);

                // Header row styling
                PdfPCell h1 = new PdfPCell(new Phrase("ID", headFont));
                PdfPCell h2 = new PdfPCell(new Phrase("Full Name", headFont));
                PdfPCell h3 = new PdfPCell(new Phrase("Actual Salary", headFont));
                h1.setBackgroundColor(gold);
                h1.setHorizontalAlignment(Element.ALIGN_CENTER);
                h2.setBackgroundColor(gold);
                h2.setHorizontalAlignment(Element.ALIGN_CENTER);
                h3.setBackgroundColor(gold);
                h3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(h1);
                table.addCell(h2);
                table.addCell(h3);

                // Data rows & sum
                BigDecimal total = BigDecimal.ZERO;
                while (rs.next()) {
                    int id = rs.getInt("EmployeeID");
                    String name = rs.getString("FullName");
                    BigDecimal actual = rs.getBigDecimal("ActualSalary");
                    if (actual == null)
                        actual = BigDecimal.ZERO;

                    PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf(id), cellFont));
                    PdfPCell c2 = new PdfPCell(new Phrase(name, cellFont));
                    PdfPCell c3 = new PdfPCell(new Phrase(actual.toString(), cellFont));
                    c1.setPadding(5);
                    c2.setPadding(5);
                    c3.setPadding(5);
                    table.addCell(c1);
                    table.addCell(c2);
                    table.addCell(c3);

                    total = total.add(actual);
                }

                document.add(table);
                document.add(Chunk.NEWLINE);

                // Total paragraph
                Paragraph totalPara = new Paragraph(
                        "Total money spent on salaries: " + total.toString(),
                        totalFont);
                totalPara.setAlignment(Element.ALIGN_RIGHT);
                document.add(totalPara);

                document.close();

                byte[] pdfBytes = baos.toByteArray();

                // 3. Write to project directory
                try (FileOutputStream fos = new FileOutputStream(OUTPUT_PDF)) {
                    fos.write(pdfBytes);
                }
                System.out.println("PDF written to " + OUTPUT_PDF);

                // 4. Upload into Reports table
                String insert = "INSERT INTO Reports (PDFReport) VALUES (?)";
                try (PreparedStatement ins = conn.prepareStatement(insert)) {
                    ins.setBytes(1, pdfBytes);
                    ins.executeUpdate();
                }
                System.out.println("PDFReport blob saved in database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}