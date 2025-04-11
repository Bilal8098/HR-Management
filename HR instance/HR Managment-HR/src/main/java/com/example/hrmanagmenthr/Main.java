package com.example.hrmanagmenthr;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Ensure URL is correct for FXML
        URL fxmlUrl = getClass().getResource("/com/example/hrmanagmenthr/MainPage.fxml");
        if (fxmlUrl == null) {
            System.err.println("FXML file not found!");
            return;  // Exit early if FXML file is not found
        }

        // Use FXMLLoader instance to load the FXML file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        Parent root = loader.load();

        // Get CSS resource and check if it's found
        URL cssUrl = getClass().getResource("/com/example/hrmanagmenthr/style.css");
        if (cssUrl == null) {
            System.err.println("CSS file not found!");
        } else {
            // Apply the stylesheet if it's found
            Scene scene = new Scene(root);
            scene.getStylesheets().add(cssUrl.toExternalForm());
            primaryStage.setTitle("Employee Manager");
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
