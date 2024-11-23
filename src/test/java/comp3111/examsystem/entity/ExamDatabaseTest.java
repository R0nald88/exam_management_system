package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExamDatabaseTest {
    List<Exam> exams;

    @Test
    public void testSavingExam() {
        exams = new ArrayList<>();

        Exam exam = new Exam();
        Course inputCourse = CourseDatabase.getInstance().getAll().get(0);
        List<Long> questionIds = QuestionDatabase.getInstance().getAll().stream().map(Entity::getId).toList();

        try {
            exam.setTime(10);
            exam.setPublished(false);
            exam.setName("Exam 1");
            exam.setCourseId(inputCourse.getCourseID());
            exam.setQuestionIds(questionIds);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "");
        }

        exams.add(exam);

        try {
            ExamDatabase.getInstance().deleteAll();
            ExamDatabase.getInstance().addExam(exam);
            assertEquals(ExamDatabase.getInstance().getAll(), exams);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "");
        }

        try {
            ExamDatabase.getInstance().addExam(exam);
            assertEquals(ExamDatabase.getInstance().getAll(), exams);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Course " + exam.getCourseId() + " cannot have two identical exam name: " + exam.getName() + ".");
        }

        try {
            ExamDatabase.getInstance().addExam(null);
            assertEquals(ExamDatabase.getInstance().getAll(), exams);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Exam does not exist.");
        }
    }

    @Test
    public void testSavingExams() {
        exams = new ArrayList<>();

        for (int a = 0; a < 4; a++) {
            Exam exam = new Exam();
            Course inputCourse = CourseDatabase.getInstance().getAll().get(0);
            List<Long> questionIds = QuestionDatabase.getInstance().getAll().stream().map(Entity::getId).toList();

            try {
                exam.setTime(a + 10);
                exam.setPublished(a % 2 == 0);
                exam.setName("Exam " + a);
                exam.setCourseId(inputCourse.getCourseID());
                exam.setQuestionIds(questionIds);
            } catch (Exception e) {
                assertEquals(e.getMessage(), "");
            }

            exams.add(exam);
        }

        try {
            ExamDatabase.getInstance().deleteAll();

            for (Exam e : exams) {
                ExamDatabase.getInstance().addExam(e);
            }

            assertEquals(ExamDatabase.getInstance().getAll(), exams);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "");
        }
    }

    @Test
    public void testUpdateExam() {
        testSavingExams();
        Exam updateExam = exams.get(0);
        long id = System.currentTimeMillis();

        try {
            updateExam.setTime(90);
            ExamDatabase.getInstance().updateExam(updateExam);
            assertEquals(ExamDatabase.getInstance().getAll(), exams);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "");
        }

        try {
            updateExam.setId(id);
            ExamDatabase.getInstance().updateExam(updateExam);
            assertEquals(ExamDatabase.getInstance().getAll(), exams);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Exam " + updateExam.getCourseId() + " " + updateExam.getName() + " does not exist.");
        }

        try {
            ExamDatabase.getInstance().updateExam(null);
            assertEquals(ExamDatabase.getInstance().getAll(), exams);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Exam does not exist.");
        }
    }

    @Test
    public void testDeleteExam() {
        testSavingExams();
        Exam deletedExam = exams.get(0);
        long id = System.currentTimeMillis();

        try {
            exams.remove(deletedExam);
            ExamDatabase.getInstance().deleteExam(deletedExam);
            assertEquals(ExamDatabase.getInstance().getAll(), exams);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "");
        }

        try {
            deletedExam.setId(id);
            ExamDatabase.getInstance().deleteExam(deletedExam);
            assertEquals(ExamDatabase.getInstance().getAll(), exams);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Exam " + deletedExam.getCourseId() + " " + deletedExam.getName() + " does not exist.");
        }

        try {
            ExamDatabase.getInstance().deleteExam(null);
            assertEquals(ExamDatabase.getInstance().getAll(), exams);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Exam does not exist.");
        }
    }

    @Test
    public void testFilterExam() {
        testSavingExams();
        Exam searchingExam = exams.get(0);
        List<Exam> e = ExamDatabase.getInstance().filter(searchingExam.getName(), searchingExam.getCourseId(), null);
        assertEquals(e.get(0), searchingExam);

        e = ExamDatabase.getInstance().queryByQuestion(searchingExam.getQuestionIds().get(0));
        assertEquals(e, exams);
    }
}
