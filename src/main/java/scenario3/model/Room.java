package scenario3.model;

/**
 * MODEL: Room
 * Lightweight room model used by RoomStatusManager.
 */
public class Room {
    private final String id;
    private final String name;
    private RoomStatus status;
    private String currentBookingId; // null if none

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
        this.status = RoomStatus.AVAILABLE;
        this.currentBookingId = null;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public synchronized RoomStatus getStatus() { return status; }
    public synchronized void setStatus(RoomStatus status) { this.status = status; }

    public synchronized String getCurrentBookingId() { return currentBookingId; }
    public synchronized void setCurrentBookingId(String bid) { this.currentBookingId = bid; }
}
