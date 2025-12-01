package scenario3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.Booking;
import shared.model.BookingRepository;
import shared.model.Room;
import shared.model.RoomRepository;
import shared.observer.Observer;

import java.lang.reflect.Field;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RoomStatusManagerTest {

    private RoomStatusManager manager;
    private BookingRepository bookingRepo;
    private RoomRepository roomRepo;

    /** isolated test root */
    private Path tempRoot;

    // =====================================================================
    // CREATE TEMP PROJECT ROOT BUT DO NOT RESET SINGLETONS
    // (this keeps constructor logic = high coverage)
    // =====================================================================
    @BeforeEach
    void setup() throws Exception {

        // 1. Create isolated test directory → all CSV writes go here
        // 1. Create isolated test directory → redirect ALL CSV writes here
        tempRoot = Files.createTempDirectory("roomstatus-test-");
        System.setProperty("user.dir", tempRoot.toString());

// 1B. Create fake data folder inside the temp root
        Path dataDir = tempRoot.resolve("data");
        Files.createDirectories(dataDir);

// 1C. Pre-create empty CSVs so BookingRepository + RoomRepository use them
        Files.writeString(dataDir.resolve("rooms.csv"), "");
        Files.writeString(dataDir.resolve("bookings.csv"), "");
        Files.writeString(dataDir.resolve("user.csv"), "");   // if needed, harmless


        // 2. Do NOT reset singletons.
        //    This preserves constructor, static init, CSV loader, lifecycle → high coverage.
        manager = RoomStatusManager.getInstance();
        bookingRepo = BookingRepository.getInstance();
        roomRepo = RoomRepository.getInstance();

        // 3. Clear repository state manually
        bookingRepo.getAllBookings().clear();
        roomRepo.getAllRooms().clear();

        // 4. Add default rooms (same as before)
        roomRepo.addRoom(new Room("R1", "Test", 5, "A", "", "B"));
        roomRepo.addRoom(new Room("R2", "Test2", 5, "A", "", "B"));
        roomRepo.addRoom(new Room("RX", "Test3", 5, "A", "", "B"));
        roomRepo.addRoom(new Room("RZ", "Test4", 5, "A", "", "B"));

        // 5. Clear RoomStatusManager internal state
        clearPrivateMap(RoomStatusManager.class, manager, "roomStatuses");
        clearPrivateMap(RoomStatusManager.class, manager, "noShowTimers");
        clearPrivateSet(RoomStatusManager.class, manager, "noShowBookings");
    }

    // =====================================================================
    // DELETE TEMP ROOT AFTER TEST
    // =====================================================================
    @AfterEach
    void cleanup() throws Exception {
        if (tempRoot != null && Files.exists(tempRoot)) {
            Files.walk(tempRoot)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try { Files.deleteIfExists(path); } catch (Exception ignored) {}
                    });
        }
    }

    // =====================================================================
    // REFLECTION HELPERS
    // =====================================================================
    private void clearPrivateMap(Class<?> clazz, Object instance, String fieldName) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        ((Map<?, ?>) f.get(instance)).clear();
    }

    private void clearPrivateSet(Class<?> clazz, Object instance, String fieldName) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        ((java.util.Set<?>) f.get(instance)).clear();
    }

    // =====================================================================
    // ALL YOUR ORIGINAL TESTS BELOW (UNCHANGED)
    // =====================================================================

    @Test
    void testDefaultRoomStatusIsAvailable() {
        assertEquals("AVAILABLE", manager.getRoomStatus("UnknownRoom"));
    }

    @Test
    void testMarkRoomVacantSetsAvailable() {
        manager.markRoomVacant("R1");
        assertEquals("AVAILABLE", manager.getRoomStatus("R1"));
    }

    @Test
    void testRegisterCheckInMarksInUse() {
        manager.registerCheckIn("B1", "R1");
        assertEquals("IN_USE", manager.getRoomStatus("R1"));
    }

    @Test
    void testForceNoShowMarksNoShow() {
        manager.forceNoShow("B55", "R2", "U1");
        assertTrue(manager.isNoShow("B55"));
        assertEquals("NO_SHOW", manager.getRoomStatus("R2"));
    }

    @Test
    void testUpdateOccupancy_NoActiveBooking() {
        manager.updateOccupancy("RZ", true);
        assertEquals("AVAILABLE", manager.getRoomStatus("RZ"));
    }

    @Test
    void testUpdateOccupancy_ActiveBookingInsideWindow_InUse() {
        LocalDateTime now = LocalDateTime.now();
        Booking b = new Booking("B10", "R1", "U1",
                now.minusMinutes(2), now.plusMinutes(5), "Study");
        b.setStatus("IN_USE");
        bookingRepo.getAllBookings().add(b);

        manager.updateOccupancy("R1", true);
        assertEquals("IN_USE", manager.getRoomStatus("R1"));
    }

    @Test
    void testUpdateOccupancy_ActiveBookingOutsideWindow_BecomesAvailable() {
        LocalDateTime now = LocalDateTime.now();
        Booking b = new Booking("B11", "R1", "U1",
                now.minusHours(1), now.minusMinutes(30), "Old");
        b.setStatus("IN_USE");
        bookingRepo.getAllBookings().add(b);

        manager.updateOccupancy("R1", true);
        assertEquals("AVAILABLE", manager.getRoomStatus("R1"));
    }

    @Test
    void testUpdateOccupancy_BookingNotCheckedInYet() {
        LocalDateTime now = LocalDateTime.now();
        Booking b = new Booking("B12", "R2", "U1",
                now.minusMinutes(2), now.plusMinutes(5), "Study");
        b.setStatus("CONFIRMED");
        bookingRepo.getAllBookings().add(b);

        manager.updateOccupancy("R2", true);
        assertEquals("AVAILABLE", manager.getRoomStatus("R2"));
    }

    @Test
    void testUpdateOccupancy_BookingIsNoShow_BecomesAvailable() {
        LocalDateTime now = LocalDateTime.now();
        Booking b = new Booking("B13", "R1", "U1",
                now.minusMinutes(2), now.plusMinutes(5), "Study");
        b.setStatus("NO_SHOW");
        bookingRepo.getAllBookings().add(b);

        manager.updateOccupancy("R1", true);
        assertEquals("AVAILABLE", manager.getRoomStatus("R1"));
    }

    @Test
    void testStartNoShowCountdown_BookingNullDoesNothing() {
        manager.startNoShowCountdown("DoesNotExist", "R1", LocalDateTime.now().plusMinutes(5));
    }

    @Test
    void testStartNoShowCountdown_AlreadyInUse_NoTimerSet() {
        LocalDateTime now = LocalDateTime.now();
        Booking b = new Booking("T1", "R1", "U1",
                now.minusMinutes(2), now.plusMinutes(10), "Study");
        b.setStatus("IN_USE");

        bookingRepo.getAllBookings().add(b);
        manager.startNoShowCountdown("T1", "R1", b.getStartTime());

        assertFalse(manager.isNoShow("T1"));
    }

    @Test
    void testStartNoShowCountdown_StartTimeAlreadyPassed_NoTimer() {
        LocalDateTime now = LocalDateTime.now();
        Booking b = new Booking("T3", "R1", "U1",
                now.minusMinutes(20), now.plusMinutes(20), "Study");
        b.setStatus("CONFIRMED");

        bookingRepo.getAllBookings().add(b);
        manager.startNoShowCountdown("T3", "R1", b.getStartTime());

        assertFalse(manager.isNoShow("T3"));
    }

    @Test
    void testCheckIn_BookingNotFoundThrows() {
        Exception ex = assertThrows(Exception.class, () ->
                manager.checkIn("NOBOOK", "R1", "U1"));
        assertTrue(ex.getMessage().contains("Booking not found"));
    }

    @Test
    void testCheckIn_WrongUserThrows() {
        LocalDateTime now = LocalDateTime.now();
        Booking b = new Booking("C1", "R1", "CorrectUser",
                now.minusMinutes(1), now.plusMinutes(20), "Study");
        b.setStatus("CONFIRMED");
        bookingRepo.getAllBookings().add(b);

        Exception ex = assertThrows(Exception.class, () ->
                manager.checkIn("C1", "R1", "WrongUser"));
        assertTrue(ex.getMessage().contains("your own booking"));
    }

    @Test
    void testCheckIn_WrongStatusThrows() {
        LocalDateTime now = LocalDateTime.now();
        Booking b = new Booking("C2", "R1", "U1",
                now.minusMinutes(1), now.plusMinutes(20), "Study");
        b.setStatus("PENDING");

        bookingRepo.getAllBookings().add(b);

        Exception ex = assertThrows(Exception.class, () ->
                manager.checkIn("C2", "R1", "U1"));

        assertTrue(ex.getMessage().contains("Only CONFIRMED"));
    }

    @Test
    void testCheckIn_TooEarlyThrows() {
        LocalDateTime start = LocalDateTime.now().plusMinutes(20);

        Booking b = new Booking("C3", "R1", "U1",
                start, start.plusMinutes(30), "Study");
        b.setStatus("CONFIRMED");

        bookingRepo.getAllBookings().add(b);

        Exception ex = assertThrows(Exception.class, () ->
                manager.checkIn("C3", "R1", "U1"));

        assertTrue(ex.getMessage().contains("Too early"));
    }

    @Test
    void testCheckIn_TooLateThrows() {
        LocalDateTime start = LocalDateTime.now().minusMinutes(40);

        Booking b = new Booking("C4", "R1", "U1",
                start, start.plusMinutes(10), "Study");
        b.setStatus("CONFIRMED");

        bookingRepo.getAllBookings().add(b);

        Exception ex = assertThrows(Exception.class, () ->
                manager.checkIn("C4", "R1", "U1"));

        assertTrue(ex.getMessage().contains("expired"));
    }

    @Test
    void testCheckIn_Successful() throws Exception {
        LocalDateTime start = LocalDateTime.now().minusMinutes(5);

        Booking b = new Booking("C5", "R1", "U99",
                start, start.plusMinutes(30), "Study");
        b.setStatus("CONFIRMED");

        bookingRepo.getAllBookings().add(b);

        manager.checkIn("C5", "R1", "U99");
        assertEquals("IN_USE", manager.getRoomStatus("R1"));
    }


    // =====================================================================
    // OBSERVER TESTS
    // =====================================================================

    @Test
    void testObserverAttachDetach() {
        TestObserver obs = new TestObserver();

        manager.attach(obs);
        manager.notifyObservers();
        assertTrue(obs.wasNotified);

        obs.wasNotified = false;
        manager.detach(obs);
        manager.notifyObservers();
        assertFalse(obs.wasNotified);
    }

    @Test
    void testHandleNoShow_ViaReflection() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        Booking b = new Booking(
                "REF1", "R1", "U1",
                now.minusMinutes(5),
                now.plusMinutes(5),
                "Study"
        );
        b.setStatus("CONFIRMED");

        bookingRepo.getAllBookings().add(b);

        var method = RoomStatusManager.class.getDeclaredMethod(
                "handleNoShow", String.class, String.class
        );
        method.setAccessible(true);

        method.invoke(manager, "REF1", "R1");

        assertTrue(manager.isNoShow("REF1"));
        assertEquals("NO_SHOW", manager.getRoomStatus("R1"));
    }

    @Test
    void testStartNoShowCountdown_TimerActuallyStarts() throws Exception {
        LocalDateTime futureStart = LocalDateTime.now().plusMinutes(5);

        Booking b = new Booking(
                "TS1", "R1", "U1",
                futureStart, futureStart.plusMinutes(30),
                "Study"
        );
        b.setStatus("CONFIRMED");
        bookingRepo.getAllBookings().add(b);

        manager.startNoShowCountdown("TS1", "R1", futureStart);

        var field = RoomStatusManager.class.getDeclaredField("noShowTimers");
        field.setAccessible(true);

        Map<String, ?> timers = (Map<String, ?>) field.get(manager);

        assertTrue(timers.containsKey("TS1"));
    }

    @Test
    void testIsNoShowReturnsFalseForUnknownId() {
        assertFalse(manager.isNoShow("UNKNOWN-ID"));
    }


    // =====================================================================
    // TEST OBSERVER IMPLEMENTATION
    // =====================================================================
    static class TestObserver implements Observer {
        boolean wasNotified = false;
        @Override
        public void update() {
            wasNotified = true;
        }
    }
}
