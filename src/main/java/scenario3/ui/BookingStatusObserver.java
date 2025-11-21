package scenario3.ui;

import javafx.application.Platform;
import shared.observer.Observer;
import scenario2.viewfx.BookingFX;

/**
 * BookingStatusObserver (ConcreteObserver)
 * ------------------------------------------------------------
 * This observer listens to room/booking updates from RoomStatusManager.
 * Whenever something changes (check-in, no-show, occupancy change),
 * RoomStatusManager.notifyObservers() triggers update(),
 * which refreshes the MyBookings UI table.
 */
public class BookingStatusObserver implements Observer {

    private final BookingFX controller;

    /**
     * Binds this observer to a specific BookingFX UI controller.
     */
    public BookingStatusObserver(BookingFX controller) {
        this.controller = controller;
    }

    @Override
    public void update() {
        // Always refresh UI on JavaFX Application Thread
        Platform.runLater(() -> {
            try {
                controller.refreshMyBookingsView();
            } catch (Exception e) {
                System.out.println("[BookingStatusObserver] UI refresh failed: " + e.getMessage());
            }
        });
    }
}
