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

    /**
     * Controller class for course management system.
     * @param url unused.
     * @param resourceBundle unused.
     */
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
            else{
                deleteBtn.setDisable(true);
                updateBtn.setDisable(true);
            }
        });
        deleteBtn.setDisable(true);
        updateBtn.setDisable(true);
    }

    /**
     * Called when the user clicks the reset button.
     * Reset the filter.
     */
    @FXML
    public void reset(){
        courseIDTxt.setText("");
        courseNameTxt.setText("");
        departmentTxt.setText("");
    }

    /**
     * Called when the user clicks the filter button.
     * Filter the records according to the filter.
     */
    @FXML
    public void filter(){
        try{
            List<Course> courses = filterCourses(courseIDTxt.getText(), courseNameTxt.getText(), departmentTxt.getText());
            recordTable.getItems().setAll(courses);
        } catch (Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    /**
     * Retrieve records from the database.
     * @param courseID the filter for course ID.
     * @param courseName the filter for course name.
     * @param department the filter for department.
     * @return a list of records matches the filter.
     */
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

    /**
     * Called when the user selects a record and clicks delete button.
     * Delete the selected record from the database.
     */
    @FXML
    public void delete(){
        deleteCourse(false);
    }

    /**
     * Perform the deletion. Called by delete() only.
     * @param deleteExam Only visible to programmer. A course that has exams will only be deleted when deleteExam is true.
     *                   Default to be false when called by delete(), and a warning will be sent to the user.
     *                   The function calls itself again and set deleteExam to be true when the user clicks OK.
     */
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

    /**
     * Called when the user clicks the refresh button, also automatically called after add/delete/update.
     * Initialize the table, choice boxes and buttons.
     */
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

    /**
     * Called when the user clicks the add button.
     * Create and add a record according to the form.
     */
    @FXML
    public void add(){
        try{
            addCourse(formCourseIDTxt.getText(), formCourseNameTxt.getText(), formDepartmentTxt.getText());
            MsgSender.showConfirm("Successful", "A new course record has been created.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    /**
     * Add the record to the database.
     * @param courseID the course ID of the new course.
     * @param courseName the course name of the new course.
     * @param department the department of the new course.
     */
    public static void addCourse(String courseID, String courseName, String department){
        Course course = new Course();
        course.setCourseID(courseID);
        course.setCourseName(courseName);
        course.setDepartment(department);
        CourseDatabase.getInstance().add(course);
    }

    /**
     * Called when the user selects a record and clicks the update button.
     * Update the selected record according to the form.
     */
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

    /**
     * Perform the update in the database.
     * @param selectedCourse the course to be updated.
     * @param courseID the new course ID.
     * @param courseName the new course name.
     * @param department the new department.
     */
    public static void updateCourse(Course selectedCourse, String courseID, String courseName, String department){
        if(!courseID.isEmpty() && !selectedCourse.getCourseID().equals(courseID))
            selectedCourse.setCourseID(courseID);
        if(!courseName.isEmpty())
            selectedCourse.setCourseName(courseName);
        if(!department.isEmpty())
            selectedCourse.setDepartment(department);
        CourseDatabase.getInstance().update(selectedCourse);
    }
}
