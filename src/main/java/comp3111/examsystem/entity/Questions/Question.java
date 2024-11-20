package comp3111.examsystem.entity.Questions;

import com.google.gson.Gson;
import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.tools.Database;

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
        Database.validateTextLength(QuestionDatabase.QUESTION_LENGTH_LIMIT, question, "question");
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) throws Exception {
        answer = answer.trim();
        this.answer = getTypeFactory().validateAnswer(answer);;
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
        Database.validateNumberRange(
                QuestionDatabase.SCORE_LOWER_LIMIT,
                QuestionDatabase.SCORE_UPPER_LIMIT,
                score,
                "score",
                ""
        );
        this.score = score;
    }

    public void setScore(String score) throws Exception {
        score = score.trim();
        Database.validateNumberRange(
                QuestionDatabase.SCORE_LOWER_LIMIT,
                QuestionDatabase.SCORE_UPPER_LIMIT,
                score,
                "score",
                ""
        );
        this.score = Integer.parseInt(score);
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
