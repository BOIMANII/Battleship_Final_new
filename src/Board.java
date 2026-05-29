

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Board extends JFrame implements ActionListener{

	Cell[][] grid = new Cell[10][10];
	JFrame frame;
	JButton[][] playerCellButtons;
	JButton[][] computerCells;
	JButton saveButton;
	JButton loadButton;
	JCheckBox toggleHorizontal;
	boolean useHorizontal;
	boolean useComplex;
	boolean computerFirst;
	int shipsPlaced = 0;
	HumanPlayer humanPlayer = new HumanPlayer();
	ComputerPlayer computerPlayer = new ComputerPlayer();
	
	/**
	 * Constructor for board
	 * This will set up humanPlayer's name, set whether or not
	 * the game is played in hard mode and initialize all cells in grid
	 * 
	 * It will then call call setShips to conduct the computer ship placement 
	 * process, and wait as player places ships
	 * 
	 * setShips will remove from the board any elements which will no longer be useful
	 * (assuming the player sets the ships - the GUI steps are skipped if setShips
	 * is setting AI ships), leaving the frame ready for the actual game
	 * 
	 * Board will then display game relevant elements, such as the computer's
	 * grid
	 * 
	 * @param playerName
	 * @param useComplex
	 */
	public Board(String playerName, boolean useComplex, boolean isLoad) throws InterruptedException {
		/*
		 * GUI note:
		 * 
		 * Set up new battleship game window
		 * 
		 * Set up player buttons on left, clickable
		 */
		if (isLoad == false) {
			humanPlayer.setName(playerName);
			this.useComplex = useComplex;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					grid[i][j] = new Cell(i, j);
				}
			}
			
			/*
			 * GUI note:
			 * Add ship types to right side of the board, add toggleHorizontal button
			 * near that somewhere
			 * 
			 * Set the text of toggleHorizontal (now just an empty checkbox) to say "toggle horizontal"
			 * 
			 * Whether or not it is checked effects the boolean value of useHorizontal
			 */
			// Set AI ships
			setShips();
			// This should pause program running until player has placed all ships
			while (!(shipsPlaced == 5)) {
				wait();
			}
			
			/*
			 * GUI note:
			 * 
			 * Set the buttons of the player's board to not be able to be clicked
			 * 
			 * Make the ship types labels nonvisible/get rid of them
			 * 
			 * Make toggleHorizontal nonclickable and set visibility to false
			 * 
			 */
		} else {
			// TODO Read all info from files
		}
		/*
		 * GUI note:
		 * 
		 * Set the grid for computer's board to the right, and make the buttons clickable
		 * 
		 * At the top of each board, run the corresponding player's getName() and display
		 * (ie Matt's ships, Computer's ships, etc)
		 * 
		 * 
		 */
	}
	
	/**
	 * Will set the ship selection for computer player
	 * 
	 * Generates a selection, then runs evaluateShipPlacement() to determine if the ship
	 * placement is valid or not
	 * If valid, initializes a ship
	 * If not valid, will generate another selection and evaluate again
	 * 
	 * Repeat until all 5 ships are placed
	 * 
	 * Occurs in a for loop to track ship placement 
	 */
	public void setShips() {
		
	}
	
	public int[][] evaluateShipPlacement(int[] selection, int length, boolean isHuman) {
		boolean canPlace = true;
		Cell thisCell = grid[selection[0]][selection[1]];
		
		// Evaluates whether or not the squares are valid
		// Try catch for out of bounds errors (selection is at the edge of the grid)
		try {
			for (int i = 0; i < length; i++) {
				// If one of the cells the ship is supposed to be on already has a placement, placement invalid
				boolean isPresent;
				if (isHuman == true) {
					isPresent = thisCell.isPlayerShipPresent();
				} else {
					isPresent = thisCell.isComputerShipPresent();
				}
				if (isPresent == true) {
					canPlace = false;
					break;
				}
				// Move onto the next cell the ship is supposed to be
				if (useHorizontal == true) {
					thisCell = grid[thisCell.getX()][thisCell.getY() + 1];
				} else {
					thisCell = grid[thisCell.getX() + 1][thisCell.getY()];
				}
			}
		} catch (Exception e) {
			canPlace = false;
		}
		// If invalid selection is made by player, popup error message
		if (isHuman == true) {
			/*
			 * GUI note:
			 * 
			 * Popup error message "Invalid placement"
			 */
		}
		
		// Depending on if the ship can be placed, generate the int[][] array and set the values of the cells
		// to reflect the placement, otherwise generate the invalid int[][] array and return
		int[][] placeCoords = new int[length][2];
		if (canPlace == true) {
			
		} else {
			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateGrids() {
		/*
		 * GUI note:
		 * 
		 * This method updates the color of the 2 grids to correctly reflect what is going on
		 * 
		 * ie orange or something for a left side player ship not hit yet, red for hit grids
		 * (both sides), blue for places not guessed yet and green for misses (both sides)
		 * 
		 * Cell has a bunch of boolean values for this very reason
		 */
	}

}
