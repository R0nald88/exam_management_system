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

/**
 * Storing exam information
 * @author Cheung Tuen King
 */
public class Exam extends Entity {
    /**
     * Store the exam name
     */
    private String name;

    /**
     * Store the exam time limit
     */
    private int time;

    /**
     * Store the id of the course in the course database the exam belongs to
     */
    private long longIdOfCourse;

    /**
     * Determine if the exam is published
     */
    private boolean published;

    /**
     * Store the question in the exam by their ids as listed in question database
     */
    private List<Long> questionIds;

    /**
     * Initialize new Exam entity with a new id assigned
     * @author Chueng Tuen King
     */
    public Exam() {
        super(System.currentTimeMillis());
    }

    /**
     * Access exam name
     * @return Exam name
     * @author Chueng Tuen King
     */
    public String getName() {
        return name;
    }

    /**
     * Access exam time in second
     * @return Exam time
     * @author Chueng Tuen King
     */
    public int getTime() {
        return time;
    }

    /**
     * Access id in course database of the course of the exam
     * @return Id in course database of the course of the exam
     * @author Chueng Tuen King
     */
    public long getLongIdOfCourse() {
        return longIdOfCourse;
    }

    /**
     * Access whether the exam is published
     * @return Boolean determining of the exam published
     * @author Chueng Tuen King
     */
    public boolean isPublished() {
        return published;
    }

    /**
     * Access whether the exam is published
     * @return "yes/ no" string
     * @author Chueng Tuen King
     */
    public String getPublished() {
        return published ? "yes" : "no";
    }

    /**
     * Access all ids in question database of the questions in the exam
     * @return List of question ids
     * @author Chueng Tuen King
     */
    public List<Long> getQuestionIds() {
        return questionIds;
    }

    /**
     * Access the full score of the exam according to the questions
     * @return Full score of the exam
     * @author Chueng Tuen King
     */
    public int getFullScore() {
        int fullScore = 0;
        if (!questionIds.isEmpty()) {
            for (Long id : questionIds) {
                Question question = QuestionDatabase.getInstance().queryByKey(id.toString());
                if (question != null) {
                    fullScore += question.getScore();
                }
            }
            return fullScore;
        }
        return -1;
    }

    /**
     * Set the question in the exam by their ids.
     * @param questionIds List of question ids as listed in question database
     * @author Chueng Tuen King
     * @throws Exception Input list is empty, question id duplicated in exam or not exist in question database
     */
    public void setQuestionIds(List<Long> questionIds) throws Exception {
        if (questionIds == null || questionIds.isEmpty()) {
            throw new Exception("Exam should contain at least 1 question.");
        }
        this.questionIds = new ArrayList<>();
        for (Long id : questionIds) {
            addQuestionId(id);
        }
    }

    /**
     * Add the question to the exam
     * @param question Question for adding
     * @author Chueng Tuen King
     * @throws Exception Question duplicated in exam or not exist in question database
     */
    public void addQuestion(Question question) throws Exception {
        if (question == null) {
            throw new Exception("Question does not exist.");
        }
        addQuestionId(question.getId());
    }

    /**
     * Add the question to the exam by their id
     * @param id Id of question for adding
     * @author Chueng Tuen King
     * @throws Exception Question id duplicated in exam or not exist in question database
     */
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

    /**
     * Search for the index (question number) of a question in the exam according to its id.
     * Return -1 if no corresponding question id
     * @param qId Id of question for searching
     * @author Chueng Tuen King
     * @return Index of question id
     */
    public int searchQuestionIdInExam(Long qId) {
        for (int i = 0; i < questionIds.size(); i++) {
            if (Objects.equals(questionIds.get(i), qId)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Delete the given question in the exam
     * @param question Question for deleting
     * @author Chueng Tuen King
     * @throws Exception Question does not exist in the exam
     */
    public void deleteQuestion(Question question) throws Exception {
        if (question == null) {
            throw new Exception("Question does not exist.");
        }
        deleteQuestionId(question.getId());
    }

    /**
     * Delete the given question in the exam according to their ids
     * @param id Question id for deleting
     * @author Chueng Tuen King
     * @throws Exception Question id does not exist in the exam
     */
    public void deleteQuestionId(long id) throws Exception {
        if (questionIds == null || !questionIds.contains(id)) {
            throw new Exception("Question " + id + " does not exist.");
        }
        questionIds.remove(id);
    }

    /**
     * Set if the exam is published
     * @param published Parameter deciding if exam is published
     * @author Chueng Tuen King
     */
    public void setPublished(boolean published) {
        this.published = published;
    }

    /**
     * Access the course the exam belongs to.
     * Return null if course does not exist
     * @author Chueng Tuen King
     * @return Course the exam belongs to
     */
    public Course getCourse() {
        List<Course> c = CourseDatabase.getInstance().queryByField("id", longIdOfCourse + "");
        return c.isEmpty() ? null : c.getFirst();
    }

    /**
     * Access the course id the exam belongs to.
     * Return null if course does not exist
     * @author Chueng Tuen King
     * @return Course id the exam belongs to
     */
    public String getCourseId() {
        Course c = getCourse();
        return c == null ? null : c.getCourseID();
    }

    /**
     * Set the course id of the exam
     * @param c Course id of the course
     * @author Chueng Tuen King
     * @throws Exception Course id does not exist
     */
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

    /**
     * Set the exam time.
     * It should be an integer within range (ExamDatabase.EXAM_TIME_LOWER_LIMIT, ExamDatabase.EXAM_TIME_UPPER_LIMIT)
     * @param time Exam time
     * @author Chueng Tuen King
     * @throws Exception Exam time out of range
     */
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

    /**
     * Set the exam time.
     * It should be an integer within range (ExamDatabase.EXAM_TIME_LOWER_LIMIT, ExamDatabase.EXAM_TIME_UPPER_LIMIT)
     * @param time Exam time in string format
     * @author Chueng Tuen King
     * @throws Exception Exam time out of range or is not an integer
     */
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

    /**
     * Set the exam name.
     * Length of name should be in range (0, ExamDatabase.EXAM_NAME_LENGTH_LIMIT)
     * @param name Exam time
     * @author Chueng Tuen King
     * @throws Exception Exam name length out of range
     */
    public void setName(String name) throws Exception {
        name = name.trim();
        Database.validateTextLength(ExamDatabase.EXAM_NAME_LENGTH_LIMIT, name, "exam name");
        this.name = name;
    }

    /**
     * Get the json string format of exam entity
     * @author Chueng Tuen King
     * @return Json string representation of exam entity
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Check if 2 exams are equal
     * @author Chueng Tuen King
     * @return Boolean determining if 2 exam entity equals
     */
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

    /**
     * Access the hash code if the exam entity
     * @author Chueng Tuen King
     * @return Hash code of exam entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, time, longIdOfCourse, published, questionIds);
    }
}
