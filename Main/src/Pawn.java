

public class Pawn {

    private Block currentBlock;
    private int distanceFromHome; //acts as relative progress check
    private boolean hasLeftStart; //is out of start
    private boolean hasReachedSafeZone; //is in the safety zone at least
    private boolean isFinished; //is home
    private Color color; //integer representing the color of the player

    public Block[] locationArray;

    public Board thisBoard;

    Pawn(Color color, Board inBoard) {
        thisBoard = inBoard;
        this.color = color;
        distanceFromHome = 0;
        hasLeftStart = false;
        hasReachedSafeZone = false;

        locationArray = new Block[3];
        this.setTargets();

        System.out.println("Pawn created, color = " + this.color.toString());
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
        thisBoard.getStartLocation(this.color).place(this);
    }

    public Block getCurrentBlock() {
        return locationArray[0];
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
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