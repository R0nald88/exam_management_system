package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Questions.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

/**
 * The StudentGradeStatisticController class handles the statistics
 * related to the grades of a student in the exam system. It manages
 * the display of the student's grades and the details of each exam,
 * including the questions and answers.
 */
public class StudentGradeStatisticController implements Initializable {

    /**
     * This GradeDetailClass is to be used by gradeTable to help display data
     * Represents the details of a student's grade for a specific exam.
     */
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
    private TableColumn<GradeDetailClass, String> courseColumn, examColumn, scoreColumn, fullScoreColumn, timeSpendColumn;

    private List<String> courseList;
    @FXML
    BarChart<String, Number> examBarChart;
    @FXML
    BarChart<String, Number> submissionBarChart;

    @FXML
    CategoryAxis examCategoryAxisBar, submissionCategoryAxisBar;
    @FXML
    NumberAxis examNumberAxisBar, submissionNumberAxisBar;

    private Student student;
    private List<Submission> studentGradeTableList;

    private final ObservableList<GradeDetailClass> gradeList = FXCollections.observableArrayList();

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is called automatically after the FXML file is loaded.
     *
     * @param url The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object,
     *                  or null if the root object was not localized.
     * @author Li Ching Ho
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gradeList.clear();
        courseList = new ArrayList<>();

        examBarChart.setLegendVisible(false);
        examBarChart.setAnimated(false);
        examCategoryAxisBar.setLabel("Exam");
        examCategoryAxisBar.setAnimated(false);
        examNumberAxisBar.setLabel("Score (%)");

        submissionBarChart.setLegendVisible(false);
        submissionBarChart.setAnimated(false);
        submissionCategoryAxisBar.setLabel("Question Type");
        submissionCategoryAxisBar.setAnimated(false);
        submissionNumberAxisBar.setLabel("Correct Percentage (%)");


        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpend"));

    }

    /**
     * Sets the student for which the statistics will be displayed.
     * Sets the content of the tables and charts
     *
     * @param student The student whose grades and statistics are to be displayed.
     * @author Li Ching Ho
     */
    public void setStudent(Student student) {
        this.student = student;
        gradeList.clear();
        courseCombox.getItems().clear();

        studentGradeTableList = SubmissionDatabase.getInstance().filter(student.getUsername(), null, null);
        if (!studentGradeTableList.isEmpty()) {
            for (Submission submission : studentGradeTableList) {
                Exam exam = ExamDatabase.getInstance().queryByKey(submission.getExamId().toString());
                if (exam != null) {
                    if (!courseList.contains(exam.getCourseId())) {
                        courseList.add(exam.getCourseId());
                    }
                }
            }
            if (courseList != null) {
                courseCombox.getItems().addAll(courseList);
            }
        }
        reset();
        setTable();
        loadChart();
    }

    /**
     * Refreshes the displayed statistics and charts for the current student.
     *
     * @author Li Ching Ho
     */
    @FXML
    public void refresh() {
        setStudent(student);
        reset();
        submissionBarChart.getData().clear();
        setTable();;
        loadChart();
    }

    private void setTable() {
        gradeTable.getItems().clear();
        if(!studentGradeTableList.isEmpty()) {
            for (Submission submission : studentGradeTableList) {
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
            gradeTable.setRowFactory(tr -> {
                TableRow<GradeDetailClass> tableRow = new TableRow<GradeDetailClass>() {
                };

                // Handle mouse click event
                tableRow.setOnMouseClicked(event -> {
                    XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();
                    seriesBar.getData().clear();
                    submissionBarChart.getData().clear();
                    if (studentGradeTableList != null && !studentGradeTableList.isEmpty()) {
                        Submission submission = studentGradeTableList.get(tableRow.getIndex());
                        if (submission != null) {
                            if (submission.getMcFullScore() != 0) {
                                Double mcPercentage = (double) (submission.getMcScore()) / submission.getMcFullScore() * 100;
                                seriesBar.getData().add(new XYChart.Data<>("Multiple Choice", mcPercentage));
                            }
                            if (submission.getTfFullScore() != 0) {
                                Double tfPercentage = (double) (submission.getTfScore()) / submission.getTfFullScore() * 100;
                                seriesBar.getData().add(new XYChart.Data<>("True/False", tfPercentage));
                            }
                            if (submission.getSqFullScore() != 0) {
                                Double sqPercentage = (double) (submission.getSqScore()) / submission.getSqFullScore() * 100;
                                seriesBar.getData().add(new XYChart.Data<>("Short Question", sqPercentage));
                            }
                        }
                        submissionBarChart.getData().add(seriesBar);
                    }
                });

                return tableRow;
            });

        }
    }
    /**
     * Loads the chart data based on the student's grades.
     *
     * @author Li Ching Ho
     */
    private void loadChart() {
        XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();
        seriesBar.getData().clear();
        examBarChart.getData().clear();
        if (studentGradeTableList != null && !studentGradeTableList.isEmpty()) {
            for (Submission submission : studentGradeTableList) {
                Exam exam = ExamDatabase.getInstance().queryByKey(submission.getExamId().toString());
                if (exam != null) {
                    Double scorePercentage = (double) (submission.getScore()) / submission.getFullScore() * 100;
                    seriesBar.getData().add(new XYChart.Data<>(exam.getCourseId() + "-" + exam.getName(),
                            scorePercentage));
                }
            }
            examBarChart.getData().add(seriesBar);
        }

    }

    /**
     * Resets the current view by clearing selections of course filter.
     *
     * @author Li Ching Ho
     */
    @FXML
    public void reset() {
        courseCombox.setValue(null); // Clear the selected item
        studentGradeTableList = SubmissionDatabase.getInstance().filter(student.getUsername(),null,null);
    }

    /**
     * Queries the student's grades based on the selected course filter.
     *
     * @author Li Ching Ho
     */
    @FXML
    public void query() {
        studentGradeTableList = SubmissionDatabase.getInstance().filter(student.getUsername(), courseCombox.getValue(),null);
        submissionBarChart.getData().clear();
        setTable();
        loadChart();
    }
}
