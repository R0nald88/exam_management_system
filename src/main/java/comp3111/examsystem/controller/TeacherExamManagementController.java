package comp3111.examsystem.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class TeacherExamManagementController {
    public TextField examNameSearchTxt;
    public ChoiceBox<String> examCourseIdSearchCombox;
    public ChoiceBox<String> examPublishedSearchCombox;
    public Button examResetBtn;
    public Button examFilterBtn;


    public TextField questionSearchTxt;
    public ChoiceBox questionTypeSearchCombox;
    public TextField questionScoreSearchCombox;
    public Button questionResetBtn;
    public Button questionFilterBtn;


    public TableView examTable;
    public TableColumn examNameCol;
    public TableColumn examCourseIdCol;
    public TableColumn examTimeCol;
    public TableColumn examPublishedCol;


    public TableView examQuestionTable;
    public TableColumn examQuestionCol;
    public TableColumn examQuestionTypeCol;
    public TableColumn examQuestionScoreCol;


    public TableView questionTable;
    public TableColumn questionCol;
    public TableColumn questionTypeCol;
    public TableColumn questionScoreCol;
    public Button deleteQuestionBtn;
    public Button addQuestionBtn;


    public TextField examNameTxt;
    public TextField examTimeTxt;
    public ChoiceBox examCourseIdCombox;
    public ChoiceBox examPublishedCombox;


    public Button refreshBtn;
    public Button deleteBtn;
    public Button updateBtn;
    public Button addBtn;

    public void refresh(ActionEvent actionEvent) {
    }

    public void delete(ActionEvent actionEvent) {
    }

    public void update(ActionEvent actionEvent) {
    }

    public void add(ActionEvent actionEvent) {
    }

    public void examReset(ActionEvent actionEvent) {

    }

    public void examFilter(ActionEvent actionEvent) {
    }

    public void questionReset(ActionEvent actionEvent) {
    }

    public void questionFilter(ActionEvent actionEvent) {
    }

    public void deleteQuestion(ActionEvent actionEvent) {
    }

    public void addQuestion(ActionEvent actionEvent) {
    }
}
