package scenario3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.Booking;
import shared.model.BookingRepository;
import shared.model.Room;
import shared.model.RoomRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SensorSystemTest – Manual High-Coverage Test Suite for Scenario 3
 * ------------------------------------------------------------------------
 * Validates the behaviour of the SensorSystem class, which simulates
 * hardware sensor events (badge scans, presence sensors, room vacancy).
 *
 * <h2>What This Test Suite Covers</h2>
 * <ul>
 *     <li>Singleton behaviour</li>
 *     <li>registerNewBooking() triggers no-show countdown</li>
 *     <li>simulateUserCheckIn() delegates to RoomStatusManager</li>
 *     <li>simulateOccupancyChange() integration</li>
 *     <li>simulateRoomVacant() integration</li>
 *     <li>Null-safety for all public methods</li>
 * </ul>
 */
public class SensorSystemTest {

    private SensorSystem sensor;
    private RoomStatusManager manager;
    private BookingRepository bookingRepo;
    private RoomRepository roomRepo;

    @BeforeEach
    void setup() throws Exception {
        sensor = SensorSystem.getInstance();
        manager = RoomStatusManager.getInstance();
        bookingRepo = BookingRepository.getInstance();
        roomRepo = RoomRepository.getInstance();

        // Clear repositories
        bookingRepo.getAllBookings().clear();
        roomRepo.getAllRooms().clear();

        // Add rooms used by tests
        roomRepo.addRoom(new Room("R1", "Room 1", 4, "A", "", "B"));
        roomRepo.addRoom(new Room("R2", "Room 2", 4, "A", "", "B"));

        // -------------------------------
        // FIXED reset of internal state
        // -------------------------------

        // roomStatuses → Map
        Field f1 = RoomStatusManager.class.getDeclaredField("roomStatuses");
        f1.setAccessible(true);
        ((Map<?, ?>) f1.get(manager)).clear();

        // noShowBookings → Set (✔ FIXED)
        Field f2 = RoomStatusManager.class.getDeclaredField("noShowBookings");
        f2.setAccessible(true);
        ((java.util.Set<?>) f2.get(manager)).clear();

        // noShowTimers → Map
        Field f3 = RoomStatusManager.class.getDeclaredField("noShowTimers");
        f3.setAccessible(true);
        ((Map<?, ?>) f3.get(manager)).clear();
    }


    // ----------------------------------------------------------
    // SINGLETON
    // ----------------------------------------------------------

    @Test
    void testSingletonInstance() {
        SensorSystem another = SensorSystem.getInstance();
        assertSame(sensor, another);
    }

    // ----------------------------------------------------------
    // registerNewBooking()
    // ----------------------------------------------------------

    @Test
    void testRegisterNewBookingStartsTimer() throws Exception {
        LocalDateTime future = LocalDateTime.now().plusMinutes(5);

        Booking b = new Booking("B1", "R1", "U1",
                future, future.plusMinutes(10), "Study");
        bookingRepo.getAllBookings().add(b);

        sensor.registerNewBooking(b);

        // Access private noShowTimers map
        Field timersField = RoomStatusManager.class.getDeclaredField("noShowTimers");
        timersField.setAccessible(true);
        Map<String, ?> timers = (Map<String, ?>) timersField.get(manager);

        assertTrue(timers.containsKey("B1"));
    }

    @Test
    void testRegisterNewBooking_NullDoesNothing() throws Exception {
        // Should not throw
        sensor.registerNewBooking(null);

        Field timersField = RoomStatusManager.class.getDeclaredField("noShowTimers");
        timersField.setAccessible(true);
        Map<String, ?> timers = (Map<String, ?>) timersField.get(manager);

        assertEquals(0, timers.size());
    }

    // ----------------------------------------------------------
    // simulateUserCheckIn()
    // ----------------------------------------------------------

    @Test
    void testSimulateUserCheckIn_MarksInUse() {
        Booking b = new Booking("B2", "R1", "U1",
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), "Test");
        b.setStatus("CONFIRMED");

        bookingRepo.getAllBookings().add(b);

        sensor.simulateUserCheckIn(b);

        assertEquals("IN_USE", manager.getRoomStatus("R1"));
    }

    @Test
    void testSimulateUserCheckIn_NullDoesNothing() {
        // Should not throw
        sensor.simulateUserCheckIn(null);
    }

    // ----------------------------------------------------------
    // simulateOccupancyChange()
    // ----------------------------------------------------------

    @Test
    void testSimulateOccupancyChange_DelegatesToManager() {
        sensor.simulateOccupancyChange("R1", true);

        // Since no booking exists → AVAILABLE
        assertEquals("AVAILABLE", manager.getRoomStatus("R1"));
    }

    // ----------------------------------------------------------
    // simulateRoomVacant()
    // ----------------------------------------------------------

    @Test
    void testSimulateRoomVacantSetsAvailable() {
        manager.registerCheckIn("BX", "R1");
        assertEquals("IN_USE", manager.getRoomStatus("R1"));

        sensor.simulateRoomVacant("R1");
        assertEquals("AVAILABLE", manager.getRoomStatus("R1"));
    }

    // ----------------------------------------------------------
    // EDGE CASE: simulateRoomVacant on unknown room
    // ----------------------------------------------------------

    @Test
    void testSimulateRoomVacant_UnknownRoomStillSetsAvailable() {
        sensor.simulateRoomVacant("UNKNOWN");
        assertEquals("AVAILABLE", manager.getRoomStatus("UNKNOWN"));
    }
}
