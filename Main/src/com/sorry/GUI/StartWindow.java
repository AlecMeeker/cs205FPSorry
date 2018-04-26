package com.sorry.GUI;

import Logic.Game;
import sql.ConnectDB;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
    private String gameLogoImgPath= "/Main/imgs/sorry_start.jpg";

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

        //GUI Components config
        initGuiComponents();
        setGuiComponents();
        addEventListenerToComponents();

        //Set layout
        this.setLayout(null);
        //Setting the window
        this.setSize(Constants.windowWidth,Constants.windowHeight-100);
        this.setTitle("Sorry! Sweet avenge board game.");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.pack(); // pack the window
        //this.setVisible(true);
        this.setResizable(false);
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
        newGameBtn.setBounds(930, 240, 120, 40);
        this.add(newGameBtn);
        loadGameBtn.setBounds(930, 320, 120, 40);
        this.add(loadGameBtn);
        statBtn.setBounds(930, 400, 120, 40);
        this.add(statBtn);
        instructionBtn.setBounds(930, 480, 120, 40);
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

                JTextArea instructionText = new JTextArea();
                instructionText.setLineWrap(true);

                JFrame instructionWindow= new JFrame();
                instructionWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                instructionWindow.setBounds(50,50,100,800);
                instructionWindow.add(instructionText);
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
        Object[] possibilities = {"ham", "spam", "yam"};

        //JOptionPane.showInputDialog(null, inputs, "My custom dialog", JOptionPane.PLAIN_MESSAGE);
        int result = JOptionPane.showConfirmDialog(null, inputs, "My custom dialog", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            System.out.println("You entered " +
                    numPlayers.getSelectedItem().toString() + ", " +
                    playerColorSelect.getSelectedItem().toString() + ", " +
                    computerDifficulties1.getSelectedItem().toString() + ", " +
                    computerDifficulties2.getSelectedItem().toString() + ", " +
                    computerDifficulties3.getSelectedItem().toString());
            System.out.println(Integer.parseInt(numPlayers.getSelectedItem().toString()));

                //Get the game Config
                int numberOfPlayers = Integer.parseInt(numPlayers.getSelectedItem().toString());
                GameWindow gw = GameWindow.getInstance();
                //gw.initWindow();
                gw.loadConfig(numberOfPlayers);
                gw.setVisible(true);
                Game newGame = new Game();
                newGame.numOfPlayers = numberOfPlayers;
                newGame.playGame();
        } else {
            System.out.println("User canceled / closed the dialog, result = " + result);
        }
    }
}
