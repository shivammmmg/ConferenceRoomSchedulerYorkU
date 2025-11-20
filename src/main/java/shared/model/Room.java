package shared.model;

import javafx.beans.property.*;

/**
 * Unified Room class supporting ALL scenarios.
 */
public class Room {

    // ============================================================
    // Primary data fields (written to CSV)
    // ============================================================
    private String roomId;
    private String roomName;
    private int capacity;
    private String location;
    private String amenities;
    private String building;

    // Scenario 3 fields
    private RoomStatus status = RoomStatus.AVAILABLE;
    private String currentBookingId = null;

    // ============================================================
    // JavaFX properties (Scenario 4)
    // ============================================================
    private final StringProperty roomIdProperty = new SimpleStringProperty();
    private final StringProperty roomNameProperty = new SimpleStringProperty();
    private final IntegerProperty capacityProperty = new SimpleIntegerProperty();
    private final StringProperty locationProperty = new SimpleStringProperty();
    private final StringProperty amenitiesProperty = new SimpleStringProperty();
    private final StringProperty buildingProperty = new SimpleStringProperty();
    private final StringProperty statusProperty = new SimpleStringProperty("AVAILABLE");

    // ============================================================
    // Constructors
    // ============================================================

    /**
     * FIXED — this was wrong before (you were assigning wrong variables).
     * Now it correctly assigns the passed parameters to the fields.
     */
    public Room(String id,
                String name,
                int capacity,
                String location,
                String amenities,
                String building,
                String statusString)
    {
        this.roomId = id;
        this.roomName = name;
        this.capacity = capacity;
        this.location = location;
        this.amenities = amenities;
        this.building = building;

        // Load correct status into enum
        setStatus(statusString);

        syncToProperties();
    }

    // NEW: Restore old 6-argument constructor used by Scenario 2 + 4
    public Room(String id,
                String name,
                int capacity,
                String location,
                String amenities,
                String building)
    {
        this(id, name, capacity, location, amenities, building, "AVAILABLE");
    }


    // Minimal constructors still allowed for other scenarios
    public Room(String id, String name) {
        this.roomId = id;
        this.roomName = name;
        syncToProperties();
    }

    public Room(String roomId, int capacity, String location) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.location = location;
        syncToProperties();
    }

    // ============================================================
    // Sync core fields → JavaFX properties
    // ============================================================
    private void syncToProperties() {
        roomIdProperty.set(roomId);
        roomNameProperty.set(roomName);
        capacityProperty.set(capacity);
        locationProperty.set(location);
        amenitiesProperty.set(amenities);
        buildingProperty.set(building);
        statusProperty.set(status.name());
    }

    // ============================================================
    // Basic Getters
    // ============================================================
    public String getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public int getCapacity() { return capacity; }
    public String getLocation() { return location; }
    public String getAmenities() { return amenities; }
    public String getBuilding() { return building; }

    // Scenario 3 compatibility
    public synchronized String getId() { return roomId; }
    public synchronized String getName() { return roomName; }

    // Emoji icon (Scenario 2)
    public String getAmenitiesIcon() {
        if (amenities == null) return "";
        if (amenities.contains("Projector") && amenities.contains("VideoConference")) return "📊📹";
        if (amenities.contains("Projector")) return "📊";
        if (amenities.contains("VideoConference")) return "📹";
        return "📋";
    }

    // ============================================================
    // STATUS — FIXED AND MADE CONSISTENT
    // ============================================================
    public synchronized String getStatus() { return status.name(); }
    public synchronized RoomStatus getStatusEnum() { return status; }

    public synchronized void setStatus(RoomStatus status) {
        this.status = status;
        statusProperty.set(status.name());
    }

    /**
     * FIXED — this correctly interprets CSV text status.
     */
    public synchronized void setStatus(String statusString) {
        if (statusString == null) {
            this.status = RoomStatus.AVAILABLE;
        } else {
            switch (statusString.toUpperCase()) {

                case "ENABLED":
                case "ACTIVE":
                case "AVAILABLE":
                    this.status = RoomStatus.AVAILABLE;
                    break;

                case "DISABLED":
                case "INACTIVE":
                    this.status = RoomStatus.DISABLED;
                    break;

                case "MAINTENANCE":
                case "MAINT":
                    this.status = RoomStatus.MAINTENANCE;
                    break;

                case "OCCUPIED":
                    this.status = RoomStatus.OCCUPIED;
                    break;

                default:
                    this.status = RoomStatus.AVAILABLE;
            }
        }

        statusProperty.set(this.status.name());
    }

    public synchronized String getCurrentBookingId() { return currentBookingId; }
    public synchronized void setCurrentBookingId(String bid) { this.currentBookingId = bid; }

    // ============================================================
    // Setters (sync both field & JavaFX prop)
    // ============================================================
    public void setRoomName(String name) {
        this.roomName = name;
        roomNameProperty.set(name);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        capacityProperty.set(capacity);
    }

    public void setLocation(String location) {
        this.location = location;
        locationProperty.set(location);
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
        amenitiesProperty.set(amenities);
    }

    public void setBuilding(String building) {
        this.building = building;
        buildingProperty.set(building);
    }

    // ============================================================
    // JavaFX property getters
    // ============================================================
    public StringProperty roomIdProperty() { return roomIdProperty; }
    public StringProperty roomNameProperty() { return roomNameProperty; }
    public IntegerProperty capacityProperty() { return capacityProperty; }
    public StringProperty locationProperty() { return locationProperty; }
    public StringProperty amenitiesProperty() { return amenitiesProperty; }
    public StringProperty buildingProperty() { return buildingProperty; }
    public StringProperty statusProperty() { return statusProperty; }

    // ============================================================
    // CSV FORMAT
    // ============================================================
    @Override
    public String toString() {
        return String.format(
                "%s,%s,%d,%s,%s,%s,%s",
                roomId,
                roomName != null ? roomName : "",
                capacity,
                location != null ? location : "",
                amenities != null ? amenities : "",
                building != null ? building : "",
                status.name()
        );
    }
}