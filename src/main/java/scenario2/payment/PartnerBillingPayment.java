package scenario2.payment;

/**
 * PartnerBillingPayment – Scenario 2 (Strategy Pattern)
 * ---------------------------------------------------------------------------
 * <p>Concrete implementation of {@link PaymentStrategy} for institutional/
 * partner billing. Unlike credit-card flows, partner payments are not charged
 * immediately; they rely on organization-side approval.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Supports non–credit-card workflows for Partner accounts.</li>
 *     <li>Implements organization-based billing logic required by Scenario 2.</li>
 *     <li>Ensures consistent response formatting via {@link PaymentResult}.</li>
 * </ul>
 *
 * <h2>Behavior</h2>
 * <ul>
 *     <li>If the amount is below the institutional threshold (50.0), payment
 *         is returned as <b>PENDING</b>.</li>
 *     <li>Large/valid charges are marked as <b>APPROVED</b>.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li>This class is a <b>Concrete Strategy</b> in the Strategy Pattern.</li>
 *     <li>Used by {@code PaymentContext} to handle Partner-specific billing.</li>
 *     <li>Allows booking flow to remain independent of payment type.</li>
 * </ul>
 */


public class PartnerBillingPayment implements PaymentStrategy {

    @Override
    public PaymentResult pay(String userId, double amount, String userType) {

        // Partner bills through institution, not card
        if (amount < 50.0) {
            return new PaymentResult(
                    PaymentResult.Status.PENDING,
                    "Partner billing pending approval"
            );
        }

        return new PaymentResult(
                PaymentResult.Status.APPROVED,
                "Partner billing approved"
        );
    }
}
