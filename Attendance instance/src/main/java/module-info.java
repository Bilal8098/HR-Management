module com.example.hrmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;

    opens com.example.hrmanagement to javafx.fxml;  // Ensure reflection access to your package
    exports com.example.hrmanagement;  // Expose your package to other modules
}
