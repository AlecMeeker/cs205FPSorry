package com.sorry.GUI;

//import java class
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// import internal class of project.
import Util.TransparencyUtil;

public class GameWindow extends JFrame{

    private BoardPanel boardPanel;
    private JLabel testPawn;
    private ArrayList<JLabel> pawns;
    private JButton movePawnBtn;
    private JButton drawCardBtn;

    //Testing Pawn movement Variables
    private int stepLength = 60;
    public static int moveSteps = 1;
    public final static String pawnImagePath= "/Main/imgs/";

    //Try to store the block position of board panel
    private Map<String, Point> blockToBoardPosition;  //java.awt.point
    private Map<String, Point> pawnStartPosition;
    private int numberOfBlocks ;

    public GameWindow() {

        initGuiComponents();
        setGuiComponentsPosition();
        addEventListenerToComponents();
    }

    private void initGuiComponents(){
        //Initial the GUI components
        this.boardPanel = new BoardPanel();
        this.movePawnBtn = new JButton("Move");
        this.drawCardBtn = new JButton("Draw Card");
        this.pawns = new ArrayList<JLabel>();
        for(int i = 0; i < 4;i++) {
            this.pawns.add(loadPawns(pawnImagePath, "blue"));  //load different color pawn
        }
        this.testPawn = loadPawns(pawnImagePath, "blue");
        //Initial other variables
        blockToBoardPosition = new HashMap<String, Point>();
        numberOfBlocks = 0;
    }

    private JLabel loadPawns(String imagePath, String color){
        try {
            Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+imagePath+color+"_pawn.jpg"));
            basicImage = TransparencyUtil.makeColorTransparent(basicImage,Color.WHITE);
            Image BoardImage = basicImage.getScaledInstance(Constants.pawnWidth, Constants.pawnHeight, Image.SCALE_SMOOTH);
            ImageIcon pawnImage = new ImageIcon(BoardImage);

            return new JLabel(pawnImage);
        } catch (Exception ex) {
            // handle exception...
            System.out.println("loadPawns failed \n" + ex.toString());
            return new JLabel();
        }
    }

    private void initBlockToBoardPosition(){

    }

    private void setGuiComponentsPosition(){

        //Set layout
        this.setLayout(null);
        //Setting the window
        this.setSize(1200,1020);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.pack(); // pack the window
        this.setVisible(true);

        // Set the initial position of Board and Pawns
        boardPanel.setBounds(Constants.boardStartX,Constants.boardStartY,Constants.boardWidth,Constants.boardHeight); // 5,5,960,960
        this.add(boardPanel);

        testPawn.setBounds(1, 1, Constants.pawnWidth, Constants.pawnHeight); //init the pawns position
        //boardPanel.add(testPawn);
        //test put pawn on start code

        Point [] blueStartPosition = {new Point(211 , 81),new Point(211 , 141),new Point(271 , 81),new Point(271 , 141)};
        for(int i = 0; i < pawns.size(); i++){
            pawns.get(i).setBounds(blueStartPosition[i].x, blueStartPosition[i].y, Constants.pawnWidth, Constants.pawnHeight);
            boardPanel.add(pawns.get(i));
        }

        movePawnBtn.setBounds(1000, 800, 100, 40);
        this.add(movePawnBtn);

        drawCardBtn.setBounds(1000, 700, 100, 40);
        this.add(drawCardBtn);



    }

    private void addEventListenerToComponents(){

        movePawnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for(int i = 0; i < moveSteps;i++) {
                    Point pt = testPawn.getLocation();
                    int x = pt.x;
                    int y = pt.y;

//                    testPawn.setLocation(211 , 81);
//                    testPawn.setLocation(211 , 141);
//                    testPawn.setLocation(271 , 81);
//                    testPawn.setLocation(271 , 141);
                    //System.out.print("(" + x + ", " + y + ")");

                    blockToBoardPosition.put("block" + numberOfBlocks, pt);
                    System.out.printf("blockToBoardPosition.put(\"block%d\", new Point(%d,%d));\n", numberOfBlocks, x, y);

                    numberOfBlocks = (numberOfBlocks + 1)%(60);

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
