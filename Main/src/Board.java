public class Board {

    private Block[] outerRing;
    //List<Block> board;
    private Block[] blueSafeZone, redSafeZone, yellowSafeZone, greenSafeZone, START_ARRAY;
    public Deck thisDeck;

    /*
    Creates a board of 60 squares, with some safety zones and homes, and links them all, then adds slides
     */
    public Board() {

        thisDeck = new Deck();
        outerRing = new Block[60];
        blueSafeZone = generateSafetyZone(Color.BLUE);
        redSafeZone = generateSafetyZone(Color.RED);
        yellowSafeZone = generateSafetyZone(Color.YELLOW);
        greenSafeZone = generateSafetyZone(Color.GREEN);

        for (int i = 0; i < 60; i++) {
            //set the correct color if it leads to a safety
            switch (i) {
                case 2:
                    outerRing[i] = new Block(Color.RED, i);
                    outerRing[i].setNextSafetyBlock(redSafeZone[0]);
                    break;
                case 17:
                    outerRing[i] = new Block(Color.BLUE, i);
                    outerRing[i].setNextSafetyBlock(blueSafeZone[0]);
                    break;
                case 32:
                    outerRing[i] = new Block(Color.YELLOW, i);
                    outerRing[i].setNextSafetyBlock(yellowSafeZone[0]);
                    break;
                case 47:
                    outerRing[i] = new Block(Color.GREEN, i);
                    outerRing[i].setNextSafetyBlock(greenSafeZone[0]);
                    break;
                default:
                    outerRing[i] = new Block(Color.NULL, i);
                    break;
            }
        }
        //now link all the blocks in the square
        for (int i = 0; i < 60; i++) {
            if (i == 59){
                linkBlocks(outerRing[i], outerRing[0]);
            }
            else {
                linkBlocks(outerRing[i], outerRing[i+1]);
            }
        }
        //this sets up and links the start blocks
        //stored [0] BLUE [1] RED [2] YELLOW [3] GREEN
        START_ARRAY = new Block[]{new Block(Color.RED, -1), new Block(Color.BLUE, -1), new Block(Color.YELLOW, -1), new Block(Color.GREEN, -1)};
        for (int z = 0; z < START_ARRAY.length; z++) {
            START_ARRAY[z].setNextBlock(outerRing[4 + (15*z)]);
        }

        //implement the sliding bits on the board
        for (int i = 1; i < 60; i += 15) {
            this.generateSlides(i);
        }
    }

    //creates a safety zone of length 6 that are all linked and the appropriate color
    private Block[] generateSafetyZone(Color safetyColor){
        System.out.println("Safety zone " + safetyColor.toString() + " generated.");
        Block[] newSafetyZone = new Block[6];
        for (int i = 0; i < 6; i++) {
            newSafetyZone[i] = new Block(safetyColor, -i);
        }
        for (int i = 0; i < 5; i++) {
            linkBlocks(newSafetyZone[i], newSafetyZone[i+1]);
        }
        return newSafetyZone;
    }

    //helper method to thoroughly link two blocks
    private void linkBlocks(Block frontBlock, Block backBlock){
        frontBlock.setNextBlock(backBlock);
        backBlock.setPreviousBlock(frontBlock);
    }

    /*
    Generates the sliding features of certain blocks
     */
    private void generateSlides(int slideStartIndex) {
        System.out.println("Slides generated.");
        outerRing[slideStartIndex].setSlideStatus(Slidiness.START);
        for (int i = 0; i < 3; i++) {
            outerRing[slideStartIndex + i].setSlideStatus(Slidiness.MIDDLE);
        }
        int newIndex = slideStartIndex + 8;
        outerRing[newIndex].setSlideStatus(Slidiness.START);
        for (int i = 0; i < 4; i++) {
            outerRing[newIndex + i].setSlideStatus(Slidiness.MIDDLE);
        }

    }

    public Block getStartLocation(Color color) {
        switch (color) {
            case RED: return START_ARRAY[0];
            case BLUE: return START_ARRAY[1];
            case YELLOW: return START_ARRAY[2];
            case GREEN: return START_ARRAY[3];
        }
        return outerRing[0];
    }
    public Block getGoalLocation(Color color) {
        switch (color) {
            case RED: return redSafeZone[5];
            case BLUE: return blueSafeZone[5];
            case YELLOW: return yellowSafeZone[5];
            case GREEN: return greenSafeZone[5];
        }
        return outerRing[0];
    }

    /*
    Allows pawns to be placed on a block
     */
    public void place(Block whereTo, Pawn thisPawn) {
        whereTo.place(thisPawn);
    }


    public Block getRedHome(){
        return START_ARRAY[0]; }
    public Block getBlueHome() {
        return START_ARRAY[1]; }
    public Block getYellowHome() {
        return START_ARRAY[2]; }
    public Block getGreenHome() {
        return START_ARRAY[3]; }

    public Block getRedGoal() {
        return redSafeZone[5];
    }
    public Block getBlueGoal() {
        return blueSafeZone[5];
    }
    public Block getYellowGoal() {
        return yellowSafeZone[5];
    }
    public Block getGreenGoal() {
        return greenSafeZone[5];
    }

}
