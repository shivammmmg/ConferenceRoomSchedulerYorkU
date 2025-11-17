package mainpage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Load Inter font properly
        Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-Regular.ttf"), 14);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/viewfx/MainPage.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("YorkU Room Scheduler");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
