import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {

    private Color color;
    private Board thisBoard;
    private ArrayList<Pawn> movablePawnList; //a list of the player's pawns that can be moved (not finished, not in start)
    private ArrayList<Pawn> homePawnList; //a list of pawns still in home
    private ArrayList<Pawn> finishedPawnList;
    ArrayList<ArrayList<Move>> potentialMovesList; //first arraylist at index 0 is potential moves for each pawn. second arraylist at index 1 is in case of 7, generate potential moves

    Player(Color inColor, Board thisBoard) {
        this.thisBoard = thisBoard;
        this.color = inColor;

        homePawnList.add(new Pawn(color, thisBoard));

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

    /*
    Generates an ArrayList of ArrayList<Move> which is all the possible moves for each turn
    First ArrayList at index 0 is the typical list, every possible move
    if size > 1 then the ArrayList at index 1 represents the double moves allowed by a 7, matched by index to the first list
     */
    private void generateMoveList(Card draw) {
        potentialMovesList = new ArrayList<>();
        System.out.println("Generating possibilities");

        switch(draw) {
            case ONE:
            case TWO:
                for (Pawn p : homePawnList) {
                    potentialMovesList.get(0).add(new Move(p, 1));
                }
                for (Pawn pp : movablePawnList) {
                    if (pp.canMoveThisFar(draw.num)) {
                        potentialMovesList.get(0).add(new Move(pp, draw.num));
                    }
                }
                break;


            case FOUR:
                for (Pawn p : movablePawnList) {
                    potentialMovesList.get(0).add(new Move(p, -4));
                }
                break;


            case SEVEN:
                //create a moveList for each possible combination
                //possibilties: any two pawns, any two that add up to 7 {0/7, 1/6, 2/5, 3/4}
                ArrayList<Pair<Move, Move>> sevenMoveList = new ArrayList<>();

                //Creates all possible pairs of legal moves based on the 7
                for (Pawn p : movablePawnList) {
                    for (int i = 0; i < 4; i++) {
                        if (p.canMoveThisFar(i)) {
                            Move firstMove = new Move(p, i);
                            for (Pawn p2 : movablePawnList) {
                                if (p2 != p && p2.canMoveThisFar(7-i)) {
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
                break;


            case TEN:
                for (Pawn p : movablePawnList) {
                    if (p.canMoveThisFar(draw.num)) {
                        potentialMovesList.get(0).add(new Move(p, 10));
                    }
                    potentialMovesList.get(0).add(new Move(p, -1));
                }
                break;


            case ELEVEN:
                for (Pawn p : movablePawnList) {
                    //Generates moves if you don't switch
                    if (p.canMoveThisFar(11)) {
                        potentialMovesList.get(0).add(new Move(p, 11));
                    }

                    //Generates moves for a switch
                    for (int i = 0; i < 60; i++) {
                        if (thisBoard.outerRing[i].pawnsHere.size() != 0 && thisBoard.outerRing[i].getPawn().getColor() != p.getColor()) {
                            potentialMovesList.get(0).add(new Move(p, thisBoard.outerRing[i].getPawn()));
                        }
                    }
                }
                break;


            case SORRY:
                for (Pawn p : homePawnList) {

                    //Generates moves for a switch
                    for (int i = 0; i < 60; i++) {
                        if (thisBoard.outerRing[i].pawnsHere.size() != 0 && thisBoard.outerRing[i].getPawn().getColor() != p.getColor()) {
                            potentialMovesList.get(0).add(new Move(p, thisBoard.outerRing[i].getPawn()));
                        }
                    }
                }
                break;


            default :
                for (Pawn p : movablePawnList) {
                    if (p.canMoveThisFar(draw.num)) {
                        potentialMovesList.get(0).add(new Move(p, draw.num));
                        System.out.println("Move added to possibilities");
                    }
                }
                break;
        }
    }
}







