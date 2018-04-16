


public class Player {
	
	private Color color;
	private Pawn[] pawnArray;
	
	Player(Color inColor) {
		this.color = inColor;
		pawnArray = new Pawn[]{new Pawn(color), new Pawn(color), new Pawn(color), new Pawn(color)};
		
		System.out.println("Player created with color =  " + color.toString());
		
	}

	public List<Move> getMoveList(Card draw) {
	    System.out.println("Generating possibilities")
		List<Move> moveList = new List();
		if (draw == SEVEN) {
		    //create a moveList for each possible combination
        }
        else {
		    for (Pawn p: pawnArray) {
		        if (!p.isHome()) {
		            moveList.add(new Move(p, p.getCurrentBlock(), draw));
		            System.out.println("Move added to possibilities")
                }
            }
        }

	}
	
}


