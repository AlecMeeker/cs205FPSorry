

public class Board {

    Public List<Block> board;
    Public Block[] blueSafeZone, redSafeZone, yellowSafeZone, greenSafeZone, HOME_ARRAY;

    public Board() {
        Block[] outerRing = Block[60];
        blueSafeZone = generateSafetyZone(BLUE);
        redSafeZone = generateSafetyZone(RED);
        yellowSafeZone = generateSafetyZone(YELLOW);
        greenSafeZone = generateSafetyZone(GREEN);

        for (int i = 0; i < 60; i++) {
            Color thisColor = NULL;
            //set the correct color if it leads to a safety
            switch (i) {
                case 2:
                    thisColor = RED;
                    break;
                case 17:
                    thisColor = BLUE;
                    break;
                case 32:
                    thisColor = YELLOW;
                    break;
                case 47:
                    thisColor = GREEN;
                    break;
            }

            outerRing[i] = new Block(thisColor, i);
        }
        for (i = 0; i < 60; i++) {
            if (i == 60){
                linkBlocks(outerRing[i], outerRing[0]);
            }
            else {
                linkBlocks((outerRing[i], outerRing[i+1]));
            }
        }


        for (int i = 2; i < 60; i+=15) {

        }

    }

    public Block[] generateSafetyZone(Color safetyColor){
        Block[] newSafetyZone = Block[6];
        for (int i = 0; i < 6; i++) {
            newSafetyZone[i] = new Block(safetyColor, i);
        }
        for (i = 0; i < 6; i++) {
            linkBlocks(newSafetyZone[i], newSafetyZone[i+1]);
        }



    }

    public void linkBlocks(Block frontBlock, Block backBlock){
        frontBlock.setNextBlock(nextBlock);
        backBlock.setPreviousBlock(frontBlock);
    }

}
