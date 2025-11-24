package scenario2.payment;


/**
 * CreditCardPayment – Scenario 2 (Strategy Pattern)
 * ---------------------------------------------------------------------------
 * <p>Concrete implementation of the {@link PaymentStrategy} interface used by
 * the booking workflow. This class simulates credit-card payments for room
 * bookings and extensions.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Provides a plug-and-play payment method under the Strategy Pattern.</li>
 *     <li>Allows BookingManager / PaymentModal to charge users uniformly
 *         without knowing which payment method is used.</li>
 *     <li>Handles basic validation (non-negative amounts).</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Strategy Pattern</b>: Allows interchangeable payment methods such
 *         as CreditCardPayment, DebitPayment, or InstitutionalBilling.</li>
 *     <li>Used by the UI so switching payment types requires no controller
 *         changes.</li>
 * </ul>
 *
 * <h2>Behavior</h2>
 * <ul>
 *     <li>Rejects zero or negative payment amounts.</li>
 *     <li>Returns an approved {@link PaymentResult} for valid charges.</li>
 *     <li>Does not perform real transactions (D3 simulation only).</li>
 * </ul>
 */

public class CreditCardPayment implements PaymentStrategy {

    @Override
    public PaymentResult pay(String userId, double amount, String userType) {

        if (amount <= 0) {
            return new PaymentResult(
                    PaymentResult.Status.FAILED,
                    "Invalid payment amount"
            );
        }

        return new PaymentResult(
                PaymentResult.Status.APPROVED,
                "Payment successful (Credit Card)"
        );
    }
}
