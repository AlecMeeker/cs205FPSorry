package Logic;

public class HumanPlayer extends Player {
    private String name;
    private Game thisGame;

    public HumanPlayer(String name, Color inColor, Board inBoard, Game thisGame) {
        super(inColor, inBoard);
        this.thisGame = thisGame;
        this.name = name;
        System.out.println(name + " entered the arena for Team " + inColor.toString() + "!\n");
    }

    @Override
    public boolean play() {
        int choice = 0;
        switch (choice) {
            case 0:
                thisGame.quitGame();
                break;
            case 1:
                currentDraw = thisBoard.thisDeck.draw();
                break;


        }


        currentDraw = thisBoard.thisDeck.draw();
        System.out.println("You drew " + currentDraw.toString());
        generateMoves();
        //CHOOSE HOW THE HUMAN SELECTS AN OPTION AND THEN IMPLEMENT THIS
        if (finishedPawnList.size() == 4) {
            System.out.println("You win!!!");
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }
}

