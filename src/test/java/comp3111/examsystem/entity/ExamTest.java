package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExamTest {
    private Exam exam;

    @Test
    public void testExamInput() {
        String inputName = "Exam";
        Course inputCourse = CourseDatabase.getInstance().getAll().get(0);
        boolean inputPublished = true;
        int inputTime = 100;
        List<Long> questionIds = QuestionDatabase.getInstance().getAll().stream().map(Entity::getId).toList();

        try {
            exam = new Exam();
            exam.setTime(inputTime);
            exam.setTime(inputTime + "");
            exam.setPublished(inputPublished);
            exam.setName(inputName);
            exam.setCourseId(inputCourse.getCourseID());
            exam.setQuestionIds(questionIds);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(exam.getName(), inputName);
        assertEquals(exam.getTime(), inputTime);
        assertEquals(exam.isPublished(), inputPublished);
        assertEquals(exam.getPublished(), "yes");
        assertEquals(exam.getCourseId(), inputCourse.getCourseID());
        assertEquals(exam.getLongIdOfCourse(), inputCourse.getId());
        assertEquals(exam.getQuestionIds(), questionIds);
        assertEquals(exam.searchQuestionIdInExam(questionIds.get(0)), 0);
        assertEquals(exam.getFullScore(), sum(QuestionDatabase.getInstance().getAll().stream().map(Question::getScore).toList()));
    }

    public int sum(List<Integer> i) {
        int s = 0;
        for (int a : i) s += a;
        return s;
    }

    @Test
    public void testInvalidQuestionIds() {
        testExamInput();
        long id = System.currentTimeMillis();

        try {
            exam.setQuestionIds(new ArrayList<>());
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Exam should contain at least 1 question.");
        }

        try {
            exam.addQuestionId(id);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Question " + id + " does not exist.");
        }

        try {
            exam.addQuestionId(exam.getQuestionIds().get(0));
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Question " + exam.getQuestionIds().get(0) + " duplicated.");
        }

        try {
            exam.addQuestion(null);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Question does not exist.");
        }

        try {
            exam.deleteQuestion(null);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Question does not exist.");
        }

        try {
            exam.deleteQuestionId(id);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Question " + id + " does not exist.");
        }
    }

    @Test
    public void testInvalidCourseIdInput() {
        testExamInput();

        try {
            exam.setCourseId(null);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Course ID does not exist.");
        }

        try {
            exam.setCourseId("GGGG");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Course ID GGGG does not exist.");
        }
    }

    @Test
    public void testSavingExam() {
        try {
            ExamDatabase.getInstance().deleteAll();

            testExamInput();
            ExamDatabase.getInstance().addExam(exam);
            Exam e = ExamDatabase.getInstance().queryByKey(exam.getId() + "");
            assertEquals(exam, e);
            assertEquals(exam.toString(), e.toString());
            assertEquals(exam.hashCode(), e.hashCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
