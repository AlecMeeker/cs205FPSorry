package GUI;

import Logic.Game;

import javax.swing.*;
import java.awt.*;


public class LaunchGame {

    public static void main(String[] args){

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        GameWindow gameWindow= new GameWindow();
        Game newGame = new Game();

        newGame.playGame();

    }
}
