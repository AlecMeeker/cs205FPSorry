package Main.sql;

import Main.src.Color;

public class DBTesting {
    public static void main(String[] args){
        System.out.println(ConnectDB.getPlayerWins("Alex"));
        System.out.println(ConnectDB.getNumGames("Alex"));
        System.out.println(ConnectDB.hasEntry("Alex"));
        System.out.println(ConnectDB.updatePlayer("Peter",true,Color.GREEN,3));
        int gameID = ConnectDB.insertGameData("Alex",10,12,Color.GREEN,Color.NULL,"Nice/Smart", "NULL","NULL",0,0,0,0,0,0,0,0,0,0,0,0);
        System.out.println(ConnectDB.updateGame(gameID, 11, 13, Color.RED, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3));
        System.out.println(gameID);
    }
}