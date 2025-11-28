package scenario2.builder;

import org.junit.Test;
import shared.model.Booking;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BookingBuilder}.
 *
 * <p>This test suite focuses on validating the behaviour of the builder:
 * <ul>
 *   <li>All required fields must be present before {@link BookingBuilder#build()} is called.</li>
 *   <li>Default values for status, payment status, and deposit are applied correctly.</li>
 *   <li>Custom values override the defaults when provided.</li>
 *   <li>The fluent API of the builder returns the same instance to allow chaining.</li>
 * </ul>
 * </p>
 *
 * <p>The tests follow the Arrange–Act–Assert structure and are written using JUnit 4.</p>
 */
public class BookingBuilderTest {

    /**
     * Creates a {@link BookingBuilder} pre-populated with a valid minimal booking.
     *
     * <p>This helper method centralizes the construction of a "happy path" builder so that
     * individual tests only adjust the specific fields they care about. This reduces duplication
     * and makes test intent clearer.</p>
     *
     * @return a {@link BookingBuilder} instance with all required fields initialized.
     */
    private BookingBuilder newDefaultBuilder() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 11, 0);

        return new BookingBuilder()
                .setBookingId("B1")
                .setRoomId("R101")
                .setUserId("user@yorku.ca")
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose("Study session");
    }

    /**
     * Verifies that when all required fields are set, the builder:
     * <ul>
     *   <li>Copies those values into the resulting {@link Booking}.</li>
     *   <li>Applies default values for status, payment status, and deposit.</li>
     * </ul>
     */
    @Test
    public void build_withAllRequiredFields_copiesValuesAndDefaults() {
        // Arrange
        BookingBuilder builder = newDefaultBuilder();

        // Act
        Booking booking = builder.build();

        // Assert
        assertEquals("B1", booking.getBookingId());
        assertEquals("R101", booking.getRoomId());
        assertEquals("user@yorku.ca", booking.getUserId());
        assertEquals("Study session", booking.getPurpose());
        assertEquals("CONFIRMED", booking.getStatus());
        assertEquals("PENDING", booking.getPaymentStatus());
        assertEquals(0.0, booking.getDepositAmount(), 0.0001);
    }

    /**
     * Verifies that a {@code null} purpose is normalized to an empty string in the built booking.
     *
     * <p>This ensures downstream code does not have to handle {@code null} checks for this field
     * and can treat it as a non-null string.</p>
     */
    @Test
    public void build_withNullPurpose_setsEmptyStringPurpose() {
        // Arrange
        BookingBuilder builder = newDefaultBuilder().setPurpose(null);

        // Act
        Booking booking = builder.build();

        // Assert
        assertNotNull("Purpose should never be null", booking.getPurpose());
        assertEquals("", booking.getPurpose());
    }

    /**
     * Verifies that attempting to build a booking without a booking ID
     * results in an {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void build_withoutBookingId_throwsIllegalStateException() {
        // Arrange
        BookingBuilder builder = newDefaultBuilder().setBookingId(null);

        // Act + Assert (exception expected)
        builder.build();
    }

    /**
     * Verifies that attempting to build a booking without a room ID
     * results in an {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void build_withoutRoomId_throwsIllegalStateException() {
        // Arrange
        BookingBuilder builder = newDefaultBuilder().setRoomId(null);

        // Act + Assert (exception expected)
        builder.build();
    }

    /**
     * Verifies that attempting to build a booking without a user ID
     * results in an {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void build_withoutUserId_throwsIllegalStateException() {
        // Arrange
        BookingBuilder builder = newDefaultBuilder().setUserId(null);

        // Act + Assert (exception expected)
        builder.build();
    }

    /**
     * Verifies that attempting to build a booking without a start time
     * results in an {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void build_withoutStartTime_throwsIllegalStateException() {
        // Arrange
        BookingBuilder builder = newDefaultBuilder().setStartTime(null);

        // Act + Assert (exception expected)
        builder.build();
    }

    /**
     * Verifies that attempting to build a booking without an end time
     * results in an {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void build_withoutEndTime_throwsIllegalStateException() {
        // Arrange
        BookingBuilder builder = newDefaultBuilder().setEndTime(null);

        // Act + Assert (exception expected)
        builder.build();
    }

    /**
     * Verifies that a custom status set on the builder overrides the default value
     * in the resulting {@link Booking}.
     */
    @Test
    public void build_withCustomStatus_usesCustomStatus() {
        // Arrange
        BookingBuilder builder = newDefaultBuilder().setStatus("PENDING_PAYMENT");

        // Act
        Booking booking = builder.build();

        // Assert
        assertEquals("PENDING_PAYMENT", booking.getStatus());
    }

    /**
     * Verifies that a custom payment status set on the builder overrides the default value
     * in the resulting {@link Booking}.
     */
    @Test
    public void build_withCustomPaymentStatus_usesCustomPaymentStatus() {
        // Arrange
        BookingBuilder builder = newDefaultBuilder().setPaymentStatus("APPROVED");

        // Act
        Booking booking = builder.build();

        // Assert
        assertEquals("APPROVED", booking.getPaymentStatus());
    }

    /**
     * Verifies that a custom deposit amount set on the builder is copied into the
     * resulting {@link Booking}.
     */
    @Test
    public void build_withCustomDeposit_setsDepositAmount() {
        // Arrange
        BookingBuilder builder = newDefaultBuilder().setDepositAmount(25.5);

        // Act
        Booking booking = builder.build();

        // Assert
        assertEquals(25.5, booking.getDepositAmount(), 0.0001);
    }

    /**
     * Verifies that the builder's fluent setter methods return the same instance.
     *
     * <p>This is important for a fluent API because it allows chaining calls such as:
     * {@code new BookingBuilder().setBookingId(...).setRoomId(...);}</p>
     */
    @Test
    public void fluentSetters_returnSameBuilderInstance() {
        // Arrange
        BookingBuilder builder = new BookingBuilder();

        // Act
        BookingBuilder result = builder
                .setBookingId("B1")
                .setRoomId("R101")
                .setUserId("user@yorku.ca");

        // Assert
        assertSame("Fluent setters should return the same builder instance", builder, result);
    }
}
