package scenario2.payment;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PartnerBillingPayment}.
 *
 * <p>This test suite verifies the threshold-based billing behavior used for
 * partner organizations. Specifically, it validates that:</p>
 *
 * <ul>
 *   <li>Amounts strictly below the billing threshold are marked as
 *       {@link PaymentResult.Status#PENDING}.</li>
 *   <li>Amounts at or above the threshold are marked as
 *       {@link PaymentResult.Status#APPROVED}.</li>
 *   <li>The same threshold logic is applied regardless of user type.</li>
 *   <li>Returned messages clearly communicate the resulting status.</li>
 * </ul>
 *
 * <p>Each test follows the Arrange–Act–Assert pattern to keep the intent clear.</p>
 */
public class PartnerBillingPaymentTest {

    /** System under test. */
    private final PartnerBillingPayment payment = new PartnerBillingPayment();

    /**
     * Verifies that negative amounts are treated as not-yet-billable and
     * thus result in a {@code PENDING} status.
     */
    @Test
    public void pay_negativeAmount_isPending() {
        // Act
        PaymentResult result = payment.pay("partner", -10.0, "PARTNER");

        // Assert
        assertEquals(PaymentResult.Status.PENDING, result.getStatus());
    }

    /**
     * Verifies that a zero amount is also treated as {@code PENDING}.
     */
    @Test
    public void pay_zeroAmount_isPending() {
        // Act
        PaymentResult result = payment.pay("partner", 0.0, "PARTNER");

        // Assert
        assertEquals(PaymentResult.Status.PENDING, result.getStatus());
    }

    /**
     * Verifies that small positive amounts below the billing threshold
     * remain in {@code PENDING} state.
     */
    @Test
    public void pay_smallAmountBelowThreshold_isPending() {
        // Act
        PaymentResult result = payment.pay("partner", 10.0, "PARTNER");

        // Assert
        assertEquals(PaymentResult.Status.PENDING, result.getStatus());
    }

    /**
     * Verifies that an amount just below the threshold is still {@code PENDING}.
     */
    @Test
    public void pay_justBelowThreshold_isPending() {
        // Act
        PaymentResult result = payment.pay("partner", 49.99, "PARTNER");

        // Assert
        assertEquals(PaymentResult.Status.PENDING, result.getStatus());
    }

    /**
     * Verifies that an amount exactly at the threshold is considered
     * approved and transitions to {@code APPROVED}.
     */
    @Test
    public void pay_atThreshold_isApproved() {
        // Act
        PaymentResult result = payment.pay("partner", 50.0, "PARTNER");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that an amount slightly above the threshold is approved.
     */
    @Test
    public void pay_aboveThreshold_isApproved() {
        // Act
        PaymentResult result = payment.pay("partner", 50.01, "PARTNER");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that large amounts are also approved; there is no upper bound
     * check in this implementation.
     */
    @Test
    public void pay_largeAmount_isApproved() {
        // Act
        PaymentResult result = payment.pay("partner", 500.0, "PARTNER");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that the threshold logic does not depend on the user type
     * string and still behaves the same for unknown user types.
     */
    @Test
    public void pay_unknownUserTypeStillUsesThresholdLogic() {
        // Act
        PaymentResult pending = payment.pay("partner", 10.0, "RANDOM");
        PaymentResult approved = payment.pay("partner", 60.0, "RANDOM");

        // Assert
        assertEquals(PaymentResult.Status.PENDING, pending.getStatus());
        assertEquals(PaymentResult.Status.APPROVED, approved.getStatus());
    }

    /**
     * Verifies that a pending result includes a message mentioning that the
     * payment is pending, which is useful for UI feedback.
     */
    @Test
    public void pendingMessageMentionsPending() {
        // Act
        PaymentResult result = payment.pay("partner", 10.0, "PARTNER");

        // Assert
        assertTrue(result.getMessage().toLowerCase().contains("pending"));
    }

    /**
     * Verifies that an approved result includes a message mentioning that
     * the payment is approved.
     */
    @Test
    public void approvedMessageMentionsApproved() {
        // Act
        PaymentResult result = payment.pay("partner", 100.0, "PARTNER");

        // Assert
        assertTrue(result.getMessage().toLowerCase().contains("approved"));
    }
}
