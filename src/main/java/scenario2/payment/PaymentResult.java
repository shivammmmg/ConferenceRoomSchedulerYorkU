package scenario2.payment;

/**
 * PaymentResult – Scenario 2 (Strategy Pattern)
 * ---------------------------------------------------------------------------
 * <p>Value object that represents the outcome of a payment attempt made
 * through a {@link PaymentStrategy}. Every payment strategy returns a
 * PaymentResult, ensuring a uniform contract across credit-card, debit,
 * and partner-billing flows.</p>
 *
 * <h2>Contained Data</h2>
 * <ul>
 *     <li><b>Status</b> – Enumeration indicating APPROVED, FAILED, or PENDING.</li>
 *     <li><b>Message</b> – Human-readable explanation to display in the UI.</li>
 * </ul>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Decouples payment outcome details from the actual booking logic.</li>
 *     <li>Provides clean, structured feedback to BookingFX and PaymentModal.</li>
 *     <li>Ensures all strategies return consistent response objects.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li>This class is the standardized <b>result object</b> used by the
 *         Strategy Pattern in Scenario 2.</li>
 *     <li>Returned by all implementations of {@link PaymentStrategy}.</li>
 * </ul>
 */


public class PaymentResult {

    public enum Status {
        APPROVED,
        FAILED,
        PENDING
    }

    private final Status status;
    private final String message;

    public PaymentResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
