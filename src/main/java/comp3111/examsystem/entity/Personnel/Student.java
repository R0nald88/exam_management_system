package comp3111.examsystem.entity.Personnel;

import comp3111.examsystem.tools.Database;

/**
 * Represents a student in the examination system.
 * This class extends the Personnel class and provides
 * specific functionality for managing student information
 * such as username, age, department, name, and password.
 */
public class Student extends Personnel {

    /**
     * Constructs a new Student with default values.
     * @author Li Ching Ho
     */
    public Student() {
        super();
    }

    /**
     * Sets the username for the student.
     * Validates the username length and checks for spaces
     * and existing usernames in the database.
     *
     * @param username The username to set.
     * @throws RuntimeException If the username is invalid or already occupied.
     * @author Li Ching Ho
     */
    @Override
    public void setUsername(String username) {
        Database.validateTextLength(StudentDatabase.STUDENT_USERNAME_LENGTH_LIMIT, username, "username");
        if (username.contains(" ")) {
            throw new RuntimeException("Username should not contain any blank space.");
        }
        // Check if username exist
        if (StudentDatabase.getInstance().existUsername(username)) {
            throw new RuntimeException("Username occupied.");
        }
        super.setUsername(username);
    }

    /**
     * Forcefully sets the username for the student without checking for existing usernames.
     *
     * @param username The username to set.
     * @throws RuntimeException If the username is invalid.
     * @author Wan Hanzhe
     */
    public void forceSetUsername(String username) {
        Database.validateTextLength(StudentDatabase.STUDENT_USERNAME_LENGTH_LIMIT, username, "username");
        if (username.contains(" ")) {
            throw new RuntimeException("Username should not contain any blank space.");
        }
        super.setUsername(username);
    }

    /**
     * Sets the age for the student.
     * Validates the age to ensure it is within the allowed range.
     *
     * @param age The age to set.
     * @throws RuntimeException If the age is out of the allowed range.
     * @author Li Ching Ho
     */
    @Override
    public void setAge(int age) {
        Database.validateNumberRange(
                StudentDatabase.STUDENT_AGE_LOWER_LIMIT,
                StudentDatabase.STUDENT_AGE_UPPER_LIMIT,
                age,
                "student age",
                ""
        );

        super.setAge(age);
    }

    /**
     * Sets the age for the student using a String input.
     * Converts the String to an integer and validates the age.
     *
     * @param age The age as a String to set.
     * @throws RuntimeException If the age is out of the allowed range.
     * @author Li Ching Ho
     */
    public void setAge(String age) {
        Database.validateNumberRange(
                StudentDatabase.STUDENT_AGE_LOWER_LIMIT,
                StudentDatabase.STUDENT_AGE_UPPER_LIMIT,
                age,
                "student age",
                ""
        );

        super.setAge(Integer.parseInt(age));
    }

    /**
     * Sets the department for the student.
     * Validates the department length.
     *
     * @param department The department to set.
     * @throws RuntimeException If the department name is invalid.
     * @author Li Ching Ho
     */
    @Override
    public void setDepartment(String department) {
        Database.validateTextLength(StudentDatabase.STUDENT_DEPT_LENGTH_LIMIT, department, "department");
        super.setDepartment(department);
    }

    /**
     * Sets the name for the student.
     * Validates the name length.
     *
     * @param name The name to set.
     * @throws RuntimeException If the name is invalid.
     * @author Li Ching Ho
     */
    @Override
    public void setName(String name) {
        Database.validateTextLength(StudentDatabase.STUDENT_NAME_LENGTH_LIMIT, name, "student name");
        super.setName(name);
    }

    /**
     * Sets the password for the student.
     * Validates the password length and format.
     *
     * @param password The password to set.
     * @throws RuntimeException If the password is invalid.
     * @author Li Ching Ho
     */
    @Override
    public void setPassword(String password) {
        password = password.trim();
        Database.validateTextLength(StudentDatabase.STUDENT_PASSWORD_LENGTH_LIMIT, password, "password");
        StudentDatabase.validatePassword(password);
        super.setPassword(password);
    }

    /**
     * Confirms the password by comparing it with the stored password.
     *
     * @param pwd The confirmed password to check.
     * @throws RuntimeException If the confirmed password does not match.
     * @author Li Ching Ho
     */
    public void confirmPassword(String pwd) {
        Database.validateTextLength(StudentDatabase.STUDENT_PASSWORD_LENGTH_LIMIT, pwd, "confirmed password");
        if (!getPassword().equals(pwd.trim())) {
            throw new RuntimeException("Confirmed password unmatched.");
        }
    }
}