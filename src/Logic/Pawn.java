package Logic;

public class Pawn {

	private int distanceFromHome; //acts as relative progress check
    private boolean hasLeftStart; //is out of start
	private boolean hasReachedSafeZone; //is in the safety zone at least
    private boolean isFinished; //is home
	private Color color; //integer representing the color of the player
    public Player myPlayer;

    public Block[] locationArray;

    public Board thisBoard;
	
	Pawn(Color color, Board inBoard, Player inPlayer) {
	    thisBoard = inBoard;
		this.color = color;
		this.myPlayer = inPlayer;
		hasLeftStart = false;
		hasReachedSafeZone = false;

		locationArray = new Block[3];
		this.setTargets();

		System.out.println(color.toString().toLowerCase() + " pawn added.");
	}

	public void setTargets() {
	    System.out.println("Setting targets");
	    locationArray[0] = thisBoard.getStartLocation(color);
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
            Block targetBlock = getCurrentBlock();
            while (moveSpaces > 0) {
                targetBlock = targetBlock.getNextBlock(color);
                moveSpaces--;
            }
            if (targetBlock.getPawn() != null && targetBlock.getPawn().getColor() == color) {
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
        if (!hasLeftStart) {
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

    public boolean hasLeftStart() {
        return hasLeftStart;
    }

    public void setHasLeftStart(boolean hasLeftStart) {
        this.hasLeftStart = hasLeftStart;
    }

    public boolean isHasReachedSafeZone() {
        return hasReachedSafeZone;
    }
    public void setHasReachedSafeZone(boolean hasReachedSafeZone) {
        this.hasReachedSafeZone = hasReachedSafeZone;
    }

}

