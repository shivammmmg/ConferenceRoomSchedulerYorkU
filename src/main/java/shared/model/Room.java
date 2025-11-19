package shared.model;

import javafx.beans.property.*;

/**
 * Unified Room class supporting ALL scenarios:
 *
 * Scenario 2:
 *  - roomId, roomName, capacity, location, amenities, building, amenitiesIcon
 *
 * Scenario 3:
 *  - getId(), getName(), status enum, booking assignment
 *
 * Scenario 4:
 *  - JavaFX properties for UI binding
 *  - full edit support for name/capacity/location/building/amenities
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

    // Full constructor (Scenario 2)
    public Room(String roomId, String roomName, int capacity,
                String location, String amenities, String building) {

        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.location = location;
        this.amenities = amenities;
        this.building = building;

        syncToProperties();
    }

    // Scenario 3 constructor
    public Room(String id, String name) {
        this.roomId = id;
        this.roomName = name;
        syncToProperties();
    }

    // Scenario 4 constructor (minimal)
    public Room(String roomId, int capacity, String location) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.location = location;
        syncToProperties();
    }

    // ============================================================
    // Sync fields → JavaFX properties
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
    // Getters (Scenario 2 & 3)
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

    // Amenity icon logic (Scenario 2 tiles)
    public String getAmenitiesIcon() {
        if (amenities == null) return "";
        if (amenities.contains("Projector") && amenities.contains("VideoConference")) return "📊📹";
        if (amenities.contains("Projector")) return "📊";
        if (amenities.contains("VideoConference")) return "📹";
        return "📋";
    }

    // Scenario 3 status
    public synchronized String getStatus() { return status.name(); }
    public synchronized RoomStatus getStatusEnum() { return status; }

    public synchronized void setStatus(RoomStatus status) {
        this.status = status;
        statusProperty.set(status.name());
    }

    // Accept String status for Scenario 4
    public synchronized void setStatus(String statusString) {
        if (statusString == null) return;
        switch (statusString.toUpperCase()) {
            case "ENABLED":
            case "ACTIVE": this.status = RoomStatus.AVAILABLE; break;
            case "DISABLED":
            case "INACTIVE": this.status = RoomStatus.MAINTENANCE; break;
            case "OCCUPIED": this.status = RoomStatus.OCCUPIED; break;
            default: this.status = RoomStatus.AVAILABLE;
        }
        statusProperty.set(this.status.name());
    }

    public synchronized String getCurrentBookingId() { return currentBookingId; }
    public synchronized void setCurrentBookingId(String bid) { this.currentBookingId = bid; }

    // ============================================================
    // Setters — NOW SYNC BOTH FIELD + PROPERTY
    // ============================================================

    public void setRoomName(String name) {
        this.roomName = name;
        this.roomNameProperty.set(name);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        this.capacityProperty.set(capacity);
    }

    public void setLocation(String location) {
        this.location = location;
        this.locationProperty.set(location);
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
        this.amenitiesProperty.set(amenities);
    }

    public void setBuilding(String building) {
        this.building = building;
        this.buildingProperty.set(building);
    }

    // ============================================================
    // JavaFX property getters (Scenario 4)
    // ============================================================

    public StringProperty roomIdProperty() { return roomIdProperty; }
    public StringProperty roomNameProperty() { return roomNameProperty; }
    public IntegerProperty capacityProperty() { return capacityProperty; }
    public StringProperty locationProperty() { return locationProperty; }
    public StringProperty amenitiesProperty() { return amenitiesProperty; }
    public StringProperty buildingProperty() { return buildingProperty; }
    public StringProperty statusProperty() { return statusProperty; }

    // ============================================================
    // CSV output
    // ============================================================
    @Override
    public String toString() {
        return String.format("%s,%s,%d,%s,%s,%s",
                roomId,
                roomName != null ? roomName : "",
                capacity,
                location != null ? location : "",
                amenities != null ? amenities : "",
                building != null ? building : ""
        );
    }
}
