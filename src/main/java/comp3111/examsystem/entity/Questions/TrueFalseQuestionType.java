package comp3111.examsystem.entity.Questions;

public class TrueFalseQuestionType extends QuestionTypeFactory {

    @Override
    public void validateAnswer(String answer) throws Exception {
        if (answer.isEmpty()) {
            throw new Exception("Answer should not be empty.");
        }
        if (!answer.equals("T") && !answer.equals("F")) {
            throw new Exception("Answer should be either \"T\" or \"F\"");
        }
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
    public void setUpForm() {

    }

    @Override
    public void initialize(QuestionForm form) {
        try {
            form.setOptionA("True");
            form.setOptionB("False");
            form.setOptionC("");
            form.setOptionD("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
