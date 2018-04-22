package Main.src;

import Main.blockType;

public class Block {
    Color SC;
    //Pawn[] onBlock;
    blockType type;
    //indices of pawns on the block
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

    public Color getSC() {
        return SC;
    }


    public boolean addPawn(int adp){

        if (numPawns<type.MaxPawns){
            return false;
        }else {
            pawnLocation[numPawns]=adp;
            numPawns++;
            return true;
        }

    }

    public int removePawn(){
        numPawns--;
        if (numPawns==-1){
            numPawns++;
            return -1;
        }else {
            int retid=pawnLocation[numPawns];
            pawnLocation[numPawns]=-1;
            return retid;
        }
    }

}
