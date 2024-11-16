package comp3111.examsystem.entity.Personnel;

import comp3111.examsystem.tools.Database;

public class Teacher extends Personnel {
    private Position position;

    public Teacher() {
        super();
    }

    @Override
    public void setUsername(String username) {
        Database.validateTextLength(TeacherDatabase.TEACHER_USERNAME_LENGTH_LIMIT, username, "username");
        if (username.contains(" ")) {
            throw new RuntimeException("Username should not contain any blank space.");
        }
        // Check if username exist
        if (TeacherDatabase.getInstance().existUsername(username)) {
            throw new RuntimeException("Username occupied.");
        }
        super.setUsername(username);
    }

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

    @Override
    public void setDepartment(String department) {
        Database.validateTextLength(TeacherDatabase.TEACHER_DEPT_LENGTH_LIMIT, department, "department");
        super.setDepartment(department);
    }

    @Override
    public void setName(String name) {
        Database.validateTextLength(TeacherDatabase.TEACHER_NAME_LENGTH_LIMIT, name, "teacher name");
        super.setName(name);
    }

    @Override
    public void setPassword(String password) {
        password = password.trim();
        Database.validateTextLength(TeacherDatabase.TEACHER_PASSWORD_LENGTH_LIMIT, password, "password");
        TeacherDatabase.validatePassword(password);
        super.setPassword(password);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(String position) {
        this.position = Position.toPosition(position);
    }

    public void confirmPassword(String pwd) {
        Database.validateTextLength(TeacherDatabase.TEACHER_PASSWORD_LENGTH_LIMIT, pwd, "confirmed password");
        if (!getPassword().equals(pwd.trim())) {
            throw new RuntimeException("Confirmed password unmatched.");
        }
    }
}
