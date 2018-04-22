/**
 * the card enum
 * by alec
 */

package Main.src;


public enum Card {
    SORRY(0,4, "Take any one pawn from Start and move it directly to a square occupied by any opponent's pawn, sending that pawn back to its own Start. A Sorry! card cannot be used on an opponent's pawn in a Safety Zone. If there are no pawns on the player's Start, or no opponent's pawns on any squares outside Safety Zones, the turn is lost.","cardSorry.png"),
    ONE(1,5, "Move a pawn from Start or move a pawn one space forward.","card1.png"),
    TWO(2,4, "Move a pawn from Start or move a pawn two spaces forward. Drawing a two entitles the player to draw again at the end of his or her turn. If the player cannot use a two to move, he or she can still draw again.","card2.png"),
    THREE(3,4, "Move a pawn three spaces forward.","card3.png"),
    FOUR(4,4, "Move a pawn four spaces backward.","card4.png"),
    FIVE(5,4, "Move a pawn five spaces forward.","card5.png"),
    SEVEN(7,4, "Move one pawn seven spaces forward, or split the seven spaces between two pawns (such as four spaces for one pawn and three for another). This makes it possible for two pawns to enter Home on the same turn, for example. The seven cannot be used to move a pawn out of Start, even if the player splits it into a six and one or a five and two. The entire seven spaces must be used or the turn is lost. You may not move backwards with a split.","card7.png"),
    EIGHT(8,4, "Move a pawn eight spaces forward.","card8.png"),
    TEN(10,4, "Move a pawn 10 spaces forward or one space backward. If none of a player's pawns can move forward 10 spaces, then one pawn must move back one space.","card10.png"),
    ELEVEN(11,4, "Move 11 spaces forward, or switch the places of one of the player's own pawns and an opponent's pawn. A player that cannot move 11 spaces is not forced to switch and instead can forfeit the turn. An 11 cannot be used to switch a pawn that is in a Safety Zone.","card11.png"),
    TWELVE(12,4, "Move a pawn 12 spaces forward.","card12.png");

    public final int num,numInDeck;
    public final String reminderText, imgName;

    Card(int num,int numInDeck, String reminderText, String imgn) {
        this.num = num;
        this.numInDeck=numInDeck;
        this.reminderText = reminderText;
        this.imgName=imgn;
    }
}
