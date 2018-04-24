package Logic;

import java.util.ArrayList;

public class Game {

    public static void playGame() {
        Board gameBoard = new Board();
        ArrayList<Player> players = new ArrayList<>();

        boolean whilePlaying = true;

        //DISPLAY GAME

        //get # of AI players and stats

        int AI_PLAYERS = 3; //change this once you meet with junziao TESTING ONLY TESTING ONLY

        //USE THIS FOR TESTING ONLY
        String name = "test";
        Color youColor = Color.RED;
        HumanPlayer you = new HumanPlayer(name, youColor, gameBoard);

        //turn this off to pit AI against each other
        //players.add(you);

        switch(AI_PLAYERS) {
            case 3:
                //FOR TESTING ONLY
                Color firstColor = Color.BLUE;
                boolean firstSmart = true, firstCruel = false;
                players.add(new AI(firstColor, gameBoard, firstSmart, firstCruel));
            case 2:
                //TESTING ONLY
                Color secondColor = Color.GREEN;
                boolean secondSmart = false, secondCruel = true;
                players.add(new AI(secondColor, gameBoard, secondSmart, secondCruel));
            case 1:
                //TESTING ONLY
                Color thirdColor = Color.YELLOW;
                boolean thirdSmart = true, thirdCruel = true;
                players.add(new AI(thirdColor, gameBoard, thirdSmart, thirdCruel));
        }

        ArrayList<Player> allPlayers = new ArrayList<>();
        allPlayers.add(you);
        allPlayers.addAll(players);

        int currentMove = 0;
        Player currentPlayer;

        System.out.println("*************************************************");
        System.out.println("*****************GAME BEGIN**********************");
        System.out.println("*************************************************");
        while (whilePlaying) {
            currentPlayer = players.get(currentMove%(players.size()));
            whilePlaying = currentPlayer.play();

            currentMove++;
        }





    }

}
