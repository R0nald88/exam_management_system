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
    private String selectedExamName;
    private String selectedExamCourseId;

    public void setStudent(Student student) {
        this.student = student;
    }

    public void initialize(URL location, ResourceBundle resources) {
        examList = ExamDatabase.getInstance().filter(null, null, "true");
        if (!examList.isEmpty()) {
            System.out.println("ExamList have exam");
            for (Exam e: examList) {
                examCombox.getItems().add(e.getCourseId()+"-"+e.getName());

            }

            examCombox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                int selectedExamIndex = examCombox.getItems().indexOf(newValue);
                System.out.println("Selected index: " + selectedExamIndex);
                selectedExamName = examList.get(selectedExamIndex).getName();
                selectedExamCourseId = examList.get(selectedExamIndex).getCourseId();
            });

        } else System.out.println("ExamList have no exam");

    }

    @FXML
    public void openExamUI(ActionEvent e) {
        if (examCombox.getValue() != null) {
            Exam selectedExam = ExamDatabase.getInstance().filter(selectedExamName, selectedExamCourseId,"true").getFirst();
            Submission submission = new Submission();
            submission.setStudent(student);
            submission.setExam(selectedExam);

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentStartExamUI.fxml"));
                Parent root = fxmlLoader.load();
                StudentStartExamController studentStartExamController = fxmlLoader.getController();
                studentStartExamController.setSubmission(submission);
                Stage stage = new Stage();
                stage.setTitle("Start Exam");
                stage.setScene(new Scene(root));

                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
                stage.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @FXML
    public void openGradeStatistic(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentGradeStatisticUI.fxml"));

            Parent root = fxmlLoader.load();
            StudentGradeStatisticController studentGradeStatisticController = fxmlLoader.getController();
            studentGradeStatisticController.setStudent(student);

            Stage stage = new Stage();
            stage.setTitle("Grade Statistics");
            stage.setScene(new Scene(root));

            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();

        } catch ( IOException e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}
