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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.geometry.Pos.*;

/**
 * The StudentStartExamController class manages the exam taking process for students.
 * It handles loading exam data, navigating between questions, saving answers,
 * and submitting the exam.
 */
public class StudentStartExamController implements Initializable {

    @FXML
    private Label examNameLbl, totalQuestionLbl, remainTimeLbl, currQuestionNumberLabel;
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

    /**
     * A private inner class for managing countdown timer values.
     */
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

    private Label optionALabel, optionBLabel, optionCLabel, optionDLabel, trueLabel, falseLabel, shortQuestionAnswerLabel;
    private RadioButton radioButtonA, radioButtonB, radioButtonC, radioButtonD, trueRadioButton, falseRadioButton;
    private CheckBox checkBoxA, checkBoxB, checkBoxC, checkBoxD;
    private HBox optionAHBox, optionBHBox, optionCHBox, optionDHBox, trueHBox, falseHBox, shortQuestionAnswerHBox;
    private TextField questionField, shortQuestionAnswerField;

    List<String> questionStringList;


    private static StudentStartExamController instance;

    /**
     * Sets the instance of this controller.
     *
     * @param studentStartExamController The instance to set.
     * @author Li Ching Ho
     */
    public static void setInstance(StudentStartExamController studentStartExamController) {
        instance = studentStartExamController;
    }

    /**
     * Gets the instance of this controller.
     *
     * @return The current instance.
     * @author Li Ching Ho
     */
    public static StudentStartExamController getInstance() {
        return instance;
    }

    private Parent root;
    /**
     * Sets the root parent for the controller.
     *
     * @param root The root parent.
     * @author Li Ching Ho
     */
    public void setRoot(Parent root) {
        this.root = root;
    }

    /**
     * Gets the root parent for the controller.
     *
     * @return root The root parent.
     * @author Li Ching Ho
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Gets the exam associated with this controller.
     *
     * @return The exam.
     * @author Li Ching Ho
     */
    public Exam getExam() {
        return exam;
    }

    /**
     * Initializes the controller after its root element has been processed.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     * @author Li Ching Ho
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //System.out.println("Start initializing start exam");
        examNameLbl.setText("Exam Name");
        totalQuestionLbl.setText("Total Question: ");
        currQuestionNumber = 0;
    }

    /**
     * Sets the submission object and initializes the exam data.
     *
     * @param submission The submission associated with the exam.
     * @author Li Ching Ho
     */
    public void setSubmission(Submission submission) {
        this.submission = submission;
        exam = ExamDatabase.getInstance().queryByKey(submission.getExamId().toString());
        student = StudentDatabase.getInstance().queryByField("username",submission.getStudentUsername()).getFirst();
        //System.out.println("In setSubmission, student:" + student);

        // Set the exam name and total questions
        examNameLbl.setText(exam.getCourseId() + "-" + exam.getName());
        questionList = exam.getQuestionIds();
        totalQuestionLbl.setText("Total Question: " + questionList.size());
        currQuestionNumber = 0;

        // Populate questionStringList with questions from the database
        questionStringList = new ArrayList<>();
        for (Question question : submission.getQuestionObjectList()) {
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
        System.out.println("Now attempting: " + exam.getCourseId() + "-" + exam.getName());
    }

    /**
     * Starts the countdown timer for the exam.
     *
     * @param countdownFrom The initial countdown value.
     * @author Li Ching Ho
     */
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

    /**
     * Switches to a specific question by updating the UI elements.
     * Saves answer of the current question
     *
     * @param destQuestionNumber The index of the question to switch to.
     * @author Li Ching Ho
     */
    public void switchToQuestion(int destQuestionNumber) {

        if (questionList == null || destQuestionNumber < 0 || destQuestionNumber >= questionList.size()) {
            System.out.println("Invalid destQuestionNumber");
            return;
        }

        currQuestionNumberLabel.setText("Question " + (destQuestionNumber + 1));
        currQuestionNumberLabel.setAlignment(CENTER);
        currQuestionNumberLabel.setFont(Font.font(14.0));


        questionVBox.getChildren().clear(); // Clear existing content


        previousBtn.setVisible(destQuestionNumber > 0);
        previousBtn.setDisable(destQuestionNumber == 0);

        nextBtn.setVisible(destQuestionNumber < questionList.size() - 1);
        nextBtn.setDisable(destQuestionNumber >= questionList.size() - 1);

        submitBtn.setVisible(destQuestionNumber == questionList.size() - 1);
        submitBtn.setDisable(destQuestionNumber < questionList.size() - 1);

        System.out.println("questionList: " + questionList);
        System.out.println("destQuestionNumber:" + destQuestionNumber);

        Question destQuestion = submission.getQuestionObjectList().get(destQuestionNumber);

        QuestionType questionType = destQuestion.getType();
        String questionTxt = destQuestion.getQuestion();
        questionField = new TextField(questionTxt);
        questionField.setEditable(false);
        questionField.setAlignment(TOP_LEFT);
        questionField.setMinHeight(50.0);
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

            if (submission.getAnswerList() != null) {
                if (submission.getAnswerList().get(destQuestionNumber) != null && !submission.getAnswerList().get(destQuestionNumber).isEmpty()) {
                    if (submission.getAnswerList().get(destQuestionNumber).contains("A")) {
                        radioButtonA.setSelected(true);
                    } else if (submission.getAnswerList().get(destQuestionNumber).contains("B")) {
                        radioButtonB.setSelected(true);
                    } else if (submission.getAnswerList().get(destQuestionNumber).contains("C")) {
                        radioButtonC.setSelected(true);
                    } else if (submission.getAnswerList().get(destQuestionNumber).contains("D")) {
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

            if (submission.getAnswerList() != null) {
                if (submission.getAnswerList().get(destQuestionNumber) != null && !submission.getAnswerList().get(destQuestionNumber).isEmpty()) {
                    if (submission.getAnswerList().get(destQuestionNumber).contains("A")) {
                        checkBoxA.setSelected(true);
                    }
                    if (submission.getAnswerList().get(destQuestionNumber).contains("B")) {
                        checkBoxB.setSelected(true);
                    }
                    if (submission.getAnswerList().get(destQuestionNumber).contains("C")) {
                        checkBoxC.setSelected(true);
                    }
                    if (submission.getAnswerList().get(destQuestionNumber).contains("D")) {
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

            if (submission.getAnswerList() != null) {
                if (submission.getAnswerList().get(destQuestionNumber) != null && !submission.getAnswerList().get(destQuestionNumber).isEmpty()) {
                    if (submission.getAnswerList().get(destQuestionNumber).contains("T")) {
                        trueRadioButton.setSelected(true);
                    } else if (submission.getAnswerList().get(destQuestionNumber).contains("F")) {
                        falseRadioButton.setSelected(true);
                    }
                }
            }

        } else {
            shortQuestionAnswerLabel = new Label("Your Answer:");
            shortQuestionAnswerLabel.setPrefSize(90.0, 125.0);
            shortQuestionAnswerLabel.setAlignment(TOP_CENTER);
            shortQuestionAnswerField = new TextField();
            shortQuestionAnswerField.setPrefSize(360.0, 125.0);
            shortQuestionAnswerField.setAlignment(TOP_LEFT);
            VBox.setMargin(shortQuestionAnswerField, new Insets(10.0));
            shortQuestionAnswerHBox = new HBox(10);
            shortQuestionAnswerHBox.setPrefSize(450.0,125.0);
            shortQuestionAnswerHBox.getChildren().addAll(shortQuestionAnswerLabel, shortQuestionAnswerField);
            questionVBox.getChildren().add(shortQuestionAnswerHBox);

            if (submission.getAnswerList() != null) {
                if (submission.getAnswerList().get(destQuestionNumber) != null && !submission.getAnswerList().get(destQuestionNumber).isEmpty()) {
                    shortQuestionAnswerField.setText(submission.getAnswerList().get(destQuestionNumber));
                }
            }
        }
        currQuestionNumber = destQuestionNumber;
    }

    /**
     * Saves the current answer for the question being answered.
     * Used by previousBtn, nextBtn, submitBtn and question clicked in side column
     *
     * @author Li Ching Ho
     */
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
        if (currQuestionAnswer != null)
            submission.saveAnswer(currQuestionNumber, currQuestionAnswer);
    }


    /**
     * Handles the action of clicking the previous question button.
     * Used by previousBtn
     *
     * @author Li  Ching Ho
     */
    @FXML
    public void previous() {
        System.out.println("Previous button clicked. Current question number: " + currQuestionNumber);
        if (currQuestionNumber > 0) {
            saveAnswer();
            switchToQuestion(currQuestionNumber - 1);
        }
    }

    /**
     * Handles the action of clicking the next question button.
     * Used by nextBtn
     *
     * @author Li Ching Ho
     */
    @FXML
    public void next() {
        System.out.println("Next button clicked. Current question number: " + currQuestionNumber);
        if (currQuestionNumber < (questionList.size() - 1)) {
            saveAnswer();
            switchToQuestion(currQuestionNumber + 1);
        }
    }

    /**
     * Handles the action of submitting the exam.
     * Calculates Score and displays some MsgBox after submission.
     * Used by submitBtn
     *
     * @param e The ActionEvent triggered by the submit button.
     * @author Li Ching Ho
     */
    @FXML
    public void submit(ActionEvent e) {
        saveAnswer();
        submission.calculateInitialScore();

        // Access the countdown value
        countdownTimer.stop();
        int remainingTime = countdownFrom.getValue();
        System.out.println("Remaining time at submission: " + remainingTime);
        submission.setTimeSpend(exam.getTime() - remainingTime);

        try {
            SubmissionDatabase.getInstance().addSubmission(submission);
            instance = null;
            MsgSender.showConfirm("Hint", "Student submit exam successful.", () -> close(e));
            MsgSender.showConfirm("Your Exam Score", submission.getNumberOfCorrect() + "/" + exam.getQuestionIds().size() +" Correct, the precision is "
                    + (int) (((double) submission.getScore()/submission.getFullScore()) * 100) + "%, the score is " + submission.getScore() + "/" + submission.getFullScore(), () -> { });
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
            studentMainController.setStudent(student);

            // Create the new stage and set the scene
            Stage stage = new Stage();
            stage.setTitle("Hi " + submission.getStudentUsername() + ", Welcome to HKUST Examination System");
            stage.setScene(new Scene(root)); // Use the loaded root
            studentMainController.setStage(stage);

            // Close the current login window
            ((Stage) questionVBox.getScene().getWindow()).close();
            stage.show();
        } catch (IOException e1) {
            MsgSender.showConfirm("Submit back to Student Main Error", e1.getMessage(), () -> {
            });
            //e1.printStackTrace();
        }
    }

    /**
     * Closes the current exam window.
     *
     * @param e The ActionEvent triggered by the close action.
     */
    public void close(ActionEvent e) {
        Stage stage = (Stage)  questionVBox.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the event when the time for the exam runs out.
     * Submit the exam automatically
     *
     * @author Li Ching Ho
     */
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
