package Scenario1.test;

public class EmailVerificationTest {
    public static void main(String[] args) {
        testEmail("student@my.yorku.ca"); // should pass ✅
        testEmail("faculty@yorku.ca");    // should pass ✅
        testEmail("random@gmail.com");    // should fail ❌
    }

    private static void testEmail(String email) {
        try {
            if (!email.endsWith("@yorku.ca") && !email.endsWith("@my.yorku.ca")) {
                throw new Exception("Invalid YorkU email address.");
            }
            System.out.println("✅ " + email + " passed verification.");
        } catch (Exception e) {
            System.out.println("❌ " + email + " failed verification: " + e.getMessage());
        }
    }
}
