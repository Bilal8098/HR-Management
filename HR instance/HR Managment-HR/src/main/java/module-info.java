module com.example.hrmanagmenthr {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hrmanagmenthr to javafx.fxml;
    exports com.example.hrmanagmenthr;
}