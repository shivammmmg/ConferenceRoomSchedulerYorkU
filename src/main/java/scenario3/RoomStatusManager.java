package scenario3;

import shared.observer.Subject;
import shared.observer.Observer;

import shared.model.Booking;
import shared.model.Room;
import shared.model.RoomRepository;
import scenario2.controller.BookingManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class RoomStatusManager implements Subject {

    // ======================================================
    // Singleton
    // ======================================================
    private static RoomStatusManager instance;

    public static synchronized RoomStatusManager getInstance() {
        if (instance == null) {
            instance = new RoomStatusManager();
        }
        return instance;
    }

    private RoomStatusManager() { }

    // ======================================================
    // Observer Pattern
    // ======================================================
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public synchronized void attach(Observer o) {
        if (o != null && !observers.contains(o)) observers.add(o);
    }

    @Override
    public synchronized void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public synchronized void notifyObservers() {
        for (Observer o : observers) o.update();
    }

    // ======================================================
    // Repositories
    // ======================================================

    private final RoomRepository roomRepo = RoomRepository.getInstance();

    private final int NO_SHOW_MINUTES = 2; // For demo

    // ======================================================
    // Internal State
    // ======================================================
    private final Map<String, String> roomStatuses = new HashMap<>();
    private final Map<String, Timer> noShowTimers = new HashMap<>();
    private final Set<String> noShowBookings = new HashSet<>();

    // ======================================================
    // Public Getters
    // ======================================================
    public synchronized String getRoomStatus(String roomId) {
        return roomStatuses.getOrDefault(roomId, "AVAILABLE");
    }

    public synchronized boolean isNoShow(String bookingId) {
        return noShowBookings.contains(bookingId);
    }

    // ======================================================
    // CHECK-IN LOGIC
    // ======================================================
    public synchronized void checkIn(String bookingId, String roomId, String userId) throws Exception {

        Booking booking = findBooking(bookingId);
        if (booking == null)
            throw new Exception("Booking not found.");

        if (!booking.getUserId().equals(userId))
            throw new Exception("You can only check into your own booking.");

        if (!"CONFIRMED".equals(booking.getStatus()))
            throw new Exception("Only CONFIRMED bookings can be checked into.");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = booking.getStartTime();

        // Allow check-in from 10 minutes BEFORE start → until start + NO_SHOW_MINUTES
        LocalDateTime earliestCheckIn = start.minusMinutes(10);
        LocalDateTime latestCheckIn   = start.plusMinutes(NO_SHOW_MINUTES);

        if (now.isBefore(earliestCheckIn)) {
            throw new Exception("Too early to check in. You can check in 10 minutes before the start time.");
        }

        if (now.isAfter(latestCheckIn)) {
            throw new Exception("Check-in window expired. This booking is now considered a NO-SHOW.");
        }


        // ------------------------
        // UPDATE BOOKING
        // ------------------------
        BookingManager.getInstance().applyDepositToFinalCost(bookingId);

        // ------------------------
        // UPDATE ROOM
        // ------------------------
        Room room = roomRepo.getById(roomId);
        if (room == null) {
            throw new Exception("Room not found.");
        }

        roomStatuses.put(roomId, "IN_USE");

        // ------------------------
        // CANCEL NO-SHOW TIMER
        // ------------------------
        cancelNoShowTimer(bookingId);
        noShowBookings.remove(bookingId);

        System.out.println("[RoomStatusManager] CHECK-IN → Booking "
                + bookingId + " | Room " + roomId + " IN_USE");

        notifyObservers();
    }

    // ======================================================
    // NO-SHOW TIMER START
    // ======================================================
    public synchronized void startNoShowCountdown(String bookingId, String roomId, LocalDateTime bookingStart) {

        cancelNoShowTimer(bookingId);

        if (bookingId == null || roomId == null || bookingStart == null)
            return;

        LocalDateTime fireTime = bookingStart.plusMinutes(NO_SHOW_MINUTES);

        long delayMs = Duration.between(LocalDateTime.now(), fireTime).toMillis();
        if (delayMs < 0) delayMs = 0;

        Timer timer = new Timer(true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handleNoShow(bookingId, roomId);
            }
        }, delayMs);

        noShowTimers.put(bookingId, timer);
    }

    // ======================================================
    // NO-SHOW HANDLER
    // ======================================================
    private synchronized void handleNoShow(String bookingId, String roomId) {

        noShowTimers.remove(bookingId);
        noShowBookings.add(bookingId);

        roomStatuses.put(roomId, "NO_SHOW");

        BookingManager.getInstance().markDepositForfeited(bookingId);

        System.out.println("[RoomStatusManager] NO-SHOW → Booking "
                + bookingId + " | Room " + roomId);

        notifyObservers();
    }

    // ======================================================
    // SENSOR EVENTS
    // ======================================================
    public synchronized void registerCheckIn(String bookingId, String roomId) {

        cancelNoShowTimer(bookingId);
        noShowBookings.remove(bookingId);

        roomStatuses.put(roomId, "IN_USE");

        BookingManager.getInstance().applyDepositToFinalCost(bookingId);

        System.out.println("[RoomStatusManager] SENSOR CHECK-IN → Booking "
                + bookingId + " | Room " + roomId);

        notifyObservers();
    }

    public synchronized void markRoomVacant(String roomId) {
        roomStatuses.put(roomId, "AVAILaABLE");
        notifyObservers();
    }

    public synchronized void updateOccupancy(String roomId, boolean occupied) {
        roomStatuses.put(roomId, occupied ? "IN_USE" : "AVAILABLE");
        notifyObservers();
    }

    // ======================================================
    // HELPERS
    // ======================================================
    private synchronized void cancelNoShowTimer(String bookingId) {
        Timer t = noShowTimers.remove(bookingId);
        if (t != null) t.cancel();
    }

    private Booking findBooking(String id) {
        return BookingManager.getInstance().getBookingById(id);
    }

    public synchronized void forceNoShow(String bookingId, String roomId, String userId) {
        // Unified no-show handler
        handleNoShow(bookingId, roomId);
    }


}
