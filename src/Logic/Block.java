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
     * @param inColor : this block's color
     * @param id : this block's id
     * @param isHome : if this block is a home block
     */
    public Block(Color inColor,  int id, boolean isHome) {
        pawnsHere = new ArrayList<>();
        this.isHome = isHome;
        this.color = inColor;
        this.id = id;
        this.slideStatus = Slidiness.NOT;
    }

    /**
     * Gets the next block, or a safety block one if given a pawn's color
     * @param pawnColor : the pawn moving's color. for generic movements and tests, given null
     * @return the next block
     */
    public Block getNextBlock(Color pawnColor) {
        if (color == pawnColor && nextSafetyBlock != null) {
            return nextSafetyBlock;
        }
        else {
            return getNextBlock();
        }
    }

    /**
     * gets the first pawn on the block
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
     * @param slideIn
     */
    public void setSlideStatus(Slidiness slideIn) {
        this.slideStatus = slideIn;
    }
    public Slidiness getSlideStatus() {
        return slideStatus;
    }



    private Block getNextBlock() {
        return nextBlock;
    }
    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    public Block getNextSafetyBlock() {
        return nextSafetyBlock;
    }
    public void setNextSafetyBlock(Block nextSafetyBlock) {
        this.nextSafetyBlock = nextSafetyBlock;
    }

    public Block getPreviousBlock() {
        return previousBlock;
    }
    public void setPreviousBlock(Block previousBlock) {
        this.previousBlock = previousBlock;
    }

    public boolean place(Pawn pawn) {
        if ((id != -10 || !isHome) && pawnsHere.size() != 0 ){
            //pawnsHere.get(0).getBounced();
            pawnsHere.remove(0);
            pawnsHere.add(pawn);
            return false;
        }
        else {
            pawnsHere.add(pawn);
            return true;
        }
    }

    public boolean removePawn() {
        if (pawnsHere.size() != 0) {
            pawnsHere.clear();;
            return true;
        }
        return false;
    }

}
