package Logic;

import SQL.ConnectDB;

import java.util.ArrayList;

public class Game {
    private int gameID = -1;

    public Player currentPlayer;
    public Player winner;

    public boolean nextTurn; // boolean representing being allowed to move to the next turn

    public boolean whilePlaying;
    public int currentMove;

    public Board gameBoard;
    public ArrayList<Pawn> everyPawn;
    public ArrayList<Player> allPlayers;

    public Game(Color playerColor, int numPlayers, ArrayList<Integer> aiDifficulties) {
        this.gameBoard = new Board();
        ArrayList<Player> players = new ArrayList<>();

        initGame(3);
    }

    public void initGame(int numAI) {

        //get # of AI players and stats

        int AI_PLAYERS = 3; //change this once you meet with junziao TESTING ONLY TESTING ONLY

        //USE THIS FOR TESTING ONLY
        String name = "test";
        Color youColor = Color.RED;
        HumanPlayer you = new HumanPlayer(name, youColor, gameBoard, this);

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

        allPlayers = new ArrayList<>();
        allPlayers.add(you);

        switch(AI_PLAYERS) {
            case 3:
                boolean firstSmart = false, firstCruel = false;
                allPlayers.add(new AI(firstColor, gameBoard, firstSmart, firstCruel, name1));
            case 2:
                boolean secondSmart = true, secondCruel = false;
                allPlayers.add(new AI(secondColor, gameBoard, secondSmart, secondCruel, name2));
            case 1:
                boolean thirdSmart = true, thirdCruel = false;
                allPlayers.add(new AI(thirdColor, gameBoard, thirdSmart, thirdCruel, name3));
        }

        everyPawn = new ArrayList<>();

        for (Player p : allPlayers) {
            everyPawn.addAll(p.startPawnList);
            everyPawn.addAll(p.movablePawnList);
            everyPawn.addAll(p.finishedPawnList);
        }

        currentMove = 0;

        System.out.println("*************************************************");
        System.out.println("*****************GAME BEGIN**********************");
        System.out.println("*************************************************");

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


    public void quitGame() {
        whilePlaying = false;
        this.saveGame();
        Color winnerColor = (winner == null)?Color.NULL:winner.color;
        String AI1Diff = ((AI)allPlayers.get(1)).demeanor;
        String AI2Diff = (allPlayers.get(2) == null)?"NULL":((AI)allPlayers.get(2)).demeanor;
        String AI3Diff = (allPlayers.get(3) == null)?"NULL":((AI)allPlayers.get(3)).demeanor;
        int playerBounce = allPlayers.get(0).bounces;
        int AI1Bounce = allPlayers.get(1).bounces;
        int AI2Bounce = (allPlayers.get(2) == null)?0:allPlayers.get(2).bounces;
        int AI3Bounce = (allPlayers.get(3) == null)?0:allPlayers.get(3).bounces;
        if (gameID != -1) {
            gameID = ConnectDB.insertGameData(generateName(), 10, currentMove, currentPlayer.color, winnerColor,
                    AI1Diff, AI2Diff, AI3Diff, playerBounce, AI1Bounce, AI2Bounce, AI3Bounce,
                    0, 0, 0, 0, 0, 0, 0, 0);
        } else {
            ConnectDB.updateGameData(gameID, 10, currentMove, winnerColor, playerBounce,
                    AI1Bounce, AI2Bounce, AI3Bounce, 0, 0, 0, 0, 0, 0, 0, 0);
        }
    }

    /*
    the new play() function. Called on click of the next turn button in the gamewindow
     */
    public void nextTurn() {
        clearHighlightAndSelect();
        currentMove++;
        currentPlayer = allPlayers.get(currentMove % allPlayers.size());

        //if current player is an AI player, do all the stuff automatically
        if (currentPlayer.getClass() == (new AI()).getClass()) {
            currentPlayer.drawStep();

            if (currentPlayer.getPawnsInHome() == 4) {
                winner = currentPlayer;
            }

            this.nextTurn();
        }


    }

    public void printData() {
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
    }

    public void clearHighlightAndSelect() {
        for (Block b : gameBoard.everyBlock) {
            b.highlighted = false;
            b.selected = false;
        }
        for (Pawn p : everyPawn) {
            p.highlighted = false;
            p.selected = false;
        }
    }
}


