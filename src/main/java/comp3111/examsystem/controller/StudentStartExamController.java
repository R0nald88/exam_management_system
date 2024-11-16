package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Questions.QuestionType;
import comp3111.examsystem.entity.Questions.Question;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentStartExamController implements Initializable {

    @FXML
    public Label examNameLbl;
    @FXML
    public Label totalQuestionLbl;
    @FXML
    public Label remainTimeLbl;
    @FXML
    private TableView<String> questionTable;
    @FXML
    private TableColumn<String, String> questionColumn;
    @FXML
    private VBox questionVBox;
    @FXML
    private Button previousBtn, nextBtn, submitBtn;

    private Exam exam;
    private Question currentQuestion; //TODO: change it to id
    private ToggleGroup toggleGroup;

    private List<Long> questionList;

    private Timeline countdownTimer;

    public void setExam(Exam selectedExam) {
        this.exam = selectedExam;
    }

    private static class CountdownValue {
        int value;

        CountdownValue(int value) {
            this.value = value;
        }

        void decrement() {
            this.value--;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionList = exam.getQuestionIds();
        //TODO: questionTable cells should be question string. How to get questions by ID


        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));

        CountdownValue countdownFrom = new CountdownValue(exam.getTime());

        countdownTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (countdownFrom.value > 0) {
                countdownFrom.decrement();
                remainTimeLbl.setText(String.valueOf(countdownFrom));
            } else {
                countdownTimer.stop(); // Stop the timer when countdown reaches 0
                handleTimeUp();
            }
        }));
        countdownTimer.setCycleCount(Timeline.INDEFINITE);
        countdownTimer.play();

    }

    //TODO: get saved answer
    public void switchToQuestion(Question question) {
        //TODO: switch to get question By id and get question number by id
        /*
        if (questionList == null || questionId == null) {

            question = null;
        }

        for (int i = 0; i < list.size(); i++) {
            if (target.equals(list.get(i))) {
                return i; // Return the index if the target value is found
            }
        }
        return -1;
        */
        saveAnswer(currentQuestion);
        questionVBox.getChildren().clear(); // Clear existing content

        int questionNumber = 1; //TODO: questionNumber not found yet!!!
        if (questionNumber == 1) {
            previousBtn.setDisable(true);
            previousBtn.setVisible(false);
            nextBtn.setDisable(false);
            nextBtn.setVisible(true);
            submitBtn.setDisable(true);
            submitBtn.setVisible(false);

        } else if (questionNumber == questionList.size()) {
            previousBtn.setDisable(false);
            previousBtn.setVisible(true);
            nextBtn.setDisable(true);
            nextBtn.setVisible(false);
            submitBtn.setDisable(false);
            submitBtn.setVisible(true);
        } else {
            previousBtn.setDisable(false);
            previousBtn.setVisible(true);
            nextBtn.setDisable(false);
            nextBtn.setVisible(true);
            submitBtn.setDisable(true);
            submitBtn.setVisible(false);
        }

        QuestionType questionType = question.getType();
        String questionTxt = question.getQuestion();

        if (questionType == QuestionType.SINGLE) {
            toggleGroup = new ToggleGroup();
            Label optionALabel = new Label("A. " + question.getOptionA());
            RadioButton radioButtonA = new RadioButton();
            radioButtonA.setToggleGroup(toggleGroup);
            HBox optionAHBox = new HBox(10);
            optionAHBox.getChildren().addAll(radioButtonA, optionALabel);
            Label optionBLabel = new Label("B. " + question.getOptionB());
            RadioButton radioButtonB = new RadioButton();
            radioButtonB.setToggleGroup(toggleGroup);
            HBox optionBHBox = new HBox(10);
            optionBHBox.getChildren().addAll(radioButtonB, optionBLabel);
            Label optionCLabel = new Label("C. " + question.getOptionC());
            RadioButton radioButtonC = new RadioButton();
            radioButtonC.setToggleGroup(toggleGroup);
            HBox optionCHBox = new HBox(10);
            optionCHBox.getChildren().addAll(radioButtonC, optionCLabel);
            Label optionDLabel = new Label("C. " + question.getOptionC());
            RadioButton radioButtonD = new RadioButton();
            radioButtonD.setToggleGroup(toggleGroup);
            HBox optionDHBox = new HBox(10);
            optionDHBox.getChildren().addAll(radioButtonD, optionDLabel);
            questionVBox.getChildren().addAll(optionAHBox, optionBHBox, optionCHBox, optionDHBox);

        } else if (questionType == QuestionType.MULTIPLE) {
            Label optionALabel = new Label("A. " + question.getOptionA());
            CheckBox checkBoxA = new CheckBox();
            HBox optionAHBox = new HBox(10);
            optionAHBox.getChildren().addAll(checkBoxA, optionALabel);
            Label optionBLabel = new Label("B. " + question.getOptionB());
            CheckBox checkBoxB = new CheckBox();
            HBox optionBHBox = new HBox(10);
            optionBHBox.getChildren().addAll(checkBoxB, optionBLabel);
            Label optionCLabel = new Label("C. " + question.getOptionC());
            CheckBox checkBoxC = new CheckBox();
            HBox optionCHBox = new HBox(10);
            optionCHBox.getChildren().addAll(checkBoxC, optionCLabel);
            Label optionDLabel = new Label("C. " + question.getOptionC());
            CheckBox checkBoxD = new CheckBox();
            HBox optionDHBox = new HBox(10);
            optionDHBox.getChildren().addAll(checkBoxD, optionDLabel);
            questionVBox.getChildren().addAll(optionAHBox, optionBHBox, optionCHBox, optionDHBox);

        } else if (questionType == QuestionType.TRUE_FALSE) {
            toggleGroup = new ToggleGroup();
            Label trueLabel = new Label("True");
            RadioButton trueRadioButton = new RadioButton();
            trueRadioButton.setToggleGroup(toggleGroup);
            HBox trueHBox = new HBox(10);
            trueHBox.getChildren().addAll(trueLabel, trueRadioButton);
            Label falseLabel = new Label("C. " + question.getOptionC());
            RadioButton falseRadioButton = new RadioButton();
            falseRadioButton.setToggleGroup(toggleGroup);
            HBox falseHBox = new HBox(10);
            falseHBox.getChildren().addAll(falseRadioButton, falseLabel);
            questionVBox.getChildren().addAll(trueHBox, falseHBox);

        } else {
            TextField questionField = new TextField(questionTxt);
            TextField textField = new TextField();
            questionVBox.getChildren().addAll(questionField, textField);
        }
    }

    //TODO: save answer
    private void saveAnswer(Question currentQuestion) {

    }


    @FXML
    public void previous() {
        //TODO: use currentQuestion
    }

    @FXML
    public void next() {
        //TODO: use currentQuestion
    }

    @FXML
    public void submit() {
        //TODO: use currentQuestion
    }

    public void handleTimeUp() {
        //TODO
    }
}
