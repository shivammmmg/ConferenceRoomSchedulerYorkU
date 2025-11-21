package scenario3;

import shared.model.Booking;

/**
 * SensorSystem (Singleton)
 * --------------------------------------------------------
 * Simulates:
 *   - badge scans / user check-in
 *   - occupancy sensors (room empty / in use)
 *   - starting no-show timers for new bookings
 *
 * It delegates all state management to RoomStatusManager.
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
