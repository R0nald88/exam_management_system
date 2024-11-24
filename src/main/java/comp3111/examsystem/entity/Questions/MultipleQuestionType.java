package comp3111.examsystem.entity.Questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Singleton containing multiple question type validation and UI set-up related methods.
 * As this class only contains utility methods, using singleton could reduce space due to class initialization.
 * @author Cheung Tuen King
 */
public final class MultipleQuestionType extends QuestionTypeFactory {
    /**
     * Multiple question type singleton instance
     */
    private static MultipleQuestionType instance = null;

    /**
     * Constructor for MultipleQuestionType
     * @author Cheung Tuen King
     */
    private MultipleQuestionType() {}

    /**
     * Access singleton instance of MultipleQuestionType
     * @author Cheung Tuen King
     * @return MultipleQuestionType singleton instance
     */
    public static MultipleQuestionType getInstance() {
        if (instance == null) instance = new MultipleQuestionType();
        return instance;
    }

    /**
     * Validate answer for multiple question.
     * Answer for multiple question should:
     * <li>contains 2 ot 4 letters only from A to D</li>
     * <li>contains no duplicated letters</li>
     * The validated answer would be finalized by ordering ascendingly
     *
     * @param answer Answer for validation
     * @return Validated answer with letters in ascending order
     * @throws Exception Any condition above is not met
     * @author Cheung Tuen King
     */
    @Override
    public String validateAnswer(String answer) throws Exception {
        if (answer.length() <= 1 || answer.length() > 4) {
            throw new Exception("The answer of Multiple Question should be 2 to 4 letters from A to D");
        }
        boolean isAvailable = false;
        String availableAnswer = "ABCD";
        for (int i = 0; i < answer.length(); i++) {
            String currentOption = answer.charAt(i) + "";
            if (availableAnswer.contains(currentOption)) {
                availableAnswer = availableAnswer.replace(currentOption, "");
            } else if ("ABCD".contains(currentOption)) {
                throw new Exception("The answer of Multiple Question should not be duplicated");
            } else {
                throw new Exception("The answer of Multiple Question should be 2 to 4 letters from A to D");
            }
        }

        char[] ans = answer.toCharArray();
        Arrays.sort(ans);
        String a = "";
        for (char c : ans) {
            a += c;
        }
        return a;
    }
}
