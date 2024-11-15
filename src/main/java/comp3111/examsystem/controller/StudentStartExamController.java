package comp3111.examsystem.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentStartExamController implements Initializable {

    @FXML
    public Label quizNameLbl;
    @FXML
    public Label totalQuestionLbl;
    @FXML
    public Label remainTimeLbl;
    @FXML
    private TableView<String> questionTable;
    @FXML
    private TableColumn<String, String> questionColumn;

    private final ObservableList<String> questionList = FXCollections.observableArrayList();
    private Timeline countdownTimer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionTable.setItems(questionList);
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));

        int countdownFrom; // Countdown from 60 seconds
        /*countdownFrom = 60;

        countdownTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (countdownFrom > 0) {
                countdownFrom--;
                remainTimeLbl.setText(String.valueOf(countdownFrom));
            } else {
                countdownTimer.stop(); // Stop the timer when countdown reaches 0
            }
        }));
        countdownTimer.setCycleCount(Timeline.INDEFINITE);
        countdownTimer.play();
        */
    }

    @FXML
    public void previous() {
    }

    @FXML
    public void next() {
    }

    @FXML
    public void submit() {
    }
}
