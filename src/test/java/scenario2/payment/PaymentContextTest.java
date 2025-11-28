package scenario2.payment;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PaymentContext}.
 *
 * <p>This test suite validates the behavior of the Strategy pattern
 * implementation used for handling different payment mechanisms.
 * In particular, it checks that:</p>
 *
 * <ul>
 *   <li>Executing a payment without a configured strategy fails gracefully.</li>
 *   <li>Concrete strategies ({@link CreditCardPayment}, {@link PartnerBillingPayment}) are
 *       delegated to correctly.</li>
 *   <li>Strategies can be switched at runtime via {@link PaymentContext#setStrategy(PaymentStrategy)}.</li>
 *   <li>Custom strategy implementations can be plugged in without changing the context.</li>
 *   <li>The context calls the underlying strategy exactly once per execution.</li>
 * </ul>
 *
 * <p>All tests follow the Arrange–Act–Assert pattern to keep intent clear and
 * make failures easy to diagnose.</p>
 */
public class PaymentContextTest {

    /**
     * Verifies that when the context is created with a {@code null} strategy,
     * executing a payment returns a {@code FAILED} result with an explanatory
     * message (instead of throwing an exception).
     */
    @Test
    public void executePayment_withoutStrategy_fails() {
        // Arrange
        PaymentContext context = new PaymentContext(null);

        // Act
        PaymentResult result = context.executePayment("user", 20.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.FAILED, result.getStatus());
        assertTrue(result.getMessage().contains("No strategy selected"));
    }

    /**
     * Verifies that when a {@link CreditCardPayment} strategy is configured,
     * a positive amount is approved by the context.
     */
    @Test
    public void executePayment_withCreditCardStrategy_approved() {
        // Arrange
        PaymentContext context = new PaymentContext(new CreditCardPayment());

        // Act
        PaymentResult result = context.executePayment("user", 20.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that a {@link PartnerBillingPayment} strategy returns
     * {@code PENDING} for small amounts (below the partner threshold).
     */
    @Test
    public void executePayment_withPartnerStrategy_pendingForSmallAmounts() {
        // Arrange
        PaymentContext context = new PaymentContext(new PartnerBillingPayment());

        // Act
        PaymentResult result = context.executePayment("user", 10.0, "PARTNER");

        // Assert
        assertEquals(PaymentResult.Status.PENDING, result.getStatus());
    }

    /**
     * Verifies that a {@link PartnerBillingPayment} strategy returns
     * {@code APPROVED} for large amounts (at or above the threshold).
     */
    @Test
    public void executePayment_withPartnerStrategy_approvedForLargeAmounts() {
        // Arrange
        PaymentContext context = new PaymentContext(new PartnerBillingPayment());

        // Act
        PaymentResult result = context.executePayment("user", 100.0, "PARTNER");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that switching strategies at runtime works as expected:
     * <ol>
     *     <li>First, a credit card payment is approved.</li>
     *     <li>Then, after switching to partner billing, a similar amount becomes pending.</li>
     * </ol>
     */
    @Test
    public void setStrategy_switchesFromCreditCardToPartner() {
        // Arrange
        PaymentContext context = new PaymentContext(new CreditCardPayment());

        // Act & Assert (credit card)
        PaymentResult cc = context.executePayment("user", 20.0, "STUDENT");
        assertEquals(PaymentResult.Status.APPROVED, cc.getStatus());

        // Act & Assert (partner billing after runtime switch)
        context.setStrategy(new PartnerBillingPayment());
        PaymentResult partner = context.executePayment("user", 20.0, "PARTNER");
        assertEquals(PaymentResult.Status.PENDING, partner.getStatus());
    }

    /**
     * Verifies that once a strategy is explicitly set to {@code null},
     * executing a payment fails in the same way as starting with no strategy.
     */
    @Test
    public void setStrategy_toNullThenExecute_fails() {
        // Arrange
        PaymentContext context = new PaymentContext(new CreditCardPayment());
        context.setStrategy(null);

        // Act
        PaymentResult result = context.executePayment("user", 10.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.FAILED, result.getStatus());
    }

    /**
     * Verifies that the context does not enforce non-null user IDs and simply
     * forwards the {@code null} to the underlying strategy (which, in this case,
     * still approves the payment).
     */
    @Test
    public void executePayment_allowsNullUserId() {
        // Arrange
        PaymentContext context = new PaymentContext(new CreditCardPayment());

        // Act
        PaymentResult result = context.executePayment(null, 10.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
    }

    /**
     * Verifies that a custom strategy that always returns {@code FAILED} can
     * be plugged into the context and that the context correctly returns the
     * custom failure message.
     */
    @Test
    public void executePayment_withCustomFailingStrategy_returnsFailed() {
        // Arrange: custom strategy that always fails
        PaymentStrategy failingStrategy = new PaymentStrategy() {
            @Override
            public PaymentResult pay(String userId, double amount, String userType) {
                return new PaymentResult(PaymentResult.Status.FAILED, "custom fail");
            }
        };

        PaymentContext context = new PaymentContext(failingStrategy);

        // Act
        PaymentResult result = context.executePayment("user", 10.0, "TYPE");

        // Assert
        assertEquals(PaymentResult.Status.FAILED, result.getStatus());
        assertEquals("custom fail", result.getMessage());
    }

    /**
     * Verifies that a custom strategy that always returns {@code PENDING} can
     * be plugged into the context and that the pending status and message are
     * preserved.
     */
    @Test
    public void executePayment_withCustomPendingStrategy_returnsPending() {
        // Arrange: custom strategy that always returns PENDING
        PaymentStrategy pendingStrategy = new PaymentStrategy() {
            @Override
            public PaymentResult pay(String userId, double amount, String userType) {
                return new PaymentResult(PaymentResult.Status.PENDING, "custom pending");
            }
        };

        PaymentContext context = new PaymentContext(pendingStrategy);

        // Act
        PaymentResult result = context.executePayment("user", 10.0, "TYPE");

        // Assert
        assertEquals(PaymentResult.Status.PENDING, result.getStatus());
        assertEquals("custom pending", result.getMessage());
    }

    /**
     * Verifies that the underlying {@link PaymentStrategy} is invoked exactly
     * once per call to {@link PaymentContext#executePayment(String, double, String)}.
     *
     * <p>This acts as a simple "spy" to ensure there are no duplicate invocations
     * inside the context.</p>
     */
    @Test
    public void executePayment_callsUnderlyingStrategyExactlyOnce() {
        // Arrange: spy-like strategy that counts invocations
        final int[] calls = {0};

        PaymentStrategy spyStrategy = new PaymentStrategy() {
            @Override
            public PaymentResult pay(String userId, double amount, String userType) {
                calls[0]++;
                return new PaymentResult(PaymentResult.Status.APPROVED, "ok");
            }
        };

        PaymentContext context = new PaymentContext(spyStrategy);

        // Act
        PaymentResult result = context.executePayment("user", 5.0, "STUDENT");

        // Assert
        assertEquals(PaymentResult.Status.APPROVED, result.getStatus());
        assertEquals(1, calls[0]);
    }
}
