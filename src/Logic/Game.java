package Logic;

import java.util.ArrayList;

public class Game {
    public Player currentPlayer;

    public ArrayList<Pawn> everyPawn;
    public int currentMove;
    public ArrayList<Player> allPlayers;

    public void playGame() {
        Board gameBoard = new Board();
        ArrayList<Player> players = new ArrayList<>();

        boolean whilePlaying = true;

        //DISPLAY GAME

        //get # of AI players and stats

        int AI_PLAYERS = 2; //change this once you meet with junziao TESTING ONLY TESTING ONLY

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

        Color yourColor = Color.RED;
        Color firstColor = generateColor();
        Color secondColor = generateColor();
        Color thirdColor = generateColor();

        while (firstColor == youColor) {
            firstColor = generateColor();
        }
        while (secondColor == firstColor || secondColor == youColor) {
            secondColor = generateColor();
        }
        while (thirdColor == firstColor || thirdColor == secondColor || thirdColor == youColor) {
            thirdColor = generateColor();
        }


        switch(AI_PLAYERS) {
            case 3:
                boolean firstSmart = false, firstCruel = false;
                players.add(new AI(firstColor, gameBoard, firstSmart, firstCruel, name1));
            case 2:
                boolean secondSmart = true, secondCruel = false;
                players.add(new AI(secondColor, gameBoard, secondSmart, secondCruel, name2));
            case 1:
                boolean thirdSmart = true, thirdCruel = false;
                players.add(new AI(thirdColor, gameBoard, thirdSmart, thirdCruel, name3));
        }

        allPlayers = new ArrayList<>();
        allPlayers.add(you);
        allPlayers.addAll(players);

        everyPawn = new ArrayList<>();

        for (Player p : players) {
            everyPawn.addAll(p.startPawnList);
            everyPawn.addAll(p.movablePawnList);
            everyPawn.addAll(p.finishedPawnList);
        }

        currentMove = 0;

        System.out.println("*************************************************");
        System.out.println("*****************GAME BEGIN**********************");
        System.out.println("*************************************************");
        while (whilePlaying) {


            System.out.print("Turn " + currentMove + "\n");
            System.out.print("All pawns: ");

            String safe = " ";
            for (Pawn p : everyPawn) {

                System.out.print(p.getCurrentBlock().id + safe);
            }
            System.out.print("\nOuterRing: ");
            //to show where all the pawns are of course
            for (int i = 0; i < 60; i++) {
                if (gameBoard.outerRing[i].getPawn() != null) {
                    System.out.print(i + ": " + gameBoard.outerRing[i].getPawn().getColor().toString().toLowerCase() + "; ");
                }
            }
            System.out.println("\n*****************");

            currentPlayer = players.get(currentMove%(players.size()));
            whilePlaying = currentPlayer.play();

            currentMove++;


        }
    }

    public void saveGame() {
        String saveState = "";
        for (Player p : allPlayers) {
            saveState += p.startPawnList.size() + ";";
            for (Pawn myPawn : p.movablePawnList) {

            }
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

    public static Color generateColor() {
        int cIndex = (int) (Math.random() * 4);
        switch (cIndex) {
            case 0:
                return Color.RED;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.YELLOW;
            default:
                return Color.NULL;
        }
    }

    public void loadState(String inState) {



    }
}


