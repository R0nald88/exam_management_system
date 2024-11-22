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

    /**
     * This ExamDetailClass is to be used by examDetailTable to help display data
     * Represents the details of a student's submission for a specific exam.
     */
    public static class ExamDetailClass {
        private String question, optionA, optionB, optionC, optionD, studentAnswer, answer, studentScore, fullScore;

        public void setQuestion(String question) {
            this.question = question;
        }

        public void setOptionA(String optionA) {
            this.optionA = optionA;
        }

        public void setOptionB(String optionB) {
            this.optionB = optionB;
        }

        public void setOptionC(String optionC) {
            this.optionC = optionC;
        }

        public void setOptionD(String optionD) {
            this.optionD = optionD;
        }

        public void setStudentAnswer(String studentAnswer) {
            this.studentAnswer = studentAnswer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public void setStudentScore(String studentScore) {
            this.studentScore = studentScore;
        }

        public void setFullScore(String fullScore) {
            this.fullScore = fullScore;
        }

        public String getQuestion() {
            return question;
        }

        public String getOptionA() {
            return optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public String getStudentAnswer() {
            return studentAnswer;
        }

        public String getAnswer() {
            return answer;
        }

        public String getStudentScore() {
            return studentScore;
        }

        public String getFullScore() {
            return fullScore;
        }
    }

    @FXML
    private ChoiceBox<String> courseCombox;
    @FXML
    private TableView<GradeDetailClass> gradeTable;
    @FXML
    private TableColumn<GradeDetailClass, String> courseColumn, examColumn, scoreColumn, fullScoreColumn, timeSpendColumn;
    @FXML
    public TableColumn<ExamDetailClass, String> questionCol, optionACol, optionBCol, optionCCol, optionDCol, answerCol, studentAnswerCol, studentScoreCol, fullScoreCol;

    private List<String> courseList;
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    PieChart pieChart;
    @FXML
    CategoryAxis categoryAxisBar;
    @FXML
    NumberAxis numberAxisBar;
    @FXML
    private TableView<ExamDetailClass> examDetailTable;

    private Student student;
    private List<Submission> studentGradeTableList;

    private final ObservableList<GradeDetailClass> gradeList = FXCollections.observableArrayList();
    private final ObservableList<ExamDetailClass> examDetailList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gradeList.clear();
        courseList = new ArrayList<>();

        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        categoryAxisBar.setLabel("Exam");
        categoryAxisBar.setAnimated(false);
        numberAxisBar.setLabel("Score");

        pieChart.setLegendVisible(false);
        pieChart.setAnimated(false);

        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpend"));

        questionCol.setCellValueFactory(new PropertyValueFactory<>("question"));
        optionACol.setCellValueFactory(new PropertyValueFactory<>("optionA"));
        optionBCol.setCellValueFactory(new PropertyValueFactory<>("optionB"));
        optionCCol.setCellValueFactory(new PropertyValueFactory<>("optionC"));
        optionDCol.setCellValueFactory(new PropertyValueFactory<>("optionD"));
        studentAnswerCol.setCellValueFactory(new PropertyValueFactory<>("studentAnswer"));
        answerCol.setCellValueFactory(new PropertyValueFactory<>("answer"));
        studentScoreCol.setCellValueFactory(new PropertyValueFactory<>("studentScore"));
        fullScoreCol.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
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
        examDetailList.clear();
        pieChart.setTitle(null);
        pieChart.getData().clear();
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
     * Sets ToolTips for all table cells in column in examDetailTable
     *
     * @author Li Ching Ho
     */
    private void setExamDetailTooltips() {
        questionCol.setCellFactory(col -> createTooltipCell());
        optionACol.setCellFactory(col -> createTooltipCell());
        optionBCol.setCellFactory(col -> createTooltipCell());
        optionCCol.setCellFactory(col -> createTooltipCell());
        optionDCol.setCellFactory(col -> createTooltipCell());
        studentAnswerCol.setCellFactory(col -> createTooltipCell());
        answerCol.setCellFactory(col -> createTooltipCell());
        studentScoreCol.setCellFactory(col -> createTooltipCell());
        fullScoreCol.setCellFactory(col -> createTooltipCell());
    }

    /**
     * Creates a TableCell with a tooltip for the exam detail table.
     *
     * @param <T> The type of the cell value.
     * @return A TableCell configured to show a tooltip.
     * @author Li Ching Ho
     */
    private <T> TableCell<ExamDetailClass, T> createTooltipCell() {
        return new TableCell<ExamDetailClass, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item.toString());
                    Tooltip tooltip = new Tooltip(item.toString()); // Set tooltip with cell data
                    setTooltip(tooltip);
                }
            }
        };
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
                    examDetailTable.getItems().clear();
                    pieChart.getData().clear();
                    if (!tableRow.isEmpty()) {
                        Submission submission = SubmissionDatabase.getInstance().queryByKey(studentGradeTableList.get(tableRow.getIndex()).getId().toString());
                        for (Question question : submission.getQuestionObjectList()) {
                            int i = submission.getQuestionObjectList().indexOf(question);
                            ExamDetailClass examDetail = new ExamDetailClass();
                            examDetail.setQuestion(question.getQuestion());
                            examDetail.setOptionA(question.getOptionA());
                            examDetail.setOptionB(question.getOptionB());
                            examDetail.setOptionC(question.getOptionC());
                            examDetail.setOptionD(question.getOptionD());
                            examDetail.setStudentAnswer(submission.getAnswerList().get(i));
                            examDetail.setAnswer(question.getAnswer());
                            examDetail.setStudentScore(submission.getScoreList().get(i).toString());
                            examDetail.setFullScore(String.valueOf(question.getScore()));

                            // Add the populated grade detail to the grade list
                            examDetailList.add(examDetail);
                        }
                        examDetailTable.setItems(examDetailList);
                        setExamDetailTooltips();

                        pieChart.setTitle("Score Distribution for each Question Type");
                        pieChart.getData().add(new PieChart.Data("Multiple-Choice Questions Score", submission.getMcFullScore()));
                        pieChart.getData().add(new PieChart.Data("True/False Questions Score", submission.getTfScore()));
                        pieChart.getData().add(new PieChart.Data("Short Questions Score", submission.getSqScore()));
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
        barChart.getData().clear();
        if (studentGradeTableList != null && !studentGradeTableList.isEmpty()) {
            for (Submission submission : studentGradeTableList) {
                Exam exam = ExamDatabase.getInstance().queryByKey(submission.getExamId().toString());
                if (exam != null) {
                    seriesBar.getData().add(new XYChart.Data<>(exam.getCourseId() + "-" + exam.getName(),
                            submission.getScore()));
                }
            }
            barChart.getData().add(seriesBar);
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
        setTable();
        loadChart();
    }
}
