package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Personnel.*;
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
    }

    public void reset(){
        usernameTxt.setText("");
        nameTxt.setText("");
        departmentTxt.setText("");
    }

    public void filter(){
        try{
            List<Teacher> teachers = new ArrayList<>();
            List<Teacher> usernameFilterTeachers = new ArrayList<>();
            List<Teacher> nameFilterTeachers = new ArrayList<>();
            List<Teacher> departmentFilterTeachers = new ArrayList<>();
            if(usernameTxt.getText().isEmpty())
                usernameFilterTeachers = TeacherDatabase.getInstance().getAll();
            else
                usernameFilterTeachers = TeacherDatabase.getInstance().queryFuzzyByField("username", usernameTxt.getText());
            if(nameTxt.getText().isEmpty())
                nameFilterTeachers = TeacherDatabase.getInstance().getAll();
            else
                nameFilterTeachers = TeacherDatabase.getInstance().queryFuzzyByField("name", nameTxt.getText());
            if(departmentTxt.getText().isEmpty())
                departmentFilterTeachers = TeacherDatabase.getInstance().getAll();
            else
                departmentFilterTeachers = TeacherDatabase.getInstance().queryFuzzyByField("department", departmentTxt.getText());
            teachers = TeacherDatabase.getInstance().join(TeacherDatabase.getInstance().join(usernameFilterTeachers, nameFilterTeachers), departmentFilterTeachers);
            recordTable.getItems().setAll(teachers);
        } catch (Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public void delete(){
        try{
            Teacher selectedTeacher = recordTable.getSelectionModel().getSelectedItem();
            TeacherDatabase.getInstance().delByFiled("username", selectedTeacher.getUsername());
            MsgSender.showConfirm("Successful", "The teacher record has been deleted.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public void refresh(){
        formUsernameTxt.setText("");
        formNameTxt.setText("");
        formAgeTxt.setText("");
        formDepartmentTxt.setText("");
        formPasswordTxt.setText("");
        genderCombox.getSelectionModel().selectFirst();
        positionCombox.getSelectionModel().selectFirst();

        reset();
        filter();
    }

    public void add(){
        try{
            Teacher teacher = new Teacher();
            teacher.setUsername(formUsernameTxt.getText());
            teacher.setName(formNameTxt.getText());
            teacher.setAge(formAgeTxt.getText());
            teacher.setDepartment(formDepartmentTxt.getText());
            teacher.setPassword(formPasswordTxt.getText());
            teacher.setGender(genderCombox.getValue());
            teacher.setPosition(positionCombox.getValue());
            TeacherDatabase.getInstance().add(teacher);
            MsgSender.showConfirm("Successful", "A new teacher record has been created.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    public void update(){
        try{
            Teacher selectedTeacher = recordTable.getSelectionModel().getSelectedItem();
            if(selectedTeacher.getUsername().equals(formUsernameTxt.getText()))
                selectedTeacher.forceSetUsername(formUsernameTxt.getText());
            else
                selectedTeacher.setUsername(formUsernameTxt.getText());
            selectedTeacher.setName(formNameTxt.getText());
            selectedTeacher.setAge(formAgeTxt.getText());
            selectedTeacher.setDepartment(formDepartmentTxt.getText());
            selectedTeacher.setPassword(formPasswordTxt.getText());
            selectedTeacher.setGender(genderCombox.getValue());
            selectedTeacher.setPosition(positionCombox.getValue());
            TeacherDatabase.getInstance().update(selectedTeacher);
            MsgSender.showConfirm("Successful", "The teacher record has been updated.", this::refresh);
        } catch(Exception e){
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }
}
