package Main.src;

/*
 Returns a move, containing fields used to rank relative quality of the move in various AI
  */
public class Move {

    public Block blockReached;
    public boolean bounced; //did this move bounce a player
    public int whomBounced; //which player bounced (winning players are better targets)
    public boolean slid; //if went over a slide
    public boolean gotSafe; //if got
    public boolean gotHome;
    public boolean gotOut;

    public Move(Pawn thisPawn, Block startingBlock, Card draw) {
        bounced = false;
        whomBounced = null;
        slid = false;
        gotSafe = false;
        gotHome = false;
        gotOut = false;

        testMove(thisPawn, startingBlock, draw);

    }

    private void testMove(Pawn thisPawn, Block startingBlock, Card draw) {
        Block currentBlock = startingBlock;
        switch (draw) {
            case ONE:
            case TWO: {
                if (!thisPawn.isOut) {
                    //starting block will be the actual first block
                    return startingBlock.nextBlock();
                }
            }
            case THREE:
            case FIVE:
            case EIGHT:
            case TWELVE: {
                for (int i = 0; i < draw.num, i++) {
                    currentBlock = currentBlock.nextBlock();
                }
                currentBlock = block.trySpecialMove(thisPawn, currentBlock)


                break;
            }
            case FOUR:
            case SEVEN:
            case TEN:
            case ELEVEN:
        }
    }
}

private Block trySpecialMove(Pawn this Pawn, Block startBlock) {
    Block currentBlock = startBlock;
    //to test for sliding
    if (currentBlock.getSlide() == START) {
        slid = true;
        while (currentBlock.getSlide() != NOT) {
            currentBlock = currentBlock.nextBlock();
            //see if it bounches any pawns along its slide
            if (currentBlock.getPawn() != null) {
                currentBlock.getPawn().getBounced();
                bounced = true;
            }
        }
    }
    return currentBlock;

    //to test for bouncing
    if (currentBlock.getPawn() != null) {
        whomBounced = currentBlock.getPawn();
        currentBlock.getPawn().getBounced();
        bounced = true;
    }

    //to test for entering the safe zone
    if (!startBlock.isSafety() && currentBlock.isSafety()) {
        gotSafe = true;
    }

    //test for got home
    if (currentBlock == thisPawn.getHomeLocation()) {
        gotHome = true;
    }

    //test for got out
    if (currentBlock == thisPawn.getStartLocation().nextBlock() && startBlock == thisPawn.getStartLocation()) {
        gotOut = true;
    }







}