package scenario2.payment;

public interface PaymentStrategy {
    PaymentResult pay(String userId, double amount, String userType);
}
