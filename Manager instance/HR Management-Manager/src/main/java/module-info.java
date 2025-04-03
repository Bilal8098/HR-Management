module com.example.hrmanagementmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hrmanagementmanager to javafx.fxml;
    exports com.example.hrmanagementmanager;
}