package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Personnel.Student;
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

public class StudentGradeStatisticController implements Initializable {

    public static class GradeDetailClass {
        private String courseNum, examName, score, fullScore;
        private int timeSpend;

        public void setCourseNum(String courseNum) {
            this.courseNum = courseNum;
        }

        public void setExamName(String examName) {
            this.examName = examName;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public void setFullScore(String fullScore) {
            this.fullScore = fullScore;
        }

        public void setTimeSpend(int timeSpend) {
            this.timeSpend = timeSpend;
        }

        public String getCourseNum() {
            return courseNum;
        }
        public String getExamName() {
            return examName;
        }
        public String getScore() {
            return score;
        }
        public String getFullScore() {
            return fullScore;
        }
        public int getTimeSpend() {
            return timeSpend;
        }
    }

    @FXML
    private ChoiceBox<String> courseCombox;
    @FXML
    private TableView<GradeDetailClass> gradeTable;
    @FXML
    private TableColumn<GradeDetailClass, String> courseColumn;
    private List<String> courseList;
    @FXML
    private TableColumn<GradeDetailClass, String> examColumn;
    @FXML
    private TableColumn<GradeDetailClass, String> scoreColumn;
    @FXML
    private TableColumn<GradeDetailClass, String> fullScoreColumn;
    @FXML
    private TableColumn<GradeDetailClass, String> timeSpendColumn;
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis categoryAxisBar;
    @FXML
    NumberAxis numberAxisBar;

    private Student student;
    private List<Submission> studentGradeTableList;
    private List<Submission> studentGradeBarChartList;

    public void setStudent(Student student) {
        this.student = student;
        gradeList.clear();
        courseCombox.getItems().clear();

        studentGradeTableList = SubmissionDatabase.getInstance().filter(student.getUsername(), null, null);
        if (!studentGradeTableList.isEmpty()) {
            for (Submission submission : studentGradeTableList) {
                Exam exam = ExamDatabase.getInstance().queryByKey(submission.getExamId().toString());
                //System.out.println("Exam submitted:" + exam);
                if (exam != null) {
                    //System.out.println(exam.getCourseId());
                    if (!courseList.contains(exam.getCourseId())) {
                        courseList.add(exam.getCourseId());
                    }
                    //System.out.println(courseList);
                }
            }
            if (courseList != null) {
                courseCombox.getItems().addAll(courseList);
            }
        }

        studentGradeBarChartList = SubmissionDatabase.getInstance().filter(student.getUsername(), null, null);
        if (!studentGradeBarChartList.isEmpty()) {
            for (Submission submission : studentGradeBarChartList) {
                GradeDetailClass gradeDetail = new GradeDetailClass();
                Exam exam = ExamDatabase.getInstance().queryByKey(submission.getExamId().toString());
                if (exam != null) {
                    gradeDetail.setCourseNum(exam.getCourseId());
                    gradeDetail.setExamName(exam.getName());
                    gradeDetail.setScore(String.valueOf(submission.getScore()));
                    gradeDetail.setFullScore(String.valueOf(exam.getFullScore()));
                    gradeDetail.setTimeSpend(submission.getTimeSpend());

                    // Add the populated grade detail to the grade list
                    gradeList.add(gradeDetail);
                }
            }
            gradeTable.setItems(gradeList);

        }
        reset();
        loadChart();
    }

    private final ObservableList<GradeDetailClass> gradeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gradeList.clear();
        courseList = new ArrayList<>();

        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        categoryAxisBar.setLabel("Exam");
        categoryAxisBar.setAnimated(false);
        numberAxisBar.setLabel("Score");

        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpend"));

    }

    @FXML
    public void refresh() {
        //System.out.println("gradeTable: " + gradeTable.getColumns());
        setStudent(student);
        reset();
        loadChart();
    }

    private void loadChart() {
        XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();
        seriesBar.getData().clear();
        barChart.getData().clear();
        if (studentGradeBarChartList != null && !studentGradeBarChartList.isEmpty()) {
            for (Submission submission : studentGradeBarChartList) {
                Exam exam = ExamDatabase.getInstance().queryByKey(submission.getExamId().toString());
                if (exam != null) {
                    seriesBar.getData().add(new XYChart.Data<>(exam.getCourseId() + "-" + exam.getName(),
                            submission.getScore()));
                }
            }
            barChart.getData().add(seriesBar);
        }

    }

    @FXML
    public void reset() {
        courseCombox.setValue(null); // Clear the selected item
        studentGradeBarChartList = SubmissionDatabase.getInstance().filter(student.getUsername(),null,null);
    }

    @FXML
    public void query() {
        studentGradeBarChartList = SubmissionDatabase.getInstance().filter(student.getUsername(), courseCombox.getValue(),null);
        loadChart();
    }
}
