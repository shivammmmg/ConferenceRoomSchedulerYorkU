package shared.model;

/**
 * Represents a Staff user in the system.
 * ------------------------------------------------------------
 * Description:
 *  - Extends the shared User class.
 *  - Automatically assigns the user type as "Staff".
 *  - Used by the Factory class to create staff accounts.
 */
public class StaffUser extends User {

    public StaffUser(String name, String email, String password) {
        super(name, email, password, "Staff");
    }
}
