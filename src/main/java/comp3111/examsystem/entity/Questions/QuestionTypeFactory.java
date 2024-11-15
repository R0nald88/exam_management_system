package comp3111.examsystem.entity.Questions;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import static comp3111.examsystem.entity.Questions.QuestionDatabase.OPTION_LENGTH_LIMIT;

public sealed abstract class QuestionTypeFactory
        permits ShortQuestionType, SingleQuestionType, MultipleQuestionType, TrueFalseQuestionType {
    public abstract void validateAnswer(String answer) throws Exception;

    public void initialize(TextField[] options, Question question) throws Exception {
        question.setOptionA(options[0].getText());
        question.setOptionB(options[1].getText());
        question.setOptionC(options[2].getText());
        question.setOptionD(options[3].getText());
    }

    public void validateOption(String option, int index) throws Exception {
        char i = (char) (index + 'A');
        if (option.isEmpty()) {
            throw new Exception("Option " + i + " should not be empty.");
        }
        if (OPTION_LENGTH_LIMIT > 0 && option.length() > OPTION_LENGTH_LIMIT) {
            throw new Exception("Option " + i + " length should not exceed " + OPTION_LENGTH_LIMIT + ".");
        }
    }

    public void saveOptions(TextField[] fields, String[] options) {
        for (int i = 0; i < fields.length; i++) {
            options[i] = fields[i].getText();
        }
    }

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
