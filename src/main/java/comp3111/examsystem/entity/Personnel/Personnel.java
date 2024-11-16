package comp3111.examsystem.entity.Personnel;
import comp3111.examsystem.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Personnel extends Entity {
    private String username;
    private String name;
    private int age;
    private Gender gender;
    private String department;
    private String password;

    public Personnel(String username, String name, int age, String gender, String department, String password) {
        this.username = username;
        this.name = name;
        this.age = age;
        this.gender = Gender.toGender(gender);
        this.department = department;
        this.password = password;
    }

    public Personnel() {
        super(System.currentTimeMillis());
    }

    private static Map<String, Personnel> registeredPersonnels = new HashMap<>();

    public static boolean register(String username, String name, int age, String gender, String department, String password, String passwordConfirm) {
        List<String> errorMessages;
        errorMessages = new ArrayList<>();

        if (username.isEmpty()) {
            errorMessages.add("Error: Username required. Please input a value in Username field.");
        }

        if (registeredPersonnels.containsKey(username)) {
            errorMessages.add("Error: Username occupied. Please input another username.");
        }

        for (Personnel personnel : registeredPersonnels.values()) {
            if (personnel.getName().equals(name)
             && personnel.getAge() == age
             && personnel.getGender().equals(gender)
             && personnel.getDepartment().equals(department)) {
                errorMessages.add("Error: Student had already registered.");
            }
        }

        if (age < 0) {
            errorMessages.add("Error: Age invalid. Please check your input in Age field.");
        }

        try {
            Integer.parseInt(department);
            errorMessages.add("Error: Department invalid. Please check your input in Department field.");
        } catch (NumberFormatException ignored) {
        }

        if (!password.equals(passwordConfirm)) {
            errorMessages.add("Error: Password does not match. Please confirm your Password again.");
        }

        if (errorMessages.isEmpty()) {
            Personnel newPersonnel = new Personnel(username, name, age, gender, department, password);
            registeredPersonnels.put(username, newPersonnel);
        }

        if (errorMessages.isEmpty()) {
            return true;
        } else return false;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = Gender.toGender(gender);
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
