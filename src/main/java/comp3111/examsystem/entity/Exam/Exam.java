package comp3111.examsystem.entity.Exam;

import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Exam extends Entity {
    public static final int EXAM_TIME_UPPER_LIMIT = -1;
    public static final int EXAM_TIME_LOWER_LIMIT = -1;
    public static final int EXAM_NAME_LENGTH_LIMIT = -1;

    private String name;
    private int time;
    private String courseId;
    private boolean published;
    private List<Long> questionIds;

    public Exam() {
        super(System.currentTimeMillis());
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public String getCourseId() {
        return courseId;
    }

    public boolean isPublished() {
        return published;
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) {
        this.questionIds = questionIds;
    }

    public void addQuestion(Question question) throws Exception {
        if (question == null) {
            throw new Exception("Question does not exist.");
        }
        addQuestionId(question.getId());
    }

    public void addQuestionId(long id) throws Exception {
        if (!QuestionDatabase.getInstance().exist(id)) {
            throw new Exception("Question " + id + " does not exist.");
        }
        if (questionIds == null) {
            questionIds = new ArrayList<>();
        }
        questionIds.add(id);
    }

    public void deleteQuestion(Question question) throws Exception {
        if (question == null) {
            throw new Exception("Question does not exist.");
        }
        deleteQuestionId(question.getId());
    }

    public void deleteQuestionId(long id) throws Exception {
        if (questionIds == null || !questionIds.contains(id)) {
            throw new Exception("Question " + id + " does not exist.");
        }
        questionIds.remove(id);
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setCourseId(String courseId) {
        // TODO: validate courseID exist
        this.courseId = courseId.trim();
    }

    public void setTime(int time) throws Exception {
        String error = "Invalid exam time.";
        if (EXAM_TIME_UPPER_LIMIT > EXAM_TIME_LOWER_LIMIT && EXAM_TIME_LOWER_LIMIT >= 0) {
            error = "Please input a valid exam time between " + EXAM_TIME_LOWER_LIMIT +
                    " and " + EXAM_TIME_UPPER_LIMIT + " second(s).";
        } else if (EXAM_TIME_UPPER_LIMIT > 0) {
            error = "Please input a valid exam time less than " + EXAM_TIME_UPPER_LIMIT + " second(s).";
        } else if (EXAM_TIME_LOWER_LIMIT >= 0) {
            error = "Please input a valid exam time larger than " + EXAM_TIME_LOWER_LIMIT + " second(s).";
        }

        if ((EXAM_TIME_LOWER_LIMIT >= 0 && time < EXAM_TIME_LOWER_LIMIT) ||
            (EXAM_TIME_UPPER_LIMIT > 0 && time > EXAM_TIME_UPPER_LIMIT)) {
            throw new Exception(error);
        }
        this.time = time;
    }

    public void setTime(String time) throws Exception {
        time = time.trim();

        if (time.isEmpty()) {
            throw new Exception("Please enter the exam time.");
        }

        int t = -1;
        try {
            t = Integer.parseInt(time);
        } catch (Exception e) {
            throw new Exception("Exam time should be an integer.");
        }

        setTime(t);
    }

    public void setName(String name) throws Exception {
        name = name.trim();
        if (name.isEmpty()) {
            throw new Exception("Please enter the exam name.");
        }
        if (EXAM_NAME_LENGTH_LIMIT > 0 && name.length() > EXAM_NAME_LENGTH_LIMIT) {
            throw new Exception("Length of exam name should not be larger than " + EXAM_NAME_LENGTH_LIMIT + ".");
        }
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return time == exam.time &&
                published == exam.published &&
                Objects.equals(name, exam.name) &&
                Objects.equals(courseId, exam.courseId) &&
                Objects.equals(questionIds, exam.questionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, time, courseId, published, questionIds);
    }
}
