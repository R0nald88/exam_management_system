package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.tools.MsgSender;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.*;

public class TeacherGradeShortQuestionController implements Initializable {

    @FXML
    private TableView<Submission> gradeTable;
    @FXML
    private TableColumn<Submission, String> studentColumn;
    @FXML
    private TableColumn<Submission, String> courseColumn;
    @FXML
    private TableColumn<Submission, String> examColumn;
    @FXML
    private TableColumn<Submission, String> scoreColumn;
    @FXML
    private TableColumn<Submission, String> fullScoreColumn;
    @FXML
    private TableColumn<Submission, String> timeSpendColumn;

    @FXML
    private TableView<String[]> questionTable;
    @FXML
    private TableColumn<String[], String> questionColumn;
    @FXML
    private TableColumn<String[], String> answerColumn;

    @FXML
    private ChoiceBox<String> examCombox;

    private List<Submission> submissions = new ArrayList<>();

    private List<Exam> exams = ExamDatabase.getInstance().getAll();

    private Dictionary<String, String> examNameToIdDict = new Hashtable<>();

    public void initialize(URL url, ResourceBundle resourceBundle){
        submissions = SubmissionDatabase.getInstance().getAll();
        ObservableList<Submission> submissionRecords = FXCollections.observableArrayList();
        for(Submission submission : submissions){
            if(true){ //!submission.getSqQuestionList().isEmpty()
                submissionRecords.add(submission);
            }
        }
        if(submissionRecords.isEmpty())
            return;

        gradeTable.setItems(submissionRecords);

        studentColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getStudentUsername()).asString());
        courseColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getCourseId()));
        examColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(
                ExamDatabase.getInstance().queryByKey(tableRow.getValue().getExamId().toString()).getName()
        ));
        scoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getScore()).asString());
        fullScoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getFullScore()).asString());
        timeSpendColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getTimeSpend()).asString());

        questionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        answerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));

        examCombox.getItems().add("All");
        for(Exam exam : exams){
            String courseName = CourseDatabase.getInstance().queryByField("courseID", exam.getCourseId()).getFirst().getCourseName();
            String examName = exam.getName();
            String examOption = courseName + " " + examName;
            examNameToIdDict.put(examOption, exam.getId().toString());
            examCombox.getItems().add(examOption);
        }
    }

    public void filter(){
        try{
            List<Submission> examFilterSubmissions = new ArrayList<>();
            if(examCombox.getValue().equals("All"))
                examFilterSubmissions = SubmissionDatabase.getInstance().getAll();
            else
                examFilterSubmissions = SubmissionDatabase.getInstance().queryByField("examId", examNameToIdDict.get(examCombox.getValue()));
            gradeTable.getItems().setAll(examFilterSubmissions);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public void grade(){
        try{
            Submission selectedSubmission = gradeTable.getSelectionModel().getSelectedItem();
            ObservableList<String[]> data = FXCollections.observableArrayList();
            List<String> questions = selectedSubmission.getSqQuestionList();
            List<String> answers = selectedSubmission.getSqAnswerList();
            for(int i=0; i<questions.size(); i++){
                String question = questions.get(i);
                String answer = answers.get(i);
                String[] pair = {question, answer};
                data.add(pair);
            }
            questionTable.setItems(data);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public void correct(){}

    public void wrong(){}
}
