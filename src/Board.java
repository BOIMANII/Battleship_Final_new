/**
 * @author Max
 * @date 2026-05-27
 * Description: The main class for which most board and game logic is located
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Board extends JFrame implements ActionListener{

	Cell[][] grid = new Cell[10][10];
	
	boolean useHorizontal = false;
	boolean useComplex;
	boolean computerFirst;
	int shipsPlaced = 0;
	HumanPlayer humanPlayer = new HumanPlayer();
	ComputerPlayer computerPlayer = new ComputerPlayer();
	int guesses = 0;
	
	
	//Gui components 
	JFrame frame;
	JButton[][] playerCellButtons;
	JButton[][] computerCells;
	JButton saveButton;
	JButton loadButton;
	JCheckBox toggleHorizontal; // Checking this box means horizontal is used
	JLabel background;
	ImageIcon backgroundImage;
	
	
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
	 * @throws FileNotFoundException 
	 */
	public Board(String playerName, boolean useComplex, boolean isLoad) throws InterruptedException, FileNotFoundException {
		/*
		 * GUI note:
		 * 
		 * Set up new battleship game window
		 * 
		 * Set up player buttons on left, clickable
		 * Player buttons are in a JButton[][]
		 * Make sure 0,0 is top left value, 10,10 is bottom right
		 * 
		 * Place in [y][x] format (just a suggestion, I have no idea how you are doing it)
		 * All other times we use [x][y] format (placing [y][x] will format so that x is actually horizontal, etc)
		 */
		backgroundImage = new ImageIcon("backgroundBoard.jpg");
		background = new JLabel(backgroundImage);
		
		
		this.add(background);
		this.setSize(2000,1000);
		this.setDefaultCloseOperation(3);
		this.setVisible(true);
		
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
			// This should pause program running until player has placed all ships
			while (!(shipsPlaced == 5)) {
				wait();
			}
			// Set AI ships
			setShips();
			
			/*
			 * GUI note:
			 * 
			 * Make the ship types labels nonvisible/get rid of them
			 * 
			 * Make toggleHorizontal nonclickable and set visibility to false
			 */
			updateGrids();
		} else {
			// TODO Read all info from files
			File humanPlayerFile = new File(playerName + "HumanPlayer.txt");
			Scanner humanScanner = new Scanner(humanPlayerFile);
			
			// Set the player name
			humanPlayer.setName(playerName);
			
			// Set up ships
			for (int i = 0; i < 5; i++) {
				String positions = "";
				positions = humanScanner.nextLine();
				String[] coordinates = positions.split(" - ");
				
				// Take the String[] of a ship's positions and create an int[][] based on the String
				int length = coordinates.length - 1;
				int[][] shipPositions = toCoords(coordinates);
				
				// Initialize ship, read hit count and evaluate if the ship is sunk
				Ship ship = new Ship(shipPositions, length);
				ship.setHitCount(Integer.parseInt(humanScanner.nextLine()));
				ship.evaluateSunk();
				
				// Add ship to Player's ships
				humanPlayer.getShips().add(ship);
			}
			
			File computerPlayerFile = new File(playerName + "ComputerPlayer.txt");
			Scanner computerScanner = new Scanner(computerPlayerFile);
			
			// Computer name is already set at initialization
			
			// Set up ships
			for (int i = 0; i < 5; i++) {
				String positions = "";
				positions = computerScanner.nextLine();
				String[] coordinates = positions.split(" - ");

				// Take the String[] of a ship's positions and create an int[][] based on the String
				int length = coordinates.length - 1;
				int[][] shipPositions = toCoords(coordinates);

				// Initialize ship, read hit count and evaluate if the ship is sunk
				Ship ship = new Ship(shipPositions, length);
				ship.setHitCount(Integer.parseInt(computerScanner.nextLine()));
				ship.evaluateSunk();

				// Add ship to Computer's ships
				computerPlayer.getShips().add(ship);
			}
			
			File boardFile = new File(playerName + "BoardGrid.txt");
			Scanner boardScanner = new Scanner(boardFile);
			
			guesses = Integer.parseInt(boardScanner.nextLine());
			useComplex = Boolean.parseBoolean(boardScanner.nextLine());
			
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					grid[i][j] = new Cell(i, j);
					
					String storageRow = boardScanner.nextLine();
					String[] conditions = storageRow.split(",");
					
					grid[i][j].setPlayerShipPresent(Boolean.parseBoolean(conditions[0]));
					grid[i][j].setComputerShipPresent(Boolean.parseBoolean(conditions[1]));
					grid[i][j].setPlayerGuessed(Boolean.parseBoolean(conditions[2]));
					grid[i][j].setComputerGuessed(Boolean.parseBoolean(conditions[3]));
				}
			}
			
			// Close scanners
			humanScanner.close();
			computerScanner.close();
			boardScanner.close();
			
			// Update grids to reflect changes
			updateGrids();
			// TODO
		}
		/*
		 * GUI note:
		 * 
		 * Set the grid for computer's board to the right, and make the buttons clickable
		 * 
		 * If not already done, set those player buttons to unclickable
		 * 
		 * Make sure 0,0 is top left value, 10,10 is bottom right
		 * 
		 * At the top of each board, run the corresponding player's getName() and display
		 * (ie Matt's ships, Computer's ships, etc)
		 * 
		 * 
		 */
		updateGrids();
		
		// Check to see if the computer "goes first" (50% chance of doing so)
		Random randgen = new Random();
		if (randgen.nextInt(0, 2) == 0) {
			computerGuess();
			// It is not possible for the computer to win first guess, hence no checkWin() is needed
		}
	}
	
	public int[][] toCoords(String[] coordinates) {
		// Take the String[] of a ship's positions and create an int[][] based on the String
		int length = coordinates.length - 1;
		int[][] shipPositions = new int[length][2];
		for (int j = 0; j < length; j++) {
			shipPositions[j][0] = Integer.parseInt(coordinates[j].substring(1, 2));
			shipPositions[j][1] = Integer.parseInt(coordinates[j].substring(3, 4));
		}
		return shipPositions;
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
		Random randgen = new Random();
		int[] selection = new int[2];
		int[][] evaluatedPositions = new int[10][10];
		boolean first = true;
		// i variable serves to record size/length of ship being placed
		for (int i = 2; i < 6; i++) {
			// Reset initial evaluated to invalid result for each ship
			evaluatedPositions = new int[10][10];
			evaluatedPositions[0][0] = -1;
			
			// Repeatedly randomly generate until valid selection is made
			while (evaluatedPositions[0][0] == -1) {
				// Randomly generate any coordinates in the grid
				selection = computerPlayer.generateShipPlacement();
				// Randomizes if the player ship will be placed horizontal or vertical
				if (randgen.nextInt(0, 2) == 0) {
					useHorizontal = !useHorizontal;
				}
				evaluatedPositions = evaluateShipPlacement(selection, i, false);
			}
			
			// evaluateShipPlacement already marks the grid cells if a valid selection is made
			// Create appropriate ship object for computer player
			Ship ship = new Ship(evaluatedPositions, i);
			computerPlayer.getShips().add(ship);
			
			// There are 2 length 3 ships in battleship, hence we must repeat i = 3 once
			if (i == 3 && first == true) {
				i--;
				first = false;
			}
		}
	}
	
	/**
	 * Evaluates the ship placement for one ship
	 * 
	 * This will first check if all the grids the ship would take up are available
	 * 
	 * If so, it will create an int[][] storing all grids the ship takes, set those cells
	 * so the appropriate player's ship is marked as present and then return
	 * 
	 * If not, it will return the invalid result
	 * 
	 * @param selection
	 * @param length
	 * @param isHuman
	 * @return
	 */
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
					thisCell = grid[thisCell.getX() + 1][thisCell.getY()];
				} else {
					thisCell = grid[thisCell.getX()][thisCell.getY() + 1];
				}
			}
		} catch (Exception e) {
			canPlace = false;
		}
		// If invalid selection is made by player, popup error message
		if (isHuman == true && canPlace == false) {
			/*
			 * GUI note:
			 * 
			 * Popup error message "Invalid placement"
			 */
		}
		
		// Depending on if the ship can be placed, generate the int[][] array and set the values of the cells
		// to reflect the placement, otherwise generate the invalid int[][] array and return
		// Go over all grids evaluated again, record them in an int[][], and set corresponding ship present boolean to true
		int[][] placeCoords = new int[length][2];
		if (canPlace == true) {
			int[] coord = {selection[0], selection[1]};
			for (int i = 0; i < length; i++) {
				// Copy cell's grid coordinates into placeCoords
				// Must be copying ints because int[]s act as pass by reference
				placeCoords[i][0] = coord[0];
				placeCoords[i][1] = coord[1];
				// Change the cell's values to reflect the placement
				if (isHuman == true) {
					grid[coord[0]][coord[1]].setPlayerShipPresent(true);
				} else {
					grid[coord[0]][coord[1]].setComputerShipPresent(true);
				}
				// Move to next valid cell
				if (useHorizontal == true) {
					coord[0]++;
				} else {
					coord[1]++;
				}
			}
		} else {
			// The invalid selection, signal no ship placement is to be made
			placeCoords[0][0] = -1;
		}
		
		return placeCoords;
	}
	
	/**
	 * This is where most of the game will be run
	 * 
	 * Button presses on different buttons will determine what will happen in game
	 * 
	 * The main buttons in board are the computer's grid (where the human player guesses),
	 * the player's grid (where the player clicks to place ships, and which cannot be clicked
	 * during the actual game) and the save button
	 * 
	 * The check box to toggle placement being horizontal or not is also present
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Check if the button clicked is one of the player's
		boolean isPlace = false;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (playerCellButtons[i][j] == e.getSource()) {
					isPlace = true;
				}
			}
		}
		
		// Based on selection, different things will happen
		// If the player pressed one of the buttons on their own cells (to place a ship)
		if (isPlace == true) {
			int[] selected = getGrid((JButton) e.getSource());
			// By how many ships are placed, the length is determined (smallest ships placed first)
			int length = shipsPlaced + 2;
			if (length == 5 || length == 6) {
				length--;
			}
			// Will return an int[][] of grid coordinates and mark on grid[][] cells if placement valid
			int[][] place = evaluateShipPlacement(selected, length, true);
			// If a ship was actually placed, increment shipsPlaced and create a ship for humanPlayer
			if (place[0][0] != -1) {
				shipsPlaced++;
				humanPlayer.getShips().add(new Ship(place, length));
				// Update grid so placement is seen
				updateGrids();
			}
		// If the button pressed is the save button
		} else if (saveButton == e.getSource()) {
			// TODO Save stuff
			try {
				// Save humanPlayer info
				PrintWriter writer = new PrintWriter(humanPlayer.getName() + "HumanPlayer.txt");
				// Save ship positions, in (#,#) - (#,#) - (#,#)... format - one row per ship for positions
				// The save the hitCount right under the same ship's positions - all other variables can be recalculated later
				// For each ship the player has...
				for (int i = 0; i < humanPlayer.getShips().size(); i++) {
					String positions = "";
					// For each position int[] the ship has, write down as a "(#,#) - "
					for (int j = 0; j < humanPlayer.getShips().get(i).getPositions().length; j++) {
						positions += "(";
						positions += humanPlayer.getShips().get(i).getPositions()[j][0];
						positions += ",";
						positions += humanPlayer.getShips().get(i).getPositions()[j][1];
						positions += ") - ";
					}
					
					// At this point we should have a list of coordinates in the above mentioned format
					// Save, then save hitCounter for the ship
					writer.println(positions);
					writer.println(humanPlayer.getShips().get(i).getHitCount());
				}
				writer.close();
				
				// Save computerPlayer info
				writer = new PrintWriter(humanPlayer.getName() + "ComputerPlayer.txt");
				// Save ship positions, as above
				for (int i = 0; i < computerPlayer.getShips().size(); i++) {
					String positions = "";
					// For each position int[] the ship has, write down as a "(#,#) - "
					for (int j = 0; j < computerPlayer.getShips().get(i).getPositions().length; j++) {
						positions += "(";
						positions += computerPlayer.getShips().get(i).getPositions()[j][0];
						positions += ",";
						positions += computerPlayer.getShips().get(i).getPositions()[j][1];
						positions += ") - ";
					}
					
					// At this point we should have a list of coordinates in the above mentioned format
					// Save, then save hitCounter for the ship
					writer.println(positions);
					writer.println(computerPlayer.getShips().get(i).getHitCount());
				}
				writer.close();
				
				writer = new PrintWriter(humanPlayer.getName() + "BoardGrid.txt");
				// First save guesses and useComplex
				writer.println(guesses);
				writer.println(useComplex);
				// Save all grid boolean values in the boolean,boolean,boolean,boolean, format in the order they are listed in the Cell class
				// Each grid should take only 1 row and each row stores the values for all 4 booleans
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						String conditions = "";
						conditions += grid[i][j].isPlayerShipPresent() + ",";
						conditions += grid[i][j].isComputerShipPresent() + ",";
						conditions += grid[i][j].isPlayerGuessed() + ",";
						conditions += grid[i][j].isComputerGuessed() + ",";
						writer.println(conditions);
					}
				}
				writer.close(); // TODO
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
		// If the check box to toggle if placement is horizontal is pressed
		// Checking the box means horizontal is used
		} else if (toggleHorizontal == e.getSource()) {
			useHorizontal = toggleHorizontal.isSelected();
		// Any other selection would be on the computer's board, which would be a guess
		} else {
			int[] guess = getGrid((JButton) e.getSource());
			
			// If player already guessed grid, so inform them this selection is invalid
			if (grid[guess[0]][guess[1]].isPlayerGuessed()) {
				/*
				 * GUI notes:
				 * 
				 * Make error popup appear saying guess is invalid (already guessed), nothing else happens
				 */
			// Player has not already guessed the grid, so increment hitCount of ship hit and check if sunk
			} else {
				// Set the grid as guessed, and increment guesses
				grid[guess[0]][guess[1]].setPlayerGuessed(true);
				guesses++;
				
				// If computer ship is hit, increment hitCount and display message if sunk
				if (grid[guess[0]][guess[1]].isComputerShipPresent()) {
					// Determine which ship is hit, increment hitCount and display message if sunk
					// (guaranteed not invalid result as we already determined computerPlayer has a
					// ship there)
					Ship ship = findShip(guess, false);
					ship.incrementHitCount();
					ship.evaluateSunk();
					// If the player's guess sinks computer's ship
					if (ship.getSunk()) {
						/*
						 * GUI notes:
						 * 
						 * Popup info thingy that will say "You have sunk computer's ___"
						 * 
						 * I'd recommend using ship.getName() and maybe an explosion image to go with the popup
						 * 
						 * If you want a list of ships each player still has, you adjust the ship count here
						 */
					}
				}
				
				// Update the grids after the guess
				updateGrids();
				checkWin();
				
				// After player's guess, computer will guess (this makes up one turn)
				computerGuess();
				checkWin();
			}
		}
	}
	
	public void computerGuess() {
		int[] guess = {-1, -1};
		
		
		
		
		
		/*
		 * TODO it's kind of stupid to have board determine if it's valid or not can't
		 * it be written into the two classes? (especially complex the logic needs to
		 * work around invalids)
		 */
		
		
		
		

		// Have the computer generate a valid guess - if not valid, it keeps guessing 
		// until a valid one is found
		boolean valid = false;
		while (valid == false) {
			// Generate a guess
			if (useComplex == true) {
				ComplexAI complex = new ComplexAI();
				guess = complex.guess();
			} else {
				SimpleAI simple = new SimpleAI();
				guess = simple.guess();
			}
			
			// If not already guessed, grid is valid
			if (grid[guess[0]][guess[1]].isComputerGuessed() == false) {
				valid = true;
			}
		}
		
		// Set the grid as guessed
		grid[guess[0]][guess[1]].setComputerGuessed(true);
		
		// If player ship is hit, increment hitCount and display message if sunk
		if (grid[guess[0]][guess[1]].isPlayerShipPresent()) {
			// Determine which ship is hit, increment hitCount and display message if sunk
			// (guaranteed not invalid result as we already determined humanPlayer has a
			// ship there)
			Ship ship = findShip(guess, true);
			ship.incrementHitCount();
			ship.evaluateSunk();
			// If the computer's guess sinks player's ship
			if (ship.getSunk()) {
				/*
				 * GUI notes:
				 * 
				 * Popup info thingy that will say "Computer has sunk your ___"
				 * 
				 * I'd recommend using ship.getName() and maybe an explosion image to go with the popup
				 * 
				 * If you want a list of ships each player still has, you adjust the ship count here
				 */
			}
		}
		
		// Update the grids after the guess
		updateGrids();
	}
	
	/**
	 * This method will check if one player has won, at which point it will display the appropriate win message
	 */
	public void checkWin() {
		// Update hasLost boolean for both players
		humanPlayer.evaluateHasLost();
		computerPlayer.evaluateHasLost();
		
		// If the player won, print victory message, add player to scoreboard and print scoreboard
		boolean isOver = false;
		if (computerPlayer.getHasLost()) {
			isOver = true;

			/*
			 * TODO
			 * 
			 * Scoreboard fileio stuff here:
			 * 
			 * Get arraylist of fileio stuff, add player's score and name to it, sort, save new list and print
			 */
			
			/*
			 * GUI notes:
			 * 
			 * Popup victory message (include scoreboard)
			 */
		// If player lost, print loss message and do nothing else
		} else if (humanPlayer.getHasLost()) {
			isOver = true;
			
			/*
			 * GUI notes:
			 * 
			 * Popup defeat message
			 */
		}
		
		if (isOver == true) {
			/*
			 * GUI notes:
			 * 
			 * Set all buttons as nonclickable and maybe if you want after popup closed close window
			 * 
			 * Whatever you think would be best I think either is fine (or maybe a restart button that'll close this one after initializing another board, thereby playing again?)
			 */
		}
	}
	
	/**
	 * This method finds the ship that occupies a certain cell
	 * 
	 * Specify which player's board you are looking on, with isHuman referring
	 * to which board you are looking on (ie true looks on player's board, etc)
	 * 
	 * There is no need to account for already sunk ships because previously sunk ships
	 * have had their cells marked as guessed, and cannot be guessed again
	 * 
	 * @param cell
	 * @return
	 */
	public Ship findShip(int[] cell, boolean isHuman) {
		Ship ship;
		if (isHuman == true) {
			// For each ship humanPlayer has...
			for (int i = 0; i < humanPlayer.getShips().size(); i++) {
				// For each cell a ship has...
				for (int j = 0; j < humanPlayer.getShips().get(i).getPositions().length; j++) {
					// If the cell the ship has matches with the cell hit, the ship is the one we are looking for
					if (humanPlayer.getShips().get(i).getPositions()[j][0] == cell[0] && humanPlayer.getShips().get(i).getPositions()[j][1] == cell[1]) {
						ship = humanPlayer.getShips().get(i);
						return ship;
					}
				}
			}
		} else {
			// For each ship computerPlayer has...
			for (int i = 0; i < computerPlayer.getShips().size(); i++) {
				// For each cell a ship has...
				for (int j = 0; j < computerPlayer.getShips().get(i).getPositions().length; j++) {
					// If the cell the ship has matches with the cell hit, the ship is the one we are looking for
					if (computerPlayer.getShips().get(i).getPositions()[j][0] == cell[0] && computerPlayer.getShips().get(i).getPositions()[j][1] == cell[1]) {
						ship = computerPlayer.getShips().get(i);
						return ship;
					}
				}
			}
		}
		
		// If no ship found, return invalid case ship
		int[][] pos = {{0, 0}, {0, 0}};
		ship = new Ship(pos, -1);
		return ship;
	}
	
	/**
	 * This method finds the x and y values (coordinates) of a button pressed
	 * 
	 * This method does so by comparing the button passed in to each button in
	 * the 2 grids and returning the [i,j] values if the button is located
	 * 
	 * This method only works if the button is a part of one of the grids
	 * 
	 * @param button
	 * @return
	 */
	public int[] getGrid(JButton button) {
		int[] location = {-1, -1}; // Default is invalid return
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (button == playerCellButtons[i][j] || button == computerCells[i][j]) {
					location[0] = i;
					location[1] = j;
				}
			}
		}
		return location;
	}
	
	/**
	 * 
	 */
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