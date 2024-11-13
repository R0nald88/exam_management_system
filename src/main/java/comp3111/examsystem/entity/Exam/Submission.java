package comp3111.examsystem.entity.Exam;

import java.time.LocalDateTime;

import comp3111.examsystem.entity.Personnel.Student;
// import comp3111.examsystem.entity.Exam;

public class Submission {
    private Student student;
    // private Exam exam;
    private String[] answer;
    private LocalDateTime startTime;
    private double score = 0;



    public Submission(Student student) { //, Exam exam) {
        this.student = student;
        //this.exam = exam;
        //this.answer = new String[exam.getNumberOfQuestions()];
        startTime = LocalDateTime.now();
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void appendAnswer(int questionNumber, String answer) {


    }

}
