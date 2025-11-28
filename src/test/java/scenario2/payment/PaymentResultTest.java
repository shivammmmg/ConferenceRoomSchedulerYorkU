package scenario2.payment;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PaymentResult} and its nested {@link PaymentResult.Status} enum.
 *
 * <p>This test class verifies that:</p>
 * <ul>
 *   <li>The constructor correctly stores status and message.</li>
 *   <li>Getter methods behave as expected (including {@code null} message).</li>
 *   <li>The {@link PaymentResult.Status} enum has the expected ordinals and values.</li>
 *   <li>Different {@link PaymentResult} instances are independent and do not share state.</li>
 * </ul>
 *
 * <p>All tests follow the Arrange–Act–Assert structure to keep intent clear
 * and make failures easier to debug.</p>
 */
public class PaymentResultTest {

    /**
     * Verifies that the constructor correctly stores the provided status and message
     * and that they are returned unchanged by the corresponding getters.
     */
    @Test
    public void constructorStoresStatusAndMessage() {
        // Arrange & Act
        PaymentResult result = new PaymentResult(PaymentResult.Status.APPROVED, "ok");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
        assertEquals("ok", result.getMessage());
    }

    /**
     * Verifies that a {@link PaymentResult} can be created with a {@code null} message
     * and that the getter returns {@code null} as expected.
     */
    @Test
    public void allowsNullMessage() {
        // Arrange & Act
        PaymentResult result = new PaymentResult(PaymentResult.Status.PENDING, null);

        // Assert
        assertEquals(PaymentResult.Status.PENDING, result.getStatus());
        assertNull(result.getMessage());
    }

    /**
     * Documents and verifies the ordinal position of {@link PaymentResult.Status#APPROVED}.
     * <p>
     * This is a brittle test if the enum order changes, but it can be useful when
     * the ordinal is persisted or relied on elsewhere.
     * </p>
     */
    @Test
    public void approvedStatusOrdinalIsZero() {
        // Act & Assert
        assertEquals(0, PaymentResult.Status.APPROVED.ordinal());
    }

    /**
     * Verifies the ordinal position of {@link PaymentResult.Status#FAILED}.
     */
    @Test
    public void failedStatusOrdinalIsOne() {
        // Act & Assert
        assertEquals(1, PaymentResult.Status.FAILED.ordinal());
    }

    /**
     * Verifies the ordinal position of {@link PaymentResult.Status#PENDING}.
     */
    @Test
    public void pendingStatusOrdinalIsTwo() {
        // Act & Assert
        assertEquals(2, PaymentResult.Status.PENDING.ordinal());
    }

    /**
     * Verifies that {@link PaymentResult.Status#values()} returns all expected statuses
     * and that the size of the enum does not change unexpectedly.
     */
    @Test
    public void valuesContainsAllStatuses() {
        // Act
        PaymentResult.Status[] values = PaymentResult.Status.values();

        // Assert
        assertEquals(3, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(PaymentResult.Status.APPROVED));
        assertTrue(java.util.Arrays.asList(values).contains(PaymentResult.Status.FAILED));
        assertTrue(java.util.Arrays.asList(values).contains(PaymentResult.Status.PENDING));
    }

    /**
     * Verifies that {@link PaymentResult.Status#valueOf(String)} correctly resolves
     * the {@code APPROVED} constant by name.
     */
    @Test
    public void valueOfApprovedReturnsApproved() {
        // Act & Assert
        assertEquals(PaymentResult.Status.APPROVED, PaymentResult.Status.valueOf("APPROVED"));
    }

    /**
     * Verifies that {@link PaymentResult.Status#valueOf(String)} correctly resolves
     * the {@code FAILED} constant by name.
     */
    @Test
    public void valueOfFailedReturnsFailed() {
        // Act & Assert
        assertEquals(PaymentResult.Status.FAILED, PaymentResult.Status.valueOf("FAILED"));
    }

    /**
     * Verifies that {@link PaymentResult.Status#valueOf(String)} correctly resolves
     * the {@code PENDING} constant by name.
     */
    @Test
    public void valueOfPendingReturnsPending() {
        // Act & Assert
        assertEquals(PaymentResult.Status.PENDING, PaymentResult.Status.valueOf("PENDING"));
    }

    /**
     * Verifies that two different {@link PaymentResult} instances hold independent data:
     * changing one instance (or just using different constructor parameters) does not
     * affect the other.
     */
    @Test
    public void differentResultsHaveDifferentData() {
        // Arrange
        PaymentResult r1 = new PaymentResult(PaymentResult.Status.APPROVED, "ok");
        PaymentResult r2 = new PaymentResult(PaymentResult.Status.FAILED, "bad");

        // Assert
        assertNotSame(r1, r2);
        assertEquals(PaymentResult.Status.APPROVED, r1.getStatus());
        assertEquals(PaymentResult.Status.FAILED, r2.getStatus());
        assertEquals("ok", r1.getMessage());
        assertEquals("bad", r2.getMessage());
    }
}
