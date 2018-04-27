package GUI;


// import internal class of project.
import Logic.Board;
import Logic.Card;
import Logic.Color;
import Logic.Deck;
import Logic.Block;
import Logic.*;
//import org.omg.CORBA.SystemException;
import utils.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
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
    private ArrayList<JLabel> pawns;
    private JButton nextBtn;
    private JButton drawCardBtn;
    private JButton quitBtn;
    private JButton saveBtn;
    private JLabel drawnCard;
    private JTextArea cardInfoReminder;

    private ArrayList<JLabel> numOfPawnsOnStart;
    private ArrayList<JLabel> numOfPawnsOnHome;
    private ArrayList<JLabel> highlightBlocks;

    /* Button config */
    private final int buttonWidth = 210;
    private final int buttonHeight = 60;
    private final int buttonFontSize = 28;
    private final String buttonFontName = "Broadway";
    private final java.awt.Color buttonBGColor = new java.awt.Color(127,255,212,1);

    /* Some board and movement variables */
    private int stepLength = 60;
    public static int safetyZoneSize = 5;
    public static int moveSteps = 1;
    private String ImagePath= "/src/imgs/";
    private String [] colorName = {"red","blue","yellow","green"};

    /* Pawns variables */
    private static JLabel selectedLabel = null; //selected pawns label
    private Map<Pawn,JLabel> pawnsBackToFront;
    private Map<JLabel,Pawn> pawnsFrontToBack;

    /*  Game config variable # of players   */
    private int numOfPawns;
    private int numOfPlayers;

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }
    public int getNumOfPawns() {
        return numOfPawns;
    }

    //BackEnd stuff.
    private Game currentGame;
    private Card curCard;
    private ArrayList<Block> allBlocks;
    private String humanPlayerColor;

    //Try to store the block position of board panel
    private Map<String, Point> blockToBoardPosition;  //java.awt.point
    private Map<Block, Point> boardToBackend;

    //some constants member
    public Deck deck;
    private StartWindow startWindow = StartWindow.getInstance();
    private Board boardGui;
    /* Boolean variables for button click */
    private boolean isDrawn;
    private boolean isMovedThisTurn;

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


        try{
            BufferedImage myImage = ImageIO.read(new File(System.getProperty("user.dir")+ImagePath + "woodDesktop.jpg"));
            this.setContentPane(new ImagePanel(myImage));
        }
        catch(Exception ex){
            System.out.println("load window background failed" + ex.toString());
        }

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
        this.setResizable(false);

        //Gui Components config
        initGuiComponents();
        setGuiComponentsPosition();

        //add motion
        addEventListenerToComponents();

    }

    private void configButton(JButton button){
        button.setBackground(buttonBGColor);
        button.setFont(new Font(buttonFontName, Font.BOLD, buttonFontSize));
        button.setBorder(null);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }

    private void initGuiComponents(){
        //Initial the GUI components
        this.boardPanel = new BoardPanel();
        this.nextBtn = new JButton("Next Turn");
        configButton(nextBtn);
        this.drawCardBtn = new JButton("Draw Card");
        configButton(drawCardBtn);
        this.quitBtn = new JButton("Quit");
        configButton(quitBtn);
        this.saveBtn = new JButton("Save Game");
        configButton(saveBtn);


        //cardInfoReminder
        this.cardInfoReminder = new JTextArea();
        this.pawns = new ArrayList<>();

        numOfPawnsOnStart = new ArrayList<>();
        numOfPawnsOnHome = new ArrayList<>();


        for(int i = 0; i < this.colorName.length;i++) {
            numOfPawnsOnStart.add(new JLabel());
            numOfPawnsOnHome.add(new JLabel());
        }

        //Initial other variables
        blockToBoardPosition = new HashMap<String, Point>();
        initBlockToBoardPosition();
    }

    private ImageIcon loadPawnIcon( String color){
        try {
            Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+ImagePath+color+"_pawn.jpg"));
            basicImage = TransparencyUtil.makeColorTransparent(basicImage,java.awt.Color.WHITE);
            Image BoardImage = basicImage.getScaledInstance(Constants.pawnWidth-10, Constants.pawnHeight, Image.SCALE_SMOOTH);
            ImageIcon pawnImage = new ImageIcon(BoardImage);

            return pawnImage;
        } catch (Exception ex) {
            // handle exception...
            System.out.println("loadPawns failed \n" + ex.toString());
            return new ImageIcon();
        }
    }

    private ImageIcon loadLabelIcon(String FileName){
        try {
            Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+ImagePath+FileName));
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

        // map the number of pawn label to start and home.
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

        drawCardBtn.setBounds(1080, 500, buttonWidth, buttonHeight);
        this.add(drawCardBtn);

        nextBtn.setBounds(1080, 600, buttonWidth, buttonHeight);
        this.add(nextBtn);

        saveBtn.setBounds(1080, 700, buttonWidth, buttonHeight);
        this.add(saveBtn);

        quitBtn.setBounds(1080, 800, buttonWidth, buttonHeight);
        this.add(quitBtn);



        cardInfoReminder.setBounds(980,300,400,300);
        cardInfoReminder.setFont(cardInfoReminder.getFont().deriveFont(20f));
        cardInfoReminder.setEditable(false);
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

        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDrawn = false;
                isMovedThisTurn = false;
                currentGame.nextTurn();
                currentGame.human.selectEndBlockStep();
                selectedLabel = null;
                removeAllHighlight();
                refreshBoard();

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
                isMovedThisTurn = false;
                //Backend human player draw card
                currentGame.human.drawStep();
                curCard = currentGame.human.getCurrentDraw();
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
                    return;
                }
                cardInfoReminder.setText(curCard.reminderText);
                boardPanel.add(drawnCard);
                drawnCard.setBounds(Constants.cardStartX,Constants.cardStartY, Constants.cardWidth,Constants.cardHeight);
                if(curCard.num != 2){
                    isDrawn = true;
                }

                //if the human's potential move size is 0, go to next turn automatically
                if (currentGame.human.potentialMovesList.get(0).size() == 0) {
                    currentGame.nextTurn();
                }
            }
        });


        boardPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //move the pieces to the high light point.

                System.out.println("mouse pos: "+e.getX()+" , "+e.getY());
                refreshBoard();
                if(isMovedThisTurn){
                    selectedLabel.setOpaque(false);
                    selectedLabel.repaint();
                    return;
                }

                for (JLabel HLBlock : highlightBlocks) {
                    // ...
                    Point pos = HLBlock.getLocation();
                    if( e.getX() - pos.x > 0 && e.getY() - pos.y > 0
                            && e.getX() - pos.x < 60 && e.getY() - pos.y < 60
                            && selectedLabel != null){
                        selectedLabel.setLocation(pos.x,pos.y);

                        for(Block block: allBlocks){
                            Point blockPt = boardToBackend.get(block);
                            if(blockPt.equals(pos)){
                                block.selected = true;
                            }
                        }

                        currentGame.human.selectEndBlockStep();

                        isMovedThisTurn = true;
                        System.out.println("selectedLabel pos: "+pos.x+" , "+pos.y);
                        break;
                    }
                }
                if(selectedLabel != null) {
                    Pawn selectedPawn = pawnsFrontToBack.get(selectedLabel);
                    selectedPawn.selected = false;
                    selectedLabel.setOpaque(false);
                    selectedLabel.repaint();
                    Pawn tmpPawn = pawnsFrontToBack.get(selectedLabel);
                    tmpPawn.selected = false;
                }

                removeAllHighlight();
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
                currentGame.saveGame();
            }
        } );

    }

    //Connect Backend Function
    private void linkBlockToBackEnd(){

        if(boardGui == null){
           System.out.println("boardGui is null");
        }

        allBlocks = boardGui.everyBlock;
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

    }

    /**
     * Use hashMap to map the backend pawns to frontend
     * frontend to backend
     * @param allPawn all the pawns from backend
     */
    private void linkPawnTogether(ArrayList<Pawn> allPawn){

        pawnsBackToFront = new HashMap<>();
        pawnsFrontToBack = new HashMap<>();
        System.out.println("Size:"+allPawn.size());
        for(int i = 0;i < allPawn.size();i++){
            pawnsBackToFront.put(allPawn.get(i),pawns.get(i));
            Point curP = blockToBoardPosition.get(allPawn.get(i).getColor().toString().toLowerCase()+"Start");
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
        removeAllPawns();
        //currentGame.quitGame();
        this.dispose();
        System.out.println("refreshBoard times");
    }

    private void removeAllPawns(){
        if(pawns ==null) {
            return;
        }
        for(JLabel pawnL: pawns){
            boardPanel.remove(pawnL);
        }
    }

    private void removeAllHighlight(){
        if(highlightBlocks ==null) {
            return;
        }
        for(JLabel HLLabel: highlightBlocks){
            boardPanel.remove(HLLabel);
        }
    }
    /* Public function */

    /**
     * Using for load game configuration
     * @param numOfPlayers
     */
    public void loadConfig(int numOfPlayers){

        this.pawns = new ArrayList<>();
        this.numOfPlayers = numOfPlayers;
        this.numOfPawns = 4 * (numOfPlayers);
        this.humanPlayerColor = currentGame.human.getColor().toString().toLowerCase();
        pawns = new ArrayList<>();
        for(int i = 0; i < numOfPawns; i++){
            pawns.add(new JLabel());
        }
    }

    /**
     * Load Game stuff from backend
     *@param newGame   the current Game instance
     */
    public void loadGameStuff(Game newGame){

        currentGame = newGame;
        loadConfig(newGame.allPlayers.size());
        ArrayList<Pawn> everyPawn = currentGame.everyPawn;
        this.boardGui = currentGame.gameBoard;
        linkBlockToBackEnd();
        linkPawnTogether(everyPawn);
        this.deck = currentGame.gameBoard.thisDeck;
    }

    /**
     *  Each turn all players AIs and Human finish the turn will refresh the GUI board
     *
     */
    public void refreshBoard(){
        long startTime = System.nanoTime();


        if(currentGame == null) {
            return;
        }
        ArrayList<Pawn> everyPawn = currentGame.everyPawn;
        ArrayList<Player> allPlayers = currentGame.allPlayers;
        removeAllPawns();
        removeAllHighlight();
        highlightBlocks = new ArrayList<>();


        for(Pawn pawn : everyPawn){
            Point tmpP =  boardToBackend.get(pawn.getCurrentBlock());
            JLabel pawnL = pawnsBackToFront.get(pawn);

            pawnL.setIcon(loadPawnIcon(pawn.getColor().toString().toLowerCase()));
            pawnL.setHorizontalAlignment(SwingConstants.CENTER);


            // If the pawn belong to human player add the click event to it
            HumanPlayer tmpPlayer = new HumanPlayer();
            if(pawn.myPlayer.getClass() == tmpPlayer.getClass())
            {
                if(pawnL.getMouseListeners().length < 1) {
                    pawnL.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                            if (selectedLabel == null) {
                                selectedLabel = (JLabel) e.getComponent();
                                Pawn selectedPawn = pawnsFrontToBack.get(selectedLabel);
                                //if clicked, pawn is now selected in backend
                                selectedPawn.selected = true;
                                //if a card has been drawn
                                if (curCard != null) {
                                    currentGame.human.selectPawnStep();
                                    refreshBoard();
                                }
                                System.out.println("null"+"( " + selectedLabel.getX() + " , " + selectedLabel.getY() + " )");
                            }
                            selectedLabel.setOpaque(true);
                            selectedLabel.setBackground(java.awt.Color.YELLOW);
                            selectedLabel.repaint();
                            System.out.println(selectedLabel.isOpaque()+"( " + selectedLabel.getX() + " , " + selectedLabel.getY() + " )");
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
            boardPanel.add(pawnL);
            pawnL.setLocation(tmpP.x,tmpP.y);

        }

        for(Player player:allPlayers){
            Color playerColor = player.getColor();
            Integer pawnsOnStart = player.getStartPawnList().size();
            Integer pawnsOnHome = player.getFinishedPawnList().size();
            System.out.println("player color: "+playerColor);
            switch(playerColor){
                case RED: numOfPawnsOnStart.get(0).setText(pawnsOnStart.toString());
                    numOfPawnsOnHome.get(0).setText(pawnsOnHome.toString());

                    break;
                case BLUE: numOfPawnsOnStart.get(1).setText(pawnsOnStart.toString());
                    numOfPawnsOnHome.get(1).setText(pawnsOnHome.toString());

                    break;
                case YELLOW: numOfPawnsOnStart.get(2).setText(pawnsOnStart.toString());
                    numOfPawnsOnHome.get(2).setText(pawnsOnHome.toString());

                    break;
                case GREEN: numOfPawnsOnStart.get(3).setText(pawnsOnStart.toString());
                    numOfPawnsOnHome.get(3).setText(pawnsOnHome.toString());

                    break;
            }
        }

        for(Block block: allBlocks){

            if(block.highlighted == true){
                Point tmpP = boardToBackend.get(block);

                JLabel tmpLabel = new JLabel(loadLabelIcon(humanPlayerColor+"_hl.jpg"));
                tmpLabel.setBounds(tmpP.x,tmpP.y,Constants.pawnWidth,Constants.pawnHeight);

                boardPanel.add(tmpLabel);
                tmpLabel.repaint();
                highlightBlocks.add(tmpLabel);
                System.out.println("highlighted");
            }
        }

        long endTime   = System.nanoTime();
        float totalTime = (endTime - startTime)/1000000000;
        System.out.println("run refreshBoard: "+totalTime);

    }

    public ArrayList<Block> movableBlocks(){
        return null;
    }

}
