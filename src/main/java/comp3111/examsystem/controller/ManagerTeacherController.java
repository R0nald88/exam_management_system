package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Personnel.*;
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

public class ManagerTeacherController implements Initializable{

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
    private ChoiceBox<String> positionCombox;
    @FXML
    private TextField formDepartmentTxt;
    @FXML
    private TextField formPasswordTxt;
    @FXML
    private TableView<Teacher> recordTable;
    @FXML
    private TableColumn<Teacher, String> usernameColumn;
    @FXML
    private TableColumn<Teacher, String> nameColumn;
    @FXML
    private TableColumn<Teacher, String> ageColumn;
    @FXML
    private TableColumn<Teacher, String> genderColumn;
    @FXML
    private TableColumn<Teacher, String> positionColumn;
    @FXML
    private TableColumn<Teacher, String> departmentColumn;
    @FXML
    private TableColumn<Teacher, String> passwordColumn;

    @FXML
    private Button deleteBtn;
    @FXML
    private Button updateBtn;


    public void initialize(URL url, ResourceBundle resourceBundle){
        List<Teacher> teachers = TeacherDatabase.getInstance().getAll();
        ObservableList<Teacher> teacherRecords = FXCollections.observableArrayList(teachers);
        recordTable.setItems(teacherRecords);
        usernameColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getUsername()));
        nameColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getName()));
        ageColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getAge()).asString());
        genderColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getGender()).asString());
        positionColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getPosition()).asString());
        departmentColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getDepartment()));
        passwordColumn.setCellValueFactory(tableRow -> new ReadOnlyObjectWrapper<>(tableRow.getValue().getPassword()));
        genderCombox.setItems(FXCollections.observableList(Arrays.stream(Gender.values()).map(Gender::getName).toList()));
        positionCombox.setItems(FXCollections.observableList(Arrays.stream(Position.values()).map(Position::getName).toList()));
        genderCombox.getSelectionModel().selectFirst();
        positionCombox.getSelectionModel().selectFirst();

        recordTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Teacher>) change -> {
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
            List<Teacher> teachers = filterTeachers(usernameTxt.getText(), nameTxt.getText(), departmentTxt.getText());
            recordTable.getItems().setAll(teachers);
        } catch (Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static List<Teacher> filterTeachers(String username, String name, String department){
        List<Teacher> teachers = new ArrayList<>();
        List<Teacher> usernameFilterTeachers = new ArrayList<>();
        List<Teacher> nameFilterTeachers = new ArrayList<>();
        List<Teacher> departmentFilterTeachers = new ArrayList<>();
        if(username.isEmpty())
            usernameFilterTeachers = TeacherDatabase.getInstance().getAll();
        else
            usernameFilterTeachers = TeacherDatabase.getInstance().queryFuzzyByField("username", username);
        if(name.isEmpty())
            nameFilterTeachers = TeacherDatabase.getInstance().getAll();
        else
            nameFilterTeachers = TeacherDatabase.getInstance().queryFuzzyByField("name", name);
        if(department.isEmpty())
            departmentFilterTeachers = TeacherDatabase.getInstance().getAll();
        else
            departmentFilterTeachers = TeacherDatabase.getInstance().queryFuzzyByField("department", department);
        teachers = TeacherDatabase.getInstance().join(TeacherDatabase.getInstance().join(usernameFilterTeachers, nameFilterTeachers), departmentFilterTeachers);
        return teachers;
    }

    @FXML
    public void delete(){
        try{
            Teacher selectedTeacher = recordTable.getSelectionModel().getSelectedItem();
            deleteTeacher(selectedTeacher);
            MsgSender.showConfirm("Successful", "The teacher record has been deleted.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static void deleteTeacher(Teacher teacher){
        TeacherDatabase.getInstance().delByFiled("username", teacher.getUsername());
    }

    @FXML
    public void refresh(){
        formUsernameTxt.setText("");
        formNameTxt.setText("");
        formAgeTxt.setText("");
        formDepartmentTxt.setText("");
        formPasswordTxt.setText("");
        genderCombox.getSelectionModel().selectFirst();
        positionCombox.getSelectionModel().selectFirst();

        deleteBtn.setDisable(true);
        updateBtn.setDisable(true);

        reset();
        filter();
    }

    @FXML
    public void add(){
        try{
            addTeacher(formUsernameTxt.getText(), formNameTxt.getText(), formAgeTxt.getText(), formDepartmentTxt.getText(), formPasswordTxt.getText(), genderCombox.getValue(), positionCombox.getValue());
            MsgSender.showConfirm("Successful", "A new teacher record has been created.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static void addTeacher(String username, String name, String age, String department, String password, String gender, String position){
        Teacher teacher = new Teacher();
        teacher.setUsername(username);
        teacher.setName(name);
        teacher.setAge(age);
        teacher.setDepartment(department);
        teacher.setPassword(password);
        teacher.setGender(gender);
        teacher.setPosition(position);
        TeacherDatabase.getInstance().add(teacher);
    }

    @FXML
    public void update(){
        try{
            Teacher selectedTeacher = recordTable.getSelectionModel().getSelectedItem();
            updateTeacher(selectedTeacher, formUsernameTxt.getText(), formNameTxt.getText(), formAgeTxt.getText(), formDepartmentTxt.getText(), formPasswordTxt.getText(), genderCombox.getValue(), positionCombox.getValue());
            MsgSender.showConfirm("Successful", "The teacher record has been updated.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public static void updateTeacher(Teacher selectedTeacher, String username, String name, String age, String department, String password, String gender, String position){
        if(selectedTeacher.getUsername().equals(username))
            selectedTeacher.forceSetUsername(username);
        else
            selectedTeacher.setUsername(username);
        selectedTeacher.setName(name);
        selectedTeacher.setAge(age);
        selectedTeacher.setDepartment(department);
        selectedTeacher.setPassword(password);
        selectedTeacher.setGender(gender);
        selectedTeacher.setPosition(position);
        TeacherDatabase.getInstance().update(selectedTeacher);
    }
}
