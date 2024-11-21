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
    private String courseId;
    private List<String> answerList;
    private List<Integer> scoreList;
    private List <Question> questionObjectList;
    private int score = 0;
    private int fullScore;
    private int numberOfCorrect = 0;
    private int singleMCScore = 0;
    private int multipleMCScore = 0;
    private int tfScore = 0;
    private int sqScore = 0;
    private List<String> sqQuestionList = new ArrayList<>();;
    private List<String> sqAnswerList = new ArrayList<>();;
    private int timeSpend;

    public Submission() {
        super(System.currentTimeMillis());
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
        Exam exam = ExamDatabase.getInstance().queryByKey(examId.toString());
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

    public void setTimeSpend(int timeSpend) {
        this.timeSpend = timeSpend;
    }

    public void updateScore(int questionNumber, int score) throws Exception{
        int originalScore = scoreList.get(questionNumber);
        scoreList.set(questionNumber, score);
        Question question = QuestionDatabase.getInstance().queryByKey(ExamDatabase.getInstance().queryByKey(examId.toString()).getQuestionIds().get(questionNumber).toString());
        int maxScore = question.getScore();
        if (score < 0 || score > maxScore) throw new Exception("Update score should be in between 0 and max score of this question.");
        if (questionObjectList.get(questionNumber).getType() == QuestionType.SINGLE) {
            singleMCScore = singleMCScore - originalScore + score;
        }
        else if (questionObjectList.get(questionNumber).getType() == QuestionType.MULTIPLE) {
            multipleMCScore = multipleMCScore - originalScore + score;
        }
        else if (questionObjectList.get(questionNumber).getType() == QuestionType.TRUE_FALSE) {
            tfScore = tfScore - originalScore + score;
        }
        else if (questionObjectList.get(questionNumber).getType() == QuestionType.SHORT_Q) {
            sqScore = sqScore - originalScore + score;
        }
        this.score = this.score-originalScore+score;
    }

    public void setFullScore(int fullScore) {
        this.fullScore = fullScore;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public Long getExamId() {
        return examId;
    }

    public List<String> getAnswer() {
        return answerList;
    }

    public int getScore() {
        return score;
    }

    public int getFullScore() {
        return fullScore;
    }

    public String getCourseId() {
        return courseId;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public List<Question> getQuestionObjectList() {
        return questionObjectList;
    }

    public void saveAnswer(int questionNumber, String answer) {
        if (questionNumber >= 0 && questionNumber < questionObjectList.size() && answer != null) {
            answerList.set(questionNumber, answer);
        }
    }

    public List<String> getSqQuestionList(){
        return sqQuestionList;
    }

    public List<String> getSqAnswerList(){
        return sqAnswerList;
    }

    public int getTimeSpend() {
        return timeSpend;
    }

    public int getNumberOfCorrect() {
        return numberOfCorrect;
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
                    if (questionObjectList.get(i).getType() == QuestionType.SINGLE) {
                        singleMCScore += questionObjectList.get(i).getScore();
                    }
                    else if (questionObjectList.get(i).getType() == QuestionType.MULTIPLE) {
                        multipleMCScore += questionObjectList.get(i).getScore();
                    }
                    else if (questionObjectList.get(i).getType() == QuestionType.TRUE_FALSE) {
                        tfScore += questionObjectList.get(i).getScore();
                    }
                    else if (questionObjectList.get(i).getType() == QuestionType.SHORT_Q) {
                        sqScore += questionObjectList.get(i).getScore();
                        if (sqQuestionList == null) sqQuestionList = new ArrayList<>();
                        sqQuestionList.add(questionObjectList.get(i).getQuestion());
                        if (sqAnswerList == null) sqAnswerList = new ArrayList<>();
                        sqAnswerList.add(answerList.get(i));
                    }
                    numberOfCorrect++;
                } else {
                    if (questionObjectList.get(i).getType() == QuestionType.SHORT_Q) {
                        if (sqQuestionList == null) sqQuestionList = new ArrayList<>();
                        sqQuestionList.add(questionObjectList.get(i).getQuestion());
                        if (sqAnswerList == null) sqAnswerList = new ArrayList<>();
                        sqAnswerList.add(answerList.get(i));
                    }
                }
                System.out.println("By now answerList: " + answerList);
            }
        }
    }

}
