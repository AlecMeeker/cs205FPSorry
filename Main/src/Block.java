public class Block {
    Block id;
    Color color;
    Block previousBlock, nextBlock;
    Block nextSafetyBlock;

    public enum Slidability {START, MIDDLE, NOT};

    Slidability slideSstatus;
    ArrayList<Pawn> pawnsHere;

    public Block(Color inColor, int id) {
        this.color = inColor;
        this.id = id;
        slideSstatus = NOT;
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

    public Pawn getPawn() {
        return pawnsHere[0];
    }

    public void setSlideSstatus(int i) {
        switch (i){
            case 0: slideSstatus = NOT;
                break;
            case 1: slideStatus = START;
                break;
            case 2: slideStatus = MIDDLE;
                break;
        }
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
        if (id != -1 && pawnsHere.size() != 0){
            pawnsHere.get(0).getBounced();
            pawnsHere.remove(0);
            pawnsHere.add(pawn);
            return false;
        }
        else {
            pawnsHere.add(pawn);
            return true;
        }
    }
}
