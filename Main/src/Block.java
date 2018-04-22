package Main.src;

import Main.blockType;

public class Block {
    Color SC;
    //Pawn[] onBlock;
    blockType type;
    int[] pawnLocation;
    int numPawns, sznext;

    Block(Color inC, blockType bt,int szn){
        SC=inC;
        type=bt;
        pawnLocation=new int[type.MaxPawns];
        numPawns=0;
        sznext=szn;
    }

    public void setColor(Color SC) {
        this.SC = SC;
    }

    public void addPawn(Pawn adp){

    }

    public void removePawn(Pawn nulpawn){

    }

}
