module com.example.hrmanagementmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.hrmanagementmanager to javafx.fxml;
    exports com.example.hrmanagementmanager;
}