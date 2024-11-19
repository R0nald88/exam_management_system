package comp3111.examsystem.entity.Exam;

import com.google.gson.Gson;
import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.tools.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Exam extends Entity {
    private String name;
    private int time;
    private long longIdOfCourse;
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

    public long getLongIdOfCourse() {
        return longIdOfCourse;
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

    public int getFullScore() {
        int fullScore = 0;
        if (!questionIds.isEmpty()) {
            for (Long id : questionIds) {
                fullScore += QuestionDatabase.getInstance().queryByKey(id.toString()).getScore();
            }
            return fullScore;
        }
        return -1;
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

    public Course getCourse() {
        List<Course> c = CourseDatabase.getInstance().queryByField("id", longIdOfCourse + "");
        return c.isEmpty() ? null : c.getFirst();
    }

    public String getCourseId() {
        Course c = getCourse();
        return c == null ? null : c.getCourseID();
    }

    public void setCourseId(String c) throws Exception {
        if (c == null || c.trim().isEmpty()) {
            throw new Exception("Course ID does not exist.");
        }

        c = c.trim();
        List<Course> list = CourseDatabase.getInstance().queryByField("courseID", c);

        if (list.isEmpty()) {
            throw new Exception("Course ID " + c + " does not exist.");
        }

        longIdOfCourse = list.getFirst().getId();
    }

    public void setTime(int time) throws Exception {
        Database.validateNumberRange(
                ExamDatabase.EXAM_TIME_LOWER_LIMIT,
                ExamDatabase.EXAM_TIME_UPPER_LIMIT,
                time,
                "exam time",
                "second(s)"
        );
        this.time = time;
    }

    public void setTime(String time) throws Exception {
        time = time.trim();
        Database.validateNumberRange(
                ExamDatabase.EXAM_TIME_LOWER_LIMIT,
                ExamDatabase.EXAM_TIME_UPPER_LIMIT,
                time,
                "exam time",
                "second(s)"
        );
        this.time = Integer.parseInt(time);
    }

    public void setName(String name) throws Exception {
        name = name.trim();
        Database.validateTextLength(ExamDatabase.EXAM_NAME_LENGTH_LIMIT, name, "exam name");
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
                Objects.equals(longIdOfCourse, exam.longIdOfCourse) &&
                Objects.equals(questionIds, exam.questionIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, time, longIdOfCourse, published, questionIds);
    }
}
