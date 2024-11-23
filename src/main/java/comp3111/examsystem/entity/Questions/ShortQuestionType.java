package comp3111.examsystem.entity.Questions;

import comp3111.examsystem.tools.Database;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import static comp3111.examsystem.entity.Questions.QuestionDatabase.ANSWER_LENGTH_LIMIT;

/**
 * Singleton containing short question type validation and UI set-up related methods.
 * As this class only contains utility methods, using singleton could reduce space due to class initialization.
 * @author Cheung Tuen King
 */
public final class ShortQuestionType extends QuestionTypeFactory {
    /**
     * Short question type singleton instance
     */
    private static ShortQuestionType instance = null;

    /**
     * Constructor for ShortQuestionType
     * @author Cheung Tuen King
     */
    private ShortQuestionType() {}

    /**
     * Access singleton instance of ShortQuestionType
     * @author Cheung Tuen King
     * @return ShortQuestionType singleton instance
     */
    public static ShortQuestionType getInstance() {
        if (instance == null) instance = new ShortQuestionType();
        return instance;
    }

    /**
     * Validate answer for short question.
     * Answer for short question should:
     * <li>have text length in range (0, QuestionDatabase.ANSWER_LENGTH_LIMIT)</li>
     *
     * @param answer Answer for validation
     * @return Validated answer
     * @throws Exception Any condition above is not met
     * @author Cheung Tuen King
     */
    @Override
    public String validateAnswer(String answer) throws Exception {
        Database.validateTextLength(ANSWER_LENGTH_LIMIT, answer, "answer");
        return answer;
    }

    /**
     * Validate the option text.
     * Short question should have all option empty
     *
     * @param option Option text
     * @param index  Integer 0 to 3 indicating option A to D
     * @throws Exception Any option text is not empty
     */
    @Override
    public void validateOption(String option, int index) throws Exception {
        if (!option.isEmpty()) {
            throw new Exception("All options of Short Question should be empty.");
        }
    }

    /**
     * Fill in the question form option UI based on the question given.
     * As for short question, all option should be empty
     *
     * @param fields TextField for question form option
     * @param form Question entity for initialization
     * @throws Exception Question option does not meet the validation rules. Visit QuestionTypeFactory.validateOption() for details
     * @author Cheung Tuen King
     */
    @Override
    public void initialize(TextField[] fields, Question form) throws Exception {
        form.setOptionA("");
        form.setOptionB("");
        form.setOptionC("");
        form.setOptionD("");
    }

    /**
     * Set up the question form UI by setting, disabling or enabling the labels and text field guiding and inputting options.
     * As for short question, all option TextField and Label should be disabled as all is predefined to be empty.
     *
     * @param labels Array of Label guiding options input
     * @param fields Array of TextField for inputting options
     * @param options Array for storing options
     * @param originalLabel Original Label as reference for resetting the modified Labels
     * @param originalField Original TextField as reference for resetting the modified TextFields
     *
     * @author Cheung Tuen King
     */
    @Override
    public void setUpForm(Label[] labels, TextField[] fields, String[] options, Label originalLabel, TextField originalField) {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setDisable(true);
            fields[i].setDisable(true);
            fields[i].setText("");
        }
    }

    /**
     * Save the option text user inputted in question form UI before user changing the question type.
     * As for short question, no option is saved as all of them are predefined to be empty.
     *
     * @param fields Array of TextField displaying option text
     * @param options Array for storing options
     * @author Cheung Tuen King
     */
    @Override
    public void saveOptions(TextField[] fields, String[] options) {

    }
}
