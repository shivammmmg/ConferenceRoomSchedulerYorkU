package scenario2.payment;

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
