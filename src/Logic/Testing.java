package Logic;

public class Testing {

	public static void main(String[] args) {
		Deck testdeck=new Deck();
		Board thisBoard = new Board();

		Player player1 = new Player(Color.BLUE, thisBoard);
		thisBoard.place(thisBoard.getStartLocation(Color.RED), new Pawn(Color.RED, thisBoard, player1));







	}

	
}
