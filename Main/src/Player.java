import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {

    private Color color;
    private Board thisBoard;
    private Pawn[] pawnArray;
    ArrayList<ArrayList<Move>> potentialMovesList; //first arraylist at index 0 is potential moves for each pawn. second arraylist at index 1 is in case of 7, generate potential moves

    Player(Color inColor, Board thisBoard) {
        thisBoard = thisBoard;
        this.color = inColor;
        pawnArray = new Pawn[]{new Pawn(color, thisBoard), new Pawn(color, thisBoard), new Pawn(color, thisBoard), new Pawn(color, thisBoard)};

        System.out.println("Player created with color =  " + color.toString());

    }

    //helper function
    private Card draw() {
        return thisBoard.thisDeck.draw();
    }

    /*
    Handles the actual drawing and generating moves based off of a draw. When the draw button is pressed (or auto pressed by AI), generateMoves()
     */
    public void generateMoves() {
        this.generateMoveList(this.draw());
    }

    private void generateMoveList(Card draw) {
        potentialMovesList = new ArrayList<ArrayList<Move>>();
        System.out.println("Generating possibilities");
        ArrayList<Move> moveList = new ArrayList();
        if (draw == Card.SEVEN) {
            //create a moveList for each possible combination
            //possibilties: any two pawns, any two that add up to 7 {0/7, 1/6, 2/5, 3/4}
            ArrayList<Pair<Move, Move>> sevenMoveList = new ArrayList<>();

            for (Pawn p : pawnArray) {
                for (int i = 0; i < 4; i++) {
                    if (p.hasLeftStart()) {
                        Move firstMove = new Move(p, i);
                        for (Pawn p2 : pawnArray) {
                            if (p2 != p && p2.hasLeftStart()) {
                                Move secondMove = new Move(p2, 7 - i);
                                Pair<Move, Move> doubleMove = new Pair<>(firstMove, secondMove);
                                sevenMoveList.add(doubleMove);
                            }
                        }
                    }
                }
            }
            for (Pair<Move, Move> movePair : sevenMoveList) {
                potentialMovesList.get(0).add(movePair.getKey());
                potentialMovesList.get(1).add(movePair.getValue());
            }


        }
        else {
            for (Pawn p: pawnArray) {
                if (!p.isFinished()) {
                    potentialMovesList.get(0).add(new Move(p, draw));
                    System.out.println("Move added to possibilities");
                }
            }
        }
    }
}




