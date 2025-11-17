import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.util.GlobalNavigationHelper;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Register the stage globally
        GlobalNavigationHelper.setStage(stage);

        Scene scene = new Scene(
                FXMLLoader.load(getClass().getResource("/mainpage/MainPage.fxml"))
        );

        stage.setScene(scene);
        stage.setTitle("YorkU Conference Room Scheduler");

        // ----------------------------------------------------------
        // FULLSCREEN BEHAVIOR (Same as LoginApp)
        // ----------------------------------------------------------
        stage.setFullScreen(true);                     // Enter fullscreen automatically
        stage.setFullScreenExitHint("");               // Remove "Press ESC to exit fullscreen"
        stage.setFullScreenExitKeyCombination(
                KeyCombination.NO_MATCH               // Disable ESC from exiting fullscreen
        );

        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
