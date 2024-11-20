package comp3111.examsystem.entity.Exam;

import comp3111.examsystem.tools.Database;

import java.util.List;

public class SubmissionDatabase extends Database<Submission> {
    private static SubmissionDatabase instance = null;

    private SubmissionDatabase() {
        super(Submission.class);
    }

    public static SubmissionDatabase getInstance() {
        if (instance == null) instance = new SubmissionDatabase();
        return instance;
    }

    public List<Submission> filter(String studentUsername, String courseId, String examId) {
        List<Submission> nameList = (studentUsername == null || studentUsername.isEmpty()) ?
                getAll() : queryFuzzyByField("studentUsername", studentUsername);

        List<Submission> courseList = (courseId == null || courseId.isEmpty()) ?
                getAll() : queryByField("courseId", courseId);

        List<Submission> examList = (examId == null || examId.isEmpty()) ?
                getAll() : queryByField("examId", examId);

        return join(nameList, join(courseList, examList));
    }

    public boolean exist(long id) {
        return queryByKey(id + "") != null;
    }

    public boolean exist(Submission submission) {
        return exist(submission.getId());
    }

    public void addSubmission(Submission entity) throws Exception {
        if (entity == null) {
            System.out.println("submission entity is null");
            throw new Exception("Submission does not exist.");
        }
        System.out.println("SubmissionDatabase.addSubmission, entity.getStudentId().toString() and entity.getExamId().toString():" + entity.getStudentUsername() + " " + entity.getExamId().toString());
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

    public void updateSubmission(Submission entity) throws Exception {
        if (entity == null) {
            throw new Exception("submission does not exist.");
        }
        // if id not exist, throw exception
        if (!exist(entity)) {
            throw new Exception("Submission of Student with username " + entity.getStudentUsername() + " conducting exam with id " + entity.getExamId() + " exam does not exist.");
        }

        update(entity);
    }

    public void deleteSubmission(Submission entity) throws Exception {
        if (entity == null) {
            throw new Exception("Submission does not exist.");
        }
        // if id not exist, throw exception
        if (!exist(entity)) {
            throw new Exception("Submission from student: " + entity.getStudentUsername() + ", course: " + entity.getCourseId() + ", exam: " + entity.getExamId() + " does not exist.");
        }

        delByKey(entity.getId() + "");
    }

    public void deleteSubmissions(List<Submission> submissions) throws Exception {
        for (Submission s : submissions) {
            deleteSubmission(s);
        }
    }

    public void deleteAll() throws Exception {
        deleteSubmissions(getAll());
    }

}
