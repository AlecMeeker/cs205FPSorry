package Logic;

import java.util.ArrayList;

public class Block {
    int id; //block's location ID
    Color color; //color, if its a special block that interfaces with a pawn's color
    Block previousBlock, nextBlock; //the blocks immediately after and before
    Block nextSafetyBlock; //the next block if this is one of those exit spaces for a certain color

    public boolean highlighted; //for highlighting this block in the GUI
    public boolean selected = false; //for selecting this block in the GUI, possibly unnecessary

    public boolean isHome; //if the block is a home block

    public Slidiness slideStatus; //if the block contains a slide, has different slideStatus such

    ArrayList<Pawn> pawnsHere; //the pawns situated on this block

    /**
     * Normal block constructor
     *
     * @param inColor : this block's color
     * @param id      : this block's id
     * @param isHome  : if this block is a home block
     */
    public Block(Color inColor, int id, boolean isHome) {
        pawnsHere = new ArrayList<>();
        this.isHome = isHome;
        this.color = inColor;
        this.id = id;
        this.slideStatus = Slidiness.NOT;
    }

    /**
     * Gets the next block, or a safety block one if given a pawn's color
     *
     * @param pawnColor : the pawn moving's color. for generic movements and tests, given null
     * @return the next block
     */
    public Block getNextBlock(Color pawnColor) {
        if (color == pawnColor && nextSafetyBlock != null) {
            return nextSafetyBlock;
        } else {
            return getNextBlock();
        }
    }

    /**
     * gets the first pawn on the block
     *
     * @return
     */
    public Pawn getPawn() {
        if (pawnsHere.size() == 0) {
            return null;
        }
        return pawnsHere.get(0);
    }

    /**
     * gets the
     *
     * @param slideIn
     */
    public void setSlideStatus(Slidiness slideIn) {
        this.slideStatus = slideIn;
    }

    public Slidiness getSlideStatus() {
        return slideStatus;
    }

    /**
     * Gets the next block
     *
     * @return next block
     */
    private Block getNextBlock() {
        return nextBlock;
    }

    /**
     * Sets the next block
     *
     * @param nextBlock next block
     */
    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    /**
     * Gets the next safety block
     *
     * @return next safety block
     */
    public Block getNextSafetyBlock() {
        return nextSafetyBlock;
    }

    /**
     * Sets the next safety block
     *
     * @param nextSafetyBlock next safety block
     */
    public void setNextSafetyBlock(Block nextSafetyBlock) {
        this.nextSafetyBlock = nextSafetyBlock;
    }

    /**
     * Gets the previous block
     *
     * @return previous block
     */
    public Block getPreviousBlock() {
        return previousBlock;
    }

    /**
     * Sets the previous block
     *
     * @param previousBlock previous block
     */
    public void setPreviousBlock(Block previousBlock) {
        this.previousBlock = previousBlock;
    }

    /**
     * Places a pawn on this block and gets rid of any pawn still on this block
     *
     * @param pawn the pawn being placed
     * @return whether or not a pawn was removed
     */
    public boolean place(Pawn pawn) {
        if ((id != -10 || !isHome) && pawnsHere.size() != 0) {
            //pawnsHere.get(0).getBounced();
            pawnsHere.remove(0);
            pawnsHere.add(pawn);
            return false;
        } else {
            pawnsHere.add(pawn);
            return true;
        }
    }

    /**
     * This method removes any pawns from this block
     *
     * @return whether or not any pawns were removed
     */
    public boolean removePawn() {
        if (pawnsHere.size() != 0) {
            pawnsHere.clear();
            ;
            return true;
        }
        return false;
    }

}
