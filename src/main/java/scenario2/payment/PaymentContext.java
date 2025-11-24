package scenario2.payment;

/**
 * PaymentContext – Scenario 2 (Strategy Pattern)
 * ---------------------------------------------------------------------------
 * <p>This class acts as the <b>Context</b> in the Strategy Pattern for the
 * payment subsystem. It holds a reference to a selected {@link PaymentStrategy}
 * and delegates the actual payment execution to that strategy.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Decouples the Booking workflow from specific payment implementations.</li>
 *     <li>Allows dynamic switching between different strategies at runtime
 *         (e.g., credit card, debit, institutional billing).</li>
 *     <li>Provides a single entry point for payment execution.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Strategy Pattern</b>: PaymentContext = Context, PaymentStrategy =
 *         Strategy interface, CreditCardPayment / DebitPayment = Concrete Strategies.</li>
 *     <li>Supports easy extension of new payment types without modifying existing
 *         booking logic.</li>
 * </ul>
 *
 * <h2>Behavior</h2>
 * <ul>
 *     <li>Fails gracefully if no strategy is set.</li>
 *     <li>Delegates validation and processing to the selected strategy.</li>
 *     <li>Returns a standardized {@link PaymentResult} object for UI handling.</li>
 * </ul>
 */


public class PaymentContext {

    private PaymentStrategy strategy;

    public PaymentContext(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public PaymentResult executePayment(String userId, double amount, String userType) {

        if (strategy == null) {
            return new PaymentResult(
                    PaymentResult.Status.FAILED,
                    "No strategy selected"
            );
        }

        return strategy.pay(userId, amount, userType);
    }
}
