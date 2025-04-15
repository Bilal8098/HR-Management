package com.example.hrmanagementmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.IOException;

import javafx.scene.control.Button;
import javafx.stage.Stage;
public class MainController {
    @FXML
    private Button viewEmps;
    public void viewEmps(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewEmployees.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Employees List");
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}