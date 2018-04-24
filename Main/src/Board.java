package Main.src;

import Main.blockType;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    Pawn nullPawn;// for all instances there is no pawn
    // blocks 0-3 start,4-63 regular boardblocks, 64-83 safety, 84-87 home blocks
    Block[] gameboard=new Block[88];
    //pawns, 0-3 you 4-7 ai1 8-11 ai2 12-15 ai3
    Pawn[] pieces=new Pawn[16];
    // player color
    Color PCcolor;

    Board(){
        generateGameBoard();
        generatePawns();

    }

    public void newGame(){
        //set pawns to home
    }

    public void load(String encoded){
        //s for saved game, 16 sets of 00-87 for pawn positions,set of 45 hex 0-B for deck order, number 00-45 for top card, z for end of save
        char[] savedState=encoded.toCharArray();

    }
    private void generateGameBoard(){
        int cntr=0;
        ArrayList<Color> coloradder=new ArrayList<Color>(Arrays.asList(Color.values()));

        // start blocks
        for (cntr=0;cntr<4;cntr++){
            int nx=(15*cntr)+8;
            Block addto=new Block(coloradder.get(cntr+1), blockType.STARTZONE,-1,nx,-1);

            gameboard[cntr]=addto;

        }
        blockType[] orderOBT={blockType.BOARD,blockType.SLIDESTART,blockType.LEADTOSAFETY,blockType.SLIDEMIDDLE,blockType.SLIDEEND,blockType.BOARD,blockType.BOARD,blockType.BOARD,blockType.BOARD,blockType.SLIDESTART,blockType.SLIDEMIDDLE,blockType.SLIDEMIDDLE,blockType.SLIDEMIDDLE,blockType.SLIDEEND,blockType.BOARD};

        // the 60 blocks
        for (int cntr2=0;cntr2<4;cntr2++){
            for (int cntr3=0;cntr3<16;cntr3++){
                int szn=-1;
                int crnt=4+(15*cntr2)+cntr3;
                int prev=crnt-1;
                int nxt=crnt+1;
                if (crnt==4){
                    prev=63;
                }else if (crnt==63){
                    nxt=4;
                }
                if (cntr3==2){
                    szn=64+(5*cntr2);
                }
                Block addto=new Block(coloradder.get(cntr2+1),orderOBT[cntr3],szn,nxt,prev);
                gameboard[crnt]=addto;
            }
        }

        // the 20 safety blocks
        for (int cntr2=0;cntr2<4;cntr2++){
            for (int cntr3=0;cntr3<5;cntr3++){
                int crnt=64+(5*cntr2)+cntr3;
                int nxt=crnt+1;
                int prev=crnt-1;
                if (cntr3==0){
                    prev=(15*cntr2)+6;
                }else if (cntr3==4){
                    nxt=84+cntr2;
                }
                Block addto=new Block(coloradder.get(cntr2+1),blockType.SAFETY,-1,nxt,prev);
                gameboard[crnt]=addto;
            }
        }

        // the 4 homes
        for (int cntr2=0;cntr2<4;cntr2++){
            int crnt=84+cntr2;
            int prev=68+(5*cntr2);
            Block addto=new Block(coloradder.get(cntr2+1),blockType.HOME,-1,-1,prev);
            gameboard[crnt]=addto;
        }
    }

    private void generatePawns(){
        ArrayList<Color> coloradder=new ArrayList<Color>(Arrays.asList(Color.values()));

        for (int cntr=0;cntr<4;cntr++){
            for (int cntr2=0;cntr2<4;cntr2++){
                pieces[(4*cntr)+cntr2]=new Pawn(coloradder.get(cntr+1),cntr);
            }
        }
    }

}
