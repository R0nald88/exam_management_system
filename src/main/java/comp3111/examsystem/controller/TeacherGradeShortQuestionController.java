package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.tools.MsgSender;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.w3c.dom.Text;

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
    private TableView<String[]> questionTable;
    @FXML
    private TableColumn<String[], String> questionColumn;
    @FXML
    private TableColumn<String[], String> answerColumn;
    @FXML
    private TableColumn<String[], String> maxScoreColumn;

    @FXML
    private ChoiceBox<String> examCombox;
    @FXML
    private Label sqScorePrompt;
    @FXML
    private TextField sqScoreTxt;

    @FXML
    private Button gradeBtn;
    @FXML
    private Button submitBtn;

    private List<Submission> submissions = new ArrayList<>();

    private Submission selectedSubmission;

    private List<Exam> exams = ExamDatabase.getInstance().getAll();

    private Dictionary<String, String> examNameToIdDict = new Hashtable<>();

    public void initialize(URL url, ResourceBundle resourceBundle){
        submissions = SubmissionDatabase.getInstance().getAll();
        ObservableList<Submission> submissionRecords = FXCollections.observableArrayList();
        for(Submission submission : submissions){
            if(!submission.getSqQuestionList().isEmpty() && !submission.isGraded()){
                submissionRecords.add(submission);
            }
        }
        if(submissionRecords.isEmpty())
            return;

        gradeTable.setItems(submissionRecords);

        studentColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getStudentUsername()).asString());
        courseColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getCourseId()));
        examColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getExamName()));

        questionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        answerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        maxScoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));

        examCombox.getItems().add("All");
        for(Exam exam : exams){
            String courseID = CourseDatabase.getInstance().queryByField("courseID", exam.getCourseId()).getFirst().getCourseID();
            String examName = exam.getName();
            String examOption = courseID + "-" + examName;
            examNameToIdDict.put(examOption, exam.getId().toString());
            examCombox.getItems().add(examOption);
        }
        examCombox.getSelectionModel().selectFirst();

        gradeTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Submission>) change -> {
            if (!change.getList().isEmpty()) {
                gradeBtn.setDisable(false);
            }
        });
        gradeBtn.setDisable(true);
        submitBtn.setDisable(true);
    }

    @FXML
    public void filter(){
        try{
            List<Submission> examFilterSubmissions = new ArrayList<>();
            if(examCombox.getValue().equals("All"))
                examFilterSubmissions = SubmissionDatabase.getInstance().getAll();
            else
                examFilterSubmissions = SubmissionDatabase.getInstance().queryByField("examId", examNameToIdDict.get(examCombox.getValue()));

            ObservableList<Submission> submissionRecords = FXCollections.observableArrayList();
            for(Submission submission : examFilterSubmissions){
                if(!submission.getSqQuestionList().isEmpty() && !submission.isGraded()){
                    submissionRecords.add(submission);
                }
            }
            gradeTable.getItems().setAll(submissionRecords);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    @FXML
    public void grade(){
        try{
            selectedSubmission = gradeTable.getSelectionModel().getSelectedItem();
            ObservableList<String[]> tableRows = FXCollections.observableArrayList();
            List<String> questions = new ArrayList<>();
            List<Integer> maxScore = new ArrayList<>();
            int total = 0;
            for (Question q : selectedSubmission.getSqQuestionList()) {
                questions.add(q.getQuestion());
                maxScore.add(q.getScore());
                total += q.getScore();
            }
            List<String> answers = selectedSubmission.getSqAnswerList();
            for(int i=0; i<questions.size(); i++){
                String question = questions.get(i);
                String answer = answers.get(i);
                String score = maxScore.get(i).toString();
                String[] data = {question, answer, score};
                tableRows.add(data);
            }
            questionTable.setItems(tableRows);
            sqScorePrompt.setText("Score(0~"+total+"):");
            submitBtn.setDisable(false);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    @FXML
    public void submit(){
        try{
            if(sqScoreTxt.getText().isEmpty())
                throw new Exception("Please enter a score.");
            selectedSubmission.updateSqScore(Integer.parseInt(sqScoreTxt.getText()));
            SubmissionDatabase.getInstance().updateSubmission(selectedSubmission);
            MsgSender.showConfirm("Successful", "The submission has been graded.", this::refresh);
        } catch (Exception e) {
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    @FXML
    public void refresh(){
        questionTable.setItems(FXCollections.observableArrayList());
        submitBtn.setDisable(true);
        gradeTable.getSelectionModel().clearSelection();
        gradeBtn.setDisable(true);
        sqScorePrompt.setText("Score:");
        sqScoreTxt.setText("");
        filter();
    }
}
