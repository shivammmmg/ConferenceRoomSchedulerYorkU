package scenario3;

import shared.model.Booking;

/**
 * SensorSystem – Scenario 3 (Check-In & Usage Monitoring)
 * ------------------------------------------------------------------------
 * This class simulates **real-world sensor activity** for Scenario 3.
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Acts as the *hardware layer* of the system (badge scanners, PIR sensors).</li>
 *     <li>Provides high-level sensor events to the business logic layer.</li>
 *     <li>Delegates all booking/state updates to {@link RoomStatusManager}.</li>
 * </ul>
 *
 * <h2>What It Simulates</h2>
 * <ul>
 *     <li><b>User check-in</b> (badge scan → “Check In” button)</li>
 *     <li><b>Room occupancy sensors</b> (detect presence / vacancy)</li>
 *     <li><b>No-show countdown start</b> for new bookings</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Singleton</b> – Only one sensor system exists for the whole application.</li>
 *     <li>Works with Observer Pattern through RoomStatusManager (Subject → Observers).</li>
 * </ul>
 *
 * <h2>Related Scenario</h2>
 * <ul>
 *     <li><b>Scenario 3</b> – Check-In, No-Show detection, and live room monitoring.</li>
 * </ul>
 *
 * <h2>Note</h2>
 * This class does **not** manage booking logic itself — it only triggers sensor-like
 * events. All logic (IN_USE, NO_SHOW, AVAILABLE) is handled inside RoomStatusManager.
 */
public class SensorSystem {

    private static SensorSystem instance;

    private final RoomStatusManager statusManager = RoomStatusManager.getInstance();

    private SensorSystem() { }

    public static synchronized SensorSystem getInstance() {
        if (instance == null) {
            instance = new SensorSystem();
        }
        return instance;
    }

    // ======================================================
    // Booking lifecycle hooks
    // ======================================================

    /**
     * Called when a new booking is created and confirmed.
     * For now, you can call this from BookingFX right after bookRoom()
     * to start the no-show countdown.
     */
    public void registerNewBooking(Booking booking) {
        if (booking == null) return;

        statusManager.startNoShowCountdown(
                booking.getBookingId(),
                booking.getRoomId(),
                booking.getStartTime()
        );
    }

    // ======================================================
    // Check-in simulation
    // ======================================================

    /**
     * Called when the user presses "Check In" in MyBookings.
     * (In a real system, this would be a badge scan event.)
     */
    public void simulateUserCheckIn(Booking booking) {
        if (booking == null) return;

        statusManager.registerCheckIn(
                booking.getBookingId(),
                booking.getRoomId()
        );
    }

    // ======================================================
    // Occupancy sensor simulation
    // ======================================================

    /**
     * Simulates an occupancy sensor update for a room.
     *
     * @param roomId   room identifier
     * @param occupied true if the sensor detects presence
     */
    public void simulateOccupancyChange(String roomId, boolean occupied) {
        statusManager.updateOccupancy(roomId, occupied);
    }

    /**
     * Convenience method: room becomes empty.
     */
    public void simulateRoomVacant(String roomId) {
        statusManager.markRoomVacant(roomId);
    }
}
