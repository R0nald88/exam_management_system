package comp3111.examsystem.entity.Questions;

import static comp3111.examsystem.entity.Questions.Question.OPTION_LENGTH_LIMIT;

public sealed abstract class QuestionTypeFactory
        permits ShortQuestionType, SingleQuestionType, MultipleQuestionType, TrueFalseQuestionType {
    public abstract void validateAnswer(String answer) throws Exception;
    public abstract void setUpForm();
    public abstract void initialize(Question form);

    public void validateOption(String option, int index) throws Exception {
        char i = (char) (index + 'A');
        if (option.isEmpty()) {
            throw new Exception("Option " + i + " should not be empty.");
        }
        if (OPTION_LENGTH_LIMIT > 0 && option.length() > OPTION_LENGTH_LIMIT) {
            throw new Exception("Option " + i + " length should not exceed " + OPTION_LENGTH_LIMIT + ".");
        }
    }

    public static QuestionTypeFactory getInstance(QuestionType type) {
        switch (type) {
            case SINGLE -> {
                return SingleQuestionType.getInstance();
            }
            case MULTIPLE -> {
                return MultipleQuestionType.getInstance();
            }
            case SHORT_Q -> {
                return ShortQuestionType.getInstance();
            }
            default -> {
                return TrueFalseQuestionType.getInstance();
            }
        }
    }
}
