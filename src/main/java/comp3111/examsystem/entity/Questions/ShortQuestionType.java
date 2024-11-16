package comp3111.examsystem.entity.Questions;

import comp3111.examsystem.tools.Database;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import static comp3111.examsystem.entity.Questions.QuestionDatabase.ANSWER_LENGTH_LIMIT;

public final class ShortQuestionType extends QuestionTypeFactory {
    private static ShortQuestionType instance = null;

    private ShortQuestionType() {}

    public static ShortQuestionType getInstance() {
        if (instance == null) instance = new ShortQuestionType();
        return instance;
    }
    @Override
    public void validateAnswer(String answer) throws Exception {
        Database.validateTextLength(ANSWER_LENGTH_LIMIT, answer, "answer");
    }

    @Override
    public void validateOption(String option, int index) throws Exception {
        if (!option.isEmpty()) {
            throw new Exception("All options of Short Question should be empty.");
        }
    }

    @Override
    public void initialize(TextField[] fields, Question form) throws Exception {
        form.setOptionA("");
        form.setOptionB("");
        form.setOptionC("");
        form.setOptionD("");
    }

    @Override
    public void setUpForm(Label[] labels, TextField[] fields, String[] options, Label originalLabel, TextField originalField) {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setDisable(true);
            fields[i].setDisable(true);
            fields[i].setText("");
        }
    }

    @Override
    public void saveOptions(TextField[] fields, String[] options) {

    }
}
