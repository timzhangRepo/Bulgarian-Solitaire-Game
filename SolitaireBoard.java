// Name: Tianchen Zhang
// USC NetID: 4347909915
// CSCI455 PA2
// Fall 2019

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;

/*
  class SolitaireBoard
  The board for Bulgarian Solitaire.  You can change what the total
  number of cards is for the game by changing NUM_FINAL_PILES, below.
  Don't change CARD_TOTAL directly, because there are only some values
  for CARD_TOTAL that result in a game that terminates.  (See comments
  below next to named constant declarations for more details on this.)
*/


public class SolitaireBoard {

    public static final int NUM_FINAL_PILES = 9;

    // number of piles in a final configuration
    // (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)

    public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
    // bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
    // see: http://en.wikipedia.org/wiki/Bulgarian_solitaire for more details
    // the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES

    // Note to students: you may not use an ArrayList -- see assgt
    // description for details.


    /**
     Representation invariant:
     <put rep. invar. comment here>
     Sum of cards in all piles must equal to CARD_TOTAL;
     currentSize >= 1 && <= CARD_TOTAL;
     currentPile[i] >= 1 && <=CARD_TOTAL; For i in range from 0 to currentSize-1;
     Capacity of currentPile = CARD_TOTAL;
     */

    // <add instance variables here>
    private int[] currentPile; //Current configuration of the SolitaireBoard
    private int currentSize; //number of elements(deck of cards) in the currentPile array;

    /**
     Creates a solitaire board with the configuration specified in piles.
     piles has the number of cards in the first pile, then the number of
     cards in the second pile, etc.
     PRE: piles contains a sequence of positive numbers that sum to
     SolitaireBoard.CARD_TOTAL
     */
    public SolitaireBoard(ArrayList<Integer> piles) {
        currentPile  = new int[CARD_TOTAL];
        for(int i=0; i<piles.size(); i++){
            currentPile[i] = piles.get(i);
        }

        currentSize = piles.size();
        // sample assert statement (you will be adding more of these calls)
        // this statement stays at the end of the constructor.
        assert isValidSolitaireBoard();
    }
    /**
     Creates a solitaire board with a random initial configuration.
     */
    public SolitaireBoard() {
        Random ran = new Random(); //Creating a random number generator
        int[] start_Piles = new int[CARD_TOTAL]; //Intiate a empty starting piles with maximum number of piles possible
        int currentSP_Size = 0; //number of elements filled in the start_piles
        int[] end_Piles;
        int cardsRemain = CARD_TOTAL;
        /* Populate the empty num_Piles with CARD_TOTAL cards randomly placed*/
        for(int i = 0; i<start_Piles.length; i++)
        {
            start_Piles[i] = ran.nextInt(cardsRemain)+1;
            int temp_TotalCards = cardsRemain - start_Piles[i];
            if(temp_TotalCards<=0){  //Boundary Check
                start_Piles[i] = cardsRemain;
                currentSP_Size = i+1; //Record final size after cards run out.
                break;}
            else{
                cardsRemain = cardsRemain - start_Piles[i];
                currentSP_Size = i+1; //if piles does not contain zero, record the location for to avoid copyof empty array
                continue;}
        }
        /*Remove start_piles that contain no cards, this is the pile that is going to be used*/

        end_Piles = Arrays.copyOfRange(start_Piles,0,currentSP_Size);
        currentSize = currentSP_Size;
        currentPile = start_Piles;
        assert(isValidSolitaireBoard());
    }
    /**
     Plays one round of Bulgarian solitaire.  Updates the configuration
     according to the rules of Bulgarian solitaire: Takes one card from each
     pile, and puts them all together in a new pile.
     The old piles that are left will be in the same relative order as before,
     and the new pile will be at the end.
     */
    public void playRound() {
        int last_Pile_cards = currentSize; //the last pile is the number of piles total. Ex; number of pile 7, then last_pile_cards is 7
        /*minus 1 from every array elements*/
        for(int i = 0; i< currentSize; i++)
        {
            currentPile[i] = currentPile[i]-1;
        }

        currentPile[currentSize] = last_Pile_cards;
        currentSize++;
        /*remove zeros from the currentPiles*/

        for(int i = 0; i< currentSize; i++)
        {
            if(currentPile[i]==0)
            {
                int j=i+1;
                while(currentPile[j]==0&&j<currentSize)
                {
                    j++;
                }
                currentPile[i] = currentPile[j];
                currentPile[j] = 0;

            }
        }

        currentSize = 0; //reset current size
        for(int i=0; i< CARD_TOTAL; i++)  //count number of elements in the currentPiles
        {
            if(currentPile[i]!=0)
            {
                currentSize++;
            }
        }
        assert(isValidSolitaireBoard()); //Does not has to stay at the end
    }
    /**
     Returns true iff the current board is at the end of the game.  That is,
     there are NUM_FINAL_PILES piles that are of sizes
     1, 2, 3, . . . , NUM_FINAL_PILES,
     in any order.
     */

    public boolean isDone() {
            HashSet<Integer> piles = new HashSet<Integer>(); //Check for duplicate(same number of cards) in the set, if return false if it does
            for(int i = 0; i< currentSize; i++)
            {
                piles.add(currentPile[i]);
            }
            if(currentSize!=piles.size()) return false;  //Check if there is a triangular set of piles for the board.
            boolean[] board_complete =new boolean[NUM_FINAL_PILES];
            for(int i=0; i<board_complete.length;i++)
            {
                for(int j = 0; j< currentSize; j++)
                {
                    if(currentPile[j]==i+1)
                    {
                        board_complete[j]=true; //Found the number

                    }

                }

            }
            boolean isComplete = true;
            for(int i=0; i<board_complete.length;i++)  //Check if there is a false value in the boolean(Missing pile)array
            {
                if(board_complete[i]==false)
                {
                    isComplete=false;
                }
            }
            assert(isValidSolitaireBoard());
            return isComplete;

    }


    /**
     Returns current board configuration as a string with the format of
     a space-separated list of numbers with no leading or trailing spaces.
     The numbers represent the number of cards in each non-empty pile.
     */
    public String configString() {

        String pile_board = "";
        for(int i = 0; i< currentSize; i++)
        {
            pile_board = pile_board+(String.valueOf(currentPile[i])+" ");
        }
        isValidSolitaireBoard();
        assert(isValidSolitaireBoard());
        return pile_board;   // duidDonemmy code to get stub to compile

    }


    /**
     checks if
     Capacity of currentPile = CARD_TOTAL;
     Sum of cards in all piles must equal to CARD_TOTAL;
     currentSize >= 1 && <= CARD_TOTAL;
     currentPile[i] >= 1 && <=CARD_TOTAL; For i in range from 0 to currentSize-1;
     return false if any invariant is true; and return true if all invariant are true;
     */
    private boolean isValidSolitaireBoard() {
           int cards_count = 0;
           if (currentPile.length != CARD_TOTAL) return false;  // Capacity of currentPile = CARD_TOTAL;
           if (currentSize<1 || currentSize>CARD_TOTAL) return false; //total number of piles does not exceed 45
           else{
               for(int i=0; i<currentPile.length; i++) //Sum of cards in all piles must equal to CARD_TOTAL;
                {
                    cards_count = cards_count + currentPile[i];
                    if(currentPile[i]<0 || currentPile[i]>46) return false; //currentPile[i] >= 1 && <=CARD_TOTAL; For i in range from 0 to currentSize-1;
                }
            }
            if(cards_count!=CARD_TOTAL) return false; //total number of cards does not exceed 45

//
        return true;
    }




}
