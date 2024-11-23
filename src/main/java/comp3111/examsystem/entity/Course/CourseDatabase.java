package comp3111.examsystem.entity.Course;

import comp3111.examsystem.tools.Database;

import java.util.ArrayList;
import java.util.List;

public class CourseDatabase extends Database<Course>{
    public static final int COURSE_ID_LENGTH_LIMIT = -1;
    public static final int COURSE_NAME_LENGTH_LIMIT = -1;
    public static final int COURSE_DEPT_LENGTH_LIMIT = -1;

    private static CourseDatabase instance = null;

    public CourseDatabase(Class<Course> entity){
        super(entity);
    }
    public static CourseDatabase getInstance() {
        if(instance == null)
            instance = new CourseDatabase(Course.class);
        return instance;
    }

    public boolean existCourseID(String courseID){
        return !queryByField("courseID", courseID).isEmpty();
    }

    /**
     * Access a list of course id for all courses in course database
     * @return List of course id
     * @author Cheung Tuen King
     */
    public static ArrayList<String> getCourseIds() {
        List<Course> courseList = CourseDatabase.getInstance().getAll();
        return new ArrayList<>(courseList.stream().map(Course::getCourseID).toList());
    }
}
