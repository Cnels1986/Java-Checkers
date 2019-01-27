/*
Chris Nelson
Java Project
Checkers
*/

import java.util.Scanner;

class Checkers{

  // global variables that will store the current selected coordinates and player
  public static int row = 0;
  public static int col = 0;
  public static boolean player;

  public static boolean quit = false;

  // =================================================

  public static void main(String []args){
    //Creates the intial 8x8 board with the 2 sides filled in
    //1 = black piece
    //2 = red piece
    //3 = black king
    //4 = red king
    int[][] newBoard = {
                    {0,1,0,1,0,1,0,1},
                    {1,0,1,0,1,0,1,0},
                    {0,1,0,1,0,1,0,1},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {2,0,2,0,2,0,2,0},
                    {0,2,0,2,0,2,0,2},
                    {2,0,2,0,2,0,2,0}
                    };
    // int[][] board = {
    //                 {0,0,0,0,0,0,0,0},
    //                 {0,0,0,0,0,0,0,0},
    //                 {0,0,0,0,0,0,0,0},
    //                 {0,0,0,0,0,0,0,0},
    //                 {0,0,0,0,0,0,0,0},
    //                 {0,0,0,0,0,0,0,0},
    //                 {0,0,0,0,0,0,0,0},
    //                 {0,0,0,0,0,0,0,0}
    //                 };
    int[][] board = {
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,1,0,0,0,0},
                    {0,0,0,0,2,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0}
                    };

    boolean victory = false;
    boolean newGame = false;
    Scanner scan = new Scanner(System.in);
    String coordinate = null;
    boolean movePiece = false;

    do{
      // intial functions to start the game: clears screen, randomly selects a player, prints initial board
      clearScreen();
      selectPlayer();
      printBoard(board, row, col);
      while(victory != true && quit != true){
        printPlayer();
        coordinate = getCoordinates();
        //checks if the user enters coordinates on the board
        if(coordinate.equals("Exit") || coordinate.equals("exit")){
          quit = true;
          newGame = false;
          break;
        }
        else{
          movePiece = verifyCoordinates(coordinate, board);
          if(movePiece == true){
            printBoard(board, row, col);
            getDirection(board);
            player = !player;
            printBoard(board, 0, 0);
            victory = checkForVictory(board);
          }
        }
      }
      if(quit != true){
        System.out.print("\nDo you want to play another game?\n1 - new game\n2 - quit\n");
        boolean validAnswer = false;
        String answer = null;
        while(validAnswer == false){
          answer = scan.nextLine();
          if(answer.charAt(0) == '1' || answer.charAt(0) == '2')
            validAnswer = true;
        }
        // new game, board is reset
        if(answer.charAt(0) == '1'){
          System.out.println("Test");
          newGame = true;
          selectPlayer();
          board = newBoard;
          victory = false;
          row = 0;
          col = 0;
        }
        else if(answer.charAt(0) == '2')
          newGame = false;
      }
      else{
        newGame = false;
        quit = true;
      }
    } while(newGame == true && quit == false);
    clearScreen();
  }


  // =================================================

  public static boolean movePiece(int[][] board, String m){
    boolean jump = false;   //tracks if a jump happened
    int piece = board[row][col];

    // normal pieces
    if(piece == 1 || piece ==2){
      // loops through the direction string from the user
      for(int i=0; i<m.length(); i++){
        // left moves
        if(m.charAt(i) == 'l' && col-1 >= 0){
          if(piece == 1){
            // checks for the jump first
            if(board[row+1][col-1] == 2 || board[row+1][col-1] == 4)
              jump = jump(board,row,col,'l');
            // checks for an empty spot, if move is possible it will break and end the loop
            else if(board[row+1][col-1] == 0){
              board[row+1][col-1] = 1;
              board[row][col] = 0;
              row = row+1;
              col = col-1;
              // makes piece a king
              if(row == 7)
                board[row][col] = 3;
              return true;
            }
          }
          else if(piece == 2){
            if(board[row-1][col-1] == 1 || board[row-1][col-1] == 3)
              jump = jump(board,row,col,'l');
            else if(board[row-1][col-1] == 0){
              board[row-1][col-1] = 2;
              board[row][col] = 0;
              row = row-1;
              col = col-1;
              if(row == 0)
                board[row][col] = 4;
              return true;
            }
          }
        }
        // right moves
        else if(m.charAt(i) == 'r' && col+1 <= 7){
          if(piece == 1){
            if(board[row+1][col+1] == 2 || board[row+1][col+1] == 4)
              jump = jump(board,row,col,'r');
            else if(board[row+1][col+1] == 0){
              board[row+1][col+1] = 1;
              board[row][col] = 0;
              row = row+1;
              col = col+1;
              if(row == 7)
                board[row][col] = 3;
              return true;
            }
          }
          else if(piece == 2){
            if(board[row-1][col+1] == 1 || board[row-1][col+1] == 3)
              jump = jump(board,row,col,'r');
            else if(board[row-1][col+1] == 0){
              board[row-1][col+1] = 2;
              board[row][col] = 0;
              row = row-1;
              col = col+1;
              if(row == 0)
                board[row][col] = 4;
              return true;
            }
          }
        }
      }
    }
    // king pieces
    else if(piece == 3 || piece == 4){
      for(int i=0 ; i<m.length(); i++){
        // up right
        if(m.charAt(i) == '2' && row-1 >= 0 && col+1 <= 7){
          // king jump
          if(board[row-1][col+1] != 0){
            // black king
            if(piece == 3 && (board[row-1][col+1] == 2 || board[row-1][col+1] == 4))
              jump = jump(board,row,col,'2');
            // red king
            else if(piece == 4 && (board[row-1][col+1] == 1 || board[row-1][col+1] == 3))
              jump = jump(board,row,col,'2');
          }
          // king normal move
          else if(board[row-1][col+1] == 0){
            board[row-1][col+1] = piece;
            board[row][col] = 0;
            row = row-1;
            col = col+1;
            return true;
          }
        }
        // down right
        else if(m.charAt(i) == '4' && row+1 <= 7 && col+1 <= 7){
          if(board[row+1][col+1] != 0){
            if(piece == 3 && (board[row+1][col+1] == 2 || board[row+1][col+1] == 4)){
              System.out.println(row + " - " + col);
              jump = jump(board,row,col,'4');
            }
            else if(piece == 4 && (board[row+1][col+1] == 1 || board[row+1][col+1] == 3))
              jump = jump(board,row,col,'4');
          }
          else if(board[row+1][col+1] == 0){
            board[row+1][col+1] = piece;
            board[row][col] = 0;
            row = row+1;
            col = col+1;
            return true;
          }
        }
        // up left
        else if(m.charAt(i) == '1' && row-1 >= 0 && col-1 >= 0){
          if(board[row-1][col-1] != 0){
            if(piece == 3 && (board[row-1][col-1] == 2 || board[row-1][col-1] == 4))
              jump = jump(board,row,col,'1');
            else if(piece == 4 && (board[row-1][col-1] == 1 || board[row-1][col-1] == 3))
              jump = jump(board,row,col,'1');
          }
          else if(board[row-1][col-1] == 0){
            board[row-1][col-1] = piece;
            board[row][col] = 0;
            row = row-1;
            col = col-1;
            return true;
          }
        }
        // down left
        else if(m.charAt(i) == '3' && row+1 <= 7 && col-1 >= 0){
          if(board[row+1][col-1] != 0){
            if(piece == 3 && (board[row+1][col-1] == 2 || board[row+1][col-1] == 4))
              jump = jump(board,row,col,'3');
            else if(piece == 4 && (board[row+1][col-1] == 1 || board[row+1][col-1] == 3))
              jump = jump(board,row,col,'3');
          }
          else if(board[row+1][col-1] == 0){
            board[row+1][col-1] = piece;
            board[row][col] = 0;
            row = row+1;
            col = col-1;
            return true;
          }
        }
      }
    }
    return jump;
  }

  // =================================================

  // function gets the users input on what direction they want to more their piece l-left or r-right. Hopefully will be able to string multiple moves together later i.e.: llr left, left, right
  public static void getDirection(int[][] board){
    String movement = null;
    Scanner scan = new Scanner(System.in);
    boolean goodInput = false;

    while(goodInput == false){
      if(board[row][col] == 1 || board[row][col] == 2){
        System.out.print("\n-- Type exit to quit --\n\nEnter the direction you would like to move/jump your piece:\nl=left\nr=right\nMultiple moves can be entered if jumps are available (ie: llr -> left, left, right)\nMovement:  ");
        movement = scan.nextLine();
        movement = movement.toLowerCase();
        if(movement.equals("exit")){
          quit = true;
          break;
        }
        else if(movement.contains("l") || movement.contains("r")){
          boolean move = movePiece(board, movement);
          while(move == false){
            System.out.print("\n*** Cannot move that direction ***\nPlease reenter (l/r): ");
            movement = scan.nextLine();
            movement = movement.toLowerCase();
            if(movement.equals("exit")){
              quit = true;
              break;
            }
            else move = movePiece(board, movement);
          }
          goodInput = true;
        }
      }
      else if(board[row][col] == 3 || board[row][col] == 4){
        System.out.println("\n-- Type exit to quit --\n\nEnter the direction you would like to move/jump your piece:\n1 = up left\n2 = up right\n3 = down left\n4 = down right\nMultiple moves can be entered if jumps are available (ie: 1142 -> up right, up right, down left, down right)\nMovement: ");
        movement = scan.nextLine();
        movement = movement.toLowerCase();
        if(movement.equals("exit")){
          quit = true;
          break;
        }
        else if(movement.contains("1") || movement.contains("2") || movement.contains("3") || movement.contains("4")){
          boolean move = movePiece(board, movement);
          while(move == false){
            System.out.print("\n*** Cannot move that direction ***\nPlease reenter (1|2|3|4): ");
            movement = scan.nextLine();
            movement = movement.toLowerCase();
            if(movement.equals("exit")){
              quit = true;
              break;
            }
            else
              move = movePiece(board, movement);
          }
          goodInput = true;
        }
      }
    }
  }

  // =================================================

  // function checks whether the piece selected has any available moves or jumps, if not user must select another piece
  public static boolean isItMovable(int r, int c,int[][] board){
    boolean move = isThereAMove(r,c,board);
    boolean jump = false;
    int piece = board[r][c];
    // king pieces
    if(piece == 3 || piece == 4){
      if(r+2 <= 7){
        // down left
        if(col-2 >= 0){
          // black king
          if((board[r+1][c-1] == 2 || board[r+1][c-1] == 4) && piece == 3 && (board[r+2][c-2] == 0))
            jump = true;
          // red king
          if((board[r+1][c-1] == 1 || board[r+1][c-1] == 3) && piece == 4 && (board[r+2][c-2] == 0))
            jump = true;
        }
        // down right
        if(col+2 <= 0){
          if((board[r+1][c+1] == 2 || board[r+1][c+1] == 4) && piece == 3 && (board[r+2][c+2] == 0))
            jump = true;
          if((board[r+1][c+1] == 1 || board[r+1][c+1] == 3) && piece == 4 && (board[r+2][c+2] == 0))
            jump = true;
        }
      }
      if(r-2 >= 0){
        // up left
        if(col-2 >= 0){
          if((board[r-1][c-1] == 2 || board[r-1][c-1] == 4) && piece == 3 && (board[r-2][c-2] == 0))
            jump = true;
          if((board[r-1][c-1] == 1 || board[r-1][c-1] == 3) && piece == 4 && (board[r-2][c-2] == 0))
            jump = true;
        }
        // up right
        if(col+2 <= 0){
          if((board[r-1][c+1] == 2 || board[r-1][c+1] == 4) && piece == 3 && (board[r-2][c+2] == 0))
            jump = true;
          if((board[r-1][c+1] == 1 || board[r-1][c+1] == 3) && piece == 4 && (board[r-2][c+2] == 0))
            jump = true;
        }
      }
    }
    // normal black, down
    else if(player == false){
      if(r+2 <= 7){
        if(c-2 >= 0){
          if((board[r+1][c-1] == 2 || board[r+1][c-1] == 4) && board[r+2][c-2] == 0)
            jump = true;
        }
        if(c+2 <= 7){
          if((board[r+1][c+1] == 2 || board[r+1][c+1] == 4 )&& board[r+2][c+2] == 0)
            jump = true;
        }
      }
    }
    // normal red, up
    else if(player == true){
      if(r-2 >= 0){
        if(c-2 >= 0){
          if((board[r-1][c-1] == 1 || board[r-1][c-1] == 3 ) && board[r-2][c-2] == 0)
            jump = true;
        }
        if(c+2 <= 7){
          if((board[r-1][c+1] == 1 || board[r-1][c+1] == 3 ) && board[r-2][c+2] == 0)
            jump = true;
        }
      }
    }
    return (jump || move);
  }

  // =================================================

  // function double checks that the coordinates the user enters is usable to move a piece
  public static boolean verifyCoordinates(String coordinate, int[][] board){
    boolean goodCoordinate = checkCoordinate(coordinate, board);
    if(goodCoordinate == true){
      row = Character.getNumericValue(coordinate.charAt(0))-1;
      col = Character.getNumericValue(coordinate.charAt(2))-1;
      if((board[row][col] == 1 || board[row][col] == 3) && player == false && isItMovable(row,col,board) == true){
        return true;
      }
      else if((board[row][col] == 2 || board[row][col] == 4) && player == true  && isItMovable(row,col,board) == true){
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
    // // king pieces, can move all directions
    else if(piece == 3 || piece == 4){
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
      if(rowDown <= 7){
        if(colLeft >= 0){
          if(board[rowDown][colLeft] == 0)
            return true;
        }
        if(colRight <= 7){
          if(board[rowDown][colRight] == 0)
            return true;
        }
      }
    }
    return false;
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
            if((board[rowDown][colLeft] == 2 || board[rowDown][colLeft] == 4) && board[jumpDown][jumpLeft] == 0){
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
            if((board[rowUp][colLeft] == 1 || board[rowUp][colLeft] == 3) && board[jumpUp][jumpLeft] == 0){
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
            if((board[rowDown][colRight] == 2 || board[rowDown][colRight] == 4) && board[jumpDown][jumpRight] == 0){
                board[rowDown][colRight] = 0;
                board[jumpDown][jumpRight] = 1;
                board[r][c] = 0;
                row = jumpDown;
                col = jumpRight;
                return true;
            }
          }
          else
            return false;
        }
        // red right
        else{
          if(jumpUp >= 0 && jumpRight <= 7){
            if((board[rowUp][colRight] == 1 || board[rowUp][colRight] ==3 ) && board[jumpUp][jumpRight] == 0){
              board[rowUp][colRight] = 0;
              board[jumpUp][jumpRight] = 2;
              board[r][c] = 0;
              row = jumpUp;
              col = jumpRight;
              return true;
            }
          }
          else
            return false;
        }
      }
    }
    // king pieces
    else if(piece == 3 || piece == 4){
      if(piece == 3){
        // up left
        if(direction == '1'){
          if(jumpUp >= 0 && jumpLeft >= 0){
            if((board[rowUp][colLeft] == 2 || board[rowUp][colLeft] == 4) && board[jumpUp][jumpLeft] == 0){
              board[rowUp][colLeft] = 0;
              board[jumpUp][jumpLeft] = 3;
              board[r][c] = 0;
              row = jumpUp;
              col = jumpLeft;
              return true;
            }
          }
          else
            return false;
        }
        // up right
        else if(direction == '2'){
          if(jumpUp >= 0 && jumpRight <= 7){
            if((board[rowUp][colRight] == 2 || board[rowUp][colRight] == 4) && board[jumpUp][jumpRight] == 0){
              board[rowUp][colRight] = 0;
              board[jumpUp][jumpRight] = 3;
              board[r][c] = 0;
              row = jumpUp;
              col = jumpLeft;
              return true;
            }
          }
          else
            return false;
        }
        // down left
        else if(direction == '3'){
          if(jumpDown <= 7 && jumpLeft >= 0){
            if((board[rowDown][colLeft] == 2 || board[rowDown][colLeft] == 4) && board[jumpDown][jumpLeft] == 0){
              board[rowDown][colLeft] = 0;
              board[jumpDown][jumpLeft] = 3;
              board[r][c] = 0;
              row = jumpDown;
              col = jumpLeft;
              return true;
            }
          }
          else
            return false;
        }
        // down right
        else if(direction == '4'){
          if(jumpDown <= 7 && jumpRight <= 7){
            if((board[rowDown][colRight] == 2 || board[rowDown][colRight] == 4) && board[jumpDown][jumpRight] == 0){
              System.out.println("HERE");
              board[rowDown][colRight] = 0;
              board[jumpDown][jumpRight] = 3;
              board[r][c] = 0;
              row = jumpDown;
              col = jumpRight;
              return true;
            }
          }
          else
            return false;
        }
      }
      else if(piece == 4){
        // up left
        if(direction == '1'){
          if(jumpUp >= 0 && jumpLeft >= 0){
            if((board[rowUp][colLeft] == 1 || board[rowUp][colLeft] == 3) && board[jumpUp][jumpLeft] == 0){
              board[rowUp][colLeft] = 0;
              board[jumpUp][jumpLeft] = 4;
              board[r][c] = 0;
              row = jumpUp;
              col = jumpLeft;
              return true;
            }
          }
          else
            return false;
        }
        // up right
        else if(direction == '2'){
          if(jumpUp >= 0 && jumpRight <= 7){
            if((board[rowUp][colRight] == 1 || board[rowUp][colRight] == 3) && board[jumpUp][jumpRight] == 0){
              board[rowUp][colRight] = 0;
              board[jumpUp][jumpRight] = 4;
              board[r][c] = 0;
              row = jumpUp;
              col = jumpLeft;
              return true;
            }
          }
          else
            return false;
        }
        // down left
        else if(direction == '3'){
          if(jumpDown <= 7 && jumpLeft >= 0){
            if((board[rowDown][colLeft] == 1 || board[rowDown][colLeft] == 3) && board[jumpDown][jumpLeft] == 0){
              board[rowDown][colLeft] = 0;
              board[jumpDown][jumpLeft] = 4;
              board[r][c] = 0;
              row = jumpDown;
              col = jumpLeft;
              return true;
            }
          }
          else
            return false;
        }
        // down right
        else if(direction == '4'){
          if(jumpDown <= 7 && jumpRight <= 7){
            if((board[rowDown][colRight] == 1 || board[rowDown][colRight] == 3) && board[jumpDown][jumpRight] == 0){
              board[rowDown][colRight] = 0;
              board[jumpDown][jumpRight] = 4;
              board[r][c] = 0;
              row = jumpDown;
              col = jumpRight;
              return true;
            }
          }
          else
            return false;
        }
      }
    }
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
    if(red == 0 || black == 0){
      if(red == 0){
        System.out.println("\n*** Player 1 wins ***\n");
      }
      else if(black == 0){
        System.out.println("\n**** Player 2 wins ***\n");
      }
        return true;
    }
    else
      return false;
  }

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
            case 3:
              System.out.print("| (-B-) ");
              break;
            case 4:
              System.out.print("| (-R-) ");
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
            case 3:
              System.out.print("|  -B-  ");
              break;
            case 4:
              System.out.print("|  -R-  ");
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

  // =================================================

  public static void selectPlayer(){
    // gets either 1 or 2 at random to see which player has the first turn
    int random = (Math.random() <= 0.5) ? 1 : 2;
    //will track which player's turn it is, alternating once complete
    //false = player 1, black
    //true = player 2, red
    if(random == 1)
      player = false;
    else
      player = true;
  }
}
