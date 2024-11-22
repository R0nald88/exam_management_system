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

/**
 * The StudentRegisterController class handles the registration process for students
 * in the examination system. It manages user input for student details and performs
 * validation before registering the student in the database.
 */
public class StudentRegisterController implements Initializable {
    @FXML
    private TextField usernameTxt, ageTxt, departmentTxt; // TextField for entering the username, age, department
    @FXML
    private TextField nameTxt; // TextField for entering the student's name
    @FXML
    private ChoiceBox<String> genderCombox; // ChoiceBox for selecting the student's gender
    @FXML
    private PasswordField passwordTxt, passwordConfirmTxt; // PasswordField for setting student login password and confirming the password
    @FXML
    private Label hintLabel; // Label for displaying hint for password requirement

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method sets up the gender selection and displays password requirements.
     *
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object,
     *                  or null if the root object was not localized.
     * @author Li Ching Ho
     */
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

    /**
     * Closes the current registration window.
     *
     * @param e The ActionEvent triggered by the close button.
     */
    @FXML
    public void close(ActionEvent e) {
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    /**
     * Handles the registration action when the register button is clicked.
     * It gathers the student information, validates it, and registers the student
     * in the database. Displays a confirmation or error message accordingly.
     *
     * @param e The ActionEvent triggered by the register button.
     * @author Li Ching Ho
     */
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

    /**
     * Retrieves the student information from the input fields and creates a Student object.
     *
     * @return A Student object containing the entered student details.
     * @author Li Ching Ho
     */
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
