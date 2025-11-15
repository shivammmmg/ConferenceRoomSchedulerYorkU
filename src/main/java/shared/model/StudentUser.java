package shared.model;

/**
 * Represents a Student user in the system.
 * ------------------------------------------------------------
 * Description:
 *  - Extends the shared User class.
 *  - Automatically assigns the user type as "Student".
 *  - Stores Student ID for identification.
 */
public class StudentUser extends User {

    private String studentId;

    public StudentUser(String name, String email, String password, String studentId) {
        super(name, email, password, "Student");
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return super.toString() + "," + studentId;
    }

    @Override
    public void displayInfo() {
        System.out.println("Student: " + name + " | ID: " + studentId + " | Email: " + email);
    }
}
