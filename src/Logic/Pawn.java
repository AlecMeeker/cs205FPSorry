package Logic;

public class Pawn {

    private boolean hasReachedSafeZone; //is in the safety zone at least
    private boolean isFinished; //is home
    private Color color; //integer representing the color of the player
    public Player myPlayer;

    public Boolean highlighted;
    public Boolean selected = false;

    public Block[] locationArray;

    public Board thisBoard;

    /**
     * Constructor for a pawn that sets the board where the pawn is played and the player the pawn belongs to
     *
     * @param inBoard  board to be played on
     * @param inPlayer player the pawn belongs to
     */
    public Pawn(Board inBoard, Player inPlayer) {
        thisBoard = inBoard;
        this.color = inPlayer.color;
        this.myPlayer = inPlayer;
        hasReachedSafeZone = false;

        locationArray = new Block[3];
        locationArray[0] = inBoard.getStartLocation(color);
        this.setTargets();

        System.out.println(color.toString().toLowerCase() + " pawn added.");

    }

    /**
     * Constructor for a pawn that gives the pawn a board, player, and starting location
     *
     * @param inBoard      board to be played on
     * @param inPlayer     player the pawn belongs to
     * @param currentBlock block the pawn is to be initialized to
     */
    public Pawn(Board inBoard, Player inPlayer, Block currentBlock) {
        thisBoard = inBoard;
        this.color = inPlayer.color;
        this.myPlayer = inPlayer;

        this.setCurrentBlock(currentBlock);
        if (currentBlock == inBoard.getStartLocation(this.color)) {
            hasReachedSafeZone = false;
            isFinished = false;
        } else if (currentBlock == locationArray[2]) {
            isFinished = true;
            hasReachedSafeZone = true;
        }
    }

    /**
     * for a pawn's color, sets their start and home (goal) location
     */
    public void setTargets() {
        System.out.println("Setting targets");

        locationArray[1] = thisBoard.getStartLocation(color);
        locationArray[2] = thisBoard.getGoalLocation(color);

    }

    /**
     * returns this pawn to its home location based on its color
     */
    public void getBounced() {
        this.move(thisBoard.getStartLocation(color));

        System.out.println(myPlayer.getName() + "'s pawn got bounced home lol");
    }

    /**
     * This method checks to see if this pawn can move the given number of spaces
     *
     * @param spaces the number of spaces ahead to check
     * @return whether or not the pawn can move there
     */
    public boolean canMoveHere(int spaces) {

        if (getDistanceFromHome() >= spaces) {
            int moveSpaces = spaces;
            Block targetBlock = this.getCurrentBlock();
            while (moveSpaces > 0) {
                targetBlock = targetBlock.getNextBlock(color);
                moveSpaces--;
            }
            while (moveSpaces < 0) {
                targetBlock = targetBlock.getPreviousBlock();
                moveSpaces++;
            }
            if (targetBlock.getPawn() != null && targetBlock.getPawn().getColor() == color && !targetBlock.isHome) {
                return false;
            } else {
                return true;
            }
        }


        return false;

    }

    /**
     * Gets the color this pawn belongs to
     *
     * @return what color the pawn is
     */
    public Color getColor() {
        return color;
    }

    /**
     * sets color of pawn
     *
     * @param color the color this pawn belongs to
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the current block the pawn is on
     *
     * @return block where pawn is located
     */
    public Block getCurrentBlock() {
        return locationArray[0];
    }

    /**
     * Sets the current block for this pawn
     *
     * @param currentBlock the block to be set
     */
    public void setCurrentBlock(Block currentBlock) {
        locationArray[0] = currentBlock;
    }

    /**
     * calculates the disatance this pawn is from its home
     *
     * @return number of blocks between where the pawn is and its home
     */
    public int getDistanceFromHome() {
        if (!hasLeftStart()) {
            return 65;
        }
        Block iteratorBlock = getCurrentBlock();
        int steps = 0;
        while (iteratorBlock != getHomeLocation()) {
            steps++;
            iteratorBlock = iteratorBlock.getNextBlock(color);
        }

        return steps;
    }

    /**
     * changes all of the fields of the pertinent blocks
     *
     * @param targetBlock the block that the pawn is moving to
     */
    public void move(Block targetBlock) {
        Block origin = this.getCurrentBlock();

        if (targetBlock == this.getStartLocation()) {
            this.myPlayer.movablePawnList.remove(this);
            this.myPlayer.startPawnList.add(this);
        }

        if (origin == getStartLocation()) {
            myPlayer.startPawnList.remove(this);
            myPlayer.movablePawnList.add(this);
        } else if (targetBlock == this.getHomeLocation()) {
            this.myPlayer.movablePawnList.remove(this);
            this.myPlayer.finishedPawnList.add(this);
        }

        origin.removePawn();
        targetBlock.place(this);
        this.setCurrentBlock(targetBlock);
    }

    /**
     * Returns whether or not the pawn is home
     *
     * @return whether or not the pawn is home
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Sets the field isFinished
     *
     * @param home whether the pawn is in the home
     */
    public void setFinished(boolean home) { //Slightly confusing name - sets the isHome field
        isFinished = home;
    }

    /**
     * Returns the home block for this color of pawn
     *
     * @return Home block for this pawn
     */
    public Block getHomeLocation() {
        return locationArray[2];
    }

    /**
     * Returns the start block for the color of this pawn
     *
     * @return Start block for this pawn
     */
    public Block getStartLocation() {
        return locationArray[1];
    }

    /**
     * calculates whether or not the pawn has reached the safe zone
     *
     * @return whether or not the pawn has reached the safe zone
     */
    public boolean hasReachedSafeZone() {
        if (getCurrentBlock() == thisBoard.getSafeBlock(getCurrentBlock().id, color)) {
            return true;
        }
        return false;
    }

    /**
     * sets the field that says whether or not the pawn has reached the safe zone
     *
     * @param hasReachedSafeZone whether or not the pawn has reached the safe zone
     */
    public void setHasReachedSafeZone(boolean hasReachedSafeZone) {
        this.hasReachedSafeZone = hasReachedSafeZone;
    }

    /*
    method used to select this pawn during gameplay
     */
    public void select() {
        this.selected = true;
    }

    public void deselect() {
        if (selected) {
            selected = false;
        }
    }

    /**
     * Figures out whether this pawn has left the start
     *
     * @return Whether or not the pawn has left start
     */
    public boolean hasLeftStart() {
        if (this.getCurrentBlock() == locationArray[1]) {
            return false;
        } else {
            return true;
        }
    }

}

