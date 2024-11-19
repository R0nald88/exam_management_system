package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.entity.Personnel.StudentDatabase;
import comp3111.examsystem.tools.MsgSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void login(ActionEvent e) {
        try {
            Student loginStudent = StudentDatabase.getInstance().validateStudentLogin(usernameTxt.getText(), passwordTxt.getText());
            MsgSender.showConfirm("Hint", "Login Successful", () -> {
                try {
                    // Load the FXML file first
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
                    Parent root = fxmlLoader.load(); // Load the root

                    // Get the controller and set the student
                    StudentMainController studentMainController = fxmlLoader.getController();
                    studentMainController.setStudent(loginStudent);

                    // Create the new stage and set the scene
                    Stage stage = new Stage();
                    stage.setTitle("Hi " + usernameTxt.getText() + ", Welcome to HKUST Examination System");
                    stage.setScene(new Scene(root)); // Use the loaded root

                    // Close the current login window
                    ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
                    stage.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (Exception e1) {
            MsgSender.showConfirm("Login Error", e1.getMessage(), () -> {
            });
        }
    }

    @FXML
    public void register() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentRegisterUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Student Register");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
    }
}
