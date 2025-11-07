package Scenario1.factory;

import shared.model.*;

/**
 * EECS 3311 - YorkU Conference Room Scheduler
 * ------------------------------------------------------------
 * Class: UserFactory
 * Pattern: Factory Method
 * Scenario: 1 - Registration & Account Management
 * ------------------------------------------------------------
 * Description:
 *  - Creates the correct User subclass (Student, Faculty, Staff, Partner)
 *    based on the account type selected during registration.
 *  - Used by UserManager (Singleton) to instantiate user objects dynamically.
 *
 * Author: Shivam Gupta
 */
public class UserFactory {

    /**
     * Creates and returns the appropriate User subclass instance.
     *
     * @param type     The user type ("Student", "Faculty", "Staff", "Partner")
     * @param name     The user's full name
     * @param email    The user's email address
     * @param password The user's password
     * @return A User object of the matching subclass
     */
    public static User createUser(String type, String name, String email, String password) {
        switch (type.toLowerCase()) {
            case "student":
                return new StudentUser(name, email, password);
            case "faculty":
                return new FacultyUser(name, email, password);
            case "staff":
                return new StaffUser(name, email, password);
            case "partner":
                return new PartnerUser(name, email, password);
            default:
                throw new IllegalArgumentException("Invalid user type: " + type);
        }
    }
}
