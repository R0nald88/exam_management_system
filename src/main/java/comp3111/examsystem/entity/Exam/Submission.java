package comp3111.examsystem.entity.Exam;

import com.google.gson.Gson;
import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a submission of an exam by a student.
 * This class handles the answers provided by the student,
 * calculates scores, and manages question data.
 */
public class Submission extends Entity{
    private String studentUsername;
    private Long examId;
    private String examName;
    private String courseId;
    private List<String> answerList;
    private List<Integer> scoreList;
    private List <Question> questionObjectList;
    private int score = 0;
    private int fullScore;
    private int numberOfCorrect = 0;
    private int timeSpend;
    private int mcScore = 0;
    private int mcFullScore = 0;
    private int tfScore = 0;
    private int tfFullScore = 0;
    private int sqScore = 0;
    private int sqFullScore = 0;
    private List<Question> sqQuestionList = new ArrayList<>();
    private List<String> sqAnswerList = new ArrayList<>();
    private List<Boolean> sqGradedBooleanList = new ArrayList<>();
    private List<Integer> sqScoreList = new ArrayList<>();
    private boolean graded = false;

    /**
     * Constructs a new Submission with a timestamp.
     * @author Li Ching Ho
     */
    public Submission() {
        super(System.currentTimeMillis());
    }

    /**
     * Sets the username of the student making the submission.
     *
     * @param studentUsername The username of the student.
     * @author Li Ching Ho
     */
    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    /**
     * Sets the ID of the exam and initializes related fields.
     *
     * @param examId The ID of the exam.
     * @author Li Ching Ho & Wan Hanzhe
     */
    public void setExamId(Long examId) {
        this.examId = examId;
        Exam exam = ExamDatabase.getInstance().queryByKey(examId.toString());
        this.examName = exam.getName();
        this.courseId = exam.getCourseId();
        answerList = new ArrayList<>(exam.getQuestionIds().size());
        scoreList =  new ArrayList<>(exam.getQuestionIds().size());
        
        questionObjectList =  new ArrayList<>(exam.getQuestionIds().size());
        for (Long qId : exam.getQuestionIds()) {
            Question q = QuestionDatabase.getInstance().queryByKey(qId.toString());
            questionObjectList.add(q);
        }
        for (int i = 0; i < exam.getQuestionIds().size(); i++) {
            answerList.add(null);
            scoreList.add(0);
            }
    }

    /**
     * Sets the full score for the exam.
     *
     * @param fullScore The total score for the exam.
     * @author Wan Hanzhe
     */
    public void setFullScore(int fullScore) {
        this.fullScore = fullScore;
    }

    /**
     * Sets the time spent on the exam.
     *
     * @param timeSpend The time spent in seconds.
     * @author Li Ching Ho
     */
    public void setTimeSpend(int timeSpend) {
        this.timeSpend = timeSpend;
    }

    /**
     * Gets the username of the student.
     *
     * @return The student's username.
     * @author Li Ching Ho
     */
    public String getStudentUsername() {
        return studentUsername;
    }

    /**
     * Gets the ID of the exam.
     *
     * @return The exam ID.
     * @author Li Ching Ho
     */
    public Long getExamId() {
        return examId;
    }

    /**
     * Gets the name of the exam.
     *
     * @return The exam name.
     * @author Li Ching Ho
     */
    public String getExamName(){
        return examName;
    }

    /**
     * Gets the course ID associated with the exam.
     *
     * @return The course ID.
     * @author Li Ching Ho
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Gets the list of answers provided by the student.
     *
     * @return The list of answers.
     * @author Li Ching Ho
     */
    public List<String> getAnswerList() {
        return answerList;
    }

    /**
     * Gets the list of question objects associated with the exam.
     *
     * @return The list of questions.
     * @author Li Ching Ho
     */
    public List<Question> getQuestionObjectList() {
        return questionObjectList;
    }

    /**
     * Gets the total score achieved by the student.
     *
     * @return The total score.
     * @author Li Ching Ho
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the full score for the exam.
     *
     * @return The full score.
     * @author Li Ching Ho
     */
    public int getFullScore() {
        return fullScore;
    }

    /**
     * Gets the time spent on the exam.
     *
     * @return The time spent in seconds.
     * @author Li Ching Ho
     */
    public int getTimeSpend() {
        return timeSpend;
    }

    /**
     * Gets the number of correct answers provided by the student.
     *
     * @return The number of correct answers.
     * @author Li Ching Ho
     */
    public int getNumberOfCorrect() {
        return numberOfCorrect;
    }

    /**
     * Gets the score for multiple choice questions.
     *
     * @return The multiple choice score.
     * @author Wan Hanzhe
     */
    public int getMcScore(){
        return mcScore;
    }

    /**
     * Gets the full score for multiple choice questions.
     *
     * @return The multiple choice full score.
     * @author Wan Hanzhe
     */
    public int getMcFullScore(){
        return mcFullScore;
    }
    /**
     * Gets the score for true/false questions.
     *
     * @return The true/false score.
     * @author Wan Hanzhe
     */
    public int getTfScore(){
        return tfScore;
    }

    /**
     * Gets the full score for true/false questions.
     *
     * @return The true/false full score.
     * @author Wan Hanzhe
     */
    public int getTfFullScore(){
        return tfFullScore;
    }

    /**
     * Gets the score for short questions.
     *
     * @return The short questions score.
     * @author Wan Hanzhe
     */
    public int getSqScore(){
        return sqScore;
    }

    /**
     * Gets the full score for short questions.
     *
     * @return The short questions full score.
     * @author Wan Hanzhe
     */
    public int getSqFullScore(){
        return sqFullScore;
    }

    /**
     * Gets the list of short answer questions.
     *
     * @return The list of short answer questions.
     * @author Wan Hanzhe
     */
    public List<Question> getSqQuestionList(){
        return sqQuestionList;
    }

    /**
     * Gets the list of student's answers to short answer questions.
     *
     * @return The list of student's answers to short answer questions.
     * @author Wan Hanzhe
     */
    public List<String> getSqAnswerList() {
        return sqAnswerList;
    }

    /**
     * Get the list of Boolean to check if the short questions in this submission has been graded.
     *
     * @return The list of Boolean for checking if the short questions
     *         in sqQuestionList is graded.
     * @author Li Ching Ho
     */
    public List<Boolean> getSqGradedBooleanList() {
        return sqGradedBooleanList;
    }

    /**
     * Gets the list of student's scores to short answer questions.
     *
     * @return The list of student's scores to short answer questions.
     * @author Wan Hanzhe
     */
    public List<Integer> getSqScoreList() {
        return sqScoreList;
    }

    /**
     * Gets the list of scores for short answer questions.
     *
     * @return The list of scores for short answer questions.
     * @author Wan HanZhe
     */
    public List<Integer> getScoreList(){
        return scoreList;
    }

    /**
     * Checks if the submission has been graded.
     *
     * @return True if graded, false otherwise.
     * @author Wan Hanzhe
     */
    public boolean isGraded(){
        return graded;
    }

    /**
     * Saves the answer for a specific question.
     *
     * @param questionNumber The index of the question.
     * @param answer The answer provided by the student.
     * @author Li Ching Ho
     */
    public void saveAnswer(int questionNumber, String answer) {
        if (questionNumber >= 0 && questionNumber < questionObjectList.size() && answer != null) {
            answerList.set(questionNumber, answer);
        }
    }

    /**
     * Converts the submission object to a JSON string.
     *
     * @return The JSON representation of the submission.
     * @author Li Ching Ho
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Calculates the initial score based on the answers provided straight after submission.
     *
     * @author Li Ching Ho
     */
    public void calculateInitialScore() {
        if (answerList != null) {
            for (int i = 0; i < questionObjectList.size(); i++) {
                if (answerList.get(i) != null && answerList.get(i).equals(questionObjectList.get(i).getAnswer())) {
                    scoreList.set(i, questionObjectList.get(i).getScore());
                    score += questionObjectList.get(i).getScore();
                    if (questionObjectList.get(i).getType() == QuestionType.SINGLE || questionObjectList.get(i).getType() == QuestionType.MULTIPLE) {
                        mcScore += questionObjectList.get(i).getScore();
                        mcFullScore += questionObjectList.get(i).getScore();
                    }
                    else if (questionObjectList.get(i).getType() == QuestionType.TRUE_FALSE) {
                        tfScore += questionObjectList.get(i).getScore();
                        tfFullScore += questionObjectList.get(i).getScore();
                    }
                    else if (questionObjectList.get(i).getType() == QuestionType.SHORT_Q) {
                        sqScore += questionObjectList.get(i).getScore();
                        sqFullScore += questionObjectList.get(i).getScore();

                        sqQuestionList.add(questionObjectList.get(i));
                        sqAnswerList.add(answerList.get(i));
                        sqGradedBooleanList.add(false);
                        sqScoreList.add(questionObjectList.get(i).getScore());
                    }
                    numberOfCorrect++;
                } else {
                    if (questionObjectList.get(i).getType() == QuestionType.SINGLE || questionObjectList.get(i).getType() == QuestionType.MULTIPLE) {
                        mcFullScore += questionObjectList.get(i).getScore();
                    }
                    else if (questionObjectList.get(i).getType() == QuestionType.TRUE_FALSE) {
                        tfFullScore += questionObjectList.get(i).getScore();
                    }
                    else if (questionObjectList.get(i).getType() == QuestionType.SHORT_Q) {
                        sqFullScore += questionObjectList.get(i).getScore();

                        sqQuestionList.add(questionObjectList.get(i));
                        sqAnswerList.add(answerList.get(i));
                        sqGradedBooleanList.add(false);
                        sqScoreList.add(0);
                    }
                }
            }
        }
    }

    /**
     * Gets the index of a question based on its ID from question list of the exam.
     *
     * @param id The ID of the question.
     * @return The index of the question, or -1 if not found.
     * @author Li Ching Ho
     */
    public int getQuestionNumberByQuestionId(long id) {
        for (int i = 0; i < questionObjectList.size(); i++) {
            if (questionObjectList.get(i).getId() == id) return i;
        }
        return -1;
    }

    /**
     * Updates the score for a specific question.
     *
     * @param questionNumber The index of the question.
     * @param score The new score to set.
     * @throws Exception If the score is out of range.
     * @author Li Ching Ho
     */
    public void updateScore(int questionNumber, int score) throws Exception{
        int originalScore = scoreList.get(questionNumber);
        Question question = QuestionDatabase.getInstance().queryByKey(ExamDatabase.getInstance().queryByKey(examId.toString()).getQuestionIds().get(questionNumber).toString());
        int maxScore = question.getScore();
        if (score < 0 || score > maxScore) throw new Exception("Update score should be in between 0 and max score of this question.");
        scoreList.set(questionNumber, score);
        if (questionObjectList.get(questionNumber).getType() == QuestionType.SINGLE || questionObjectList.get(questionNumber).getType() == QuestionType.MULTIPLE) {
            mcScore = mcScore - originalScore + score;
        }
        else if (questionObjectList.get(questionNumber).getType() == QuestionType.TRUE_FALSE) {
            tfScore = tfScore - originalScore + score;
        }
        else if (questionObjectList.get(questionNumber).getType() == QuestionType.SHORT_Q) {
            sqScore = sqScore - originalScore + score;
            for (int i = 0; i < sqQuestionList.size(); i++) {
                if (Objects.equals(sqQuestionList.get(i).getId(), questionObjectList.get(questionNumber).getId())) {
                    sqScoreList.set(i, score);
                    sqScoreList.set(i, score);
                    sqGradedBooleanList.set(i, true);
                    break;
                }
            }
        }
        this.score = this.score-originalScore+score;
        graded = true;
        for (Boolean b : sqGradedBooleanList) {
            if (b == false) graded = false;
        }
    }

    /**
     * Updates the score for short answer questions.
     *
     * @param newSqScore The new score for short answer questions.
     * @throws Exception If the new score is out of the valid range.
     * @author Wan Hanzhe
     */
    public void updateSqScore(int newSqScore) throws Exception{
        if (newSqScore < 0 || newSqScore > sqFullScore)
            throw new Exception("Score should be non-negative and no larger than the total score of all short questions.");
        score += newSqScore - sqScore;
        sqScore = newSqScore;
        graded = true;
    }

}
