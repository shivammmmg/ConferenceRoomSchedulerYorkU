package scenario3.controller;

import javafx.application.Platform;
import shared.model.RoomStatus;
import scenario3.observer.RoomStatusObserver;
import shared.model.Booking;
import shared.model.Room;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SINGLETON: RoomStatusManager
 * Authoritative in-memory manager of rooms and bookings.
 */
public class RoomStatusManager {

    private static RoomStatusManager instance;

    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    private final List<RoomStatusObserver> observers = Collections.synchronizedList(new ArrayList<>());
    private static final DateTimeFormatter LOG_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String ts() { return LocalDateTime.now().format(LOG_FORMAT); }

    private RoomStatusManager() {
        rooms.put("R101", new Room("R101", "Seminar Rm 101"));
        rooms.put("R102", new Room("R102", "Seminar Rm 102"));
        rooms.put("R201", new Room("R201", "Small Group Room A"));
        rooms.put("R202", new Room("R202", "Small Group Room B"));
    }

    public static synchronized RoomStatusManager getInstance() {
        if (instance == null) instance = new RoomStatusManager();
        return instance;
    }

    // Observer management
    public void registerObserver(RoomStatusObserver o) { if (o != null && !observers.contains(o)) observers.add(o); }
    public void unregisterObserver(RoomStatusObserver o) { observers.remove(o); }

    private void notifyObservers(Room room) {
        Platform.runLater(() -> {
            synchronized (observers) {
                for (RoomStatusObserver o : observers) {
                    try { o.onRoomStatusChanged(room); }
                    catch (Exception ex) { System.out.println(ts() + " — [Observer Error] " + ex.getMessage()); }
                }
            }
        });
    }

    // Room operations
    public Collection<Room> getAllRooms() { return Collections.unmodifiableCollection(rooms.values()); }
    public Room getRoom(String roomId) { return rooms.get(roomId); }

    public synchronized void updateRoomStatus(String roomId, RoomStatus status) {
        Room r = rooms.get(roomId);
        if (r == null) return;
        r.setStatus(status);
        System.out.println(ts() + " — Room " + roomId + " status updated to " + status);
        notifyObservers(r);
    }

    public synchronized void assignBookingToRoom(String roomId, String bookingId) {
        Room r = rooms.get(roomId);
        if (r == null) return;
        r.setCurrentBookingId(bookingId);
        r.setStatus(RoomStatus.RESERVED);
        System.out.println(ts() + " — Room " + roomId + " assigned booking " + bookingId);
        notifyObservers(r);
    }

    public synchronized void clearBookingFromRoom(String roomId) {
        Room r = rooms.get(roomId);
        if (r == null) return;
        r.setCurrentBookingId(null);
        r.setStatus(RoomStatus.AVAILABLE);
        System.out.println(ts() + " — Room " + roomId + " booking cleared");
        notifyObservers(r);
    }

    // Booking operations
    public void addBooking(Booking b) {
        bookings.put(b.getBookingId(), b);
        assignBookingToRoom(b.getRoomId(), b.getBookingId());
    }

    public Booking getBooking(String bookingId) { return bookings.get(bookingId); }

    public void markCheckedIn(String bookingId) {
        Booking b = bookings.get(bookingId);
        if (b == null) return;

        b.setCheckedIn(true);
        Room r = rooms.get(b.getRoomId());
        if (r != null) {
            r.setStatus(RoomStatus.IN_USE);
            System.out.println(ts() + " — Booking " + bookingId + " checked in.");
            notifyObservers(r);
        }
    }

    public void markNoShow(String bookingId) {
        Booking b = bookings.get(bookingId);
        if (b == null) return;

        // Already processed? Exit early
        if (b.isDepositForfeited() && b.isForfeitPopupShown()) return;

        b.forfeitDeposit();
        b.setForfeitPopupShown(true);

        Room r = rooms.get(b.getRoomId());
        if (r != null) {
            r.setStatus(RoomStatus.NO_SHOW);
            r.setCurrentBookingId(null);
            notifyObservers(r);
        }



        System.out.println(ts() + " — Booking " + bookingId + " marked NO_SHOW (deposit forfeited)");
    }
}
