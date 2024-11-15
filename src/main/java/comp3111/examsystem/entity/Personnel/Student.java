package comp3111.examsystem.entity.Personnel;

import comp3111.examsystem.entity.Personnel.Personnel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Student extends Personnel {

    public Student(String username, String name, int age, String gender, String department, String password, String passwordConfirm) {
        super(username, name, age, gender, department, password);
    }
}