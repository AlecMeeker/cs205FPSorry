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

        String name1 = generateName();
        String name2 = generateName();
        String name3 = generateName();

        while (name2 == name1) {
            name2 = generateName();
        }
        while (name3 == name2 || name3 == name1) {
            name3 = generateName();
        }


        switch(AI_PLAYERS) {
            case 3:
                //FOR TESTING ONLY
                Color firstColor = Color.BLUE;
                boolean firstSmart = true, firstCruel = false;
                players.add(new AI(firstColor, gameBoard, firstSmart, firstCruel, name1));
            case 2:
                //TESTING ONLY
                Color secondColor = Color.GREEN;
                boolean secondSmart = false, secondCruel = true;
                players.add(new AI(secondColor, gameBoard, secondSmart, secondCruel, name2));
            case 1:
                //TESTING ONLY
                Color thirdColor = Color.YELLOW;
                boolean thirdSmart = true, thirdCruel = true;
                players.add(new AI(thirdColor, gameBoard, thirdSmart, thirdCruel, name3));
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

            //to show where all the pawns are of course
            for (int i = 0; i < 60; i++) {
                if (gameBoard.outerRing[i].getPawn() != null) {
                    System.out.print(i + ": " + gameBoard.outerRing[i].getPawn().getColor().toString().toLowerCase() + "; ");
                }
            }
            System.out.println();

            currentPlayer = players.get(currentMove%(players.size()));
            whilePlaying = currentPlayer.play();

            currentMove++;


        }





    }

    public static String generateName() {
        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("Alexei");
        nameList.add("Artyom");
        nameList.add("Alyosha");
        nameList.add("Anatoly");
        nameList.add("Boris");
        nameList.add("Vasily");
        nameList.add("Vladislav");
        nameList.add("Vitaly");
        nameList.add("Vladimir");
        nameList.add("Dmitry");
        nameList.add("Ivan");
        nameList.add("Leonid");
        nameList.add("Mikhail");
        nameList.add("Georgy");
        nameList.add("Konstantin");
        nameList.add("Nikita");
        nameList.add("Rasputin");
        nameList.add("Pyotr");
        nameList.add("Sergei");
        nameList.add("Stanislav");
        nameList.add("Yuri");
        nameList.add("Vladimir Putin himself");
        nameList.add("Guccifer2.0");
        nameList.add("Fancy Bear");
        nameList.add("Julian Assange");
        nameList.add("Anonymous");

        int nameIndex = (int)(Math.random() * nameList.size());

        return nameList.get(nameIndex);
    }
}
