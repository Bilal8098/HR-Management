module com.example.hrmanagementemployee {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires javafx.graphics;
    requires org.postgresql.jdbc;


    opens com.example.hrmanagementemployee to javafx.fxml;  // Ensure reflection access to your package
    exports com.example.hrmanagementemployee;  // Expose your package to other modules
}