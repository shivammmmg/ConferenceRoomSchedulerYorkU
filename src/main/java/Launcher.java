import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.util.GlobalNavigationHelper;

/**
 * Launcher – Main Entry Point (Full Application)
 * ------------------------------------------------------------------------
 * <p>This class is the **primary launcher** for the entire Conference Room
 * Scheduler system. When the runnable project is submitted, THIS is the
 * file the TA executes to start your application.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Bootstraps the JavaFX runtime (extends {@link Application}).</li>
 *     <li>Loads the Main Landing Page (MainPage.fxml).</li>
 *     <li>Registers the global stage via {@link GlobalNavigationHelper} so that
 *         all modules (Scenarios 1–4) can perform consistent navigation.</li>
 *     <li>Applies fullscreen behavior matching the LoginApp / RegisterApp
 *         for a unified UI experience.</li>
 * </ul>
 *
 * <h2>System Context</h2>
 * <ul>
 *     <li><b>Scenario 1</b> – Users proceed to Login from MainPage.</li>
 *     <li><b>Scenario 2</b> – BookingFX is launched after login.</li>
 *     <li><b>Scenario 3</b> – Observer + Sensor logic triggered during booking usage.</li>
 *     <li><b>Scenario 4</b> – AdminFX dashboard for admins/chief coordinators.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Facade</b>: GlobalNavigationHelper manages app-wide navigation.</li>
 *     <li>Other patterns (Singleton, Observer, Builder, Factory, Strategy)
 *         are initialized indirectly after launch.</li>
 * </ul>
 *
 * <h2>Notes for Submission</h2>
 * <ul>
 *     <li>This class should be used as the **main entry point** when testing your project.</li>
 *     <li>Does not handle business logic — only UI initialization and navigation setup.</li>
 * </ul>
 */
public class Launcher extends Application {

    /**
     * Initializes the application:
     * <ul>
     *     <li>Registers the primary stage globally</li>
     *     <li>Loads MainPage.fxml</li>
     *     <li>Applies fullscreen UI rules</li>
     * </ul>
     *
     * @param stage the main application window created by JavaFX
     */
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
