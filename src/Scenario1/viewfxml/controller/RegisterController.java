package Scenario1.viewfxml.controller;

import Scenario1.controller.UserManager;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;


import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {

    // FROM FXML (background + overlay + form)
    @FXML private ImageView backgroundImage;
    @FXML private Rectangle darkOverlay;
    @FXML private VBox formCard;

    // INPUT FIELDS
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;
    @FXML private ProgressBar strengthBar;
    @FXML private Label strengthLabel;

    // USER TYPE
    @FXML private ComboBox<String> typeBox;

    // STUDENT ID
    @FXML private Label studentIdLbl;
    @FXML private TextField studentIdField;

    // BUTTONS
    @FXML private Button registerBtn;
    @FXML private Button backBtn;
    @FXML private Button showPassBtn;

    // ROOT container for toast messages
    @FXML private StackPane root;

    private final ContextMenu emailSuggestions = new ContextMenu();

    // ----------------------------------------------------------
    // INITIALIZE
    // ----------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBackgroundEffects();
        setupEmailSuggestions();
        setupUserTypeLogic();
        setupPasswordStrengthListener();
        setupPasswordToggle();
        setupRegisterButton();
        setupBackButton();
    }

    // ----------------------------------------------------------
    // BACKGROUND BLUR + DARK OVERLAY
    // ----------------------------------------------------------
    private void setupBackgroundEffects() {
        // Blur
        GaussianBlur blur = new GaussianBlur(18);
        backgroundImage.setEffect(blur);

        // Dark overlay auto-resize
        darkOverlay.widthProperty().bind(backgroundImage.fitWidthProperty());
        darkOverlay.heightProperty().bind(backgroundImage.fitHeightProperty());

        // Fade-in animation for form
        FadeTransition fadeIn = new FadeTransition(Duration.millis(900), formCard);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    // ----------------------------------------------------------
    // EMAIL SUGGESTIONS
    // ----------------------------------------------------------
    private void setupEmailSuggestions() {
        List<String> domains = Arrays.asList(
                "@my.yorku.ca", "@yorku.ca", "@gmail.com",
                "@yahoo.com", "@hotmail.com", "@outlook.com"
        );

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            emailSuggestions.getItems().clear();

            int at = newVal.indexOf('@');
            if (at == -1) {
                emailSuggestions.hide();
                return;
            }

            String before = newVal.substring(0, at);

            for (String d : domains) {
                MenuItem item = new MenuItem(before + d);
                item.setOnAction(e -> emailField.setText(item.getText()));
                emailSuggestions.getItems().add(item);
            }

            emailSuggestions.show(emailField, Side.BOTTOM, 0, 0);
        });

        emailField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) emailSuggestions.hide();
        });
    }

    // ----------------------------------------------------------
    // SHOW / HIDE PASSWORD TOGGLE
    // ----------------------------------------------------------
    private void setupPasswordToggle() {

        // Sync both fields
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        showPassBtn.setOnAction(e -> {
            boolean showing = visiblePasswordField.isVisible();

            if (showing) {
                // Switch to hidden mode
                passwordField.setVisible(true);
                passwordField.setManaged(true);

                visiblePasswordField.setVisible(false);
                visiblePasswordField.setManaged(false);

                showPassBtn.setText("👁"); // closed eye
            } else {
                // Switch to visible mode
                visiblePasswordField.setVisible(true);
                visiblePasswordField.setManaged(true);

                passwordField.setVisible(false);
                passwordField.setManaged(false);

                showPassBtn.setText("🙈"); // open eye
            }
        });
    }


    // ----------------------------------------------------------
    // USER TYPE LOGIC
    // ----------------------------------------------------------
    private void setupUserTypeLogic() {
        typeBox.getItems().addAll("Student", "Faculty", "Staff", "Partner");

        typeBox.setOnAction(e -> {
            String type = typeBox.getValue();
            boolean isStudent = "Student".equals(type);

            studentIdLbl.setVisible(isStudent);
            studentIdField.setVisible(isStudent);

            if (isStudent)
                emailField.setPromptText("e.g., name@my.yorku.ca");
            else if ("Faculty".equals(type) || "Staff".equals(type))
                emailField.setPromptText("e.g., name@yorku.ca");
            else
                emailField.setPromptText("e.g., name@domain.com");
        });
    }

    // ----------------------------------------------------------
    // PASSWORD STRENGTH
    // ----------------------------------------------------------
    private void setupPasswordStrengthListener() {
        passwordField.textProperty().addListener((obs, old, val) -> updatePasswordStrength(val));
    }

    private void updatePasswordStrength(String password) {
        double strength = 0;

        if (password.length() >= 8) strength += 0.25;
        if (Pattern.compile("[A-Z]").matcher(password).find()) strength += 0.25;
        if (Pattern.compile("[0-9]").matcher(password).find()) strength += 0.25;
        if (Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) strength += 0.25;

        strengthBar.setProgress(strength);

        if (strength < 0.4) {
            strengthLabel.setText("Strength: Weak");
            strengthLabel.setStyle("-fx-text-fill: #d9534f;");
        } else if (strength < 0.8) {
            strengthLabel.setText("Strength: Fair");
            strengthLabel.setStyle("-fx-text-fill: #f0ad4e;");
        } else {
            strengthLabel.setText("Strength: Strong");
            strengthLabel.setStyle("-fx-text-fill: #5cb85c;");
        }
    }

    // ----------------------------------------------------------
    // REGISTER BUTTON
    // ----------------------------------------------------------
    private void setupRegisterButton() {
        registerBtn.setOnAction(e -> {
            try {
                boolean success = UserManager.getInstance().register(
                        nameField.getText(),
                        emailField.getText(),
                        passwordField.getText(),
                        typeBox.getValue(),
                        studentIdField.getText()
                );

                if (success) {
                    showToast("✅ Registration Successful!");
                    clearFields();
                }

            } catch (Exception ex) {
                showToast("❌ " + ex.getMessage());
            }
        });
    }

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

    // ----------------------------------------------------------
    // BACK BUTTON
    // ----------------------------------------------------------
    private void setupBackButton() {
        backBtn.setOnAction(e -> System.out.println("Back to login - implement navigation"));
    }

    // ----------------------------------------------------------
    // TOAST NOTIFICATION
    // ----------------------------------------------------------
    private void showToast(String message) {
        Label toast = new Label(message);
        toast.setStyle(
                "-fx-background-color: #333333; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 8;"
        );
        toast.setOpacity(0);

        root.getChildren().add(toast);
        HBox.setMargin(toast, new Insets(20, 0, 0, 20));

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(0.92);
        fadeIn.play();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), toast);
        fadeOut.setDelay(Duration.seconds(2.5));
        fadeOut.setFromValue(0.92);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(ev -> root.getChildren().remove(toast));
        fadeOut.play();
    }
}
