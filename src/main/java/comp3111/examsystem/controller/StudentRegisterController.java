package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
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
    private ComboBox<String> genderSelect;
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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentRegisterUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Student Register");
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }
}
