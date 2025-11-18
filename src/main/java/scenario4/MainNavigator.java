package scenario4;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainNavigator {

    private static Stage mainStage;

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void loadPage(String fxmlPath) throws Exception {
        Parent root = FXMLLoader.load(MainNavigator.class.getResource(fxmlPath));
        mainStage.setScene(new Scene(root, 900, 600));
        mainStage.show();
    }
}
