package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionType;
import comp3111.examsystem.tools.MsgSender;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.event.MouseEvent;
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
    private TableColumn<String[], String> studentAnswerColumn;
    @FXML
    private TableColumn<String[], String> studentScoreColumn;
    @FXML
    private TableColumn<String[], String> maxScoreColumn;

    @FXML
    private ChoiceBox<String> examCombox;
    @FXML
    private TextField sqScoreTxt;

    private List<Submission> submissions = new ArrayList<>();

    private Submission selectedSubmission;

    private List<Exam> exams = ExamDatabase.getInstance().getAll();

    private Question editingQuestionObject;

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
        studentAnswerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        studentScoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        maxScoreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));

        examCombox.getItems().add("All");
        for(Exam exam : exams){
            String courseID = CourseDatabase.getInstance().queryByField("courseID", exam.getCourseId()).getFirst().getCourseID();
            String examName = exam.getName();
            String examOption = courseID + "-" + examName;
            examNameToIdDict.put(examOption, exam.getId().toString());
            examCombox.getItems().add(examOption);
        }
        examCombox.getSelectionModel().selectFirst();
    }

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

    public void grade(){
        try{
            questionTable.getItems().clear();
            selectedSubmission = gradeTable.getSelectionModel().getSelectedItem();
            ObservableList<String[]> tableRows = FXCollections.observableArrayList();
            //List<String> questions = selectedSubmission.getSqQuestionList();
            //List<String> answers = selectedSubmission.getSqAnswerList();
            //List<Integer> maxScore = selectedSubmission.getSqFullScoreList();
            for(int i=0; i<selectedSubmission.getSqQuestionList().size(); i++){
                if (!selectedSubmission.getSqGradedBooleanList().get(i)) {
                    String question = selectedSubmission.getSqQuestionList().get(i).getQuestion();
                    String answer = selectedSubmission.getSqQuestionList().get(i).getAnswer();
                    String studentAnswer = selectedSubmission.getSqAnswerList().get(i);
                    String score = Integer.toString(selectedSubmission.getSqScoreList().get(i));
                    String fullScore = Integer.toString(selectedSubmission.getSqQuestionList().get(i).getScore());
                    String[] data = {question, answer, studentAnswer, score, fullScore};
                    tableRows.add(data);
                }
            }
            questionTable.setItems(tableRows);
            questionTable.setRowFactory(tv -> {
                TableRow<String[]> tableRow = new TableRow<>() {};

                tableRow.setOnMouseClicked(event -> {
                    if (!tableRow.isEmpty()) {
                        String scoreString = selectedSubmission.getScoreList().get(selectedSubmission.getQuestionNumberByQuestionId(selectedSubmission.getSqQuestionList().get(tableRow.getIndex()).getId())).toString();
                        editingQuestionObject = selectedSubmission.getQuestionObjectList().get(selectedSubmission.getQuestionNumberByQuestionId(selectedSubmission.getSqQuestionList().get(tableRow.getIndex()).getId()));
                        sqScoreTxt.setText(scoreString);
                    }
                });
                return tableRow;
            });
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public void submit(){
        try{
            if (sqScoreTxt.getText() == null || Objects.equals(sqScoreTxt.getText(), "")) throw new Exception("You haven't inputted any value in Score.");
            selectedSubmission.updateScore(selectedSubmission.getQuestionNumberByQuestionId(editingQuestionObject.getId()), Integer.parseInt(sqScoreTxt.getText()));
            String submissionJson = selectedSubmission.toString(); // Ensure this is a valid JSON string
            SubmissionDatabase.getInstance().updateSubmission(selectedSubmission);
            MsgSender.showConfirm("Successful", "The question has been graded.", this::refresh);
        } catch (Exception e) {
            //MsgSender.showConfirm("Error", e.getMessage(), ()->{});
            e.printStackTrace();
        }
    }

    public void refresh(){
        questionTable.setItems(FXCollections.observableArrayList());
        sqScoreTxt.setText("");
        filter();
    }
}
