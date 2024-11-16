package comp3111.examsystem.entity.Exam;

import com.google.gson.Gson;
import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import comp3111.examsystem.entity.Personnel.Student;
// import comp3111.examsystem.entity.Exam;

public class Submission extends Entity{
    private Student student;
    private Exam exam;
    private List<String> answerList;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double score = 0;

    public Submission() {
        super(System.currentTimeMillis());
        startTime = LocalDateTime.now();
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void endExam() {
        this.endTime = LocalDateTime.now();
    }

    public String getStartTime() {
        return startTime.toString();
    }

    public String getEndTime() {
        return endTime.toString();
    }

    public String getTimeSpent() {
        return String.valueOf(Duration.between(startTime, endTime).getSeconds());
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Student getStudent() {
        return student;
    }

    public Exam getExam() {
        return exam;
    }

    public List<String> getAnswer() {
        return answerList;
    }

    public double getScore() {
        return score;
    }

    public void saveAnswer(int questionNumber, String answer) {
        answerList.set(questionNumber, answer);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public void calculateScore() {
        for (int i = 0; i < exam.getQuestionIds().size(); i++) {
            Question question = QuestionDatabase.getInstance().queryByKey(exam.getQuestionIds().get(i).toString());
            if (answerList.get(i).equals(question.getAnswer())) {
                score += question.getScore();
            }
        }
    }

}
