package comp3111.examsystem.entity.Personnel;

import comp3111.examsystem.tools.Database;

public class Student extends Personnel {

    public Student() {
        super();
    }

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

    public void forceSetUsername(String username) {
        Database.validateTextLength(StudentDatabase.STUDENT_USERNAME_LENGTH_LIMIT, username, "username");
        if (username.contains(" ")) {
            throw new RuntimeException("Username should not contain any blank space.");
        }
        super.setUsername(username);
    }

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

    @Override
    public void setDepartment(String department) {
        Database.validateTextLength(StudentDatabase.STUDENT_DEPT_LENGTH_LIMIT, department, "department");
        super.setDepartment(department);
    }

    @Override
    public void setName(String name) {
        Database.validateTextLength(StudentDatabase.STUDENT_NAME_LENGTH_LIMIT, name, "student name");
        super.setName(name);
    }

    @Override
    public void setPassword(String password) {
        password = password.trim();
        Database.validateTextLength(StudentDatabase.STUDENT_PASSWORD_LENGTH_LIMIT, password, "password");
        StudentDatabase.validatePassword(password);
        super.setPassword(password);
    }

    public void confirmPassword(String pwd) {
        Database.validateTextLength(StudentDatabase.STUDENT_PASSWORD_LENGTH_LIMIT, pwd, "confirmed password");
        if (!getPassword().equals(pwd.trim())) {
            throw new RuntimeException("Confirmed password unmatched.");
        }
    }
}