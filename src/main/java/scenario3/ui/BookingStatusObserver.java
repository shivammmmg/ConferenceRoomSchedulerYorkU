package scenario3.ui;

import javafx.application.Platform;
import shared.observer.Observer;
import scenario2.viewfx.BookingFX;

/**
 * BookingStatusObserver – Scenario 3 (Check-In & Usage Monitoring)
 * ------------------------------------------------------------------
 * <p>This class is a <b>Concrete Observer</b> in the Observer Design Pattern.
 * It listens for notifications from the RoomStatusManager (Subject) whenever
 * booking/room data changes — such as:</p>
 *
 * <ul>
 *     <li>User checks in to a booked room</li>
 *     <li>No-show logic triggers booking cancellation</li>
 *     <li>Room occupancy or countdown timers change</li>
 *     <li>Background refresh events occur (auto-update)</li>
 * </ul>
 *
 * <h2>Purpose</h2>
 * <p>Ensures that the <b>My Bookings</b> table inside {@link BookingFX}
 * always reflects the latest state without requiring manual refresh from users.</p>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Observer Pattern</b> (Scenario 3): This class is the ConcreteObserver.</li>
 *     <li>RoomStatusManager is the Subject that calls update() on every change.</li>
 *     <li>Updates are pushed safely to JavaFX using {@code Platform.runLater()}.</li>
 * </ul>
 *
 * <h2>Why Needed?</h2>
 * <ul>
 *     <li>Makes UI responsive to backend changes in real time.</li>
 *     <li>Prevents stale booking data during active check-in cycles.</li>
 *     <li>Supports auto-refresh functionality required by Scenario 3.</li>
 * </ul>
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
