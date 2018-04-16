package com.sorry.GUI;

import Util.TransparencyUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JFrame{

    private BoardPanel boardPanel;
    private JLabel testPawn;
    private JButton movePawnBtn;
    private JButton drawCardBtn;

    private int stepLength = 60;
    public static int moveSteps = 1;
    public final static String pawnImagePath= "/Main/imgs/blue_pawn.jpg";

    public GameWindow() {

        //Initial the Members
        this.boardPanel = new BoardPanel();
        this.movePawnBtn = new JButton("Move");
        this.drawCardBtn = new JButton("Draw Card");
        try {
            Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+pawnImagePath));

            basicImage = TransparencyUtil.makeColorTransparent(basicImage,Color.WHITE);
            Image BoardImage = basicImage.getScaledInstance(Constants.pawnWidth, Constants.pawnHeight, Image.SCALE_SMOOTH);
            ImageIcon pawnImage = new ImageIcon(BoardImage);
            this.testPawn  = new JLabel(pawnImage);
        } catch (Exception ex) {
            // handle exception...
            System.out.println("loading image failed");
        }

        

        //Set layout
        this.setLayout(null);

        // Set the initial position of Board and Pawns
        boardPanel.setBounds(5,5,Constants.boardWidth,Constants.boardHeight);
        testPawn.setBounds(1, 1, Constants.pawnWidth, Constants.pawnHeight);
        movePawnBtn.setBounds(1000, 800, 100, 40);
        drawCardBtn.setBounds(1000, 700, 100, 40);

        testPawn.setBackground(Color.BLACK);
        testPawn.setForeground(Color.BLUE);

        this.add(boardPanel);
        boardPanel.add(testPawn);
        this.add(movePawnBtn);
        this.add(drawCardBtn);
        //Setting the window
        this.setSize(1200,1020);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      this.pack(); // pack the window
        this.setVisible(true);

        movePawnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for(int i = 0; i < moveSteps;i++) {
                    Point pt = testPawn.getLocation();

                    int x = pt.x;
                    int y = pt.y;

                    System.out.print("(" + x + ", " + y + ")");
                    if (x < 850 && y < 50) {
                        testPawn.setLocation(x + stepLength, y);
                    } else if (x > 850 && y < 850) {
                        testPawn.setLocation(x, y + stepLength );
                    } else if (x > 50 && y > 850) {
                        testPawn.setLocation(x - stepLength, y);
                    } else if (x < 50 && y > 50) {
                        testPawn.setLocation(x, y - stepLength);
                    }
                }
            }
        });
    }
}
