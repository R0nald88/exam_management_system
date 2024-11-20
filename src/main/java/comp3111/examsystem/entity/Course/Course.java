package comp3111.examsystem.entity.Course;

import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.tools.Database;

public class Course extends Entity {
    private String courseID;
    private String courseName;
    private String department;

    public Course(String courseID, String courseName, String department){
        this.courseID = courseID;
        this.courseName = courseName;
        this.department = department;
    }

    public Course(){
        super(System.currentTimeMillis());
    }

    public String getCourseID(){
        return courseID;
    }

    public String getCourseName(){
        return courseName;
    }

    public String getDepartment(){
        return department;
    }

    public void setCourseID(String courseID){
        Database.validateTextLength(CourseDatabase.COURSE_ID_LENGTH_LIMIT, courseID, "courseID");
        if(courseID.contains(" "))
            throw new RuntimeException("Course ID should not contain any blank spaces.");
        if(CourseDatabase.getInstance().existCourseID(courseID))
            throw new RuntimeException("Course ID already exists.");
        this.courseID = courseID;
    }

    public void forceSetCourseID(String courseID){
        Database.validateTextLength(CourseDatabase.COURSE_ID_LENGTH_LIMIT, courseID, "courseID");
        if(courseID.contains(" "))
            throw new RuntimeException("Course ID should not contain any blank spaces.");
        this.courseID = courseID;
    }

    public void setCourseName(String courseName){
        Database.validateTextLength(CourseDatabase.COURSE_NAME_LENGTH_LIMIT, courseName, "courseName");
        this.courseName = courseName;
    }

    public void setDepartment(String  department){
        Database.validateTextLength(CourseDatabase.COURSE_DEPT_LENGTH_LIMIT, department, "department");
        this.department = department;
    }
}
