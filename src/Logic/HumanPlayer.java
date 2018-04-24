package Logic;

public class HumanPlayer extends Player {
    private String name;

    public HumanPlayer(String name, Color inColor, Board inBoard) {
        super(inColor, inBoard);
        this.name = name;
        System.out.println(name + " entered the arena for Team " + inColor.toString() + "!\n");
    }

    @Override
    public boolean play() {
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
