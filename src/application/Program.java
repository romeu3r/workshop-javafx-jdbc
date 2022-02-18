package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Program extends Application {
    private static Scene mainStage;

    public static void main(String[] args) {
        launch();
    }

    public static Scene getMainStage() {
        return mainStage;
    }

    @Override
    public synchronized void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
        ScrollPane scrollPane = fxmlLoader.load();

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        mainStage = new Scene(scrollPane);
        stage.setScene(mainStage);
        stage.setTitle("My application");
        stage.show();
    }
}