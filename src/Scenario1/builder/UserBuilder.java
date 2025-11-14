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
 *  - Builds User objects step by step.
 *  - Supports setting optional fields (like studentId).
 *  - Used by UserManager for clean object creation.
 *
 * Author: Shivam Gupta
 * Date: November 2025
 */
public class UserBuilder {

    private String name;
    private String email;
    private String password;
    private String userType;
    private String studentId = ""; // optional

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

    public User build() {

        if ("Student".equalsIgnoreCase(userType)) {
            return new StudentUser(name, email, password, studentId);
        }

        if ("Faculty".equalsIgnoreCase(userType)) {
            return new FacultyUser(name, email, password);
        }

        if ("Staff".equalsIgnoreCase(userType)) {
            return new StaffUser(name, email, password);
        }

        if ("Partner".equalsIgnoreCase(userType)) {
            return new PartnerUser(name, email, password);
        }

        throw new IllegalArgumentException("Unknown user type: " + userType);
    }
}