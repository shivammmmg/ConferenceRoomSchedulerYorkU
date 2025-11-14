package Scenario1.viewfxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegisterApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/register.fxml"));
            Scene scene = new Scene(root);

            stage.setTitle("YorkU Room Scheduler - Register");
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Could not load register.fxml");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
