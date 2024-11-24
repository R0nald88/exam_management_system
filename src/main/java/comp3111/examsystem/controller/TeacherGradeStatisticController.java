package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Personnel.StudentDatabase;
import comp3111.examsystem.entity.Questions.QuestionType;
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

/**
 * Controller class for grade statistics.
 * @author Wan Hanzhe
 */
public class TeacherGradeStatisticController implements Initializable {
    @FXML
    private ChoiceBox<String> courseCombox;
    @FXML
    private ChoiceBox<String> examCombox;
    @FXML
    private ChoiceBox<String> studentCombox;
    @FXML
    private ChoiceBox<String> questionCombox;
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

    /**
     * A list of all courses.
     */
    private List<Course> courses = CourseDatabase.getInstance().getAll();

    /**
     * A list of all exams.
     */
    private List<Exam> exams = ExamDatabase.getInstance().getAll();

    /**
     * A list of all students.
     */
    private List<Student> students = StudentDatabase.getInstance().getAll();

    /**
     * A list of filtered submissions.
     */
    private List<Submission> currentSubmissionList = new ArrayList<>();

    /**
     * A dictionary that maps (course ID, exam name) to exam ID.
     */
    private Dictionary<String, String> examNameToIdDict = new Hashtable<>();

    /**
     * Initialize the table, choice boxes and charts.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentSubmissionList = SubmissionDatabase.getInstance().getAll();
        ObservableList<Submission> submissionRecords = FXCollections.observableArrayList(currentSubmissionList);
        gradeTable.setItems(submissionRecords);

        if(submissionRecords.isEmpty())
            return;

        studentColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getStudentUsername()).asString());
        courseColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getCourseId()));
        examColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getExamName()));
        scoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getScore()).asString());
        fullScoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getFullScore()).asString());
        timeSpendColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getTimeSpend()).asString());

        courseCombox.getItems().add("All");
        for(Course course : courses){
            courseCombox.getItems().add(course.getCourseID());
        }

        examNameToIdDict = new Hashtable<>();
        examCombox.getItems().add("All");
        for(Exam exam : exams){
            String courseID = CourseDatabase.getInstance().queryByField("courseID", exam.getCourseId()).getFirst().getCourseID();
            String examName = exam.getName();
            String examOption = courseID + "-" + examName;
            examNameToIdDict.put(examOption, exam.getId().toString());
            examCombox.getItems().add(examOption);
        }

        studentCombox.getItems().add("All");
        for(Student student : students){
            studentCombox.getItems().add(student.getUsername());
        }

        questionCombox.getItems().add("All");
        questionCombox.getItems().add("MC");
        questionCombox.getItems().add("T/F");
        questionCombox.getItems().add("Short Questions");


        barChart.setLegendVisible(false);
        categoryAxisBar.setLabel("Exams");
        categoryAxisBar.setAnimated(false);
        numberAxisBar.setLabel("Avg. Score (%)");
        pieChart.setLegendVisible(false);
        pieChart.setTitle("Avg. Score (%)");
        lineChart.setLegendVisible(false);
        categoryAxisLine.setLabel("Exams");
        categoryAxisLine.setAnimated(false);
        numberAxisLine.setLabel("Avg. Score (%)");

        refresh();
        loadChart();
    }

    /**
     * Called when the user clicks the refresh button.
     * Initialize the table, choice boxes and charts.
     */
    @FXML
    public void refresh() {
        reset();
        filter();
    }

    /**
     * Called by filter().
     * Refresh the charts.
     */
    private void loadChart() {
        XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();
        seriesBar.getData().clear();
        barChart.getData().clear();

        XYChart.Series<String, Number> seriesLine = new XYChart.Series<>();
        seriesLine.getData().clear();
        lineChart.getData().clear();

        pieChart.getData().clear();

        for(Exam exam : exams){
            int count = 0;
            int total = 0;
            int full = 0;
            for(Submission submission : currentSubmissionList){
                if(exam.getId().equals(submission.getExamId())){
                    count++;
                    if(questionCombox.getValue().equals("All")){
                        total += submission.getScore();
                        full = submission.getFullScore();
                    }
                    else if(questionCombox.getValue().equals("MC")){
                        total += submission.getMcScore();
                        full = submission.getMcFullScore();
                    }
                    else if(questionCombox.getValue().equals("T/F")){
                        total += submission.getTfScore();
                        full = submission.getTfFullScore();
                    }
                    else if(questionCombox.getValue().equals("Short Questions")){
                        total += submission.getSqScore();
                        full = submission.getSqFullScore();
                    }
                }
            }
            if(count != 0 && full != 0){
                float percentage = (float) (total / count) / full * 100;
                seriesBar.getData().add(new XYChart.Data<>(exam.getCourseId()+"-"+exam.getName(), percentage));
                seriesLine.getData().add(new XYChart.Data<>(exam.getCourseId()+"-"+exam.getName(), percentage));
                pieChart.getData().add(new PieChart.Data(exam.getCourseId()+"-"+exam.getName(), percentage));
            }
        }
        barChart.getData().add(seriesBar);
        lineChart.getData().add(seriesLine);

    }

    /**
     * Called when the user clicks the reset button.
     * Reset the filter.
     */
    @FXML
    public void reset() {
        courseCombox.getSelectionModel().selectFirst();
        examCombox.getSelectionModel().selectFirst();
        studentCombox.getSelectionModel().selectFirst();
        questionCombox.getSelectionModel().selectFirst();
    }

    /**
     * Called when the user clicks the filter button.
     * Filter the submission records according to the filter.
     * The charts will also be updated.
     */
    @FXML
    public void filter() {
        try{
            List<Submission> filteredSubmissions = filterSubmissions(courseCombox.getValue(), examCombox.getValue(), studentCombox.getValue());
            List<Submission> submissionRecords = new ArrayList<>();

            if(questionCombox.getValue().equals("All")){
                scoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getScore()).asString());
                fullScoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getFullScore()).asString());
                for(Submission submission : filteredSubmissions){
                    if(submission.getFullScore() > 0){
                        submissionRecords.add(submission);
                    }
                }
            }
            else if(questionCombox.getValue().equals("MC")){
                scoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getMcScore()).asString());
                fullScoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getMcFullScore()).asString());
                for(Submission submission : filteredSubmissions){
                    if(submission.getMcFullScore() > 0){
                        submissionRecords.add(submission);
                    }
                }
            }
            else if(questionCombox.getValue().equals("T/F")){
                scoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getTfScore()).asString());
                fullScoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getTfFullScore()).asString());
                for(Submission submission : filteredSubmissions){
                    if(submission.getTfFullScore() > 0){
                        submissionRecords.add(submission);
                    }
                }
            }
            else if(questionCombox.getValue().equals("Short Questions")){
                scoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getSqScore()).asString());
                fullScoreColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getSqFullScore()).asString());
                for(Submission submission : filteredSubmissions){
                    if(submission.getSqFullScore() > 0){
                        submissionRecords.add(submission);
                    }
                }
            }
            currentSubmissionList = submissionRecords;
            gradeTable.getItems().setAll(currentSubmissionList);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
        loadChart();
    }

    /**
     * Retrieve submission records from the database.
     * @param courseFilter the filter for course.
     * @param examFilter the filter for exam.
     * @param studentFilter the filter for student.
     * @return a list of records matches the filter.
     */
    public List<Submission> filterSubmissions(String courseFilter, String examFilter, String studentFilter){
        List<Submission> courseFilterSubmissions = new ArrayList<>();
        List<Submission> examFilterSubmissions = new ArrayList<>();
        List<Submission> studentFilterSubmissions = new ArrayList<>();
        if(courseFilter.equals("All"))
            courseFilterSubmissions = SubmissionDatabase.getInstance().getAll();
        else
            courseFilterSubmissions = SubmissionDatabase.getInstance().queryByField("courseId", courseFilter);
        if(examFilter.equals("All"))
            examFilterSubmissions = SubmissionDatabase.getInstance().getAll();
        else
            examFilterSubmissions = SubmissionDatabase.getInstance().queryByField("examId", examNameToIdDict.get(examFilter));
        if(studentFilter.equals("All"))
            studentFilterSubmissions = SubmissionDatabase.getInstance().getAll();
        else
            studentFilterSubmissions = SubmissionDatabase.getInstance().queryByField("studentUsername", studentFilter);
        List<Submission> filteredSubmissions = SubmissionDatabase.getInstance().join(SubmissionDatabase.getInstance().join(courseFilterSubmissions, examFilterSubmissions), studentFilterSubmissions);
        return filteredSubmissions;
    }
}
