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
            Parent root = FXMLLoader.load(
                    getClass().getResource("fxml/register.fxml")   // path is correct
            );

            Scene scene = new Scene(root);

            stage.setTitle("YorkU Conference Room Scheduler - Register");
            stage.setScene(scene);

            // Optional but recommended for Option 2 layout
            stage.setResizable(true);
            stage.setMaximized(true);  // makes background fill screen
            // stage.setFullScreen(true); // if you prefer true fullscreen

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error loading register.fxml");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
