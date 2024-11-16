package comp3111.examsystem.entity.Exam;

import comp3111.examsystem.tools.Database;

import java.util.List;

public class ExamDatabase extends Database<Exam> {
    public static final int EXAM_TIME_UPPER_LIMIT = -1;
    public static final int EXAM_TIME_LOWER_LIMIT = -1;
    public static final int EXAM_NAME_LENGTH_LIMIT = -1;
    private static ExamDatabase instance = null;

    private ExamDatabase() {
        super(Exam.class);
    }

    public static ExamDatabase getInstance() {
        if (instance == null) instance = new ExamDatabase();
        return instance;
    }

    public List<Exam> filter(String name, String courseId, String published) {
        List<Exam> nameList = (name == null || name.isEmpty()) ?
                getAll() : queryFuzzyByField("name", name);

        List<Exam> courseList = (courseId == null || courseId.isEmpty()) ?
                getAll() : queryByField("courseId", courseId);

        List<Exam> publishedList = (published == null || published.isEmpty()) ?
                getAll() : queryByField("published", published);

        return join(nameList, join(courseList, publishedList));
    }

    public boolean exist(long id) {
        return queryByKey(id + "") != null;
    }

    public boolean exist(Exam exam) {
        return exist(exam.getId());
    }

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

    public void deleteExams(List<Exam> exams) throws Exception {
        for (Exam e : exams) {
            deleteExam(e);
        }
    }

    public void deleteAll() throws Exception {
        deleteExams(getAll());
    }
}
