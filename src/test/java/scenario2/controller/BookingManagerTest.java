package scenario2.controller;

import org.junit.Before;
import org.junit.Test;
import scenario2.builder.BookingBuilder;
import shared.model.Booking;
import shared.model.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BookingManager} in Scenario 2.
 *
 * <p>This test suite validates the application-level business logic implemented by
 * {@link BookingManager}, without touching CSV I/O. The tests exercise:</p>
 *
 * <ul>
 *   <li>Pricing and deposit rules for different user types.</li>
 *   <li>Repository-based lookup helpers (e.g., {@code findBookingById}).</li>
 *   <li>Status transitions such as cancel, check-in, and no-show handling.</li>
 *   <li>Extension logic (validation + deposit updates).</li>
 *   <li>Lookup of currently active bookings for a room (sensor use case).</li>
 * </ul>
 *
 * <p>Tests follow the Arrange–Act–Assert pattern and use an in-memory
 * {@link BookingRepository} that is cleared before each test to guarantee isolation.</p>
 */
public class BookingManagerTest {

    /** System under test. */
    private BookingManager manager;

    /** Shared in-memory repository used by {@link BookingManager}. */
    private BookingRepository repo;

    /**
     * Resets the singleton {@link BookingManager} and clears the shared
     * {@link BookingRepository} so each test starts from a clean state.
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

    /**
     * Verifies that the hourly rate is correctly returned for all known user types.
     */
    @Test
    public void getHourlyRateForUserType_returnsCorrectRateForKnownTypes() {
        // Act + Assert
        assertEquals(20.0, manager.getHourlyRateForUserType("STUDENT"), 0.0001);
        assertEquals(30.0, manager.getHourlyRateForUserType("FACULTY"), 0.0001);
        assertEquals(40.0, manager.getHourlyRateForUserType("STAFF"), 0.0001);
        assertEquals(50.0, manager.getHourlyRateForUserType("PARTNER"), 0.0001);
    }

    /**
     * Verifies that the hourly-rate lookup:
     * <ul>
     *   <li>is case-insensitive, and</li>
     *   <li>falls back to the student/default rate for null/empty/unknown types.</li>
     * </ul>
     */
    @Test
    public void getHourlyRateForUserType_isCaseInsensitiveAndHasDefault() {
        // Act + Assert
        assertEquals(20.0, manager.getHourlyRateForUserType("student"), 0.0001);
        assertEquals(20.0, manager.getHourlyRateForUserType(null), 0.0001);
        assertEquals(20.0, manager.getHourlyRateForUserType(""), 0.0001);
        assertEquals(20.0, manager.getHourlyRateForUserType("UNKNOWN"), 0.0001);
    }

    /**
     * Verifies that the deposit is based on a single hour of usage, regardless
     * of the requested duration in hours.
     */
    @Test
    public void getDepositForUserTypeAndDuration_usesSingleHourRate() {
        // Act + Assert
        assertEquals(20.0, manager.getDepositForUserTypeAndDuration("STUDENT", 1), 0.0001);
        assertEquals(20.0, manager.getDepositForUserTypeAndDuration("STUDENT", 5), 0.0001);
        assertEquals(50.0, manager.getDepositForUserTypeAndDuration("PARTNER", 10), 0.0001);
    }

    /**
     * Verifies that the estimated total uses max(hours, 1) * hourly rate
     * and handles zero/negative durations by charging at least one hour.
     */
    @Test
    public void getEstimatedTotalForUser_multipliesHoursByRateAndRoundsUp() {
        // Act + Assert
        assertEquals(40.0, manager.getEstimatedTotalForUser("STUDENT", 2), 0.0001);
        assertEquals(20.0, manager.getEstimatedTotalForUser("STUDENT", 0), 0.0001);
        assertEquals(20.0, manager.getEstimatedTotalForUser("STUDENT", -3), 0.0001);
    }

    /**
     * Verifies that the extension deposit uses the single-hour rate
     * independent of the exact minutes requested (as long as it is valid).
     */
    @Test
    public void calculateExtensionDeposit_usesSingleHourRateRegardlessOfMinutes() {
        // Arrange
        double oneHourDeposit = manager.getDepositForUserTypeAndDuration("STUDENT", 1);

        // Act
        double thirtyMinDeposit = manager.calculateExtensionDeposit("STUDENT", 30);
        double sixtyOneMinDeposit = manager.calculateExtensionDeposit("STUDENT", 61);

        // Assert
        assertEquals(oneHourDeposit, thirtyMinDeposit, 0.0001);
        assertEquals(oneHourDeposit, sixtyOneMinDeposit, 0.0001);
    }

    // =====================================================================
    // HELPER TO BUILD BOOKINGS
    // =====================================================================

    /**
     * Helper factory method that builds a valid {@link Booking} with the most
     * common test defaults.
     *
     * @param id      booking identifier.
     * @param roomId  associated room identifier.
     * @param userId  user e-mail identifier.
     * @param start   start date-time.
     * @param end     end date-time.
     * @return a fully initialized {@link Booking} instance.
     */
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

    /**
     * Verifies that {@link BookingManager#findBookingById(String)} returns
     * the booking that exists in the repository.
     */
    @Test
    public void findBookingById_returnsBookingFromRepository() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        repo.getAllBookings().add(newBooking("B100", "R101", "user@yorku.ca", start, end));

        // Act
        Booking found = manager.findBookingById("B100");

        // Assert
        assertNotNull(found);
        assertEquals("B100", found.getBookingId());
    }

    /**
     * Verifies that {@link BookingManager#findBookingById(String)} returns {@code null}
     * when no booking exists for the given identifier.
     */
    @Test
    public void findBookingById_unknownId_returnsNull() {
        // Act + Assert
        assertNull(manager.findBookingById("NOPE"));
    }

    /**
     * Verifies that {@link BookingManager#getAllUserBookings(String)} filters bookings
     * by user identifier in a case-insensitive manner.
     */
    @Test
    public void getAllUserBookings_filtersByUserIdIgnoringCase() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        repo.getAllBookings().add(newBooking("B1", "R101", "user1@yorku.ca",
                now.plusHours(1), now.plusHours(2)));
        repo.getAllBookings().add(newBooking("B2", "R202", "USER1@yorku.ca",
                now.plusHours(3), now.plusHours(4)));
        repo.getAllBookings().add(newBooking("B3", "R303", "other@yorku.ca",
                now.plusHours(1), now.plusHours(2)));

        // Act
        List<Booking> userBookings = manager.getAllUserBookings("user1@yorku.ca");

        // Assert
        assertEquals(2, userBookings.size());
    }

    // =====================================================================
    // canExtendBooking DIRECT TESTS
    // =====================================================================

    /**
     * {@code false} when the booking is {@code null}.
     */
    @Test
    public void canExtendBooking_nullBooking_returnsFalse() {
        // Act + Assert
        assertFalse(manager.canExtendBooking(null, 30));
    }

    /**
     * invalid extension durations (not multiple of 15, zero, or negative).
     */
    @Test
    public void canExtendBooking_invalidExtraMinutes_returnsFalse() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);
        Booking booking = newBooking("BEXT", "R101", "user@yorku.ca", start, end);

        // Act + Assert
        assertFalse(manager.canExtendBooking(booking, 10));   // not multiple of 15
        assertFalse(manager.canExtendBooking(booking, 0));    // zero
        assertFalse(manager.canExtendBooking(booking, -15));  // negative
    }

    // =====================================================================
    // CANCEL / STATUS TRANSITIONS
    // =====================================================================

    /**
     * Verifies that a valid cancellation:
     * <ul>
     *   <li>changes the status to {@code CANCELLED},</li>
     *   <li>marks the payment status as {@code REFUNDED}, and</li>
     *   <li>returns {@code true}.</li>
     * </ul>
     */
    @Test
    public void cancelBooking_setsStatusCancelledAndRefunds() throws Exception {
        // Arrange
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

        // Act
        boolean result = manager.cancelBooking("B1", "user@yorku.ca");

        // Assert
        assertTrue(result);
        assertEquals("CANCELLED", b.getStatus());
        assertEquals("REFUNDED", b.getPaymentStatus());
    }

    /**
     * Verifies that cancelling an already cancelled booking returns {@code false}
     * and does not change the state.
     */
    @Test
    public void cancelBooking_whenAlreadyCancelled_returnsFalse() throws Exception {
        // Arrange
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

        // Act
        boolean result = manager.cancelBooking("B2", "user@yorku.ca");

        // Assert
        assertFalse(result);
    }

    /**
     * Verifies that attempting to cancel a booking as a different user
     * results in an exception (authorization check).
     */
    @Test(expected = Exception.class)
    public void cancelBooking_withDifferentUser_throwsException() throws Exception {
        // Arrange
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

        // Act + Assert (exception expected)
        manager.cancelBooking("B3", "someoneelse@yorku.ca");
    }

    /**
     * Verifies that applying the deposit when the user checks in
     * marks the booking as {@code IN_USE}.
     */
    @Test
    public void applyDepositToFinalCost_marksBookingInUse() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusMinutes(30);
        LocalDateTime end = start.plusHours(1);
        Booking b = newBooking("B10", "R101", "user@yorku.ca", start, end);
        repo.getAllBookings().add(b);

        // Act
        manager.applyDepositToFinalCost("B10");

        // Assert
        assertEquals("IN_USE", b.getStatus());
    }

    /**
     * Verifies that marking a deposit as forfeited sets the booking status to
     * {@code NO_SHOW} and the payment status to {@code FORFEITED}.
     */
    @Test
    public void markDepositForfeited_marksNoShowAndForfeited() {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusHours(2);
        LocalDateTime end = start.plusHours(1);
        Booking b = newBooking("B11", "R101", "user@yorku.ca", start, end);
        repo.getAllBookings().add(b);

        // Act
        manager.markDepositForfeited("B11");

        // Assert
        assertEquals("NO_SHOW", b.getStatus());
        assertEquals("FORFEITED", b.getPaymentStatus());
    }

    // =====================================================================
    // ACTIVE BOOKING LOOKUP
    // =====================================================================

    /**
     * Verifies that {@link BookingManager#getActiveBookingForRoom(String, LocalDateTime)}
     * returns the non-cancelled booking that is active at the given time for the room.
     */
    @Test
    public void getActiveBookingForRoom_returnsBookingInTimeWindow() {
        // Arrange
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

        // Act
        Booking found = manager.getActiveBookingForRoom("R101", now);

        // Assert
        assertNotNull(found);
        assertEquals("B20", found.getBookingId());
    }

    /**
     * Verifies that {@link BookingManager#getActiveBookingForRoom(String, LocalDateTime)}
     * returns {@code null} when there is no active booking matching the room and time.
     */
    @Test
    public void getActiveBookingForRoom_returnsNullIfNoMatching() {
        // Act
        Booking found = manager.getActiveBookingForRoom("R999", LocalDateTime.now());

        // Assert
        assertNull(found);
    }

    // =====================================================================
    // EXTEND BOOKING (userEmail + minutes)
    // =====================================================================

    /**
     * Verifies that a valid booking extension:
     * <ul>
     *   <li>extends the end time by the requested minutes, and</li>
     *   <li>adds the calculated extension deposit to the existing deposit.</li>
     * </ul>
     */
    @Test
    public void extendBooking_addsTimeAndDepositOnSuccess() throws Exception {
        // Arrange
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

        // Act
        Booking extended = manager.extendBooking("B30", "user@yorku.ca", 30, "STUDENT");

        // Assert
        assertEquals(end.plusMinutes(30), extended.getEndTime());
        assertEquals(40.0, extended.getDepositAmount(), 0.0001);
    }

    /**
     * Verifies that trying to extend a booking as a different user
     * fails with an exception (authorization check).
     */
    @Test(expected = Exception.class)
    public void extendBooking_withDifferentUser_throwsException() throws Exception {
        // Arrange
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

        // Act + Assert (exception expected)
        manager.extendBooking("B31", "notowner@yorku.ca", 30, "STUDENT");
    }
}
