package Scenario1.viewfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import Scenario1.controller.UserManager;

public class RegisterFX extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("YorkU Conference Scheduler - Register");

        // --- Root Layout ---
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30, 50, 30, 50));

        // --- Title ---
        Label title = new Label("Create New Account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // --- Form Layout ---
        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);
        form.setAlignment(Pos.CENTER);


        Label nameLbl = new Label("Full Name:");
        TextField nameTxt = new TextField();
        nameTxt.setPromptText("Full Name");
        nameTxt.setPrefWidth(280);

        Label emailLbl = new Label("Email:");
        TextField emailTxt = new TextField();
        emailTxt.setPromptText("Email (e.g., name@my.yorku.ca)");

        Label passLbl = new Label("Password:");
        PasswordField passTxt = new PasswordField();
        passTxt.setPromptText("Enter your password");

        Label hintLbl = new Label("Password must contain uppercase, lowercase, number, special char, and 8+ chars.");
        hintLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");

        Label typeLbl = new Label("User Type:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Student", "Faculty", "Staff", "Partner");
        typeBox.setPrefWidth(280);

        form.add(nameLbl, 0, 0);
        form.add(nameTxt, 1, 0);
        form.add(emailLbl, 0, 1);
        form.add(emailTxt, 1, 1);
        form.add(passLbl, 0, 2);
        form.add(passTxt, 1, 2);
        form.add(hintLbl, 1, 3);
        form.add(typeLbl, 0, 4);
        form.add(typeBox, 1, 4);

        form.getColumnConstraints().addAll(
                new ColumnConstraints(120),  // first column (labels)
                new ColumnConstraints(280)   // second column (fields)
        );


        // --- Buttons ---
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);

        Button registerBtn = new Button("Register");
        Button backBtn = new Button("Back to Login");

        registerBtn.setStyle("-fx-background-color: #1E90FF; -fx-text-fill: white; -fx-font-weight: bold;");
        backBtn.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");

        registerBtn.setOnAction(e -> {
            try {
                boolean success = UserManager.getInstance().register(
                        nameTxt.getText(),
                        emailTxt.getText(),
                        passTxt.getText(),
                        typeBox.getValue()
                );

                if (success) {
                    new Alert(Alert.AlertType.INFORMATION, "Registration successful!").showAndWait();
                    nameTxt.clear();
                    emailTxt.clear();
                    passTxt.clear();
                    typeBox.setValue(null);
                }

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        backBtn.setOnAction(e -> {
            try {
                new LoginFX().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttons.getChildren().addAll(registerBtn, backBtn);

        // --- Assemble Layout ---
        root.getChildren().addAll(title, form, buttons);

        Scene scene = new Scene(root, 450, 500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
