package Logic;

import java.util.ArrayList;

/*
 Returns a move, containing fields used to rank relative quality of the move in various AI
  */
public class Move {

    public Pawn p;
    public Block blockReached;
    public boolean bounced; //did this move bounce a player
    public ArrayList<Pawn> whomBounced; //which player bounced (winning players are better targets)
    public boolean slid; //if went over a slide
    public boolean gotSafe; //if got
    public boolean gotHome;
    public boolean leftStart;
    public int frontPawns; //pawns within 10 of the block reached. represents possible targets for later bounces
    public int backPawns; //pawns behind this pawn, representing future potential danger
    public Block origin;

    /*
    computes a move based purely on the integer number rather than the card itself
    used in case you draw a 7, and split that into 1/6. The 1 doesn't count as a 1 to move out
     */
    public Move (Pawn thisPawn, int spaces) {
        this.p = thisPawn;
        origin = thisPawn.getCurrentBlock();
        bounced = false;
        whomBounced = new ArrayList<>();
        slid = false;
        gotSafe = false;
        gotHome = false;
        leftStart = false;

        //Runs the block to see where it lands
        blockReached = move(spaces);
        frontPawns = getFrontPawns();
        backPawns = getBackPawns();
    }

    public Move (Pawn thisPawn, Pawn targetPawn) {
        this.p = thisPawn;
        whomBounced = new ArrayList<>();
        bounced = true;
        whomBounced.add(targetPawn);

        blockReached = targetPawn.getCurrentBlock();
    }

    /*
    helper method to iterate through the board
     */
    private Block move(int spaces) {

        Block currentBlock = p.getCurrentBlock();
        if (spaces > 0) {
            for (int i = 0 ; i < spaces; i++) {
                currentBlock = currentBlock.getNextBlock(p.getColor());
            }
        }
        else {
            for (int i = 0; i < -spaces; i++) {
                currentBlock = currentBlock.getPreviousBlock();
            }
        }
        currentBlock = trySpecialMove(p, currentBlock);
        return currentBlock;
    }

    /*
    helper method to account for special moves within the testMove method
     */
    private Block trySpecialMove(Pawn thisPawn, Block startBlock) {
        Block currentBlock = startBlock;

        //to test for sliding
        if (currentBlock.getSlideStatus() == Slidiness.START) {
            slid = true;
            while (currentBlock.getSlideStatus() != Slidiness.NOT) {
                currentBlock = currentBlock.getNextBlock(thisPawn.getColor());
                //see if it bounches any pawns along its slide
                if (currentBlock.getPawn() != null) {
                    whomBounced.add(currentBlock.getPawn());
                    bounced = true;
                }
            }
        }

        //to test for bouncing
        if (currentBlock.getPawn() != null && currentBlock.getPawn().getColor() != p.getColor()) {
            whomBounced.add(currentBlock.getPawn());
            //currentBlock.getPawn().getBounced(); THIS IS JUST THE POTENTIAL MOVE, DOESN"T ACTUALLY BOUNCE IT HERE
            bounced = true;
        }

        //to test for entering the safe zone
        if ((startBlock.id > 0) && currentBlock.id < 0) {
            gotSafe = true;
        }

        //test for got home
        if (currentBlock == thisPawn.getHomeLocation()) {
            gotHome = true;
        }

        //test for got out
        Block startLocation = thisPawn.getStartLocation();
        Block rightOutsideStart = thisPawn.getStartLocation().getNextBlock(Color.NULL);
        Block shouldBeStart = origin;
        Block nextFromOrigin = origin.getNextBlock(Color.NULL);
        if (origin == thisPawn.getStartLocation() && currentBlock != origin) {
            leftStart = true;
        }

        //returns the block that the pawn ultimately could land on
        return currentBlock;


    }

    /*
    get the 10 pawns in front of where this pawn will land
     */
    private int getFrontPawns() {
        int pawnsInFront = 0;
        if (p.getCurrentBlock().id > -1) {
            Block iterBlock = p.getCurrentBlock();
            for (int i = 0; i < 10; i++) {
                iterBlock = iterBlock.getNextBlock(Color.NULL);
                if (iterBlock.pawnsHere.size() != 0 && iterBlock.getPawn().getColor() != p.getColor()) {
                    pawnsInFront++;
                }
            }
        }
        return pawnsInFront;
    }

    /*
    get the 10 pawns behind where the pawn will land
     */
    private int getBackPawns() {
        int pawnsInBack = 0;
        if (p.getCurrentBlock().id > -1) {
            Block iterBlock = p.getCurrentBlock();
            for (int i = 0; i < 10; i++) {
                iterBlock = iterBlock.getPreviousBlock();
                if (iterBlock.pawnsHere.size() != 0 && iterBlock.getPawn().getColor() != p.getColor()) {
                    pawnsInBack++;
                }
            }
        }
        return pawnsInBack;
    }

    /*
    used to run the move the player has chosen
     */
    public void enactMove() {
        p.setCurrentBlock(blockReached);
        blockReached.place(p);

        //for all those you have bounced in your path, send em home
        if (whomBounced.size() != 0) {
            for (Pawn p : whomBounced) {
                p.getBounced();
            }
        }

        if (leftStart) {
            System.out.println(p.toString() + " left " + p.getColor().toString().toLowerCase() + " start.\n");
        }
        else {
            System.out.println(p.toString() + " moved to " + blockReached.toString() + "\n");
        }
    }
}