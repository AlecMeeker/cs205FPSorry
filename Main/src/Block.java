package Main.src;

import Main.blockType;

public class Block {
    Color SC;
    //Pawn[] onBlock;
    blockType type;
    int[] pawnLocation;
    int numPawns, sznext, next,prev;

    Block(Color inC, blockType bt,int szn, int n, int p){
        SC=inC;
        type=bt;
        pawnLocation=new int[type.MaxPawns];
        numPawns=0;
        next=-1;
        prev=-1;
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
