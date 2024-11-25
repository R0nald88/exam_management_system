package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SubmissionDatabaseTest {

    private SubmissionDatabase submissionDatabase;

    @BeforeEach
    public void setUp() {
        submissionDatabase = SubmissionDatabase.getInstance();
        try {
            submissionDatabase.deleteAll(); // Clear the database before each test
        } catch (Exception ignored){
        }

    }

    @Test
    public void testAddSubmission() throws Exception {
        Submission submission = new Submission();
        submission.setStudentUsername("student1");
        submission.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        submissionDatabase.addSubmission(submission);
        assertTrue(submissionDatabase.exist(submission));
    }

    @Test
    public void testAddDuplicateSubmission() throws Exception {
        Submission submission1 = new Submission();
        submission1.setStudentUsername("student1");
        submission1.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        Submission submission2 = new Submission();
        submission2.setStudentUsername("student1");
        submission2.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        submissionDatabase.addSubmission(submission1);

        Exception exception = assertThrows(Exception.class, () -> {
            submissionDatabase.addSubmission(submission2);
        });

        assertEquals("Student student1 cannot have 2 submission for exam " + ExamDatabase.getInstance().getAll().getFirst().getCourseId() + " " + ExamDatabase.getInstance().getAll().getFirst().getName() + ".", exception.getMessage());
    }

    @Test
    public void testUpdateSubmission() throws Exception {
        Submission submission = new Submission();
        submission.setStudentUsername("student1");
        submission.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());
        submission.setTimeSpend(40);

        submissionDatabase.addSubmission(submission);

        submission.setTimeSpend(20);
        submissionDatabase.updateSubmission(submission);

        assertEquals(20, submissionDatabase.queryByKey(submission.getId().toString()).getTimeSpend());
    }

    @Test
    public void testDeleteSubmission() throws Exception {
        Submission submission = new Submission();
        submission.setStudentUsername("student1");
        submission.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        submissionDatabase.addSubmission(submission);
        submissionDatabase.deleteSubmission(submission);

        assertFalse(submissionDatabase.exist(submission));
    }

    @Test
    public void testFilterSubmissions() throws Exception {
        Submission submission1 = new Submission();
        submission1.setStudentUsername("student1");
        submission1.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        Submission submission2 = new Submission();
        submission2.setStudentUsername("student2");
        submission2.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        submissionDatabase.addSubmission(submission1);
        submissionDatabase.addSubmission(submission2);

        List<Submission> filtered = submissionDatabase.filter("student1", ExamDatabase.getInstance().getAll().getFirst().getCourseId(), ExamDatabase.getInstance().getAll().getFirst().getId().toString());
        assertEquals(1, filtered.size());
        assertEquals("student1", filtered.getFirst().getStudentUsername());
    }

    @Test
    public void testDeleteAllSubmissions() throws Exception {
        Submission submission1 = new Submission();
        submission1.setStudentUsername("student1");
        submission1.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        Submission submission2 = new Submission();
        submission2.setStudentUsername("student2");
        submission2.setExamId(ExamDatabase.getInstance().getAll().getFirst().getId());

        submissionDatabase.addSubmission(submission1);
        submissionDatabase.addSubmission(submission2);

        submissionDatabase.deleteAll();
        assertEquals(0, submissionDatabase.getAll().size());
    }
}