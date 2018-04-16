

public class Board {

    Block[] outerRing;
    Public List<Block> board;
    Public Block[] blueSafeZone, redSafeZone, yellowSafeZone, greenSafeZone, HOME_ARRAY;

    /*
    Creates a board of 60 squares, with some safety zones and homes, and links them all
     */
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
                    outerRing[i] = new Block(RED, i);
                    outerRing[i].setNextSafetyBlock(redSafeZone[0]);
                    break;
                case 17:
                    outerRing[i] = new Block(BLUE, i);
                    outerRing[i].setNextSafetyBlock(blueSafeZone[0]);
                    break;
                case 32:
                    outerRing[i] = new Block(YELLOW, i);
                    outerRing[i].setNextSafetyBlock(yellowSafeZone[0]);
                    break;
                case 47:
                    outerRing[i] = new Block(GREEN, i);
                    outerRing[i].setNextSafetyBlock(greenSafeZone[0]);
                    break;
                default:
                    outerRing[i] = new Block(NULL, i);
                    break;
            }
        }
        //now link all the blocks in the square
        for (i = 0; i < 60; i++) {
            if (i == 60){
                linkBlocks(outerRing[i], outerRing[0]);
            }
            else {
                linkBlocks((outerRing[i], outerRing[i+1]));
            }
        }
        //this sets up and links the home blocks
        //stored [0] BLUE [1] RED [2] YELLOW [3] GREEN
        HOME_ARRAY = {new Block(RED, -1), new Block(BLUE, -1), new Block(YELLOW, -1), new Block(GREEN, -1)};
        for (i = 0; i < HOME_ARRAY.size(); i++) {
            HOME_ARRAY[i].setNextBlock(outerRing[4 + (15*i)]);
        }
    }

    public Block[] generateSafetyZone(Color safetyColor){
        System.out.println("Safety zone " + safetyColor.toString() + " generated.");
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

    public void generateSlides(int slideStartIndex) {
        System.out.println("Slides generated.");
        outerRing[slideStartIndex].setSlideStatus(1);
        for (int i = 0; i < 3; i++) {
            outerRing[slideStartIndex + i].setSlideStatus(2);
        }
        newIndex = slideStartIndex + 8;
        outerRing[slideStartIndex].setSlideStatus(1);
        for (int i = 0; i < 4; i++) {
            outerRing[newIndex + i].setSlideStatus(2);
        }

    }

    public Block getRedHome(){
        return HOME_ARRAY[0]; }
    public Block getBlueHome() {
        return HOME_ARRAY{1]; }
    public Block getYellowHome() {
        return HOME_ARRAY[2]; }
    public Block getGreenHome() {
        return HOME_ARRAY[3]; }
}
