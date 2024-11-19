package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.tools.MsgSender;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

    private Submission submission;
    private int currQuestionNumber;
    private String currQuestionAnswer;

    private ToggleGroup toggleGroup;

    private List<Long> questionList;

    private Timeline countdownTimer;

    private static class CountdownValue {
        int value;

        CountdownValue(int value) {
            this.value = value;
        }

        void decrement() {
            this.value--;
        }

        int getValue() {
            return value;
        }
    }
    @FXML
    Label currQuestionNumberLabel;
    Label optionALabel;
    RadioButton radioButtonA;
    CheckBox checkBoxA;
    HBox optionAHBox;
    Label optionBLabel;
    RadioButton radioButtonB;
    CheckBox checkBoxB;
    HBox optionBHBox;
    Label optionCLabel;
    RadioButton radioButtonC;
    CheckBox checkBoxC;
    HBox optionCHBox;
    Label optionDLabel;
    RadioButton radioButtonD;
    CheckBox checkBoxD;
    HBox optionDHBox;
    Label trueLabel;
    RadioButton trueRadioButton;
    HBox trueHBox;
    Label falseLabel;
    RadioButton falseRadioButton;
    HBox falseHBox;
    TextField questionField;
    TextField shortQuestionAnswerField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Start initializing start exam");
        examNameLbl.setText("Exam Name");
        totalQuestionLbl.setText("Total Question: ");
        currQuestionNumber = 0;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;

        examNameLbl.setText(submission.getExam().getCourseId()+"-"+submission.getExam().getName());
        questionList = submission.getExam().getQuestionIds();
        totalQuestionLbl.setText("Total Question: " + questionList.size());
        ObservableList<String> questionObservableList = FXCollections.observableArrayList();
        currQuestionNumber = 0;
        for (Long questionId : questionList) {
            Question question = QuestionDatabase.getInstance().queryByKey(questionId.toString());
            questionObservableList.add(question.getQuestion());
        }

        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));

        questionColumn.setCellFactory(tc -> {
            TableCell<String, String> cell = new TableCell<>();
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    currQuestionNumber = questionTable.getItems().indexOf(cell.getItem());
                    switchToQuestion(currQuestionNumber);
                }
            });
            return cell;
        });
        questionTable.setItems(questionObservableList);
        switchToQuestion(0);

        CountdownValue countdownFrom = new CountdownValue(submission.getExam().getTime());

        countdownTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (countdownFrom.value > 0) {
                countdownFrom.decrement();
                remainTimeLbl.setText("Remain Time: " + countdownFrom.getValue());
            } else {
                countdownTimer.stop(); // Stop the timer when countdown reaches 0
                handleTimeUp();
            }
        }));
        countdownTimer.setCycleCount(Timeline.INDEFINITE);
        countdownTimer.play();
    }

    public void switchToQuestion(int destQuestionNumber) {

        if (questionList == null || destQuestionNumber < 0 || destQuestionNumber > questionList.size() ) {
            return;
        }

        saveAnswer();

        currQuestionNumberLabel.setText("Question " + (destQuestionNumber+1));


        questionVBox.getChildren().clear(); // Clear existing content


        if (destQuestionNumber == 1) {
            previousBtn.setDisable(true);
            previousBtn.setVisible(false);
            nextBtn.setDisable(false);
            nextBtn.setVisible(true);
            submitBtn.setDisable(true);
            submitBtn.setVisible(false);

        } else if (destQuestionNumber == questionList.size()) {
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

        Question destQuestion = QuestionDatabase.getInstance().queryByKey(questionList.get(destQuestionNumber).toString());

        QuestionType questionType = destQuestion.getType();
        String questionTxt = destQuestion.getQuestion();
        questionField = new TextField(questionTxt);
        questionVBox.getChildren().add(questionField);

        if (questionType == QuestionType.SINGLE) {
            toggleGroup = new ToggleGroup();
            if (!destQuestion.getOptionA().isEmpty()) {
                optionALabel = new Label("A. " + destQuestion.getOptionA());
                radioButtonA = new RadioButton();
                radioButtonA.setId("A");
                radioButtonA.setToggleGroup(toggleGroup);
                optionAHBox = new HBox(10);
                optionAHBox.getChildren().addAll(radioButtonA, optionALabel);
                questionVBox.getChildren().add(optionAHBox);
            }
            if (!destQuestion.getOptionB().isEmpty()) {
                optionBLabel = new Label("B. " + destQuestion.getOptionB());
                radioButtonB = new RadioButton();
                radioButtonB.setId("B");
                radioButtonB.setToggleGroup(toggleGroup);
                optionBHBox = new HBox(10);
                optionBHBox.getChildren().addAll(radioButtonB, optionBLabel);
                questionVBox.getChildren().add(optionBHBox);
            }
            if (!destQuestion.getOptionC().isEmpty()) {
                optionCLabel = new Label("C. " + destQuestion.getOptionC());
                radioButtonC = new RadioButton();
                radioButtonC.setId("C");
                radioButtonC.setToggleGroup(toggleGroup);
                optionCHBox = new HBox(10);
                optionCHBox.getChildren().addAll(radioButtonC, optionCLabel);
                questionVBox.getChildren().add(optionCHBox);
            }
            if (!destQuestion.getOptionD().isEmpty()) {
                optionDLabel = new Label("D. " + destQuestion.getOptionC());
                radioButtonD = new RadioButton();
                radioButtonD.setId("D");
                radioButtonD.setToggleGroup(toggleGroup);
                optionDHBox = new HBox(10);
                optionDHBox.getChildren().addAll(radioButtonD, optionDLabel);
                questionVBox.getChildren().add(optionDHBox);
            }

            if (submission.getAnswer() != null) {
                if (!submission.getAnswer().get(destQuestionNumber).isEmpty()) {
                    if (submission.getAnswer().get(destQuestionNumber).contains("A")) {
                        radioButtonA.setSelected(true);
                    } else if (submission.getAnswer().get(destQuestionNumber).contains("B")) {
                        radioButtonB.setSelected(true);
                    } else if (submission.getAnswer().get(destQuestionNumber).contains("C")) {
                        radioButtonC.setSelected(true);
                    } else if (submission.getAnswer().get(destQuestionNumber).contains("D")) {
                        radioButtonD.setSelected(true);
                    }
                }
            }

        } else if (questionType == QuestionType.MULTIPLE) {
            if (!destQuestion.getOptionA().isEmpty()) {
                optionALabel = new Label("A. " + destQuestion.getOptionA());
                checkBoxA = new CheckBox();
                optionAHBox = new HBox(10);
                optionAHBox.getChildren().addAll(checkBoxA, optionALabel);
                questionVBox.getChildren().add(optionAHBox);
            }
            if (!destQuestion.getOptionB().isEmpty()) {
                optionBLabel = new Label("B. " + destQuestion.getOptionB());
                checkBoxB = new CheckBox();
                optionBHBox = new HBox(10);
                optionBHBox.getChildren().addAll(checkBoxB, optionBLabel);
                questionVBox.getChildren().add(optionBHBox);
            }
            if (!destQuestion.getOptionC().isEmpty()) {
                optionCLabel = new Label("C. " + destQuestion.getOptionC());
                checkBoxC = new CheckBox();
                optionCHBox = new HBox(10);
                optionCHBox.getChildren().addAll(checkBoxC, optionCLabel);
                questionVBox.getChildren().add(optionCHBox);
            }
            if (!destQuestion.getOptionD().isEmpty()) {
                optionDLabel = new Label("D. " + destQuestion.getOptionC());
                checkBoxD = new CheckBox();
                optionDHBox = new HBox(10);
                optionDHBox.getChildren().addAll(checkBoxD, optionDLabel);
                questionVBox.getChildren().add(optionDHBox);
            }

            if(submission.getAnswer() != null) {
                if (!submission.getAnswer().get(destQuestionNumber).isEmpty()) {
                    if (submission.getAnswer().get(destQuestionNumber).contains("A")) {
                        checkBoxA.setSelected(true);
                    } else if (submission.getAnswer().get(destQuestionNumber).contains("B")) {
                        checkBoxB.setSelected(true);
                    } else if (submission.getAnswer().get(destQuestionNumber).contains("C")) {
                        checkBoxC.setSelected(true);
                    } else if (submission.getAnswer().get(destQuestionNumber).contains("D")) {
                        checkBoxD.setSelected(true);
                    }
                }
            }

        } else if (questionType == QuestionType.TRUE_FALSE) {
            toggleGroup = new ToggleGroup();
            trueLabel = new Label("True");
            trueRadioButton = new RadioButton();
            trueRadioButton.setId("T");
            trueRadioButton.setToggleGroup(toggleGroup);
            trueHBox = new HBox(10);
            trueHBox.getChildren().addAll(trueLabel, trueRadioButton);
            falseLabel = new Label("False");
            falseRadioButton = new RadioButton();
            falseRadioButton.setId("F");
            falseRadioButton.setToggleGroup(toggleGroup);
            falseHBox = new HBox(10);
            falseHBox.getChildren().addAll(falseRadioButton, falseLabel);
            questionVBox.getChildren().addAll(questionField, trueHBox, falseHBox);

            if(submission.getAnswer() != null) {
                if (!submission.getAnswer().get(destQuestionNumber).isEmpty()) {
                    if (submission.getAnswer().get(destQuestionNumber).contains("T")) {
                        trueRadioButton.setSelected(true);
                    } else if (submission.getAnswer().get(destQuestionNumber).contains("F")) {
                        falseRadioButton.setSelected(true);
                    }
                }
            }

        } else {
            shortQuestionAnswerField = new TextField();
            questionVBox.getChildren().addAll(questionField, shortQuestionAnswerField);

            if(submission.getAnswer() != null) {
                if (!submission.getAnswer().get(destQuestionNumber).isEmpty()) {
                    shortQuestionAnswerField.setText(submission.getAnswer().get(destQuestionNumber));
                }
            }
        }
    }

    //Save answer: will be used by question label cell and previous and next button
    private void saveAnswer() {
        QuestionType currQuestionType = QuestionDatabase.getInstance().queryByKey(questionList.get(currQuestionNumber).toString()).getType();
        currQuestionAnswer = null;
        if (currQuestionType == QuestionType.SINGLE || currQuestionType == QuestionType.TRUE_FALSE) {
            if (toggleGroup != null) {
                RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                if (selectedRadioButton != null) {
                    currQuestionAnswer = selectedRadioButton.getId();
                }
            }
        } else if (currQuestionType == QuestionType.MULTIPLE) {
            StringBuilder selectedOptions = new StringBuilder();
            if (checkBoxA.isSelected()) {
                selectedOptions.append("A");
            }
            if (checkBoxB.isSelected()) {
                selectedOptions.append("B");
            }
            if (checkBoxC.isSelected()) {
                selectedOptions.append("C");
            }
            if (checkBoxD.isSelected()) {
                selectedOptions.append("D");
            }
            currQuestionAnswer = selectedOptions.toString().trim();

        } else { //currQuestionType == QuestionType.SHORT_Q
            currQuestionAnswer = shortQuestionAnswerField.getText();
        }
        if (currQuestionAnswer != null) submission.saveAnswer(currQuestionNumber, currQuestionAnswer);
    }


    @FXML
    public void previous() {
        saveAnswer();
        switchToQuestion(currQuestionNumber-1);
    }

    @FXML
    public void next() {
        saveAnswer();
        switchToQuestion(currQuestionNumber+1);

    }

    @FXML
    public void submit(ActionEvent e) {
        saveAnswer();
        submission.calculateScore();
        try {
            SubmissionDatabase.getInstance().addSubmission(submission);
            MsgSender.showConfirm("Hint", "Student Register successful.", () -> close(e));
        } catch (Exception e1) {
            MsgSender.showConfirm("Submit Error", e1.getMessage(), () -> {});
        }
    }

    public void close(ActionEvent e) {
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    public void handleTimeUp() {
        //TODO
        ActionEvent dummyEvent = new ActionEvent();
        try {
            MsgSender.showConfirm("Time's up", "Time's up!\nAll answer saved will be submitted automatically.", () -> submit(dummyEvent));
        } catch (Exception e1) {
            MsgSender.showConfirm("Submit Error", e1.getMessage(), () -> {
            });
        }

    }
}
