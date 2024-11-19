package comp3111.examsystem.controller;

import comp3111.examsystem.entity.Personnel.Gender;
import comp3111.examsystem.entity.Personnel.Position;
import comp3111.examsystem.entity.Personnel.Teacher;
import comp3111.examsystem.entity.Personnel.TeacherDatabase;
import comp3111.examsystem.tools.MsgSender;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class TeacherRegisterUIController implements Initializable {
    @FXML private TextField usernameTxt;
    @FXML private TextField nameTxt;
    @FXML private ChoiceBox<String> genderCombox;
    @FXML private TextField ageTxt;
    @FXML private ChoiceBox<String> positionCombox;
    @FXML private TextField deptTxt;
    @FXML private PasswordField pwdTxt;
    @FXML private PasswordField confirmedPwdTxt;
    @FXML private Label hintLabel;

    public void close(ActionEvent e) {
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    public void register(ActionEvent actionEvent) {
        try {
            Teacher teacher = getTeacher();
            TeacherDatabase.getInstance().registerTeacher(teacher);
            MsgSender.showConfirm("Successful Registration", "Teacher registered successfully.", () -> close(actionEvent));
        } catch (Exception e) {
            MsgSender.showConfirm("Registration Error", e.getMessage(), () -> {});
        }
    }

    private Teacher getTeacher() {
        Teacher teacher = new Teacher();
        teacher.setUsername(usernameTxt.getText());
        teacher.setName(nameTxt.getText());
        teacher.setAge(ageTxt.getText());
        teacher.setDepartment(deptTxt.getText());
        teacher.setPassword(pwdTxt.getText());
        teacher.setPosition(positionCombox.getSelectionModel().getSelectedItem());
        teacher.setGender(genderCombox.getSelectionModel().getSelectedItem());
        teacher.confirmPassword(confirmedPwdTxt.getText());
        return teacher;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderCombox.setItems(FXCollections.observableList(Arrays.stream(Gender.values()).map(Gender::getName).toList()));
        positionCombox.setItems(FXCollections.observableList(Arrays.stream(Position.values()).map(Position::getName).toList()));
        genderCombox.getSelectionModel().selectFirst();
        positionCombox.getSelectionModel().selectFirst();
        hintLabel.setText(
                "Password should contain:\n" +
                "1. At least 1 special character from \"!@$%^&*_+<>/-\"\n" +
                "2. At least 1 number from 0 to 9\n" +
                "3. At least 1 small letter from a to z\n" +
                "4. At least 1 capital letter from A to Z\n" +
                "5. No blank space"
        );
    }
}
