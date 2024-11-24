package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Personnel.StudentDatabase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerStudentControllerTest {

    public Student student0 = new Student();
    public Student student1 = new Student();
    public Student student2 = new Student();

    @Test
    void studentController() {
        List<Student> studentList = StudentDatabase.getInstance().getAll();
        for(Student student : studentList)
            StudentDatabase.getInstance().delByKey(student.getId().toString());

        addStudent();

        filterStudents0();
        filterStudents1();
        filterStudents2();
        filterStudents3();
        filterStudents4();
        filterStudents5();
        filterStudents6();
        filterStudents7();

        deleteStudent();

        updateStudent();

        List<Student> newStudentList = StudentDatabase.getInstance().getAll();
        for(Student student : newStudentList)
            StudentDatabase.getInstance().delByKey(student.getId().toString());
    }

    void addStudent(){
        student0.setUsername("student0");
        student0.setName("Jack");
        student0.setAge("25");
        student0.setDepartment("MATH");
        student0.setPassword("123456!Az");
        student0.setGender("Male");
        ManagerStudentController.addStudent("student0", "Jack", "25", "MATH", "123456!Az", "Male");

        student1.setUsername("student1");
        student1.setName("Jack");
        student1.setAge("25");
        student1.setDepartment("PHYS");
        student1.setPassword("123456!Az");
        student1.setGender("Male");
        ManagerStudentController.addStudent("student1", "Jack", "25", "PHYS", "123456!Az", "Male");

        student2.setUsername("student2");
        student2.setName("Ray");
        student2.setAge("25");
        student2.setDepartment("PHYS");
        student2.setPassword("123456!Az");
        student2.setGender("Male");
        ManagerStudentController.addStudent("student2", "Ray", "25", "PHYS", "123456!Az", "Male");

        List<Student> expect = new ArrayList<>();
        expect.add(student0);
        expect.add(student1);
        expect.add(student2);

        List<Student> actual = StudentDatabase.getInstance().getAll();

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }

    void deleteStudent(){
        ManagerStudentController.deleteStudent(student1);
        ManagerStudentController.deleteStudent(student2);

        List<Student> expect = new ArrayList<>();
        expect.add(student0);

        List<Student> actual = StudentDatabase.getInstance().getAll();

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }

    void updateStudent(){
        Student student = StudentDatabase.getInstance().getAll().getFirst();
        ManagerStudentController.updateStudent(student, "student0", "David", "30", "CSE", "13579!Az", "Male");
        student.setName("David");
        student.setAge("30");
        student.setDepartment("CSE");
        student.setPassword("13579!Az");
        student.setGender("Male");

        List<Student> expect = new ArrayList<>();
        expect.add(student);

        List<Student> actual = StudentDatabase.getInstance().getAll();

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }

    void filterStudents0() {
        String input0 = "student0";
        String input1 = "";
        String input2 = "";

        List<Student> expect = new ArrayList<>();
        expect.add(student0);

        List<Student> actual = ManagerStudentController.filterStudents(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }

    void filterStudents1() {
        String input0 = "";
        String input1 = "Jack";
        String input2 = "";

        List<Student> expect = new ArrayList<>();
        expect.add(student0);
        expect.add(student1);

        List<Student> actual = ManagerStudentController.filterStudents(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }

    void filterStudents2() {
        String input0 = "";
        String input1 = "";
        String input2 = "PHYS";

        List<Student> expect = new ArrayList<>();
        expect.add(student1);
        expect.add(student2);

        List<Student> actual = ManagerStudentController.filterStudents(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }

    void filterStudents3() {
        String input0 = "student0";
        String input1 = "Jack";
        String input2 = "";

        List<Student> expect = new ArrayList<>();
        expect.add(student0);

        List<Student> actual = ManagerStudentController.filterStudents(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }

    void filterStudents4() {
        String input0 = "student0";
        String input1 = "";
        String input2 = "MATH";

        List<Student> expect = new ArrayList<>();
        expect.add(student0);

        List<Student> actual = ManagerStudentController.filterStudents(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }

    void filterStudents5() {
        String input0 = "";
        String input1 = "Ray";
        String input2 = "PHYS";

        List<Student> expect = new ArrayList<>();
        expect.add(student2);

        List<Student> actual = ManagerStudentController.filterStudents(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }

    void filterStudents6() {
        String input0 = "student2";
        String input1 = "Ray";
        String input2 = "PHYS";

        List<Student> expect = new ArrayList<>();
        expect.add(student2);

        List<Student> actual = ManagerStudentController.filterStudents(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }

    void filterStudents7() {
        String input0 = "";
        String input1 = "";
        String input2 = "";

        List<Student> expect = new ArrayList<>();
        expect.add(student0);
        expect.add(student1);
        expect.add(student2);

        List<Student> actual = ManagerStudentController.filterStudents(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
        }
    }
}