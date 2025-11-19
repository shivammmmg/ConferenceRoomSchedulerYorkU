package scenario1.controller;

import scenario1.controller.NavigationHelper;
import scenario1.controller.UserManager;

import javafx.stage.Stage;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * RegisterController
 * -----------------------------------------------------------
 * Handles ALL UI logic for the Registration Screen.
 *
 * Features:
 *  - Animated background + logo drop/bounce
 *  - Email auto-suggestions (e.g., @my.yorku.ca)
 *  - Password visibility toggle (eye button)
 *  - Password strength indicator with color-coded bar
 *  - Dynamic Student ID visibility based on selected role
 *  - Full validation through UserManager
 *  - Navigation to Login screen
 *
 * This controller directly supports Scenario 1 requirements.
 */
public class RegisterController implements Initializable {

    // -----------------------------
    // UI COMPONENTS (injected from FXML)
    // -----------------------------
    @FXML private ImageView backgroundImage;
    @FXML private Rectangle darkOverlay;
    @FXML private VBox formCard;
    @FXML private ImageView logoImage;

    // User input fields
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;  // used when password is revealed
    @FXML private ProgressBar strengthBar;
    @FXML private Label strengthLabel;

    // User type dropdown
    @FXML private ComboBox<String> typeBox;

    // Student fields (shown only when type = Student)
    @FXML private Label studentIdLbl;
    @FXML private TextField studentIdField;

    // Buttons
    @FXML private Button registerBtn;
    @FXML private Button backBtn;
    @FXML private Button showPassBtn;

    // Root container used for popup ownership
    @FXML private StackPane root;

    // Auto-suggest menu for email domains
    private final ContextMenu emailSuggestions = new ContextMenu();


    /**
     * Called automatically when register.fxml loads.
     * Initializes all UI behaviors and event listeners.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBackgroundEffects();
        setupLogoAnimation();
        setupEmailSuggestions();
        setupUserTypeLogic();
        setupPasswordStrengthListener();
        setupPasswordToggle();
        setupRegisterButton();
        setupBackButton();
    }

    // ---------------------------------------------------------------------
    // UI: Background Blur & Dark Overlay Sync (keeps overlay same size)
    // ---------------------------------------------------------------------
    private void setupBackgroundEffects() {

        // Apply blur to background for a premium UI feel
        GaussianBlur blur = new GaussianBlur(11);
        backgroundImage.setEffect(blur);

        // Ensure overlay always matches the image size
        darkOverlay.widthProperty().bind(backgroundImage.fitWidthProperty());
        darkOverlay.heightProperty().bind(backgroundImage.fitHeightProperty());
    }

    // ---------------------------------------------------------------------
    // UI: Logo Slide Down + Bounce Animation (YorkU logo entrance)
    // ---------------------------------------------------------------------
    private void setupLogoAnimation() {

        // Start above screen (hidden)
        logoImage.setTranslateY(-700);
        logoImage.setOpacity(1); // no fade, only movement animation

        // Launcher slide animation
        TranslateTransition slide = new TranslateTransition(Duration.millis(2500), logoImage);
        slide.setFromY(-700);
        slide.setToY(0);
        slide.setInterpolator(javafx.animation.Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

        // Small bounce after landing
        TranslateTransition bounce = new TranslateTransition(Duration.millis(300), logoImage);
        bounce.setFromY(0);
        bounce.setToY(-20);
        bounce.setCycleCount(2);
        bounce.setAutoReverse(true);

        slide.setOnFinished(e -> bounce.play());
        slide.play();
    }

    // ---------------------------------------------------------------------
    // Feature: Email domain auto-completion (suggestions)
    // ---------------------------------------------------------------------
    private void setupEmailSuggestions() {

        // Suggested popular domains
        List<String> domains = Arrays.asList(
                "@my.yorku.ca", "@yorku.ca", "@gmail.com",
                "@yahoo.com", "@hotmail.com", "@outlook.com"
        );

        // Trigger suggestions when user types
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            emailSuggestions.getItems().clear();

            int at = newVal.indexOf('@');

            // Only show suggestions when '@' exists
            if (at == -1) {
                emailSuggestions.hide();
                return;
            }

            String before = newVal.substring(0, at);

            // Create menu items
            for (String domain : domains) {
                MenuItem item = new MenuItem(before + domain);
                item.setOnAction(e -> emailField.setText(item.getText()));
                emailSuggestions.getItems().add(item);
            }

            emailSuggestions.show(emailField, Side.BOTTOM, 0, 0);
        });

        // Hide menu when field loses focus
        emailField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) emailSuggestions.hide();
        });
    }

    // ---------------------------------------------------------------------
    // Feature: Show / Hide password (eye toggle button)
    // ---------------------------------------------------------------------
    private void setupPasswordToggle() {

        // Both fields always share the same text
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        showPassBtn.setOnAction(e -> {
            boolean showing = visiblePasswordField.isVisible();

            if (showing) {
                // Switch back to hidden mode
                passwordField.setVisible(true);
                passwordField.setManaged(true);

                visiblePasswordField.setVisible(false);
                visiblePasswordField.setManaged(false);

                showPassBtn.setText("👁");
            } else {
                // Switch to visible mode
                visiblePasswordField.setVisible(true);
                visiblePasswordField.setManaged(true);

                passwordField.setVisible(false);
                passwordField.setManaged(false);

                showPassBtn.setText("🙈");
            }
        });
    }

    // ---------------------------------------------------------------------
    // Feature: Dynamic Student ID visibility based on User Type
    // ---------------------------------------------------------------------
    private void setupUserTypeLogic() {

        typeBox.getItems().addAll("Student", "Faculty", "Staff", "Partner");

        typeBox.setOnAction(e -> {
            String type = typeBox.getValue();
            boolean isStudent = "Student".equals(type);

            // Only display Student ID for students
            studentIdLbl.setVisible(isStudent);
            studentIdField.setVisible(isStudent);

            // Adjust email hint depending on type
            if (isStudent)
                emailField.setPromptText("e.g., name@my.yorku.ca");
            else if ("Faculty".equals(type) || "Staff".equals(type))
                emailField.setPromptText("e.g., name@yorku.ca");
            else
                emailField.setPromptText("e.g., name@domain.com");
        });
    }

    // ---------------------------------------------------------------------
    // Feature: Password Strength Meter (weak/fair/strong)
    // ---------------------------------------------------------------------
    private void setupPasswordStrengthListener() {
        passwordField.textProperty().addListener((obs, old, val) -> updatePasswordStrength(val));
    }

    private void updatePasswordStrength(String password) {

        double strength = 0;

        // Scoring rules (25% per rule satisfied)
        if (password.length() >= 8) strength += 0.25;
        if (Pattern.compile("[A-Z]").matcher(password).find()) strength += 0.25;
        if (Pattern.compile("[0-9]").matcher(password).find()) strength += 0.25;
        if (Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) strength += 0.25;

        strengthBar.setProgress(strength);

        // Remove previous style
        strengthBar.getStyleClass().removeAll("weak-bar", "fair-bar", "strong-bar");

        // Color + label update
        if (strength < 0.4) {
            strengthLabel.setText("Strength: Weak");
            strengthLabel.setStyle("-fx-text-fill: #d9534f;");
            strengthBar.getStyleClass().add("weak-bar");
        }
        else if (strength < 0.8) {
            strengthLabel.setText("Strength: Fair");
            strengthLabel.setStyle("-fx-text-fill: #f0ad4e;");
            strengthBar.getStyleClass().add("fair-bar");
        }
        else {
            strengthLabel.setText("Strength: Strong");
            strengthLabel.setStyle("-fx-text-fill: #5cb85c;");
            strengthBar.getStyleClass().add("strong-bar");
        }
    }

    // ---------------------------------------------------------------------
    // REGISTER BUTTON → Validates & Creates User
    // ---------------------------------------------------------------------
    private void setupRegisterButton() {

        registerBtn.setOnAction(e -> {
            try {
                // Attempt registration via UserManager
                boolean success = UserManager.getInstance().register(
                        nameField.getText(),
                        emailField.getText(),
                        passwordField.getText(),
                        typeBox.getValue(),
                        studentIdField.getText()
                );

                // Success
                if (success) {
                    showInfoDialog("Registration Successful!");
                    clearFields();
                }

            } catch (Exception ex) {
                showErrorDialog(ex.getMessage());
            }
        });
    }

    /**
     * Reset all fields back to default state after successful registration.
     */
    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        strengthBar.setProgress(0);
        strengthLabel.setText("Strength: Weak");
        typeBox.setValue(null);
        studentIdField.clear();
        studentIdLbl.setVisible(false);
        studentIdField.setVisible(false);
    }

    // ---------------------------------------------------------------------
    // BACK BUTTON → Navigate back to Login
    // ---------------------------------------------------------------------
    private void setupBackButton() {
        backBtn.setOnAction(e ->
                NavigationHelper.navigate((Stage) backBtn.getScene().getWindow(), "login.fxml")
        );
    }

    // ---------------------------------------------------------------------
    // Generic Popup Methods (error / info)
    // ---------------------------------------------------------------------
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Failed");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.initOwner(root.getScene().getWindow());
        alert.showAndWait();
    }

    private void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Registration Completed");
        alert.setContentText(message);
        alert.initOwner(root.getScene().getWindow());
        alert.showAndWait();
    }
}
