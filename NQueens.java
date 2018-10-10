//-----------------------------------------------------------------------------------------------
/* Keerthi Krishnan 
 cruz id: kvkrishn 
 NQueens.java
 This assignment's objective is to place n queens on an n by n chessboard such that no two queens can 
 attack each other. The concepts implemented were stacks and backtracking.
 */
 //-----------------------------------------------------------------------------------------------

import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.lang.Math;

class NQueens
{
    // input reads the input file, output prints the solutions to output file
        static FileReader input;
        static PrintWriter output;
        static int numQueens = 0;


    public static void main(String[] args) throws IOException {
        // if the argument length is less than 2 when running the program, then print these instructions.

          if(args.length < 2){
            System.out.println("Usage: java -jar NQueens.jar <input file> <output file>");
            System.exit(1);
        }
        //reads the file input and prints the solution to the input in the output file

        input = new FileReader(args[0]);
        output = new PrintWriter(new FileWriter(args[1]));
        BufferedReader in = new BufferedReader(input);
        String line;

        while((line = in.readLine()) != null){
            if(line == null) {
                break;
            }
        
        // creates 2 arrays: 1 string and 1 int array that is then passed through the for loop and converted to strings to read file.
            String[] number = line.split(" ");
            int[] intNums = new int[number.length];

            for(int i = 0; i < intNums.length; i++){
                intNums[i] = Integer.parseInt(number[i]);
            }



        // assigns the array to 3 variables in order for the file to read those 3 variables inputted in the input file
            int boardSize = intNums[0]; // takes in board size 
            int k = 1; // increments array we store the row and col in
            int chessboard[][] = new int[boardSize][boardSize];  //create the board
            Stack<Integer> stack = new Stack<Integer>(); // create new stack

            Integer pos[] = new Integer[intNums.length-1]; // creates an Integer array pos that stores the row and col
            boolean chess = true; // checks if the chessboard is valid
            int h = 0; // incrementor for pos array 
            numQueens = 0;
            while( k < intNums.length) {
                int col = intNums[k] - 1; // stores col
                k++;
                int row = intNums[k] - 1; // stores row
                k++;
                pos[h] = col; // transfers col to pos array
                pos[h+1] = row; // transfers row to pos array

                stack.push(col); // pushes col into stack as each queen can only be on one column 
                chessboard[col][row] = 2; //queens given by sesh
                h= h+ 2; // increments by 2 for the pos array
            }
            int x = 0; 
            if(isTheSame(pos, x)){ // if the coordinates for both queens are the same then set chess to false
                chess = false;
            }

    
            boolean bAttacking = false; // checks if the queens are attacking each other

            for(int i=1; i<=intNums.length-1; i+=2){ // as you iterate through row and column
                if(isAttacking(intNums[i]-1, intNums[i+1]-1, chessboard)){ // if it is attacking
                    bAttacking = true; // set bAttacking to true
                    break;
                } else {
                bAttacking = false; // else set it to false 
                }
            }
            if(bAttacking == true){ // if it is attacking then the validity of chessboard is false 
                chess = false;
            }
            else if(bAttacking == false) { // if it is not attacking 
                int col=0; 
        
        boolean queenCreated = false; // boolean the queenCreated if a queen is placed 
        boolean delCol = false; //boolean to set when you pop off a column
        int currRow = 0; // the current row 
        boolean ok = true; // boolean ok set to true

        while(col < boardSize){ // while the column is less than the size of the board 
            ok = true; // set it to be true 
            if(chess == false){ // if it is not valid then break
                break;
            }

            for(int rowCheck = 0; rowCheck < boardSize; rowCheck++){ // iterating through the rows
                if(chessboard[col][rowCheck] == 2){ // if it is a queen he places
                    col++; // increment the column 
                    ok = false; // set ok to be false
                    break;
                }
            }
            if(ok == false){ // if ok is false
                continue; // continues with incremented col
            } 
             if(delCol == false){ // if the popped column is false 
                currRow = 0; // set the row to 0
            }
            for(int row = currRow; row<boardSize; row++){ // iterate through the rows 
                queenCreated = false; // queen placed is set to false 
                delCol = false; // popped column is false 
                boolean check = isAttacking(col, row, chessboard); // check if it is attacking 
                if(check == false){ // if it is not attacking
                    if(chessboard[col][row] != 2){ // if it is not the queen he places 
                        stack.push(col); // push in the col
                        queenCreated = true; // we place a queen
                        chessboard[col][row] = 1; // set it to 1, meaning it is our queen
                        break;
                    } 

                } else {
                    continue;
                    } // end of else
            } // end of row for loop
                    if(queenCreated){
                        col++;
                    }
                    else{

                        boolean check2 = stack.isEmpty();
                        if(check2){ // check if stack is empty 
                            chess = false;
                            break;
                        }
                        col = stack.pop(); // pops the column off stack if it is attacking
                        for(int c = 0; c< boardSize; c++){ // iterate through rows 
                            if(chessboard[col][c] == 2){ // if its the queen he placed
                                chess = false; // board is invalid 
                                break;
                            }
                        }
                        if(chess == true) { // if the board is valid
                        for(int r = 0; r < boardSize; r++){ // iterate through rows
                            if(chessboard[col][r]==1){ // if its our queen
                                chessboard[col][r] = 0; // set it to 0, meaning we popped it off
                                currRow = r + 1; // increment row 
                                if(currRow == boardSize){ // if the row is equal to board size 
                                    if(stack.isEmpty()) { // check if stack is empty 
                                        chess = false;
                                        break;
                                    }
                                    col = stack.pop(); // pop column
                                    for(int c = 0; c < boardSize; c++){
                                        if(chessboard[col][c] == 2){
                                            chess = false;
                                            break;
                                        }
                                    }
                                    if(chess == true){
                                    for(int r2 = 0; r2 < boardSize; r2++){ // iterate through rows 
                                        if(chessboard[col][r2] == 1){ // if its our queen
                                            chessboard[col][r2] = 0; // set it equal to 0
                                            currRow = r2 +1; // increment row 
                                            break;
                                        }
                                    }
                                }
                                }
                                break;
                            }
                        }
                    }
                    delCol = true; // set the popped col to true 
                 } // end of else
                } // end of while loop  
            } // end of if attacking

         if(chess == false){ // if the board is not valid then print no solution
                output.print("No solution");
            }
            else if(chess == true) { // if the board is valid, use outprint method to print solutions 
                outprint(stack, chessboard, output);
        } 
        output.println();
        } // closes main while loop
        input.close();
        output.close();
    }
    // methods start here

// this method checks that if u have more than 1 queen, that the col and the row of both queens are not the same 
    public static boolean isTheSame(Integer[] storing, int index){
        boolean isSame = false;
        if(storing.length != 2) {
                 index = 0;
            while(index < storing.length - 1) {
                if(storing[index] == storing[index +2] && storing[index+1] == storing[index+3]){
                    isSame = true;
                    break;
                }
                index = index + 2;
                if((index + 2) > storing.length-1){
                    break;
                }
            }
        }
        return isSame;
    }

// this prints out the solutions to the output file 
    public static void outprint(Stack<Integer> stack, int[][] chessboard, PrintWriter output){
        for (int i = 0; i < stack.size(); i++){
                    for (int j = 0; j < stack.size(); j++){
                        if (chessboard[i][j] == 1 || chessboard[i][j] == 2){
                            output.print((i + 1) + " "); // prints solutions
                            output.print((j + 1) + " ");
                }
            }
        }
    }
     
// checks if one queen is attacking another
     public static boolean isAttacking(int col, int row, int board[][]){
       //q is the number of queens

        for (int i = 0; i < board.length; i++ ){ //loops through to check if the current position is being attacked

            if ((board[col][i] == 1 || board[col][i] == 2) && i != row){ // checks columns to mark off where the queen can't be placed.
                return true;
            }

            if ((board[i][row] == 1 || board[i][row] == 2) && i != col){ // checks the row in order to mark off non-queen areas.
                return true;
            }
        }
        
        for (int i = 0; i < board.length ; i++){ // checks the diagonals using slope.
            for (int j = 0; j < board.length; j++){ // slope = rise/run
                int xSlope = col- i; // run 
                 int ySlope = row - j; // rise 
                
                if (Math.abs((ySlope)) == Math.abs(xSlope)){ // in this case, since absolute value of rise/run = 1, then rise must equal run.
                    if ((board[i][j] == 1 || board[i][j] == 2) && ((i != col) || (j != row))){// if rise equals run and other conditions are met, then marks off diagonal.
                            return true;
                        } 
                    } 
                    
            }
    }
    return false;
     }

} // end of main