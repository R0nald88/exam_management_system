package comp3111.examsystem.entity.Personnel;
import java.util.HashMap;
import java.util.Map;

public class Personnel {
    private String username;
    private String name;
    private int age;
    private String gender;
    private String department;
    private String password;

    public Personnel(String username, String name, int age, String gender, String department, String password) {
        this.username = username;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.department = department;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
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
        this.gender = gender;
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
