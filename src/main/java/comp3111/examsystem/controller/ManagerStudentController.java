package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Personnel.Gender;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class ManagerStudentController implements Initializable{

    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField departmentTxt;
    @FXML
    private TextField formUsernameTxt;
    @FXML
    private TextField formNameTxt;
    @FXML
    private TextField formAgeTxt;
    @FXML
    private ChoiceBox<String> genderCombox;
    @FXML
    private TextField formDepartmentTxt;
    @FXML
    private TextField formPasswordTxt;
    @FXML
    private TableView<Student> recordTable;
    @FXML
    private TableColumn<Student, String> usernameColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> ageColumn;
    @FXML
    private TableColumn<Student, String> genderColumn;
    @FXML
    private TableColumn<Student, String> departmentColumn;
    @FXML
    private TableColumn<Student, String> passwordColumn;


    public void initialize(URL url, ResourceBundle resourceBundle){
        List<Student> students = StudentDatabase.getInstance().getAll();
        ObservableList<Student> studentRecords = FXCollections.observableArrayList(students);
        recordTable.setItems(studentRecords);
        usernameColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getUsername()));
        nameColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getName()));
        ageColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getAge()).asString());
        genderColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getGender()).asString());
        departmentColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getDepartment()));
        passwordColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getPassword()));
        genderCombox.setItems(FXCollections.observableList(Arrays.stream(Gender.values()).map(Gender::getName).toList()));
        genderCombox.getSelectionModel().selectFirst();
    }

    @FXML
    public void reset(){
        usernameTxt.setText("");
        nameTxt.setText("");
        departmentTxt.setText("");
    }

    @FXML
    public void filter(){
        try{
            List<Student> students = new ArrayList<>();
            List<Student> usernameFilterStudents = new ArrayList<>();
            List<Student> nameFilterStudents = new ArrayList<>();
            List<Student> departmentFilterStudents = new ArrayList<>();
            if(usernameTxt.getText().isEmpty())
                usernameFilterStudents = StudentDatabase.getInstance().getAll();
            else
                usernameFilterStudents = StudentDatabase.getInstance().queryFuzzyByField("username", usernameTxt.getText());
            if(nameTxt.getText().isEmpty())
                nameFilterStudents = StudentDatabase.getInstance().getAll();
            else
                nameFilterStudents = StudentDatabase.getInstance().queryFuzzyByField("name", nameTxt.getText());
            if(departmentTxt.getText().isEmpty())
                departmentFilterStudents = StudentDatabase.getInstance().getAll();
            else
                departmentFilterStudents = StudentDatabase.getInstance().queryFuzzyByField("department", departmentTxt.getText());
            students = StudentDatabase.getInstance().join(StudentDatabase.getInstance().join(usernameFilterStudents, nameFilterStudents), departmentFilterStudents);
            recordTable.getItems().setAll(students);
        } catch (Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    @FXML
    public void delete(){
        try{
            Student selectedStudent = recordTable.getSelectionModel().getSelectedItem();
            StudentDatabase.getInstance().delByFiled("username", selectedStudent.getUsername());
            MsgSender.showConfirm("Successful", "The student record has been deleted.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    @FXML
    public void refresh(){
        formUsernameTxt.setText("");
        formNameTxt.setText("");
        formAgeTxt.setText("");
        formDepartmentTxt.setText("");
        formPasswordTxt.setText("");
        genderCombox.getSelectionModel().selectFirst();

        reset();
        filter();
    }

    @FXML
    public void add(){
        try{
            Student student = new Student();
            student.setUsername(formUsernameTxt.getText());
            student.setName(formNameTxt.getText());
            student.setAge(formAgeTxt.getText());
            student.setDepartment(formDepartmentTxt.getText());
            student.setPassword(formPasswordTxt.getText());
            student.setGender(genderCombox.getValue());
            StudentDatabase.getInstance().add(student);
            MsgSender.showConfirm("Successful", "A new student record has been created.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    @FXML
    public void update(){
        try{
            Student selectedStudent = recordTable.getSelectionModel().getSelectedItem();
            if(selectedStudent.getUsername().equals(formUsernameTxt.getText()))
                selectedStudent.forceSetUsername(formUsernameTxt.getText());
            else
                selectedStudent.setUsername(formUsernameTxt.getText());
            selectedStudent.setName(formNameTxt.getText());
            selectedStudent.setAge(formAgeTxt.getText());
            selectedStudent.setDepartment(formDepartmentTxt.getText());
            selectedStudent.setPassword(formPasswordTxt.getText());
            selectedStudent.setGender(genderCombox.getValue());
            StudentDatabase.getInstance().update(selectedStudent);
            MsgSender.showConfirm("Successful", "The student record has been updated.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }
}
