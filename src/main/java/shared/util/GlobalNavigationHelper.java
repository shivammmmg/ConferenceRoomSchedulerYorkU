package shared.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyCombination;

public class GlobalNavigationHelper {

    private static Stage primaryStage;

    // Register the Stage from Launcher.java
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    // Navigate to ANY FXML in ANY scenario
    public static void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    GlobalNavigationHelper.class.getResource(fxmlPath)
            );

            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);

            // ---------------------------------------
            // FULLSCREEN settings for ALL pages
            // ---------------------------------------
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("[Navigation Error] Cannot load: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
