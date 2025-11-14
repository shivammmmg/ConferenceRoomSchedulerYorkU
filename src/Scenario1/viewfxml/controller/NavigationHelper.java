package Scenario1.viewfxml.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * NavigationHelper
 * -------------------------------
 * A small utility class that centralizes scene navigation logic
 * for all controllers in Scenario 1.
 *
 * Why this class exists:
 *  - Prevents repeating the same navigation code (DRY principle)
 *  - Ensures every screen loads using the same method
 *  - Automatically re-applies fullscreen properties when navigating
 *  - Simplifies controller code (controllers only call navigate())
 *
 * Usage:
 *      NavigationHelper.navigate(stage, "login.fxml");
 *      NavigationHelper.navigate(stage, "register.fxml");
 */
public class NavigationHelper {

    /**
     * Navigates to another FXML file within the application.
     *
     * @param stage    The current JavaFX Stage (window) to update.
     * @param fxmlPath The FXML filename to load (e.g. "login.fxml").
     *
     * Behavior:
     *  - Loads the new FXML file
     *  - Replaces the existing scene
     *  - Forces fullscreen mode again (important because alerts break fullscreen)
     *  - Removes the default fullscreen exit hint & key binding
     */
    public static void navigate(Stage stage, String fxmlPath) {
        try {
            // Load requested FXML from Scenario1's fxml folder
            FXMLLoader loader = new FXMLLoader(
                    NavigationHelper.class.getResource("/Scenario1/viewfxml/fxml/" + fxmlPath)
            );

            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Replace the current scene with the new one
            stage.setScene(scene);

            // Re-apply fullscreen settings on every screen change
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");                         // Hide "press ESC to exit fullscreen"
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable exit key

        } catch (Exception e) {
            System.err.println("[Navigation Error] Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
