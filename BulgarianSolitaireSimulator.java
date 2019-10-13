// Name: Tianchen Zhang
// USC NetID: 4347909915
// CSCI455 PA2
// Fall 2019


/**
 This solitaireboard simulator simulates cards play. Depends on the main arguments passed in,
 it can generate a random of piles with random cards or the user passes a String of values for piles.
 It will play and output out the configuration of each play and output done if the game is finished.
 The mian method checks the argument passed in and calls the playSimulation method to start the game;
 */

import java.util.ArrayList;
import java.util.Scanner;



public class BulgarianSolitaireSimulator {


    public static void main(String[] args) {

        boolean singleStep = false;
        boolean userConfig = false;
        Scanner scan = new Scanner(System.in);

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-u")) {
                userConfig = true;
            }
            else if (args[i].equals("-s")) {
                singleStep = true;
            }
        }
        playSimulation(singleStep,true,scan);
    }

    /*The method is for validating userinputs. for example if the user passed a non-integer number, it will ask the user to re-enter a String.
    *The method takes Scanner and CARD_TOTAL as inputs and does no output.
    * The method will be passed if and only if the user enter the correct String.
    * */
   private static ArrayList<Integer> readAndValidateInteger(Scanner in, int CARD_TOTAL)
   {
       ArrayList<Integer> userinputs = new ArrayList<Integer>();
       boolean flag = true;
       System.out.println("Number of total cards is "+CARD_TOTAL);

       while(flag) {
           System.out.println("You will be entering the initial configuration of the cards (i.e., how many in each pile).");
           System.out.println("Please enter a space-separated list of positive integers followed by newline:");
           String input = in.nextLine();
           Scanner scanInput = new Scanner(input);
           boolean num_flag= false; //falg2 equals true when user input number that is greater than CARD_TOTAL or smaller than 1
           int sum = 0;
           while (scanInput.hasNextInt()) {
               int temp_num = scanInput.nextInt();

               if(temp_num>0 && temp_num <CARD_TOTAL+1)  //number parameter checking.. checks each input and the sum
               {
                   sum = sum+temp_num;
                       userinputs.add(temp_num);
               }else
               {
                   num_flag = true; //falg2 equals true when user input number does not pass the parameter checking..
               }
           }
           if(scanInput.hasNext()||num_flag||sum!=CARD_TOTAL) //checks if userinput is invalid, sum is CARD_TOTAL
           {
               System.out.println("ERROR: Each pile must have at least one card and the total number of cards must be "+CARD_TOTAL);
               userinputs.clear(); //remove all buffer stored in the arraylist before running again.
           }
           else{
               flag = false;
           }
       }
    return userinputs;
   }

   /*The playSimulation method determines if to randomly populated a board or generate a board according to the user.
   * The method takes two boolean variables singleStep, userConfig and Scanner as inputs
   * The method will not return any value.
   *  */
    private static void playSimulation (boolean singleStep, boolean userConfig, Scanner in)
    {

        SolitaireBoard board;
        int CARD_TOTAL = SolitaireBoard.CARD_TOTAL;
        if(userConfig == false){
             board = new SolitaireBoard();

        }
        else
        {
             board = new SolitaireBoard(readAndValidateInteger(in,CARD_TOTAL));

        }
        System.out.println("Initial configuration:"+board.configString());
        int numRound = 0; //Store the number of round for the game program is at.
        if(singleStep == false)
        {
            while(!board.isDone())
            {
                board.playRound();
                numRound++;
                System.out.println("["+numRound+"]"+"Current configuration:"+board.configString());
            }
            System.out.println("Done!");
        }
        else{
            String check_enter; //A string variable to store value after user hit enter.
            while(!board.isDone())
            {

                System.out.print("<Type return to continue>");
                check_enter = in.nextLine();
                if(check_enter.equals(""))
                {
                    board.playRound();
                    numRound++;
                    System.out.println("["+numRound+"]"+"Current configuration:"+board.configString());

                }
            }
            System.out.print("<Type return to continue>"); //Last call for user to hit enter to give Done!
            check_enter = in.nextLine();
            System.out.println("Done!");

        }





    }




}