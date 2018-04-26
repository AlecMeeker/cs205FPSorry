package Logic;

import java.util.ArrayList;

public class AI extends Player {

    public Boolean smart; //smart or dumb ai
    public Boolean cruel; //cruel or nice ai
    public String demeanor; //string used to interface with the database
    public String name; //the name

    public AI() {
        super(Color.NULL, new Board());
    }

    public AI(Color inColor, Board inBoard, boolean isSmart, boolean isCruel, String name) {
        super(inColor, inBoard);
        this.name = name;
        System.out.println(name + " entered the arena for Team " + inColor.toString() + "!\n");

        smart = isSmart;
        cruel = isCruel;
        String smartness = "";
        String cruelty = "";
        if (smart) {
            smartness = "Smart";
        }
        else {
            smartness = "Dumb";
        }
        if (cruel) {
            cruelty = "Cruel";
        }
        else {
            cruelty = "Nice";
        }
        demeanor = cruelty + "/" + smartness;
    }

    @Override
    public void drawStep() {
        System.out.println("It's " + name + "'s turn (" + color.toString().toLowerCase() + ")");

        System.out.println("pawns in start: " + startPawnList.size() + "; pawns movable: " + movablePawnList.size() + "; pawns finished: " + finishedPawnList.size());
        System.out.print("StartList: ");
        for (Pawn p : startPawnList) {
            System.out.print(p.getCurrentBlock().id + " ");
        }
        System.out.print("\nMovableList: ");
        for (Pawn p : movablePawnList) {
            System.out.print(p.getCurrentBlock().id + " ");
        }
        System.out.print("\n");

        currentDraw = thisBoard.thisDeck.draw();
        generateMoves();
        if (smart) {
            enactBestMoveIndex(currentDraw);
        }
        else {
            enactRandomMove(currentDraw);
        }
        if (finishedPawnList.size() == 4) {
            System.out.println(name + " WINS!");
        }
    }

    public void selectPawnStep() { }
    public void selectEndBlockStep() { }
    public void selectSecondPawnStep(){ }
    public void selectSecondEndBlockStep(){ }

    /*
	rates a with move with an integer, to allow for optimal move selection
	 */
    public double rateMove(Move m) {
        int crueltyMultiplier = 1;
        if (cruel) {
            crueltyMultiplier = 2;
        }

        double moveRating = 0;

        moveRating += m.frontPawns;
        moveRating -= m.backPawns * .5; //doesn't seem like we ought to punish that too harshly

        if (m.gotHome) {
            moveRating += 3;
        }
        if (m.leftStart) {
            moveRating += 2.5;
        }
        if (m.bounced) {
            for (int i = 0; i < m.whomBounced.size(); i++) {
                moveRating += 2 * crueltyMultiplier;
                moveRating += m.whomBounced.get(i).myPlayer.movablePawnList.size(); //targets players who have a lot of movable pawns first-off
                moveRating += ( 65 - m.whomBounced.get(i).getDistanceFromHome() / 65); //targets pawns that are closest to their goal,
            }
        }

        rankPawns();
        if (movablePawnList.contains(m.p)) {
            moveRating += ((4 - this.movablePawnList.indexOf(m.p)) * .25); //+ .25 for doing the front moves first
        }

        if (m.slid) {
            moveRating += 1;
        }



        return moveRating;
    }

    public void enactBestMoveIndex(Card draw) {
        if (potentialMovesList.get(0).size() == 0) {
            System.out.println("No possible moves. Pass\n");
            return;}

        movablePawnList = rankPawns(movablePawnList);
        ArrayList<Double> ratedList = new ArrayList<>();
        if (draw == Card.SEVEN) {
            int i = 0;
            for (Move m : potentialMovesList.get(0)) {
                ratedList.add(rateMove(potentialMovesList.get(0).get(i)) + rateMove(potentialMovesList.get(1).get(i)));
                i++;
            }
        }
        else {
            for (Move m : potentialMovesList.get(0)) {
                ratedList.add(rateMove(m));
            }
        }

        double best = 0;
        int bestIndex = 0;
        for (int d = 0; d < ratedList.size(); d++) {
            if (ratedList.get(d) > best) {
                best = ratedList.get(d);
                bestIndex = d;
            }
        }

        //The part that does the actual move
        Move thisMove = potentialMovesList.get(0).get(bestIndex);
        thisMove.enactMove();
        shiftPawns(thisMove);
        if (draw == Card.SEVEN) {
            Move secondMove = potentialMovesList.get(1).get(bestIndex);
            secondMove.enactMove();
            shiftPawns(secondMove);
        }


    }

    public void enactRandomMove(Card draw) {
        //if empty do nothing
        if (potentialMovesList.get(0).size() == 0) {
            System.out.println("No possible moves. Pass\n");
            return;}

        //generate a random index as choice
        int randomIndex = (int) (Math.random() * potentialMovesList.get(0).size());

        if (draw == Card.SEVEN) {
            Move secondMove = potentialMovesList.get(1).get(randomIndex);
            secondMove.enactMove();
            shiftPawns(secondMove);
        }
        Move thisMove = potentialMovesList.get(0).get(randomIndex);
        thisMove.enactMove();
        shiftPawns(thisMove);

    }

    public String getName() {
        return name;
    }


}
