package scenario4.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Factory {

    private Parent loadFXML(String fileName) throws Exception {
        return FXMLLoader.load(
                Factory.class.getResource("/scenario4/" + fileName)
        );
    }

    public void loadBookingPage(Stage stage) throws Exception {
        Parent root = loadFXML("booking.fxml");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void loadConfirmationPage(Stage stage) throws Exception {
        Parent root = loadFXML("confirmation.fxml");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void loadPaymentPage(Stage stage) throws Exception {
        Parent root = loadFXML("payment.fxml");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
