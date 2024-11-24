package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.tools.MsgSender;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class ManagerCourseController implements Initializable{

    @FXML
    private TextField courseIDTxt;
    @FXML
    private TextField courseNameTxt;
    @FXML
    private TextField departmentTxt;
    @FXML
    private TextField formCourseIDTxt;
    @FXML
    private TextField formCourseNameTxt;
    @FXML
    private TextField formDepartmentTxt;
    @FXML
    private TableView<Course> recordTable;
    @FXML
    private TableColumn<Course, String> courseIDColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, String> departmentColumn;

    @FXML
    private Button deleteBtn;
    @FXML
    private Button updateBtn;


    public void initialize(URL url, ResourceBundle resourceBundle){
        List<Course> courses = CourseDatabase.getInstance().getAll();
        ObservableList<Course> courseRecords = FXCollections.observableArrayList(courses);
        recordTable.setItems(courseRecords);
        courseIDColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getCourseID()));
        courseNameColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getCourseName()));
        departmentColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getDepartment()));

        recordTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Course>) change -> {
            if (!change.getList().isEmpty()) {
                deleteBtn.setDisable(false);
                updateBtn.setDisable(false);
            }
        });
        deleteBtn.setDisable(true);
        updateBtn.setDisable(true);
    }

    @FXML
    public void reset(){
        courseIDTxt.setText("");
        courseNameTxt.setText("");
        departmentTxt.setText("");
    }

    @FXML
    public void filter(){
        try{
            List<Course> courses = filterCourses(courseIDTxt.getText(), courseNameTxt.getText(), departmentTxt.getText());
            recordTable.getItems().setAll(courses);
        } catch (Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static List<Course> filterCourses(String courseID, String courseName, String department){
        List<Course> courses = new ArrayList<>();
        List<Course> courseIDFilterCourses = new ArrayList<>();
        List<Course> courseNameFilterCourses = new ArrayList<>();
        List<Course> departmentFilterCourses = new ArrayList<>();
        if(courseID.isEmpty())
            courseIDFilterCourses = CourseDatabase.getInstance().getAll();
        else
            courseIDFilterCourses = CourseDatabase.getInstance().queryFuzzyByField("courseID", courseID);
        if(courseName.isEmpty())
            courseNameFilterCourses = CourseDatabase.getInstance().getAll();
        else
            courseNameFilterCourses = CourseDatabase.getInstance().queryFuzzyByField("courseName", courseName);
        if(department.isEmpty())
            departmentFilterCourses = CourseDatabase.getInstance().getAll();
        else
            departmentFilterCourses = CourseDatabase.getInstance().queryFuzzyByField("department", department);
        courses = CourseDatabase.getInstance().join(CourseDatabase.getInstance().join(courseIDFilterCourses, courseNameFilterCourses), departmentFilterCourses);
        return courses;
    }

    @FXML
    public void delete(){
        deleteCourse(false);
    }

    private void deleteCourse(boolean deleteExam) {
        try{
            Course selectedCourse = recordTable.getSelectionModel().getSelectedItem();
            List<Exam> affectedExam = ExamDatabase.getInstance().queryByField("longIdOfCourse", selectedCourse.getId().toString());

            if (!affectedExam.isEmpty() && !deleteExam) {
                MsgSender.showConfirm("Warning",
                        "Selected course has associated exam(s) and submission(s). \nClick \"OK\" to continue deleting the course and associated exam(s) and submission(s).",
                        () -> deleteCourse(true)
                );
                return;
            } else if (!affectedExam.isEmpty()) {
//                SubmissionDatabase db = SubmissionDatabase.getInstance();
//                for (Exam e : affectedExam) {
//                    db.deleteSubmissions(db.queryByField("examId", e.getId().toString()));
//                }

                ExamDatabase.getInstance().deleteExams(affectedExam);
            }

            CourseDatabase.getInstance().delByFiled("courseID", selectedCourse.getCourseID());
            MsgSender.showConfirm("Successful", "The course record has been deleted.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    @FXML
    public void refresh(){
        formCourseIDTxt.setText("");
        formCourseNameTxt.setText("");
        formDepartmentTxt.setText("");

        deleteBtn.setDisable(true);
        updateBtn.setDisable(true);

        reset();
        filter();
    }

    @FXML
    public void add(){
        try{
            addCourse(formCourseIDTxt.getText(), formCourseNameTxt.getText(), formDepartmentTxt.getText());
            MsgSender.showConfirm("Successful", "A new course record has been created.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static void addCourse(String courseID, String courseName, String department){
        Course course = new Course();
        course.setCourseID(courseID);
        course.setCourseName(courseName);
        course.setDepartment(department);
        CourseDatabase.getInstance().add(course);
    }

    @FXML
    public void update(){
        try{
            Course selectedCourse = recordTable.getSelectionModel().getSelectedItem();
            updateCourse(selectedCourse, formCourseIDTxt.getText(), formCourseNameTxt.getText(), formDepartmentTxt.getText());
            MsgSender.showConfirm("Successful", "The course record has been updated.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static void updateCourse(Course selectedCourse, String courseID, String courseName, String department){
        if(selectedCourse.getCourseID().equals(courseID))
            selectedCourse.forceSetCourseID(courseID);
        else
            selectedCourse.setCourseID(courseID);
        selectedCourse.setCourseName(courseName);
        selectedCourse.setDepartment(department);
        CourseDatabase.getInstance().update(selectedCourse);
    }
}
