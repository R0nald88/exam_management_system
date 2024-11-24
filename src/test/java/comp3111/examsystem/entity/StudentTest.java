package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Personnel.Gender;
import comp3111.examsystem.entity.Personnel.Position;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Personnel.StudentDatabase;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentTest {
    private Student student;

    @Test
    public void testStudentInput() {
        StudentDatabase.getInstance().deleteAll();
        String inputUsername = "s1",
                inputPwd = "123456!aZ",
                inputDept = "MATH",
                inputName = "Student t123",
                inputGender = Gender.MALE.getName();
        int inputAge = 43;
        Position inputPos = Position.ASSISTANT_PROFESSOR;

        try {
            student = new Student();
            student.setGender(inputGender);
            student.setPassword(inputPwd);
            student.setAge(inputAge + "");
            student.setAge(inputAge);
            student.setName(inputName);
            student.setDepartment(inputDept);
            student.setUsername(inputUsername);
            student.forceSetUsername(inputUsername);
            student.confirmPassword(inputPwd);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "");
        }

        assertEquals(student.getGender(), Gender.toGender(inputGender));
        assertEquals(student.getName(), inputName);
        assertEquals(student.getPassword(), inputPwd);
        assertEquals(student.getAge(), inputAge);
        assertEquals(student.getDepartment(), inputDept);
        assertEquals(student.getUsername(), inputUsername);
    }

    @Test
    public void testInvalidUsername() {
        try {
            testStudentInput();
            student.setUsername(" student 1");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Username should not contain any blank space.");
        }

        try {
            testStudentInput();
            student.forceSetUsername(" student 1");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Username should not contain any blank space.");
        }
    }

    @Test
    public void testInvalidPassword() {
        try {
            testStudentInput();
            student.setPassword("12QW");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should contain at least " +
                    StudentDatabase.STUDENT_PASSWORD_LENGTH_LOWER_LIMIT +
                    " characters.");
        }

        try {
            student.setPassword("12QWredew");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should contain at least 1 special character \"!@$%^&*_+<>/-\".");
        }

        try {
            student.setPassword("12QWr#@  edew");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should not contain any blank space.");
        }

        try {
            student.setPassword("QWr#@edew");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should contain at least 1 number from 0 to 9.");
        }

        try {
            student.setPassword("QWr#@32423");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should contain at least 1 small letter from a to z");
        }

        try {
            student.setPassword("234r#@edew");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Password should contain at least 1 capital letter from A to Z");
        }

        try {
            student.confirmPassword("234r#@edew");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Confirmed password unmatched.");
        }
    }

    @Test
    public void testSavingStudent() {
        StudentDatabase.getInstance().deleteAll();
        testStudentInput();
        StudentDatabase.getInstance().registerStudent(student);
        List<Student> list = StudentDatabase.getInstance().getAll();
        assertEquals(list.getFirst().getUsername(), student.getUsername());

        try {
            student.setUsername(student.getUsername());
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Username occupied.");
        }
    }

    @Test
    public void testStudentLogin() {
        testSavingStudent();

        try {
            StudentDatabase.getInstance().validateStudentLogin("sfsd", null);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Username does not exist.");
        }

        try {
            StudentDatabase.getInstance().validateStudentLogin(student.getUsername(), "fsda");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Incorrect password.");
        }
    }
}
