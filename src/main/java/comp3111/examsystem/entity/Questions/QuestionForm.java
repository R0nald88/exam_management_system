package comp3111.examsystem.entity.Questions;

import comp3111.examsystem.entity.Entity;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class QuestionForm {
    public static final int OPTION_LENGTH_LIMIT = -1;
    public static final int ANSWER_LENGTH_LIMIT = -1;
    public static final int QUESTION_LENGTH_LIMIT = -1;
    public static final int TIME_LIMIT = -1;
    public static final int SCORE_LIMIT = -1;

    private String question;
    private String answer;
    private int score;
    private long id;
    private int time;
    private QuestionType type;
    private String optionA, optionB, optionC, optionD;
    private QuestionTypeFactory typeFactory;

    public QuestionForm() {
        setId(System.currentTimeMillis());
        setType(QuestionType.SINGLE);
    }

    public QuestionForm(Question q) {
        question = q.getQuestion();
        answer = q.getAnswer();
        score = q.getScore();
        id = q.getId();
        time = q.getTime();
        optionA = q.getOptionA();
        optionB = q.getOptionB();
        optionC = q.getOptionC();
        optionD = q.getOptionD();
        setType(q.getType());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) throws Exception {
        if (time <= 0) {
            throw new Exception("Time should not be less than 0s.");
        }
        if (TIME_LIMIT > 0 && time > TIME_LIMIT) {
            throw new Exception("Time should not exceed " + TIME_LIMIT + "s.");
        }
        this.time = time;
    }

    public void setTime(String time) throws Exception {
        int s = 0;
        try {
            s = Integer.parseInt(time);
        } catch (Exception e) {
            throw new Exception("Time should be an integer.");
        }
        setTime(s);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) throws Exception {
        if (question.isEmpty()) {
            throw new Exception("Question should not be empty.");
        }
        if (QUESTION_LENGTH_LIMIT > 0 && question.length() > QUESTION_LENGTH_LIMIT) {
            throw new Exception("Question length should not exceed " + QUESTION_LENGTH_LIMIT + ".");
        }
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) throws Exception {
        answer = answer.trim();
        final String[] availableAnswer = {"A", "B", "C", "D"};

        switch (type) {
            case SINGLE -> {
                boolean isAvailable = false;
                for (String a : availableAnswer) {
                    if (a.equals(answer)) {
                        isAvailable = true;
                        break;
                    }
                }
                if (answer.length() != 1 || !isAvailable) {
                    throw new Exception("The answer of single question should be 1 letter from A to D");
                }
            }
            case MULTIPLE -> {
                if (answer.length() <= 1) {
                    throw new Exception("The answer of multiple question should be 2 to 4 letters from A to D");
                }
                boolean isAvailable = false;
                for (String a : availableAnswer) {
                    if (a.equals(answer)) {
                        isAvailable = true;
                        break;
                    }
                }
            }
        }
        this.answer = answer;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
        switch (type) {
            case MULTIPLE -> typeFactory = new MultipleQuestionType();
            case SINGLE -> typeFactory = new SingleQuestionType();
            case TRUE_FALSE -> typeFactory = new TrueFalseQuestionType();
            case SHORT_Q -> typeFactory = new ShortQuestionType();
        }
        typeFactory.initialize(this);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) throws Exception {
        if (score <= 0) {
            throw new Exception("Score should not be less than 0.");
        }
        if (SCORE_LIMIT > 0 && score > SCORE_LIMIT) {
            throw new Exception("Score should not exceed " + SCORE_LIMIT + ".");
        }

        this.score = score;
    }

    public void setScore(String score) throws Exception {
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
        typeFactory.validateOption(optionA, 0);
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) throws Exception {
        typeFactory.validateOption(optionB, 1);
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) throws Exception {
        typeFactory.validateOption(optionC, 2);
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) throws Exception {
        typeFactory.validateOption(optionD, 3);
        this.optionD = optionD;
    }

    public Question toQuestion(QuestionForm form) {
        return new Question(
                form.getId(),
                form.getQuestion(),
                form.getAnswer(),
                form.getScore(),
                form.getTime(),
                form.getType(),
                form.getOptionA(),
                form.getOptionB(),
                form.getOptionC(),
                form.getOptionD()
        );
    }
}
