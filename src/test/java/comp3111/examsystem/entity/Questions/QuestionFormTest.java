package comp3111.examsystem.entity.Questions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuestionFormTest {
    @Test
    public void testInputOptions() {
        String inputOptionA = "Answer 1";
        String inputOptionB = "Answer 2";
        String inputOptionC = "Answer 3";
        String inputOptionD = "Answer 4";
        String output1 = "", output2 = "", output3 = "", output4 = "";

        Question form = new Question();
        try {
            form.setOptionA(inputOptionA);
            form.setOptionB(inputOptionB);
            form.setOptionC(inputOptionC);
            form.setOptionD(inputOptionD);
            output1 = form.getOptionA();
            output2 = form.getOptionB();
            output3 = form.getOptionC();
            output4 = form.getOptionD();
        } catch (Exception e) {
            output1 = e.getMessage();
        }

        assertEquals(output1, inputOptionA);
        assertEquals(output2, inputOptionB);
        assertEquals(output3, inputOptionC);
        assertEquals(output4, inputOptionD);
    }

    @Test
    public void testAnswers() {
        String inputSingleAnswer = "A";
        String inputMultipleAnswer = "AB";
        String output1 = "", output2 = "";

        Question form = new Question();
        try {
            form.setAnswer(inputSingleAnswer);
            output1 = form.getAnswer();

            form.setType(QuestionType.MULTIPLE);
            form.setAnswer(inputMultipleAnswer);
            output2 = form.getAnswer();
        } catch (Exception e) {
            output1 = e.getMessage();
        }

        assertEquals(output1, inputSingleAnswer);
        assertEquals(output2, inputMultipleAnswer);
    }
}
