module comp3111.examsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    // requires json.simple;
    requires junit;
    requires com.google.gson;

    opens comp3111.examsystem to javafx.fxml;
    exports comp3111.examsystem;
    opens comp3111.examsystem.controller to javafx.fxml;
    exports comp3111.examsystem.controller;
    exports comp3111.examsystem.entity.Questions;
    exports comp3111.examsystem.entity.Exam;
    exports comp3111.examsystem.entity.Course;
    opens comp3111.examsystem.entity to com.google.gson;
    opens comp3111.examsystem.entity.Questions to com.google.gson;
    opens comp3111.examsystem.entity.Exam to com.google.gson;
    opens comp3111.examsystem.entity.Personnel to com.google.gson;
    opens comp3111.examsystem.entity.Course to com.google.gson;
}