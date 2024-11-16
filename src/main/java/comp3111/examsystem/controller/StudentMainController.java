package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.entity.Exam.Exam;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentMainController implements Initializable {
    @FXML
    ComboBox<String> examCombox;

    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void openExamUI(ActionEvent e) {
        Exam selectedExam = null;// Retrieve selected exam details

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentStartExamUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Start Exam");
        try {
            Parent root = fxmlLoader.load();
            StudentStartExamController studentStartExamController = fxmlLoader.getController();
            studentStartExamController.setExam(selectedExam);
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch ( IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public void openGradeStatistic(ActionEvent e) {
        //TODO: set Student Grade Statistics content to this student


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentGradeStatisticUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Grade Statistics");
        try {
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
