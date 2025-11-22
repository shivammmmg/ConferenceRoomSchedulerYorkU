package scenario2.payment;

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
