module com.example.hrmanagementhr {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires javafx.graphics;
    requires org.postgresql.jdbc;

    opens com.example.hrmanagmenthr to javafx.fxml;  // Ensure reflection access to your package
    exports com.example.hrmanagmenthr;  // Expose your package to other modules
}