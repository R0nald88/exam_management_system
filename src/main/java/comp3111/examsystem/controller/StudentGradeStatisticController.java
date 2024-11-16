package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam.Exam;
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
import java.util.List;
import java.util.ResourceBundle;

public class StudentGradeStatisticController implements Initializable {
    public static class GradeExampleClass {
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
    private TableView<GradeExampleClass> gradeTable;
    @FXML
    private TableColumn<GradeExampleClass, String> courseColumn;
    @FXML
    private TableColumn<GradeExampleClass, String> examColumn;
    @FXML
    private TableColumn<GradeExampleClass, String> scoreColumn;
    @FXML
    private TableColumn<GradeExampleClass, String> fullScoreColumn;
    @FXML
    private TableColumn<GradeExampleClass, String> timeSpendColumn;
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis categoryAxisBar;
    @FXML
    NumberAxis numberAxisBar;

    private Student student;
    private List<Exam> examList;

    public void setStudent(Student student) {
        this.student = student;
    }

    private final ObservableList<GradeExampleClass> gradeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //TODO: set Student Grade Statistics content to this student

        barChart.setLegendVisible(false);
        categoryAxisBar.setLabel("Exam");
        numberAxisBar.setLabel("Score");

        gradeList.add(new GradeExampleClass());
        gradeTable.setItems(gradeList);
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpend"));

        refresh();
        loadChart();
    }

    @FXML
    public void refresh() {
        reset();
        loadChart();
    }

    private void loadChart() {

        //TODO: set Student Grade Statistics content to this student and handle filter
        //TODO: may also change it to load table as well

        XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();
        seriesBar.getData().clear();
        barChart.getData().clear();
        for (int i = 0;  i < 5; i++) {
            seriesBar.getData().add(new XYChart.Data<>("COMP" + i, 50));
        }
        barChart.getData().add(seriesBar);

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
