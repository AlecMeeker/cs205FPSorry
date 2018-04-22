package com.sorry.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JFrame {

    //Singleton
    private static volatile StartWindow instance = null;
    private static Object mutex = new Object();

    //gui components
    private JButton newGameBtn;

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
        this.newGameBtn = new JButton("New Game");

    }

    private void setGuiComponents(){
        newGameBtn.setBounds(960, 200, 120, 40);
        this.add(newGameBtn);

    }

    private void addEventListenerToComponents(){
        newGameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                GameWindow gw = GameWindow.getInstance();
                gw.setVisible(true);

            }
        });
    }
}
