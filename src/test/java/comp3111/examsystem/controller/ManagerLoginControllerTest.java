package comp3111.examsystem.controller;

import org.junit.Test;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;


public class ManagerLoginControllerTest {

    @Test
    public void verify1(){
        String input1 = "admin";
        String input2 = "12345678";
        boolean expected = true;
        boolean actual = ManagerLoginController.verify(input1, input2);
        assertEquals(expected, actual);
    }

    @Test
    public void verify2(){
        String input1 = "velvet";
        String input2 = "12345678";
        boolean expected = false;
        boolean actual = ManagerLoginController.verify(input1, input2);
        assertEquals(expected, actual);
    }

    @Test
    public void verify3(){
        String input1 = "admin";
        String input2 = "underground";
        boolean expected = false;
        boolean actual = ManagerLoginController.verify(input1, input2);
        assertEquals(expected, actual);
    }

    @Test
    public void verify4(){
        String input1 = "velvet";
        String input2 = "underground";
        boolean expected = false;
        boolean actual = ManagerLoginController.verify(input1, input2);
        assertEquals(expected, actual);
    }

}