package shared.model;

import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    // ============================================================
    // CONSTRUCTORS
    // ============================================================

    @Test
    void testFullConstructor_LoadsFieldsCorrectly() {
        Room r = new Room(
                "R101",
                "Lecture Hall",
                100,
                "2nd Floor",
                "Projector",
                "Building A",
                "MAINT"
        );

        assertEquals("R101", r.getRoomId());
        assertEquals("Lecture Hall", r.getRoomName());
        assertEquals(100, r.getCapacity());
        assertEquals("2nd Floor", r.getLocation());
        assertEquals("Projector", r.getAmenities());
        assertEquals("Building A", r.getBuilding());
        assertEquals(RoomStatus.MAINTENANCE, r.getStatusEnum());
    }

    @Test
    void testSixArgConstructor_DefaultStatusIsAvailable() {
        Room r = new Room("A1", "RoomA", 10, "1F", "VC", "B1");

        assertEquals(RoomStatus.AVAILABLE, r.getStatusEnum());
        assertEquals("AVAILABLE", r.getStatus());
    }

    @Test
    void testTwoArgConstructor() {
        Room r = new Room("X1", "Mini Room");
        assertEquals("X1", r.getRoomId());
        assertEquals("Mini Room", r.getRoomName());
    }

    @Test
    void testThreeArgConstructor() {
        Room r = new Room("Z1", 20, "Basement");
        assertEquals("Z1", r.getRoomId());
        assertEquals(20, r.getCapacity());
        assertEquals("Basement", r.getLocation());
    }

    // ============================================================
    // PROPERTY SYNC TESTS
    // ============================================================

    @Test
    void testSyncToProperties() {
        Room r = new Room("101", "Conf", 20, "L2", "Proj", "ENG", "ACTIVE");

        assertEquals("101", r.roomIdProperty().get());
        assertEquals("Conf", r.roomNameProperty().get());
        assertEquals(20, r.capacityProperty().get());
        assertEquals("L2", r.locationProperty().get());
        assertEquals("Proj", r.amenitiesProperty().get());
        assertEquals("ENG", r.buildingProperty().get());
        assertEquals("AVAILABLE", r.statusProperty().get()); // ACTIVE → AVAILABLE
    }

    @Test
    void testSettersUpdateFieldsAndProperties() {
        Room r = new Room("10", "Old", 5, "A", "None", "B");

        r.setRoomName("NewName");
        assertEquals("NewName", r.getRoomName());
        assertEquals("NewName", r.roomNameProperty().get());

        r.setCapacity(77);
        assertEquals(77, r.getCapacity());
        assertEquals(77, r.capacityProperty().get());

        r.setLocation("NewLoc");
        assertEquals("NewLoc", r.getLocation());
        assertEquals("NewLoc", r.locationProperty().get());

        r.setAmenities("Projector");
        assertEquals("Projector", r.getAmenities());
        assertEquals("Projector", r.amenitiesProperty().get());

        r.setBuilding("ENG");
        assertEquals("ENG", r.getBuilding());
        assertEquals("ENG", r.buildingProperty().get());
    }

    // ============================================================
    // STATUS PARSING LOGIC
    // ============================================================

    @Test
    void testStatusParsing_AvailableVariants() {
        Room r = new Room("1", "A", 1, "L", "", "", "active");

        assertEquals(RoomStatus.AVAILABLE, r.getStatusEnum());
    }

    @Test
    void testStatusParsing_DisabledVariants() {
        Room r = new Room("1", "A", 1, "L", "", "", "INACTIVE");

        assertEquals(RoomStatus.DISABLED, r.getStatusEnum());
    }

    @Test
    void testStatusParsing_MaintenanceVariants() {
        Room r = new Room("1", "A", 1, "L", "", "", "MAINT");

        assertEquals(RoomStatus.MAINTENANCE, r.getStatusEnum());
    }

    @Test
    void testStatusParsing_Occupied() {
        Room r = new Room("1", "A", 1, "L", "", "", "OCCUPIED");

        assertEquals(RoomStatus.OCCUPIED, r.getStatusEnum());
    }

    @Test
    void testStatusParsing_DefaultsToAvailableOnUnknown() {
        Room r = new Room("1", "A", 1, "L", "", "", "UNKNOWN123");

        assertEquals(RoomStatus.AVAILABLE, r.getStatusEnum());
    }

    @Test
    void testSetStatusEnum() {
        Room r = new Room("1", "A");

        r.setStatus(RoomStatus.OCCUPIED);
        assertEquals("OCCUPIED", r.statusProperty().get());
        assertEquals(RoomStatus.OCCUPIED, r.getStatusEnum());
    }

    // ============================================================
    // CURRENT BOOKING ID
    // ============================================================

    @Test
    void testCurrentBookingId() {
        Room r = new Room("1", "A");
        assertNull(r.getCurrentBookingId());

        r.setCurrentBookingId("BKG123");
        assertEquals("BKG123", r.getCurrentBookingId());
    }

    // ============================================================
    // AMENITIES ICON LOGIC
    // ============================================================

    @Test
    void testAmenitiesIcon_ProjAndVC() {
        Room r = new Room("1", "A", 1, "L", "Projector,VideoConference", "", "");
        assertEquals("📊📹", r.getAmenitiesIcon());
    }

    @Test
    void testAmenitiesIcon_OnlyProjector() {
        Room r = new Room("1", "A", 1, "L", "Projector", "", "");
        assertEquals("📊", r.getAmenitiesIcon());
    }

    @Test
    void testAmenitiesIcon_OnlyVC() {
        Room r = new Room("1", "A", 1, "L", "VideoConference", "", "");
        assertEquals("📹", r.getAmenitiesIcon());
    }

    @Test
    void testAmenitiesIcon_Default() {
        Room r = new Room("1", "A", 1, "L", "Whiteboard", "", "");
        assertEquals("📋", r.getAmenitiesIcon());
    }

    @Test
    void testAmenitiesIcon_Null() {
        Room r = new Room("1", "A", 1, "L", null, "", "");
        assertEquals("", r.getAmenitiesIcon());
    }

    // ============================================================
    // CSV OUTPUT
    // ============================================================

    @Test
    void testToStringCsvFormat() {
        Room r = new Room("101", "BigRoom", 50, "L2", "Proj", "ENG", "DISABLED");

        String csv = r.toString();
        assertEquals("101,BigRoom,50,L2,Proj,ENG,DISABLED", csv);
    }
}
