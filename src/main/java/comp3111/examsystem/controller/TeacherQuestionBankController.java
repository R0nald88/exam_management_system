package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;
import comp3111.examsystem.entity.Questions.QuestionTypeFactory;
import comp3111.examsystem.tools.MsgSender;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for UI TeacherQuestionBank.fxml
 * @author Cheung Tuen King
 */
public class TeacherQuestionBankController implements Initializable {
    // buttons
    @FXML private Button refreshBtn;
    @FXML private Button deleteBtn;
    @FXML private Button updateBtn;
    @FXML private Button addBtn;

    // input form
    @FXML private TextField questionFormTxt;
    @FXML private Label questionFormLabel;
    @FXML private Label optionALabel;
    @FXML private TextField optionATxt;
    @FXML private Label optionBLabel;
    @FXML private TextField optionBTxt;
    @FXML private Label optionCLabel;
    @FXML private TextField optionCTxt;
    @FXML private Label optionDLabel;
    @FXML private TextField optionDTxt;
    @FXML private TextField answerTxt;
    @FXML private TextField scoreTxt;
    @FXML private ChoiceBox<String> typeCombox;
    @FXML private VBox questionFormLayout;
    private final String[] options = new String[]{"", "", "", ""};

    // table
    @FXML private TableView<Question> questionTable;
    @FXML private TableColumn<Question, String> questionCol;
    @FXML private TableColumn<Question, String> optionACol;
    @FXML private TableColumn<Question, String> optionBCol;
    @FXML private TableColumn<Question, String> optionCCol;
    @FXML private TableColumn<Question, String> optionDCol;
    @FXML private TableColumn<Question, String> answerCol;
    @FXML private TableColumn<Question, String> typeCol;
    @FXML private TableColumn<Question, Integer> scoreCol;

    // filter
    @FXML private Button resetBtn;
    @FXML private Button filterBtn;
    @FXML private TextField scoreSearchTxt;
    @FXML private ChoiceBox<String> typeSearchCombox;
    @FXML private TextField questionSearchTxt;
    private String scoreFilter, questionFilter, typeFilter;

    /**
     * Reset the question table filter.
     * All text fields in question table filter is set to empty.
     * All combo boxes in question table filter is set to "all".
     * This method is called when "Reset" button in question table filter is clicked
     * @author Chueng Tuen King
     */
    public void reset() {
        questionSearchTxt.setText("");
        typeSearchCombox.getSelectionModel().selectFirst();
        scoreSearchTxt.setText("");
    }

    /**
     * Filter the question table based on the text fields and combo boxes input in question table filter.
     * Empty text field or Combo box set to "all" would not be used in filtering.
     * If score text field in question table filter is not an integer, an error message dialog is prompted and filtering is terminated.
     * After filtering, all question in question table is deselected and the question form is cleared.
     * This method is called when "Filter" button in question table filter is clicked
     * @author Chueng Tuen King
     */
    public void filter() {
        try {
            List<Question> questions = QuestionDatabase.getInstance().filter(
                    questionSearchTxt.getText(),
                    typeSearchCombox.getSelectionModel().isSelected(0) ? null :
                            typeSearchCombox.getSelectionModel().getSelectedItem(),
                    scoreSearchTxt.getText());
            questionTable.getItems().setAll(questions);
        } catch (Exception e) {
            MsgSender.showConfirm("Question Filter Error", e.getMessage(), () -> {});
            return;
        }

        questionFilter = questionSearchTxt.getText();
        typeFilter = typeSearchCombox.getSelectionModel().isSelected(0) ? null :
                typeSearchCombox.getSelectionModel().getSelectedItem();
        scoreFilter = scoreSearchTxt.getText();
        refreshQuestionTable();
    }

    /**
     * Refresh the question table by reloading the question data from question database.
     * After refreshing, all question in question table is deselected and the question form is cleared.
     * This method is called when "Refresh" button in exam table filter is clicked
     * @author Chueng Tuen King
     */
    public void refresh() {
        refreshQuestionTable();
    }

    /**
     * Delete the selected question from question table.
     * After successful deletion, the question table is refreshed, all question in question table is deselected and the question form is cleared.
     * If any error occurred, the operation is terminated with error message dialog prompted.
     * If any exam contains the deleting question, a warning dialog is prompted to notify affected exam.
     * If the user click "OK" in the warning dialog, all affected exam will delete the question as well while deleting those exam containing only th deleted question;
     * else, the operation terminated.
     * This method is called when "Delete" button is clicked
     * @author Chueng Tuen King
     */
    public void delete() {
        try {
            deleteQuestion(false);
        } catch (RuntimeException e) {
            MsgSender.showConfirm(
                "Question Deletion Warning",
                e.getMessage() +
                "\nPress \"OK\" to continue deleting this question and the exam(s) containing only this question.",
                () -> {
                    deleteQuestion(true);
                });
        }
    }

    /**
     * Delete the selected question from question table.
     * After successful deletion, the question table is refreshed, all question in question table is deselected and the question form is cleared.
     * If any error occurred, the operation is terminated with error message dialog prompted.
     * If any exam contains the deleting question and deleteExam is false, a warning dialog is prompted to notify affected exam;
     * else, all affected exam will delete the question as well while deleting those exam containing only th deleted question.
     * @author Chueng Tuen King
     */
    private void deleteQuestion(boolean deleteExam) {
        try {
            QuestionDatabase.getInstance().deleteQuestion(questionTable.getSelectionModel().getSelectedItem(), deleteExam);
            MsgSender.showMsg("Successful Question Deletion", "Question deleted successfully.", this::refreshQuestionTable);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            MsgSender.showConfirm("Question Deletion Error", e.getMessage(), () -> {});
        }
    }

    /**
     * Update the selected question in question table.
     * After successful updating, a notification dialog is popped up and the exam table is refreshed.
     * If any error occurred, the update operation is terminated with error message prompted in dialog.
     * This method is called when "Update" button in question input form is clicked
     * @author Chueng Tuen King
     */
    public void update() {
        try {
            Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
            selectedQuestion.setQuestion(questionFormTxt.getText());
            selectedQuestion.setType(QuestionType.toType(typeCombox.getSelectionModel().getSelectedItem()));
            selectedQuestion.getTypeFactory().initialize(new TextField[]{optionATxt, optionBTxt, optionCTxt, optionDTxt}, selectedQuestion);
            selectedQuestion.setScore(scoreTxt.getText());
            selectedQuestion.setAnswer(answerTxt.getText());
            QuestionDatabase.getInstance().updateQuestion(selectedQuestion);
            MsgSender.showMsg("Successful Question Update", "Question updated successfully.", this::refreshQuestionTable);
        } catch (Exception e) {
            MsgSender.showConfirm("Question Update Error", e.getMessage(), () -> {});
        }
    }

    /**
     * Create a question in question table and save to the database.
     * After successful creation, a notification dialog is popped up and the question table is refreshed.
     * If any error occurred, the creation operation is terminated with error message prompted in dialog.
     * This method is called when "Add" button in question input form is clicked
     * @author Chueng Tuen King
     */
    public void add() {
        try {
            Question question = new Question();
            question.setQuestion(questionFormTxt.getText());
            question.setType(QuestionType.toType(typeCombox.getSelectionModel().getSelectedItem()));
            question.getTypeFactory().initialize(new TextField[]{optionATxt, optionBTxt, optionCTxt, optionDTxt}, question);
            question.setScore(scoreTxt.getText());
            question.setAnswer(answerTxt.getText());
            QuestionDatabase.getInstance().addQuestion(question);
            MsgSender.showMsg("Successful Question Creation", "Question created successfully.", this::refreshQuestionTable);
        } catch (Exception e) {
            MsgSender.showConfirm("Question Creation Error", e.getMessage(), () -> {});
        }
    }

    /**
     * Initialize the QuestionBank UI for teacher
     * @author Cheung Tuen King
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initQuestionTable();
        initQuestionFilter();
        initQuestionForm();
    }

    /**
     * Check if the attribute is empty and return "---" if so to represent empty cell
     * @param s String for checking
     * @return Marked string
     * @author Cheung Tuen King
     */
    private String markEmptyCell(String s) {
        if (s == null || s.isEmpty()) {
            return "---";
        }
        return s;
    }

    /**
     * Set up the question table, including:
     * <li>Set the value of each columns to link with corresponding attribute from Question entity</li>
     * <li>Fill in the question form automatically based on the row (question entity) selected</li>
     * <li>Enable or disable the "Update" and "Delete" button automatically by checking if any row selected</li>
     * @author Cheung Tuen King
     */
    private void initQuestionTable() {
        questionCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getQuestion()));
        optionACol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(
                markEmptyCell(tableRow.getValue().getOptionA())
        ));
        optionBCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(
                markEmptyCell(tableRow.getValue().getOptionB())
        ));
        optionCCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(
                markEmptyCell(tableRow.getValue().getOptionC())
        ));
        optionDCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(
                markEmptyCell(tableRow.getValue().getOptionD())
        ));
        answerCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getAnswer()));
        scoreCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getScore()));
        typeCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getType().getName()));

        questionTable.setEditable(false);
        questionTable.setPlaceholder(new Label("No question record."));
        questionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        questionTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Question>) change -> {
            resetQuestionForm();
            if (change.getList().isEmpty()) {
                updateBtn.setDisable(true);
                deleteBtn.setDisable(true);
                return;
            }

            Question selectedQuestion = change.getList().getFirst();
            setUpFormByType(selectedQuestion.getType().getName());
            questionFormTxt.setText(selectedQuestion.getQuestion());
            optionATxt.setText(selectedQuestion.getOptionA());
            optionBTxt.setText(selectedQuestion.getOptionB());
            optionCTxt.setText(selectedQuestion.getOptionC());
            optionDTxt.setText(selectedQuestion.getOptionD());
            scoreTxt.setText(selectedQuestion.getScore() + "");
            typeCombox.getSelectionModel().select(selectedQuestion.getType().getName());
            answerTxt.setText(selectedQuestion.getAnswer());

            updateBtn.setDisable(false);
            deleteBtn.setDisable(false);
        });

        refreshQuestionTable();
    }

    /**
     * Initialize the question filter by setting up the combo boxes selections
     * @author Cheung Tuen King
     */
    private void initQuestionFilter() {
        ArrayList<String> typeList = new ArrayList<>(Arrays.stream(QuestionType.values()).map(QuestionType::getName).toList());
        typeList.addFirst("all");
        typeSearchCombox.setItems(FXCollections.observableList(typeList));
        reset();
    }

    /**
     * Initialize the question input form by setting up the combo boxes selections
     * When the selection changed, save the options for current type and retrieved the options saved for the type selected.
     * @author Cheung Tuen King
     */
    private void initQuestionForm() {
        typeCombox.setItems(FXCollections.observableList(Arrays.stream(QuestionType.values()).map(QuestionType::getName).toList()));
        typeCombox.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            saveOptions(s);
            setUpFormByType(t1);
        });

        resetQuestionForm();
    }

    /**
     * Reset the question input form by setting all text field to empty and
     * combo boxes to "all"
     * @author Cheung Tuen King
     */
    private void resetQuestionForm() {
        typeCombox.getSelectionModel().selectFirst();
        // setUpFormByType(QuestionType.SINGLE.getName());
        questionFormTxt.setText("");
        optionATxt.setText("");
        optionBTxt.setText("");
        optionCTxt.setText("");
        optionDTxt.setText("");
        answerTxt.setText("");
        scoreTxt.setText("");
    }

    /**
     * Refresh and filter the question table based on the field in question filter.
     * Empty text field or Combo box set to "all" would not be used in filtering.
     * If score text field in question table filter is not an integer, an error message dialog is prompted and filtering is terminated.
     * After refreshing, all selected question is deselected and question form is reset.
     * @author Cheung Tuen King
     */
    private void refreshQuestionTable() {
        try {
            List<Question> questions = QuestionDatabase.getInstance().filter(questionFilter, typeFilter, scoreFilter);
            questionTable.getItems().setAll(questions);
        } catch (Exception e) {
            MsgSender.showConfirm("Filter Error", e.getMessage(), () -> {});
        }

        clearSelectedQuestion();
    }

    /**
     * Set up the option text field in question form according to the type selected.
     * @param type Question type selected by user
     * @author Cheung Tuen King
     */
    private void setUpFormByType(String type) {
        QuestionTypeFactory.getInstance(
                QuestionType.toType(type)
        ).setUpForm(
                new Label[]{optionALabel, optionBLabel, optionCLabel, optionDLabel},
                new TextField[]{optionATxt, optionBTxt, optionCTxt, optionDTxt},
                options, questionFormLabel, questionFormTxt
        );
    }

    /**
     * Save the option that user input for the current question and retrieved when the type is reselected
     * @param type Question type selected by user
     * @author Cheung Tuen King
     */
    private void saveOptions(String type) {
        System.out.println(type);
        if (type == null) return;
        QuestionTypeFactory.getInstance(
                QuestionType.toType(type)
        ).saveOptions(
                new TextField[]{optionATxt, optionBTxt, optionCTxt, optionDTxt},
                options
        );
    }

    /**
     * Clear the selection in question table and disable the "Update" and "Delete" button.
     * @author Cheung Tuen King
     */
    private void clearSelectedQuestion() {
        questionTable.getSelectionModel().clearSelection();
        deleteBtn.setDisable(true);
        updateBtn.setDisable(true);
    }
}
