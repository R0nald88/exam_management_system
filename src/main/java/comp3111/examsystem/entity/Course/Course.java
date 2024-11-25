package comp3111.examsystem.entity.Course;

import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.tools.Database;

/**
 * Class for course object.
 * @author Wan Hanzhe
 */
public class Course extends Entity {
    /**
     * The course ID.
     */
    private String courseID;
    /**
     * The course name.
     */
    private String courseName;
    /**
     * The department of the course.
     */
    private String department;

    /**
     * Course constructor.
     * @param courseID The course ID.
     * @param courseName The course name.
     * @param department The department of the course.
     */
    public Course(String courseID, String courseName, String department){
        super(System.currentTimeMillis());
        this.courseID = courseID;
        this.courseName = courseName;
        this.department = department;
    }

    /**
     * Default Course constructor.
     */
    public Course(){
        super(System.currentTimeMillis());
    }

    /**
     * Getter for course ID.
     * @return course ID.
     */
    public String getCourseID(){
        return courseID;
    }

    /**
     * Getter for course name.
     * @return course name.
     */
    public String getCourseName(){
        return courseName;
    }

    /**
     * Getter for department.
     * @return department.
     */
    public String getDepartment(){
        return department;
    }

    /**
     * Setter for course ID.
     * @param courseID course ID.
     */
    public void setCourseID(String courseID){
        Database.validateTextLength(CourseDatabase.COURSE_ID_LENGTH_LIMIT, courseID, "courseID");
        if(courseID.contains(" "))
            throw new RuntimeException("Course ID should not contain any blank spaces.");
        if(CourseDatabase.getInstance().existCourseID(courseID))
            throw new RuntimeException("Course ID already exists.");
        this.courseID = courseID;
    }

    /**
     * Set course ID without check for uniqueness. Unused.
     * @param courseID course ID.
     */
    public void forceSetCourseID(String courseID){
        Database.validateTextLength(CourseDatabase.COURSE_ID_LENGTH_LIMIT, courseID, "courseID");
        if(courseID.contains(" "))
            throw new RuntimeException("Course ID should not contain any blank spaces.");
        this.courseID = courseID;
    }

    /**
     * Setter for course name.
     * @param courseName course name.
     */
    public void setCourseName(String courseName){
        Database.validateTextLength(CourseDatabase.COURSE_NAME_LENGTH_LIMIT, courseName, "courseName");
        this.courseName = courseName;
    }

    /**
     * Setter for department.
     * @param department the department.
     */
    public void setDepartment(String  department){
        Database.validateTextLength(CourseDatabase.COURSE_DEPT_LENGTH_LIMIT, department, "department");
        this.department = department;
    }
}
