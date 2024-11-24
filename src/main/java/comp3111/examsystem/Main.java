package comp3111.examsystem;

import comp3111.examsystem.tools.MsgSender;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Main class to start the first page of the system
 */
public class Main extends Application {
	/**
	 * Open the LoginUI as the first page of the system
	 */
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginUI.fxml"));
			Scene scene = new Scene(fxmlLoader.load(), 640, 480);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			MsgSender.showMsg(e.getMessage());
		}
	}

	/**
	 * Main method to start the program
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
