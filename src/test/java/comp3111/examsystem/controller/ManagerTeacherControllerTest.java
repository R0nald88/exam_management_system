package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Personnel.Teacher;
import comp3111.examsystem.entity.Personnel.TeacherDatabase;
import comp3111.examsystem.entity.Personnel.Teacher;
import comp3111.examsystem.entity.Personnel.TeacherDatabase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTeacherControllerTest {

    public Teacher teacher0 = new Teacher();
    public Teacher teacher1 = new Teacher();
    public Teacher teacher2 = new Teacher();

    @Test
    void teacherController() {
        List<Teacher> teacherList = TeacherDatabase.getInstance().getAll();
        for(Teacher teacher : teacherList)
            TeacherDatabase.getInstance().delByKey(teacher.getId().toString());

        addTeacher();

        filterTeachers0();
        filterTeachers1();
        filterTeachers2();
        filterTeachers3();
        filterTeachers4();
        filterTeachers5();
        filterTeachers6();
        filterTeachers7();

        deleteTeacher();

        updateTeacher();

        List<Teacher> newTeacherList = TeacherDatabase.getInstance().getAll();
        for(Teacher teacher : newTeacherList)
            TeacherDatabase.getInstance().delByKey(teacher.getId().toString());
    }


    void addTeacher(){
        teacher0.setUsername("teacher0");
        teacher0.setName("Jack");
        teacher0.setAge("25");
        teacher0.setDepartment("MATH");
        teacher0.setPassword("123456!Az");
        teacher0.setGender("Male");
        teacher0.setPosition("Professor");
        ManagerTeacherController.addTeacher("teacher0", "Jack", "25", "MATH", "123456!Az", "Male", "Professor");

        teacher1.setUsername("teacher1");
        teacher1.setName("Jack");
        teacher1.setAge("25");
        teacher1.setDepartment("PHYS");
        teacher1.setPassword("123456!Az");
        teacher1.setGender("Male");
        teacher1.setPosition("Professor");
        ManagerTeacherController.addTeacher("teacher1", "Jack", "25", "PHYS", "123456!Az", "Male", "Professor");

        teacher2.setUsername("teacher2");
        teacher2.setName("Ray");
        teacher2.setAge("25");
        teacher2.setDepartment("PHYS");
        teacher2.setPassword("123456!Az");
        teacher2.setGender("Male");
        teacher2.setPosition("Professor");
        ManagerTeacherController.addTeacher("teacher2", "Ray", "25", "PHYS", "123456!Az", "Male", "Professor");

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher0);
        expect.add(teacher1);
        expect.add(teacher2);

        List<Teacher> actual = TeacherDatabase.getInstance().getAll();

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }

    void deleteTeacher(){
        ManagerTeacherController.deleteTeacher(teacher1);
        ManagerTeacherController.deleteTeacher(teacher2);

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher0);

        List<Teacher> actual = TeacherDatabase.getInstance().getAll();

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }

    void updateTeacher(){
        Teacher teacher = TeacherDatabase.getInstance().getAll().getFirst();
        ManagerTeacherController.updateTeacher(teacher, "teacher0", "David", "30", "CSE", "13579!Az", "Male", "Instructor");
        teacher.setName("David");
        teacher.setAge("30");
        teacher.setDepartment("CSE");
        teacher.setPassword("13579!Az");
        teacher.setGender("Male");
        teacher.setPosition("Instructor");

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher);

        List<Teacher> actual = TeacherDatabase.getInstance().getAll();

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }

    void filterTeachers0() {
        String input0 = "teacher0";
        String input1 = "";
        String input2 = "";

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher0);

        List<Teacher> actual = ManagerTeacherController.filterTeachers(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }

    void filterTeachers1() {
        String input0 = "";
        String input1 = "Jack";
        String input2 = "";

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher0);
        expect.add(teacher1);

        List<Teacher> actual = ManagerTeacherController.filterTeachers(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }

    void filterTeachers2() {
        String input0 = "";
        String input1 = "";
        String input2 = "PHYS";

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher1);
        expect.add(teacher2);

        List<Teacher> actual = ManagerTeacherController.filterTeachers(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }

    void filterTeachers3() {
        String input0 = "teacher0";
        String input1 = "Jack";
        String input2 = "";

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher0);

        List<Teacher> actual = ManagerTeacherController.filterTeachers(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }

    void filterTeachers4() {
        String input0 = "teacher0";
        String input1 = "";
        String input2 = "MATH";

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher0);

        List<Teacher> actual = ManagerTeacherController.filterTeachers(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }

    void filterTeachers5() {
        String input0 = "";
        String input1 = "Ray";
        String input2 = "PHYS";

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher2);

        List<Teacher> actual = ManagerTeacherController.filterTeachers(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }

    void filterTeachers6() {
        String input0 = "teacher2";
        String input1 = "Ray";
        String input2 = "PHYS";

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher2);

        List<Teacher> actual = ManagerTeacherController.filterTeachers(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }

    void filterTeachers7() {
        String input0 = "";
        String input1 = "";
        String input2 = "";

        List<Teacher> expect = new ArrayList<>();
        expect.add(teacher0);
        expect.add(teacher1);
        expect.add(teacher2);

        List<Teacher> actual = ManagerTeacherController.filterTeachers(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getUsername(), actual.get(i).getUsername());
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getAge(), actual.get(i).getAge());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
            assertEquals(expect.get(i).getPassword(), actual.get(i).getPassword());
            assertEquals(expect.get(i).getGender(), actual.get(i).getGender());
            assertEquals(expect.get(i).getPosition(), actual.get(i).getPosition());
        }
    }
}