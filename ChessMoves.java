/*-----------------------------------------------------------------------------------------------------------
Keerthi Krishnan 
kvkrishn 
File: ChessMoves.java 
This program performs the basic task of finding a piece and validating moves as well as checking if a repsective king is in check
------------------------------------------------------------------------------------------------------------- */

import java.util.Scanner;
import java.lang.*;
import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
    

public class ChessMoves {

// row, column, 3 string arrays, and 2 int arrays created as well as a LinkedList and ChessPiece object created 
  public int row;
  public int col;
  public String []ChessBoard; //stores all locations
  public String []coordinates;  //stores all the moves 
  // public String []moves; 

  private int startingPoint[]; // created to store the given move coordinates 
  private int endingPoint[];   // stores the end coordinates to where the chesspieces wanna move

  public char type = '1';   //states type of chess piece

  // public int count = 0;

  //creates objects of LinkedListChess and ChessPiece class
  public LinkedList Chesspieces;

  private void readInputFile(String[] args) throws FileNotFoundException, IOException{

    // row and col for each chess piece location
    int row = 0;
    int col = 0;

    // open files
    FileReader file = new FileReader(args[0]);
    PrintWriter out = new PrintWriter(new FileWriter(args[1]));   
    BufferedReader reader = new BufferedReader(file);
    String line = "";
    

   // read lines from in, extract and print tokens from each line
    while((line = reader.readLine()) != null ) {
        if(line == null) {
            break;
        }

        String[] scan = line.split(":");

        //locations into scan array
        ChessBoard = scan[0].split(" ");

        Chesspieces = new LinkedList();  

        //chess pieces into scan array
        String piece = "";


        for(int j=0;j<ChessBoard.length;j=j+3){
            piece = ChessBoard[j];
            type = piece.charAt(0);
            row = Integer.parseInt(ChessBoard[j+1]);
            col = Integer.parseInt(ChessBoard[j+2]);

            Chesspieces.addPiece(this.createPiece(type, row, col)); //adds all pieces in chess board
        }

        coordinates = scan[1].split(" ");


          canMove(Chesspieces, out);
    }
        
    file.close();
    out.close();

  }


/* This method basically checks whether a move is valid or not based on multiple conditions such as the color, whether it is in the attacking range, etc. */
private void canMove(LinkedList chess, PrintWriter output) {

  boolean isLegal = false;

  boolean whiteCheck = true; 

  for(int i=1;i<coordinates.length;i+=4) {
       isLegal = false;
      startingPoint = new int[2]; 
      endingPoint = new int[2];

      startingPoint[0] = Integer.parseInt(coordinates[i]); // stores column of starting coordinates
      startingPoint[1] = Integer.parseInt(coordinates[i+1]); // stores rows of starting coordinates 

      endingPoint[0] = Integer.parseInt(coordinates[i+2]); // stores destination column of move 
      endingPoint[1] = Integer.parseInt(coordinates[i+3]); // stores destination row of move

      Node startingNode = chess.findPiece(startingPoint[0], startingPoint[1]);  // finds if piece exists and stores it in node    
      Node endingNode = chess.findPiece(endingPoint[0], endingPoint[1]); // finds if piece exists and stores it in a node

    if(startingNode == null){
      isLegal = false;
      output.println(startingPoint[0] + " " + startingPoint[1] + " " + endingPoint[0] + " " + endingPoint[1] + " illegal");
      break;
    }

    if(chess.CheckKing(!whiteCheck)){
        isLegal = false;
        output.println(startingPoint[0] + " " + startingPoint[1] + " " + endingPoint[0] + " " + endingPoint[1] + " illegal");
        break;
    } else {
        isLegal = true;
    }
    // if the starting node is null, then a piece does not exist and it is illegal
      ChessPiece pieceStart = startingNode.getData();
      boolean whiteColor = pieceStart.getColor() == 'w' ? true : false;

      if(whiteColor != whiteCheck){
        isLegal = false;
        output.println(startingPoint[0] + " " + startingPoint[1] + " " + endingPoint[0] + " " + endingPoint[1] + " illegal");
        break;
      }
      if(pieceStart.isValidMove(startingPoint, endingPoint) == false) {
        isLegal = false;
        output.println(startingPoint[0] + " " + startingPoint[1] + " " + endingPoint[0] + " " + endingPoint[1] + " illegal");
        break;

      }

      // if the color of the piece is black and it is the first move then it is illegal
      if(pieceStart.getColor() == 'b' && i == 1){
        isLegal = false;
        output.println(startingPoint[0] + " " + startingPoint[1] + " " + endingPoint[0] + " " + endingPoint[1] + " illegal");
        break;
      } 

    // if the destination node is null, then check the move and set the new location at the destination node 
      if(endingNode == null){
        if(chess.checkMove(startingPoint, endingPoint, startingNode)){
          pieceStart.setLocation(endingPoint);
          isLegal = true;
        } else {

        // otherwise, print illegal
          isLegal = false;
          output.println(startingPoint[0] + " " + startingPoint[1] + " " + endingPoint[0] + " " + endingPoint[1] + " illegal");
          break;
        }

      } else {

        ChessPiece piecesEnd = endingNode.getData(); // stores the data that is in the ending node

    // if the color of the starting piece is equal to the color of the ending piece, then print illegal
        if(pieceStart.getColor() == piecesEnd.getColor()) {
          isLegal = false;
          output.println(startingPoint[0] + " " + startingPoint[1] + " " + endingPoint[0] + " " + endingPoint[1] + " illegal");
          break;
        } else {

        // if it is not the same color, then check the move and if check move returns true, then set isLegal to true
          if(chess.checkMove(startingPoint, endingPoint, startingNode)){
            isLegal = true;
            } else {
            // else print illegal
              isLegal = false;
              output.println(startingPoint[0] + " " + startingPoint[1] + " " + endingPoint[0] + " " + endingPoint[1] + " illegal");
              break;
            }
            // if the piece is able to attack the end piece, then delete that node and set the location of the starting piece to the end piece
            if(pieceStart.isAttacking(piecesEnd)) {
              chess.deletePiece(endingNode);
              pieceStart.setLocation(piecesEnd.getLocation());

              Node p1 = Chesspieces.findPiece(piecesEnd.getRow(), piecesEnd.getCol());
            
          }
        }
      }
      whiteCheck = !whiteCheck;

      if(chess.CheckKing(!whiteCheck)){
          isLegal = false;
          output.println(startingPoint[0] + " " + startingPoint[1] + " " + endingPoint[0] + " " + endingPoint[1] + " illegal");
          break;
      } else {
          isLegal = true;
      }
      // whiteCheck = !whiteCheck;
  }
  // if isLegal is true, then print legal wherever it is true 
  if(isLegal == true)
    output.println("legal");

  return;


  
} 


 

  /*  creates pieces in linked list based on type */
  public ChessPiece createPiece(char type, int row, int col) {
      switch(type){
          case 'k':
          case 'K':
              return new King(row, col, type);
          case 'q':
          case 'Q':
              return new Queen(row, col, type);
          case 'r':
          case 'R':
              return new Rook(row, col, type);
          case 'b':
          case 'B':
              return new Bishop(row, col, type);
          case 'n':
          case 'N':
              return new Knight(row, col, type);
          default:
              return null;
      }

  }



  //main method    
  public static void main(String[] args) throws IOException {

    // check number of command line arguments is at least 2
    if(args.length < 2){
       System.out.println("Usage: java –jar ChessMoves.jar <input file> <output file>");
       System.exit(1);
    }

    //creates new object ChessBoard
    ChessMoves chess = new ChessMoves(); 

    //calls input file reader method
    chess.readInputFile(args);


  }
}