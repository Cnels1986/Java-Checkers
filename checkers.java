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
    // int[][] board = {
    //                 {0,1,0,1,0,1,0,1},
    //                 {1,0,1,0,1,0,1,0},
    //                 {0,1,0,1,0,1,0,1},
    //                 {0,0,0,0,0,0,0,0},
    //                 {0,0,0,0,0,0,0,0},
    //                 {2,0,2,0,2,0,2,0},
    //                 {0,2,0,2,0,2,0,2},
    //                 {2,0,2,0,2,0,2,0}
    //                 };
    int[][] board = {
                    {0,1,0,1,0,1,0,1},
                    {1,0,1,0,1,0,2,0},
                    {0,1,0,1,0,0,0,1},
                    {0,0,0,0,1,0,0,0},
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

    clearScreen();
    printBoard(board, row, col);
    while(victory != true && quit != true){
      printPlayer();
      coordinate = getCoordinates();

      //checks if the user enters coordinates on the board
      if(coordinate.equals("Exit") || coordinate.equals("exit")){
        quit = true;
      }
      else{
        movePiece = verifyCoordinates(coordinate, board);
        if(movePiece == true){
          printBoard(board, row, col);
          getDirection(board);
          player = !player;
          printBoard(board, 0, 0);
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
            else if(board[row+1][col-1] == 2){
              boolean jump = jump(board, row, col, 'l');
              return jump;
            }
          }
          // move right
          else if(m.charAt(i) == 'r' && col+1 <= 7){
            if(board[row+1][col+1] == 0){
              board[row+1][col+1] = 1;
              board[row][col] = 0;
              row = row+1;
              col = col+1;
              return true;
            }
            else if(board[row+1][col+1] == 2){
              boolean jump = jump(board, row, col, 'r');
              return jump;
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
            else if(board[row-1][col-1] == 1){
              boolean jump = jump(board, row, col, '1');
              return jump;
            }
          }
          // move right
          else if(m.charAt(i) == 'r' && col+1 <= 7){
            if(board[row-1][col+1] == 0){
              board[row-1][col+1] = 2;
              board[row][col] = 0;
              row = row-1;
              col = col+1;
              return true;
            }
            else if(board[row-1][col+1] == 1){
              boolean jump = jump(board, row, col, 'r');
              return jump;
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

  // function gets the users input on what direction they want to more their piece l-left or r-right. Hopefully will be able to string multiple moves together later i.e.: llr left, left, right
  public static void getDirection(int[][] board){
    String movement = null;
    Scanner scan = new Scanner(System.in);
    boolean goodInput = false;

    while(goodInput == false){
      System.out.print("\nEnter the direction you would like to move your piece:\nl=left\nr=right\nMultiple moves can be entered if moves are legal (ie: llr -> left, left, right)\nMovement:  ");

      movement = scan.nextLine();
      movement = movement.toLowerCase();

      if(movement.equals("Exit") || movement.equals("exit"))
        quit = true;
      else if(movement.contains("l") || movement.contains("r")){
        boolean move = movePiece(board, movement);
        while(move == false){
          System.out.print("\n*** Cannot move that direction ***\nPlease reenter (l/r): ");
          movement = scan.nextLine();
          movement = movement.toLowerCase();
          move = movePiece(board, movement);
        }
        goodInput = true;
      }
    }
  }

  // =================================================

  public static boolean isItMovable(int r, int c,int[][] board){
    boolean move = isThereAMove(r,c,board);
    boolean jump = false;
    int piece = board[r][c];
    // king pieces
    if(piece == 3 || piece == 4){
      jump = true;
    }
    // normal black, down
    else if(player == false){
      if(r+2 <= 7){
        if(c-2 >= 0){
          if(board[r+1][c-1] == 2 && board[r+2][c-2] == 0)
            jump = true;
        }
        if(c+2 <= 7){
          if(board[r+1][c+1] == 2 && board[r+2][c+2] == 0)
            jump = true;
        }
      }
    }
    // normal red, up
    else if(player == true){
      if(r-2 >= 0){
        if(c-2 >= 0){
          if(board[r-1][c-1] == 1 && board[r-2][c-2] == 0)
            jump = true;
        }
        if(c+2 <= 7){
          if(board[r-1][c+1] == 1 && board[r-2][c+2] == 0)
            jump = true;
        }
      }
    }
    return (jump || move);
  }

  // =================================================

  public static boolean verifyCoordinates(String coordinate, int[][] board){
    boolean goodCoordinate = checkCoordinate(coordinate, board);
    if(goodCoordinate == true){
      row = Character.getNumericValue(coordinate.charAt(0))-1;
      col = Character.getNumericValue(coordinate.charAt(2))-1;
      if((board[row][col] == 1 /*|| board[row][col] == 3*/) && player == false && isItMovable(row,col,board) == true){
        return true;
      }
      else if((board[row][col] == 2 /*&& board[row][col] == 4*/) && player == true  && isItMovable(row,col,board) == true){
        return true;
      }
      else{
        System.out.println("\n*** NOT YOUR PIECE OR PIECE IS UNMOVABLE ***");
        return false;
      }
    }
    else{
      System.out.println("\n*** INVALID COORDINATE, PLEASE REENTER ***");
      return false;
    }
  }

  // =================================================

  // function gets input from the user for the coordinates
  public static String getCoordinates(){
    Scanner scan = new Scanner(System.in);
    System.out.print("\nEnter the coordinates for the piece you wish to move. Enter exit to quit.\nExample = row,col : ");
    String coordinate = scan.nextLine();
    return coordinate;
  }

  // =================================================

  // function just prints which player's turn it is
  public static void printPlayer(){
    if(player == false)
      System.out.println("\nPlayer 1's Turn (b/B)");
    else
      System.out.println("\nPlayer 2's Turn (r/R)");
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
  public static boolean jump(int[][] board, int r, int c, char direction){
    int piece = board[r][c];
    // potential coordinates for other player's pieces
    int rowUp = r-1;
    int rowDown = r+1;
    int colLeft = c-1;
    int colRight = c+1;
    // potential coordinates for piece to jump to
    int jumpUp = r-2;
    int jumpDown = r+2;
    int jumpLeft = c-2;
    int jumpRight = c+2;

    // normal pieces
    if(piece == 1 || piece == 2){
      // left moves
      if(direction == 'l'){
        // black left
        if(player == false){
          if(jumpDown <= 7 && jumpLeft >=0){
            // if there's an opponants piece and the jump space is empty
            if(board[rowDown][colLeft] == 2 && board[jumpDown][jumpLeft] == 0){
                board[rowDown][colLeft] = 0;
                board[jumpDown][jumpLeft] = 1;
                board[r][c] = 0;
                row = jumpDown;
                col = jumpLeft;
                return true;
            }
          }
          else
            return false;
        }
        // red left
        else{
          if(jumpUp >= 0 && jumpLeft >= 0){
            if(board[rowUp][colLeft] == 1 && board[jumpUp][jumpLeft] == 0){
              board[rowUp][colLeft] = 0;
              board[jumpUp][jumpLeft] = 2;
              board[r][c] = 0;
              row = jumpUp;
              col = jumpLeft;
              return true;
            }
          }
          else
            return false;
        }
      }
      else if(direction == 'r'){
        // black right
        if(player == false){
          if(jumpDown <= 7 && jumpRight <= 7){
            if(board[rowDown][colRight] == 2 && board[jumpDown][jumpRight] == 0){
                board[rowDown][colRight] = 0;
                board[jumpDown][jumpRight] = 1;
                board[r][c] = 0;
                row = jumpDown;
                col = jumpLeft;
                return true;
            }
          }
          else
            return false;
        }
        // red right
        else{
          if(jumpUp >= 0 && jumpRight <= 7){
            if(board[rowUp][colRight] == 1 && board[jumpUp][jumpRight] == 0){
              board[rowUp][colRight] = 0;
              board[jumpUp][jumpRight] = 2;
              board[r][c] = 0;
              row = jumpUp;
              col = jumpLeft;
              return true;
            }
          }
          else
            return false;
        }
      }
    }
    // king pieces
    // else if(piece == 3 || piece == 4){
    //
    // }
    return false;
  }

  // =================================================

  // function will scan the board/matrix to see if a player has been eliminated
  public static boolean checkForVictory(int[][] board){
    int red = 0;
    int black = 0;
    for(int a=0 ; a<=7; a++){
      for(int b=0; b<=7; b++){
        if(board[a][b] == 1 || board[a][b] == 3)
          black++;
        else if(board[a][b] == 2 || board[a][b] == 4)
          red++;
      }
    }
    if(red > 0 && black >0)
      return false;
    else
      return true;
  }

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
      // System.out.print("|  " + a +"\n");
      if(a != 7){
        System.out.println("   |       |       |       |       |       |       |       |       |");
        System.out.println("   -----------------------------------------------------------------");
        System.out.println("   |       |       |       |       |       |       |       |       |");
      }
    }
    // prints the bottom border
    System.out.println("   |       |       |       |       |       |       |       |       |");
    System.out.println("   -----------------------------------------------------------------");
    // System.out.println("       1       2       3       4       5       6       7       8    ");
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
