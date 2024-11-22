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

/**
 * The StudentLoginController class handles the login process for students
 * in the examination system. It manages user input, validates credentials,
 * and navigates to the main student interface upon successful login.
 */
public class StudentLoginController implements Initializable {
    @FXML
    private TextField usernameTxt; // TextField for entering username
    @FXML
    private PasswordField passwordTxt; // TextField for entering password

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is called automatically after the FXML file is loaded.
     *
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object,
     *                  or null if the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Handles the login action when the login button is clicked.
     * Validates the student's login credentials and navigates to the main UI
     * upon successful authentication.
     *
     * @param e The ActionEvent triggered by the login button click.
     * @author Li Ching Ho
     */
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
                    studentMainController.setStage(stage);

                    // Close the current login window
                    ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
                    stage.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        } catch (Exception e1) {
            MsgSender.showConfirm("Login Error", e1.getMessage(), () -> {});
        }
    }

    /**
     * Opens the registration window for new students to register.
     *
     * @author Li Ching Ho
     */
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
