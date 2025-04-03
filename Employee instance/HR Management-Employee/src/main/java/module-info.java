module com.example.hrmanagementemployee {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hrmanagementemployee to javafx.fxml;
    exports com.example.hrmanagementemployee;
}