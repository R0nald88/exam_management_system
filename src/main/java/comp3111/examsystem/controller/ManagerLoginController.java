package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
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

public class ManagerLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    public void initialize(URL location, ResourceBundle resources) {
        createRecord("velvet", "12345678");
    }

    public void createRecord(String username, String password){
        try{
            FileWriter myWriter = new FileWriter("src/main/resources/database/ManagerCredentials.txt",true);
            myWriter.write(username + " " + password + "\n");
            myWriter.close();
        } catch (IOException e1){
            e1.printStackTrace();
        }
    }

    @FXML
    public void login(ActionEvent e) {
        String username = usernameTxt.getText();
        String password = passwordTxt.getText();
        try{
            File myObj = new File("src/main/resources/database/ManagerCredentials.txt");
            Scanner myReader = new Scanner(myObj);
            while(true){
                String data = myReader.nextLine();
                StringTokenizer st = new StringTokenizer(data);
                if(username.equals(st.nextToken()) && password.equals(st.nextToken()))
                    break;
                if(!myReader.hasNextLine()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Invalid username or password!");
                    alert.setContentText("Please retry.");
                    alert.show();
                    return;
                }
            }
        } catch (FileNotFoundException e2){
            e2.printStackTrace();
        }

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
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                stage.show();
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            }
        });
    }

}
