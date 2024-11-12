package comp3111.examsystem.entity.Questions;

public final class SingleQuestionType extends QuestionTypeFactory {
    private static SingleQuestionType instance = null;

    private SingleQuestionType() {}

    public static SingleQuestionType getInstance() {
        if (instance == null) instance = new SingleQuestionType();
        return instance;
    }

    @Override
    public void validateAnswer(String answer) throws Exception {
        if (answer.length() != 1 || !"ABCD".contains(answer)) {
            throw new Exception("The answer of Single Question should be 1 letter from A to D");
        }
    }

    @Override
    public void setUpForm() {

    }

    @Override
    public void initialize(Question form) {

    }
}
