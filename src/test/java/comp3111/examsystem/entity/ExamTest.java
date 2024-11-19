package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExamTest {
    private Exam exam;

    @Test
    public void testExamInput() {
        String inputName = "Exam", inputCourseId = "COMP2130";
        boolean inputPublished = true;
        int inputTime = 100;
        List<Long> questionIds = List.of(1731594430470L);

        try {
            exam = new Exam();
            exam.setTime(inputTime);
            exam.setPublished(inputPublished);
            exam.setName(inputName);
            // exam.setLongIdOfCourse(inputCourseId);
            exam.setQuestionIds(questionIds);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assertEquals(exam.getName(), inputName);
        assertEquals(exam.getTime(), inputTime);
        assertEquals(exam.isPublished(), inputPublished);
        assertEquals(exam.getCourseId(), inputCourseId);
        assertEquals(exam.getQuestionIds(), questionIds);
    }

    @Test
    public void testSavingExam() {
        try {
            ExamDatabase.getInstance().deleteAll();

            testExamInput();
            ExamDatabase.getInstance().addExam(exam);
            Exam e = ExamDatabase.getInstance().queryByKey(exam.getId() + "");
            System.out.println(e.toString());
            assertEquals(exam, e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
