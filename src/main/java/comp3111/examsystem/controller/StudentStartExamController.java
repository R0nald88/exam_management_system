package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Personnel.StudentDatabase;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.tools.MsgSender;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private Student student;
    private Exam exam;

    private int currQuestionNumber;
    private String currQuestionAnswer;

    private ToggleGroup toggleGroup;

    private List<Long> questionList;

    private Timeline countdownTimer;
    private CountdownValue countdownFrom;

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
    List<String> questionStringList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Start initializing start exam");
        examNameLbl.setText("Exam Name");
        totalQuestionLbl.setText("Total Question: ");
        currQuestionNumber = 0;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
        exam = ExamDatabase.getInstance().queryByKey(submission.getExamId().toString());
        this.submission.setFullScore(exam.getFullScore());
        student = StudentDatabase.getInstance().queryByKey(submission.getStudentId().toString());

        // Set the exam name and total questions
        examNameLbl.setText(exam.getCourseId() + "-" + exam.getName());
        questionList = exam.getQuestionIds();
        totalQuestionLbl.setText("Total Question: " + questionList.size());
        currQuestionNumber = 0;

        // Populate questionStringList with questions from the database
        questionStringList = new ArrayList<>();
        for (Question question : QuestionDatabase.getInstance().queryByKeys(questionList.stream().map(Object::toString).toList())) {
            questionStringList.add(question.getQuestion());
        }

        // Debugging output
        System.out.println("Question String List: " + questionStringList);

        // Set up the TableColumn to display questions
        questionColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));

        // Set items for questionTable
        ObservableList<String> observableQuestions = FXCollections.observableList(questionStringList);
        questionTable.setItems(observableQuestions);

        // Set up cell click event
        questionColumn.setCellFactory(tc -> {
            TableCell<String, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                    }
                }
            };

            // Add mouse click event to the cell
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    saveAnswer();
                    int rowIndex = cell.getIndex(); // Get the row index
                    switchToQuestion(rowIndex); // Call switchToQuestion with the row index
                }
            });

            return cell;
        });

        // Additional configuration for the TableView
        questionTable.setEditable(false);
        questionTable.setPlaceholder(new Label("No question record."));
        questionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Switch to the first question
        switchToQuestion(0);

        // Start the countdown timer
        CountdownValue countdownFrom = new CountdownValue(exam.getTime());
        startCountdownTimer(countdownFrom);
    }

    private void startCountdownTimer(CountdownValue countdownFrom) {
        this.countdownFrom = countdownFrom; // Store the value
        if (countdownTimer == null) {
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
    }

    public void switchToQuestion(int destQuestionNumber) {

        if (questionList == null || destQuestionNumber < 0 || destQuestionNumber >= questionList.size()) {
            System.out.println("Invalid destQuestionNumber");
            return;
        }

        currQuestionNumberLabel.setText("Question " + (destQuestionNumber + 1));


        questionVBox.getChildren().clear(); // Clear existing content


        previousBtn.setVisible(destQuestionNumber > 0);
        previousBtn.setDisable(destQuestionNumber == 0);

        nextBtn.setVisible(destQuestionNumber < questionList.size() - 1);
        nextBtn.setDisable(destQuestionNumber >= questionList.size() - 1);

        submitBtn.setVisible(destQuestionNumber == questionList.size() - 1);
        submitBtn.setDisable(destQuestionNumber < questionList.size() - 1);

        Question destQuestion = QuestionDatabase.getInstance().queryByKey(questionList.get(destQuestionNumber).toString());

        QuestionType questionType = destQuestion.getType();
        String questionTxt = destQuestion.getQuestion();
        questionField = new TextField(questionTxt);
        questionField.setEditable(false);
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
                optionDLabel = new Label("D. " + destQuestion.getOptionD());
                radioButtonD = new RadioButton();
                radioButtonD.setId("D");
                radioButtonD.setToggleGroup(toggleGroup);
                optionDHBox = new HBox(10);
                optionDHBox.getChildren().addAll(radioButtonD, optionDLabel);
                questionVBox.getChildren().add(optionDHBox);
            }

            if (submission.getAnswer() != null) {
                if (submission.getAnswer().get(destQuestionNumber) != null && !submission.getAnswer().get(destQuestionNumber).isEmpty()) {
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
                optionDLabel = new Label("D. " + destQuestion.getOptionD());
                checkBoxD = new CheckBox();
                optionDHBox = new HBox(10);
                optionDHBox.getChildren().addAll(checkBoxD, optionDLabel);
                questionVBox.getChildren().add(optionDHBox);
            }

            if (submission.getAnswer() != null) {
                if (submission.getAnswer().get(destQuestionNumber) != null && !submission.getAnswer().get(destQuestionNumber).isEmpty()) {
                    if (submission.getAnswer().get(destQuestionNumber).contains("A")) {
                        checkBoxA.setSelected(true);
                    }
                    if (submission.getAnswer().get(destQuestionNumber).contains("B")) {
                        checkBoxB.setSelected(true);
                    }
                    if (submission.getAnswer().get(destQuestionNumber).contains("C")) {
                        checkBoxC.setSelected(true);
                    }
                    if (submission.getAnswer().get(destQuestionNumber).contains("D")) {
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
            trueHBox.getChildren().addAll(trueRadioButton, trueLabel);
            falseLabel = new Label("False");
            falseRadioButton = new RadioButton();
            falseRadioButton.setId("F");
            falseRadioButton.setToggleGroup(toggleGroup);
            falseHBox = new HBox(10);
            falseHBox.getChildren().addAll(falseRadioButton, falseLabel);
            questionVBox.getChildren().add(trueHBox);
            questionVBox.getChildren().add(falseHBox);

            if (submission.getAnswer() != null) {
                if (submission.getAnswer().get(destQuestionNumber) != null && !submission.getAnswer().get(destQuestionNumber).isEmpty()) {
                    if (submission.getAnswer().get(destQuestionNumber).contains("T")) {
                        trueRadioButton.setSelected(true);
                    } else if (submission.getAnswer().get(destQuestionNumber).contains("F")) {
                        falseRadioButton.setSelected(true);
                    }
                }
            }

        } else {
            shortQuestionAnswerField = new TextField();
            VBox.setMargin(shortQuestionAnswerField, new Insets(10.0));
            questionVBox.getChildren().add(shortQuestionAnswerField);

            if (submission.getAnswer() != null) {
                if (submission.getAnswer().get(destQuestionNumber) != null && !submission.getAnswer().get(destQuestionNumber).isEmpty()) {
                    shortQuestionAnswerField.setText(submission.getAnswer().get(destQuestionNumber));
                }
            }
        }
        currQuestionNumber = destQuestionNumber;
    }

    //Save answer: will be used by question label cell and previous and next button
    private void saveAnswer() {
        System.out.println("At saveAnswer, currQuestionNumber: " + currQuestionNumber);
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
        System.out.println("After saveAnswer, currQuestionNumber: " + currQuestionNumber + " currQuestionAnswer: " + currQuestionAnswer);
        if (currQuestionAnswer != null && !currQuestionAnswer.isEmpty())
            submission.saveAnswer(currQuestionNumber, currQuestionAnswer);
    }


    @FXML
    public void previous() {
        System.out.println("Previous button clicked. Current question number: " + currQuestionNumber);
        if (currQuestionNumber > 0) {
            saveAnswer();
            switchToQuestion(currQuestionNumber - 1);
        }
    }

    @FXML
    public void next() {
        System.out.println("Next button clicked. Current question number: " + currQuestionNumber);
        if (currQuestionNumber < (questionList.size() - 1)) {
            saveAnswer();
            switchToQuestion(currQuestionNumber + 1);
        }
    }

    @FXML
    public void submit(ActionEvent e) {
        saveAnswer();
        submission.calculateScore();

        // Access the countdown value
        countdownTimer.stop();
        int remainingTime = countdownFrom.getValue();
        System.out.println("Remaining time at submission: " + remainingTime);
        submission.setTimeSpend(exam.getTime() - remainingTime);

        try {
            SubmissionDatabase.getInstance().addSubmission(submission);
            MsgSender.showConfirm("Hint", "Student submit exam successful.", () -> close(e));
            MsgSender.showConfirm("Your Exam Score", submission.getNumberOfCorrect() + "/" + exam.getQuestionIds().size() +" Correct, the precision is "
                    + (int) (((double) submission.getScore()/exam.getFullScore()) * 100) + "%, the score is " + submission.getScore() + "/" + exam.getFullScore(), () -> { });
        } catch (Exception e1) {
            MsgSender.showConfirm("Submit Error", e1.getMessage(), () -> {
            });
            //e1.printStackTrace();
        }

        try {
            // Load the FXML file first
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
            Parent root = fxmlLoader.load(); // Load the root

            // Get the controller and set the student
            StudentMainController studentMainController = fxmlLoader.getController();
            studentMainController.setStudent(StudentDatabase.getInstance().queryByKey(submission.getStudentId().toString()));

            // Create the new stage and set the scene
            Stage stage = new Stage();
            stage.setTitle("Hi " + StudentDatabase.getInstance().queryByKey(submission.getStudentId().toString()).getUsername() + ", Welcome to HKUST Examination System");
            stage.setScene(new Scene(root)); // Use the loaded root

            // Close the current login window
            ((Stage) questionVBox.getScene().getWindow()).close();
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void close(ActionEvent e) {
        Stage stage = (Stage)  questionVBox.getScene().getWindow();
        stage.close();
    }

    public void handleTimeUp() {
        // Use Platform.runLater to ensure that the dialog is shown after current processing
        Platform.runLater(() -> {
            ActionEvent dummyEvent = new ActionEvent();
            try {
                MsgSender.showConfirm("Time's up", "Your time to finish your exam is up! \nAll answers saved will be submitted automatically.", () -> submit(dummyEvent));
            } catch (Exception e1) {
                MsgSender.showConfirm("Handle Time Up Error", e1.getMessage(), () -> {
                });
                //e1.printStackTrace();
            }
        });
    }
}
