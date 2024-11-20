package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Personnel.StudentDatabase;
import comp3111.examsystem.tools.MsgSender;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class TeacherGradeStatisticController implements Initializable {
    public static class GradeExampleClass {
        public String getStudentUsername() {
            return "student";
        }
        public String getCourseNum() {
            return "comp3111";
        }
        public String getExamName() {
            return "final";
        }
        public String getScore() {
            return "100";
        }
        public String getFullScore() {
            return "100";
        }
        public String getTimeSpend() {
            return "60";
        }
    }

    @FXML
    private ChoiceBox<String> courseCombox;
    @FXML
    private ChoiceBox<String> examCombox;
    @FXML
    private ChoiceBox<String> studentCombox;
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
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis categoryAxisBar;
    @FXML
    NumberAxis numberAxisBar;
    @FXML
    LineChart<String, Number> lineChart;
    @FXML
    CategoryAxis categoryAxisLine;
    @FXML
    NumberAxis numberAxisLine;
    @FXML
    PieChart pieChart;

    private List<Course> courses = CourseDatabase.getInstance().getAll();

    private List<Exam> exams = ExamDatabase.getInstance().getAll();

    private List<Student> students = StudentDatabase.getInstance().getAll();

    private List<Submission> currentSubmissionList = new ArrayList<>();

    private Dictionary<String, String> examNameToIdDict = new Hashtable<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentSubmissionList = SubmissionDatabase.getInstance().getAll();
        ObservableList<Submission> submissionRecords = FXCollections.observableArrayList(currentSubmissionList);
        gradeTable.setItems(submissionRecords);

        studentColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getStudentUsername()).asString());
        courseColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getCourseId()));
        examColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(
                ExamDatabase.getInstance().queryByKey(tableRow.getValue().getExamId().toString()).getName()
        ));
        scoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getScore()).asString());
        fullScoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getFullScore()).asString());
        timeSpendColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getTimeSpend()).asString());

        courseCombox.getItems().add("All");
        for(Course course : courses){
            courseCombox.getItems().add(course.getCourseID());
        }

        examCombox.getItems().add("All");
        for(Exam exam : exams){
            String courseName = CourseDatabase.getInstance().queryByField("courseID", exam.getCourseId()).getFirst().getCourseName();
            String examName = exam.getName();
            String examOption = courseName + " " + examName;
            examNameToIdDict.put(examOption, exam.getId().toString());
            examCombox.getItems().add(examOption);
        }

        studentCombox.getItems().add("All");
        for(Student student : students){
            studentCombox.getItems().add(student.getUsername());
        }


        barChart.setLegendVisible(false);
        categoryAxisBar.setLabel("Course");
        numberAxisBar.setLabel("Avg. Score");
        pieChart.setLegendVisible(false);
        pieChart.setTitle("Student Scores");
        lineChart.setLegendVisible(false);
        categoryAxisLine.setLabel("Exam");
        numberAxisLine.setLabel("Avg. Score");

        refresh();
        loadChart();
    }

    @FXML
    public void refresh() {
        reset();
        filter();
    }

    private void loadChart() {
        XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();
        seriesBar.getData().clear();
        barChart.getData().clear();

        XYChart.Series<String, Number> seriesLine = new XYChart.Series<>();
        seriesLine.getData().clear();
        lineChart.getData().clear();

        pieChart.getData().clear();

        for(Course course : courses){
            int count = 0;
            long total = 0;
            for(Submission submission : currentSubmissionList){
                if(course.getCourseID().equals(submission.getCourseId())){
                    count++;
                    total += submission.getScore();
                }
            }
            long average = total / count;
            seriesBar.getData().add(new XYChart.Data<>(course.getCourseID(), average));
            seriesLine.getData().add(new XYChart.Data<>(course.getCourseID(), average));
            pieChart.getData().add(new PieChart.Data(course.getCourseID(), average));
        }
        barChart.getData().add(seriesBar);
        lineChart.getData().add(seriesLine);

    }

    @FXML
    public void reset() {
        courseCombox.getSelectionModel().selectFirst();
        examCombox.getSelectionModel().selectFirst();
        studentCombox.getSelectionModel().selectFirst();
    }

    @FXML
    public void filter() {
        try{
            List<Submission> courseFilterSubmissions = new ArrayList<>();
            List<Submission> examFilterSubmissions = new ArrayList<>();
            List<Submission> studentFilterSubmissions = new ArrayList<>();
            if(courseCombox.getValue().equals("All"))
                courseFilterSubmissions = SubmissionDatabase.getInstance().getAll();
            else
                courseFilterSubmissions = SubmissionDatabase.getInstance().queryByField("courseId", courseCombox.getValue());
            if(examCombox.getValue().equals("All"))
                examFilterSubmissions = SubmissionDatabase.getInstance().getAll();
            else
                examFilterSubmissions = SubmissionDatabase.getInstance().queryByField("examId", examNameToIdDict.get(examCombox.getValue()));
            if(studentCombox.getValue().equals("All"))
                studentFilterSubmissions = SubmissionDatabase.getInstance().getAll();
            else
                studentFilterSubmissions = SubmissionDatabase.getInstance().queryByField("studentUsername", studentCombox.getValue());
            currentSubmissionList = SubmissionDatabase.getInstance().join(SubmissionDatabase.getInstance().join(courseFilterSubmissions, examFilterSubmissions), studentFilterSubmissions);
            gradeTable.getItems().setAll(currentSubmissionList);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
        loadChart();
    }
}
