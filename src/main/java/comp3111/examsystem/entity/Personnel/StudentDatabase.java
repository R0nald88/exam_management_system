package comp3111.examsystem.entity.Personnel;

import comp3111.examsystem.tools.Database;

import java.util.List;

public class StudentDatabase extends Database<Student> {
    public static final int STUDENT_AGE_UPPER_LIMIT = 100;
    public static final int STUDENT_AGE_LOWER_LIMIT = 5;
    public static final int STUDENT_USERNAME_LENGTH_LIMIT = -1;
    public static final int STUDENT_NAME_LENGTH_LIMIT = -1;
    public static final int STUDENT_PASSWORD_LENGTH_LIMIT = -1;
    public static final int STUDENT_DEPT_LENGTH_LIMIT = -1;
    public static final int STUDENT_PASSWORD_LENGTH_LOWER_LIMIT = 8;
    private static StudentDatabase instance = null;

    private StudentDatabase(Class<Student> entity) {
        super(entity);
    }

    public static StudentDatabase getInstance() {
        if (instance == null) instance = new StudentDatabase(Student.class);
        return instance;
    }

    public boolean existUsername(String username) {
        return !queryByField("username", username).isEmpty();
    }

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

    private static boolean containAny(String input, String check) {
        for (char a : input.toCharArray()) {
            if (check.contains(a + "")) return true;
        }
        return false;
    }
}