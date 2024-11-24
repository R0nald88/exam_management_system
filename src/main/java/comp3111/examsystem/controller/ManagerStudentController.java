package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Personnel.Gender;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Personnel.StudentDatabase;
import comp3111.examsystem.tools.MsgSender;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

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

    @FXML
    private Button deleteBtn;
    @FXML
    private Button updateBtn;



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

        recordTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Student>) change -> {
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
        usernameTxt.setText("");
        nameTxt.setText("");
        departmentTxt.setText("");
    }

    @FXML
    public void filter(){
        try{
            List<Student> students = filterStudents(usernameTxt.getText(), nameTxt.getText(), departmentTxt.getText());
            recordTable.getItems().setAll(students);
        } catch (Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static List<Student> filterStudents(String username, String name, String department){
        List<Student> students = new ArrayList<>();
        List<Student> usernameFilterStudents = new ArrayList<>();
        List<Student> nameFilterStudents = new ArrayList<>();
        List<Student> departmentFilterStudents = new ArrayList<>();
        if(username.isEmpty())
            usernameFilterStudents = StudentDatabase.getInstance().getAll();
        else
            usernameFilterStudents = StudentDatabase.getInstance().queryByField("username", username);
        if(name.isEmpty())
            nameFilterStudents = StudentDatabase.getInstance().getAll();
        else
            nameFilterStudents = StudentDatabase.getInstance().queryByField("name", name);
        if(department.isEmpty())
            departmentFilterStudents = StudentDatabase.getInstance().getAll();
        else
            departmentFilterStudents = StudentDatabase.getInstance().queryByField("department", department);
        students = StudentDatabase.getInstance().join(StudentDatabase.getInstance().join(usernameFilterStudents, nameFilterStudents), departmentFilterStudents);
        return students;
    }

    @FXML
    public void delete(){
        try{
            Student selectedStudent = recordTable.getSelectionModel().getSelectedItem();
            deleteStudent(selectedStudent);
            MsgSender.showConfirm("Successful", "The student record has been deleted.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static void deleteStudent(Student student){
        StudentDatabase.getInstance().delByFiled("username", student.getUsername());
    }

    @FXML
    public void refresh(){
        formUsernameTxt.setText("");
        formNameTxt.setText("");
        formAgeTxt.setText("");
        formDepartmentTxt.setText("");
        formPasswordTxt.setText("");
        genderCombox.getSelectionModel().selectFirst();

        deleteBtn.setDisable(true);
        updateBtn.setDisable(true);

        reset();
        filter();
    }

    @FXML
    public void add(){
        try{
            addStudent(formUsernameTxt.getText(), formNameTxt.getText(), formAgeTxt.getText(), formDepartmentTxt.getText(), formPasswordTxt.getText(), genderCombox.getValue());
            MsgSender.showConfirm("Successful", "A new student record has been created.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static void addStudent(String username, String name, String age, String department, String password, String gender){
        Student student = new Student();
        student.setUsername(username);
        student.setName(name);
        student.setAge(age);
        student.setDepartment(department);
        student.setPassword(password);
        student.setGender(gender);
        StudentDatabase.getInstance().add(student);
    }

    @FXML
    public void update(){
        try{
            Student selectedStudent = recordTable.getSelectionModel().getSelectedItem();
            updateStudent(selectedStudent, formUsernameTxt.getText(), formNameTxt.getText(), formAgeTxt.getText(), formDepartmentTxt.getText(), formPasswordTxt.getText(), genderCombox.getValue());
            MsgSender.showConfirm("Successful", "The student record has been updated.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static void updateStudent(Student selectedStudent, String username, String name, String age, String department, String password, String gender){
        if(selectedStudent.getUsername().equals(username))
            selectedStudent.forceSetUsername(username);
        else
            selectedStudent.setUsername(username);
        selectedStudent.setName(name);
        selectedStudent.setAge(age);
        selectedStudent.setDepartment(department);
        selectedStudent.setPassword(password);
        selectedStudent.setGender(gender);
        StudentDatabase.getInstance().update(selectedStudent);
    }
}
