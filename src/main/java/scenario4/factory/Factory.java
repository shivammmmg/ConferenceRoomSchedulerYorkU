package scenario4.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Factory – Scenario 4 (UI Page Factory)
 * --------------------------------------------------------------------------
 * <p>This class implements a simple <b>Factory pattern</b> used to create and
 * load the different FXML pages for Scenario 4.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Centralizes FXML loading logic</li>
 *     <li>Reduces duplication across controllers</li>
 *     <li>Ensures consistent stage switching and scene creation</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Factory Method Pattern</b> – Abstracts "how" pages are created.</li>
 *     <li>Controllers simply call methods like loadBookingPage(), without
 *         knowing any FXML paths or loader details.</li>
 *     <li>Keeps Scenario 4 navigation modular and testable.</li>
 * </ul>
 *
 * <h2>Used In</h2>
 * <ul>
 *     <li>Scenario 4 Admin pages</li>
 *     <li>Standalone testing of booking/confirmation/payment screens</li>
 * </ul>
 */


public class Factory {

    /**
     * Loads an FXML file located under /scenario4/.
     *
     * @param fileName name of the FXML file (e.g., "booking.fxml")
     * @return the loaded UI root node
     * @throws Exception if the FXML cannot be found or parsed
     *
     * <p>This private helper centralizes the FXML loading process so that all
     * factory methods rely on a single, clean FXML loader path.</p>
     */

    private Parent loadFXML(String fileName) throws Exception {
        return FXMLLoader.load(
                Factory.class.getResource("/scenario4/" + fileName)
        );
    }

    /**
     * Loads the Booking page (booking.fxml) into the provided Stage.
     *
     * <p>Creates a new Scene using the loaded FXML and makes it visible on the
     * given Stage. Used by controllers that need to display the booking screen.</p>
     *
     * @param stage JavaFX Stage to render the page on
     * @throws Exception if loading booking.fxml fails
     */

    public void loadBookingPage(Stage stage) throws Exception {
        Parent root = loadFXML("booking.fxml");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Loads the Confirmation page (confirmation.fxml) into the given Stage.
     *
     * <p>Ensures consistent page switching within Scenario 4.</p>
     *
     * @param stage the stage to display the scene on
     * @throws Exception if confirmation.fxml cannot be loaded
     */

    public void loadConfirmationPage(Stage stage) throws Exception {
        Parent root = loadFXML("confirmation.fxml");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Loads the Payment page (payment.fxml) and displays it inside the provided Stage.
     *
     * <p>Encapsulates navigation for the payment flow in Scenario 4.</p>
     *
     * @param stage the stage on which the Payment UI will be displayed
     * @throws Exception if payment.fxml fails to load
     */


    public void loadPaymentPage(Stage stage) throws Exception {
        Parent root = loadFXML("payment.fxml");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
