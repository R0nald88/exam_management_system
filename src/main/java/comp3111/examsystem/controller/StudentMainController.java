package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.entity.Exam.Exam;
import comp3111.examsystem.entity.Exam.ExamDatabase;
import comp3111.examsystem.entity.Exam.Submission;
import comp3111.examsystem.entity.Exam.SubmissionDatabase;
import comp3111.examsystem.entity.Personnel.Student;
import comp3111.examsystem.tools.MsgSender;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The StudentMainController class manages the main interface for students
 * in the examination system. It allows students to select exams, start exams,
 * and view their grade statistics.
 */
public class StudentMainController implements Initializable {
    @FXML
    private ChoiceBox<String> examCombox;
    private Student student;
    private List<Exam> examList;
    private String selectedExamName;
    private String selectedExamCourseId;

    private static List<Stage> activeStages = new ArrayList<>();
    private Stage currentStage;

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is called automatically after the FXML file is loaded.
     *
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object,
     *                  or null if the root object was not localized.
     * @author Li Ching Ho
     */
    public void initialize(URL location, ResourceBundle resources) {
        // Close all existing stages of StudentMainController

        examList = ExamDatabase.getInstance().filter(null, null, "true");
        if (!examList.isEmpty()) {
            for (Exam e: examList) {
                examCombox.getItems().add(e.getCourseId()+"-"+e.getName());
            }

            examCombox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                int selectedExamIndex = examCombox.getItems().indexOf(newValue);
                System.out.println("Selected index: " + selectedExamIndex);
                selectedExamName = examList.get(selectedExamIndex).getName();
                selectedExamCourseId = examList.get(selectedExamIndex).getCourseId();
            });

        } else System.out.println("ExamList have no exam");

    }

    /**
     * Sets the student for this controller and populates the exam selection box
     * with available exams based on the student's submissions.
     *
     * @param student The student whose information will be used.
     * @author Li Ching Ho
     */
    public void setStudent(Student student) {
        this.student = student;
        List<Submission> studentSubmissionList = SubmissionDatabase.getInstance().filter(student.getUsername(),null, null);
        if (!studentSubmissionList.isEmpty()) {
            System.out.println(studentSubmissionList);
            examCombox.getItems().clear();
            System.out.println("After clear in setStudent in student main, examList: " + examCombox.getItems());
            examList = ExamDatabase.getInstance().filter(null, null, "true");

            if (!examList.isEmpty()) {
                List<Exam> removedExam = new ArrayList<>();
                for (Exam e : examList) {
                    for (Submission s : studentSubmissionList) {
                        System.out.println("Comparing " + e.getId() + " and " + s.getExamId() + "... Decision: " + !Objects.equals(e.getId(), s.getExamId()));
                        if (Objects.equals(e.getId(), s.getExamId())) {
                            removedExam.add(e);
                        }
                    }
                }
                examList.removeAll(removedExam);
                //System.out.println("After adding exams, examList: " + examList);
                for (Exam e : examList) {
                    examCombox.getItems().add(e.getCourseId() + "-" + e.getName());
                }

                examCombox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    int selectedExamIndex = examCombox.getItems().indexOf(newValue);
                    selectedExamName = examList.get(selectedExamIndex).getName();
                    selectedExamCourseId = examList.get(selectedExamIndex).getCourseId();
                    //System.out.println("Selected index: " + selectedExamIndex + "selectedExamName: " + selectedExamName + " selectedExamCourseId: " + selectedExamCourseId);
                });

            } else System.out.println("ExamList have no exam");
        }
    }

    /**
     * Sets the current stage for this controller and manages active stages.
     *
     * @param stage The current stage to be set for this controller.
     * @author Li Ching Ho
     */
    @FXML
    public void setStage(Stage stage) {
        this.currentStage = stage; // Store the reference to the current stage

        // Close all existing stages of StudentMainController
        for (Stage existingStage : new ArrayList<>(activeStages)) {
            if (existingStage.isShowing()) {
                existingStage.close();
            }
        }

        // Add the current stage to the list of active stages
        activeStages.add(currentStage);

        // Add a listener to remove the stage from activeStages when it is closed
        currentStage.setOnCloseRequest(event -> {
            activeStages.remove(currentStage);
        });
    }

    /**
     * Opens the exam user interface for the selected exam.
     *
     * @param e The ActionEvent triggered when the user wants to start the exam.
     * @author Li Ching Ho
     */
    @FXML
    public void openExamUI(ActionEvent e) {
        if (examCombox.getValue() != null) {
            //System.out.println(ExamDatabase.getInstance().filter(selectedExamName, selectedExamCourseId, "true"));
            Exam selectedExam = ExamDatabase.getInstance().filter(selectedExamName, selectedExamCourseId, "true").getFirst();
            Submission submission = new Submission();
            submission.setStudentUsername(student.getUsername());
            submission.setExamId(selectedExam.getId());

            // Check if the controller instance already exists
            StudentStartExamController existingController = StudentStartExamController.getInstance();

            if (existingController != null) {
                // Prompt the user about the ongoing exam
                MsgSender.showConfirm("You have an exam ongoing",
                        "You have an exam ongoing: " + existingController.getExam().getCourseId() + "-" + existingController.getExam().getName() +
                                "\nYou can only finish and submit that exam before conducting another one.\nDo you want to continue doing that exam?",
                        () -> {
                            // User pressed confirm, return to the existing exam scene
                            Stage stage = (Stage) existingController.getRoot().getScene().getWindow();
                            stage.show();
                        });
            } else {
                // If no ongoing exam, proceed to load the new exam UI
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentStartExamUI.fxml"));
                    Parent root = fxmlLoader.load();
                    StudentStartExamController newController = fxmlLoader.getController();
                    newController.setRoot(root);
                    newController.setSubmission(submission);
                    System.out.println("openSubmissionUI, student username: " + submission.getStudentUsername());

                    // Save the new instance for future use
                    StudentStartExamController.setInstance(newController);

                    Stage stage = new Stage();
                    stage.setTitle("Start Exam");
                    stage.setScene(new Scene(root));

                    stage.show();
                } catch (IOException e1) {
                    MsgSender.showConfirm("Main Open Exam UI Error", e1.getMessage(), () -> {});
                }
            }
        }
        else { // examCombox.getValue() == null
            try {
                throw new RuntimeException("No Exam selected in Exam choice box");
            } catch (RuntimeException ex) {
                MsgSender.showConfirm("No exam selected", ex.getMessage(), () -> {});
            }
        }
    }

    /**
     * Opens the grade statistics interface for the student.
     *
     * @param e The ActionEvent triggered when the user wants to view grade statistics.
     * @author Li Ching Ho
     */
    @FXML
    public void openGradeStatistic(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentGradeStatisticUI.fxml"));

            Parent root = fxmlLoader.load();
            StudentGradeStatisticController studentGradeStatisticController = fxmlLoader.getController();
            System.out.println(student.getUsername());
            studentGradeStatisticController.setStudent(student);

            Stage stage = new Stage();
            stage.setTitle("Grade Statistics");
            stage.setScene(new Scene(root));

            stage.show();

        } catch ( IOException e1) {
            MsgSender.showConfirm("Open Grade Statistics Error", e1.getMessage(), () -> {});
            //e1.printStackTrace();
        }
    }

    /**
     * Exits the application.
     */
    @FXML
    public void exit() {
        System.exit(0);
    }
}
