package scenario1.controller;

import shared.model.User;
import scenario1.controller.UserManager;
import scenario1.controller.NavigationHelper;
import shared.util.GlobalNavigationHelper;
import shared.model.UserType;
import shared.model.SystemUser;

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

import scenario4.AdminFX;

public class LoginController implements Initializable {

    @FXML private StackPane root;
    @FXML private ImageView backgroundImage;
    @FXML private Rectangle darkOverlay;
    @FXML private ImageView logoImage;

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;

    @FXML private Button showPassBtn;
    @FXML private Button loginBtn;
    @FXML private Button forgotBtn;
    @FXML private Button registerBtn;

    private final ContextMenu emailSuggestions = new ContextMenu();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBackgroundEffects();
        setupLogoAnimation();
        setupEmailSuggestions();
        setupPasswordToggle();
        setupLoginButton();
        setupForgotButton();
        setupRegisterButton();

        root.setOpacity(0);

        javafx.application.Platform.runLater(() -> {
            FadeTransition fade = new FadeTransition(Duration.millis(900), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        });
    }

    private void setupBackgroundEffects() {
        backgroundImage.setEffect(new GaussianBlur(10));
        darkOverlay.widthProperty().bind(backgroundImage.fitWidthProperty());
        darkOverlay.heightProperty().bind(backgroundImage.fitHeightProperty());
    }

    private void setupLogoAnimation() {
        logoImage.setTranslateY(-500);
        logoImage.setOpacity(0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(2000), logoImage);
        slide.setFromY(-500);
        slide.setToY(0);

        FadeTransition fade = new FadeTransition(Duration.millis(300), logoImage);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition bounce = new TranslateTransition(Duration.millis(250), logoImage);
        bounce.setFromY(0);
        bounce.setToY(-20);
        bounce.setAutoReverse(true);
        bounce.setCycleCount(2);

        slide.setOnFinished(e -> bounce.play());

        fade.play();
        slide.play();
    }

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

        emailField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) emailSuggestions.hide();
        });
    }

    private void setupPasswordToggle() {

        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        showPassBtn.setOnAction(e -> {
            boolean showing = visiblePasswordField.isVisible();

            visiblePasswordField.setVisible(!showing);
            visiblePasswordField.setManaged(!showing);

            passwordField.setVisible(showing);
            passwordField.setManaged(showing);

            showPassBtn.setText(showing ? "👁" : "🙈");
        });
    }

    private void setupLoginButton() {
        loginBtn.setOnAction(e -> {

            String email = emailField.getText().trim();
            String pass = passwordField.getText().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                showError("Please fill in all fields.");
                return;
            }

            if (!UserManager.getInstance().checkIfEmailRegistered(email)) {
                showError("No account found with this email.\nCreate an account first.");
                return;
            }

            User logged = UserManager.getInstance().login(email, pass);

            if (logged == null) {
                showError("Incorrect password.");
                return;
            }

            SystemUser sysUser = (SystemUser) logged;
            UserType type = sysUser.getType();

            switch (type) {

                case STUDENT:
                case FACULTY:
                case STAFF:
                case PARTNER:

                    scenario2.viewfx.BookingFX app = new scenario2.viewfx.BookingFX();
                    app.setLoggedInUser(logged.getEmail(), type.name());
                    app.start(new Stage());
                    break;

                // =========================================================
                // 🔥 ADMIN + CHIEF → Scenario 4 Admin Dashboard
                // =========================================================
                case ADMIN:
                case CHIEF_EVENT_COORDINATOR:

                    AdminFX adminApp = new AdminFX();
                    adminApp.setRole(type.name());   // 🔥 Pass role to Scenario 4
                    adminApp.start(new Stage());
                    break;

                default:
                    showError("Unknown user type: " + type);
            }
        });
    }


    private void setupForgotButton() {
        forgotBtn.setOnAction(e ->
                NavigationHelper.navigate((Stage) forgotBtn.getScene().getWindow(), "forgot_password.fxml"));
    }

    private void setupRegisterButton() {
        registerBtn.setOnAction(e ->
                NavigationHelper.navigate((Stage) registerBtn.getScene().getWindow(), "register.fxml"));
    }

    @FXML
    private void goBack() {
        GlobalNavigationHelper.navigateTo("/mainpage/MainPage.fxml");
    }

    private void showError(String message) {
        Stage stage = (Stage) root.getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.initOwner(stage);

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
