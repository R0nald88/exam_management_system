package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class QuestionTest {
    private Question question;

    @Test
    public void testSingleQuestionInput() {
        String inputOptionA = "Single Answer 1";
        String inputOptionB = "Single Answer 2";
        String inputOptionC = "Single Answer 3";
        String inputOptionD = "Single Answer 4";
        String inputQuestion = "Single Question?";
        QuestionType inputQuestionType = QuestionType.SINGLE;
        int inputScore = 34;
        String inputAnswer = "D";
        String outputError = "";

        question = new Question();
        try {
            question.setType(inputQuestionType);
            question.setOptionA(inputOptionA);
            question.setOptionB(inputOptionB);
            question.setOptionC(inputOptionC);
            question.setOptionD(inputOptionD);
            question.setQuestion(inputQuestion);
            question.setScore(inputScore);
            question.setAnswer(inputAnswer);
        } catch (Exception e) {
            outputError = e.getMessage();
        }

        assertEquals(question.getOptionA(), inputOptionA);
        assertEquals(question.getOptionB(), inputOptionB);
        assertEquals(question.getOptionC(), inputOptionC);
        assertEquals(question.getOptionD(), inputOptionD);
        assertEquals(question.getQuestion(), inputQuestion);
        assertEquals(question.getType().getName(), inputQuestionType.getName());
        assertEquals(question.getAnswer(), inputAnswer);
        assertEquals(question.getScore(), inputScore);
        assertEquals(outputError, "");
    }
    
    @Test
    public void testInvalidSingleOptionInput() {
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
    public void testInvalidSingleAnswerInput() {
        String inputA = "AFS";
        String outputA = "The answer of Single Question should be 1 letter from A to D";
        testSingleQuestionInput();

        try {
            question.setAnswer(inputA);
        } catch (Exception e) {
            assertEquals(e.getMessage(), outputA);
        }
    }

    @Test
    public void testMultipleQuestionInput() {
        String inputOptionA = "Multiple Answer 1";
        String inputOptionB = "Multiple Answer 2";
        String inputOptionC = "Multiple Answer 3";
        String inputOptionD = "Multiple Answer 4";
        String inputQuestion = "Multiple Question?";
        QuestionType inputQuestionType = QuestionType.MULTIPLE;
        int inputScore = 150;
        String inputAnswer = "ABD";
        String outputError = "";

        question = new Question();
        try {
            question.setType(inputQuestionType);
            question.setOptionA(inputOptionA);
            question.setOptionB(inputOptionB);
            question.setOptionC(inputOptionC);
            question.setOptionD(inputOptionD);
            question.setQuestion(inputQuestion);
            question.setScore(inputScore);
            question.setAnswer(inputAnswer);
        } catch (Exception e) {
            outputError = e.getMessage();
        }

        assertEquals(question.getOptionA(), inputOptionA);
        assertEquals(question.getOptionB(), inputOptionB);
        assertEquals(question.getOptionC(), inputOptionC);
        assertEquals(question.getOptionD(), inputOptionD);
        assertEquals(question.getQuestion(), inputQuestion);
        assertEquals(question.getType().getName(), inputQuestionType.getName());
        assertEquals(question.getAnswer(), inputAnswer);
        assertEquals(question.getScore(), inputScore);
        assertEquals(outputError, "");
    }

    @Test
    public void testInvalidMultipleOptionInput() {
        testMultipleQuestionInput();
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
    public void testInvalidMultipleAnswerInput() {
        String inputA = "AFS", inputB = "BDB", inputC = "IVHSOIH";
        String outputA = "The answer of Multiple Question should be 2 to 4 letters from A to D",
                outputB = "The answer of Multiple Question should not be duplicated",
                outputC = "The answer of Multiple Question should be 2 to 4 letters from A to D";
        testMultipleQuestionInput();

        try {
            question.setAnswer(inputA);
        } catch (Exception e) {
            assertEquals(e.getMessage(), outputA);
        }

        try {
            question.setAnswer(inputB);
        } catch (Exception e) {
            assertEquals(e.getMessage(), outputB);
        }

        try {
            question.setAnswer(inputC);
        } catch (Exception e) {
            assertEquals(e.getMessage(), outputC);
        }
    }

    @Test
    public void testShortQuestionInput() {
        String inputOptionA = "";
        String inputOptionB = "";
        String inputOptionC = "";
        String inputOptionD = "";
        String inputQuestion = "Short Question?";
        QuestionType inputQuestionType = QuestionType.SHORT_Q;
        int inputScore = 150;
        String inputAnswer = "ABD";
        String outputError = "";

        question = new Question();
        try {
            question.setType(inputQuestionType);
            question.setOptionA(inputOptionA);
            question.setOptionB(inputOptionB);
            question.setOptionC(inputOptionC);
            question.setOptionD(inputOptionD);
            question.setQuestion(inputQuestion);
            question.setScore(inputScore);
            question.setAnswer(inputAnswer);
        } catch (Exception e) {
            outputError = e.getMessage();
        }

        assertEquals(question.getOptionA(), inputOptionA);
        assertEquals(question.getOptionB(), inputOptionB);
        assertEquals(question.getOptionC(), inputOptionC);
        assertEquals(question.getOptionD(), inputOptionD);
        assertEquals(question.getQuestion(), inputQuestion);
        assertEquals(question.getType().getName(), inputQuestionType.getName());
        assertEquals(question.getAnswer(), inputAnswer);
        assertEquals(question.getScore(), inputScore);
        assertEquals(outputError, "");
    }

    @Test
    public void testTrueFalseQuestionInput() {
        String inputOptionA = "True";
        String inputOptionB = "False";
        String inputOptionC = "";
        String inputOptionD = "";
        String inputQuestion = "TF Question?";
        QuestionType inputQuestionType = QuestionType.TRUE_FALSE;
        int inputScore = 150;
        String inputAnswer = "T";
        String outputError = "";

        question = new Question();
        try {
            question.setType(inputQuestionType);
            question.setOptionA(inputOptionA);
            question.setOptionB(inputOptionB);
            question.setOptionC(inputOptionC);
            question.setOptionD(inputOptionD);
            question.setQuestion(inputQuestion);
            question.setScore(inputScore);
            question.setAnswer(inputAnswer);
        } catch (Exception e) {
            outputError = e.getMessage();
            e.printStackTrace();
        }

        assertEquals(question.getOptionA(), inputOptionA);
        assertEquals(question.getOptionB(), inputOptionB);
        assertEquals(question.getOptionC(), inputOptionC);
        assertEquals(question.getOptionD(), inputOptionD);
        assertEquals(question.getQuestion(), inputQuestion);
        assertEquals(question.getType().getName(), inputQuestionType.getName());
        assertEquals(question.getAnswer(), inputAnswer);
        assertEquals(question.getScore(), inputScore);
        assertEquals(outputError, "");
    }

    @Test
    public void testInvalidTrueFalseOptionInput() {
        testTrueFalseQuestionInput();
        String inputA = "312", inputB = "4123", inputC = "4234", inputD = "erw";
        String outputA = "Option A of True/ False Question should be \"True\"",
                outputB = "Option B of True/ False Question should be \"False\"",
                outputC = "Option C and D of True/ False Question should be empty.",
                outputD = "Option C and D of True/ False Question should be empty.";

        try {
            question.setOptionA(inputA);
        } catch (Exception e) {
            assertEquals(e.getMessage(), outputA);
        }

        try {
            question.setOptionB(inputB);
        } catch (Exception e) {
            assertEquals(e.getMessage(), outputB);
        }

        try {
            question.setOptionC(inputC);
        } catch (Exception e) {
            assertEquals(e.getMessage(), outputC);
        }

        try {
            question.setOptionD(inputD);
        } catch (Exception e) {
            assertEquals(e.getMessage(), outputD);
        }
    }

    @Test
    public void testInvalidTrueFalseAnswerInput() {
        String inputA = "", inputB = "RR";
        String outputA = "Answer should not be empty.", outputB = "Answer should be either \"T\" or \"F\"";
        testTrueFalseQuestionInput();

        try {
            question.setAnswer(inputA);
        } catch (Exception e) {
            assertEquals(e.getMessage(), outputA);
        }

        try {
            question.setAnswer(inputB);
        } catch (Exception e) {
            assertEquals(e.getMessage(), outputB);
        }
    }

    @Test
    public void testInvalidQuestionInput() {
        try {
            testSingleQuestionInput();
            question.setQuestion("");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Please enter the question.");
        }

        try {
            testMultipleQuestionInput();
            question.setQuestion("");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Please enter the question.");
        }

        try {
            testShortQuestionInput();
            question.setQuestion("");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Please enter the question.");
        }

        try {
            testTrueFalseQuestionInput();
            question.setQuestion("");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Please enter the question.");
        }
    }

    @Test
    public void testInvalidScore() {
        String inputA = "GEFD";
        int inputC = -1;

        try {
            testSingleQuestionInput();
            question.setScore(inputA);
        } catch (Exception e) {
            assertEquals("The score should be an integer.", e.getMessage());
        }

        try {
            testSingleQuestionInput();
            question.setScore(inputC);
        } catch (Exception e) {
            assertEquals("Please enter a valid score larger than 0.", e.getMessage());
        }
    }

    @Test
    public void testQuestionTypeName() {
        assertEquals(QuestionType.toType("single"), QuestionType.SINGLE);
        assertEquals(QuestionType.toType("multiple"), QuestionType.MULTIPLE);
        assertEquals(QuestionType.toType("true false"), QuestionType.TRUE_FALSE);
        assertEquals(QuestionType.toType("short"), QuestionType.SHORT_Q);
    }

    @Test
    public void testSavingQuestion() {
        try {
            QuestionDatabase.getInstance().deleteAll();
            testSingleQuestionInput();
            QuestionDatabase.getInstance().addQuestion(question);
            List<Question> q = QuestionDatabase.getInstance().getAll();
            assertEquals(q.getFirst(), question);
            assertEquals(q.getFirst().toString(), question.toString());
            assertEquals(q.getFirst().hashCode(), question.hashCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
