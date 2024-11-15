package comp3111.examsystem.entity.Exam;

import com.google.gson.Gson;
import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Exam extends Entity {
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

    public String getPublished() {
        return published ? "yes" : "no";
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) throws Exception {
        if (questionIds == null || questionIds.isEmpty()) {
            throw new Exception("Exam should contain at least 1 question.");
        }
        this.questionIds = new ArrayList<>();
        for (Long id : questionIds) {
            addQuestionId(id);
        }
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
        if (questionIds.contains(id)) {
            throw new Exception("Question " + id + " duplicated.");
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
        if (ExamDatabase.EXAM_TIME_UPPER_LIMIT > ExamDatabase.EXAM_TIME_LOWER_LIMIT && ExamDatabase.EXAM_TIME_LOWER_LIMIT >= 0) {
            error = "Please input a valid exam time between " + ExamDatabase.EXAM_TIME_LOWER_LIMIT +
                    " and " + ExamDatabase.EXAM_TIME_UPPER_LIMIT + " second(s).";
        } else if (ExamDatabase.EXAM_TIME_UPPER_LIMIT > 0) {
            error = "Please input a valid exam time less than " + ExamDatabase.EXAM_TIME_UPPER_LIMIT + " second(s).";
        } else if (ExamDatabase.EXAM_TIME_LOWER_LIMIT >= 0) {
            error = "Please input a valid exam time larger than " + ExamDatabase.EXAM_TIME_LOWER_LIMIT + " second(s).";
        }

        if ((ExamDatabase.EXAM_TIME_LOWER_LIMIT >= 0 && time < ExamDatabase.EXAM_TIME_LOWER_LIMIT) ||
            (ExamDatabase.EXAM_TIME_UPPER_LIMIT > 0 && time > ExamDatabase.EXAM_TIME_UPPER_LIMIT)) {
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
        if (ExamDatabase.EXAM_NAME_LENGTH_LIMIT > 0 && name.length() > ExamDatabase.EXAM_NAME_LENGTH_LIMIT) {
            throw new Exception("Length of exam name should not be larger than " + ExamDatabase.EXAM_NAME_LENGTH_LIMIT + ".");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
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
