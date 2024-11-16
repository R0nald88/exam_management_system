package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Entity;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;
import comp3111.examsystem.tools.MsgSender;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    // exam form
    @FXML private TextField examNameTxt;
    @FXML private TextField examTimeTxt;
    @FXML private ChoiceBox<String> examCourseIdCombox;
    @FXML private ChoiceBox<String> examPublishedCombox;


    @FXML private Button refreshBtn;
    @FXML private Button deleteBtn;
    @FXML private Button updateBtn;
    @FXML private Button addBtn;

    public void refresh(ActionEvent actionEvent) {
        refreshExamTable();
    }

    public void delete(ActionEvent actionEvent) {
        try {
            ExamDatabase.getInstance().deleteExam(examTable.getSelectionModel().getSelectedItem());
            MsgSender.showConfirm("Successful Deletion", "Exam deleted successfully.", this::refreshExamTable);
        } catch (Exception e) {
            MsgSender.showConfirm("Exam Deletion Error", e.getMessage(), () -> {});
        }

    }

    public void update(ActionEvent actionEvent) {
        try {
            Exam exam = examTable.getSelectionModel().getSelectedItem();
            exam.setCourseId(examCourseIdCombox.getSelectionModel().getSelectedItem());
            exam.setName(examNameTxt.getText());
            exam.setTime(examTimeTxt.getText());
            exam.setPublished("yes".equals(examPublishedCombox.getSelectionModel().getSelectedItem()));
            exam.setQuestionIds(selectedQuestion.stream().map(Entity::getId).toList());
            ExamDatabase.getInstance().updateExam(exam);
            MsgSender.showConfirm("Successful Exam Update", "Exam updated successfully.", () -> {
                refreshExamTable();
                resetExamForm();
            });
        } catch (Exception e) {
            MsgSender.showConfirm("Exam Update Error", e.getMessage(), () -> {});
        }
    }

    public void add(ActionEvent actionEvent) {
        try {
            Exam exam = new Exam();
            exam.setCourseId(examCourseIdCombox.getSelectionModel().getSelectedItem());
            exam.setName(examNameTxt.getText());
            exam.setTime(examTimeTxt.getText());
            exam.setPublished("yes".equals(examPublishedCombox.getSelectionModel().getSelectedItem()));
            exam.setQuestionIds(selectedQuestion.stream().map(Entity::getId).toList());
            ExamDatabase.getInstance().addExam(exam);
            MsgSender.showConfirm("Successful Exam Creation", "Exam added successfully.", () -> {
                refreshExamTable();
                resetExamForm();
            });
        } catch (Exception e) {
            MsgSender.showConfirm("Exam Creation Error", e.getMessage(), () -> {});
        }
    }

    public void examReset() {
        examPublishedSearchCombox.getSelectionModel().selectFirst();
        examCourseIdSearchCombox.getSelectionModel().selectFirst();
        examNameSearchTxt.setText("");
    }

    public void examFilter(ActionEvent actionEvent) {
        examPublishedFilter = examPublishedSearchCombox.getSelectionModel().isSelected(0) ? null :
                examPublishedSearchCombox.getSelectionModel().getSelectedItem();
        examNameFilter = examNameSearchTxt.getText().trim();
        examCourseIdFilter = examCourseIdSearchCombox.getSelectionModel().isSelected(0) ? null :
                examCourseIdSearchCombox.getSelectionModel().getSelectedItem();
        refreshExamTable();
    }

    public void questionReset() {
        questionTypeSearchCombox.getSelectionModel().selectFirst();
        questionScoreSearchTxt.setText("");
        questionSearchTxt.setText("");
    }

    public void questionFilter(ActionEvent actionEvent) {
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

    public void deleteQuestion(ActionEvent actionEvent) {
        selectedQuestion.removeAll(examQuestionTable.getSelectionModel().getSelectedItems());
        refreshQuestionTable();
    }

    public void addQuestion(ActionEvent actionEvent) {
        selectedQuestion.addAll(questionTable.getSelectionModel().getSelectedItems());
        System.out.println(selectedQuestion.toString());
        refreshQuestionTable();
    }

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

    private void refreshExamTable() {
        List<Exam> examList = ExamDatabase.getInstance().filter(examNameFilter, examCourseIdFilter, examPublishedFilter);
        // System.out.println(examList.toString());
        examTable.getItems().setAll(FXCollections.observableList(examList));
        clearSelectedExam();
    }

    private void refreshQuestionTable() {
        try {
            List<Question> questionList = QuestionDatabase.getInstance().filter(questionFilter, questionTypeFilter, questionScoreFilter);
            if (selectedQuestion != null && !selectedQuestion.isEmpty()) {
                questionList.removeAll(selectedQuestion);
            }
            questionTable.getItems().setAll(FXCollections.observableList(questionList));
        } catch (Exception e) {
            MsgSender.showMsg(e.getMessage());
        }
        refreshExamQuestionTable();
        clearSelectedQuestion();
    }

    private void refreshExamQuestionTable() {
        System.out.println(selectedQuestion);
        examQuestionTable.setItems(FXCollections.observableList(selectedQuestion));
        clearSelectedExamQuestion();
    }

    private void initExamForm() {
        examPublishedCombox.setItems(FXCollections.observableList(List.of("yes", "no")));
        examCourseIdCombox.setItems(FXCollections.observableList(getCourseIds()));
    }

    private void initExamFilter() {
        examPublishedSearchCombox.setItems(FXCollections.observableList(List.of("all", "yes", "no")));
        ArrayList<String> courseIds = getCourseIds();
        courseIds.addFirst("all");
        examCourseIdSearchCombox.setItems(FXCollections.observableList(courseIds));
        examReset();
    }

    private void initQuestionFilter() {
        ArrayList<String> typeList = new ArrayList<>(Arrays.stream(QuestionType.values()).map(QuestionType::getName).toList());
        typeList.addFirst("all");
        questionTypeSearchCombox.setItems(FXCollections.observableList(typeList));
        questionReset();
    }

    private void resetExamForm() {
        examNameTxt.setText("");
        examTimeTxt.setText("");
        examPublishedCombox.getSelectionModel().selectFirst();
        examCourseIdCombox.getSelectionModel().selectFirst();
        selectedQuestion.clear();
        examQuestionTable.setItems(FXCollections.observableList(selectedQuestion));
        refreshQuestionTable();
    }

    private static ArrayList<String> getCourseIds() {
        // TODO: set up course Id combox
        return new ArrayList<>(List.of("COMP2031", "COMP2130", "COMP3111", "COMP5111", "COMP1010"));
    }

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
}
