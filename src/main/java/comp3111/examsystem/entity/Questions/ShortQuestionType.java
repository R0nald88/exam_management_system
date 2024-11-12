package comp3111.examsystem.entity.Questions;

import static comp3111.examsystem.entity.Questions.Question.ANSWER_LENGTH_LIMIT;

public final class ShortQuestionType extends QuestionTypeFactory {
    private static ShortQuestionType instance = null;

    private ShortQuestionType() {}

    public static ShortQuestionType getInstance() {
        if (instance == null) instance = new ShortQuestionType();
        return instance;
    }
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
    public void initialize(Question form) {
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
