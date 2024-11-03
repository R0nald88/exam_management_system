package comp3111.examsystem.entity.Questions;

import static comp3111.examsystem.entity.Questions.QuestionForm.ANSWER_LENGTH_LIMIT;

public class ShortQuestionType extends QuestionTypeFactory {
    @Override
    public void validateAnswer(String answer) throws Exception {
        if (answer.isEmpty()) {
            throw new Exception("Answer should not be empty.");
        }
        if (ANSWER_LENGTH_LIMIT > 0 && answer.length() > ANSWER_LENGTH_LIMIT) {
            throw new Exception("Answer length should not exceeds " + ANSWER_LENGTH_LIMIT + ".");
        }
    }

    @Override
    public void validateOption(String option, int index) throws Exception {
        if (!option.isEmpty()) {
            throw new Exception("All options of Short Question should be empty.");
        }
    }

    @Override
    public void initialize(QuestionForm form) {
        try {
            form.setOptionA("");
            form.setOptionB("");
            form.setOptionC("");
            form.setOptionD("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUpForm() {

    }
}
