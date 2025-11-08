package Scenario1.builder;

import shared.model.*;

/**
 * EECS 3311 - YorkU Conference Room Scheduler
 * ------------------------------------------------------------
 * Class: UserBuilder
 * Pattern: Builder
 * Scenario: 1 - Registration & Account Management
 * ------------------------------------------------------------
 * Description:
 *  - Provides a flexible way to construct User objects step-by-step.
 *  - Used by UserManager (Singleton) to build validated User instances.
 *
 * Author: Shivam Gupta
 * Date: November 2025
 */
public class UserBuilder {

    // attributes that will be set step-by-step
    private String name;
    private String email;
    private String password;
    private String type;

    // === builder methods ===
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

    public UserBuilder setType(String type) {
        this.type = type;
        return this;
    }

    // === build method ===
    public User build() {
        if (name == null || email == null || password == null || type == null) {
            throw new IllegalStateException("Missing required fields to build User.");
        }

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
