package comp3111.examsystem.entity.Questions;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class QuestionTest {
    private Question question;

    @Test
    public void testSingleQuestionInput() {
        String inputOptionA = "Answer 1";
        String inputOptionB = "Answer 2";
        String inputOptionC = "Answer 3";
        String inputOptionD = "Answer 4";
        String inputQuestion = "Question?";
        QuestionType inputQuestionType = QuestionType.SINGLE;
        int inputScore = 34;
        String inputAnswer = "D";
        String outputError = "";

        question = new Question();
        try {
            question.setOptionA(inputOptionA);
            question.setOptionB(inputOptionB);
            question.setOptionC(inputOptionC);
            question.setOptionD(inputOptionD);
            question.setQuestion(inputQuestion);
            question.setScore(inputScore);
            question.setType(inputQuestionType);
            question.setAnswer(inputAnswer);
        } catch (Exception e) {
            outputError = e.getMessage();
        }

        assertEquals(question.getOptionA(), inputOptionA);
        assertEquals(question.getOptionB(), inputOptionB);
        assertEquals(question.getOptionC(), inputOptionC);
        assertEquals(question.getOptionD(), inputOptionD);
        assertEquals(question.getQuestion(), inputQuestion);
        assertEquals(question.getType().toString(), inputQuestionType.toString());
        assertEquals(question.getAnswer(), inputAnswer);
        assertEquals(question.getScore(), inputScore);
        assertEquals(outputError, "");
    }
    
    @Test
    public void testInvalidSingleOption() {
        testSingleQuestionInput();
        boolean hasEmptyError = false, 
                hasExcessiveError = false, 
                hasEmptyErrorAfterTrimming = false,
                hasExcessiveErrorAfterTrimming = false,
                hasErrorAfterTrimming = false;
        String  originalOptionA = question.getOptionA(),
                originalOptionB = question.getOptionB(),
                originalOptionC = question.getOptionC(),
                originalOptionD = question.getOptionD();
        
        try {
            question.setOptionA("");
        } catch (Exception e) {
            hasEmptyError = true;
            System.out.println(e.getMessage());
        }

        try {
            question.setOptionB("Answer Answer Answer Answer Answer Answer Answer Answer Answer");
        } catch (Exception e) {
            hasExcessiveError = true;
            System.out.println(e.getMessage());
        }

        try {
            question.setOptionC("                   ");
        } catch (Exception e) {
            hasEmptyErrorAfterTrimming = true;
            System.out.println(e.getMessage());
        }

        try {
            question.setOptionD("         Answer Answer Answer Answer Answer Answer Answer Answer Answer          ");
        } catch (Exception e) {
            hasExcessiveErrorAfterTrimming = true;
            System.out.println(e.getMessage());
        }
        
        assertTrue(hasEmptyError);
        assertTrue(hasExcessiveError);
        assertTrue(hasEmptyErrorAfterTrimming);
        assertTrue(hasExcessiveErrorAfterTrimming);

        assertEquals(originalOptionA, question.getOptionA());
        assertEquals(originalOptionB, question.getOptionB());
        assertEquals(originalOptionC, question.getOptionC());
        assertEquals(originalOptionD, question.getOptionD());

        try {
            question.setOptionD("                         Answer                      ");
        } catch (Exception e) {
            hasErrorAfterTrimming = true;
            System.out.println(e.getMessage());
        }

        assertFalse(hasErrorAfterTrimming);
        assertEquals(question.getOptionD(), "Answer");
    }

    @Test
    public void testSavingQuestion() {
        try {
            QuestionDatabase.getInstance().deleteAll();
            testSingleQuestionInput();
            QuestionDatabase.getInstance().addQuestion(question);
            List<Question> q = QuestionDatabase.getInstance().getAll();
            System.out.println(q.getFirst().toString());
            assertEquals(q.getFirst(), question);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
