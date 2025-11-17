package scenario1.controller;


import shared.model.User;
import scenario1.controller.UserManager;
import scenario1.controller.NavigationHelper;
import shared.util.GlobalNavigationHelper;

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

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Login screen.
 *
 * Responsibilities:
 *  - Handle login validation
 *  - Animate the UI (logo drop, fade-in)
 *  - Provide email auto-completion suggestions
 *  - Toggle password visibility (eye icon)
 *  - Navigate to Register or Forgot Password screens
 *
 * Scenario 1 – Registration & Account Management
 */
public class LoginController implements Initializable {

    // ------------------------------
    // FXML UI COMPONENTS
    // ------------------------------
    @FXML private StackPane root;                // Root container for fade-in animation
    @FXML private ImageView backgroundImage;     // Blurred background image
    @FXML private Rectangle darkOverlay;         // Dark overlay for better contrast
    @FXML private ImageView logoImage;           // YorkU logo with slide + bounce animation

    @FXML private TextField emailField;          // Email input
    @FXML private PasswordField passwordField;   // Hidden password field
    @FXML private TextField visiblePasswordField;// Visible password replacement

    @FXML private Button showPassBtn;            // Eye icon button
    @FXML private Button loginBtn;               // Login button
    @FXML private Button forgotBtn;              // Forgot Password navigation
    @FXML private Button registerBtn;            // Register navigation

    // Email auto-completion context menu
    private final ContextMenu emailSuggestions = new ContextMenu();


    // ----------------------------------------------------------
    // INITIALIZATION
    // ----------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBackgroundEffects();     // Blur + overlay binding
        setupLogoAnimation();         // Slide + bounce animation
        setupEmailSuggestions();      // Email domain suggestions
        setupPasswordToggle();        // Eye icon logic
        setupLoginButton();           // Login validation + model interaction
        setupForgotButton();          // Navigation to Forgot Password
        setupRegisterButton();        // Navigation to Register

        // Screen fade-in for cleaner UI transition
        root.setOpacity(0);
        javafx.application.Platform.runLater(() -> {
            FadeTransition fade = new FadeTransition(Duration.millis(900), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        });
    }


    // ----------------------------------------------------------
    // BACKGROUND (BLUR + OVERLAY)
    // ----------------------------------------------------------
    private void setupBackgroundEffects() {
        backgroundImage.setEffect(new GaussianBlur(10));

        // Bind overlay dimensions so it always covers the scene
        darkOverlay.widthProperty().bind(backgroundImage.fitWidthProperty());
        darkOverlay.heightProperty().bind(backgroundImage.fitHeightProperty());
    }


    // ----------------------------------------------------------
    // LOGO ANIMATION (DROP + BOUNCE)
    // ----------------------------------------------------------
    private void setupLogoAnimation() {
        logoImage.setTranslateY(-500);
        logoImage.setOpacity(0);

        // Smooth drop animation
        TranslateTransition slide = new TranslateTransition(Duration.millis(2000), logoImage);
        slide.setFromY(-500);
        slide.setToY(0);

        // Fade-in during drop
        FadeTransition fade = new FadeTransition(Duration.millis(300), logoImage);
        fade.setFromValue(0);
        fade.setToValue(1);

        // Small bounce effect at the end
        TranslateTransition bounce = new TranslateTransition(Duration.millis(250), logoImage);
        bounce.setFromY(0);
        bounce.setToY(-20);
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
     * Suggests domain completions when the user types something like "name@".
     * Works similarly to email apps.
     */
    private void setupEmailSuggestions() {

        List<String> domains = Arrays.asList("@my.yorku.ca", "@yorku.ca", "@gmail.com");

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            emailSuggestions.getItems().clear();

            int atIndex = newVal.indexOf("@");
            if (atIndex == -1) {
                emailSuggestions.hide();
                return;
            }

            String before = newVal.substring(0, atIndex);

            for (String d : domains) {
                MenuItem item = new MenuItem(before + d);
                item.setOnAction(e -> emailField.setText(item.getText()));
                emailSuggestions.getItems().add(item);
            }

            emailSuggestions.show(emailField, Side.BOTTOM, 0, 0);
        });

        // Hide suggestions when user clicks away
        emailField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) emailSuggestions.hide();
        });
    }


    // ----------------------------------------------------------
    // PASSWORD SHOW / HIDE (EYE ICON)
    // ----------------------------------------------------------
    private void setupPasswordToggle() {

        // Keep text synced between visible and hidden password fields
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        showPassBtn.setOnAction(e -> {
            boolean showing = visiblePasswordField.isVisible();

            // Toggle which password field is visible
            visiblePasswordField.setVisible(!showing);
            visiblePasswordField.setManaged(!showing);

            passwordField.setVisible(showing);
            passwordField.setManaged(showing);

            // Update icon
            showPassBtn.setText(showing ? "👁" : "🙈");
        });
    }


    // ----------------------------------------------------------
    // LOGIN VALIDATION LOGIC
    // ----------------------------------------------------------
    private void setupLoginButton() {
        loginBtn.setOnAction(e -> {

            String email = emailField.getText().trim();
            String pass = passwordField.getText().trim();

            // Basic field validation
            if (email.isEmpty() || pass.isEmpty()) {
                showError("Please fill in all fields.");
                return;
            }

            // Email not registered
            if (!UserManager.getInstance().checkIfEmailRegistered(email)) {
                showError("No account found with this email.\nCreate an account first.");
                return;
            }

            // Attempt login
            User logged = UserManager.getInstance().login(email, pass);

            if (logged == null) {
                // Password incorrect
                showError("Incorrect password.");
            } else {
                // Successful login
                showInfo("Welcome " + logged.getName() + "! 🎉");

                // TODO: Navigate to Dashboard (Scenario 2/3/4)
            }
        });
    }


    // ----------------------------------------------------------
    // NAVIGATION BUTTONS
    // ----------------------------------------------------------
    /** Navigates to Forgot Password screen. */
    private void setupForgotButton() {
        forgotBtn.setOnAction(e ->
                NavigationHelper.navigate((Stage) forgotBtn.getScene().getWindow(), "forgot_password.fxml"));
    }

    /** Navigates to Registration screen. */
    private void setupRegisterButton() {
        registerBtn.setOnAction(e ->
                NavigationHelper.navigate((Stage) registerBtn.getScene().getWindow(), "register.fxml"));
    }

    @FXML
    private void goBack() {
        GlobalNavigationHelper.navigateTo("/mainpage/MainPage.fxml");
    }



    // ----------------------------------------------------------
    // POPUP HELPERS
    // ----------------------------------------------------------
    /**
     * Shows an error popup dialog.
     */
    private void showError(String message) {
        Stage stage = (Stage) root.getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.initOwner(stage);

        // FIX: Leave fullscreen temporarily so JavaFX can show the alert
        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
    }

    private void showInfo(String message) {
        Stage stage = (Stage) root.getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Login Successful");
        alert.setContentText(message);
        alert.initOwner(stage);

        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
    }
}
