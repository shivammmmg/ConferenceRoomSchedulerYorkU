package scenario2.payment;

/**
 * PaymentStrategy – Scenario 2 (Room Booking & Payment)
 * ---------------------------------------------------------------------------
 * <p>Strategy Pattern interface defining a uniform contract for all payment
 * methods in the system. Scenario 2 supports multiple payment mechanisms
 * (credit card, debit, partner billing), and each one implements this
 * interface to provide its own logic.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Decouples payment logic from BookingManager and UI controllers.</li>
 *     <li>Allows new payment methods to be added without modifying existing code.</li>
 *     <li>Ensures every payment flow returns a {@link PaymentResult} object.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li>This is the <b>Strategy interface</b> in the Strategy Pattern.</li>
 *     <li>Concrete strategies include:
 *         <ul>
 *             <li>{@code CreditCardPayment}</li>
 *             <li>{@code PartnerBillingPayment}</li>
 *         </ul>
 *     </li>
 *     <li>Used by {@code PaymentContext} to execute the chosen strategy.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>
 * PaymentContext ctx = new PaymentContext(new CreditCardPayment());
 * PaymentResult result = ctx.executePayment(userId, amount, userType);
 * </pre>
 */


public interface PaymentStrategy {
    PaymentResult pay(String userId, double amount, String userType);
}
