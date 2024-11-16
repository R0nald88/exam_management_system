package comp3111.examsystem.entity.Personnel;

import comp3111.examsystem.tools.Database;

import java.util.List;

public class TeacherDatabase extends Database<Teacher> {
    public static final int TEACHER_AGE_UPPER_LIMIT = 100;
    public static final int TEACHER_AGE_LOWER_LIMIT = 5;
    public static final int TEACHER_USERNAME_LENGTH_LIMIT = -1;
    public static final int TEACHER_NAME_LENGTH_LIMIT = -1;
    public static final int TEACHER_PASSWORD_LENGTH_LIMIT = -1;
    public static final int TEACHER_DEPT_LENGTH_LIMIT = -1;
    public static final int TEACHER_PASSWORD_LENGTH_LOWER_LIMIT = 8;
    private static TeacherDatabase instance = null;

    private TeacherDatabase(Class<Teacher> entity) {
        super(entity);
    }

    public static TeacherDatabase getInstance() {
        if (instance == null) instance = new TeacherDatabase(Teacher.class);
        return instance;
    }

    public boolean existUsername(String username) {
        return !queryByField("username", username).isEmpty();
    }

    public void validateTeacherLogin(String username, String pwd) {
        List<Teacher> list = queryByField("username", username);
        if (list.isEmpty()) {
            throw new RuntimeException("Username does not exist.");
        }
        if (!list.getFirst().getPassword().equals(pwd)) {
            throw new RuntimeException("Incorrect password.");
        }
    }

    public void registerTeacher(Teacher teacher) {
        add(teacher);
    }

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

    private static boolean containAny(String input, String check) {
        for (char a : input.toCharArray()) {
            if (check.contains(a + "")) return true;
        }
        return false;
    }
}
