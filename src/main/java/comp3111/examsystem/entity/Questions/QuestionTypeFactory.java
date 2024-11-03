package comp3111.examsystem.entity.Questions;

import static comp3111.examsystem.entity.Questions.QuestionForm.OPTION_LENGTH_LIMIT;

public abstract class QuestionTypeFactory {
    public abstract void validateAnswer(String answer) throws Exception;
    public abstract void setUpForm();
    public abstract void initialize(QuestionForm form);

    public void validateOption(String option, int index) throws Exception {
        char i = (char) (index + 'A');
        if (option.isEmpty()) {
            throw new Exception("Option " + i + " should not be empty.");
        }
        if (OPTION_LENGTH_LIMIT > 0 && option.length() > OPTION_LENGTH_LIMIT) {
            throw new Exception("Option " + i + " length should not exceed " + OPTION_LENGTH_LIMIT + ".");
        }
    }
}
