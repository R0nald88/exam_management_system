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
        if (question.getType() == QuestionType.SINGLE) {
            singleMCScore = singleMCScore - originalScore + score;
        }
        else if (question.getType() == QuestionType.MULTIPLE) {
            multipleMCScore = multipleMCScore - originalScore + score;
        }
        else if (question.getType() == QuestionType.TRUE_FALSE) {
            tfScore = tfScore - originalScore + score;
        }
        else if (question.getType() == QuestionType.SHORT_Q) {
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

    public void saveAnswer(int questionNumber, String answer) {
        Exam exam = ExamDatabase.getInstance().queryByKey(examId.toString());
        if (exam != null) {
            if (answerList == null) {

                answerList = new ArrayList<>(exam.getQuestionIds().size());
                // Initialize the list with nulls or default values
                for (int i = 0; i < exam.getQuestionIds().size(); i++) {
                    answerList.add(null); // or any default value
                }
                System.out.println("getExam().getQuestionIds().size(): " + exam.getQuestionIds().size());
            }


            // Now it's safe to set the answer
            if (questionNumber >= 0 && questionNumber < exam.getQuestionIds().size() && answer != null) {
                answerList.set(questionNumber, answer);
            }
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
        Exam exam = ExamDatabase.getInstance().queryByKey(examId.toString());
        if (exam != null) {
            if (scoreList == null) {
                scoreList = new ArrayList<>(exam.getQuestionIds().size());
                for (int i = 0; i < exam.getQuestionIds().size(); i++) {
                    scoreList.add(0); // Initialize with default score of 0
                }
            }
            if (answerList != null) {
                for (int i = 0; i < exam.getQuestionIds().size(); i++) {
                    Question question = QuestionDatabase.getInstance().queryByKey(exam.getQuestionIds().get(i).toString());
                    if (answerList.get(i) != null && answerList.get(i).equals(question.getAnswer())) {
                        System.out.println(i);
                        scoreList.set(i, question.getScore());
                        score += question.getScore();
                        if (question.getType() == QuestionType.SINGLE) {
                            singleMCScore += question.getScore();
                        }
                        else if (question.getType() == QuestionType.MULTIPLE) {
                            multipleMCScore += question.getScore();
                        }
                        else if (question.getType() == QuestionType.TRUE_FALSE) {
                            tfScore += question.getScore();
                        }
                        else if (question.getType() == QuestionType.SHORT_Q) {
                            sqScore += question.getScore();
                            if (sqQuestionList == null) sqQuestionList = new ArrayList<>();
                            sqQuestionList.add(question.getQuestion());
                            if (sqAnswerList == null) sqAnswerList = new ArrayList<>();
                            sqAnswerList.add(answerList.get(i));
                        }
                        numberOfCorrect++;
                    }
                    System.out.println("By now answerList: " + answerList);
                }
            }
        }
    }

}
