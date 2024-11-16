package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Personnel.Student;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentMainController implements Initializable {
    @FXML
    private ChoiceBox<String> examCombox;
    private Student student;
    private List<Exam> examList;

    public void setStudent(Student student) {
        this.student = student;
    }

    public void initialize(URL location, ResourceBundle resources) {
        examList = ExamDatabase.getInstance().filter(student.getName(), null, "yes");
        if (!examList.isEmpty()) {
            for (Exam e: examList) {
                examCombox.getItems().add(e.getName());
            }
        }
    }

    @FXML
    public void openExamUI(ActionEvent e) {
        if (examCombox.getValue() != null) {
            Exam selectedExam = ExamDatabase.getInstance().queryByField("Name", examCombox.getValue()).getFirst();
            Submission submission = new Submission();
            submission.setStudent(student);
            submission.setExam(selectedExam);

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentStartExamUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Start Exam");
            try {
                Parent root = fxmlLoader.load();
                StudentStartExamController studentStartExamController = fxmlLoader.getController();
                studentStartExamController.setSubmission(submission);
                stage.setScene(new Scene(fxmlLoader.load()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        }
    }

    @FXML
    public void openGradeStatistic(ActionEvent e) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentGradeStatisticUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Grade Statistics");
        try {
            Parent root = fxmlLoader.load();
            StudentGradeStatisticController studentGradeStatisticController = fxmlLoader.getController();
            studentGradeStatisticController.setStudent(student);
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch ( IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}
