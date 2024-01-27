package com.hms;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/hms/Home.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 900, 650);

        stage.setTitle("Hotel Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(new Image(Main.class.getResource("/images/hotel-icon.png").toExternalForm()));

        stage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            onExitButtonClick(stage);
        });

        stage.show();
    }

    public void onExitButtonClick(Stage currentStage) {
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        ButtonType home = new ButtonType("Home");

        Alert alert = new Alert(Alert.AlertType.NONE, "Do you want to Exit?", yes, no, home);
        alert.setTitle("Choose");
        alert.getDialogPane().setPrefSize(300, 90);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getButtonTypes().setAll(yes, no, home);

        // Set a handler for when the user clicks the X button
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(e -> {
            alert.setResult(ButtonType.CANCEL);
        });

        Optional<ButtonType> choice = alert.showAndWait();

        if (choice.isPresent()) {
            if (choice.get() == home) {
                try {
                    start(currentStage);    // go to homepage
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (choice.get() == yes) {
                Platform.exit(); // Exit the application
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
