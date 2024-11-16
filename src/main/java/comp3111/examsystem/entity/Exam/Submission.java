package comp3111.examsystem.entity.Exam;

import java.time.LocalDateTime;
import java.util.List;

import comp3111.examsystem.entity.Personnel.Student;
// import comp3111.examsystem.entity.Exam;

public class Submission {
    private Student student;
    private Exam exam;
    private String[] answer;
    private LocalDateTime startTime;
    private double score = 0;



    public Submission(Student student, Exam exam) {
        this.student = student;
        this.exam = exam;
        this.answer = new String[exam.getQuestionIds().size()];
        startTime = LocalDateTime.now();
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

    public String[] getAnswer() {
        return answer;
    }

    public double getScore() {
        return score;
    }

    public void appendAnswer(int questionNumber, String answer) {
        //TODO

    }

    public void calculateScore() {
        //TODO
    }

}
