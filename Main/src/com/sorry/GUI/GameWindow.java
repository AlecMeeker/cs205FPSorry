package com.sorry.GUI;

// import internal class of project.
import Logic.*;
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
    private JButton quitBtn;
    private JButton saveBtn;
    private JLabel drawnCard;
    private JTextArea cardInfoReminder;

    private ArrayList<JLabel> numOfPawnsOnStart;  //blue: 0, red: 1, yellow: 2,green: 3
    private ArrayList<JLabel> numOfPawnsOnHome;    //blue: 0, red: 1, yellow: 2,green: 3

    /* Some board and movement variables */
    private final int stepLength = 60;
    public static int safetyZoneSize = 5;
    public static int moveSteps = 1;
    private String ImagePath= "/Main/imgs/";
    private String [] colorName = {"red","blue","yellow","green"};


    /* Pawns variables */
    private static JLabel selectedLabel = null; //selected pawns label
    private Map<Pawn,JLabel> pawnsBackToFront;
    private Map<JLabel,Pawn> pawnsFrontToBack;

    /*  Game config variable # of players   */
    private int numOfPawns;
    private int numOfPlayers;

    /* Variable getters and setters */
    public int getNumOfPawns() {
        return numOfPawns;
    }
    public void setNumOfPawns(int numOfPawns) {
        this.numOfPawns = numOfPawns;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public Card getCurCard() {
        return curCard;
    }
    public void setCurCard(Card curCard) {
        this.curCard = curCard;
    }

    //BackEnd stuff.
    private Card curCard;
    private ArrayList<Block> allBlocks;
    //Try to store the block position of board panel
    private Map<String, Point> blockToBoardPosition;  //java.awt.point
    private Map<Block, Point> boardToBackend;

    //some constants member
    private Deck deck ;
    private StartWindow startWindow = StartWindow.getInstance();

    /* Boolean variables for button click */
    private boolean isDrawn;


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

    public void initWindow(){
        System.out.println("initWindow\n");
        //Gui Components config
        initGuiComponents();
        setGuiComponentsPosition();

        //initBackEnd;
        //initBackEnd();

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
               quitAndSave();
            }
            @Override
            public void windowActivated(java.awt.event.WindowEvent windowEvent){
                startWindow.setVisible(false);
            }
        });

        //this.pack(); // pack the window

        this.setResizable(false);

    }


    private void initGuiComponents(){
        //Initial the GUI components
        this.boardPanel = new BoardPanel();
        this.movePawnBtn = new JButton("Move");
        this.drawCardBtn = new JButton("Draw Card");
        this.quitBtn = new JButton("Quit");
        this.saveBtn = new JButton("Save Game");
        this.cardInfoReminder = new JTextArea();

        this.pawns = new ArrayList<>();
        //cardInfoReminder
        numOfPawnsOnStart = new ArrayList<>();
        numOfPawnsOnHome = new ArrayList<>();

        for(int i = 0; i < this.colorName.length;i++) {
            numOfPawnsOnStart.add(new JLabel());
            numOfPawnsOnHome.add(new JLabel());
        }

//        this.testPawn = loadPawns(this.ImagePath, "red");
        //Initial other variables
        blockToBoardPosition = new HashMap<String, Point>();
        initBlockToBoardPosition();
    }

    private ImageIcon loadPawnIcon( String color){
        try {
            Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+ImagePath+color+"_pawn.jpg"));
            basicImage = TransparencyUtil.makeColorTransparent(basicImage,java.awt.Color.WHITE);
            Image BoardImage = basicImage.getScaledInstance(Constants.pawnWidth, Constants.pawnHeight, Image.SCALE_SMOOTH);
            ImageIcon pawnImage = new ImageIcon(BoardImage);

            return pawnImage;
        } catch (Exception ex) {
            // handle exception...
            System.out.println("loadPawns failed \n" + ex.toString());
            return new ImageIcon();
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

//        testPawn.setBounds(Constants.pawnStartX, Constants.pawnStartY, Constants.pawnWidth, Constants.pawnHeight); //init the pawns position
//        boardPanel.add(testPawn);

        //put set count label of pawns

        Point temP = blockToBoardPosition.get("redStart");
        numOfPawnsOnStart.get(0).setBounds(temP.x,temP.y + 60,40,40);
        boardPanel.add(numOfPawnsOnStart.get(0));

        temP = blockToBoardPosition.get("redSafetyZone5");
        numOfPawnsOnHome.get(0).setBounds(temP.x,temP.y + 60,40,40);
        boardPanel.add(numOfPawnsOnHome.get(0));

        temP = blockToBoardPosition.get("blueStart");
        numOfPawnsOnStart.get(1).setBounds(temP.x+60,temP.y ,40,40);
        boardPanel.add(numOfPawnsOnStart.get(1));

        temP = blockToBoardPosition.get("blueSafetyZone5");
        numOfPawnsOnHome.get(1).setBounds(temP.x+60,temP.y ,40,40);
        boardPanel.add(numOfPawnsOnHome.get(1));

        temP = blockToBoardPosition.get("yellowStart");
        numOfPawnsOnStart.get(2).setBounds(temP.x,temP.y - 60 ,40,40);
        boardPanel.add(numOfPawnsOnStart.get(2));

        temP = blockToBoardPosition.get("yellowSafetyZone5");
        numOfPawnsOnHome.get(2).setBounds(temP.x,temP.y - 60 ,40,40);
        boardPanel.add(numOfPawnsOnHome.get(2));

        temP = blockToBoardPosition.get("greenStart");
        numOfPawnsOnStart.get(3).setBounds(temP.x-60,temP.y ,40,40);
        boardPanel.add(numOfPawnsOnStart.get(3));

        temP = blockToBoardPosition.get("greenSafetyZone5");
        numOfPawnsOnHome.get(3).setBounds(temP.x-60,temP.y  ,40,40);
        boardPanel.add(numOfPawnsOnHome.get(3));

        //test the display number of pawn
        for(int i = 0; i < colorName.length; i++){

            setLabelFont(numOfPawnsOnHome.get(i));
            setLabelFont(numOfPawnsOnStart.get(i));
        }

        movePawnBtn.setBounds(1000, 900, 100, 40);
        this.add(movePawnBtn);

        drawCardBtn.setBounds(1000, 700, 100, 40);
        this.add(drawCardBtn);

        quitBtn.setBounds(1000, 800, 100, 40);
        this.add(quitBtn);

        saveBtn.setBounds(1000, 200, 100, 40);
        this.add(saveBtn);

        cardInfoReminder.setBounds(980,300,400,300);
        cardInfoReminder.setFont(cardInfoReminder.getFont().deriveFont(20f));

        setTextAreaTran(cardInfoReminder);
        this.add(cardInfoReminder);
    }
    private void setTextAreaTran(JTextArea textArea){
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea) {
            @Override
            protected void paintComponent(Graphics g) {
                try {
                    Composite composite = ((Graphics2D)g).getComposite();

                    ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0));
                    g.setColor(getBackground());
                    g.fillRect(0, 0, getWidth(), getHeight());

                    ((Graphics2D)g).setComposite(composite);
                    paintChildren(g);
                }
                catch(IndexOutOfBoundsException e) {
                    super.paintComponent(g);
                }
            }
        };

        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
    }
    private void setLabelFont(JLabel label){
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = label.getWidth();

// Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = label.getHeight();

// Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

// Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
    }

    private void addEventListenerToComponents(){

        movePawnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDrawn = false;
//                for(int i = 0; i < moveSteps;i++) {
//                    Point pt = testPawn.getLocation();
//                    int x = pt.x;
//                    int y = pt.y;
//
//                    testPawn.setLocation(91 , 381);
//                    testPawn.setLocation(151 , 441);
//
//                    if (x < 50 && y > 50) {
//                        testPawn.setLocation(x, y - stepLength);
//                    }  else if (x < 850 && y < 50) {
//                        testPawn.setLocation(x + stepLength, y);
//                    } else if (x > 850 && y < 850) {
//                        testPawn.setLocation(x, y + stepLength );
//                    } else if (x > 50 && y > 850) {
//                        testPawn.setLocation(x - stepLength, y);
//                    }
//                }
            }
        });

        drawCardBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (deck == null) {
                    return;
                }
                if (isDrawn) {
                    return;
                }
                    curCard = deck.draw();
                    try {
                        Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+ImagePath+curCard.imgName));

                        basicImage = basicImage.getScaledInstance(Constants.cardWidth, Constants.cardHeight, Image.SCALE_SMOOTH);
                        ImageIcon cardImg = new ImageIcon(basicImage);
                        if(drawnCard == null){
                            drawnCard = new JLabel(cardImg);
                        }
                        else{
                            drawnCard.setIcon(cardImg);
                        }
                        System.out.println(System.getProperty("user.dir")+ImagePath+curCard.num);
                    } catch (Exception ex) {
                        // handle exception...
                        System.out.println("loadCards failed \n" + ex.toString());
                    }
                cardInfoReminder.setText(curCard.reminderText);
                boardPanel.add(drawnCard);
                drawnCard.setBounds(Constants.cardStartX,Constants.cardStartY, Constants.cardWidth,Constants.cardHeight);
                if(curCard.num != 2){
                    isDrawn = true;
                }
            }
        });

        //add mouse click event to pawns JLabel.

        //testing code kick boolean variable
        // How to move the pawn
//        for(int i = 0; i < pawns.size(); i++){
//
//            pawns.get(i).addMouseListener(new MouseListener() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    if(selectedLabel == null){
//                        selectedLabel = (JLabel)e.getComponent();
//                        selectedLabel.setOpaque(true);
//                        selectedLabel.setBackground(Color.YELLOW);
//                        if(curCard != null){
//
//                        }
//                        System.out.println("( "+selectedLabel.getX()+" , "+selectedLabel.getY()+" )");
//                    }
//                }
//
//                @Override
//                public void mousePressed(MouseEvent e) {
//
//                }
//
//                @Override
//                public void mouseReleased(MouseEvent e) {
//
//                }
//
//                @Override
//                public void mouseEntered(MouseEvent e) {
//
//                }
//
//                @Override
//                public void mouseExited(MouseEvent e) {
//
//                }
//            });
//
//        }
        boardPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //move the pieces to the high light point.
                System.out.println("mouse pos: "+e.getX()+" , "+e.getY());
                for (Point pos : blockToBoardPosition.values()) {
                    // ...
                    if( e.getX() - pos.x > 0 && e.getY() - pos.y > 0
                            && e.getX() - pos.x < 60 && e.getY() - pos.y < 60
                            && selectedLabel != null){
                        selectedLabel.setLocation(pos.x,pos.y);
                        selectedLabel.setOpaque(false);
                        System.out.println("selectedLabel pos: "+pos.x+" , "+pos.y);
                        break;
                    }
                }
                selectedLabel = null;
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


        quitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitAndSave();
            }
        } );


        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("save game");
            }
        } );

    }


    /* Connect Backend Function */
    private void linkBlockToBackEnd(Board boardPa){

        Board board = boardPa;
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

    private void linkPawnTogether(ArrayList<Pawn> allPawn){

        pawnsBackToFront = new HashMap<>();
        pawnsFrontToBack = new HashMap<>();
        for(int i = 0;i < allPawn.size();i++){
            pawnsBackToFront.put(allPawn.get(i),pawns.get(i));
            Point curP = blockToBoardPosition.get(allPawn.get(i).getColor().toString().toLowerCase()+"Start");
            System.out.println(allPawn.get(i).getColor().toString()+"Start");
            pawns.get(i).setBounds(curP.x, curP.y, Constants.pawnWidth, Constants.pawnHeight);
            boardPanel.add(pawns.get(i));
        }

        for(Map.Entry entry: pawnsBackToFront.entrySet()){
            pawnsFrontToBack.put((JLabel) entry.getValue(),(Pawn)entry.getKey());
        }

    }

    // for close the window event
    private void quitAndSave(){
        startWindow.setVisible(true);
        this.dispose();
    }

    /* Public function */
    public void loadConfig(int numOfPlayers){

        this.pawns = new ArrayList<>();
        this.numOfPlayers = numOfPlayers;
        this.numOfPawns = 4 * (numOfPlayers);
        pawns = new ArrayList<>();
        for(int i = 0; i < numOfPawns; i++){
            pawns.add(new JLabel());
        }
    }

    public void loadGameStuff(ArrayList<Pawn> everyPawn, Board board){
        linkBlockToBackEnd(board);
        linkPawnTogether(everyPawn);
        this.deck = board.thisDeck;
    }

    public void refreshBoard(ArrayList<Pawn> everyPawn, Board board){

        int i = 0;
        for(JLabel pawnL: pawns){
            boardPanel.remove(pawnL);
            i++;
        }

        for(Pawn pawn : everyPawn){
            Point tmpP =  boardToBackend.get(pawn.getCurrentBlock());
            JLabel pawnL = pawnsBackToFront.get(pawn);
            pawnL.setIcon(loadPawnIcon(pawn.getColor().toString()));

            // If the pawn belong to human player add the click event to it
            HumanPlayer tmpPlayer = new HumanPlayer();
//            if(pawn.myPlayer.getClass() == tmpPlayer.getClass())
//            {


//            }
            if(pawnL.getMouseListeners().length < 1) {
                pawnL.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if (selectedLabel == null) {
                            selectedLabel = (JLabel) e.getComponent();

                            if (curCard != null) {
                                ;
                            }
                            System.out.println("null"+"( " + selectedLabel.getX() + " , " + selectedLabel.getY() + " )");
                        }
                        selectedLabel.setOpaque(true);
                        selectedLabel.setBackground(java.awt.Color.YELLOW);
                        System.out.println("( " + selectedLabel.getX() + " , " + selectedLabel.getY() + " )");
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
            boardPanel.add(pawnL);
            pawnL.setLocation(tmpP.x,tmpP.y);

        }
        System.out.println("how many pawnsLabel now: "+i);

        System.out.println("run refreshBoard");
    }

}
