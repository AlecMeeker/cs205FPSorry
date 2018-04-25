package Logic;

public class Pawn {

	private boolean hasReachedSafeZone; //is in the safety zone at least
    private boolean isFinished; //is home
	private Color color; //integer representing the color of the player
    public Player myPlayer;

    public Boolean highlighted;
    public Boolean selected;

    public Block[] locationArray;

    public Board thisBoard;
	
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

	public Pawn(Board inBoard, Player inPlayer, Block currentBlock) {
	    thisBoard = inBoard;
	    this.color = inPlayer.color;
	    this.myPlayer = inPlayer;

	    this.setCurrentBlock(currentBlock);
	    if (currentBlock == inBoard.getStartLocation(this.color)) {
	        hasReachedSafeZone = false;
	        isFinished = false;
        }
        else if (currentBlock == locationArray[2]) {
	        isFinished = true;
	        hasReachedSafeZone = true;
        }
    }

	public void setTargets() {
	    System.out.println("Setting targets");

	    locationArray[1] = thisBoard.getStartLocation(color);
	    locationArray[2] = thisBoard.getGoalLocation(color);

    }

    /*
    returns this pawn to its home location based on its color
     */
    public void getBounced() {
	    this.move(thisBoard.getStartLocation(color));

	    System.out.println(myPlayer.getName() + "'s pawn got bounced home lol");
    }

    public boolean canMoveHere(int spaces) {

        if (getDistanceFromHome() >= spaces) {
            int moveSpaces = spaces;
            Block targetBlock = this.getCurrentBlock();
            while (moveSpaces > 0) {
                targetBlock = targetBlock.getNextBlock(color);
                moveSpaces--;
            }
            if (targetBlock.getPawn() != null && targetBlock.getPawn().getColor() == color && !targetBlock.isHome) {
                return false;
            }
            else {
                return true;
            }
        }


        return false;

    }


    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public Block getCurrentBlock() {
        return locationArray[0];
    }
    public void setCurrentBlock(Block currentBlock) {
        locationArray[0] = currentBlock;
    }

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

    /*
    changes all of the fields of the pertinent blocks
     */
    public void move(Block targetBlock) {
        Block origin = this.getCurrentBlock();

        if (targetBlock == this.getStartLocation()) {
            this.myPlayer.movablePawnList.remove(this);
            this.myPlayer.startPawnList.add(this);
        }

        else if (targetBlock == this.getHomeLocation()) {
            this.myPlayer.movablePawnList.remove(this);
            this.myPlayer.finishedPawnList.add(this);
        }

        origin.removePawn();
        targetBlock.place(this);
        this.setCurrentBlock(targetBlock);
    }

    public boolean isFinished() {
        return isFinished;
    }
    public void setFinished(boolean home) { //Slightly confusing name - sets the isHome field
        isFinished = home;
    }

    public Block getHomeLocation() {
	    return locationArray[2];
    }

    public Block getStartLocation() {
        return locationArray[1];
    }

    public boolean hasReachedSafeZone() {
        if (getCurrentBlock() == thisBoard.getSafeBlock(getCurrentBlock().id, color)){
            return  true;
        }
        return false;
    }

    public void setHasReachedSafeZone(boolean hasReachedSafeZone) {
        this.hasReachedSafeZone = hasReachedSafeZone;
    }

    public void select() {
        this.selected = true;
    }

    public boolean hasLeftStart() {
        if (this.getCurrentBlock() == locationArray[1]) {
            return false;
        }
        else {
            return true;
        }
    }

}

