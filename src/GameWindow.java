package com.sorry.GUI;

import javax.swing.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame{

    private BoardPanel boardPanel;
    private JLabel testPawn;
    private JButton movePawn;

    public static int moveSteps = 1;


    public GameWindow(){

        //Initial the Members
        this.boardPanel = new BoardPanel();
        this.testPawn = new JLabel("Pawn");
        this.movePawn = new JButton("Move");

        //Set layout
        this.setLayout(null);

        boardPanel.setBounds(0,0,950,950);
        testPawn.setBounds(20, 20, 50, 50);
        movePawn.setBounds(1000, 800, 100, 40);

        testPawn.setBackground(Color.BLACK);
        testPawn.setForeground(Color.BLUE);

        this.add(boardPanel);
        boardPanel.add(testPawn);
        this.add(movePawn);
        //Setting the window
        this.setSize(1200,1000);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      this.pack();
        this.setVisible(true);

        movePawn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point pt = testPawn.getLocation();

                int x = pt.x;
                int y = pt.y;

                testPawn.setLocation(x+10 * moveSteps,y);

            }
        });


    }





}
