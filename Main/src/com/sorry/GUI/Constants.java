package com.sorry.GUI;

public final class Constants {   // this class use for define some constants for GUI

    private static volatile Constants instance = null;
    private static Object mutex = new Object();


    //board Constants
    public final static int boardWidth = 960;
    public final static int boardHeight = 960;
    public final static int boardStartX = 5;
    public final static int boardStartY = 5;
    public final static int totalBlockAroundBoard = 60;

    public final static int pawnWidth = 58;
    public final static int pawnHeight = 58;
    public final static int pawnStartX = 1;
    public final static int pawnStartY = 1;


    //window size
    public final static int windowWidth = 1200;
    public final static int windowHeight = 1020;

    private Constants(){

    }

    public static Constants getInstance(){ //Thread safe singleton model
        Constants result = instance;
            if(result == null){
                synchronized (mutex){
                    result = instance;
                    if(result == null){
                        instance = result = new Constants();
                    }
                }

            }
        return result;
    }
}
