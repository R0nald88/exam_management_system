package comp3111.examsystem.entity.Exam;

import comp3111.examsystem.tools.Database;

import java.util.List;

public class SubmissionDatabase extends Database<Submission> {
    /**
     * Submission singleton instance
     */
    private static SubmissionDatabase instance = null;

    private SubmissionDatabase() {
        super(Submission.class);
    }

    /**
     * Returns submission database singleton
     * for retrieving, adding, updating and deleting Submission entity
     *
     * @return SubmissionDatabase
     * @author Li Ching Ho
     */
    public static SubmissionDatabase getInstance() {
        if (instance == null) instance = new SubmissionDatabase();
        return instance;
    }

    /**
     * Filter exams in the database based on the parameters provided.
     * If parameters is null or empty string, filtering on associated field is not performed.
     *
     * @param studentUsername Student username for filtering.
     *                 Perform exact matching on this field.
     *
     * @param courseId Course ID for filtering.
     *                 Perform exact matching on this field.
     *
     * @param examId Exam ID for filtering.
     *                  Perform exact matching on this field.
     *
     * @return List of exams matching the parameters
     * @author Li Ching Ho
     */
    public List<Submission> filter(String studentUsername, String courseId, String examId) {
        List<Submission> nameList = (studentUsername == null || studentUsername.isEmpty()) ?
                getAll() : queryByField("studentUsername", studentUsername);

        List<Submission> courseList = (courseId == null || courseId.isEmpty()) ?
                getAll() : queryByField("courseId", courseId);

        List<Submission> examList = (examId == null || examId.isEmpty()) ?
                getAll() : queryByField("examId", examId);

        return join(nameList, join(courseList, examList));
    }

    /**
     * Check if the provided submission ID exists
     *
     * @param id Submission id for checking
     * @return Boolean determining if the submission ID provided exists
     * @author Li Ching Ho
     */
    public boolean exist(long id) {
        return queryByKey(id + "") != null;
    }

    /**
     * Check if the provided submission exists by checking its id
     *
     * @param submission Submission for checking
     * @return Boolean determining if the submission provided exists
     * @author Li Ching Ho
     */
    public boolean exist(Submission submission) {
        return exist(submission.getId());
    }

    /**
     * Validate and add the submission to the database.
     * ID would be auto-changed if identical ID already exists in the database.
     *
     * @param entity Submission for adding
     * @throws Exception The submission is null, or submission with identical student username, course ID and exam ID exists
     * @author Li Ching Ho
     */
    public void addSubmission(Submission entity) throws Exception {
        if (entity == null) {
            System.out.println("submission entity is null");
            throw new Exception("Submission does not exist.");
        }

        // if id exist, assign a new id
        if (exist(entity)) {
            entity.setId(System.currentTimeMillis());
        }

        // cannot have two identical submission for 1 student
        if (!filter(entity.getStudentUsername(), ExamDatabase.getInstance().queryByKey(entity.getExamId().toString()).getCourseId(), entity.getExamId().toString()).isEmpty()) {
            throw new Exception("Student " + entity.getStudentUsername() + " cannot have 2 submission for exam " + ExamDatabase.getInstance().queryByKey(entity.getExamId().toString()).getCourseId() + " " + entity.getExamId().toString() + ".");
        }
        super.add(entity);
    }

    /**
     * Validate and update the submission provided
     *
     * @param entity Submission for updating
     * @throws Exception The provided submission does not have id matched in the database. Perhaps add the submission instead.
     * @author Li Ching Ho
     */
    public void updateSubmission(Submission entity) throws Exception {
        if (entity == null) {
            throw new Exception("submission does not exist.");
        }
        // if id not exist, throw exception
        if (!exist(entity)) {
            throw new Exception("Submission of Student with username " + entity.getStudentUsername() + " conducting exam " + entity.getCourseId() + " " + entity.getExamName() + " exam does not exist.");
        }

        update(entity);
    }

    /**
     * Delete the provided submission.
     *
     * @param entity submission for deleting
     * @throws Exception The provided submission does not exist in the database
     * @author Li Ching Ho
     */
    public void deleteSubmission(Submission entity) throws Exception {
        if (entity == null) {
            throw new Exception("Submission does not exist.");
        }
        // if id not exist, throw exception
        if (!exist(entity)) {
            throw new Exception("Submission of Student with username " + entity.getStudentUsername() + " conducting exam " + entity.getCourseId() + " " + entity.getExamName() + " exam does not exist.");
        }

        delByKey(entity.getId() + "");
    }

    /**
     * Delete the list of submissions provided.
     *
     * @param submissions List of submissions for deleting
     * @throws Exception There exist a submission provided which does not exist in the database.
     * @author Li Ching Ho
     */
    public void deleteSubmissions(List<Submission> submissions) throws Exception {
        for (Submission s : submissions) {
            deleteSubmission(s);
        }
    }

    /**
     * Delete all the submission in the database.
     *
     * @throws Exception There exist a submission provided which does not exist in the database.
     * @author Li Ching Ho
     */
    public void deleteAll() throws Exception {
        deleteSubmissions(getAll());
    }

}
