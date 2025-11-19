package scenario3.controller;

import shared.model.Booking;
import scenario3.model.CheckInBookingWrapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * CheckInManager: handles bookings and no-show scheduling
 */
public class CheckInManager {

    private final RoomStatusManager manager = RoomStatusManager.getInstance();
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    private final long NO_SHOW_MINUTES = 30;  // (can shorten for testing)
    private final ConcurrentHashMap<String, ScheduledFuture<?>> noShowTasks =
            new ConcurrentHashMap<>();

    /**
     * Creates a new booking and schedules a no-show check.
     */
    public String createBooking(String roomId, String userId,
                                LocalDateTime start, LocalDateTime end) {

        String bookingId = "BKG-" + UUID.randomUUID().toString().substring(0, 8);

        // Create actual booking (shared model)
        Booking b = new Booking(
                bookingId,
                roomId,
                userId,
                start,
                end,
                "No purpose specified" // default, not important for scenario 3
        );

        // Store as wrapper inside manager
        manager.addBooking(b);

        // Calculate no-show delay
        long delaySeconds =
                Duration.between(LocalDateTime.now(),
                        start.plusMinutes(NO_SHOW_MINUTES)).getSeconds();

        if (delaySeconds < 0) delaySeconds = 5; // For testing

        // Schedule task to detect no-show
        ScheduledFuture<?> future = scheduler.schedule(() -> {

            CheckInBookingWrapper wrap = manager.getBooking(bookingId);

            if (wrap != null && !wrap.isCheckedIn()) {
                manager.markNoShow(bookingId);
            }

        }, delaySeconds, TimeUnit.SECONDS);

        noShowTasks.put(bookingId, future);

        return bookingId;
    }

    /**
     * Attempts a check-in for the given booking.
     */
    public boolean checkIn(String bookingId, String userId) {

        CheckInBookingWrapper wrap = manager.getBooking(bookingId);

        // Validate wrapper + user
        if (wrap == null) return false;
        Booking b = wrap.getBooking();
        if (!b.getUserId().equals(userId)) return false;

        // Cancel scheduled no-show detection
        ScheduledFuture<?> f = noShowTasks.remove(bookingId);
        if (f != null) f.cancel(false);

        // Mark wrapper state
        wrap.setCheckedIn(true);

        // Inform manager to update room status
        manager.markCheckedIn(bookingId);

        return true;
    }

    public void shutdown() { scheduler.shutdownNow(); }

    public RoomStatusManager getRoomManager() { return manager; }
}
