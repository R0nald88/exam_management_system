package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Questions.Question;
import comp3111.examsystem.entity.Questions.QuestionDatabase;
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
        private String courseNum, examName, score, fullScore, timeSpend;

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

        public void setTimeSpend(String timeSpend) {
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
        public String getTimeSpend() {
            return timeSpend;
        }
    }

    @FXML
    private ChoiceBox<String> courseCombox;
    @FXML
    private TableView<GradeDetailClass> gradeTable;
    @FXML
    private TableColumn<GradeDetailClass, String> courseColumn;
    private ObservableList<String> courseList;
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
    private List<Submission> studentGradeList;

    public void setStudent(Student student) {
        this.student = student;

        studentGradeList = SubmissionDatabase.getInstance().filter(student.getName(), null, null);
        if (!studentGradeList.isEmpty()) {
            for (Submission submission : studentGradeList) {
                if (courseList.isEmpty() || !courseList.contains(submission.getExam().getCourseId())) {
                    courseList.add(submission.getExam().getCourseId());
                }
            }
            courseCombox.getItems().addAll(courseList);


            for (Submission submission : studentGradeList) {
                GradeDetailClass gradeDetail = new GradeDetailClass();

                gradeDetail.setCourseNum(submission.getExam().getCourseId());
                gradeDetail.setExamName(submission.getExam().getName());
                gradeDetail.setScore(String.valueOf(submission.getScore()));
                gradeDetail.setFullScore(String.valueOf(submission.getExam().getFullScore()));
                gradeDetail.setTimeSpend(submission.getTimeSpent());

                // Add the populated grade detail to the grade list
                gradeList.add(gradeDetail);


                refresh();
                loadChart();
            }
        }
    }

    private final ObservableList<GradeDetailClass> gradeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gradeList.clear();
        studentGradeList = SubmissionDatabase.getInstance().filter(null, null, null);
        if (!studentGradeList.isEmpty()) {
            for (Submission submission : studentGradeList) {
                if (courseList.isEmpty() || !courseList.contains(submission.getExam().getCourseId())) {
                    courseList.add(submission.getExam().getCourseId());
                }
            }
            courseCombox.getItems().addAll(courseList);
        }

        barChart.setLegendVisible(false);
        categoryAxisBar.setLabel("Exam");
        numberAxisBar.setLabel("Score");

        gradeList.add(new GradeDetailClass());
        gradeTable.setItems(gradeList);
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpend"));

        refresh();
    }

    @FXML
    public void refresh() {
        reset();
        loadChart();
    }

    private void loadChart() {
        XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();
        seriesBar.getData().clear();
        barChart.getData().clear();
        if (!studentGradeList.isEmpty()) {
            for (Submission submission : studentGradeList) {
                seriesBar.getData().add(new XYChart.Data<>(submission.getExam().getCourseId() + "-" + submission.getExam().getName(),
                        submission.getScore()));
            }
            barChart.getData().add(seriesBar);
        }

    }

    @FXML
    public void reset() {
        courseCombox.setValue(null); // Clear the selected item
    }

    @FXML
    public void query() {
        loadChart();
    }
}
