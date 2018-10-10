//------------------------------------------------------------------------------------------------------------
//  Keerthi Krishnan, kvkrishn
// HW2
// A Program that uses a linked list in order to place pieces on a chessboard, find a specific piece, and tell whether
// another piece is in the attacking range of this piece. 
//------------------------------------------------------------------------------------------------------------


import java.util.Scanner;
import java.lang.*;
import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;


public class ChessBoard {

// Public variables used for location, pieces, rows, columns, and types
  public int row;
  public int col;
  public String []area;
  public String []objects;
  public char type = '1';

//LinkedList and ChessPiece object created
  public LinkedList ChessBoard1;
  private ChessPiece object;

// this method reads the input file, traverses the linked list, applying any function necessary and prints the solution to the output file.
    private void IOReaderandWriter(String[] args) throws FileNotFoundException, IOException{

      int row1 = 0;
      int col1 = 0;
      // goes to open files
      
      // takes in input file as first argument, takes in output file as second agument, uses BufferedReader to go through input file
        FileReader input = new FileReader(args[0]);
        PrintWriter output = new PrintWriter(new FileWriter(args[1]));   
        BufferedReader reader = new BufferedReader(input);
        String line = "";
        

          // reads lines from the input file, uses colons and whitespace as delimiter for the input and extracts the data
        while((line = reader.readLine()) != null ) {
        
            if(line == null) {
                break;
            }

            String[] inputScan = line.split(":");

            // gets the information about the row and column 
            area = inputScan[0].split(" ");
            row = Integer.parseInt(area[0]);
            col = Integer.parseInt(area[1]);
            
            // linked list object made
            ChessBoard1 = new LinkedList();  

            // gets rid of whitespace in the input file
            objects = inputScan[1].split(" ");
            String c = "";

            // iterates through object array and sets each argument as the type, row, and column 
            for(int i=1;i<objects.length;i=i+3){
                c = objects[i];
                type = c.charAt(0);
                row1 = Integer.parseInt(objects[i+1]);
                col1 = Integer.parseInt(objects[i+2]);
                
                // calls the addPiece method from the linked list and uses method pieceInit in order to enter in the types of pieces into the linked list
                ChessBoard1.addPiece(this.piecesInit(type, row1, col1));


            }
            // checks for validity aka whether any 2 pieces are on the chessboard. If it is, it continues, otherwise it prints invalid.
            if(ChessBoard1.checkPiece()) {
              object = ChessBoard1.findPiece(row,col);  // Given the row and column, find the piece at the location.

            // if there is a piece there, print out the piece
              if(object != null) {
                    output.print(object);


            // if this piece is in the attacking range of another piece, print out y which stands for yes. Otherwise print n for no.
              if(ChessBoard1.attacking(object)) { 
                    output.print(" y ");

              } else {
                  output.print(" n ");
              }
            
            } else {
                output.print("-");
            }

            } else {
              output.print("Invalid");
          }
          output.println();

            
        }
            
        
        // close the input file, and the output file
        input.close();
        output.close();
    
  }
 
// adds in chesspieces through switch cases. Basically identifies type of piece and creates a new object from their respective classes.
  public ChessPiece piecesInit(char type, int row, int col) {
      switch(type){
          case 'q':
          case 'Q':
              return new Queen(row, col, type);
          case 'k':
          case 'K':
              return new King(row, col, type);
          case 'n':
          case 'N':
              return new Knight(row, col, type);
          case 'r':
          case 'R':
              return new Rook(row, col, type);
          case 'b':
          case 'B':
              return new Bishop(row, col, type);
          default:
              return null;
      }

  }


      
  public static void main(String[] args) throws IOException {
            // check number of command line arguments is at least 2
    if(args.length < 2){
       System.out.println("Usage: java –jar ChessBoard.jar <input file> <output file>");
       System.exit(1);
    }

// create a new ChessBoard object 
    ChessBoard chess = new ChessBoard();

// apply the input file reader and output file writer function to ChessBoard object 
    chess.IOReaderandWriter(args);


  }

}
    
