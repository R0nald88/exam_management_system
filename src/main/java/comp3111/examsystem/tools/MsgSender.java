package comp3111.examsystem.tools;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Utility class for popping up dialog showing message in the UI
 */
public class MsgSender {
    /**
     * Show an information dialog prompting the message
     * @param msg Message to be shown
     */
    static public void showMsg(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("Hint");
        alert.headerTextProperty().set(msg);
        alert.showAndWait();
    }

    /**
     * Show an confirmation dialog prompting the message, with callback after user confirm the message
     * @param msg Message to be shown
     * @param callback Function to be called after user clicked "OK" button
     * @param title Title of the dialog
     */
    static public void showConfirm(String title, String msg, Runnable callback) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(msg);
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            callback.run();
        }
    }
}
