package Main.src;

public class Board {

    Public List<Block> board;
    Public Block[] blueSafeZone, redSafeZone, yellowSafeZone, greenSafeZone, HOME_ARRAY;

    public Board() {
        Block[] outerRing = Block[60];
        for (int i = 0; i < 60; i++) {
            outerRing[i] = new Block(NULL, i);
        }
        for (i = 0; i < 60; i++) {
            if (i == 60){
                outerRing[i].setNextBlock(outerRing[0]);
            }
        }
    }

    public Block[] generateSafetyZone(Color safetyColor){
        Block[] newSafetyZone = Block[6];
        for (int i = 0; i < 6; i++) {
            newSafetyZone[i] = new Block(safetyColor, i);
        }
        for (i = 0; i < 6; i++) {

        }
    }

    public void linkBlocks(Block frontBlock, Block backBlock){
        frontBlock.setNextBlock(nextBlock);
        backBlock.setPreviousBlock(frontBlock);
    }

}
