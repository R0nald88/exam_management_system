package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.tools.MsgSender;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for main page of manager.
 * @author Wan Hanzhe
 */
public class ManagerMainController implements Initializable {
    @FXML
    private VBox mainbox;

    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Direct user to the student management system when StudentManagement button is pressed.
     */
    @FXML
    public void openStudentManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentRecord.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Student Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    /**
     * Direct user to the teacher management system when TeacherManagement button is pressed.
     */
    @FXML
    public void openTeacherManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherRecord.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Teacher Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    /**
     * Direct user to the course management system when CourseManagement button is pressed.
     */
    @FXML
    public void openCourseManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CourseRecord.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Course Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            MsgSender.showConfirm("Error", e.getMessage(), ()->{});
        }
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}
