package scenario3.viewfxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Small JavaFX launcher for Scenario 3 dashboard.
 * Run this class to test Scenario 3 independently.
 */
public class DashboardApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Use absolute classpath starting with "/" for resources
        Parent root = FXMLLoader.load(getClass().getResource("/scenario3/viewfxml/fxml/dashboard.fxml"));

        Scene scene = new Scene(root, 1100, 700);

        primaryStage.setTitle("YorkU - Room Monitor Dashboard");
        primaryStage.setScene(scene);

        primaryStage.setFullScreen(false);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
