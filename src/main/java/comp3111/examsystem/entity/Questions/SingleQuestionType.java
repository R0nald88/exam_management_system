package comp3111.examsystem.entity.Questions;

/**
 * Singleton containing single question type validation and UI set-up related methods.
 * As this class only contains utility methods, using singleton could reduce space due to class initialization.
 * @author Cheung Tuen King
 */
public final class SingleQuestionType extends QuestionTypeFactory {
    /**
     * Single question type singleton instance
     */
    private static SingleQuestionType instance = null;

    /**
     * Constructor for SingleQuestionType
     * @author Cheung Tuen King
     */
    private SingleQuestionType() {}

    /**
     * Access singleton instance of SingleQuestionType
     * @author Cheung Tuen King
     * @return SingleQuestionType singleton instance
     */
    public static SingleQuestionType getInstance() {
        if (instance == null) instance = new SingleQuestionType();
        return instance;
    }

    /**
     * Validate answer for single question.
     * Answer for multiple question should:
     * <li>contains 1 letter only from A to D</li>
     *
     * @param answer Answer for validation
     * @return Validated answer
     * @throws Exception Any condition above is not met
     * @author Cheung Tuen King
     */
    @Override
    public String validateAnswer(String answer) throws Exception {
        if (answer.length() != 1 || !"ABCD".contains(answer)) {
            throw new Exception("The answer of Single Question should be 1 letter from A to D");
        }

        return answer;
    }
}
