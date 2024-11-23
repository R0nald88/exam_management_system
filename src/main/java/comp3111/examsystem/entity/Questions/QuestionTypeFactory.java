package comp3111.examsystem.entity.Questions;

import comp3111.examsystem.tools.Database;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import static comp3111.examsystem.entity.Questions.QuestionDatabase.OPTION_LENGTH_LIMIT;

/**
 * Sealed abstract class containing validation and UI set-up related methods for different question type.
 * Should be inherited only by 4 different question types
 * @author Cheung Tuen King
 */
public sealed abstract class QuestionTypeFactory
        permits ShortQuestionType, SingleQuestionType, MultipleQuestionType, TrueFalseQuestionType {

    /**
     * Validate the answer text, varying on different question type.
     * Visit this method of specific type for details
     *
     * @param answer Answer for validation
     * @return Validated and finalized answer for storing
     * @throws Exception Any validation rule is not met for the specific type
     * @author Cheung Tuen King
     */
    public abstract String validateAnswer(String answer) throws Exception;

    /**
     * Fill in the question form option UI based on the question given.
     * Different question type could have different UI initialization.
     * This concrete method particularly for single and multiple type
     *
     * @param options TextField for question form option
     * @param question Question entity for initialization
     * @throws Exception Question option does not meet the validation rules. Visit QuestionTypeFactory.validateOption() for details
     * @author Cheung Tuen King
     */
    public void initialize(TextField[] options, Question question) throws Exception {
        question.setOptionA(options[0].getText());
        question.setOptionB(options[1].getText());
        question.setOptionC(options[2].getText());
        question.setOptionD(options[3].getText());
    }

    /**
     * Validate option, which have text length in range (0, QuestionDatabase.OPTION_LENGTH_LIMIT)
     *
     * @param option Option text
     * @param index Integer 0 to 3 indicating option A to D
     * @throws Exception Option text length out of range
     * @author Cheung Tuen King
     */
    public void validateOption(String option, int index) throws Exception {
        char i = (char) (index + 'A');
        Database.validateTextLength(OPTION_LENGTH_LIMIT, option, "option " + i);
    }

    /**
     * Save the option text user inputted in question form UI before user changing the question type.
     * The saved questions are used to restore the option text when the type change to single or multiple type.
     *
     * @param fields Array of TextField displaying option text
     * @param options Array for storing options
     * @author Cheung Tuen King
     */
    public void saveOptions(TextField[] fields, String[] options) {
        for (int i = 0; i < fields.length; i++) {
            options[i] = fields[i].getText();
        }
    }

    /**
     * Set up the question form UI by setting, disabling or enabling the labels and text field guiding and inputting options.
     * Current implementation is for multiple and single question type,
     * where all Labels showing "Option A" to "Option D" and option TextField enabled
     *
     * @param labels Array of Label guiding options input
     * @param fields Array of TextField for inputting options
     * @param options Array for storing options
     * @param originalLabel Original Label as reference for resetting the modified Labels
     * @param originalField Original TextField as reference for resetting the modified TextFields
     *
     * @author Cheung Tuen King
     */
    public void setUpForm(Label[] labels, TextField[] fields, String[] options, Label originalLabel, TextField originalField) {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setDisable(false);
            labels[i].setText("Option " + (char) ('A' + i));
        }

        for (int i = 0; i < fields.length; i++) {
            fields[i].setDisable(false);
            fields[i].setText(options[i]);
        }
    }

    /**
     * Access the specific subclasses of QuestionTypeFactory based on the question type provided
     *
     * @param type Question type
     * @return QuestionTypeFactory subclasses related to the type given
     * @author Cheung Tuen King
     */
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
