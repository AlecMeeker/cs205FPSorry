package Main.sql;

import Main.src.Color;

import javax.swing.*;
import java.awt.print.PrinterException;

public class DBTesting {
    public static void main(String[] args) {
        int gameID = ConnectDB.insertGameData("Alex", 10, 12, Color.GREEN, Color.NULL, "Nice/Smart", "NULL", "NULL", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        System.out.println(ConnectDB.updateGameData(gameID, 11, 13, Color.RED, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3));
        System.out.println(gameID);
        System.out.println(ConnectDB.saveGameData(1, "zyxwvut"));
        System.out.println(ConnectDB.loadGameData(1));
        String stats[] = ConnectDB.getPlayerStats("Alex");
        System.out.println("Stats: ");
        for (int i = 0; i < stats.length; i++) {
            System.out.println(stats[i]);
        }
        String info[][] = ConnectDB.getAllGameInfo();
        System.out.println("Info: ");
        for (int i = 0; i < info.length; i++) {
            for (int j = 0; j < info[i].length; j++) {
                System.out.print(info[i][j]);
                System.out.print("  ");
            }
            System.out.println();
        }
        JTable table = ConnectDB.getAsJTable(info);
    }
}