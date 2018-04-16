/**
 * the deck
 * by Alec
 * */

package Main.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Deck {
    final int DECKSIZE=45;
    Card[] deck=new Card[DECKSIZE];
    ArrayList<Integer> randomizer=new ArrayList<Integer>();
    int TCindex;

    public Deck(){
        generateDeck();
        TCindex=0;
        for (int i=0;i<DECKSIZE;i++){
            randomizer.add(i);
        }
        shuffle();
    }

    // get the top card
    public Card getTC(){
        return deck[TCindex];
    }

    // get the deck index
    public int getTCindex() {
        return TCindex;
    }

    // get cards left
    public int getCardsLeft(){
        return DECKSIZE-(TCindex+1);
    }

    // draw a single card
    public Card draw(){
        TCindex++;
        if(TCindex==DECKSIZE){
            shuffle();
            TCindex=0;
        }
        return getTC();
    }

    // shuffle the deck
    private void shuffle(){
        // randmize positions of integers
        Collections.shuffle(randomizer);
        // initialize temporary deck
        Card[] tempdeck=new Card[DECKSIZE];
        // fill tempdeck with appropriate deck options
        for (int j=0;j<DECKSIZE;j++){
            tempdeck[j]=deck[randomizer.get(j)];
        }
        // replace deck with tempdeck
        for (int k=0;k<DECKSIZE;k++){
            deck[k]=tempdeck[k];
        }
    }

    // generates all cards for the deck
    private void generateDeck(){
        ArrayList<Card> starter=new ArrayList<Card>(Arrays.asList(Card.values()));
        int i=0;
        for (int ic=0;ic<starter.size();ic++){
            for (int dc=0;dc<starter.get(ic).numInDeck;dc++){
                deck[i]=starter.get(ic);
                i++;
            }
        }
    }


    // testing main function
    public static void main(String[] args){
        Deck testdeck=new Deck();
        System.out.println(Arrays.toString(testdeck.deck));
    }

}

