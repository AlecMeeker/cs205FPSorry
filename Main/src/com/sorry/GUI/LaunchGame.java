package com.sorry.GUI;

import javax.swing.*;
import java.awt.*;

public class LaunchGame {

    public static void main(String[] args){

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        GameWindow gameWindow= new GameWindow();
        gameWindow.setResizable(false); //set the gameWindow as fixed szie.
    }
}
