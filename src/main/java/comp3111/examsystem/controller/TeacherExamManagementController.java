package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;
import comp3111.examsystem.tools.MsgSender;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static comp3111.examsystem.entity.Course.CourseDatabase.getCourseIds;

/**
 * Controller class for UI TeacherExamManagement.fxml
 * @author Cheung Tuen King
 */
public class TeacherExamManagementController implements Initializable {
    // exam filter
    @FXML private TextField examNameSearchTxt;
    @FXML private ChoiceBox<String> examCourseIdSearchCombox;
    @FXML private ChoiceBox<String> examPublishedSearchCombox;
    @FXML private Button examResetBtn;
    @FXML private Button examFilterBtn;
    private String examNameFilter = null,
            examCourseIdFilter = null,
            examPublishedFilter = null;

    // question filter
    @FXML private TextField questionSearchTxt;
    @FXML private ChoiceBox<String> questionTypeSearchCombox;
    @FXML private TextField questionScoreSearchTxt;
    @FXML private Button questionResetBtn;
    @FXML private Button questionFilterBtn;
    private String questionFilter = null,
            questionScoreFilter = null,
            questionTypeFilter = null;

    // exam table
    @FXML private TableView<Exam> examTable;
    @FXML private TableColumn<Exam, String> examNameCol;
    @FXML private TableColumn<Exam, String> examCourseIdCol;
    @FXML private TableColumn<Exam, Integer> examTimeCol;
    @FXML private TableColumn<Exam, String> examPublishedCol;

    // question in exam table
    @FXML private TableView<Question> examQuestionTable;
    @FXML private TableColumn<Question, String> examQuestionCol;
    @FXML private TableColumn<Question, String> examQuestionTypeCol;
    @FXML private TableColumn<Question, Integer> examQuestionScoreCol;
    private List<Question> selectedQuestion;

    // exam table
    @FXML private TableView<Question> questionTable;
    @FXML private TableColumn<Question, String> questionCol;
    @FXML private TableColumn<Question, String> questionTypeCol;
    @FXML private TableColumn<Question, Integer> questionScoreCol;
    @FXML private Button deleteQuestionBtn;
    @FXML private Button addQuestionBtn;
    @FXML private Button deleteAllQuestionBtn;
    @FXML private Button addAllQuestionBtn;

    // exam form
    @FXML private TextField examNameTxt;
    @FXML private TextField examTimeTxt;
    @FXML private ChoiceBox<String> examCourseIdCombox;
    @FXML private ChoiceBox<String> examPublishedCombox;

    @FXML private Button refreshBtn;
    @FXML private Button deleteBtn;
    @FXML private Button updateBtn;
    @FXML private Button addBtn;

    /**
     * Refresh the exam table by reloading the exam data from exam database.
     * After refreshing, all selected exam in exam table is deselected
     * This method is called when "Refresh" button in exam table filter is clicked
     * @author Chueng Tuen King
     */
    public void refresh() {
        refreshExamTable();
    }

    /**
     * Delete the selected exam in exam table from database.
     * After successful deletion, a notification dialog is popped up and the exam table is refreshed.
     * If any error occurred, the delete operation is terminated with error message prompted in dialog.
     * This method is called when "Delete" button in exam table is clicked
     * @author Chueng Tuen King
     */
    public void delete() {
        try {
            Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
            List<Submission> list = SubmissionDatabase.getInstance().queryByField("examId", selectedExam.getId().toString());



            ExamDatabase.getInstance().deleteExam(selectedExam);
            MsgSender.showMsg("Successful Deletion", "Exam deleted successfully.", this::refreshExamTable);
        } catch (Exception e) {
            MsgSender.showConfirm("Exam Deletion Error", e.getMessage(), () -> {});
        }

    }

    /**
     * Update the selected exam in exam table.
     * After successful updating, a notification dialog is popped up and the exam table is refreshed.
     * If any error occurred, the update operation is terminated with error message prompted in dialog.
     * This method is called when "Update" button in exam input form is clicked
     * @author Chueng Tuen King
     */
    public void update() {
        try {
            Exam exam = examTable.getSelectionModel().getSelectedItem();
            exam.setCourseId(examCourseIdCombox.getSelectionModel().getSelectedItem());
            exam.setName(examNameTxt.getText());
            exam.setTime(examTimeTxt.getText());
            exam.setPublished("yes".equals(examPublishedCombox.getSelectionModel().getSelectedItem()));
            exam.setQuestionIds(selectedQuestion.stream().map(Entity::getId).toList());
            ExamDatabase.getInstance().updateExam(exam);
            MsgSender.showMsg("Successful Exam Update", "Exam updated successfully.", () -> {
                refreshExamTable();
                resetExamForm();
            });
        } catch (Exception e) {
            MsgSender.showConfirm("Exam Update Error", e.getMessage(), () -> {});
        }
    }

    /**
     * Create an exam in exam table and save to the database.
     * After successful creation, a notification dialog is popped up and the exam table is refreshed.
     * If any error occurred, the creation operation is terminated with error message prompted in dialog.
     * This method is called when "Add" button in exam input form is clicked
     * @author Chueng Tuen King
     */
    public void add() {
        try {
            Exam exam = new Exam();
            exam.setCourseId(examCourseIdCombox.getSelectionModel().getSelectedItem());
            exam.setName(examNameTxt.getText());
            exam.setTime(examTimeTxt.getText());
            exam.setPublished("yes".equals(examPublishedCombox.getSelectionModel().getSelectedItem()));
            exam.setQuestionIds(selectedQuestion.stream().map(Entity::getId).toList());
            ExamDatabase.getInstance().addExam(exam);
            MsgSender.showMsg("Successful Exam Creation", "Exam added successfully.", () -> {
                refreshExamTable();
                resetExamForm();
            });
        } catch (Exception e) {
            MsgSender.showConfirm("Exam Creation Error", e.getMessage(), () -> {});
            //e.printStackTrace();
        }
    }

    /**
     * Reset the exam table filter.
     * All text fields in exam table filter is set to empty.
     * All combo boxes in exam table filter is set to "all".
     * This method is called when "Reset" button in exam table filter is clicked
     * @author Chueng Tuen King
     */
    public void examReset() {
        examPublishedSearchCombox.getSelectionModel().selectFirst();
        examCourseIdSearchCombox.getSelectionModel().selectFirst();
        examNameSearchTxt.setText("");
    }

    /**
     * Filter the exam table based on the text fields and combo boxes input in exam table filter.
     * Empty text field or Combo box set to "all" would not be used in filtering.
     * After filtering, all exam in exam table is deselected and the exam input form is cleared.
     * This method is called when "Filter" button in exam table filter is clicked
     * @author Chueng Tuen King
     */
    public void examFilter() {
        examPublishedFilter = examPublishedSearchCombox.getSelectionModel().isSelected(0) ? null :
                examPublishedSearchCombox.getSelectionModel().getSelectedItem();
        examNameFilter = examNameSearchTxt.getText().trim();
        examCourseIdFilter = examCourseIdSearchCombox.getSelectionModel().isSelected(0) ? null :
                examCourseIdSearchCombox.getSelectionModel().getSelectedItem();
        refreshExamTable();
    }

    /**
     * Reset the question table filter.
     * All text fields in question table filter is set to empty.
     * All combo boxes in question table filter is set to "all".
     * This method is called when "Reset" button in question table filter is clicked
     * @author Chueng Tuen King
     */
    public void questionReset() {
        questionTypeSearchCombox.getSelectionModel().selectFirst();
        questionScoreSearchTxt.setText("");
        questionSearchTxt.setText("");
    }

    /**
     * Filter the question table based on the text fields and combo boxes input in question table filter.
     * Empty text field or Combo box set to "all" would not be used in filtering.
     * If score text field in question table filter is not an integer, an error message dialog is prompted and filtering is terminated.
     * After filtering, all question in question table is deselected.
     * This method is called when "Filter" button in question table filter is clicked
     * @author Chueng Tuen King
     */
    public void questionFilter() {
        try {
            List<Question> questions = QuestionDatabase.getInstance().filter(
                    questionSearchTxt.getText(),
                    questionTypeSearchCombox.getSelectionModel().isSelected(0) ? null :
                            questionTypeSearchCombox.getSelectionModel().getSelectedItem(),
                    questionScoreSearchTxt.getText());
            questionTable.getItems().setAll(questions);
        } catch (Exception e) {
            MsgSender.showConfirm("Question Filter Error", e.getMessage(), () -> {});
            return;
        }

        questionFilter = questionSearchTxt.getText();
        questionTypeFilter = questionTypeSearchCombox.getSelectionModel().getSelectedItem();
        questionScoreFilter = questionScoreSearchTxt.getText();
        refreshQuestionTable();
    }

    /**
     * Delete the selected question from the table showing all question in the exam.
     * After successful deletion, the question table and the table showing all question in the exam are refreshed and all question in such table is deselected.
     * The deleted question is reappeared in the question table.
     * If any error occurred, the operation is terminated with error message dialog prompted.
     * This method is called when "Delete from Left" button is clicked
     * @author Chueng Tuen King
     */
    public void deleteQuestion() {
        selectedQuestion.removeAll(examQuestionTable.getSelectionModel().getSelectedItems());
        refreshQuestionTable();
    }

    /**
     * Add the selected question from the question table to the table showing all question in the exam.
     * After successful adding, the question table and the table showing all question in the exam are refreshed and all question in such table is deselected.
     * The added question is reappeared in the table showing all question in the exam.
     * If any error occurred, the operation is terminated with error message dialog prompted.
     * This method is called when "Add to Left" button is clicked
     * @author Chueng Tuen King
     */
    public void addQuestion() {
        selectedQuestion.addAll(questionTable.getSelectionModel().getSelectedItems());
        // System.out.println(selectedQuestion.toString());
        refreshQuestionTable();
    }

    /**
     * Initialize the Exam management UI for teacher
     * @author Cheung Tuen King
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initExamTable();
        initExamQuestionTable();
        initQuestionTable();
        initExamForm();
        initExamFilter();
        initQuestionFilter();

        resetExamForm();
        refreshExamTable();
        refreshQuestionTable();
    }

    /**
     * Set up the exam table, including:
     * <li>Set the value of each columns to link with corresponding attribute from Exam entity</li>
     * <li>Fill in the exam form automatically based on the row (exam entity) selected</li>
     * <li>Enable or disable the "Delete" and "Update" button automatically by checking if any row selected</li>
     * @author Cheung Tuen King
     */
    private void initExamTable() {

        examNameCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getName()));
        examCourseIdCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getCourseId()));
        examTimeCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getTime()));
        examPublishedCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getPublished()));
        examTable.setEditable(false);
        examTable.setPlaceholder(new Label("No exam record."));
        examTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        examTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Exam>) change -> {
            if (change.getList().isEmpty()) {
                updateBtn.setDisable(true);
                deleteBtn.setDisable(true);
                resetExamForm();
                return;
            }
            Exam selectedExam = change.getList().getFirst();
            examTimeTxt.setText(selectedExam.getTime() + "");
            examNameTxt.setText(selectedExam.getName());
            examCourseIdCombox.getSelectionModel().select(selectedExam.getCourseId());
            examPublishedCombox.getSelectionModel().select(selectedExam.getPublished());
            selectedQuestion = QuestionDatabase.getInstance().queryByKeys(
                    selectedExam.getQuestionIds().stream().map(Object::toString).toList());
            refreshQuestionTable();

            updateBtn.setDisable(false);
            deleteBtn.setDisable(false);
        });
    }

    /**
     * Set up the table showing all question in an exam, including:
     * <li>Set the value of each columns to link with corresponding attribute from Question entity</li>
     * <li>Enable or disable the "Delete from Left" button automatically by checking if any row selected</li>
     * @author Cheung Tuen King
     */
    private void initExamQuestionTable() {
        examQuestionCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getQuestion()));
        examQuestionScoreCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getScore()));
        examQuestionTypeCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getType().getName()));
        examQuestionTable.setEditable(false);
        examQuestionTable.setPlaceholder(new Label("No question selected."));
        examQuestionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectedQuestion = new ArrayList<>();

        // handle record on select
        examQuestionTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Question>) change -> {
            deleteQuestionBtn.setDisable(change.getList().isEmpty());
        });
    }

    /**
     * Set up the question table, including:
     * <li>Set the value of each columns to link with corresponding attribute from Question entity</li>
     * <li>Enable or disable the "Add to Left" button automatically by checking if any row selected</li>
     * @author Cheung Tuen King
     */
    private void initQuestionTable() {
        questionCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getQuestion()));
        questionScoreCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getScore()));
        questionTypeCol.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getType().getName()));
        questionTable.setEditable(false);
        questionTable.setPlaceholder(new Label("No question record."));
        questionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // handle record on select
        questionTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Question>) change -> {
            addQuestionBtn.setDisable(change.getList().isEmpty());
        });
    }

    /**
     * Refresh and filter the exam table based on the field in exam filter
     * Empty text field or Combo box set to "all" would not be used in filtering.
     * After refreshing, all selected exam is deselected and exam input form is reset.
     * @author Cheung Tuen King
     */
    private void refreshExamTable() {
        List<Exam> examList = ExamDatabase.getInstance().filter(examNameFilter, examCourseIdFilter,
                examPublishedFilter == null ? null : examPublishedFilter.equals("yes") ? "true" : "false"
        );
        // System.out.println(examList.toString());
        examTable.getItems().setAll(FXCollections.observableList(examList));
        clearSelectedExam();
    }

    /**
     * Refresh and filter the question table based on the field in question filter.
     * Eliminate those question being selected from the exam.
     * Empty text field or Combo box set to "all" would not be used in filtering.
     * If score text field in question table filter is not an integer, an error message dialog is prompted and filtering is terminated.
     * After refreshing, all selected question is deselected.
     * @author Cheung Tuen King
     */
    private void refreshQuestionTable() {
        try {
            List<Question> questionList = QuestionDatabase.getInstance().filter(questionFilter, questionTypeFilter, questionScoreFilter);
            if (selectedQuestion != null && !selectedQuestion.isEmpty()) {
                questionList.removeAll(selectedQuestion);
            }
            questionTable.getItems().setAll(FXCollections.observableList(questionList));
            addAllQuestionBtn.setDisable(questionList.isEmpty());
        } catch (Exception e) {
            MsgSender.showMsg(e.getMessage());
        }
        refreshExamQuestionTable();
        clearSelectedQuestion();
    }

    /**
     * Refresh the table showing all question in the exam
     * After refreshing, all selected question is deselected.
     * @author Cheung Tuen King
     */
    private void refreshExamQuestionTable() {
        // System.out.println(selectedQuestion);
        examQuestionTable.setItems(FXCollections.observableList(selectedQuestion));
        deleteAllQuestionBtn.setDisable(selectedQuestion.isEmpty());
        clearSelectedExamQuestion();
    }

    /**
     * Initialize the exam input form by setting up the combo boxes selections
     * @author Cheung Tuen King
     */
    private void initExamForm() {
        examPublishedCombox.setItems(FXCollections.observableList(List.of("yes", "no")));
        examCourseIdCombox.setItems(FXCollections.observableList(getCourseIds()));
    }

    /**
     * Initialize the exam table filter form by setting up the combo boxes selections
     * @author Cheung Tuen King
     */
    private void initExamFilter() {
        examPublishedSearchCombox.setItems(FXCollections.observableList(List.of("all", "yes", "no")));
        ArrayList<String> courseIds = getCourseIds();
        courseIds.addFirst("all");
        examCourseIdSearchCombox.setItems(FXCollections.observableList(courseIds));
        examReset();
    }

    /**
     * Initialize the question filter form by setting up the combo boxes selections
     * @author Cheung Tuen King
     */
    private void initQuestionFilter() {
        ArrayList<String> typeList = new ArrayList<>(Arrays.stream(QuestionType.values()).map(QuestionType::getName).toList());
        typeList.addFirst("all");
        questionTypeSearchCombox.setItems(FXCollections.observableList(typeList));
        questionReset();
    }

    /**
     * Reset the exam input form by setting all text field to empty,
     * combo boxes to "all" and clear table showing the selected question in exam
     * @author Cheung Tuen King
     */
    private void resetExamForm() {
        examNameTxt.setText("");
        examTimeTxt.setText("");
        examPublishedCombox.getSelectionModel().selectFirst();
        examCourseIdCombox.getSelectionModel().selectFirst();
        selectedQuestion.clear();
        examQuestionTable.setItems(FXCollections.observableList(selectedQuestion));
        refreshQuestionTable();
    }


    /**
     * Clear all selection in exam table.
     * Disable "Update" and "Delete" button in exam table
     * @author Cheung Tuen King
     */
    private void clearSelectedExam() {
        examTable.getSelectionModel().clearSelection();
        updateBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }

    private void clearSelectedExamQuestion() {
        examQuestionTable.getSelectionModel().clearSelection();
        deleteQuestionBtn.setDisable(true);
    }

    private void clearSelectedQuestion() {
        questionTable.getSelectionModel().clearSelection();
        addQuestionBtn.setDisable(true);
    }

    public void addAllQuestion(ActionEvent actionEvent) {
        selectedQuestion.addAll(questionTable.getItems());
        refreshQuestionTable();
    }

    public void deleteAllQuestion(ActionEvent actionEvent) {
        selectedQuestion.clear();
        refreshQuestionTable();
    }
}
