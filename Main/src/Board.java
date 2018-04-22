package Main.src;

import Main.blockType;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    Pawn nullPawn;// for all instances there is no pawn
    // blocks 0-3 starts,4-63 regular boardblocks, 64-83 safety, 84-87 home blocks
    Block[] gameboard=new Block[88];
    //pawns, 0-3 you 4-7 ai1 8-11 ai2 12-15 ai3
    Pawn[] pieces=new Pawn[16];
    // player color
    Color PCcolor;

    Board(){
        generateGameBoard();

    }

    private void generateGameBoard(){
        int cntr=0;
        ArrayList<Color> coloradder=new ArrayList<Color>(Arrays.asList(Color.values()));

        // start blocks
        for (cntr=0;cntr<4;cntr++){
            int nx=0;
            switch (cntr){
                case 0:
                   nx=8;
                   break;
                case 1:
                    nx=23;
                    break;
                case 2:
                    nx=38;
                    break;
                case 3:
                    nx=53;
                    break;
            }
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

}
