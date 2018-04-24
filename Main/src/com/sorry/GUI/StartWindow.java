package com.sorry.GUI;

import utils.TransparencyUtil;

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
    private JPanel gameLogo;

    //
    private String gameLogoImgPath= "/Main/imgs/";

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
        this.setSize(Constants.windowWidth,Constants.windowHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.pack(); // pack the window
        this.setVisible(true);
    }

    private void initGuiComponents(){
        newGameBtn = new JButton("New Game");
        try {
            Image basicImage = ImageIO.read(new File(System.getProperty("user.dir")+gameLogoImgPath+"_pawn.jpg"));
            basicImage = TransparencyUtil.makeColorTransparent(basicImage,java.awt.Color.WHITE);
            Image BoardImage = basicImage.getScaledInstance(Constants.pawnWidth, Constants.pawnHeight, Image.SCALE_SMOOTH);
            ImageIcon pawnImage = new ImageIcon(BoardImage);
        } catch (Exception ex) {
            // handle exception...
            System.out.println("loadPawns failed \n" + ex.toString());
        }
    }

    private void setGuiComponents(){
        newGameBtn.setBounds(960, 200, 120, 40);
        this.add(newGameBtn);

    }

    private void addEventListenerToComponents(){
        newGameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

//                GameWindow gw = GameWindow.getInstance();
//                gw.setVisible(true);
                newGameDialog();
            }
        });
    }

    private void newGameDialog(){

        String[] players = {
                "2","3","4"
        };

        JComboBox<String> numPlayers = new JComboBox<String>(players);

        String [] aiBehaviors = {"Dumb","Smart","Nice","Cruel"};

        JComboBox<String> computerDifficulties = new JComboBox<String>(aiBehaviors);

        final JComponent[] inputs = new JComponent[] {
                new JLabel("Number of Players"),
                numPlayers,
                new JLabel("Computer 1"),
                computerDifficulties,

        };
        Object[] possibilities = {"ham", "spam", "yam"};

        //JOptionPane.showInputDialog(null, inputs, "My custom dialog", JOptionPane.PLAIN_MESSAGE);
        int result = JOptionPane.showConfirmDialog(null, inputs, "My custom dialog", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
//            System.out.println("You entered " +
//                    firstName.getText() + ", " +
//                    lastName.getText() + ", " +
//                    password.getText());
        } else {
            System.out.println("User canceled / closed the dialog, result = " + result);
        }
    }
}
