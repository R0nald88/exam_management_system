package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SubmissionDatabaseTest {


    @BeforeEach
    public void setUp() {
        try {
            SubmissionDatabase.getInstance().deleteAll(); // Clear the database before each test
        } catch (Exception ignored){
        }

    }

    @Test
    public void testAddSubmission() throws Exception {
        Submission submission = new Submission();
        submission.setStudentUsername("student1");
        submission.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        SubmissionDatabase.getInstance().addSubmission(submission);
        assertTrue(SubmissionDatabase.getInstance().exist(submission));
    }

    @Test
    public void testAddDuplicateSubmission() throws Exception {
        Submission submission1 = new Submission();
        submission1.setStudentUsername("student1");
        submission1.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

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
        Submission submission = new Submission();
        submission.setStudentUsername("student1");
        submission.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());
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
        submission.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        SubmissionDatabase.getInstance().addSubmission(submission);
        SubmissionDatabase.getInstance().deleteSubmission(submission);

        assertFalse(SubmissionDatabase.getInstance().exist(submission));
    }

    @Test
    public void testFilterSubmissions() throws Exception {
        Submission submission1 = new Submission();
        submission1.setStudentUsername("student1");
        submission1.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        Submission submission2 = new Submission();
        submission2.setStudentUsername("student1");
        submission2.setExamId(ExamDatabase.getInstance().getAll().getLast().getId());

        Submission submission3 = new Submission();
        submission3.setStudentUsername("student2");
        submission3.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        SubmissionDatabase.getInstance().addSubmission(submission1);
        SubmissionDatabase.getInstance().addSubmission(submission2);
        SubmissionDatabase.getInstance().addSubmission(submission3);

        List<Submission> filtered1 = SubmissionDatabase.getInstance().filter("student1", null, null);
        assertEquals(2, filtered1.size());

        List<Submission> filtered2 = SubmissionDatabase.getInstance().filter("student1", ExamDatabase.getInstance().getAll().getLast().getCourseId(), ExamDatabase.getInstance().getAll().getLast().getId().toString());
        assertEquals(1, filtered2.size());
    }

    @Test
    public void testDeleteAllSubmissions() throws Exception {
        Submission submission1 = new Submission();
        submission1.setStudentUsername("student1");
        submission1.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        Submission submission2 = new Submission();
        submission2.setStudentUsername("student2");
        submission2.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        SubmissionDatabase.getInstance().addSubmission(submission1);
        SubmissionDatabase.getInstance().addSubmission(submission2);

        SubmissionDatabase.getInstance().deleteAll();
        assertEquals(0, SubmissionDatabase.getInstance().getAll().size());
    }
}