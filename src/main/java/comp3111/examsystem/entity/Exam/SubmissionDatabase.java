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

    public List<Submission> filter(String studentName, String courseId, String examId) {
        List<Submission> nameList = (studentName == null || studentName.isEmpty()) ?
                getAll() : queryFuzzyByField("name", studentName);

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
            throw new Exception("Submission does not exist.");
        }
        // if id exist, assign a new id
        if (exist(entity)) {
            entity.setId(System.currentTimeMillis());
        }

        // cannot have two identical submission for 1 student
        if (!filter(entity.getStudent().getName(), entity.getExam().getCourseId(), entity.getExam().getId().toString()).isEmpty()) {
            throw new Exception("Student " + entity.getStudent().getName() + " cannot have 2 submission for exam " + entity.getExam().getId().toString() + ".");
        }
        super.add(entity);
    }

    public void updateSubmission(Submission entity) throws Exception {
        if (entity == null) {
            throw new Exception("submission does not exist.");
        }
        // if id not exist, throw exception
        if (!exist(entity)) {
            throw new Exception("Submission of " + entity.getStudent().getName() + " conducting " + entity.getExam().getName() + " exam does not exist.");
        }

        update(entity);
    }


}
