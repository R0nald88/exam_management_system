package comp3111.examsystem.entity.Personnel;

import comp3111.examsystem.tools.Database;

import java.util.List;

/**
 * Represents a database for managing student entities.
 * This class extends the generic Database class and provides
 * methods for student registration, login validation, and
 * username existence checks.
 */
public class StudentDatabase extends Database<Student> {
    public static final int STUDENT_AGE_UPPER_LIMIT = 100;
    public static final int STUDENT_AGE_LOWER_LIMIT = 5;
    public static final int STUDENT_USERNAME_LENGTH_LIMIT = -1;
    public static final int STUDENT_NAME_LENGTH_LIMIT = -1;
    public static final int STUDENT_PASSWORD_LENGTH_LIMIT = -1;
    public static final int STUDENT_DEPT_LENGTH_LIMIT = -1;
    public static final int STUDENT_PASSWORD_LENGTH_LOWER_LIMIT = 8;
    private static StudentDatabase instance = null;

    /**
     * Private constructor to prevent instantiation.
     *
     * @param entity The class type of the Student entity.
     * @author Li Ching Ho
     */
    private StudentDatabase(Class<Student> entity) {
        super(entity);
    }

    /**
     * Gets the singleton instance of the StudentDatabase.
     *
     * @return The instance of StudentDatabase.
     * @author Li Ching Ho
     */
    public static StudentDatabase getInstance() {
        if (instance == null) instance = new StudentDatabase(Student.class);
        return instance;
    }

    /**
     * Checks if a username already exists in the database.
     *
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     * @author Li Ching Ho
     */
    public boolean existUsername(String username) {
        return !queryByField("username", username).isEmpty();
    }

    /**
     * Validates student login by checking the username and password.
     *
     * @param username The username of the student.
     * @param pwd The password of the student.
     * @return The Student object if the login is successful.
     * @throws RuntimeException If the username does not exist or the password is incorrect.
     * @author Li Ching Ho
     */
    public Student validateStudentLogin(String username, String pwd) {
        List<Student> list = queryByField("username", username);
        if (list.isEmpty()) {
            throw new RuntimeException("Username does not exist.");
        }
        else if (!list.getFirst().getPassword().equals(pwd)) {
            throw new RuntimeException("Incorrect password.");
        }
        return list.getFirst();

    }

    /**
     * Registers a new student by adding it to the database.
     *
     * @param student The Student object to register.
     * @author Li Ching Ho
     */
    public void registerStudent(Student student) {
        add(student);
    }

    public static void validatePassword(String pwd) {
        final String specialChars = "!@$%^&*_+<>/-",
                numChars = "1234567890",
                smallChars = "qwertyuioopasdfghjklzxcvbnm",
                capChars = "QWERTYUIOPASDFGHJKLZXCVBNM";

        pwd = pwd.trim();
        if (pwd.length() < StudentDatabase.STUDENT_PASSWORD_LENGTH_LOWER_LIMIT) {
            throw new RuntimeException("Password should contain at least " +
                    StudentDatabase.STUDENT_PASSWORD_LENGTH_LOWER_LIMIT +
                    " characters.");
        }
        if (!containAny(pwd, specialChars)) {
            throw new RuntimeException("Password should contain at least 1 special character \"" + specialChars + "\".");
        }
        if (pwd.contains(" ")) {
            throw new RuntimeException("Password should not contain any blank space.");
        }
        if (!containAny(pwd, numChars)) {
            throw new RuntimeException("Password should contain at least 1 number from 0 to 9.");
        }
        if (!containAny(pwd, smallChars)) {
            throw new RuntimeException("Password should contain at least 1 small letter from a to z");
        }
        if (!containAny(pwd, capChars)) {
            throw new RuntimeException("Password should contain at least 1 capital letter from A to Z");
        }
    }

    /**
     * Checks if the input string contains any character from a specified set.
     *
     * @param input The string to check.
     * @param check The string containing characters to look for.
     * @return True if the input contains any characters from the check string, false otherwise.
     * @author Li Ching Ho
     */
    private static boolean containAny(String input, String check) {
        for (char a : input.toCharArray()) {
            if (check.contains(a + "")) return true;
        }
        return false;
    }
}