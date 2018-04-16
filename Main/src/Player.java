package Main.src;


public class Player {
	
	private Color color;
	private Pawn[] pawnArray;
	
	Player(Color inColor) {
		this.color = inColor;
		pawnArray = new Pawn[]{new Pawn(color), new Pawn(color), new Pawn(color), new Pawn(color)};
		
		System.out.println("Player created with color =  " + color.toString());
		
	}

	public List<Move> getMoveList(Card draw) {
		List<Move> moveList = new List();

		if (draw == SEVEN) {


		}
	}
	
}


