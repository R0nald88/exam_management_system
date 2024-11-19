package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Course.Course;
import comp3111.examsystem.entity.Course.CourseDatabase;
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
import javafx.scene.control.TextField;
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


    public void initialize(URL url, ResourceBundle resourceBundle){
        List<Course> courses = CourseDatabase.getInstance().getAll();
        ObservableList<Course> courseRecords = FXCollections.observableArrayList(courses);
        recordTable.setItems(courseRecords);
        courseIDColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getCourseID()));
        courseNameColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getCourseName()));
        departmentColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getDepartment()));
    }

    public void reset(){
        courseIDTxt.setText("");
        courseNameTxt.setText("");
        departmentTxt.setText("");
    }

    public void filter(){
        try{
            List<Course> courses = new ArrayList<>();
            List<Course> courseIDFilterCourses = new ArrayList<>();
            List<Course> courseNameFilterCourses = new ArrayList<>();
            List<Course> departmentFilterCourses = new ArrayList<>();
            if(courseIDTxt.getText().isEmpty())
                courseIDFilterCourses = CourseDatabase.getInstance().getAll();
            else
                courseIDFilterCourses = CourseDatabase.getInstance().queryFuzzyByField("courseID", courseIDTxt.getText());
            if(courseNameTxt.getText().isEmpty())
                courseNameFilterCourses = CourseDatabase.getInstance().getAll();
            else
                courseNameFilterCourses = CourseDatabase.getInstance().queryFuzzyByField("courseName", courseNameTxt.getText());
            if(departmentTxt.getText().isEmpty())
                departmentFilterCourses = CourseDatabase.getInstance().getAll();
            else
                departmentFilterCourses = CourseDatabase.getInstance().queryFuzzyByField("department", departmentTxt.getText());
            courses = CourseDatabase.getInstance().join(CourseDatabase.getInstance().join(courseIDFilterCourses, courseNameFilterCourses), departmentFilterCourses);
            recordTable.getItems().setAll(courses);
        } catch (Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public void delete(){
        try{
            Course selectedCourse = recordTable.getSelectionModel().getSelectedItem();
            CourseDatabase.getInstance().delByFiled("courseID", selectedCourse.getCourseID());
            MsgSender.showConfirm("Successful", "The course record has been deleted.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public void refresh(){
        formCourseIDTxt.setText("");
        formCourseNameTxt.setText("");
        formDepartmentTxt.setText("");

        reset();
        filter();
    }

    public void add(){
        try{
            Course course = new Course();
            course.setCourseID(formCourseIDTxt.getText());
            course.setCourseName(formCourseNameTxt.getText());
            course.setDepartment(formDepartmentTxt.getText());
            CourseDatabase.getInstance().add(course);
            MsgSender.showConfirm("Successful", "A new course record has been created.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public void update(){
        try{
            Course selectedCourse = recordTable.getSelectionModel().getSelectedItem();
            selectedCourse.setCourseID(formCourseIDTxt.getText());
            selectedCourse.setCourseName(formCourseNameTxt.getText());
            selectedCourse.setDepartment(formDepartmentTxt.getText());
            CourseDatabase.getInstance().update(selectedCourse);
            MsgSender.showConfirm("Successful", "The course record has been updated.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }
}
