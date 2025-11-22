package scenario2.payment;

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
