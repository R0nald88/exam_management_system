package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.tools.MsgSender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Controller class for manager login.
 * @author Wan Hanzhe
 */
public class ManagerLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    private static boolean testing = false;

    public void initialize(URL location, ResourceBundle resources) {
        //createRecord("admin", "12345678");
    }

    /**
     * Create an admin record, unused.
     * @param username The username of the manager.
     * @param password The password of the manager.
     */
    public void createRecord(String username, String password){
        try{
            FileWriter myWriter = new FileWriter("src/main/resources/database/manager.txt",true);
            myWriter.write(username + " " + password + "\n");
            myWriter.close();
        } catch (Exception e1){
            MsgSender.showConfirm("Error", e1.getMessage(), ()->{});
        }
    }

    /**
     * Verify manager login.
     * @param username The input username.
     * @param password The input password.
     * @return true if both username and password are matched, false otherwise.
     */
    public static boolean verify(String username, String password){
        try{
            File myObj = new File("src/main/resources/database/manager.txt");
            Scanner myReader = new Scanner(myObj);
            while(!testing){
                String data = myReader.nextLine();
                StringTokenizer st = new StringTokenizer(data);
                if(username.equals(st.nextToken()) && password.equals(st.nextToken())){
                    break;
                }
                if(!myReader.hasNextLine()){
                    return false;
                }
            }
        } catch (Exception e2){
            MsgSender.showConfirm("Error", e2.getMessage(), ()->{});
        }
        return true;
    }

    /**
     * This method is called when the user click the login button.
     * The user will be directed to main page if login successful.
     * @param e The Action Event.
     */
    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();
        boolean verified = verify(username, password);
        if(verified){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Welcome!");
            alert.setHeaderText("Login successfully!");
            alert.setContentText("Hello, " + usernameTxt.getText() + "!");
            alert.showAndWait().ifPresent(response -> {
                if(response == ButtonType.OK){
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ManagerMainUI.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Hi " + usernameTxt.getText() +", Welcome to HKUST Examination System");
                    try {
                        stage.setScene(new Scene(fxmlLoader.load()));
                    } catch (Exception e3) {
                        MsgSender.showConfirm("Error", e3.getMessage(), ()->{});
                    }
                    stage.show();
                    ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
                }
            });
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Invalid username or password!");
            alert.setContentText("Please retry.");
            alert.show();
        }

    }

}
