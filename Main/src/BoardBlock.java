
package Main.src;


public class BoardBlock extends Block {
    BoardBlock[] degree1=new BoardBlock[2];//next,previous
    Pawn onBlock;

    Slide Sval;

    BoardBlock(){
        super();
        Sval=Slide.NOT;
    }

    public void addNext(BoardBlock nxt){
        degree1[0]=nxt;
    }

    public void addPrevious(BoardBlock prev){
        degree1[1]=prev;
    }

    public void updateSlide(Slide us){
        Sval=us;
    }

    public BoardBlock getNext(){
        return degree1[0];
    }

    public BoardBlock getPrevious(){
        return degree1[1];
    }

    public Slide getSlideValue() {
        return Sval;
    }


}
