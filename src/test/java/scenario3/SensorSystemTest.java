package scenario3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scenario2.controller.BookingManager;
import shared.model.Booking;
import shared.model.BookingRepository;
import shared.model.Room;
import shared.model.RoomRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SensorSystemTest – Manual High-Coverage Test Suite for Scenario 3
 * (CSV writes redirected to TestData/ via temporary project root)
 */
public class SensorSystemTest {

    private SensorSystem sensor;
    private RoomStatusManager manager;
    private BookingRepository bookingRepo;
    private RoomRepository roomRepo;

    /** Temporary test root folder (each test gets its own). */
    private Path tempProjectRoot;

    @BeforeEach
    void setup() throws Exception {
        // ======================
        // 1. Create fresh root
        // ======================
        tempProjectRoot = Files.createTempDirectory("sensor-test-");

        // 1A — RoomRepository reads from user.dir/data/rooms.csv
        System.setProperty("user.dir", tempProjectRoot.toString());

        // 1B — BookingRepository reads from booking.csv.path
        Files.createDirectories(tempProjectRoot.resolve("data")); // ensure /data exists
        System.setProperty("booking.csv.path",
                tempProjectRoot.resolve("data/bookings.csv").toString());

        // ======================
        // 2. Reset all singletons
        // ======================
        resetSingleton(RoomStatusManager.class, "instance");
        resetSingleton(SensorSystem.class, "instance");
        resetSingleton(RoomRepository.class, "instance");
        BookingRepository.resetForTests();   // <-- official reset
        resetSingleton(BookingManager.class, "instance");

        // ======================
        // 3. Build repos
        // ======================
        roomRepo = RoomRepository.getInstance();       // uses user.dir/data/rooms.csv
        bookingRepo = BookingRepository.getInstance(); // uses booking.csv.path

        roomRepo.getAllRooms().clear();
        bookingRepo.getAllBookings().clear();

        roomRepo.addRoom(new Room("R1", "Room 1", 4, "A", "", "B"));
        roomRepo.addRoom(new Room("R2", "Room 2", 4, "A", "", "B"));

        // ======================
        // 4. Manager + Sensor
        // ======================
        manager = RoomStatusManager.getInstance();
        sensor = SensorSystem.getInstance();

        clearPrivateMap(RoomStatusManager.class, manager, "roomStatuses");
        clearPrivateMap(RoomStatusManager.class, manager, "noShowTimers");
        clearPrivateSet(RoomStatusManager.class, manager, "noShowBookings");
    }


    /** After each test: delete the temporary directory. */
    @AfterEach
    void cleanup() throws Exception {
        if (tempProjectRoot != null && Files.exists(tempProjectRoot)) {
            Files.walk(tempProjectRoot)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try { Files.deleteIfExists(path); } catch (Exception ignored) {}
                    });
        }
    }

    // =============================================================
    // Reflection helpers
    // =============================================================

    private void resetSingleton(Class<?> clazz, String fieldName) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(null, null); // static → target = null
    }

    private void clearPrivateMap(Class<?> clazz, Object instance, String fieldName) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        Object val = f.get(instance);
        if (val instanceof Map<?, ?> map) {
            map.clear();
        }
    }

    private void clearPrivateSet(Class<?> clazz, Object instance, String fieldName) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        Object val = f.get(instance);
        if (val instanceof Set<?> set) {
            set.clear();
        }
    }

    // =============================================================
    // TESTS
    // =============================================================

    @Test
    void testSingletonInstance() {
        SensorSystem another = SensorSystem.getInstance();
        assertSame(sensor, another);
    }

    @Test
    void testRegisterNewBookingStartsTimer() throws Exception {
        LocalDateTime future = LocalDateTime.now().plusMinutes(5);

        Booking b = new Booking("B1", "R1", "U1",
                future, future.plusMinutes(10), "Study");
        b.setStatus("CONFIRMED");

        bookingRepo.getAllBookings().add(b);

        sensor.registerNewBooking(b);

        // let the timer scheduling run
        Thread.sleep(100);

        Field timersField = RoomStatusManager.class.getDeclaredField("noShowTimers");
        timersField.setAccessible(true);
        Map<String, ?> timers = (Map<String, ?>) timersField.get(manager);

        // ✅ A timer SHOULD be registered for B1
        assertTrue(timers.containsKey("B1"));
    }

    @Test
    void testRegisterNewBooking_NullDoesNothing() throws Exception {
        sensor.registerNewBooking(null);

        Field timersField = RoomStatusManager.class.getDeclaredField("noShowTimers");
        timersField.setAccessible(true);
        Map<String, ?> timers = (Map<String, ?>) timersField.get(manager);

        assertEquals(0, timers.size());
    }

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
        sensor.simulateUserCheckIn(null);
        // Just ensure no exceptions
    }

    @Test
    void testSimulateOccupancyChange_DelegatesToManager() {
        sensor.simulateOccupancyChange("R1", true);
        assertEquals("AVAILABLE", manager.getRoomStatus("R1"));
    }

    @Test
    void testSimulateRoomVacantSetsAvailable() {
        manager.registerCheckIn("BX", "R1");
        assertEquals("IN_USE", manager.getRoomStatus("R1"));

        sensor.simulateRoomVacant("R1");
        assertEquals("AVAILABLE", manager.getRoomStatus("R1"));
    }

    @Test
    void testSimulateRoomVacant_UnknownRoomStillSetsAvailable() {
        sensor.simulateRoomVacant("UNKNOWN");
        assertEquals("AVAILABLE", manager.getRoomStatus("UNKNOWN"));
    }
}
