package comp3111.examsystem.entity;

import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuestionTest {
    @Test
    public void testInputSingleOptions() {
        String inputOptionA = "Answer 1";
        String inputOptionB = "Answer 2";
        String inputOptionC = "Answer 3";
        String inputOptionD = "Answer 4";
        String output1 = "", output2 = "", output3 = "", output4 = "";

        Question question = new Question();
        try {
            question.setOptionA(inputOptionA);
            question.setOptionB(inputOptionB);
            question.setOptionC(inputOptionC);
            question.setOptionD(inputOptionD);
            output1 = question.getOptionA();
            output2 = question.getOptionB();
            output3 = question.getOptionC();
            output4 = question.getOptionD();
        } catch (Exception e) {
            output1 = e.getMessage();
        }

        assertEquals(output1, inputOptionA);
        assertEquals(output2, inputOptionB);
        assertEquals(output3, inputOptionC);
        assertEquals(output4, inputOptionD);
    }

    @Test
    public void testInputSingleAnswers() {
        String inputSingleAnswer = "A";
        String output1 = "";

        Question form = new Question();
        try {
            form.setAnswer(inputSingleAnswer);
            output1 = form.getAnswer();
        } catch (Exception e) {
            output1 = e.getMessage();
        }

        assertEquals(output1, inputSingleAnswer);
        assertEquals(output2, inputMultipleAnswer);
    }
}
