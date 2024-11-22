package comp3111.examsystem.entity.Personnel;

import comp3111.examsystem.tools.Database;

import java.util.List;

/**
 * Database for storing and accessing teacher entity
 * @author Cheugn Tuen King
 */
public class TeacherDatabase extends Database<Teacher> {

    /**
     * Upper limit of teacher age. Set to -1 for removing upper limit
     */
    public static final int TEACHER_AGE_UPPER_LIMIT = 100;

    /**
     * Lower limit of teacher age. Set to -1 for removing lower limit
     */
    public static final int TEACHER_AGE_LOWER_LIMIT = 5;

    /**
     * Length limit of teacher username. Set to -1 for removing length limit
     */
    public static final int TEACHER_USERNAME_LENGTH_LIMIT = -1;

    /**
     * Length limit of teacher name. Set to -1 for removing length limit
     */
    public static final int TEACHER_NAME_LENGTH_LIMIT = -1;

    /**
     * Upper length limit of teacher password. Set to -1 for removing upper length limit
     */
    public static final int TEACHER_PASSWORD_LENGTH_LIMIT = -1;

    /**
     * Length limit of department teacher belongs to. Set to -1 for removing length limit
     */
    public static final int TEACHER_DEPT_LENGTH_LIMIT = -1;

    /**
     * Lower length limit of teacher password
     */
    public static final int TEACHER_PASSWORD_LENGTH_LOWER_LIMIT = 8;


    /**
     * Teacher singleton instance
     */
    private static TeacherDatabase instance = null;


    /**
     * Constructor of TeacherDatabase
     * @author Cheung Tuen King
     */
    private TeacherDatabase() {
        super(Teacher.class);
    }

    /**
     * Access TeacherDatabase singleton
     * @author Cheung Tuen King
     * @return TeacherDatabase singleton
     */
    public static TeacherDatabase getInstance() {
        if (instance == null) instance = new TeacherDatabase();
        return instance;
    }

    /**
     * Validate the username and password for teacher login
     * @author Cheung Tuen King
     * @param username Username for login
     * @param pwd Password for login
     * @throws RuntimeException Username does not exist, or password incorrect
     */
    public void validateTeacherLogin(String username, String pwd) {
        List<Teacher> list = queryByField("username", username);
        if (list.isEmpty()) {
            throw new RuntimeException("Username does not exist.");
        }
        if (!list.getFirst().getPassword().equals(pwd)) {
            throw new RuntimeException("Incorrect password.");
        }
    }

    /**
     * Register the validated teacher entity
     * @author Cheung Tuen King
     * @param teacher Teacher for registration
     */
    public void registerTeacher(Teacher teacher) {
        add(teacher);
    }

    /**
     * Validate the password given.
     * Password should:
     * <li>have length in range (0, TeacherDatabase.TEACHER_PASSWORD_LENGTH_LIMIT)</li>
     * <li>contain no blank space</li>
     * <li>have at least 1 capital letter</li>
     * <li>have at least 1 small letter</li>
     * <li>have at least 1 number</li>
     * <li>have at least 1 special character "!@$%^&*_+<>/-"</li>
     *
     * @author Cheung Tuen King
     * @param pwd Teacher name
     * @throws RuntimeException Any above condition is not met
     */
    public static void validatePassword(String pwd) {
        final String specialChars = "!@$%^&*_+<>/-",
                numChars = "1234567890",
                smallChars = "qwertyuioopasdfghjklzxcvbnm",
                capChars = "QWERTYUIOPASDFGHJKLZXCVBNM";

        pwd = pwd.trim();
        if (pwd.length() < TeacherDatabase.TEACHER_PASSWORD_LENGTH_LOWER_LIMIT) {
            throw new RuntimeException("Password should contain at least " +
                    TeacherDatabase.TEACHER_PASSWORD_LENGTH_LOWER_LIMIT +
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
     * Check if input string contain at least one of the characters in check string
     * @param input Input string for checking
     * @param check List of target characters
     * @return Boolean determining if the input string contain any check string characters
     * @author Cheung Tuen King
     */
    private static boolean containAny(String input, String check) {
        for (char a : input.toCharArray()) {
            if (check.contains(a + "")) return true;
        }
        return false;
    }

    /**
     * Delete all teachers in the database. For testing only
     * @author Cheung Tuen King
     */
    public void deleteAll() {
        for (Teacher t : getAll()) {
            delByKey(t.getId().toString());
        }
    }
}
