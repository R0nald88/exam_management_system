package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Personnel.StudentDatabase;
import comp3111.examsystem.tools.MsgSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentRegisterController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private ChoiceBox<String> genderCombox;
    @FXML
    private TextField ageTxt;
    @FXML
    private TextField departmentTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private PasswordField passwordConfirmTxt;


    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void close(ActionEvent e) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Hi " + usernameTxt.getText() +", Welcome to HKUST Examination System");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public void register(ActionEvent e) {
        try {
            Student student = getStudent();
            StudentDatabase.getInstance().registerStudent(student);
            MsgSender.showConfirm("Hint", "Student Register successful.", () -> close(e));
        } catch (Exception e1) {
            MsgSender.showConfirm("Registration Error", e1.getMessage(), () -> {});
        }

        close(e);
    }

    private Student getStudent() {
        Student student = new Student();
        student.setUsername(usernameTxt.getText());
        student.setName(nameTxt.getText());
        student.setGender(genderCombox.getSelectionModel().getSelectedItem());
        student.setAge(ageTxt.getText());
        student.setDepartment(departmentTxt.getText());
        student.setPassword(passwordTxt.getText());
        student.confirmPassword(passwordConfirmTxt.getText());
        return student;
    }
}
