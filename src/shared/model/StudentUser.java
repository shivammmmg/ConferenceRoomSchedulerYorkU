package shared.model;

/**
 * Represents a Student user in the system.
 * ------------------------------------------------------------
 * Description:
 *  - Extends the shared User class.
 *  - Automatically assigns the user type as "Student".
 *  - Used by the Factory class to create student accounts.
 */
public class StudentUser extends User {

    public StudentUser(String name, String email, String password) {
        super(name, email, password, "Student");
    }
}
