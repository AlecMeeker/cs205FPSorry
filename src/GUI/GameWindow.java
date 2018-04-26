package GUI;


// import internal class of project.
import Logic.Board;
import Logic.Card;
import Logic.Deck;
import Logic.Block;
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
    private JLabel drawedCard;
    private ArrayList<JLabel> numOfPawnsOnStart;
    private ArrayList<JLabel> numOfPawnsOnHome;

    //Testing Pawn movement Variables
    private int stepLength = 60;
    private int numberOfPawns = 16;
    public static int safetyZoneSize = 5;
    public static int moveSteps = 1;
    private String ImagePath= "/src/imgs/";
    private String [] colorName = {"red","blue","yellow","green"};
    public static int count;
    private static JLabel seletedLabel = null;

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    private int numOfPlayers;

    //BackEnd stuff.
    private Board board;
    private Card curCard;
    private ArrayList<Block> allBlocks;
    //Try to store the block position of board panel
    private Map<String, Point> blockToBoardPosition;  //java.awt.point
    private Map<Block, Point> boardToBackend;

    //some constants member
    public static Deck deck = new Deck();
    private StartWindow startWindow = StartWindow.getInstance();

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

        //initBackEnd;
        initBackEnd();

        //add motion
        addEventListenerToComponents();

        //Set layout
        this.setLayout(null);
        //Setting the window
        this.setSize(Constants.windowWidth,Constants.windowHeight);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                startWindow.setVisible(true);
            }
            @Override
            public void windowActivated(java.awt.event.WindowEvent windowEvent){
                startWindow.setVisible(false);
            }
        });

        //this.pack(); // pack the window
        this.setVisible(true);
        this.setResizable(false);

    }

    private void initGuiComponents(){
        //Initial the GUI components
        this.boardPanel = new BoardPanel();
        this.movePawnBtn = new JButton("Move");
        this.drawCardBtn = new JButton("Draw Card");
        this.pawns = new ArrayList<>();
        numOfPawnsOnStart = new ArrayList<>();
        numOfPawnsOnHome = new ArrayList<>();

        for(int i = 0; i < this.colorName.length;i++) {
            for(int j = 0; j < this.numberOfPawns/4; j++){
                this.pawns.add(loadPawns(this.ImagePath, this.colorName[i]));  //load different color pawn
            }
            numOfPawnsOnStart.add(new JLabel());
            numOfPawnsOnHome.add(new JLabel());
        }
        for(int i = 0; i < this.colorName.length;i++) {

        }
        this.testPawn = loadPawns(this.ImagePath, "red");
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
        Point  blueStartPosition = new Point(241 , 111);
        Point yellowStartPosition = new Point(791,241);
        Point greenStartPosition = new Point(661 , 791);
        Point redStartPosition = new Point(111 , 661);

        ArrayList<Point> tempArr = new ArrayList<>();
        tempArr.add(redStartPosition);
        tempArr.add(blueStartPosition);
        tempArr.add(yellowStartPosition);
        tempArr.add(greenStartPosition);

        for(int i = 0; i < tempArr.size(); i++){
            blockToBoardPosition.put(this.colorName[i] + "Start", tempArr.get(i));
        }

        //Safety zone positions
        Point redSZ = blockToBoardPosition.get("block2" );
        Point blueSZ = blockToBoardPosition.get("block17" );
        Point yellowSZ = blockToBoardPosition.get("block32" );
        Point greenSZ = blockToBoardPosition.get("block47" );


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

        Point blueHomePositions =  new Point(endBlueSZ.x,endBlueSZ.y + 110);
        Point yellowHomePositions =  new Point(endYellowSZ.x - 110,endYellowSZ.y);
        Point greenHomePositions =  new Point(endGreenSZ.x ,endGreenSZ.y - 110);
        Point redHomePositions =  new Point(endRedSZ.x + 110,endRedSZ.y );

        blockToBoardPosition.put("blueSafetyZone5", blueHomePositions);
        blockToBoardPosition.put("yellowSafetyZone5", yellowHomePositions);
        blockToBoardPosition.put("greenSafetyZone5", greenHomePositions);
        blockToBoardPosition.put("redSafetyZone5", redHomePositions);

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

        testPawn.setBounds(Constants.pawnStartX, Constants.pawnStartY, Constants.pawnWidth, Constants.pawnHeight); //init the pawns position
        boardPanel.add(testPawn);

        //put pawns on start position
        for(int i = 0; i < colorName.length; i++){
            for(int j = 0; j < pawns.size()/4; j++){
                Point curP = blockToBoardPosition.get(colorName[i]+"SafetyZone5");
                pawns.get(i*4+j).setBounds(curP.x, curP.y, Constants.pawnWidth, Constants.pawnHeight);
                boardPanel.add(pawns.get(i*4+j));
            }
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

                    testPawn.setLocation(91 , 381);
                    testPawn.setLocation(151 , 441);

                    if (x < 50 && y > 50) {
                        testPawn.setLocation(x, y - stepLength);
                    }  else if (x < 850 && y < 50) {
                        testPawn.setLocation(x + stepLength, y);
                    } else if (x > 850 && y < 850) {
                        testPawn.setLocation(x, y + stepLength );
                    } else if (x > 50 && y > 850) {
                        testPawn.setLocation(x - stepLength, y);
                    }



                }
            }
        });

        drawCardBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                curCard = deck.draw();
                try {
                    Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+ImagePath+curCard.imgName));

                    basicImage = basicImage.getScaledInstance(Constants.cardWidth, Constants.cardHeight, Image.SCALE_SMOOTH);
                    ImageIcon cardImg = new ImageIcon(basicImage);
                    if(drawedCard == null){
                        drawedCard = new JLabel(cardImg);
                    }
                    else{
                        drawedCard.setIcon(cardImg);
                    }
                    System.out.println(System.getProperty("user.dir")+ImagePath+curCard.imgName);
                } catch (Exception ex) {
                    // handle exception...
                    System.out.println("loadCards failed \n" + ex.toString());
                }

                boardPanel.add(drawedCard);
                drawedCard.setBounds(Constants.cardStartX,Constants.cardStartY, Constants.cardWidth,Constants.cardHeight);

            }
        });

        //add mouse click event to pawns JLabel.

        //test kick boolean variable
        // How to move the pawn
        for(int i = 0; i < pawns.size(); i++){

            pawns.get(i).addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(seletedLabel == null){
                        seletedLabel = (JLabel)e.getComponent();
                        if(curCard != null){

                        }
                        System.out.println("( "+seletedLabel.getX()+" , "+seletedLabel.getY()+" )");
                    }

//                    else if(seletedLabel != (JLabel)e.getComponent() && canKick){
//                        Point startPoint = blockToBoardPosition.get("blueStart0");
//                        System.out.println("startPoint: "+startPoint.x+" , "+startPoint.y);
//                        JLabel temP = (JLabel) e.getComponent();
//                        temP.setLocation(startPoint.x,startPoint.y);
//                    }
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
                }
                seletedLabel = null;
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

    private void  initBackEnd(){
        linkBlockToBackEnd();
    }

    //Connect Backend Function
    private void linkBlockToBackEnd(){
        board = new Board();
        allBlocks = board.everyBlock;
        boardToBackend = new HashMap<>();
        for(int i = 0; i < allBlocks.size(); i++){

            //0-59 outerRing, 60 - 66 redSafetyZone
            if(i < 60){
                boardToBackend.put(allBlocks.get(i),blockToBoardPosition.get("block"+i));
            }
            if(60<= i && i < 66){
                boardToBackend.put(allBlocks.get(i),blockToBoardPosition.get("redSafetyZone"+(i-60)));
            }

            if(66<= i && i < 72){
                boardToBackend.put(allBlocks.get(i),blockToBoardPosition.get("blueSafetyZone"+(i-66)));
            }

            if(72<= i && i < 78){
                boardToBackend.put(allBlocks.get(i),blockToBoardPosition.get("greenSafetyZone"+(i-72)));
            }
            if(78<= i && i < 84){
                boardToBackend.put(allBlocks.get(i),blockToBoardPosition.get("yellowSafetyZone"+(i-78)));
            }
            String [] tempColor = {"red","blue","yellow","green"};

            if(84 <= i && i <88){
                boardToBackend.put(allBlocks.get(i),blockToBoardPosition.get(tempColor[i-84]+"Start"));
            }

        }
//        int j = 0;
//        for(Map.Entry<Block, Point> entry : boardToBackend.entrySet()) {
//            Block key = entry.getKey();
//            Point point = entry.getValue();
//
//            // do what you have to do here
//            // In your case, another loop.
//            System.out.print("{"+ key.getColor() + key.getId() +":");
//            System.out.printf(" (%d,%d)}\n" ,point.x,point.y);
//            j += 1;
//        }
//        System.out.println("blocks: "+j);
    }

    public void playGame(){

    }

}
