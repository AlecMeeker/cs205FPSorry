package Main.src;

public class Board {
    Pawn nullPawn;// for all instances there is no pawn
    // blocks 0-3 homes,4-63 regular boardblocks, 64-83 safety, 84-87 home blocks
    Block[] gameboard=new Block[88];
    //pawns, 0-3 you 4-7 ai1 8-11 ai2 12-15 ai3
    Pawn[] pieces=new Pawn[16];
    // player color
    Color PCcolor;




}
