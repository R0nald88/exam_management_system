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

    public void reset() {
        questionSearchTxt.setText("");
        typeSearchCombox.getSelectionModel().selectFirst();
        scoreSearchTxt.setText("");
    }

    public void filter(ActionEvent actionEvent) {
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

    public void refresh(ActionEvent actionEvent) {
        refreshQuestionTable();
    }

    public void delete(ActionEvent actionEvent) {
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

    private void deleteQuestion(boolean deleteExam) {
        try {
            QuestionDatabase.getInstance().deleteQuestion(questionTable.getSelectionModel().getSelectedItem(), deleteExam);
            MsgSender.showConfirm("Successful Question Deletion", "Question deleted successfully.", this::refreshQuestionTable);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            MsgSender.showConfirm("Question Deletion Error", e.getMessage(), () -> {});
        }
    }

    public void update(ActionEvent actionEvent) {
        try {
            Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
            selectedQuestion.setQuestion(questionFormTxt.getText());
            selectedQuestion.setType(QuestionType.toType(typeCombox.getSelectionModel().getSelectedItem()));
            selectedQuestion.getTypeFactory().initialize(new TextField[]{optionATxt, optionBTxt, optionCTxt, optionDTxt}, selectedQuestion);
            selectedQuestion.setScore(scoreTxt.getText());
            selectedQuestion.setAnswer(answerTxt.getText());
            QuestionDatabase.getInstance().updateQuestion(selectedQuestion);
            MsgSender.showConfirm("Successful Question Update", "Question updated successfully.", this::refreshQuestionTable);
        } catch (Exception e) {
            MsgSender.showConfirm("Question Update Error", e.getMessage(), () -> {});
            e.printStackTrace();
        }
    }

    public void add(ActionEvent actionEvent) {
        try {
            Question question = new Question();
            question.setQuestion(questionFormTxt.getText());
            question.setType(QuestionType.toType(typeCombox.getSelectionModel().getSelectedItem()));
            question.getTypeFactory().initialize(new TextField[]{optionATxt, optionBTxt, optionCTxt, optionDTxt}, question);
            question.setScore(scoreTxt.getText());
            question.setAnswer(answerTxt.getText());
            QuestionDatabase.getInstance().addQuestion(question);
            MsgSender.showConfirm("Successful Question Creation", "Question created successfully.", this::refreshQuestionTable);
        } catch (Exception e) {
            MsgSender.showConfirm("Question Creation Error", e.getMessage(), () -> {});
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initQuestionTable();
        initQuestionFilter();
        initQuestionForm();
    }

    private String markEmptyCell(String s) {
        if (s == null || s.isEmpty()) {
            return "---";
        }
        return s;
    }

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
            if (change.getList().isEmpty()) {
                updateBtn.setDisable(true);
                deleteBtn.setDisable(true);
                resetQuestionForm();
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

    private void initQuestionFilter() {
        ArrayList<String> typeList = new ArrayList<>(Arrays.stream(QuestionType.values()).map(QuestionType::getName).toList());
        typeList.addFirst("all");
        typeSearchCombox.setItems(FXCollections.observableList(typeList));
        reset();
    }

    private void initQuestionForm() {
        typeCombox.setItems(FXCollections.observableList(Arrays.stream(QuestionType.values()).map(QuestionType::getName).toList()));
        typeCombox.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            saveOptions(s);
            setUpFormByType(t1);
        });

        resetQuestionForm();
    }

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

    private void refreshQuestionTable() {
        try {
            List<Question> questions = QuestionDatabase.getInstance().filter(questionFilter, typeFilter, scoreFilter);
            questionTable.getItems().setAll(questions);
        } catch (Exception e) {
            MsgSender.showConfirm("Filter Error", e.getMessage(), () -> {});
        }

        clearSelectedQuestion();
    }

    private void setUpFormByType(String type) {
        QuestionTypeFactory.getInstance(
                QuestionType.toType(type)
        ).setUpForm(
                new Label[]{optionALabel, optionBLabel, optionCLabel, optionDLabel},
                new TextField[]{optionATxt, optionBTxt, optionCTxt, optionDTxt},
                options, questionFormLabel, questionFormTxt
        );
    }

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

    private void clearSelectedQuestion() {
        questionTable.getSelectionModel().clearSelection();
        deleteBtn.setDisable(true);
        updateBtn.setDisable(true);
    }
}
