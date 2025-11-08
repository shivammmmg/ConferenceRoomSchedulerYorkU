package Scenario1.viewfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginFX extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Login Screen");
        Label lbl = new Label("This will be the LoginFX screen.");
        Scene scene = new Scene(lbl, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
