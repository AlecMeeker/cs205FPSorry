package Main.src;

public class Block {
    Color SC;
    Pawn[] onBlock;
    int pawns;

    Block(Color inC, Pawn[] inp){
        SC=inC;
        onBlock=inp;
        pawns=inp.length;
    }

    Block(){
        SC=Color.NULL;
        onBlock=null;
    }

    public void setColor(Color SC) {
        this.SC = SC;
    }

    public void addPawn(Pawn adp){
        onBlock[pawns]=adp;
        pawns++;
    }

    public void removePawn(Pawn nulpawn){
        pawns--;
        onBlock[pawns]=nulpawn;
    }

}
