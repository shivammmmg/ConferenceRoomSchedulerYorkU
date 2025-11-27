package scenario4.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.Room;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RoomFactoryTest – Unit tests for the RoomFactory class (Scenario 4).
 * ---------------------------------------------------------------------
 * <p>This test suite ensures:</p>
 * <ul>
 *     <li>Factory Pattern behavior is correct</li>
 *     <li>{@code createRoom()} consistently constructs fully initialized Room objects</li>
 *     <li>All object fields are populated with the input arguments</li>
 *     <li>No caching or unintended reuse occurs</li>
 *     <li>Factory handles numeric, empty, and Unicode inputs safely</li>
 * </ul>
 * <p>All tests together provide 100% class, method, and line coverage
 * for the RoomFactory class.</p>
 */

class RoomFactoryTest {

    private RoomFactory factory;

    @BeforeEach
    void setUp() {
        factory = new RoomFactory();
    }

    // 1 – createRoom never returns null
    @Test
    void testCreateRoomReturnsNonNull() {
        Room room = factory.createRoom("R101", "Main Hall", 50,
                "LSB 120A", "Projector;Whiteboard", "Lassonde");
        assertNotNull(room);
    }

    // 2 – id and name are set correctly
    @Test
    void testIdAndNameAreSet() {
        Room room = factory.createRoom("R202", "Conference Room", 30,
                "LSB 220", "TV", "Lassonde");
        assertEquals("R202", room.getRoomId());
        assertEquals("Conference Room", room.getRoomName());
    }

    // 3 – capacity is passed through correctly
    @Test
    void testCapacityIsSet() {
        Room room = factory.createRoom("R303", "Lab", 120,
                "LSB 303", "Computers", "Lassonde");
        assertEquals(120, room.getCapacity());
    }

    // 4 – location is set correctly
    @Test
    void testLocationIsSet() {
        Room room = factory.createRoom("R404", "Breakout Space", 10,
                "Bergeron 404", "Sofas", "Bergeron");
        assertEquals("Bergeron 404", room.getLocation());
    }

    // 5 – amenities are set correctly
    @Test
    void testAmenitiesAreSet() {
        String amenities = "Projector;Mic;Speakers";
        Room room = factory.createRoom("R505", "Auditorium", 200,
                "ACE 505", amenities, "ACE");
        assertEquals(amenities, room.getAmenities());
    }

    // 6 – building is set correctly
    @Test
    void testBuildingIsSet() {
        Room room = factory.createRoom("R606", "Study Room", 8,
                "Scott Library 606", "Whiteboard", "Scott Library");
        assertEquals("Scott Library", room.getBuilding());
    }

    // 7 – two calls with same data give two different objects (no caching)
    @Test
    void testCreateRoomReturnsNewInstanceEachTime() {
        Room r1 = factory.createRoom("R707", "Seminar Room", 25,
                "LSB 707", "Projector", "Lassonde");
        Room r2 = factory.createRoom("R707", "Seminar Room", 25,
                "LSB 707", "Projector", "Lassonde");

        assertNotSame(r1, r2);
        // changing one should not affect the other
        r1.setCapacity(100);
        assertEquals(25, r2.getCapacity());
    }

    // 8 – factory supports zero capacity (edge numeric value)
    @Test
    void testZeroCapacityAllowed() {
        Room room = factory.createRoom("R000", "Storage", 0,
                "Basement", "", "Service Building");
        assertEquals(0, room.getCapacity());
    }

    // 9 – factory supports large capacity values
    @Test
    void testLargeCapacityAllowed() {
        Room room = factory.createRoom("R999", "Convocation Hall", 1000,
                "Central Hall", "Stage;Lighting", "Central Building");
        assertEquals(1000, room.getCapacity());
    }

    // 10 – factory supports Unicode / special characters in fields
    @Test
    void testUnicodeAndSpecialCharactersSupported() {
        Room room = factory.createRoom("Ω-101★", "国际会议厅 – Global Hall", 150,
                "Toronto – ΛSB 1F", "4K 🖥; 🎤; 🎧", "世界中心 / World Hub");
        assertEquals("Ω-101★", room.getRoomId());
        assertEquals("国际会议厅 – Global Hall", room.getRoomName());
        assertEquals("Toronto – ΛSB 1F", room.getLocation());
        assertEquals("4K 🖥; 🎤; 🎧", room.getAmenities());
        assertEquals("世界中心 / World Hub", room.getBuilding());
    }
}

