package com.example.hrmanagement;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
               FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Remove default title bar
        stage.initStyle(StageStyle.UNDECORATED);

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));
        stage.setTitle("HR Management");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}