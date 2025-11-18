package scenario3.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.*;
import shared.model.Booking;

/**
 * CheckInManager: handles bookings and no-show scheduling
 */
public class CheckInManager {

    private final RoomStatusManager manager = RoomStatusManager.getInstance();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private final long NO_SHOW_MINUTES = 30;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> noShowTasks = new ConcurrentHashMap<>();

    public String createBooking(String roomId, String userId, LocalDateTime start, LocalDateTime end) {
        String bookingId = "BKG-" + UUID.randomUUID().toString().substring(0, 8);
        Booking b = new Booking(bookingId, roomId, userId, start, end);
        manager.addBooking(b);

        long delaySeconds = Duration.between(LocalDateTime.now(), start.plusMinutes(NO_SHOW_MINUTES)).getSeconds();
        if (delaySeconds < 0) delaySeconds = 5; // small delay if past start

        ScheduledFuture<?> f = scheduler.schedule(() -> {
            Booking latest = manager.getBooking(bookingId);
            if (latest != null && !latest.isCheckedIn()) {
                manager.markNoShow(bookingId);
            }
        }, delaySeconds, TimeUnit.SECONDS);

        noShowTasks.put(bookingId, f);
        return bookingId;
    }

    public boolean checkIn(String bookingId, String userId) {
        Booking b = manager.getBooking(bookingId);
        if (b == null || !b.getUserId().equals(userId)) return false;

        manager.markCheckedIn(bookingId);
        ScheduledFuture<?> f = noShowTasks.remove(bookingId);
        if (f != null) f.cancel(true);
        return true;
    }

    public void shutdown() { scheduler.shutdownNow(); }
}
