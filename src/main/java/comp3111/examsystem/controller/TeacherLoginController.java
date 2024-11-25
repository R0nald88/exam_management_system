package comp3111.examsystem.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import comp3111.examsystem.Main;
import comp3111.examsystem.entity.Personnel.TeacherDatabase;
import comp3111.examsystem.tools.MsgSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TeacherLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Login the teacher
     * @param e Action event of Login button
     */
    @FXML
    public void login(ActionEvent e) {
        try {
            TeacherDatabase.getInstance().validateTeacherLogin(usernameTxt.getText(), passwordTxt.getText());
            MsgSender.showMsg("Login Successfully", "Successful Login", () -> {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherMainUI.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Hi " + usernameTxt.getText() +", Welcome to HKUST Examination System");
                try {
                    stage.setScene(new Scene(fxmlLoader.load()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
                stage.show();
            });
        } catch (Exception e1) {
            MsgSender.showConfirm("Login Error", e1.getMessage(), () -> {});
        }

    }

    /**
     * Open Teacher Registration Page
     * @author Cheung Tuen King
     */
    @FXML
    public void register() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherRegisterUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Teacher Register");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
    }
}
