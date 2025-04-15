package com.example.hrmanagementmanager;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Manager instance");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}