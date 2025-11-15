package scenario1.test;

import scenario1.controller.UserManager;
import shared.model.User;

public class UserManagerTest {
    public static void main(String[] args) throws Exception {
        // Get singleton instance
        UserManager manager = UserManager.getInstance();

        // Load and display all existing users from CSV
        System.out.println("=== Users loaded from CSV ===");
        for (User u : manager.getAllUsers()) {
            System.out.println(u.toString());
        }

        // Test registration
        System.out.println("\n=== Registering a new user ===");
        manager.register(
                "John Doe",
                "john@yorku.ca",
                "Abc@1234",
                "Student",
                "S1234567"   // <-- New parameter added here
        );


        // Test login
        System.out.println("\n=== Testing Login ===");
        User logged = manager.login("john@yorku.ca", "Abc@1234");
        if (logged != null) {
            System.out.println("Login success for: " + logged.getName());
        } else {
            System.out.println("Login failed.");
        }
    }
}
