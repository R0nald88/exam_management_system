package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Personnel.StudentDatabase;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubmissionTest {
    private static Submission submission = new Submission();
    private static Exam exam = new Exam();
    private static Question singleQ = new Question();
    private static Question multipleQ = new Question();
    private static Question tfQ = new Question();
    private static Question shortQ = new Question();

    @BeforeAll
    public static void setUp() {
        try {
            singleQ.setType(QuestionType.SINGLE);
            singleQ.setOptionA("A");
            singleQ.setOptionB("B");
            singleQ.setOptionC("C");
            singleQ.setOptionD("D");
            singleQ.setQuestion("Single Question: ans=A " + System.currentTimeMillis());
            singleQ.setScore(5);
            singleQ.setAnswer("A");
            QuestionDatabase.getInstance().addQuestion(singleQ);

            multipleQ.setType(QuestionType.MULTIPLE);
            multipleQ.setOptionA("A");
            multipleQ.setOptionB("B");
            multipleQ.setOptionC("C");
            multipleQ.setOptionD("D");
            multipleQ.setQuestion("Multiple Question: ans=AB " + System.currentTimeMillis());
            multipleQ.setScore(10);
            multipleQ.setAnswer("AB");
            Thread.sleep(10);
            QuestionDatabase.getInstance().addQuestion(multipleQ);

            tfQ.setType(QuestionType.TRUE_FALSE);
            tfQ.setQuestion("T/F Question: ans=T " + System.currentTimeMillis());
            tfQ.setScore(5);
            tfQ.setAnswer("T");
            Thread.sleep(10);
            QuestionDatabase.getInstance().addQuestion(tfQ);

            shortQ.setType(QuestionType.SHORT_Q);
            shortQ.setQuestion("Short Question: ans=Short Question" + System.currentTimeMillis());
            shortQ.setScore(20);
            shortQ.setAnswer("Short Question");
            Thread.sleep(10);
            QuestionDatabase.getInstance().addQuestion(shortQ);

            exam.setName("Testing Exam " + System.currentTimeMillis());
            exam.setPublished(true);
            exam.addQuestion(singleQ);
            exam.addQuestion(multipleQ);
            exam.addQuestion(tfQ);
            exam.addQuestion(shortQ);
            exam.setCourseId("COMP3111");
            exam.setTime(60);
            ExamDatabase.getInstance().addExam(exam);
            submission.setExamId(exam.getId());
            submission.setStudentUsername("Testing" + String.valueOf(System.currentTimeMillis()));
            submission.setTimeSpend(50);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveAnswer() {
        submission.saveAnswer(0, "B");
        assertEquals("B", submission.getAnswerList().get(0));
        submission.saveAnswer(1, "AB");
        assertEquals("AB", submission.getAnswerList().get(1));
        submission.saveAnswer(2, "T");
        assertEquals("T", submission.getAnswerList().get(2));
        submission.saveAnswer(3,"Short");
        assertEquals("Short", submission.getAnswerList().get(3));
        submission.saveAnswer(0, "A");
        assertEquals("A", submission.getAnswerList().get(0));
    }

    @Test
    public void testCalculateInitialScore() {
        for (int i = 0; i < exam.getQuestionIds().size(); i++) {
            submission.saveAnswer(i,null);
            submission.calculateInitialScore();
        }
        submission.saveAnswer(0, "B");
        submission.saveAnswer(1, "AB");
        submission.saveAnswer(3, "Short Question");
        submission.calculateInitialScore();
        assertEquals(30, submission.getScore());
        assertEquals(10, submission.getMcScore());
        assertEquals(0, submission.getTfScore());
        assertEquals(20, submission.getSqScore());
    }

    @Test
    public void testGetQuestionNumberByQuestionId() {
        long questionId = singleQ.getId();
        int index = submission.getQuestionNumberByQuestionId(questionId);
        assertEquals(0, index);
    }

    @Test
    public void testUpdateScore() throws Exception {
        for (int i = 0; i < exam.getQuestionIds().size(); i++) submission.saveAnswer(i,null);
        submission.saveAnswer(0, "A");
        submission.saveAnswer(3, "short question");
        submission.calculateInitialScore();
        int originalScore = submission.getScore();
        int originalSqScore = submission.getSqScore();

        submission.updateScore(3, 17); // Update to a new score
        assertEquals(originalScore+17, submission.getScore());
        assertEquals(originalSqScore+17, submission.getSqScore());

        String outputMsg = "";
        try {
            submission.updateScore(3, 21); // Update to a new score
        } catch (Exception e) {
            outputMsg = e.getMessage();
        }
        assertEquals("Update score should be in between 0 and max score of this question.", outputMsg);
    }

    @Test
    public void testUpdateSqScore() throws Exception {
        for (int i = 0; i < exam.getQuestionIds().size(); i++) submission.saveAnswer(i,null);
        submission.saveAnswer(0, "A");
        submission.saveAnswer(3, "short question");
        submission.calculateInitialScore();
        int originalSqScore = submission.getSqScore();
        submission.updateSqScore(10); // Assuming valid range
        assertEquals(originalSqScore+10, submission.getSqScore());

        String outputMsg = "";
        try {
            submission.updateSqScore(30);
        } catch (Exception e) {
            outputMsg = e.getMessage();
        }
        assertEquals("Score should be non-negative and no larger than the total score of all short questions.", outputMsg);
    }
}