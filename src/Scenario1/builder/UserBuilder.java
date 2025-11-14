package Scenario1.builder;

import shared.model.*;

/**
 * =============================================================================
 * CLASS: UserBuilder
 * PATTERN: Builder Pattern
 * SCENARIO: 1 — Registration & Account Management
 * =============================================================================
 *
 * PURPOSE:
 *   - Removes complexity from object creation in UserManager.
 *   - Provides a clean, step-by-step way to construct User objects.
 *   - Supports optional fields (e.g., studentId is only used for Student users).
 *   - Makes user creation flexible, readable, and maintainable.
 *
 * HOW IT WORKS:
 *   - Each setter returns the same builder instance → enables method chaining.
 *   - The build() method creates the correct subclass of User based on userType.
 *
 * SUPPORTED USER CLASSES:
 *   - StudentUser
 *   - FacultyUser
 *   - StaffUser
 *   - PartnerUser
 *
 * WHEN THIS GETS USED:
 *   - Called exclusively by UserManager.register()
 *     Example:
 *       new UserBuilder()
 *          .setName(name)
 *          .setEmail(email)
 *          ...
 *          .build();
 *
 * AUTHOR: Shivam Gupta
 * DATE: November 2025
 * =============================================================================
 */
public class UserBuilder {

    // -------------------------------------------------------------------------
    // Fields collected during the building process
    // -------------------------------------------------------------------------
    private String name;
    private String email;
    private String password;
    private String userType;
    private String studentId = "";  // Optional field for Student accounts only


    // -------------------------------------------------------------------------
    // Fluent Builder Setters
    // Return "this" to allow chaining: new UserBuilder().setName(...).setEmail(...)
    // -------------------------------------------------------------------------
    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    public UserBuilder setStudentId(String studentId) {
        this.studentId = studentId;
        return this;
    }


    // -------------------------------------------------------------------------
    // BUILD METHOD
    // Creates and returns the correct User model object.
    // Throws exception if an unsupported user type is passed.
    // -------------------------------------------------------------------------
    public User build() {

        // Students require a studentId
        if ("Student".equalsIgnoreCase(userType)) {
            return new StudentUser(name, email, password, studentId);
        }

        // Faculty User
        if ("Faculty".equalsIgnoreCase(userType)) {
            return new FacultyUser(name, email, password);
        }

        // Staff User
        if ("Staff".equalsIgnoreCase(userType)) {
            return new StaffUser(name, email, password);
        }

        // External Partner User
        if ("Partner".equalsIgnoreCase(userType)) {
            return new PartnerUser(name, email, password);
        }

        // Safety: if we get here, the type is invalid or unknown
        throw new IllegalArgumentException("Unknown user type: " + userType);
    }
}
