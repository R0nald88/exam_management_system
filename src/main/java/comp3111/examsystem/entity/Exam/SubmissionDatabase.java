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

    public List<Submission> filter(String studentId, String courseId, String examId) {
        List<Submission> nameList = (studentId == null || studentId.isEmpty()) ?
                getAll() : queryFuzzyByField("studentId", studentId);

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
        System.out.println(entity.getStudentId().toString() + " " + entity.getExamId().toString());
        // if id exist, assign a new id
        if (exist(entity)) {
            entity.setId(System.currentTimeMillis());
        }

        // cannot have two identical submission for 1 student
        if (!filter(entity.getStudentId().toString(), ExamDatabase.getInstance().queryByKey(entity.getExamId().toString()).getCourseId(), entity.getExamId().toString()).isEmpty()) {
            throw new Exception("Student " + entity.getStudentId().toString() + " cannot have 2 submission for exam " + ExamDatabase.getInstance().queryByKey(entity.getExamId().toString()).getCourseId() + " " + entity.getExamId().toString() + ".");
        }
        super.add(entity);
    }

    public void updateSubmission(Submission entity) throws Exception {
        if (entity == null) {
            throw new Exception("submission does not exist.");
        }
        // if id not exist, throw exception
        if (!exist(entity)) {
            throw new Exception("Submission of Student with id " + entity.getStudentId() + " conducting exam with id " + entity.getExamId() + " exam does not exist.");
        }

        update(entity);
    }


}
