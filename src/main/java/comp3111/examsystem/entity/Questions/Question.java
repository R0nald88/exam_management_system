package comp3111.examsystem.entity.Questions;

import comp3111.examsystem.entity.Entity;

public class Question extends Entity {
    private final String question;
    private final String answer;
    private final int score;
    private final int time;
    private final QuestionType type;
    private final String optionA, optionB, optionC, optionD;

    public Question(Long id, String question, String answer, int score, int time, QuestionType type, String optionA, String optionB, String optionC, String optionD) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.score = score;
        this.time = time;
        this.optionA = optionA;
        this.type = type;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }

    public QuestionType getType() {
        return type;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }
}
