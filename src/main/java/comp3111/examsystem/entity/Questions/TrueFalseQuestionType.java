package comp3111.examsystem.entity.Questions;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Singleton containing true/ false question type validation and UI set-up related methods.
 * As this class only contains utility methods, using singleton could reduce space due to class initialization.
 * @author Cheung Tuen King
 */
public final class TrueFalseQuestionType extends QuestionTypeFactory {
    /**
     * True/ False question type singleton instance
     */
    private static TrueFalseQuestionType instance = null;

    /**
     * Constructor for TrueFalseQuestionType
     * @author Cheung Tuen King
     */
    private TrueFalseQuestionType() {}

    /**
     * Access singleton instance of TrueFalseQuestionType
     * @author Cheung Tuen King
     * @return TrueFalseQuestionType singleton instance
     */
    public static TrueFalseQuestionType getInstance() {
        if (instance == null) instance = new TrueFalseQuestionType();
        return instance;
    }

    /**
     * Validate answer.
     * Answer for true/ false question should:
     * <li>be a letter of either "T" or "F"</li>
     *
     * @param answer Answer for validation
     * @return Validated answer
     * @throws Exception Any condition above is not met
     * @author Cheung Tuen King
     */
    @Override
    public String validateAnswer(String answer) throws Exception {
        if (answer.isEmpty()) {
            throw new Exception("Answer should not be empty.");
        }
        if (!answer.equals("T") && !answer.equals("F")) {
            throw new Exception("Answer should be either \"T\" or \"F\"");
        }
        return answer;
    }

    /**
     * Validate the option text.
     * True/ false question should have the first 2 option being "True" and "False.
     * Rest should all be empty.
     *
     * @param option Option text
     * @param index  Integer 0 to 3 indicating option A to D
     * @throws Exception Any option text does not meet the condition above
     */
    @Override
    public void validateOption(String option, int index) throws Exception {
        switch (index) {
            case 0 -> {
                if (!option.equals("True")) throw new Exception("Option A of True/ False Question should be \"True\"");
            }
            case 1 -> {
                if (!option.equals("False")) throw new Exception("Option B of True/ False Question should be \"False\"");
            }
            case 2, 3 -> {
                if (!option.isEmpty()) throw new Exception("Option C and D of True/ False Question should be empty.");
            }
        }
    }

    /**
     * Set up the question form UI by setting, disabling or enabling the labels and text field guiding and inputting options.
     * As for true/ false question, all option TextField and Label should be disabled as all is predefined as in validateOption()
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
        for (int i = 0; i < 4; i++) {
            labels[i].setDisable(true);
            fields[i].setDisable(true);
        }

        fields[0].setText("True");
        fields[1].setText("False");
        fields[2].setText("");
        fields[3].setText("");
    }

    /**
     * Fill in the question form option UI based on the question given.
     * As for true/ false question, all option is predefined as in validateOption()
     *
     * @param fields TextField for question form option
     * @param form Question entity for initialization
     * @throws Exception Question option does not meet the validation rules. Visit QuestionTypeFactory.validateOption() for details
     * @author Cheung Tuen King
     */
    @Override
    public void initialize(TextField[] fields, Question form) throws Exception {
        form.setOptionA("True");
        form.setOptionB("False");
        form.setOptionC("");
        form.setOptionD("");
    }

    /**
     * Save the option text user inputted in question form UI before user changing the question type.
     * As for true/ false question, no option is saved as all of them are predefined as in validateOption()
     *
     * @param fields Array of TextField displaying option text
     * @param options Array for storing options
     * @author Cheung Tuen King
     */
    @Override
    public void saveOptions(TextField[] fields, String[] options) {

    }
}
