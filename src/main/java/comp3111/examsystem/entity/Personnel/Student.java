package comp3111.examsystem.entity.Personnel;
import java.util.HashMap;
import java.util.Map;

public class Student extends Personnel{
    private String username;
    private String name;
    private int age;
    private String gender;
    private String department;
    private String password;

    private static Map<String, Student> registeredStudents = new HashMap<>();

    public Student(String username, String name, int age, String gender, String department, String password) {
        super(username, name, age, gender, department, password);
    }


    public static void registerStudent(String username, String name, int age, String gender, String department, String password, String passwordConfirm) {
        if (username.isEmpty()) {
            System.out.println("Error: Username required. Please input a value in Username field.");
            return;
        }

        if (registeredStudents.containsKey(username)) {
            System.out.println("Error: Username occupied. Please input another username.");
            return;
        }

        for (Student student : registeredStudents.values()) {
            if (student.getName().equals(name)) {
                System.out.println("Error: Student had already registered.");
                return;
            }
        }

        if (age < 0) {
            System.out.println("Error: Age invalid. Please check your input in Age field.");
            return;
        }

        try {
            Integer.parseInt(department);
            System.out.println("Error: Department invalid. Please check your input in Department field.");
            return;
        } catch (NumberFormatException ignored) {
        }

        if (!password.equals(passwordConfirm)) {
            System.out.println("Error: Password not match. Please confirm your Password again.");
            return;
        }

        Student newStudent = new Student(username, name, age, gender, department, password);
        registeredStudents.put(username, newStudent);
        System.out.println("Student registered successfully.");
    }

    public String getName() {
        return name;
    }
}
