package com.sorry.GUI;


// import internal class of project.
import other.Card;
import other.Deck;
import utils.TransparencyUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import java class



public class GameWindow extends JFrame{


    //Singleton
    private static volatile GameWindow instance = null;
    private static Object mutex = new Object();

    //GUI components
    private BoardPanel boardPanel;
    private JLabel testPawn;
    private ArrayList<JLabel> pawns;
    private JButton movePawnBtn;
    private JButton drawCardBtn;


    //Testing Pawn movement Variables
    private int stepLength = 60;
    private int numberOfPawns = 16;
    public static int safetyZoneSize = 5;
    public static int moveSteps = 1;
    private String pawnImagePath= "/Main/imgs/";
    private String [] colorName = {"blue","yellow","green","red"};
    public static int count;
    private static JLabel seletedLabel = null;


    //Try to store the block position of board panel
    private  Map<String, Point> blockToBoardPosition;  //java.awt.point
    private Map<String, Point> pawnStartPosition;

    //some constants member
    public static Deck deck = new Deck();

    private GameWindow(){

        initWindow();
    }

    public static GameWindow getInstance(){ //Thread safe singleton model
        GameWindow result = instance;
        if(result == null){
            synchronized (mutex){
                result = instance;
                if(result == null){
                    instance = result = new GameWindow();
                }
            }
        }
        return result;
    }

    private void initWindow(){

        //Gui Components config
        initGuiComponents();
        setGuiComponentsPosition();
        addEventListenerToComponents();

        //Set layout
        this.setLayout(null);
        //Setting the window
        this.setSize(Constants.windowWidth,Constants.windowHeight);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //this.pack(); // pack the window
        this.setVisible(true);
        this.setResizable(false);


    }

    private void initGuiComponents(){
        //Initial the GUI components
        this.boardPanel = new BoardPanel();
        this.movePawnBtn = new JButton("Move");
        this.drawCardBtn = new JButton("Draw Card");
        this.pawns = new ArrayList<JLabel>();


        for(int i = 0; i < this.colorName.length;i++) {
            for(int j = 0; j < this.numberOfPawns/4; j++){
                this.pawns.add(loadPawns(this.pawnImagePath, this.colorName[i]));  //load different color pawn
            }
        }

        this.testPawn = loadPawns(this.pawnImagePath, "red");
        //Initial other variables
        blockToBoardPosition = new HashMap<String, Point>();
        initBlockToBoardPosition();
    }

    private JLabel loadPawns(String imagePath, String color){
        try {
            Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+imagePath+color+"_pawn.jpg"));
            basicImage = TransparencyUtil.makeColorTransparent(basicImage,java.awt.Color.WHITE);
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

        int x = Constants.pawnStartX;
        int y = Constants.pawnStartY;
        int numberOfBlocks = 0;
        blockToBoardPosition.put("block" + numberOfBlocks, new Point(x,y));

        for(int i = 0; i < Constants.totalBlockAroundBoard;i++) {

            //System.out.printf("blockToBoardPosition.put(\"block%d\", new Point(%d,%d));\n", numberOfBlocks, x, y);

            numberOfBlocks = (numberOfBlocks + 1)%(60);

            if (x < 850 && y < 50) {
                x = x + stepLength;
                Point curP = new Point(x, y);
                blockToBoardPosition.put("block" + numberOfBlocks, curP);
            } else if (x > 850 && y < 850) {
                y = y + stepLength;
                Point curP = new Point(x, y);
                blockToBoardPosition.put("block" + numberOfBlocks, curP);
            } else if (x > 50 && y > 850) {
                x = x - stepLength;
                Point curP = new Point(x, y);
                blockToBoardPosition.put("block" + numberOfBlocks, curP);
            } else if (x < 50 && y > 50) {
                y = y - stepLength;
                Point curP = new Point(x, y);
                blockToBoardPosition.put("block" + numberOfBlocks, curP);
            }

        }

        //Pawn Start positions
        Point [] blueStartPosition = {new Point(211 , 81),new Point(211 , 141),new Point(271 , 141),new Point(271 , 81)};
        Point [] yellowStartPosition = {new Point(761,211),new Point(761,271),new Point(821,271),new Point(821,211)};
        Point [] greenStartPosition = {new Point(691 , 821),new Point(691 , 761),new Point(631 , 761),new Point(631, 821)};
        Point [] redStartPosition = {new Point(81 , 691),new Point(81 , 631),new Point(141 , 631),new Point(141 , 691)};

        ArrayList<Point []> tempArr = new ArrayList<Point []>();
        tempArr.add(blueStartPosition);
        tempArr.add(yellowStartPosition);
        tempArr.add(greenStartPosition);
        tempArr.add(redStartPosition);

        for(int i = 0; i < tempArr.size(); i++){
            for(int j = 0; j < tempArr.get(i).length;j++)
                blockToBoardPosition.put(this.colorName[i] + "Start" + j, tempArr.get(i)[j]);
        }

        //Safety zone positions

        Point blueSZ = blockToBoardPosition.get("block2" );
        Point yellowSZ = blockToBoardPosition.get("block17" );
        Point greenSZ = blockToBoardPosition.get("block32" );
        Point redSZ = blockToBoardPosition.get("block47" );

        for(int i = 0;i < this.safetyZoneSize; i++){
            blockToBoardPosition.put("blueSafetyZone"+i, new Point(blueSZ.x,blueSZ.y + this.stepLength * (i+1)));
            blockToBoardPosition.put("yellowSafetyZone"+i, new Point(yellowSZ.x - this.stepLength * (i+1),yellowSZ.y));
            blockToBoardPosition.put("greenSafetyZone"+i, new Point(greenSZ.x,greenSZ.y - this.stepLength * (i+1)));
            blockToBoardPosition.put("redSafetyZone"+i, new Point(redSZ.x + this.stepLength * (i+1),redSZ.y));
        }

        Point endBlueSZ = blockToBoardPosition.get("blueSafetyZone4");
        Point endYellowSZ = blockToBoardPosition.get("yellowSafetyZone4");
        Point endGreenSZ = blockToBoardPosition.get("greenSafetyZone4");
        Point endRedSZ = blockToBoardPosition.get("redSafetyZone4");

        Point[] blueHomePositions =  {new Point(endBlueSZ.x - 30,endBlueSZ.y + 140),new Point(endBlueSZ.x + 30,endBlueSZ.y + 140)
                                    ,new Point(endBlueSZ.x + 30,endBlueSZ.y + 80),new Point(endBlueSZ.x - 30,endBlueSZ.y + 80)};
        Point[] yellowHomePositions =  {new Point(endYellowSZ.x - 140,endYellowSZ.y - 30),new Point(endYellowSZ.x - 140,endYellowSZ.y + 30)
                                    ,new Point(endYellowSZ.x - 80,endYellowSZ.y + 30),new Point(endYellowSZ.x - 80,endYellowSZ.y - 30)};
        Point[] greenHomePositions =  {new Point(endGreenSZ.x + 30,endGreenSZ.y - 140),new Point(endGreenSZ.x - 30,endGreenSZ.y - 140)
                                    ,new Point(endGreenSZ.x - 30,endGreenSZ.y - 80),new Point(endGreenSZ.x + 30,endGreenSZ.y - 80)};
        Point[] redHomePositions =  {new Point(endRedSZ.x + 140,endRedSZ.y - 30),new Point(endRedSZ.x + 140,endRedSZ.y + 30)
                ,new Point(endRedSZ.x + 80,endRedSZ.y + 30),new Point(endRedSZ.x + 80,endRedSZ.y - 30)};

        tempArr = new ArrayList<Point []>();
        tempArr.add(blueHomePositions);
        tempArr.add(yellowHomePositions);
        tempArr.add(greenHomePositions);
        tempArr.add(redHomePositions);

        for(int i = 0; i < tempArr.size(); i++){
            for(int j = 0; j < tempArr.get(i).length;j++)
                blockToBoardPosition.put(this.colorName[i] + "Home" + j, tempArr.get(i)[j]);
        }
//        for(Map.Entry<String, Point> entry : blockToBoardPosition.entrySet()) {
//            String key = entry.getKey();
//            Point point = entry.getValue();
//
//            // do what you have to do here
//            // In your case, another loop.
//            System.out.printf("{%s : (%d,%d)}\n",key,point.x,point.y);
//        }

    }

    private void setGuiComponentsPosition(){

        // Set the initial position of Board and Pawns
        boardPanel.setBounds(Constants.boardStartX,Constants.boardStartY,Constants.boardWidth,Constants.boardHeight); // 5,5,960,960
        this.add(boardPanel);

        testPawn.setBounds(Constants.pawnStartX, Constants.pawnStartX, Constants.pawnWidth, Constants.pawnHeight); //init the pawns position
        boardPanel.add(testPawn);

        //put pawns on start position
        for(int i = 0; i < colorName.length; i++){
            for(int j = 0; j < pawns.size()/4; j++){
                Point curP = blockToBoardPosition.get(colorName[i]+"Home"+j);
                pawns.get(i*4+j).setBounds(curP.x, curP.y, Constants.pawnWidth, Constants.pawnHeight);
                boardPanel.add(pawns.get(i*4+j));
            }
        }

//        for(int i = 0; i < pawns.size(); i++){
//            pawns.get(i).setBounds(redStartPosition[i].x, redStartPosition[i].y, Constants.pawnWidth, Constants.pawnHeight);
//            boardPanel.add(pawns.get(i));
//        }

        movePawnBtn.setBounds(1000, 800, 100, 40);
        this.add(movePawnBtn);

        drawCardBtn.setBounds(1000, 700, 100, 40);
        this.add(drawCardBtn);

    }

    private void addEventListenerToComponents(){

        movePawnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ArrayList<Point> safetyZones = new ArrayList<Point>();

                for(int j  = 0; j < colorName.length;j++){
                    for(int i  = 0; i < safetyZoneSize; i++){
                        safetyZones.add(blockToBoardPosition.get(colorName[j]+"SafetyZone" + i));
                    }
                }

                for(int i = 0; i < moveSteps;i++) {
                    Point pt = testPawn.getLocation();
                    int x = pt.x;
                    int y = pt.y;

                    testPawn.setLocation(91 , 381);
                    testPawn.setLocation(151 , 441);
//                    testPawn.setLocation(91 , 381);
                    //System.out.print("(" + x + ", " + y + ")");

//                    count = count % safetyZones.size();
//
//                    testPawn.setLocation(safetyZones.get(count).x , safetyZones.get(count).y);
//
//                    count ++;


//                    if (x < 850 && y < 50) {
//                        testPawn.setLocation(x + stepLength, y);
//                    } else if (x > 850 && y < 850) {
//                        testPawn.setLocation(x, y + stepLength );
//                    } else if (x > 50 && y > 850) {
//                        testPawn.setLocation(x - stepLength, y);
//                    } else if (x < 50 && y > 50) {
//                        testPawn.setLocation(x, y - stepLength);
//                    }
                }
            }
        });

        drawCardBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    Card curCard = deck.draw();
                    System.out.println( curCard.num);


            }
        });

        for(int i = 0; i < pawns.size(); i++){

            pawns.get(i).addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    seletedLabel = (JLabel)e.getComponent();
                    System.out.println("( "+seletedLabel.getX()+" , "+seletedLabel.getY()+" )");
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

        }

        boardPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //move the pieces to the high light point.
                System.out.println("mouse pos: "+e.getX()+" , "+e.getY());
                for (Point pos : blockToBoardPosition.values()) {
                    // ...
                    if( e.getX() - pos.x > 0 && e.getY() - pos.y > 0
                            && e.getX() - pos.x < 60 && e.getY() - pos.y < 60
                            && seletedLabel != null){
                        seletedLabel.setLocation(pos.x,pos.y);
                        System.out.println("seletedLabel pos: "+pos.x+" , "+pos.y);
                        break;
                    }
                    else{

                    }
                }


            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }



}
