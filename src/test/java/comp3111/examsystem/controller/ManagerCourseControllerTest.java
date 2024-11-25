package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerCourseControllerTest {
    public Course course0 = new Course();
    public Course course1 = new Course();
    public Course course2 = new Course();

    @Test
    void courseController() {
        List<Course> courseList = CourseDatabase.getInstance().getAll();
        for(Course course : courseList)
            CourseDatabase.getInstance().delByKey(course.getId().toString());

        addCourse();

        filterCourses0();
        filterCourses1();
        filterCourses2();
        filterCourses3();
        filterCourses4();
        filterCourses5();
        filterCourses6();
        filterCourses7();

        CourseDatabase.getInstance().delByFiled("courseID", course1.getCourseID());
        CourseDatabase.getInstance().delByFiled("courseID", course2.getCourseID());

        updateCourse();

        List<Course> newCourseList = CourseDatabase.getInstance().getAll();
        for(Course course : newCourseList)
            CourseDatabase.getInstance().delByKey(course.getId().toString());
    }

    public void addCourse(){
        course0.setCourseID("course0");
        course0.setCourseName("Calculus");
        course0.setDepartment("MATH");
        ManagerCourseController.addCourse("course0", "Calculus", "MATH");

        course1.setCourseID("course1");
        course1.setCourseName("Calculus");
        course1.setDepartment("PHYS");
        ManagerCourseController.addCourse("course1", "Calculus", "PHYS");

        course2.setCourseID("course2");
        course2.setCourseName("General Relativity");
        course2.setDepartment("PHYS");
        ManagerCourseController.addCourse("course2", "General Relativity", "PHYS");

        List<Course> expect = new ArrayList<>();
        expect.add(course0);
        expect.add(course1);
        expect.add(course2);

        List<Course> actual = CourseDatabase.getInstance().getAll();

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getCourseID(), actual.get(i).getCourseID());
            assertEquals(expect.get(i).getCourseName(), actual.get(i).getCourseName());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
        }
    }

    void updateCourse(){
        Course course = CourseDatabase.getInstance().getAll().getFirst();
        ManagerCourseController.updateCourse(course, "course0", "Python", "CSE");
        course.setCourseName("Python");
        course.setDepartment("CSE");

        List<Course> expect = new ArrayList<>();
        expect.add(course);

        List<Course> actual = CourseDatabase.getInstance().getAll();

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getCourseID(), actual.get(i).getCourseID());
            assertEquals(expect.get(i).getCourseName(), actual.get(i).getCourseName());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
        }
    }

    void filterCourses0() {
        String input0 = "course0";
        String input1 = "";
        String input2 = "";

        List<Course> expect = new ArrayList<>();
        expect.add(course0);

        List<Course> actual = ManagerCourseController.filterCourses(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getCourseID(), actual.get(i).getCourseID());
            assertEquals(expect.get(i).getCourseName(), actual.get(i).getCourseName());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
        }
    }

    void filterCourses1() {
        String input0 = "";
        String input1 = "Calculus";
        String input2 = "";

        List<Course> expect = new ArrayList<>();
        expect.add(course0);
        expect.add(course1);

        List<Course> actual = ManagerCourseController.filterCourses(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getCourseID(), actual.get(i).getCourseID());
            assertEquals(expect.get(i).getCourseName(), actual.get(i).getCourseName());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
        }
    }

    void filterCourses2() {
        String input0 = "";
        String input1 = "";
        String input2 = "PHYS";

        List<Course> expect = new ArrayList<>();
        expect.add(course1);
        expect.add(course2);

        List<Course> actual = ManagerCourseController.filterCourses(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getCourseID(), actual.get(i).getCourseID());
            assertEquals(expect.get(i).getCourseName(), actual.get(i).getCourseName());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
        }
    }

    void filterCourses3() {
        String input0 = "course0";
        String input1 = "Calculus";
        String input2 = "";

        List<Course> expect = new ArrayList<>();
        expect.add(course0);

        List<Course> actual = ManagerCourseController.filterCourses(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getCourseID(), actual.get(i).getCourseID());
            assertEquals(expect.get(i).getCourseName(), actual.get(i).getCourseName());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
        }
    }

    void filterCourses4() {
        String input0 = "course0";
        String input1 = "";
        String input2 = "MATH";

        List<Course> expect = new ArrayList<>();
        expect.add(course0);

        List<Course> actual = ManagerCourseController.filterCourses(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getCourseID(), actual.get(i).getCourseID());
            assertEquals(expect.get(i).getCourseName(), actual.get(i).getCourseName());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
        }
    }

    void filterCourses5() {
        String input0 = "";
        String input1 = "General Relativity";
        String input2 = "PHYS";

        List<Course> expect = new ArrayList<>();
        expect.add(course2);

        List<Course> actual = ManagerCourseController.filterCourses(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getCourseID(), actual.get(i).getCourseID());
            assertEquals(expect.get(i).getCourseName(), actual.get(i).getCourseName());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
        }
    }

    void filterCourses6() {
        String input0 = "course2";
        String input1 = "General Relativity";
        String input2 = "PHYS";

        List<Course> expect = new ArrayList<>();
        expect.add(course2);

        List<Course> actual = ManagerCourseController.filterCourses(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getCourseID(), actual.get(i).getCourseID());
            assertEquals(expect.get(i).getCourseName(), actual.get(i).getCourseName());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
        }
    }

    void filterCourses7() {
        String input0 = "";
        String input1 = "";
        String input2 = "";

        List<Course> expect = new ArrayList<>();
        expect.add(course0);
        expect.add(course1);
        expect.add(course2);

        List<Course> actual = ManagerCourseController.filterCourses(input0, input1, input2);

        assertEquals(expect.size(), actual.size());

        for(int i=0; i<expect.size(); i++){
            assertEquals(expect.get(i).getCourseID(), actual.get(i).getCourseID());
            assertEquals(expect.get(i).getCourseName(), actual.get(i).getCourseName());
            assertEquals(expect.get(i).getDepartment(), actual.get(i).getDepartment());
        }
    }
}