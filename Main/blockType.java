package Main;

import Main.src.Slide;

public enum blockType {
    STARTZONE(4,Slide.NOT),
    BOARD(1,Slide.NOT),
    SLIDESTART(1,Slide.START),
    SLIDEMIDDLE(1,Slide.MIDDLE),
    SLIDEEND(1,Slide.END),
    SAFETY(1,Slide.NOT),
    HOME(4,Slide.NOT);

    public final int MaxPawns;
    public final Slide slidevalue;

    blockType(int np,Slide sv){
        this.MaxPawns=np;
        this.slidevalue=sv;
    }
}
