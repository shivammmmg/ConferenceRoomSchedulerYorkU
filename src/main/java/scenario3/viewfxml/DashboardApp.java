package scenario3.viewfxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        boolean admin = true;  // <-- switch between admin/user dashboard

        String fxml = admin ?
                "/scenario3/viewfxml/fxml/admin_dashboard.fxml" :
                "/scenario3/viewfxml/fxml/user_dashboard.fxml";

        Parent root = FXMLLoader.load(getClass().getResource(fxml));

        Scene scene = new Scene(root, 1100, 700);

        primaryStage.setTitle(admin ? "Admin Dashboard" : "User Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}