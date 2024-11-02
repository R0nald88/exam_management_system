package comp3111.examsystem.controller;

import org.junit.Test;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;


public class ManagerLoginControllerTest {
    @Test
    public void loginEvent1(){
        String username = "velvet";
        String password = "12345678";
        Boolean expected = true;
        Boolean actual = ManagerLoginController.checkInput(username, password);
        assertEquals(expected, actual);
    }
    @Test
    public void loginEvent2(){
        String username = "velvet";
        String password = "qwerasdf";
        Boolean expected = false;
        Boolean actual = ManagerLoginController.checkInput(username, password);
        assertEquals(expected, actual);
    }
    @Test
    public void loginEvent3(){
        String username = "velvet";
        String password = "";
        Boolean expected = false;
        Boolean actual = ManagerLoginController.checkInput(username, password);
        assertEquals(expected, actual);
    }
    @Test
    public void loginEvent4(){
        String username = "Underground";
        String password = "12345678";
        Boolean expected = false;
        Boolean actual = ManagerLoginController.checkInput(username, password);
        assertEquals(expected, actual);
    }
    @Test
    public void loginEvent5(){
        String username = "Underground";
        String password = "qwerasdf";
        Boolean expected = false;
        Boolean actual = ManagerLoginController.checkInput(username, password);
        assertEquals(expected, actual);
    }
    @Test
    public void loginEvent6(){
        String username = "Underground";
        String password = "";
        Boolean expected = false;
        Boolean actual = ManagerLoginController.checkInput(username, password);
        assertEquals(expected, actual);
    }
    @Test
    public void loginEvent7(){
        String username = "";
        String password = "12345678";
        Boolean expected = false;
        Boolean actual = ManagerLoginController.checkInput(username, password);
        assertEquals(expected, actual);
    }
    @Test
    public void loginEvent8(){
        String username = "";
        String password = "qwerasdf";
        Boolean expected = false;
        Boolean actual = ManagerLoginController.checkInput(username, password);
        assertEquals(expected, actual);
    }
    @Test
    public void loginEvent9(){
        String username = "";
        String password = "";
        Boolean expected = false;
        Boolean actual = ManagerLoginController.checkInput(username, password);
        assertEquals(expected, actual);
    }
}