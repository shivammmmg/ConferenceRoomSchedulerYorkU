package scenario1.controller;

import scenario1.controller.UserManager;
import scenario1.controller.NavigationHelper;
import shared.model.User;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Controller for the "Forgot Password" page.
 *
 * Handles:
 *  - Reset instructions validation (email exists or not)
 *  - Auto-complete suggestions for email input
 *  - Logo animation + background blur
 *  - Full-screen–safe popup behavior
 *  - Navigation back to Login page
 *
 * Scenario 1 – Registration & Account Management
 */
public class ForgotPasswordController implements Initializable {

    // ------------------------------
    // FXML UI COMPONENTS
    // ------------------------------
    @FXML private StackPane root;               // Root container for scene transitions + alerts
    @FXML private ImageView backgroundImage;    // Blurred background image
    @FXML private Rectangle darkOverlay;        // Semi-transparent overlay
    @FXML private ImageView logoImage;          // YorkU logo with animation
    @FXML private TextField emailField;         // User’s email input
    @FXML private Button resetBtn;              // Reset password action button
    @FXML private Button backBtn;               // Navigation back to Login

    // Context menu for domain suggestions as user types
    private final ContextMenu emailSuggestions = new ContextMenu();


    // ----------------------------------------------------------
    // INITIALIZATION
    // ----------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBackgroundEffects();      // Blur + overlay binding
        setupLogoAnimation();          // Slide + bounce animation
        setupEmailSuggestions();       // Domain autocomplete
        setupResetButton();            // Reset password validation logic
        setupBackButton();             // Back to Login navigation

        // Smooth fade-in transition when screen loads
        root.setOpacity(0);
        javafx.application.Platform.runLater(() -> {
            FadeTransition fade = new FadeTransition(Duration.millis(900), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        });
    }


    // ----------------------------------------------------------
    // POPUP HANDLER (FULLSCREEN SAFE)
    // ----------------------------------------------------------

    /**
     * Shows an alert popup that works even while the stage is in fullscreen mode.
     * JavaFX cannot show modal dialogs over fullscreen windows unless fullscreen
     * is temporarily disabled.
     */
    private void showPopup(Alert.AlertType type, String title, String header, String content) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Link popup to current window
        Stage stage = (Stage) root.getScene().getWindow();
        alert.initOwner(stage);

        // JavaFX Fix: must exit fullscreen before showing modal alert
        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);  // Restore fullscreen automatically
    }


    // ----------------------------------------------------------
    // NAVIGATION HANDLER
    // ----------------------------------------------------------

    /** Navigates to another FXML screen using the shared NavigationHelper. */
    private void navigate(String fxml) {
        NavigationHelper.navigate((Stage) root.getScene().getWindow(), fxml);
    }


    // ----------------------------------------------------------
    // BACKGROUND SETUP
    // ----------------------------------------------------------

    /**
     * Adds blur to the background and ensures the dark overlay always fills the screen.
     */
    private void setupBackgroundEffects() {
        backgroundImage.setEffect(new GaussianBlur(10));

        // Bind overlay size to background image so it covers the entire window
        darkOverlay.widthProperty().bind(backgroundImage.fitWidthProperty());
        darkOverlay.heightProperty().bind(backgroundImage.fitHeightProperty());
    }


    // ----------------------------------------------------------
    // LOGO ANIMATION
    // ----------------------------------------------------------

    /**
     * Logo smoothly drops from above with a bounce effect.
     * Creates a polished modern login/forgot-password experience.
     */
    private void setupLogoAnimation() {

        logoImage.setTranslateY(-500);
        logoImage.setOpacity(0);

        // Slide down
        TranslateTransition slide = new TranslateTransition(Duration.millis(2200), logoImage);
        slide.setFromY(-500);
        slide.setToY(0);

        // Fade-in
        FadeTransition fade = new FadeTransition(Duration.millis(300), logoImage);
        fade.setFromValue(0);
        fade.setToValue(1);

        // Small bounce at the end
        TranslateTransition bounce = new TranslateTransition(Duration.millis(250), logoImage);
        bounce.setFromY(0);
        bounce.setToY(-15);
        bounce.setAutoReverse(true);
        bounce.setCycleCount(2);

        slide.setOnFinished(e -> bounce.play());

        fade.play();
        slide.play();
    }


    // ----------------------------------------------------------
    // EMAIL AUTOCOMPLETE
    // ----------------------------------------------------------

    /**
     * Adds automatic domain suggestions as the user types their email.
     * Works similarly to Gmail's intelligent email completion.
     */
    private void setupEmailSuggestions() {

        List<String> domains = Arrays.asList("@my.yorku.ca", "@yorku.ca", "@gmail.com");

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            emailSuggestions.getItems().clear();

            int at = newVal.indexOf("@");
            if (at == -1) {
                emailSuggestions.hide();
                return;
            }

            String before = newVal.substring(0, at);

            // Add menu items for each domain option
            for (String d : domains) {
                MenuItem item = new MenuItem(before + d);
                item.setOnAction(e -> emailField.setText(item.getText()));
                emailSuggestions.getItems().add(item);
            }

            emailSuggestions.show(emailField, Side.BOTTOM, 0, 0);
        });

        // Hide suggestions when focus is lost
        emailField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) emailSuggestions.hide();
        });
    }


    // ----------------------------------------------------------
    // RESET PASSWORD BUTTON
    // ----------------------------------------------------------

    /**
     * Handles the "Reset Password" button click.
     * Checks:
     * 1. Email field not empty
     * 2. Email is registered
     * Then simulates sending reset instructions.
     */
    private void setupResetButton() {
        resetBtn.setOnAction(e -> {

            String email = emailField.getText().trim();
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


            // EMPTY EMAIL
            if (email.isEmpty()) {

                System.out.println("");
                System.out.println("┌──────────────────────────────────── FORGOT PASSWORD ─────────────────────────────────────────┐");

                String border = "│ %-12s : %-77s │";

                System.out.println(String.format(border, "Email", "(empty)"));
                System.out.println(String.format(border, "Status", "FAILED"));
                System.out.println(String.format(border, "Reason", "No email entered"));
                System.out.println(String.format(border, "Timestamp", now.format(fmt)));

                System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
                System.out.println("");

                showError("Please enter your email.");
                return;
            }

            // EMAIL NOT REGISTERED
            if (!UserManager.getInstance().checkIfEmailRegistered(email)) {

                System.out.println("");
                System.out.println("┌──────────────────────────────────── FORGOT PASSWORD ─────────────────────────────────────────┐");

                String border = "│ %-12s : %-77s │";

                System.out.println(String.format(border, "Email", email));
                System.out.println(String.format(border, "Status", "FAILED"));
                System.out.println(String.format(border, "Reason", "Email not registered"));
                System.out.println(String.format(border, "Timestamp", now.format(fmt)));

                System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
                System.out.println("");

                showError("No account found with this email.");
                return;
            }

            // SUCCESS
            System.out.println("");
            System.out.println("┌──────────────────────────────────── FORGOT PASSWORD ─────────────────────────────────────────┐");

            String border = "│ %-12s : %-77s │";

            System.out.println(String.format(border, "Email", email));
            System.out.println(String.format(border, "Status", "SUCCESS"));
            System.out.println(String.format(border, "Message", "Reset instructions sent"));
            System.out.println(String.format(border, "Timestamp", now.format(fmt)));

            System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
            System.out.println("");

            showInfo("Password reset instructions sent (simulated).");
            emailField.clear();

        });
    }



    // ----------------------------------------------------------
    // BACK BUTTON (RETURN TO LOGIN)
    // ----------------------------------------------------------

    /** Navigates user back to login page. */
    private void setupBackButton() {
        backBtn.setOnAction(e -> navigate("login.fxml"));
    }


    // ----------------------------------------------------------
    // POPUP SHORTCUTS
    // ----------------------------------------------------------

    private void showError(String msg) {
        showPopup(
                Alert.AlertType.ERROR,
                "Password Reset Failed",
                "Error",
                msg
        );
    }

    private void showInfo(String msg) {
        showPopup(
                Alert.AlertType.INFORMATION,
                "Request Received",
                "Success",
                msg
        );
    }
}
