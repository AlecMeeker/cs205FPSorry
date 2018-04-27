package GUI;

import Logic.Game;
import SQL.ConnectDB;
import Logic.Color;
import utils.ImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class StartWindow extends JFrame {

    //Singleton
    private static volatile StartWindow instance = null;
    private static Object mutex = new Object();

    //gui components
    private JButton newGameBtn;
    private JButton loadGameBtn;
    private JButton statBtn;
    private JButton instructionBtn;
    private JLabel gameLogo;

    //some
    private String gameLogoImgPath= "/src/imgs/sorry_start.jpg";
    private String ImagePath = "/src/imgs/";
    private String instructionString = "The modern deck contains 45 cards: there are five 1 cards as well as four each of the other cards (Sorry!, 2, 3, 4, 5, 7, 8, 10, 11 and 12). The 6s or 9s are omitted to avoid confusion. The first edition of the game had 44 cards (four of each) and the extra 1 card was soon introduced as an option for quicker play.[5] A 1996 board from Waddingtons had 5 of each card.\n" +
            "\n" +
            "Cards are annotated with the following actions:\n" +
            "\n" +
            "1\tMove a pawn from Start or move a pawn one space forward.\n" +
            "2\tMove a pawn from Start or move a pawn two spaces forward. Drawing a two entitles the player to draw again at the end of his or her turn. If the player cannot use a two to move, he or she can still draw again.\n" +
            "3\tMove a pawn three spaces forward.\n" +
            "4\tMove a pawn four spaces backward.\n" +
            "5\tMove a pawn five spaces forward.\n" +
            "7\tMove one pawn seven spaces forward, or split the seven spaces between two pawns (such as four spaces for one pawn and three for another). This makes it possible for two pawns to enter Home on the same turn, for example. The seven cannot be used to move a pawn out of Start, even if the player splits it into a six and one or a five and two. The entire seven spaces must be used or the turn is lost. You may not move backwards with a split.\n" +
            "8\tMove a pawn eight spaces forward.\n" +
            "10\tMove a pawn 10 spaces forward or one space backward. If none of a player's pawns can move forward 10 spaces, then one pawn must move back one space.\n" +
            "11\tMove 11 spaces forward, or switch the places of one of the player's own pawns and an opponent's pawn. A player that cannot move 11 spaces is not forced to switch and instead can forfeit the turn. An 11 cannot be used to switch a pawn that is in a Safety Zone.\n" +
            "12\tMove a pawn 12 spaces forward.\n" +
            "Sorry! card\tTake any one pawn from Start and move it directly to a square occupied by any opponent's pawn, sending that pawn back to its own Start. A Sorry! card cannot be used on an opponent's pawn in a Safety Zone. If there are no pawns on the player's Start, or no opponent's pawns on any squares outside Safety Zones, the turn is lost."+"Each player chooses four pawns of one color and places them in his or her Start. One player is selected to play first.\n" +
            "\n" +
            "Each player in turn draws one card from the deck and follows its instructions. To begin the game, all of a player's four pawns are restricted to Start; a player can only move them out onto the rest of the board if he or she draws a 1 or 2 card. A 1 or a 2 places a pawn on the space directly outside of start (a 2 does not entitle the pawn to move a second space).\n" +
            "\n" +
            "The Relaxation Start: When a young player is playing, especially when learning the game of Sorry!, a relaxation is offered in allowing one of his or her pawns to begin the game already on the board, on the space directly outside his or her Start, as the tedium of waiting for a 1 or a 2 can be wearisome even for experienced players.\n" +
            "\n" +
            "A pawn can jump over any other pawn during its move. However, two pawns cannot occupy the same square; a pawn that lands on a square occupied by another player's pawn \"bumps\" that pawn back to its own Start. Players can not bump their own pawns back to Start; if the only way to complete a move would result in a player bumping his or her own pawn, the player's pawns remain in place and the player loses his or her turn.\n" +
            "\n" +
            "If a pawn lands at the start of a slide (except those of its own color), either by direct movement or as the result of a switch from an 11 card or a Sorry card, it immediately \"slides\" to the last square of the slide. All pawns on all spaces of the slide (including those belonging to the sliding player) are sent back to their respective Starts.[4]\n" +
            "\n" +
            "The last five squares before each player's Home are \"Safety Zones\", and are specially colored corresponding to the colors of the Homes they lead to. Access is limited to pawns of the same color. Pawns inside the Safety Zones are immune to being bumped by opponent's pawns or being switched with opponents' pawns via 11 or Sorry! cards. However, if a pawn is forced via a 10 or 4 card to move backwards out of the Safety Zone, it is no longer considered \"safe\" and may be bumped by or switched with " +
            "opponents' pawns as usual until it re-enters the Safety Zone.";
    //
    private int numPlayer;
    private StartWindow(){
        initWindow();
    }

    public static StartWindow getInstance(){ //Thread safe singleton model
        StartWindow result = instance;
        if(result == null){
            synchronized (mutex){
                result = instance;
                if(result == null){
                    instance = result = new StartWindow();
                }
            }
        }
        return result;
    }

    private void initWindow(){

        //Set layout
        this.setLayout(null);
        //Setting the window
        this.setSize(Constants.windowWidth-100,Constants.windowHeight);
        this.setTitle("Sorry! The Sweet Revenge board game.");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try{
            BufferedImage myImage = ImageIO.read(new File(System.getProperty("user.dir")+ImagePath + "woodDesktop.jpg"));
            this.setContentPane(new ImagePanel(myImage));
        }
        catch(Exception ex){
            System.out.println("load window background failed" + ex.toString());
        }
        this.setResizable(false);
        //GUI Components config
        initGuiComponents();
        setGuiComponents();
        addEventListenerToComponents();


        //this.pack(); // pack the window
        //this.setVisible(true);
    }

    private void initGuiComponents(){
        newGameBtn = new JButton("New Game");
        loadGameBtn = new JButton("Load Game");
        statBtn = new JButton("Statistic");
        instructionBtn = new JButton("Instruction");
        try {
            Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+gameLogoImgPath));
            basicImage = basicImage.getScaledInstance(Constants.gameLogoWidth, Constants.gameLogoHeight, Image.SCALE_SMOOTH);
            ImageIcon gameLogoimg = new ImageIcon(basicImage);
            gameLogo = new JLabel(gameLogoimg);
        } catch (Exception ex) {
            // handle exception...
            System.out.println("loadPawns failed \n" + ex.toString());
        }
    }

    private void setGuiComponents(){
        newGameBtn.setBounds(980, 240, 120, 40);
        this.add(newGameBtn);
        loadGameBtn.setBounds(980, 320, 120, 40);
        this.add(loadGameBtn);
        statBtn.setBounds(980, 400, 120, 40);
        this.add(statBtn);
        instructionBtn.setBounds(980, 480, 120, 40);
        this.add(instructionBtn);

        gameLogo.setBounds(Constants.gameLogoStartX,Constants.gameLogoStartY,Constants.gameLogoWidth,Constants.gameLogoHeight);
        this.add(gameLogo);
    }

    private void addEventListenerToComponents(){
        newGameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                newGameDialog();

            }
        });
        loadGameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        statBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TableDisplay td = new TableDisplay();
                td.setVisible(true);
            }
        });

        instructionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JTextArea instructionTextArea = new JTextArea();
                instructionTextArea.setLineWrap(true);
                instructionTextArea.setText(instructionString);
                instructionTextArea.setFont(instructionTextArea.getFont().deriveFont(16f));
                JScrollPane scroll = new JScrollPane(instructionTextArea);
                scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                JFrame instructionWindow= new JFrame();
                instructionWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                instructionWindow.setBounds(50,50,1000,800);
                instructionWindow.add(scroll);
                instructionWindow.setVisible(true);
            }
        });


    }
    class TableDisplay extends JFrame
    {
        public TableDisplay()
        {
            JTable table = ConnectDB.getAsJTable(ConnectDB.getAllGameInfo());
            table.setPreferredScrollableViewportSize(table.getPreferredSize());
            table.setFillsViewportHeight(true);
            //add the table to the frame
            this.add(new JScrollPane(table));

            this.setTitle("Statistics");
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setBounds(100,100,800,800);
            this.setResizable(false);
            this.pack();
            this.setVisible(true);
        }
    }

    private void newGameDialog(){

        String[] players = {
                "2","3","4"
        };
        JComboBox<String> numPlayers = new JComboBox<String>(players);

        String [] playerColors = {"blue","yellow","green","red"};

        JComboBox<String> playerColorSelect = new JComboBox<String>(playerColors);

        String [] aiBehaviors = {"Dumb & Nice","Dumb & Cruel","Smart & Nice","Smart & Cruel"};
        JComboBox<String> computerDifficulties1 = new JComboBox<String>(aiBehaviors);
        JComboBox<String> computerDifficulties2 = new JComboBox<String>(aiBehaviors);
        JComboBox<String> computerDifficulties3 = new JComboBox<String>(aiBehaviors);

        final JComponent[] inputs = new JComponent[] {
                new JLabel("Number of Players"),
                numPlayers,
                new JLabel("Player Color"),
                playerColorSelect,
                new JLabel("Computer 1"),
                computerDifficulties1,
                new JLabel("Computer 2"),
                computerDifficulties2,
                new JLabel("Computer 3"),
                computerDifficulties3,

        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "Game Config", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {

            System.out.println("You entered " +
                    numPlayers.getSelectedItem().toString() + ", " +
                    playerColorSelect.getSelectedItem().toString() + ", " +
                    computerDifficulties1.getSelectedItem().toString() + ", " +
                    computerDifficulties2.getSelectedItem().toString() + ", " +
                    computerDifficulties3.getSelectedItem().toString());

            numPlayer = Integer.parseInt(numPlayers.getSelectedItem().toString());
            ArrayList<Integer> aiDifficulties = new ArrayList<>();

            switch (numPlayer) {
                case 4:
                    aiDifficulties.add(getOptionFromString(computerDifficulties3.getSelectedItem().toString()));
                case 3:
                    aiDifficulties.add(getOptionFromString(computerDifficulties2.getSelectedItem().toString()));
                case 2:
                    aiDifficulties.add(getOptionFromString(computerDifficulties1.getSelectedItem().toString()));
            }

            Game newGame = new Game(getColorFromString(playerColorSelect.getSelectedItem().toString()), aiDifficulties);

            GameWindow gw = GameWindow.getInstance();
            gw.loadConfig(numPlayer);
            gw.loadGameStuff(newGame.everyPawn,newGame);

            gw.setVisible(true);
            gw.refreshBoard(newGame.everyPawn,newGame.allPlayers);
        } else {
            System.out.println("User canceled / closed the dialog, result = " + result);
        }
    }

    public int getOptionFromString(String diffString) {

        switch (diffString) {
            case "Dumb & Nice" :
                return 0;
            case "Dumb & Cruel" :
                return 1;
            case "Smart & Nice" :
                return 2;
            case "Smart & Cruel" :
                return 3;
            default :
                return 5;
        }
    }

    public Color getColorFromString(String colorString){
        switch (colorString) {
            case "blue" :
                return Color.BLUE;
            case "red" :
                return Color.RED;
            case "yellow" :
                return Color.YELLOW;
            case "green" :
                return Color.GREEN;
            default :
                return Color.NULL;
        }
    }
}
