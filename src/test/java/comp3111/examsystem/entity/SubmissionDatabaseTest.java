package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SubmissionDatabaseTest {


    @BeforeEach
    public void setUp() {
        try {
            for (Course c: CourseDatabase.getInstance().getAll()) {
                CourseDatabase.getInstance().delByKey(c.getId().toString());
            }
            ExamDatabase.getInstance().deleteAll();
            SubmissionDatabase.getInstance().deleteAll(); // Clear the database before each test
        } catch (Exception ignored){
        }

    }

    @Test
    public void testAddSubmission() throws Exception {
        Submission submission = new Submission();
        submission.setStudentUsername("student1");
        Course course = new Course("TEST1001", "Testing 1001", "TEST");
        CourseDatabase.getInstance().add(course);
        Exam exam = new Exam();
        exam.setCourseId("TEST1001");
        exam.setName("Exam");
        for (Question q: QuestionDatabase.getInstance().getAll()) {
            exam.addQuestion(q);
        }
        ExamDatabase.getInstance().add(exam);
        submission.setExamId(exam.getId());

        SubmissionDatabase.getInstance().addSubmission(submission);
        assertTrue(SubmissionDatabase.getInstance().exist(submission));
    }

    @Test
    public void testAddDuplicateSubmission() throws Exception {
        Course course = new Course("TEST1003", "Testing 1003", "TEST");
        CourseDatabase.getInstance().add(course);
        Exam exam = new Exam();
        exam.setCourseId("TEST1003");
        exam.setName("Exam");
        for (Question q: QuestionDatabase.getInstance().getAll()) {
            exam.addQuestion(q);
        }

        ExamDatabase.getInstance().add(exam);

        Submission submission1 = new Submission();
        submission1.setStudentUsername("student1");
        submission1.setExamId(exam.getId());

        Submission submission2 = new Submission();
        submission2.setStudentUsername("student1");
        submission2.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        SubmissionDatabase.getInstance().addSubmission(submission1);

        Exception exception = assertThrows(Exception.class, () -> {
            SubmissionDatabase.getInstance().addSubmission(submission2);
        });

        assertEquals("Student student1 cannot have 2 submission for exam " + ExamDatabase.getInstance().getAll().getFirst().getCourseId() + " " + ExamDatabase.getInstance().getAll().getFirst().getName() + ".", exception.getMessage());
    }

    @Test
    public void testUpdateSubmission() throws Exception {
        Course course = new Course("TEST1002", "Testing 1002", "TEST");
        CourseDatabase.getInstance().add(course);
        Exam exam = new Exam();
        exam.setCourseId("TEST1002");
        exam.setName("Exam");
        for (Question q: QuestionDatabase.getInstance().getAll()) {
            exam.addQuestion(q);
        }
        ExamDatabase.getInstance().addExam(exam);
        Submission submission = new Submission();
        submission.setStudentUsername("student1");
        submission.setExamId(exam.getId());
        submission.setTimeSpend(40);

        SubmissionDatabase.getInstance().addSubmission(submission);

        submission.setTimeSpend(20);
        SubmissionDatabase.getInstance().updateSubmission(submission);

        assertEquals(20, SubmissionDatabase.getInstance().queryByKey(submission.getId().toString()).getTimeSpend());
    }

    @Test
    public void testDeleteSubmission() throws Exception {
        Submission submission = new Submission();
        submission.setStudentUsername("student1");
        Course course = new Course("TEST1004", "Testing 1004", "TEST");
        CourseDatabase.getInstance().add(course);
        Exam exam = new Exam();
        exam.setCourseId("TEST1004");
        exam.setName("Exam");
        for (Question q: QuestionDatabase.getInstance().getAll()) {
            exam.addQuestion(q);
        }
        ExamDatabase.getInstance().addExam(exam);

        submission.setExamId(exam.getId());

        SubmissionDatabase.getInstance().addSubmission(submission);
        SubmissionDatabase.getInstance().deleteSubmission(submission);

        assertFalse(SubmissionDatabase.getInstance().exist(submission));
    }

    @Test
    public void testFilterSubmissions() throws Exception {
        Course course = new Course("TEST1005", "Testing 1005", "TEST");
        CourseDatabase.getInstance().add(course);
        CourseDatabase.getInstance().add(course);
        Exam exam = new Exam();
        exam.setCourseId("TEST1005");
        exam.setName("Exam");
        for (Question q: QuestionDatabase.getInstance().getAll()) {
            exam.addQuestion(q);
        }
        ExamDatabase.getInstance().add(exam);

        Submission submission1 = new Submission();
        submission1.setStudentUsername("student1");
        submission1.setExamId(exam.getId());

        Submission submission3 = new Submission();
        submission3.setStudentUsername("student2");
        submission3.setExamId(exam.getId());

        SubmissionDatabase.getInstance().addSubmission(submission1);
        SubmissionDatabase.getInstance().addSubmission(submission3);

        List<Submission> filtered = SubmissionDatabase.getInstance().filter("student1", null, null);
        assertEquals(1, filtered.size());

        List<Submission> filtered1 = SubmissionDatabase.getInstance().filter("student2", exam.getCourseId(), exam.getId().toString());
        assertEquals(1, filtered1.size());
    }

    @Test
    public void testDeleteAllSubmissions() throws Exception {
        Course course = new Course("TEST1007", "Testing 1007", "TEST");
        CourseDatabase.getInstance().add(course);
        Exam exam = new Exam();
        exam.setCourseId("TEST1007");
        exam.setName("Exam");
        for (Question q: QuestionDatabase.getInstance().getAll()) {
            exam.addQuestion(q);
        }
        ExamDatabase.getInstance().addExam(exam);



        Submission submission1 = new Submission();
        submission1.setStudentUsername("student1");
        submission1.setExamId(exam.getId());

        Submission submission2 = new Submission();
        submission2.setStudentUsername("student2");
        submission2.setExamId(exam.getId());
        SubmissionDatabase.getInstance().addSubmission(submission1);
        SubmissionDatabase.getInstance().addSubmission(submission2);

        SubmissionDatabase.getInstance().deleteAll();
        assertEquals(0, SubmissionDatabase.getInstance().getAll().size());
    }
}