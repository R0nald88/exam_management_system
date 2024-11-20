package comp3111.examsystem.entity.Exam;

import com.google.gson.Gson;
import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import comp3111.examsystem.entity.Personnel.Student;
// import comp3111.examsystem.entity.Exam;

public class Submission extends Entity{
    private String studentUsername;
    private Long examId;
    private String courseId;
    private List<String> answerList;
    private int score = 0;
    private int fullScore;
    private int numberOfCorrect = 0;
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

    public void setScore(int score) {
        this.score = score;
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

    public void calculateScore() {
        Exam exam = ExamDatabase.getInstance().queryByKey(examId.toString());
        if (exam != null) {
            if (answerList != null) {
                for (int i = 0; i < exam.getQuestionIds().size(); i++) {
                    Question question = QuestionDatabase.getInstance().queryByKey(exam.getQuestionIds().get(i).toString());
                    if (answerList.get(i) != null && answerList.get(i).equals(question.getAnswer())) {
                        score += question.getScore();
                        numberOfCorrect++;
                    }
                }
            }
        }
    }

}
