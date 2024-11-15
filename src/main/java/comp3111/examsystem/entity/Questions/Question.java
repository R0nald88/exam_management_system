package comp3111.examsystem.entity.Questions;

import com.google.gson.Gson;
import comp3111.examsystem.entity.Entity;

import java.util.Objects;

public class Question extends Entity {
    private String question;
    private String answer;
    private int score;
    private QuestionType type;
    private String optionA = "", optionB = "", optionC = "", optionD = "";

    public Question() {
        super(System.currentTimeMillis());
        setType(QuestionType.SINGLE);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) throws Exception {
        question = question.trim();
        if (question.isEmpty()) {
            throw new Exception("Question should not be empty.");
        }
        if (QuestionDatabase.QUESTION_LENGTH_LIMIT > 0 && question.length() > QuestionDatabase.QUESTION_LENGTH_LIMIT) {
            throw new Exception("Question length should not exceed " + QuestionDatabase.QUESTION_LENGTH_LIMIT + ".");
        }
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) throws Exception {
        answer = answer.trim();
        getTypeFactory().validateAnswer(answer);
        this.answer = answer;
    }

    public QuestionType getType() {
        return type;
    }

    public QuestionTypeFactory getTypeFactory() {
        return QuestionTypeFactory.getInstance(type);
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) throws Exception {
        String error = "Invalid exam time.";
        if (QuestionDatabase.SCORE_UPPER_LIMIT > QuestionDatabase.SCORE_LOWER_LIMIT && QuestionDatabase.SCORE_LOWER_LIMIT >= 0) {
            error = "Please input a valid score between " + QuestionDatabase.SCORE_LOWER_LIMIT +
                    " and " + QuestionDatabase.SCORE_UPPER_LIMIT + " second(s).";
        } else if (QuestionDatabase.SCORE_UPPER_LIMIT > 0) {
            error = "Please input a valid score less than " + QuestionDatabase.SCORE_UPPER_LIMIT + " second(s).";
        } else if (QuestionDatabase.SCORE_LOWER_LIMIT >= 0) {
            error = "Please input a valid score larger than " + QuestionDatabase.SCORE_LOWER_LIMIT + " second(s).";
        }

        if ((QuestionDatabase.SCORE_LOWER_LIMIT >= 0 && score < QuestionDatabase.SCORE_LOWER_LIMIT) ||
                (QuestionDatabase.SCORE_UPPER_LIMIT > 0 && score > QuestionDatabase.SCORE_UPPER_LIMIT)) {
            throw new Exception(error);
        }
        this.score = score;
    }

    public void setScore(String score) throws Exception {
        score = score.trim();

        if (score.isEmpty()) {
            throw new Exception("Please enter the question score.");
        }

        int s = 0;
        try {
            s = Integer.parseInt(score);
        } catch (Exception e) {
            throw new Exception("Score should be an integer.");
        }
        setScore(s);
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) throws Exception {
        optionA = optionA.trim();
        getTypeFactory().validateOption(optionA, 0);
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) throws Exception {
        optionB = optionB.trim();
        getTypeFactory().validateOption(optionB, 1);
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) throws Exception {
        optionC = optionC.trim();
        getTypeFactory().validateOption(optionC, 2);
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) throws Exception {
        optionD = optionD.trim();
        getTypeFactory().validateOption(optionD, 3);
        this.optionD = optionD;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return score == question1.score &&
                Objects.equals(question, question1.question) &&
                Objects.equals(answer, question1.answer) &&
                type == question1.type &&
                Objects.equals(optionA, question1.optionA) &&
                Objects.equals(optionB, question1.optionB) &&
                Objects.equals(optionC, question1.optionC) &&
                Objects.equals(optionD, question1.optionD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, answer, score, type, optionA, optionB, optionC, optionD);
    }
}
