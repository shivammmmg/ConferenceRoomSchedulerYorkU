package scenario2.payment;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link CreditCardPayment}.
 *
 * <p>This suite focuses on validating the behaviour of the {@link CreditCardPayment#pay(String, double, String)}
 * method. It verifies that:</p>
 *
 * <ul>
 *   <li>Invalid amounts (negative or zero) are rejected with a {@code FAILED} {@link PaymentResult}.</li>
 *   <li>Positive amounts are approved regardless of user type or user id.</li>
 *   <li>Returned messages are informative and consistent with the status.</li>
 *   <li>The method is deterministic for the same input arguments.</li>
 * </ul>
 *
 * <p>Each test follows the Arrange–Act–Assert (AAA) pattern where applicable.</p>
 */
public class CreditCardPaymentTest {

    /** System under test. */
    private final CreditCardPayment payment = new CreditCardPayment();

    /**
     * Verifies that a negative amount is treated as invalid and results in a
     * {@link PaymentResult.Status#FAILED} status with an explanatory message.
     */
    @Test
    public void pay_negativeAmount_fails() {
        // Act
        PaymentResult result = payment.pay("user", -10.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.FAILED, result.getStatus());
        assertTrue(result.getMessage().toLowerCase().contains("invalid"));
    }

    /**
     * Verifies that a zero amount is also rejected as invalid.
     */
    @Test
    public void pay_zeroAmount_fails() {
        // Act
        PaymentResult result = payment.pay("user", 0.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.FAILED, result.getStatus());
    }

    /**
     * Verifies that a small positive amount is approved.
     */
    @Test
    public void pay_smallPositiveAmount_approved() {
        // Act
        PaymentResult result = payment.pay("user", 1.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that a large positive amount is also approved
     * (i.e., there is no upper bound check in this implementation).
     */
    @Test
    public void pay_largePositiveAmount_approved() {
        // Act
        PaymentResult result = payment.pay("user", 1000.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that a {@code null} user type does not cause the payment
     * to fail as long as the amount is valid.
     */
    @Test
    public void pay_nullUserTypeStillApproved() {
        // Act
        PaymentResult result = payment.pay("user", 10.0, null);

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that an unknown user type is still accepted for a valid amount.
     */
    @Test
    public void pay_unknownUserTypeStillApproved() {
        // Act
        PaymentResult result = payment.pay("user", 10.0, "RANDOM_TYPE");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that an empty user id does not cause the payment to fail
     * as long as the amount is valid.
     */
    @Test
    public void pay_emptyUserIdStillApproved() {
        // Act
        PaymentResult result = payment.pay("", 10.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that the method is deterministic: calling {@code pay} twice with the
     * same arguments returns equivalent results.
     */
    @Test
    public void pay_sameArgsTwice_givesConsistentResult() {
        // Act
        PaymentResult r1 = payment.pay("user", 20.0, "STUDENT");
        PaymentResult r2 = payment.pay("user", 20.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, r1.getStatus());
        assertEquals(PaymentResult.Status.APPROVED, r2.getStatus());
    }

    /**
     * Verifies that when a payment fails, the message mentions an invalid amount,
     * giving feedback that can be surfaced to the user.
     */
    @Test
    public void failedMessageMentionsInvalidAmount() {
        // Act
        PaymentResult result = payment.pay("user", 0.0, "STUDENT");

        // Assert
        assertTrue(result.getMessage().toLowerCase().contains("invalid"));
    }

    /**
     * Verifies that an approved payment includes a reference to
     * "Credit Card" in the success message, making the payment method explicit.
     */
    @Test
    public void approvedMessageMentionsCreditCard() {
        // Act
        PaymentResult result = payment.pay("user", 15.0, "STUDENT");

        // Assert
        assertTrue(result.getMessage().contains("Credit Card"));
    }
}
