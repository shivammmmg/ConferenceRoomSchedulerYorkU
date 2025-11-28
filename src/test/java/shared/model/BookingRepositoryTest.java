package shared.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BookingRepository}.
 *
 * <p>This test suite verifies the in-memory behavior of the singleton
 * {@link BookingRepository}, specifically:</p>
 * <ul>
 *     <li>That the internal booking list is mutable and shared.</li>
 *     <li>Lookup by identifier via {@link BookingRepository#findById(String)}.</li>
 *     <li>Room-based filtering via {@link BookingRepository#getBookingsForRoom(String)}.</li>
 * </ul>
 *
 * <p>Tests are intentionally isolated from CSV I/O to keep them deterministic and
 * fast. We only interact with the list returned from
 * {@link BookingRepository#getAllBookings()}.</p>
 */
public class BookingRepositoryTest {

    private BookingRepository repo;

    /**
     * Resets the singleton repository’s in-memory state before each test case
     * to ensure tests do not interfere with one another.
     */
    @Before
    public void setUp() {
        repo = BookingRepository.getInstance();
        repo.getAllBookings().clear();
    }

    /**
     * Creates a simple {@link Booking} instance with fixed start/end times and user,
     * parameterized by booking ID and room ID.
     *
     * @param id     booking identifier to assign.
     * @param roomId room identifier to assign.
     * @return a new {@link Booking} for testing purposes.
     */
    private Booking newBooking(String id, String roomId) {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 11, 0);

        return new Booking(id, roomId, "user@yorku.ca", start, end, "Test");
    }

    /**
     * Verifies that {@link BookingRepository#getAllBookings()} returns a mutable
     * list that can be modified by callers, and that the modifications are
     * reflected when the list is retrieved again.
     */
    @Test
    public void getAllBookings_returnsMutableList() {
        // Arrange
        assertEquals("Repository should be empty at the start of the test",
                0, repo.getAllBookings().size());

        // Act – modify the list directly
        repo.getAllBookings().add(newBooking("B1", "R101"));
        repo.getAllBookings().add(newBooking("B2", "R202"));

        // Assert – size should reflect the added bookings
        assertEquals(2, repo.getAllBookings().size());
    }

    /**
     * Verifies that {@link BookingRepository#findById(String)} returns the booking
     * with the matching identifier when it exists in the repository.
     */
    @Test
    public void findById_returnsMatchingBooking() {
        // Arrange
        Booking b1 = newBooking("B1", "R101");
        Booking b2 = newBooking("B2", "R202");
        repo.getAllBookings().add(b1);
        repo.getAllBookings().add(b2);

        // Act
        Booking found = repo.findById("B2");

        // Assert
        assertNotNull("Expected a booking with ID B2 to be found", found);
        assertEquals("B2", found.getBookingId());
        assertEquals("R202", found.getRoomId());
    }

    /**
     * Verifies that {@link BookingRepository#findById(String)} returns {@code null}
     * when no booking exists for the given identifier.
     */
    @Test
    public void findById_unknownId_returnsNull() {
        // Arrange
        repo.getAllBookings().add(newBooking("B1", "R101"));

        // Act & Assert
        assertNull("Unknown ID should return null", repo.findById("NOPE"));
    }

    /**
     * Verifies that {@link BookingRepository#getBookingsForRoom(String)} returns
     * only the bookings whose room ID matches the given parameter.
     */
    @Test
    public void getBookingsForRoom_filtersByRoomId() {
        // Arrange
        repo.getAllBookings().add(newBooking("B1", "R101"));
        repo.getAllBookings().add(newBooking("B2", "R202"));
        repo.getAllBookings().add(newBooking("B3", "R101"));

        // Act
        List<Booking> r101Bookings = repo.getBookingsForRoom("R101");

        // Assert
        assertEquals("Expected two bookings for room R101", 2, r101Bookings.size());
        assertTrue("All returned bookings should have roomId = R101",
                r101Bookings.stream().allMatch(b -> "R101".equals(b.getRoomId())));
    }

    /**
     * Verifies that {@link BookingRepository#getBookingsForRoom(String)} returns
     * an empty list (not {@code null}) when no bookings exist for the given room.
     */
    @Test
    public void getBookingsForRoom_noMatchesReturnsEmptyList() {
        // Arrange
        repo.getAllBookings().add(newBooking("B1", "R101"));

        // Act
        List<Booking> r999Bookings = repo.getBookingsForRoom("R999");

        // Assert
        assertNotNull("Method should never return null; prefer empty list",
                r999Bookings);
        assertTrue("No bookings should be returned for unknown room ID",
                r999Bookings.isEmpty());
    }
}
