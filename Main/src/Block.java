package Main.src;

public class Block {
    Block id;
    Color color;
    Block previousBlock, nextBlock;
    Pawn pawnHere;

    public Block(Color inColor, int id) {
        this.color = inColor;
        this.id = id;
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
