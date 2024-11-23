package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Personnel.Gender;
import comp3111.examsystem.entity.Personnel.Position;
import comp3111.examsystem.entity.Personnel.Teacher;
import comp3111.examsystem.entity.Personnel.TeacherDatabase;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeacherTest {
    private Teacher teacher;

    @Test
    public void testTeacherInput() {
        TeacherDatabase.getInstance().deleteAll();
        String inputUsername = "user1",
                inputPwd = "1234%^QWe",
                inputDept = "COMP",
                inputName = "Teacher 3209",
                inputGender = Gender.MALE.getName();
        int inputAge = 43;
        Position inputPos = Position.ASSISTANT_PROFESSOR;

        try {
            teacher = new Teacher();
            teacher.setGender(inputGender);
            teacher.setPassword(inputPwd);
            teacher.setAge(inputAge + "");
            teacher.setAge(inputAge);
            teacher.setName(inputName);
            teacher.setDepartment(inputDept);
            teacher.setUsername(inputUsername);
            teacher.forceSetUsername(inputUsername);
            teacher.confirmPassword(inputPwd);
            teacher.setPosition(inputPos);
            teacher.setPosition(inputPos.getName());
        } catch (Exception e) {
            assertEquals(e.getMessage(), "");
        }

        assertEquals(teacher.getGender(), Gender.toGender(inputGender));
        assertEquals(teacher.getPosition(), inputPos);
        assertEquals(teacher.getName(), inputName);
        assertEquals(teacher.getPassword(), inputPwd);
        assertEquals(teacher.getAge(), inputAge);
        assertEquals(teacher.getDepartment(), inputDept);
        assertEquals(teacher.getUsername(), inputUsername);
    }

    @Test
    public void testInvalidUsername() {
        try {
            testTeacherInput();
            teacher.setUsername(" cs sd");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Username should not contain any blank space.");
        }

        try {
            testTeacherInput();
            teacher.forceSetUsername(" cs sd");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Username should not contain any blank space.");
        }
    }

    @Test
    public void testInvalidPassword() {
        try {
            testTeacherInput();
            teacher.setPassword("12QW");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should contain at least " +
                    TeacherDatabase.TEACHER_PASSWORD_LENGTH_LOWER_LIMIT +
                    " characters.");
        }

        try {
            teacher.setPassword("12QWredew");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should contain at least 1 special character \"!@$%^&*_+<>/-\".");
        }

        try {
            teacher.setPassword("12QWr#@  edew");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should not contain any blank space.");
        }

        try {
            teacher.setPassword("QWr#@edew");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should contain at least 1 number from 0 to 9.");
        }

        try {
            teacher.setPassword("QWr#@32423");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should contain at least 1 small letter from a to z");
        }

        try {
            teacher.setPassword("234r#@edew");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should contain at least 1 capital letter from A to Z");
        }

        try {
            teacher.confirmPassword("234r#@edew");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Confirmed password unmatched.");
        }
    }

    @Test
    public void testSavingTeacher() {
        TeacherDatabase.getInstance().deleteAll();
        testTeacherInput();
        TeacherDatabase.getInstance().registerTeacher(teacher);
        List<Teacher> list = TeacherDatabase.getInstance().getAll();
        assertEquals(list.getFirst().getUsername(), teacher.getUsername());

        try {
            teacher.setUsername(teacher.getUsername());
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Username occupied.");
        }
    }

    @Test
    public void testTeacherLogin() {
        testSavingTeacher();

        try {
            TeacherDatabase.getInstance().validateTeacherLogin("sfsd", null);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Username does not exist.");
        }

        try {
            TeacherDatabase.getInstance().validateTeacherLogin(teacher.getUsername(), "fsda");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Incorrect password.");
        }
    }
}
