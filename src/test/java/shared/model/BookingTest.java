package shared.model;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link Booking}.
 *
 * <p>This test suite verifies the core domain behavior of the {@link Booking} entity,
 * including:</p>
 * <ul>
 *     <li>Constructors and default values (status, payment status, deposit amount).</li>
 *     <li>Setter and getter behavior across all mutable fields.</li>
 *     <li>Date/time formatting helpers for UI display.</li>
 *     <li>Time-interval overlap detection.</li>
 *     <li>Extension rules and validation in {@link Booking#extendTo(LocalDateTime)}.</li>
 *     <li>CSV-compatible serialization via {@link Booking#toString()}.</li>
 * </ul>
 *
 * <p>All tests follow the Arrange–Act–Assert structure to keep intent clear and
 * make failures easier to reason about.</p>
 */
public class BookingTest {

    /**
     * Returns a canonical start time used by multiple tests to keep them deterministic.
     *
     * @return a {@link LocalDateTime} representing 2025-01-01 10:00.
     */
    private LocalDateTime start() {
        return LocalDateTime.of(2025, 1, 1, 10, 0);
    }

    /**
     * Returns a canonical end time used by multiple tests to keep them deterministic.
     *
     * @return a {@link LocalDateTime} representing 2025-01-01 11:30.
     */
    private LocalDateTime end() {
        return LocalDateTime.of(2025, 1, 1, 11, 30);
    }

    /**
     * Verifies that the “simple” constructor initializes required fields and
     * applies default values for status, payment status, and deposit amount.
     */
    @Test
    public void simpleConstructor_setsDefaults() {
        // Arrange & Act
        Booking booking = new Booking("B1", "R101", "user@yorku.ca",
                start(), end(), "Study");

        // Assert
        assertEquals("B1", booking.getBookingId());
        assertEquals("R101", booking.getRoomId());
        assertEquals("user@yorku.ca", booking.getUserId());
        assertEquals("Study", booking.getPurpose());
        assertEquals("CONFIRMED", booking.getStatus());
        assertEquals("PENDING", booking.getPaymentStatus());
        assertEquals(0.0, booking.getDepositAmount(), 0.0001);
    }

    /**
     * Verifies that the “full” constructor copies all provided fields verbatim,
     * including non-default status, payment status, and deposit amount.
     */
    @Test
    public void fullConstructor_copiesAllFields() {
        // Arrange & Act
        Booking booking = new Booking("B2", "R202", "user2@yorku.ca",
                start(), end(), "Interview",
                "CANCELLED", "FAILED", 30.5);

        // Assert
        assertEquals("B2", booking.getBookingId());
        assertEquals("R202", booking.getRoomId());
        assertEquals("user2@yorku.ca", booking.getUserId());
        assertEquals("Interview", booking.getPurpose());
        assertEquals("CANCELLED", booking.getStatus());
        assertEquals("FAILED", booking.getPaymentStatus());
        assertEquals(30.5, booking.getDepositAmount(), 0.0001);
    }

    /**
     * Verifies that all setters correctly update the corresponding fields and
     * that getters reflect the new values.
     */
    @Test
    public void settersUpdateFieldsCorrectly() {
        // Arrange
        Booking booking = new Booking("B3", "R101", "user@yorku.ca",
                start(), end(), "Study");

        LocalDateTime newStart = start().plusHours(1);
        LocalDateTime newEnd = end().plusHours(1);

        // Act
        booking.setStatus("PENDING_PAYMENT");
        booking.setPaymentStatus("APPROVED");
        booking.setDepositAmount(25.0);
        booking.setRoomId("R999");
        booking.setPurpose("New purpose");
        booking.setStartTime(newStart);
        booking.setEndTime(newEnd);

        // Assert
        assertEquals("PENDING_PAYMENT", booking.getStatus());
        assertEquals("APPROVED", booking.getPaymentStatus());
        assertEquals(25.0, booking.getDepositAmount(), 0.0001);
        assertEquals("R999", booking.getRoomId());
        assertEquals("New purpose", booking.getPurpose());
        assertEquals(newStart, booking.getStartTime());
        assertEquals(newEnd, booking.getEndTime());
    }

    /**
     * Verifies that {@link Booking#getFormattedDate()} returns a human-readable
     * date string in the expected format (e.g., {@code "Jan 01, 2025"}).
     */
    @Test
    public void getFormattedDate_returnsExpectedFormat() {
        // Arrange
        Booking booking = new Booking("B4", "R101", "user@yorku.ca",
                start(), end(), "Test");

        // Act
        String formatted = booking.getFormattedDate();

        // Assert
        assertEquals("Jan 01, 2025", formatted);
    }

    /**
     * Verifies that {@link Booking#getFormattedTime()} returns the start and end
     * times as a range string (e.g., {@code "10:00 - 11:30"}).
     */
    @Test
    public void getFormattedTime_returnsExpectedRange() {
        // Arrange
        Booking booking = new Booking("B5", "R101", "user@yorku.ca",
                start(), end(), "Test");

        // Act
        String formatted = booking.getFormattedTime();

        // Assert
        assertEquals("10:00 - 11:30", formatted);
    }

    /**
     * Verifies that {@link Booking#overlapsInterval(LocalDateTime, LocalDateTime)}
     * returns {@code true} when the given interval overlaps with the booking’s
     * time window.
     */
    @Test
    public void overlapsInterval_returnsTrueForOverlappingRanges() {
        // Arrange
        Booking booking = new Booking("B6", "R101", "user@yorku.ca",
                start(), end(), "Test");

        LocalDateTime otherStart = start().minusMinutes(30); // starts before
        LocalDateTime otherEnd = start().plusMinutes(10);    // overlaps into booking

        // Act & Assert
        assertTrue(booking.overlapsInterval(otherStart, otherEnd));
    }

    /**
     * Verifies that {@link Booking#overlapsInterval(LocalDateTime, LocalDateTime)}
     * returns {@code false} for clearly non-overlapping intervals.
     */
    @Test
    public void overlapsInterval_returnsFalseForNonOverlappingRanges() {
        // Arrange
        Booking booking = new Booking("B7", "R101", "user@yorku.ca",
                start(), end(), "Test");

        LocalDateTime otherStart = end().plusMinutes(1);  // starts after booking ends
        LocalDateTime otherEnd = end().plusMinutes(30);

        // Act & Assert
        assertFalse(booking.overlapsInterval(otherStart, otherEnd));
    }

    /**
     * Verifies that {@link Booking#overlapsInterval(LocalDateTime, LocalDateTime)}
     * is defensive against {@code null} arguments and returns {@code false}
     * when either start or end (or both) are {@code null}.
     */
    @Test
    public void overlapsInterval_withNullArgumentsReturnsFalse() {
        // Arrange
        Booking booking = new Booking("B8", "R101", "user@yorku.ca",
                start(), end(), "Test");

        // Act & Assert
        assertFalse(booking.overlapsInterval(null, end()));
        assertFalse(booking.overlapsInterval(start(), null));
        assertFalse(booking.overlapsInterval(null, null));
    }

    /**
     * Verifies that {@link Booking#extendTo(LocalDateTime)} updates the end time
     * when provided with a strictly later timestamp.
     */
    @Test
    public void extendTo_withLaterTime_updatesEndTime() {
        // Arrange
        Booking booking = new Booking("B9", "R101", "user@yorku.ca",
                start(), end(), "Test");

        LocalDateTime newEnd = end().plusMinutes(30);

        // Act
        booking.extendTo(newEnd);

        // Assert
        assertEquals(newEnd, booking.getEndTime());
    }

    /**
     * Verifies that {@link Booking#extendTo(LocalDateTime)} throws an
     * {@link IllegalArgumentException} when called with {@code null}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void extendTo_withNull_throwsException() {
        // Arrange
        Booking booking = new Booking("B10", "R101", "user@yorku.ca",
                start(), end(), "Test");

        // Act
        booking.extendTo(null);
    }

    /**
     * Verifies that {@link Booking#extendTo(LocalDateTime)} throws an
     * {@link IllegalArgumentException} when given an end time that is equal
     * to or earlier than the current end time (i.e., not a true extension).
     */
    @Test(expected = IllegalArgumentException.class)
    public void extendTo_withEarlierOrEqualTime_throwsException() {
        // Arrange
        Booking booking = new Booking("B11", "R101", "user@yorku.ca",
                start(), end(), "Test");

        // Act – passing the same end time should violate the “extension” rule
        booking.extendTo(end()); // equal
    }

    /**
     * Verifies that {@link Booking#toString()} produces a CSV-like representation
     * that contains all important fields, and that the deposit amount is formatted
     * with two decimal places.
     */
    @Test
    public void toString_containsAllFieldsInCsvFormat() {
        // Arrange
        Booking booking = new Booking("B12", "R101", "user@yorku.ca",
                start(), end(), "Test",
                "CONFIRMED", "APPROVED", 20.0);

        // Act
        String csv = booking.toString();

        // Assert
        assertTrue(csv.contains("B12"));
        assertTrue(csv.contains("R101"));
        assertTrue(csv.contains("user@yorku.ca"));
        assertTrue(csv.contains("Test"));
        assertTrue(csv.contains("CONFIRMED"));
        assertTrue(csv.contains("APPROVED"));
        assertTrue(csv.endsWith("20.00"));
    }
}
