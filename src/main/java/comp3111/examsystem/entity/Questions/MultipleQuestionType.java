package comp3111.examsystem.entity.Questions;

public final class MultipleQuestionType extends QuestionTypeFactory {
    private static MultipleQuestionType instance = null;

    private MultipleQuestionType() {}

    public static MultipleQuestionType getInstance() {
        if (instance == null) instance = new MultipleQuestionType();
        return instance;
    }

    @Override
    public void validateAnswer(String answer) throws Exception {
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
    }
}
