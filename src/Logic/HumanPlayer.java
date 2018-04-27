package Logic;

public class HumanPlayer extends Player {
    private String name;
    private Game thisGame;


    public HumanPlayer(){

    }
    public HumanPlayer(String name, Color inColor, Board inBoard, Game thisGame) {
        super(inColor, inBoard);
        this.thisGame = thisGame;
        this.name = name;
        System.out.println(name + " entered the arena for Team " + inColor.toString() + "!\n");
    }

    public boolean play() {
        return false;
    }

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

    public void selectPawnStep() {
        for (Move m : potentialMovesList.get(0)) {
            if (m.p.selected) {
                this.refreshHighlight();
            }
        }
    }

    public void selectEndBlockStep() {
        for (Move m : potentialMovesList.get(0)) {
            if (m.blockReached.selected) {
                m.enactMove();
                if (finishedPawnList.size() == 4) {
                    System.out.println("You win!!!");
                }
            }
        }
    }

    public void selectSecondPawnStep() {
        for (Move m : potentialMovesList.get(1)) {
            if (m.p.selected) {
                this.refreshHighlight();
            }
        }
    }

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

    public String getName() {
        return name;
    }
}

