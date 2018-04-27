package Logic;

public class HumanPlayer extends Player {
    private String name;
    private Game thisGame;

    /**
     * Default constructor
     */
    public HumanPlayer(){}

    /**
     * TODO
     * @param name
     * @param inColor
     * @param inBoard
     * @param thisGame
     */
    public HumanPlayer(String name, Color inColor, Board inBoard, Game thisGame) {
        super(inColor, inBoard);
        this.thisGame = thisGame;
        this.name = name;
        System.out.println(name + " entered the arena for Team " + inColor.toString() + "!\n");
    }

    /**
     * TODO
     * @return
     */
    public boolean play() {
        return false;
    }

    /**
     * TODO
     */
    public void drawStep() {
        int choice = 1;
        switch (choice) {
            case 0:
                thisGame.quitGame();
                break;
            case 1:
                currentDraw = thisBoard.thisDeck.draw();
                break;

        }

        System.out.println("You drew " + currentDraw.toString());
        generateMoves();
        this.refreshHighlight();
    }

    /**
     * TODO
     */
    public void selectPawnStep() {
        for (Move m : potentialMovesList.get(0)) {
            if (m.p.selected) {
                this.refreshHighlight();
            }
        }
    }

    /**
     * TODO
     */
    public void selectEndBlockStep() {
        for (Move m : potentialMovesList.get(0)) {
            if (m.blockReached.selected) {
                m.enactMove();

                m.blockReached.selected = false;

                if (finishedPawnList.size() == 4) {
                    System.out.println("You win!!!");
                }
                break;
            }
        }
    }

    /**
     * TODO
     */
    public void selectSecondPawnStep() {
        for (Move m : potentialMovesList.get(1)) {
            if (m.p.selected) {
                this.refreshHighlight();
            }
        }
    }

    /**
     * TODO
     */
    public void selectSecondEndBlockStep() {
        for (Move m : potentialMovesList.get(1)) {
            if (m.blockReached.selected) {
                m.enactMove();
                if (finishedPawnList.size() == 4) {
                    System.out.println("You win!!!");
                }
            }
        }
    }

    /**
     * Gets the name of the player
     * @return  name of player
     */
    public String getName() {
        return name;
    }
}

