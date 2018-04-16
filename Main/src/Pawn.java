package Main.src;

public class Pawn {

	private Block currentBlock; //FIX THIS FOR BETTER POINTER TO MAP LOCATION
	private int distanceFromHome; //acts as relative progress check
    private boolean isOut; //is out of home
	private boolean isSafe; //is in the safety zone at least
    private boolean isHome; //is home
	private Color color; //integer representing the color of the player

    public Block[] locationArray; //NEEDS TO BE UPDATED ONCE THE BOARD IS FINISHED

	
	Pawn(Color color) {
		this.color = color;
		distanceFromHome = 0;
		isOut = false;
		//set currentNode equal to the correct node based on the color
		isSafe = false;
		//locationArray = {startBlock, startBlock, homeBlock}

		System.out.println("Pawn created, color = " + this.color.toString());
	}

	public int getColorID() {
	    return colorID;
    }
    public void setColorID(int colorIDin){
	    this.colorID = colorIDin;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }
    public void setCurrentBlock(Block currentBlock) {
        this.currentBlock = currentBlock;
    }

    public int getDistanceFromHome() {
        return distanceFromHome;
    }
    public void setDistanceFromHome(int distanceFromHome) {
        this.distanceFromHome = distanceFromHome;
    }

    public boolean isHome() {
        return isHome;
    }
    public void setHome(boolean home) { //Slightly confusing name - sets the isHome field
        isHome = home;
    }

    public Block getHomeLocation() {
	    return locationArray[2];
    }

    public Block getStartLocation() {
	    return locationArray[1];
    }

    public boolean isOut() {
        return isOut;
    }
    public void setOut(boolean out) {
        isOut = out;
    }

    public boolean isSafe() {
        return isSafe;
    }
    public void setSafe(boolean safe) {
        isSafe = safe;
    }


    /*public void getBounced() {
	    currentBlock =
    }*/
	
	
}

