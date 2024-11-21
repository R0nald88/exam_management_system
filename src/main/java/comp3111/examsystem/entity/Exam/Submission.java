package comp3111.examsystem.entity.Exam;

import com.google.gson.Gson;
import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;

import java.util.ArrayList;
import java.util.List;
import comp3111.examsystem.entity.Exam.Exam;

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
    private List<String> sqQuestionList = new ArrayList<>();
    private List<String> sqAnswerList = new ArrayList<>();
    private List<Integer> sqFullScoreList = new ArrayList<>();
    private boolean graded = false;

    public Submission() {
        super(System.currentTimeMillis());
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

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
        System.out.println(exam.getQuestionIds().size());
        for (int i = 0; i < exam.getQuestionIds().size(); i++) {
            answerList.add(null);
            scoreList.add(0);
            }
    }

    public void setFullScore(int fullScore) {
        this.fullScore = fullScore;
    }

    public void setTimeSpend(int timeSpend) {
        this.timeSpend = timeSpend;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public Long getExamId() {
        return examId;
    }

    public String getExamName(){
        return examName;
    }

    public String getCourseId() {
        return courseId;
    }

    public List<String> getAnswer() {
        return answerList;
    }

    public List<Question> getQuestionObjectList() {
        return questionObjectList;
    }

    public int getScore() {
        return score;
    }

    public int getFullScore() {
        return fullScore;
    }

    public int getTimeSpend() {
        return timeSpend;
    }

    public int getNumberOfCorrect() {
        return numberOfCorrect;
    }

    public int getMcScore(){
        return mcScore;
    }

    public int getMcFullScore(){
        return mcFullScore;
    }

    public int getTfScore(){
        return tfScore;
    }

    public int getTfFullScore(){
        return tfFullScore;
    }

    public int getSqScore(){
        return sqScore;
    }

    public int getSqFullScore(){
        return sqFullScore;
    }

    public List<String> getSqQuestionList(){
        return sqQuestionList;
    }

    public List<String> getSqAnswerList(){
        return sqAnswerList;
    }

    public List<Integer> getSqFullScoreList(){
        return sqFullScoreList;
    }

    public boolean isGraded(){
        return graded;
    }

    public void saveAnswer(int questionNumber, String answer) {
        if (questionNumber >= 0 && questionNumber < questionObjectList.size() && answer != null) {
            answerList.set(questionNumber, answer);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public void calculateInitialScore() {
        if (answerList != null) {
            for (int i = 0; i < questionObjectList.size(); i++) {
                if (answerList.get(i) != null && answerList.get(i).equals(questionObjectList.get(i).getAnswer())) {
                    System.out.println(i);
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

                        sqQuestionList.add(questionObjectList.get(i).getQuestion());
                        sqAnswerList.add(answerList.get(i));
                        sqFullScoreList.add(questionObjectList.get(i).getScore());
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

                        sqQuestionList.add(questionObjectList.get(i).getQuestion());
                        sqAnswerList.add(answerList.get(i));
                        sqFullScoreList.add(questionObjectList.get(i).getScore());
                    }
                }
                System.out.println("By now answerList: " + answerList);
            }
        }
    }

    public void updateScore(int questionNumber, int score) throws Exception{
        int originalScore = scoreList.get(questionNumber);
        scoreList.set(questionNumber, score);
        Question question = QuestionDatabase.getInstance().queryByKey(ExamDatabase.getInstance().queryByKey(examId.toString()).getQuestionIds().get(questionNumber).toString());
        int maxScore = question.getScore();
        if (score < 0 || score > maxScore) throw new Exception("Update score should be in between 0 and max score of this question.");
        if (questionObjectList.get(questionNumber).getType() == QuestionType.SINGLE || questionObjectList.get(questionNumber).getType() == QuestionType.MULTIPLE) {
            mcScore = mcScore - originalScore + score;
        }
        else if (questionObjectList.get(questionNumber).getType() == QuestionType.TRUE_FALSE) {
            tfScore = tfScore - originalScore + score;
        }
        else if (questionObjectList.get(questionNumber).getType() == QuestionType.SHORT_Q) {
            sqScore = sqScore - originalScore + score;
        }
        this.score = this.score-originalScore+score;
    }

    public void updateSqScore(int newSqScore) throws Exception{
        if (newSqScore < 0 || newSqScore > sqFullScore)
            throw new Exception("Score should be non-negative and no larger than the maximum score.");
        score += newSqScore - sqScore;
        sqScore = newSqScore;
        graded = true;
    }

}
