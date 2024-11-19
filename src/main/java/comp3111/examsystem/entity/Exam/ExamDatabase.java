package comp3111.examsystem.entity.Exam;

import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.tools.Database;

import java.util.ArrayList;
import java.util.List;

public class ExamDatabase extends Database<Exam> {
    /**
     * Restriction of the upper time limit of exam time. Set to -1 to remove restriction.
     */
    public static final int EXAM_TIME_UPPER_LIMIT = -1;

    /**
     * Restriction of the lower time limit of exam time. Set to -1 to remove restriction.
     */
    public static final int EXAM_TIME_LOWER_LIMIT = -1;

    /**
     * Restriction of the length limit of exam name. Set to -1 to remove restriction.
     */
    public static final int EXAM_NAME_LENGTH_LIMIT = -1;

    /**
     * Exam singleton instance
     */
    private static ExamDatabase instance = null;

    private ExamDatabase() {
        super(Exam.class);
    }

    /**
     * Returns exam database singleton
     * for retrieving, adding, updating and deleting Exam entity
     *
     * @return ExamDatabase
     * @author Cheung Tuen King
     */
    public static ExamDatabase getInstance() {
        if (instance == null) instance = new ExamDatabase();
        return instance;
    }

    /**
     * Filter exams in the database based on the parameters provided.
     * If parameters is null or empty string, filtering on associated field is not performed.
     *
     * @param name Exam name for filtering.
     *             Perform fuzzy matching on this field.
     *
     * @param courseId Course ID for filtering.
     *                 Perform exact matching on this field.
     *
     * @param published Filter the published exam or not.
     *                  Perform exact matching on this field.
     *
     * @return List of exams matching the parameters
     * @author Cheung Tuen King
     */
    public List<Exam> filter(String name, String courseId, String published) {
        List<Exam> courseList;

        if (courseId == null || courseId.trim().isEmpty()) {
            courseList = getAll();
        } else if (!CourseDatabase.getInstance().existCourseID(courseId.trim())) {
            return new ArrayList<>();
        } else {
            long cId = CourseDatabase.getInstance().queryByField("courseID", courseId.trim()).getFirst().getId();
            courseList = queryByField("longIdOfCourse", courseId.trim());
        }

        List<Exam> nameList = (name == null || name.trim().isEmpty()) ?
                getAll() : queryFuzzyByField("name", name.trim());

        List<Exam> publishedList = (published == null || published.trim().isEmpty()) ?
                getAll() : queryByField("published", published.trim());

        return join(nameList, join(courseList, publishedList));
    }

    /**
     * Search for exams containing question id provided
     *
     * @param id Question Id
     * @return List of exams containing the question Id
     * @author Cheung Tuen King
     */
    public List<Exam> queryByQuestion(long id) {
        List<Exam> exams = getAll();
        exams.removeIf(exam -> !exam.getQuestionIds().contains(id));
        return exams;
    }

    /**
     * Check if the provided exam ID exists
     *
     * @param id Exam id for checking
     * @return Boolean determining if the exam ID provided exists
     * @author Cheung Tuen King
     */
    public boolean exist(long id) {
        return queryByKey(id + "") != null;
    }

    /**
     * Check if the provided exam exists by checking its ID
     *
     * @param exam Exam for checking
     * @return Boolean determining if the exam provided exists
     * @author Cheung Tuen King
     */
    public boolean exist(Exam exam) {
        return exist(exam.getId());
    }

    /**
     * Validate and add the exam to the database.
     * ID would be auto-changed if identical ID already exists in the database.
     *
     * @param entity Exam for adding
     * @throws Exception The exam is null, or exam with identical name and course ID exists
     * @author Cheung Tuen King
     */
    public void addExam(Exam entity) throws Exception {
        if (entity == null) {
            throw new Exception("Exam does not exist.");
        }
        // if id exist, assign a new id
        if (exist(entity)) {
            entity.setId(System.currentTimeMillis());
        }

        // cannot have two identical exam name in one course
        if (!filter(entity.getName(), entity.getCourseId(), null).isEmpty()) {
            throw new Exception("Course " + entity.getCourseId() + " cannot have two identical exam name: " + entity.getName() + ".");
        }
        super.add(entity);
    }

    /**
     * Validate and update the exam provided
     *
     * @param entity Exam for updating
     * @throws Exception The provided exam does not have id matched in the database. Perhaps add the exam instead.
     * @author Cheung Tuen King
     */
    public void updateExam(Exam entity) throws Exception {
        if (entity == null) {
            throw new Exception("Exam does not exist.");
        }
        // if id not exist, throw exception
        if (!exist(entity)) {
            throw new Exception("Exam " + entity.getCourseId() + " " + entity.getName() + " does not exist.");
        }

        update(entity);
    }

    /**
     * Delete the provided exam.
     *
     * @param entity Exam for deleting
     * @throws Exception The provided exam does not exist in the database
     * @author Cheung Tuen King
     */
    public void deleteExam(Exam entity) throws Exception {
        if (entity == null) {
            throw new Exception("Exam does not exist.");
        }
        // if id not exist, throw exception
        if (!exist(entity)) {
            throw new Exception("Exam " + entity.getCourseId() + " " + entity.getName() + " does not exist.");
        }

        delByKey(entity.getId() + "");
    }

    /**
     * Delete the list of exams provided.
     *
     * @param exams List of exams for deleting
     * @throws Exception There exist an exam provided which does not exist in the database.
     * @author Cheung Tuen King
     */
    public void deleteExams(List<Exam> exams) throws Exception {
        for (Exam e : exams) {
            deleteExam(e);
        }
    }

    /**
     * Delete all the exam in the database. For testing only.
     *
     * @throws Exception There exist an exam provided which does not exist in the database.
     * @author Cheung Tuen King
     */
    public void deleteAll() throws Exception {
        deleteExams(getAll());
    }
}
