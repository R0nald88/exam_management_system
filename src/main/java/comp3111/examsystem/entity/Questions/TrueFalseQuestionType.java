package comp3111.examsystem.entity.Questions;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public final class TrueFalseQuestionType extends QuestionTypeFactory {
    private static TrueFalseQuestionType instance = null;

    private TrueFalseQuestionType() {}

    public static TrueFalseQuestionType getInstance() {
        if (instance == null) instance = new TrueFalseQuestionType();
        return instance;
    }

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

    @Override
    public void initialize(TextField[] fields, Question form) throws Exception {
        form.setOptionA("True");
        form.setOptionB("False");
        form.setOptionC("");
        form.setOptionD("");
    }

    @Override
    public void saveOptions(TextField[] fields, String[] options) {

    }
}
