package scenario2.controller;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scenario2.builder.BookingBuilder;
import shared.model.Booking;
import shared.model.BookingRepository;
import shared.model.Room;
import shared.model.RoomRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BookingManager} in Scenario 2.
 *
 * <p>This test suite validates the application-level business logic implemented by
 * {@link BookingManager}. Tests are written so that they only touch the
 * TestData CSV files and never modify the production data under {@code data/}.</p>
 */
public class BookingManagerTest {

    /** System under test. */
    private BookingManager manager;

    /** Shared repository used by {@link BookingManager}. */
    private BookingRepository repo;

    // =====================================================================
    // GLOBAL TEST SETUP
    // =====================================================================

    /**
     * Configure the CSV path used by BookingRepository/BookingManager so that
     * tests read/write from {@code TestData/bookings.csv}, not {@code data/bookings.csv}.
     */
    @BeforeClass
    public static void configureCsvForTests() {
        System.setProperty(BookingRepository.BOOKING_CSV_PROPERTY, "TestData/data/bookings.csv");
        BookingRepository.resetForTests();
    }

    /**
     * Reset in-memory state before each test.
     */
    @Before
    public void setUp() {
        manager = BookingManager.getInstance();
        repo = BookingRepository.getInstance();

        // Ensure no state leakage between tests.
        repo.getAllBookings().clear();
    }

    // =====================================================================
    // PRICING LOGIC
    // =====================================================================

    /** Hourly rate for each known user type. */
    @Test
    public void getHourlyRateForUserType_returnsCorrectRateForKnownTypes() {
        assertEquals(20.0, manager.getHourlyRateForUserType("STUDENT"), 0.0001);
        assertEquals(30.0, manager.getHourlyRateForUserType("FACULTY"), 0.0001);
        assertEquals(40.0, manager.getHourlyRateForUserType("STAFF"), 0.0001);
        assertEquals(50.0, manager.getHourlyRateForUserType("PARTNER"), 0.0001);
    }

    /** Case-insensitivity + default rate path. */
    @Test
    public void getHourlyRateForUserType_isCaseInsensitiveAndHasDefault() {
        assertEquals(20.0, manager.getHourlyRateForUserType("student"), 0.0001);
        assertEquals(20.0, manager.getHourlyRateForUserType(null), 0.0001);
        assertEquals(20.0, manager.getHourlyRateForUserType(""), 0.0001);
        assertEquals(20.0, manager.getHourlyRateForUserType("UNKNOWN"), 0.0001);
    }

    /** Deposit uses one hour of rate (ignores duration). */
    @Test
    public void getDepositForUserTypeAndDuration_usesSingleHourRate() {
        assertEquals(20.0, manager.getDepositForUserTypeAndDuration("STUDENT", 1), 0.0001);
        assertEquals(20.0, manager.getDepositForUserTypeAndDuration("STUDENT", 5), 0.0001);
        assertEquals(50.0, manager.getDepositForUserTypeAndDuration("PARTNER", 10), 0.0001);
    }

    /** Estimated total is max(hours,1) * rate. */
    @Test
    public void getEstimatedTotalForUser_multipliesHoursByRateAndRoundsUp() {
        assertEquals(40.0, manager.getEstimatedTotalForUser("STUDENT", 2), 0.0001);
        assertEquals(20.0, manager.getEstimatedTotalForUser("STUDENT", 0), 0.0001);
        assertEquals(20.0, manager.getEstimatedTotalForUser("STUDENT", -3), 0.0001);
    }

    /** Extension deposit equals the single-hour deposit regardless of minutes. */
    @Test
    public void calculateExtensionDeposit_usesSingleHourRateRegardlessOfMinutes() {
        double oneHourDeposit = manager.getDepositForUserTypeAndDuration("STUDENT", 1);

        double thirtyMinDeposit = manager.calculateExtensionDeposit("STUDENT", 30);
        double sixtyOneMinDeposit = manager.calculateExtensionDeposit("STUDENT", 61);

        assertEquals(oneHourDeposit, thirtyMinDeposit, 0.0001);
        assertEquals(oneHourDeposit, sixtyOneMinDeposit, 0.0001);
    }

    // =====================================================================
    // ROOM HELPERS + SEARCH
    // =====================================================================

    /** getAllRooms should never return null. */
    @Test
    public void getAllRooms_returnsNonNullList() {
        assertNotNull(manager.getAllRooms());
    }

    /** getAvailableBuildings should never return null. */
    @Test
    public void getAvailableBuildings_returnsNonNullList() {
        assertNotNull(manager.getAvailableBuildings());
    }

    /** getAvailableEquipment should never return null. */
    @Test
    public void getAvailableEquipment_returnsNonNullList() {
        assertNotNull(manager.getAvailableEquipment());
    }

    /** searchAvailableRooms happy path with loose filters. */
    @Test
    public void searchAvailableRooms_withLooseFilters_returnsList() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);

        assertNotNull(manager.searchAvailableRooms(start, end, 0, null, null));
    }

    /** searchAvailableRooms invalid time ordering throws exception. */
    @Test(expected = IllegalArgumentException.class)
    public void searchAvailableRooms_endBeforeStart_throwsException() {
        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = start.minusHours(1);

        manager.searchAvailableRooms(start, end, 0, null, null);
    }

    /** getRoomById delegates correctly to RoomRepository (if any rooms exist). */
    @Test
    public void getRoomById_returnsRoomFromRepositoryWhenPresent() {
        List<Room> allRooms = RoomRepository.getInstance().getAllRoomsList();
        if (allRooms.isEmpty()) {
            // If there are no rooms in the CSV, just ensure it returns null for a bogus id.
            assertNull(manager.getRoomById("NON_EXISTENT_ROOM"));
        } else {
            Room first = allRooms.get(0);
            Room byId = manager.getRoomById(first.getRoomId());
            assertNotNull(byId);
            assertEquals(first.getRoomId(), byId.getRoomId());
        }
    }

    // =====================================================================
    // HELPER TO BUILD BOOKINGS
    // =====================================================================

    /** Helper factory for a common Booking. */
    private Booking newBooking(String id,
                               String roomId,
                               String userId,
                               LocalDateTime start,
                               LocalDateTime end) {
        return new BookingBuilder()
                .setBookingId(id)
                .setRoomId(roomId)
                .setUserId(userId)
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Test")
                .build();
    }

    // =====================================================================
    // LOOKUP HELPERS
    // =====================================================================

    @Test
    public void findBookingById_returnsBookingFromRepository() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        repo.getAllBookings().add(newBooking("B100", "R101", "user@yorku.ca", start, end));

        Booking found = manager.findBookingById("B100");

        assertNotNull(found);
        assertEquals("B100", found.getBookingId());
    }

    @Test
    public void findBookingById_unknownId_returnsNull() {
        assertNull(manager.findBookingById("NOPE"));
    }

    @Test
    public void getAllUserBookings_filtersByUserIdIgnoringCase() {
        LocalDateTime now = LocalDateTime.now();

        repo.getAllBookings().add(newBooking("B1", "R101", "user1@yorku.ca",
                now.plusHours(1), now.plusHours(2)));
        repo.getAllBookings().add(newBooking("B2", "R202", "USER1@yorku.ca",
                now.plusHours(3), now.plusHours(4)));
        repo.getAllBookings().add(newBooking("B3", "R303", "other@yorku.ca",
                now.plusHours(1), now.plusHours(2)));

        List<Booking> userBookings = manager.getAllUserBookings("user1@yorku.ca");

        assertEquals(2, userBookings.size());
    }

    @Test
    public void getAllUserBookings_unknownUser_returnsEmptyList() {
        assertTrue(manager.getAllUserBookings("nobody@yorku.ca").isEmpty());
    }

    // =====================================================================
    // canExtendBooking DIRECT TESTS
    // =====================================================================

    @Test
    public void canExtendBooking_nullBooking_returnsFalse() {
        assertFalse(manager.canExtendBooking(null, 30));
    }

    @Test
    public void canExtendBooking_invalidExtraMinutes_returnsFalse() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);
        Booking booking = newBooking("BEXT", "R101", "user@yorku.ca", start, end);

        assertFalse(manager.canExtendBooking(booking, 10));   // not multiple of 15
        assertFalse(manager.canExtendBooking(booking, 0));    // zero
        assertFalse(manager.canExtendBooking(booking, -15));  // negative
    }

    /** Too close to end time (inside 10-min buffer) should be false. */
    @Test
    public void canExtendBooking_tooCloseToEndTime_returnsFalse() {
        LocalDateTime endSoon = LocalDateTime.now().plusMinutes(5);
        LocalDateTime start = endSoon.minusHours(1);
        Booking booking = newBooking("BTIME", "R101", "user@yorku.ca", start, endSoon);

        assertFalse(manager.canExtendBooking(booking, 15));
    }

    /** Conflict with another booking in same room should be false. */
    @Test
    public void canExtendBooking_conflictWithOtherBooking_returnsFalse() {
        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = start.plusHours(1);
        Booking booking = newBooking("BMAIN", "R-CONFLICT", "user@yorku.ca", start, end);

        LocalDateTime otherStart = end; // immediate next slot
        LocalDateTime otherEnd = otherStart.plusMinutes(30);
        Booking other = newBooking("BOTHER", "R-CONFLICT", "other@yorku.ca", otherStart, otherEnd);

        repo.getAllBookings().add(booking);
        repo.getAllBookings().add(other);

        assertFalse(manager.canExtendBooking(booking, 30));
    }

    // =====================================================================
    // CANCEL / STATUS TRANSITIONS
    // =====================================================================

    @Test
    public void cancelBooking_setsStatusCancelledAndRefunds() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);

        Booking b = new BookingBuilder()
                .setBookingId("B1")
                .setRoomId("R101")
                .setUserId("user@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Test")
                .setPaymentStatus("APPROVED")
                .setDepositAmount(20.0)
                .build();

        repo.getAllBookings().add(b);

        boolean result = manager.cancelBooking("B1", "user@yorku.ca");

        assertTrue(result);
        assertEquals("CANCELLED", b.getStatus());
        assertEquals("REFUNDED", b.getPaymentStatus());
    }

    @Test
    public void cancelBooking_whenAlreadyCancelled_returnsFalse() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);

        Booking b = new BookingBuilder()
                .setBookingId("B2")
                .setRoomId("R101")
                .setUserId("user@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Test")
                .setStatus("CANCELLED")
                .build();

        repo.getAllBookings().add(b);

        boolean result = manager.cancelBooking("B2", "user@yorku.ca");

        assertFalse(result);
    }

    @Test(expected = Exception.class)
    public void cancelBooking_withDifferentUser_throwsException() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);

        Booking b = new BookingBuilder()
                .setBookingId("B3")
                .setRoomId("R101")
                .setUserId("owner@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Test")
                .build();

        repo.getAllBookings().add(b);

        manager.cancelBooking("B3", "someoneelse@yorku.ca");
    }

    @Test(expected = Exception.class)
    public void cancelBooking_unknownId_throwsException() throws Exception {
        manager.cancelBooking("NO_SUCH_BOOKING", "user@yorku.ca");
    }

    @Test
    public void applyDepositToFinalCost_marksBookingInUse() {
        LocalDateTime start = LocalDateTime.now().minusMinutes(30);
        LocalDateTime end = start.plusHours(1);
        Booking b = newBooking("B10", "R101", "user@yorku.ca", start, end);
        repo.getAllBookings().add(b);

        manager.applyDepositToFinalCost("B10");

        assertEquals("IN_USE", b.getStatus());
    }

    @Test
    public void markDepositForfeited_marksNoShowAndForfeited() {
        LocalDateTime start = LocalDateTime.now().minusHours(2);
        LocalDateTime end = start.plusHours(1);
        Booking b = newBooking("B11", "R101", "user@yorku.ca", start, end);
        repo.getAllBookings().add(b);

        manager.markDepositForfeited("B11");

        assertEquals("NO_SHOW", b.getStatus());
        assertEquals("FORFEITED", b.getPaymentStatus());
    }

    // =====================================================================
    // ACTIVE BOOKING LOOKUP
    // =====================================================================

    @Test
    public void getActiveBookingForRoom_returnsBookingInTimeWindow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusMinutes(10);
        LocalDateTime end = now.plusMinutes(50);

        Booking active = newBooking("B20", "R101", "user@yorku.ca", start, end);

        Booking cancelled = new BookingBuilder()
                .setBookingId("B21")
                .setRoomId("R101")
                .setUserId("user@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Cancelled")
                .setStatus("CANCELLED")
                .build();

        repo.getAllBookings().add(active);
        repo.getAllBookings().add(cancelled);

        Booking found = manager.getActiveBookingForRoom("R101", now);

        assertNotNull(found);
        assertEquals("B20", found.getBookingId());
    }

    @Test
    public void getActiveBookingForRoom_returnsNullIfNoMatching() {
        Booking found = manager.getActiveBookingForRoom("R999", LocalDateTime.now());
        assertNull(found);
    }

    // =====================================================================
    // EXTEND BOOKING (userEmail + minutes)
    // =====================================================================

    @Test
    public void extendBooking_addsTimeAndDepositOnSuccess() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);

        Booking booking = new BookingBuilder()
                .setBookingId("B30")
                .setRoomId("R101")
                .setUserId("user@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Extend me")
                .setStatus("CONFIRMED")
                .setDepositAmount(20.0)
                .build();

        repo.getAllBookings().add(booking);

        Booking extended = manager.extendBooking("B30", "user@yorku.ca", 30, "STUDENT");

        assertEquals(end.plusMinutes(30), extended.getEndTime());
        assertEquals(40.0, extended.getDepositAmount(), 0.0001);
    }

    @Test(expected = Exception.class)
    public void extendBooking_withDifferentUser_throwsException() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);

        Booking booking = new BookingBuilder()
                .setBookingId("B31")
                .setRoomId("R101")
                .setUserId("owner@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Extend me")
                .build();

        repo.getAllBookings().add(booking);

        manager.extendBooking("B31", "notowner@yorku.ca", 30, "STUDENT");
    }

    @Test(expected = Exception.class)
    public void extendBooking_withNonActiveStatus_throwsException() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);

        Booking booking = new BookingBuilder()
                .setBookingId("BSTAT")
                .setRoomId("R101")
                .setUserId("user@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Test")
                .setStatus("CANCELLED")
                .build();

        repo.getAllBookings().add(booking);

        manager.extendBooking("BSTAT", "user@yorku.ca", 30, "STUDENT");
    }

    // =====================================================================
    // LEGACY FILE-BASED EXTENSION METHODS
    // =====================================================================

    @Test
    public void extendBooking_fileBased_extendsAndAddsDeposit() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);

        Booking booking = new BookingBuilder()
                .setBookingId("BFILE1")
                .setRoomId("RFILE")
                .setUserId("user@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("File-based")
                .setStatus("CONFIRMED")
                .setDepositAmount(10.0)
                .build();

        repo.getAllBookings().clear();
        repo.getAllBookings().add(booking);
        repo.saveAll(); // writes to TestData/bookings.csv

        LocalDateTime newEnd = end.plusHours(1);

        Booking updated = manager.extendBooking("BFILE1", newEnd, 5.0);

        assertEquals(newEnd, updated.getEndTime());
        assertEquals(15.0, updated.getDepositAmount(), 0.0001);
    }

    @Test
    public void extendBookingOneSlot_fileBased_reusesCoreExtensionLogic() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = start.plusHours(1);

        Booking booking = new BookingBuilder()
                .setBookingId("BFILE2")
                .setRoomId("RFILE")
                .setUserId("user@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("File slot")
                .setStatus("CONFIRMED")
                .setDepositAmount(20.0)
                .build();

        repo.getAllBookings().clear();
        repo.getAllBookings().add(booking);
        repo.saveAll();

        Booking updated = manager.extendBookingOneSlot("BFILE2", 60, 5.0);

        assertEquals(end.plusMinutes(60), updated.getEndTime());
        assertEquals(25.0, updated.getDepositAmount(), 0.0001);
    }

    // =====================================================================
    // BOOKING CREATION + EDIT
    // =====================================================================

    @Test
    public void bookRoom_createsConfirmedBookingAndPersists() throws Exception {
        List<Room> rooms = RoomRepository.getInstance().getAllRoomsList();
        if (rooms.isEmpty()) {
            // If no rooms exist in data, we cannot meaningfully test booking creation.
            return;
        }

        Room room = rooms.get(0);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        int beforeSize = repo.getAllBookings().size();

        Booking booking = manager.bookRoom(
                room.getRoomId(),
                "student@yorku.ca",
                start,
                end,
                "Test meeting",
                "STUDENT"
        );

        assertNotNull(booking);
        assertEquals("CONFIRMED", booking.getStatus());
        assertEquals("APPROVED", booking.getPaymentStatus());
        assertEquals(20.0, booking.getDepositAmount(), 0.0001);
        assertEquals(beforeSize + 1, repo.getAllBookings().size());
    }

    @Test(expected = Exception.class)
    public void bookRoom_withNonExistingRoom_throwsException() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);

        manager.bookRoom("NON_EXISTENT_ROOM",
                "student@yorku.ca",
                start,
                end,
                "Test",
                "STUDENT");
    }

    @Test
    public void editBooking_updatesFieldsAndSaves() throws Exception {
        List<Room> rooms = RoomRepository.getInstance().getAllRoomsList();
        if (rooms.isEmpty()) {
            return;
        }
        String roomId = rooms.get(0).getRoomId();

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);

        Booking booking = new BookingBuilder()
                .setBookingId("BEDIT")
                .setRoomId(roomId)
                .setUserId("user@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Old purpose")
                .setStatus("CONFIRMED")
                .build();

        repo.getAllBookings().add(booking);

        LocalDateTime newStart = start.plusHours(1);
        LocalDateTime newEnd = newStart.plusHours(1);
        String newPurpose = "Updated";

        manager.editBooking("BEDIT",
                "user@yorku.ca",
                roomId,
                newStart,
                newEnd,
                newPurpose);

        assertEquals(roomId, booking.getRoomId());
        assertEquals(newStart, booking.getStartTime());
        assertEquals(newEnd, booking.getEndTime());
        assertEquals(newPurpose, booking.getPurpose());
    }

    @Test(expected = Exception.class)
    public void editBooking_conflictingBooking_throwsException() throws Exception {
        List<Room> rooms = RoomRepository.getInstance().getAllRoomsList();
        if (rooms.isEmpty()) {
            throw new Exception("No rooms available for conflict test");
        }
        String roomId = rooms.get(0).getRoomId();

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);

        Booking original = new BookingBuilder()
                .setBookingId("BEDIT2")
                .setRoomId(roomId)
                .setUserId("user@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Original")
                .setStatus("CONFIRMED")
                .build();

        Booking other = new BookingBuilder()
                .setBookingId("BCLASH")
                .setRoomId(roomId)
                .setUserId("other@yorku.ca")
                .setStartTime(start.plusMinutes(30))
                .setEndTime(end.plusHours(1))
                .setPurpose("Other")
                .setStatus("CONFIRMED")
                .build();

        repo.getAllBookings().add(original);
        repo.getAllBookings().add(other);

        LocalDateTime newStart = start.plusMinutes(45);
        LocalDateTime newEnd = newStart.plusHours(1);

        manager.editBooking("BEDIT2",
                "user@yorku.ca",
                roomId,
                newStart,
                newEnd,
                "Updated");
    }
}
