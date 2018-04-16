public class Block {
    Block id;
    Color color;
    Block previousBlock, nextBlock;
    Block nextSafetyBlock;
    Pawn pawnHere;

    public Block(Color inColor, int id) {
        this.color = inColor;
        this.id = id;
    }

    //returns the first safety block if it's the right color and location
    public Block getNextBlock(Color pawnColor) {
        if (color == pawnColor && nextSafetyBlock != null) {
            return nextSafetyBlock;
        }
        else {
            return getNextBlock();
        }
    }

    public Block getNextBlock() {
        return nextBlock;
    }
    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    public Block getPreviousBlock() {
        return previousBlock;
    }
    public void setPreviousBlock(Block previousBlock) {
        this.previousBlock = previousBlock;
    }
}
