module comp3111.examsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    // requires json.simple;
    requires junit;


    opens comp3111.examsystem to javafx.fxml;
    exports comp3111.examsystem;
    opens comp3111.examsystem.controller to javafx.fxml;
    exports comp3111.examsystem.controller;
    exports comp3111.examsystem.entity.Questions;
    exports comp3111.examsystem.entity.Exam;
}