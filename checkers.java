/*
Chris Nelson
Java Project
Checkers
*/

import java.util.Scanner;

class Checkers{

  // global variables that will store the current selected coordinates
  public static int row = 0;
  public static int col = 0;
  // will store current player
  public static boolean player;
  public static boolean quit = false;

  // =================================================

  public static void main(String []args){
    //Creates the intial 8x8 board with the 2 sides filled in
    //1 = black piece
    //2 = red piece
    //3 = black king
    //4 = red king
    int[][] board = {
                    {0,1,0,1,0,1,0,1},
                    {1,0,1,0,1,0,1,0},
                    {0,1,0,1,0,1,0,1},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {2,0,2,0,2,0,2,0},
                    {0,2,0,2,0,2,0,2},
                    {2,0,2,0,2,0,2,0}
                  };

    boolean victory = false;
    // gets either 1 or 2 at random to see which player has the first turn
    int random = (Math.random() <= 0.5) ? 1 : 2;
    //will track which player's turn it is, alternating once complete
    //false = player 1, black
    //true = player 2, red
    if(random == 1)
      player = false;
    else
      player = true;

    Scanner scan = new Scanner(System.in);
    String coordinate = null;
    boolean movePiece = false;
    boolean canMovePiece = false;    // will track if selected piece is movables
    String movement = null;

    clearScreen();
    printBoard(board, row, col);

    while(victory != true && quit != true){
      if(player == false)
        System.out.println("\nPlayer 1's Turn (b/B)");
      else
        System.out.println("\nPlayer 2's Turn (r/R)");

      System.out.print("\nEnter the coordinates for the piece you wish to move. Enter exit to quit.\nExample = row,col : ");
      coordinate = scan.nextLine();

      //checks if the user enters coordinates on the board
      if(coordinate.equals("Exit") || coordinate.equals("exit")){
        quit = true;
      }
      else{
        boolean goodCoordinate = checkCoordinate(coordinate, board);
        if(goodCoordinate == true){
          row = Character.getNumericValue(coordinate.charAt(0))-1;
          col = Character.getNumericValue(coordinate.charAt(2))-1;
          if((board[row][col] == 1 /*|| board[row][col] == 3*/) && player == false && isItMovable(row,col,board) == true){
            movePiece = true;
          }
          else if((board[row][col] == 2 /*&& board[row][col] == 4*/) && player == true  && isItMovable(row,col,board) == true){
            movePiece = true;
          }
          else{
            System.out.println("\n*** NOT YOUR PIECE OR PIECE IS UNMOVABLE ***");
            movePiece = false;
          }
        }
        else{
          System.out.println("\n*** INVALID COORDINATE, PLEASE REENTER ***");
          movePiece = false;
        }

        if(movePiece == true){
          printBoard(board, row, col);
          System.out.print("\nEnter the direction you would like to move your piece:\nl=left\nr=right\nMmultiple moves can be entered if moves are legal (ie: llr -> left, left, right)\nMovement:  ");
          movement = scan.nextLine();
          movement = movement.toLowerCase();
          if(movement.equals("Exit") || movement.equals("exit"))
            quit = true;
          else if(movement.contains("l") || movement.contains("r")){
            boolean move = movePiece(board, movement);
            System.out.println(move);
            // while(move == false){
            //   System.out.println("*** Cannot move that direction ***");
            //   movement = scan.nextLine();
            //   movement = movement.toLowerCase();
            //   move = movePiece(board, movement);
            // }
            printBoard(board, 0, 0);
          }
        }
      }
    }
    clearScreen();
  }

  // =================================================

  public static boolean movePiece(int[][] board, String m){
    boolean move = true;
    boolean badMove = false;
    int piece = board[row][col];
    while(move == true){
      for(int i=0 ; i<m.length(); i++){
        //black
        if(piece == 1){
          // move left
          if(m.charAt(i) == 'l' && col-1 >= 0){
            if(board[row+1][col-1] == 0){
              board[row+1][col-1] = 1;
              board[row][col] = 0;
              row = row+1;
              col = col-1;
              return true;
            }
          }
          else
            return false;
          // move right
          if(m.charAt(i) == 'r' && col+1 <= 7){
            if(board[row+1][col+1] == 0){
              board[row+1][col+1] = 1;
              board[row][col] = 0;
              row = row+1;
              col = col+1;
              return true;
            }
          }
          else
            return false;
        }
        //red
        else if(piece == 2){
          // move left
          if(m.charAt(i) == 'l' && col-1 >= 0){
            if(board[row-1][col-1] == 0){
              board[row-1][col-1] = 2;
              board[row][col] = 0;
              row = row-1;
              col = col-1;
              return true;
            }
          }
          else
            return false;
          // move right
          if(m.charAt(i) == 'r' && col+1 <= 7){
            if(board[row-1][col+1] == 0){
              board[row-1][col+1] = 2;
              board[row][col] = 0;
              row = row-1;
              col = col+1;
              return true;
            }
          }
          else
            return false;
        }
      }
      move = false;
    }
    return false;
  }

  // =================================================

  public static boolean isItMovable(int r, int c,int[][] board){
    return isThereAMove(r,c,board);
  }

  // =================================================

  // function will check is there is a possible move
  public static boolean isThereAMove(int r, int c, int[][] board){
    int piece = board[r][c];
    // gets the possible new coordinates
    int rowUp = r-1;
    int rowDown = r+1;
    int colLeft = c-1;
    int colRight = c+1;
    // normal pieces
    if(piece == 1 || piece == 2){
      // black pieces, moving down
      if(player == false){
        // checks if the new row is within range of the matrix
        if(rowDown <= 7){
          // checks if the move left is within range of the matrix
          if(colLeft >= 0){
            // checks if new spot is empty
            if(board[rowDown][colLeft] == 0)
              return true;
          }
          // checks if the move right is within range of the matrix
          if(colRight <= 7){
            // checks if new spot is empty
            if(board[rowDown][colRight] == 0)
              return true;
          }
        }
      }
      // red pieces, moving up
      else{
        if(rowUp >= 0){
          if(colLeft >= 0){
            if(board[rowUp][colLeft] == 0)
              return true;
          }
          if(colRight <= 7){
            if(board[rowUp][colRight] == 0)
              return true;
          }
        }
      }
    }
    return false;
    // // king pieces
    // else if(piece == 3 || piece == 4){
    //   // black kings
    //   if(player == false){
    //
    //   }
    //   // red kings
    //   else{
    //
    //   }
    // }
  }

  // =================================================

  // function will check is there is a possible jump
  // public static boolean isThereAJump(){
  //
  // }

  // =================================================

  // function will turn the piece into a king, changing its movements
  // public static void makePieceKing(){
  //
  // }

  // =================================================

  //function clears screen so the build can be displayed at the top at all times
  public static void clearScreen(){
    //Clears console before displaying anything
    System.out.print("\033[H\033[2J");
  }

  // =================================================

  // Function takes the matrix that represents the board and prints it out
  public static void printBoard(int[][] board, int row, int col){
    // rows
    clearScreen();
    for(int a=0; a<8; a++){
        // prints the top border
        if(a == 0){
          System.out.println("       1       2       3       4       5       6       7       8    ");
          System.out.println("   -----------------------------------------------------------------");
          System.out.println("   |       |       |       |       |       |       |       |       |");
        }
      // columns
      for(int b=0; b<8; b++){
        if(b == 0)
          System.out.print((a+1) + "  ");

        if(a == row && b == col){
          switch(board[a][b]){
            case 0:
              System.out.print("|       ");
              break;
            case 1:
              System.out.print("|  (b)  ");
              break;
            case 2:
              System.out.print("|  (r)  ");
              break;
          }
        }
        else{
          switch(board[a][b]){
            case 0:
              System.out.print("|       ");
              break;
            case 1:
              System.out.print("|   b   ");
              break;
            case 2:
              System.out.print("|   r   ");
              break;
          }
        }
      }
      // new line
      System.out.print("|\n");
      if(a != 7){
        System.out.println("   |       |       |       |       |       |       |       |       |");
        System.out.println("   -----------------------------------------------------------------");
        System.out.println("   |       |       |       |       |       |       |       |       |");
      }
    }
    // prints the bottom border
    System.out.println("   |       |       |       |       |       |       |       |       |");
    System.out.println("   -----------------------------------------------------------------");
  }

  // =================================================

  // function checks the coordinates entered to make sure there is a piece at that location
  public static boolean checkCoordinate(String coordinate, int[][] board){
    // coordinate string must be 3 characters long, anything else is incorrect input
    if(coordinate.length() == 3){
      // gets the numeric values of the coordinates entered in the first and third spot of the string x,y
      int row = Character.getNumericValue(coordinate.charAt(0));
      int col = Character.getNumericValue(coordinate.charAt(2));
      // checks the values of the row and column to make sure they're both within the range of 1-8, returns true if they are and false if they are not
      if((row >= 1 && row <= 8) && (col >= 1 && col <= 8)){
        // checks if there's a piece in that coordinate
        if(board[row-1][col-1] != 0)
          return true;
        else
          return false;
      }
      else
        return false;
    }
    else
      return false;
  }
}
