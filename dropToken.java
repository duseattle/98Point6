//	Author: Abdullah Mohammad
//  School: University of Washington - Computer Science
//  Date: 11/28/2018



import java.util.*;
//import java.util.scanner.*;
public class dropToken {

	public static void main(String []args) {
		final int BOARDSIZE = 4;
		//gameboard is a square matrix that is set to 4x4 to satisfy the game description but can be changed to any size 
		//by changing the constant BOARDSIZE above.
		int[][] board = new int[BOARDSIZE][BOARDSIZE];	
		//stores a list of the rows that were previously (successfully) put into
		ArrayList<Integer> history = new ArrayList<Integer>();
		System.out.println("Welcome to 98Point6 Drop Token!");
	    System.out.println("Let's Start!\nPlayer 1 goes first.");
	    Scanner input = new Scanner(System.in);
	    String insert = input.nextLine();
	    boolean win = false;
	    while (!insert.equals("EXIT")) {
	    	String[] command = insert.split("\\s+");
	        if (command[0].equals("PUT") && !win) {
	        	int position = -1;
	        	while (position == -1 ) {  //this while-loop is used to make sure the input after "PUT" is an integer
	        		try {
	        			position = Integer.parseInt(command[1]);
	        	    } catch(Exception e) {
	        			break;
	        		}
	        	}
        		if (position < 1 || position > board.length) {  //makes sure the column number is valid on the gameboard.
        			System.out.println("ERROR: Valid row numbers are 1-" + board.length);
        		}
        		else {
        			int checkRow = getRow(board, position); //returns lowest unoccupied row of the board
        			if (checkRow == -1) {
        				System.out.println("ERROR: Row is full.");
        			}
        			else {
        				history.add(position);
        				board[checkRow][position - 1] = ((history.size() - 1) % 2 + 1);
        				//((history.size() - 1) % 2 + 1) alternates between Player 1 and Player 2 with the numbers 1 and 2 respectively
        				if (gameWon(board, checkRow, position, ((history.size() - 1) % 2 + 1))){
        					System.out.println("WIN");
        					win = true;
        				}
        				else if (boardFull(board)) {
        					System.out.println("DRAW");
        				}
        				else {
        					System.out.println("OK");
        				}
        			}
        		
        		}
        	}
	        
	        
	        else if (command[0].equals("GET")) {
	        	//prints out the list of previous "moves" or columns placed into successfully
	        	for (int i = 0; i < history.size(); i++) {
	        		System.out.println(history.get(i));
	        	}
	        }
	        else if (command[0].equals("BOARD")) {
	        	printBoard(board);
	        }
	        else {
	        	System.out.println("ERROR");
	        }
	        insert = input.nextLine();
	    }
	    input.close(); 
	    System.out.println("Thank you for playing!");
	}
	
	//this method accepts the 4x4 array used for the gameboard and prints it out to the console as required by the game description
	private static void printBoard(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			System.out.print("| ");
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.print("+");
		for (int k = 0; k < board[0].length * 2; k++) {
			System.out.print("-");
		}
		System.out.println();
		System.out.print("  ");
		for (int p = 1; p <= board.length; p++) {
			System.out.print(p + " ");
		}
		System.out.println();
		
	}
	
	//this method accepts the "gameboard" multi-dimensional array as well as the specific column of interest. It starts from the 
	//bottom of the column -if you look at it visually - or the last column-index of the array. It finds the lowest possible row
	//where a token can be placed in that column. If the column is full, it returns -1
	private static int getRow(int[][] board, int column) {
		int x = board.length - 1;
		while (x >= 0) {
			if (board[x][column - 1] == 0) {
				return x;
			}
			x--;
		}
		return -1;
	}
	
	//this game checks to see if the player who just did their move has won. It does this by calling three separate functions to 
    //see if the player has 4 of their tokens in a row horizontally, vertically, or diagonally.
	private static boolean gameWon(int[][]board, int row, int column, int playerNum) {
		return (checkVertical(board, column, playerNum) 
				|| checkHorizontal(board, row, playerNum) 
				|| checkDiagonal(board, playerNum));
	}
	
	//this method uses a for-loop to check for 4 tokens of the same player in a row in each column of the gameboard
	private static boolean checkVertical(int[][]board, int column, int playerNum) {
		for (int i = 0; i < board.length; i++) {
			if (board[i][column - 1] != playerNum) {
				return false;
			}
		}
		return true;
	}
	
	//this method also uses a for-loop to see if a player has 4 or of their tokens in a row horizontally in any one of the rows.
	//It accepts the gameboard, the player (either player1 or player2) who most recently did their move, and the row number.
	private static boolean checkHorizontal(int[][]board, int row, int playerNum) {
		for (int i = 0; i < board[row].length; i++) {
			if (board[row][i] != playerNum) {
				return false;
			}
		}
		return false;
	}
	
	//this method checks to see if a player has 4 of their tokens in a row diagonally across the board. It does this using 
	//two for-loops. One for checking along the diagonal from top-left to bottom-right and the other from top-right to 
    // botto-left.
	private static boolean checkDiagonal(int[][] board, int playerNum) {
		boolean diagonal1 = true;
		boolean diagonal2 = true;
		for (int i = 0; i < board.length; i++) {
			if (board[i][i] != playerNum) {
				diagonal1 = false;
			}
		}
		
		for (int i = 0; i < board.length; i++) {
			if (board[i][board.length - (i + 1)] != playerNum) {
				diagonal2 = false;
			}
		}
		
		return (diagonal1 || diagonal2);
	}
	
	//this method traverses the entire gameboard / multi-dimensional array to see if the entire board has been filled.
	//If it doesn't find any vacant spot, it returns true, which then causes the game to end in a draw.
	private static boolean boardFull(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	

}