package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.entity.Personnel.Gender;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Personnel.StudentDatabase;
import comp3111.examsystem.tools.MsgSender;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
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
    private TextField usernameTxt, ageTxt, departmentTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private ChoiceBox<String> genderCombox;
    @FXML
    private PasswordField passwordTxt, passwordConfirmTxt;

    @FXML
    private Label hintLabel;


    public void initialize(URL location, ResourceBundle resources) {
        genderCombox.getItems().addAll(Gender.MALE.getName(), Gender.FEMALE.getName());
        hintLabel.setText(
                "Password should contain:\n" +
                        "1. At least 1 special character from \"!@$%^&*_+<>/-\"\n" +
                        "2. At least 1 number from 0 to 9\n" +
                        "3. At least 1 small letter from a to z\n" +
                        "4. At least 1 capital letter from A to Z\n" +
                        "5. No blank space"
        );
    }

    @FXML
    public void close(ActionEvent e) {
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
