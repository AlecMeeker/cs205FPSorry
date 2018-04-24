package GUI;

import javax.swing.*;
import java.awt.*;

import static Logic.Game.playGame;

public class LaunchGame {

    public static void main(String[] args){

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        GameWindow gameWindow= new GameWindow();

        playGame();

    }
}
