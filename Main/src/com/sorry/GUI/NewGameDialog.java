package com.sorry.GUI;

import javax.swing.*;

public class NewGameDialog extends JDialog{

    //Singleton design
    private static volatile NewGameDialog instance = null;
    private static Object mutex = new Object();

    //Customize components


    private NewGameDialog(){
        initDialog();
    }

    public static NewGameDialog getInstance(){ //Thread safe singleton model
        NewGameDialog result = instance;
        if(result == null){
            synchronized (mutex){
                result = instance;
                if(result == null){
                    instance = result = new NewGameDialog();
                }
            }
        }
        return result;
    }

    private void initDialog(){

    }
}
