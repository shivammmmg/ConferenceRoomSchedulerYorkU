package scenario2.payment;

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
