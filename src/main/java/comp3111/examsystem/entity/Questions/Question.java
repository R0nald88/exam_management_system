package comp3111.examsystem.entity.Questions;

import com.google.gson.Gson;
import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.tools.Database;

import java.util.Objects;

/**
 * Entity for storing question information
 * @author Cheung Tuen King
 */
public class Question extends Entity {
    /**
     * Question text
     */
    private String question;

    /**
     * Answer text, varying on the question type
     */
    private String answer;

    /**
     * Question score
     */
    private int score;

    /**
     * Question type
     */
    private QuestionType type;

    /**
     * Options given for the question, varying on the question type
     */
    private String optionA = "", optionB = "", optionC = "", optionD = "";

    /**
     * Constructor for question entity
     * @author Cheung Tuen King
     */
    public Question() {
        super(System.currentTimeMillis());
        setType(QuestionType.SINGLE);
    }

    /**
     * Access question text
     * @author Cheung Tuen King
     * @return Question text
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Set the question text.
     * Length of which should be in range (0, QuestionDatabase.QUESTION_LENGTH_LIMIT)
     * @author Cheung Tuen King
     * @param question Question text for setting
     * @throws Exception Question text length out of range
     */
    public void setQuestion(String question) throws Exception {
        question = question.trim();
        Database.validateTextLength(QuestionDatabase.QUESTION_LENGTH_LIMIT, question, "question");
        this.question = question;
    }

    /**
     * Access answer text
     * @author Cheung Tuen King
     * @return Answer text
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Set the answer text.
     * Length of which should be in range (0, QuestionDatabase.ANSWER_LENGTH_LIMIT).
     * Different question type has different validation rules for the answer.
     * Visit specific QuestionTypeFactory.validateAnswer() for details.
     *
     * @author Cheung Tuen King
     * @param answer Answer text for setting
     * @throws Exception Answer text length out of range, or validation failed for the question type
     */
    public void setAnswer(String answer) throws Exception {
        answer = answer.trim();
        this.answer = getTypeFactory().validateAnswer(answer);;
    }

    /**
     * Access the question type
     *
     * @author Cheung Tuen King
     * @return Question type
     */
    public QuestionType getType() {
        return type;
    }

    /**
     * Access the question type factory corresponding to the question type
     *
     * @author Cheung Tuen King
     * @return QuestionTypeFactory subclasses for specific Question type
     */
    public QuestionTypeFactory getTypeFactory() {
        return QuestionTypeFactory.getInstance(type);
    }

    /**
     * Set the question type
     *
     * @author Cheung Tuen King
     * @param type Question type for setting
     */
    public void setType(QuestionType type) {
        this.type = type;
    }

    /**
     * Access the question score
     *
     * @author Cheung Tuen King
     * @return Question score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the question score.
     * Question score should be in range (QuestionDatabase.SCORE_LOWER_LIMIT, QuestionDatabase.SCORE_UPPER_LIMIT)
     *
     * @author Cheung Tuen King
     * @param score Question score
     * @throws Exception Question score out of range
     */
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

    /**
     * Set the question score.
     * Question score should be in range (QuestionDatabase.SCORE_LOWER_LIMIT, QuestionDatabase.SCORE_UPPER_LIMIT)
     *
     * @author Cheung Tuen King
     * @param score Question score in string
     * @throws Exception Question score is not an integer, or out of range
     */
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

    /**
     * Access option A
     * @author Cheung Tuen King
     * @return Option A
     */
    public String getOptionA() {
        return optionA;
    }

    /**
     * Set option A.
     * Validation rule for options vary on the question type.
     * Visit specific QuestionTypeFactory.validateOption() for details
     *
     * @author Cheung Tuen King
     * @param optionA Option A for setting
     * @throws Exception Validation failed
     */
    public void setOptionA(String optionA) throws Exception {
        optionA = optionA.trim();
        getTypeFactory().validateOption(optionA, 0);
        this.optionA = optionA;
    }

    /**
     * Access option B
     * @author Cheung Tuen King
     * @return Option B
     */
    public String getOptionB() {
        return optionB;
    }

    /**
     * Set option B.
     * Validation rule for options vary on the question type.
     * Visit specific QuestionTypeFactory.validateOption() for details
     *
     * @author Cheung Tuen King
     * @param optionB Option B for setting
     * @throws Exception Validation failed
     */
    public void setOptionB(String optionB) throws Exception {
        optionB = optionB.trim();
        getTypeFactory().validateOption(optionB, 1);
        this.optionB = optionB;
    }

    /**
     * Access option C
     * @author Cheung Tuen King
     * @return Option C
     */
    public String getOptionC() {
        return optionC;
    }

    /**
     * Set option C.
     * Validation rule for options vary on the question type.
     * Visit specific QuestionTypeFactory.validateOption() for details
     *
     * @author Cheung Tuen King
     * @param optionC Option C for setting
     * @throws Exception Validation failed
     */
    public void setOptionC(String optionC) throws Exception {
        optionC = optionC.trim();
        getTypeFactory().validateOption(optionC, 2);
        this.optionC = optionC;
    }

    /**
     * Access option D
     * @author Cheung Tuen King
     * @return Option D
     */
    public String getOptionD() {
        return optionD;
    }

    /**
     * Set option D.
     * Validation rule for options vary on the question type.
     * Visit specific QuestionTypeFactory.validateOption() for details
     *
     * @author Cheung Tuen King
     * @param optionD Option D for setting
     * @throws Exception Validation failed
     */
    public void setOptionD(String optionD) throws Exception {
        optionD = optionD.trim();
        getTypeFactory().validateOption(optionD, 3);
        this.optionD = optionD;
    }

    /**
     * Access the json string representation of question entity
     * @author Cheung Tuen King
     * @return Json string format of question entity
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Check if the input object is the same as this question entity
     * @author Cheung Tuen King
     * @param o Object for checking
     * @return Boolean determining if 2 objects are equal
     */
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

    /**
     * Access the hash code of question entity
     * @author Cheung Tuen King
     * @return Hash code of question entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(question, answer, score, type, optionA, optionB, optionC, optionD);
    }
}
