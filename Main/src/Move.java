
/*
 Returns a move, containing fields used to rank relative quality of the move in various AI
  */
public class Move {

    public Pawn p;
    public Block blockReached;
    public boolean bounced; //did this move bounce a player
    public Pawn whomBounced; //which player bounced (winning players are better targets)
    public boolean slid; //if went over a slide
    public boolean gotSafe; //if got
    public boolean gotHome;
    public boolean gotOut;

    /*
    computes a move based purely on the integer number rather than the card itself
    used in case you draw a 7, and split that into 1/6. The 1 doesn't count as a 1 to move out
     */
    public Move (Pawn thisPawn, int spaces) {
        this.p = thisPawn;
        bounced = false;
        whomBounced = null;
        slid = false;
        gotSafe = false;
        gotHome = false;
        gotOut = false;

        //Runs the block to see where it lands
        Block currentBlock = move(spaces);
        blockReached = currentBlock;
    }

    public Move (Pawn thisPawn, Pawn targetPawn) {
        this.p = thisPawn;
        bounced = true;
        whomBounced = targetPawn;

        blockReached = targetPawn.getCurrentBlock();
    }

    /*
    helper method to iterate through the board
     */
    private Block move(int spaces) {
        if (p.hasLeftStart() && !p.isFinished()) {

            Block currentBlock = p.getCurrentBlock();
            if (spaces > 0) {
                for (int i = 0 ; i < spaces; i ++) {
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
        return p.getCurrentBlock();
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
                    //currentBlock.getPawn().getBounced();
                    bounced = true;
                }
            }
        }

        //to test for bouncing
        if (currentBlock.getPawn() != null) {
            whomBounced = currentBlock.getPawn();
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
        if (currentBlock == thisPawn.getStartLocation().getNextBlock(thisPawn.getColor()) && startBlock == thisPawn.getStartLocation()) {
            gotOut = true;
        }

        //returns the block that the pawn ultimately could land on
        return currentBlock;


    }
}