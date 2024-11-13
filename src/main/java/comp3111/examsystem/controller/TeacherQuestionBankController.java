package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TeacherQuestionBankController {
    // buttons
    @FXML private Button refreshBtn;
    @FXML private Button deleteBtn;
    @FXML private Button updateBtn;
    @FXML private Button addBtn;

    // input form
    @FXML private TextField questionFormTxt;
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
    @FXML private ChoiceBox<QuestionType> typeCombox;

    // table
    @FXML private TableView<Question> questionTable;
    @FXML private TableColumn<Question, String> questionCol;
    @FXML private TableColumn<Question, String> optionACol;
    @FXML private TableColumn<Question, String> optionBCol;
    @FXML private TableColumn<Question, String> optionCCol;
    @FXML private TableColumn<Question, String> optionDCol;
    @FXML private TableColumn<Question, String> answerCol;
    @FXML private TableColumn<Question, QuestionType> typeCol;
    @FXML private TableColumn<Question, Integer> scoreCol;

    // filter
    @FXML private Button resetBtn;
    @FXML private Button filterBtn;
    @FXML private TextField scoreSearchTxt;
    @FXML private ChoiceBox<QuestionType> typeSearchCombox;
    @FXML private TextField questionSearchTxt;

    public void reset(ActionEvent actionEvent) {

    }

    public void filter(ActionEvent actionEvent) {

    }

    public void refresh(ActionEvent actionEvent) {
    }

    public void delete(ActionEvent actionEvent) {
    }

    public void update(ActionEvent actionEvent) {
    }

    public void add(ActionEvent actionEvent) {
    }
}
