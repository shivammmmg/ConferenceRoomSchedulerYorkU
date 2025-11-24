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

/**
 * RoomStatusManager – Scenario 3 (Check-In & Usage Monitoring)
 * ------------------------------------------------------------------
 * <p>This class serves as the <b>central engine</b> for all real-time
 * room/booking status logic in Scenario 3. It combines:</p>
 *
 * <ul>
 *     <li><b>Singleton Pattern</b> – one global manager for all active room states</li>
 *     <li><b>Observer Pattern</b> – notifies UI components (BookingFX) of changes</li>
 *     <li><b>No-Show Automation</b> – timers that auto-mark bookings as NO_SHOW</li>
 *     <li><b>Sensor Integration</b> – handles simulated sensor check-in/vacancy events</li>
 * </ul>
 *
 * <h2>System Responsibilities</h2>
 * <ul>
 *     <li>Tracks the live status of each room:
 *         <ul>
 *             <li>AVAILABLE</li>
 *             <li>IN_USE</li>
 *             <li>NO_SHOW</li>
 *         </ul>
 *     </li>
 *     <li>Starts/stops NO-SHOW countdown timers for each booking</li>
 *     <li>Handles real user check-in logic (Scenario 2 → Scenario 3 interaction)</li>
 *     <li>Triggers UI refreshes through Observer notifications</li>
 *     <li>Integrates with sensors for automated occupancy updates</li>
 * </ul>
 *
 * <h2>Why This Class Exists (D3 Justification)</h2>
 * <p>Scenario 3 requires “real-time” updates to booking state. Rather than having
 * BookingFX poll constantly, this manager <b>pushes</b> updates using Observers, so:</p>
 * <ul>
 *     <li>My Bookings auto-refresh when a check-in/no-show occurs</li>
 *     <li>Sensor events update UI instantly</li>
 *     <li>No duplicated logic across controllers</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Singleton</b>: Only one global status engine exists.</li>
 *     <li><b>Observer</b>: UI elements (BookingStatusObserver) attach to receive notifications.</li>
 *     <li><b>Facade-Like Role</b>: Central access point for check-ins, timers, and occupancy updates.</li>
 * </ul>
 *
 * <h2>Scenarios Supported</h2>
 * <ul>
 *     <li><b>Scenario 2</b> – Booking creation (this class checks booking state)</li>
 *     <li><b>Scenario 3</b> – Check-in, No-Show, Usage Monitoring</li>
 *     <li><b>Scenario 4</b> – Admin dashboards rely on accurate live room status</li>
 * </ul>
 *
 * <h2>Key Features</h2>
 * <ul>
 *     <li>10-minute early check-in window</li>
 *     <li>NO_SHOW automation after configurable delay</li>
 *     <li>Real-time UI updates</li>
 *     <li>Sensor-triggered room state transitions</li>
 *     <li>Booking-aware occupancy logic (supports multiple overlapping edge cases)</li>
 * </ul>
 */


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

    private final int NO_SHOW_MINUTES = 30; // For demo = 2 minutes

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

        Booking b = BookingManager.getInstance().getBookingById(bookingId);
        if (b == null) return;

        // ❌ Do NOT start timer if user already checked in
        if ("IN_USE".equals(b.getStatus())) return;

        // ❌ Do NOT start timer if already NO_SHOW
        if ("NO_SHOW".equals(b.getStatus())) return;

        // ❌ Do NOT restart timer if booking start has passed
        if (LocalDateTime.now().isAfter(bookingStart)) return;

        cancelNoShowTimer(bookingId);

        LocalDateTime fireTime = bookingStart.plusMinutes(NO_SHOW_MINUTES);
        long delayMs = Duration.between(LocalDateTime.now(), fireTime).toMillis();
        if (delayMs < 0) delayMs = 0;

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override public void run() {
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
        roomStatuses.put(roomId, "AVAILABLE");
        notifyObservers();
    }

    public synchronized void updateOccupancy(String roomId, boolean occupied) {
        LocalDateTime now = LocalDateTime.now();

        // 1. Is there an active booking right now?
        Booking active = BookingManager.getInstance().getActiveBookingForRoom(roomId, now);

        if (active == null) {
            // No booking → always AVAILABLE
            roomStatuses.put(roomId, "AVAILABLE");
            notifyObservers();
            return;
        }

        String bookingStatus = active.getStatus();

        // 2. If booking is NO_SHOW → ALWAYS AVAILABLE
        if ("NO_SHOW".equals(bookingStatus)) {
            roomStatuses.put(roomId, "AVAILABLE");
            notifyObservers();
            return;
        }

        // 3. If user checked in → IN_USE during booking window ONLY
        if ("IN_USE".equals(bookingStatus)) {
            // Inside booking window → IN_USE
            if (!now.isBefore(active.getStartTime()) && !now.isAfter(active.getEndTime())) {
                roomStatuses.put(roomId, "IN_USE");
            }
            // Outside booking window → AVAILABLE
            else {
                roomStatuses.put(roomId, "AVAILABLE");
            }

            notifyObservers();
            return;
        }

        // 4. Booking exists but user has NOT checked in yet
        // Room MUST be AVAILABLE during grace/check-in period
        roomStatuses.put(roomId, "AVAILABLE");
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
