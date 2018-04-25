package Logic;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
	protected Card currentDraw;

	protected int bounces;
	protected Color color;
	protected Board thisBoard;
	protected String name;

	protected ArrayList<Pawn> movablePawnList; //a list of the player's pawns that can be moved (not finished, not in start)
    protected ArrayList<Pawn> startPawnList; //a list of pawns still in home
	protected ArrayList<Pawn> finishedPawnList; //a list of pawns who have won

	public ArrayList<ArrayList<Move>> potentialMovesList; //first arraylist at index 0 is potential moves for each pawn. second arraylist at index 1 is in case of 7, generate potential moves
	
	protected Player(Color inColor, Board thisBoard) {

		startPawnList = new ArrayList<>();
		movablePawnList = new ArrayList<>();
		finishedPawnList = new ArrayList<>();

		bounces = 0;

		this.thisBoard = thisBoard;
		this.color = inColor;
		for (int i = 0 ; i < 4; i++) {
			startPawnList.add(new Pawn(thisBoard, this));
		}
	}

	/*
	generates a player with pawns at given locations. Used to load a game
	 */
	protected Player(Color inColor, int startListSize, int finishedListSize, List<Integer> ids) {
		startPawnList = new ArrayList<>();
		for (int i = 0; i < startListSize; i++) {
			startPawnList.add(new Pawn(thisBoard, this));
		}
		for (int j = 0; j < finishedListSize; j++) {
			finishedPawnList.add(new Pawn(thisBoard, this, thisBoard.getGoalLocation(color)));
		}
		for (Integer id : ids) {
			if (id >= 0) {
				movablePawnList.add(new Pawn(thisBoard, this, thisBoard.outerRing[id]));
			}
			else {
				movablePawnList.add(new Pawn(thisBoard, this, thisBoard.getSafeBlock(id, color)));
			}
		}
	}

	//helper function
	private Card draw() {
		return thisBoard.thisDeck.draw();
	}

	public void rankPawns() {
		movablePawnList = rankPawns(movablePawnList);
	}

	protected ArrayList<Pawn> rankPawns(ArrayList<Pawn> thisList) {
		if (thisList.size() != 0) {

			int furthest = -10;
			int topIndex = -1;
			for (int i = 0; i < thisList.size(); i++) {
				if (thisList.get(i).getDistanceFromHome() > furthest) {
					furthest = thisList.get(i).getDistanceFromHome();
					topIndex = i;
				}
			}
			Pawn temp = thisList.get(topIndex);
			thisList.remove(topIndex);

			if (thisList.size() > 1) {
				thisList = rankPawns(thisList);
			}

			thisList.add(temp);
		}

		int pawnsInBack = 0;
		return thisList;
	}

	/*
	Handles the actual drawing and generating moves based off of a draw. When the draw button is pressed (or auto pressed by AI), generateMoves()
	 */
	public void generateMoves() {
		this.generateMoveList(currentDraw);
	}

	/*
	Generates an ArrayList of ArrayList<Move> which is all the possible moves for each turn
	First ArrayList at index 0 is the typical list, every possible move
	if size > 1 then the ArrayList at index 1 represents the double moves allowed by a 7, matched by index to the first list
	 */
	private void generateMoveList(Card draw) {
		potentialMovesList = new ArrayList<>();
		potentialMovesList.add(new ArrayList<Move>());
		potentialMovesList.add(new ArrayList<Move>());
	    System.out.println("...generating possibilities...");

	    switch(draw) {
			case ONE:
			case TWO:
				for (Pawn p : startPawnList) {
					if (p.canMoveHere(1)) {
						potentialMovesList.get(0).add(new Move(p, 1));
					}
				}
				for (Pawn pp : movablePawnList) {
					if (pp.canMoveHere(draw.num)) {
						potentialMovesList.get(0).add(new Move(pp, draw.num));
					}
				}
				break;


			case FOUR:
				for (Pawn p : movablePawnList) {
					if (p.canMoveHere(-4)) {
						potentialMovesList.get(0).add(new Move(p, -4));
					}
				}
				break;


			case SEVEN:
				//create a moveList for each possible combination
				//possibilties: any two pawns, any two that add up to 7 {0/7, 1/6, 2/5, 3/4}
				ArrayList<Pair<Move, Move>> sevenMoveList = new ArrayList<>();

				//Creates all possible pairs of legal moves based on the 7
				if (movablePawnList.size() == 1 && movablePawnList.get(0).canMoveHere(7)) {
					Move thisMove = new Move(movablePawnList.get(0), 7);
				}

				for (Pawn p : movablePawnList) {
					for (int i = 0; i < 4; i++) {
						if (p.canMoveHere(i)) {
							Move firstMove = new Move(p, i);
							for (Pawn p2 : movablePawnList) {
								if (p2 != p && p2.canMoveHere(7-i)) {
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
					if (p.canMoveHere(draw.num)) {
						potentialMovesList.get(0).add(new Move(p, 10));
					}
					if (p.canMoveHere(-1)) {
						potentialMovesList.get(0).add(new Move(p, -1));
					}
				}
				break;


			case ELEVEN:
				for (Pawn p : movablePawnList) {
					//Generates moves if you don't switch
					if (p.canMoveHere(11)) {
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
				for (Pawn p : startPawnList) {

					//Generates moves for a switch
					for (int i = 0; i < 60; i++) {
						if (thisBoard.outerRing[i].getPawn() != null && thisBoard.outerRing[i].getPawn().getColor() != p.getColor()) {
							potentialMovesList.get(0).add(new Move(p, thisBoard.outerRing[i].getPawn()));
						}
					}
				}
				break;


			default :
				for (Pawn p : movablePawnList) {
					if (p.canMoveHere(draw.num)) {
						potentialMovesList.get(0).add(new Move(p, draw.num));
						System.out.println("Move added to possibilities");
					}
				}
				break;
		}
	}


	/*
	helper class that puts the pawns in the correct buckets based on the move
	 */
	public void shiftPawns(Move moveIn) {
	
		if (moveIn.sorry) {
			startPawnList.remove(moveIn.p);
			movablePawnList.add(moveIn.p);
		}
		if (moveIn.leftStart) {
			startPawnList.remove(moveIn.p);
			movablePawnList.add(moveIn.p);
		}
	}

	public abstract boolean play();

	public abstract String getName();

	public void refreshHighlight() {
		for (Move m : potentialMovesList.get(0)) {
			m.p.highlighted = true;
			if (m.p.selected) {
				m.blockReached.highlighted = true;
			}
		}
	}

	public int getPawnsInHome() {
		return finishedPawnList.size();
	}

	public int getPawnsInStart() {
		return startPawnList.size();
	}


}



