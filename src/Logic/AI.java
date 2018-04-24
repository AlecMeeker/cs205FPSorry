package Logic;

import java.util.ArrayList;

public class AI extends Player {

    public Boolean smart;
    public Boolean cruel;
    public String name;

    public AI(Color inColor, Board inBoard, boolean isSmart, boolean isCruel) {
        super(inColor, inBoard);
        smart = isSmart;
        cruel = isCruel;
        name = generateName();
    }

    @Override
    public boolean play() {
        System.out.println("It's " + name + "'s turn.");
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
            return false;
        }
        return true;
    }

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


    public String generateName() {
        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("Alexei");
        nameList.add("Anatoly");
        nameList.add("Boris");
        nameList.add("Vasily");
        nameList.add("Vladislav");
        nameList.add("Vitaly");
        nameList.add("Dmitry");
        nameList.add("Ivan");
        nameList.add("Konstantin");
        nameList.add("Nikita");
        nameList.add("Pyotr");
        nameList.add("Sergei");
        nameList.add("Yuri");

        int nameIndex = (int)(Math.random() * nameList.size());

        return nameList.get(nameIndex);
    }
}
