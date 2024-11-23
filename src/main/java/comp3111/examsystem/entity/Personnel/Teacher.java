package comp3111.examsystem.entity.Personnel;

import comp3111.examsystem.tools.Database;

import java.util.List;

/**
 * Entity storing Teacher information
 */
public class Teacher extends Personnel {
    /**
     * Store the position of the teacher
     */
    private Position position;

    /**
     * Constructor of Teacher entity
     * @author Cheung Tuen King
     */
    public Teacher() {
        super();
    }

    /**
     * Set the username of the teacher.
     * Username should not contain any space with length in range (0, TeacherDatabase.TEACHER_USERNAME_LENGTH_LIMIT).
     * Username should also be unique in teacher database
     *
     * @param username Username of teacher
     * @author Cheung Tuen King
     * @throws RuntimeException Duplicate username in teacher database, contain space in username, or length out of range
     */
    @Override
    public void setUsername(String username) {
        Database.validateTextLength(TeacherDatabase.TEACHER_USERNAME_LENGTH_LIMIT, username, "username");
        if (username.contains(" ")) {
            throw new RuntimeException("Username should not contain any blank space.");
        }
        // Check if username exist
        List<Teacher> curr = TeacherDatabase.getInstance().queryByField("username", username);
        if (!curr.isEmpty() && !curr.getFirst().id.equals(getId())) {
            throw new RuntimeException("Username occupied.");
        }
        super.setUsername(username);
    }

    /**
     * Set the username of the teacher without checking if it exists in database
     * Username should not contain any space with length in range (0, TeacherDatabase.TEACHER_USERNAME_LENGTH_LIMIT).
     *
     * @author Cheung Tuen King
     * @param username Username of teacher
     * @throws RuntimeException Contain space in username, or length out of range
     */
    public void forceSetUsername(String username) {
        Database.validateTextLength(TeacherDatabase.TEACHER_USERNAME_LENGTH_LIMIT, username, "username");
        if (username.contains(" ")) {
            throw new RuntimeException("Username should not contain any blank space.");
        }
        super.setUsername(username);
    }

    /**
     * Set the age of the teacher
     * Age should be in range (TeacherDatabase.TEACHER_AGE_LOWER_LIMIT, TeacherDatabase.TEACHER_AGE_UPPER_LIMIT).
     *
     * @author Cheung Tuen King
     * @param age Age of teacher
     * @throws RuntimeException Age out of range
     */
    @Override
    public void setAge(int age) {
        Database.validateNumberRange(
                TeacherDatabase.TEACHER_AGE_LOWER_LIMIT,
                TeacherDatabase.TEACHER_AGE_UPPER_LIMIT,
                age,
                "teacher age",
                ""
        );

        super.setAge(age);
    }

    /**
     * Set the age of the teacher
     * Age should be in range (TeacherDatabase.TEACHER_AGE_LOWER_LIMIT, TeacherDatabase.TEACHER_AGE_UPPER_LIMIT).
     *
     * @author Cheung Tuen King
     * @param age Age of teacher
     * @throws RuntimeException Age out of range or is not an integer
     */
    public void setAge(String age) {
        Database.validateNumberRange(
                TeacherDatabase.TEACHER_AGE_LOWER_LIMIT,
                TeacherDatabase.TEACHER_AGE_UPPER_LIMIT,
                age,
                "teacher age",
                ""
        );

        super.setAge(Integer.parseInt(age));
    }

    /**
     * Set the department teacher belongs to
     * Department length should be in range (0, TeacherDatabase.TEACHER_DEPT_LENGTH_LIMIT).
     *
     * @author Cheung Tuen King
     * @param department Department teacher belongs to
     * @throws RuntimeException Length of department out of range
     */
    @Override
    public void setDepartment(String department) {
        Database.validateTextLength(TeacherDatabase.TEACHER_DEPT_LENGTH_LIMIT, department, "department");
        super.setDepartment(department);
    }

    /**
     * Set the name of the teacher
     * Name length should be in range (0, TeacherDatabase.TEACHER_NAME_LENGTH_LIMIT).
     *
     * @author Cheung Tuen King
     * @param name Teacher name
     * @throws RuntimeException Length of name out of range
     */
    @Override
    public void setName(String name) {
        Database.validateTextLength(TeacherDatabase.TEACHER_NAME_LENGTH_LIMIT, name, "teacher name");
        super.setName(name);
    }

    /**
     * Set the password of the teacher account
     * Password should:
     * <li>have length in range (0, TeacherDatabase.TEACHER_PASSWORD_LENGTH_LIMIT)</li>
     * <li>contain no blank space</li>
     * <li>have at least 1 capital letter</li>
     * <li>have at least 1 small letter</li>
     * <li>have at least 1 number</li>
     * <li>have at least 1 special character "!@$%^&*_+<>/-"</li>
     *
     * @author Cheung Tuen King
     * @param password Teacher name
     * @throws RuntimeException Any above condition is not met
     */
    @Override
    public void setPassword(String password) {
        password = password.trim();
        Database.validateTextLength(TeacherDatabase.TEACHER_PASSWORD_LENGTH_LIMIT, password, "password");
        TeacherDatabase.validatePassword(password);
        super.setPassword(password);
    }

    /**
     * Access the position of the teacher
     * @return Position of the teacher
     * @author Cheung Tuen King
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Set the position of the teacher
     * @author Cheung Tuen King
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Set the position of the teacher
     *
     * @author Cheung Tuen King
     * @throws Exception Position does not exist
     */
    public void setPosition(String position) {
        this.position = Position.toPosition(position);
    }

    /**
     * Check if the confirmed password same as the password set
     *
     * @author Cheung Tuen King
     * @throws RuntimeException Password unmatched
     */
    public void confirmPassword(String pwd) {
        Database.validateTextLength(TeacherDatabase.TEACHER_PASSWORD_LENGTH_LIMIT, pwd, "confirmed password");
        if (!getPassword().equals(pwd.trim())) {
            throw new RuntimeException("Confirmed password unmatched.");
        }
    }
}
