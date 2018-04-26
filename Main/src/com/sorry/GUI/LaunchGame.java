package com.sorry.GUI;
public class LaunchGame {

    public static void main(String[] args){

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        StartWindow startWindow = StartWindow.getInstance();
        startWindow.setVisible(true);
        GameWindow gw = GameWindow.getInstance();
//        gw.setVisible(true);

//        Game newGame = new Game();
//
//        newGame.playGame();

    }
}
