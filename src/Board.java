
/**
 * @author Max
 * @author Anthony
 * @date 2026-05-27
 * Description: The main class for which most board and game logic is located
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Board extends JFrame implements ActionListener {

		// Game Logic
		Cell[][] grid = new Cell[10][10];
		HumanPlayer humanPlayer = new HumanPlayer();
		ComputerPlayer computerPlayer = new ComputerPlayer();
		String playerName;
		int shipsPlaced = 0;
		int guesses = 0;
		int hit;
		int miss;
		int sunk;
		boolean useHorizontal = false;
		boolean useComplex;
		boolean computerFirst;

		// Frame and Background
		JFrame frame;
		JLabel background;
		ImageIcon backgroundImage;

		// Layout Containers West (Player Board & Info)
		JPanel westPanel;
		JPanel westGrid;
		JPanel westWestPanel;
		JPanel northWestPanel;
		JPanel namePanel;
		JPanel westBottomPanel;
		JPanel westNumbersGrid;
		JPanel westSpacer;
		JPanel statsPanel;

		// Layout Containers East (Enemy Board, Ships, & Controls)
		JPanel eastPanel;
		JPanel eastGrid;
		JPanel eastEastGrid;
		JPanel westEastGrid;
		JPanel eastEastPanel;
		JPanel northEastPanel;
		JPanel southEastPanel;
		JPanel aiNamePanel;
		JPanel eastNumbersGrid;
		JPanel eastSpacer;
		JPanel toggelPanel;

		// Button Component
		JButton[][] playerCellButtons;
		JButton[][] computerCells;
		JButton saveButton;
		JButton loadButton;
		JCheckBox toggleHorizontal;

		// Name Plate and Stats Dispaly
		JLabel nameLabel;
		JLabel computerNamePlate;
		JLabel hitLabel;
		JLabel missLabel;
		JLabel sunkLabel;

		// FleetDisplays
		JLabel textVenator;
		JLabel textAcclamator;
		JLabel textArquitens;
		JLabel textInterceptor;

		// Ship Assets and Icons
		JLabel[] shipPlaceImage;
		ImageIcon iconShipBoom;
		ImageIcon iconVenator;
		ImageIcon iconAcclamator;
		ImageIcon iconArquitens;
		ImageIcon iconInterceptor;
		
		// Cell-specific 80x80 variants for grid placement
		ImageIcon cellVenator;
		ImageIcon cellAcclamator;
		ImageIcon cellArquitens;
		ImageIcon cellInterceptor;

		// User Prompts and Alerts
		JOptionPane invalidPlacment;

	/**
	 * Constructor for board This will set up humanPlayer's name, set whether or not
	 * the game is played in hard mode and initialize all cells in grid
	 * 
	 * It will then call call setShips to conduct the computer ship placement
	 * process, and wait as player places ships
	 * 
	 * setShips will remove from the board any elements which will no longer be
	 * useful (assuming the player sets the ships - the GUI steps are skipped if
	 * setShips is setting AI ships), leaving the frame ready for the actual game
	 * 
	 * Board will then display game relevant elements, such as the computer's grid
	 * 
	 * @param playerName
	 * @param useComplex
	 * @throws FileNotFoundException
	 */
	public Board(String playerName, boolean useComplex, boolean isLoad)
			throws InterruptedException {
		
		// Game Logic
				if (playerName.equalsIgnoreCase("Enter Name")) {
					this.playerName = "The Republic";
				} else {
					this.playerName = "Commander " + playerName;
				}

				// Frame and Background
				backgroundImage = new ImageIcon("backgroundBoard.jpg");
				background = new JLabel(backgroundImage);

				// Layout Containers West (Player Board & Info)
				westPanel = new JPanel();
				westGrid = new JPanel();
				westWestPanel = new JPanel();
				northWestPanel = new JPanel();
				namePanel = new JPanel();
				westBottomPanel = new JPanel(new BorderLayout());
				westSpacer = new JPanel();
				westNumbersGrid = new JPanel(new GridLayout(1, 10));
				statsPanel = new JPanel();

				// Layout Containers East (Enemy Board, Ships, & Controls)
				eastPanel = new JPanel();
				eastGrid = new JPanel();
				eastEastGrid = new JPanel();
				westEastGrid = new JPanel();
				eastEastPanel = new JPanel();
				northEastPanel = new JPanel();
				southEastPanel = new JPanel();
				aiNamePanel = new JPanel();
				eastNumbersGrid = new JPanel();
				eastSpacer = new JPanel();
				toggelPanel = new JPanel();

				// Button Component
				toggleHorizontal = new JCheckBox();
				saveButton = new JButton();
				loadButton = new JButton(); 

				playerCellButtons = new JButton[10][10];
				computerCells = new JButton[10][10];

				for (int y = 0; y < 10; y++) {
					for (int x = 0; x < 10; x++) {
						playerCellButtons[y][x] = new JButton();
						playerCellButtons[y][x].addActionListener(this);
						playerCellButtons[y][x].setPreferredSize(new Dimension(80, 80));
						playerCellButtons[y][x].putClientProperty("row", y);
						playerCellButtons[y][x].putClientProperty("col", x);

						computerCells[y][x] = new JButton();
						computerCells[y][x].addActionListener(this);
						computerCells[y][x].setPreferredSize(new Dimension(80, 80));
						computerCells[y][x].putClientProperty("row", y);
						computerCells[y][x].putClientProperty("col", x);
					}
				}

				// Name Plate and Stats Dispaly
				nameLabel = new JLabel();
				computerNamePlate = new JLabel();
				hitLabel = new JLabel();
				missLabel = new JLabel();
				sunkLabel = new JLabel();

				// FleetDisplays
				textVenator = new JLabel();
				textAcclamator = new JLabel();
				textArquitens = new JLabel();
				textInterceptor = new JLabel();

				// Ship Assets and Icons
				iconShipBoom = new ImageIcon("iconShipBoom.png");
				iconVenator = scaleImage("iconVenator.png", 250, 100);
				iconAcclamator = scaleImage("iconAcclamator.png", 250, 100);
				iconArquitens = scaleImage("iconArquitens.png", 250, 100);
				iconInterceptor = scaleImage("iconInterceptor.png", 250, 100);

				shipPlaceImage = new JLabel[4];
				shipPlaceImage[0] = new JLabel(iconVenator);
				shipPlaceImage[1] = new JLabel(iconAcclamator);
				shipPlaceImage[2] = new JLabel(iconArquitens);
				shipPlaceImage[3] = new JLabel(iconInterceptor);
				
				// Ship Assets and Icons
				iconShipBoom = new ImageIcon("iconShipBoom.png");
				iconVenator = scaleImage("iconVenator.png", 250, 100);
				iconAcclamator = scaleImage("iconAcclamator.png", 250, 100);
				iconArquitens = scaleImage("iconArquitens.png", 250, 100);
				iconInterceptor = scaleImage("iconInterceptor.png", 250, 100);
				cellVenator = scaleImage("iconVenator.png", 80, 80);
				cellAcclamator = scaleImage("iconAcclamator.png", 80, 80);
				cellArquitens = scaleImage("iconArquitens.png", 80, 80);
				cellInterceptor = scaleImage("iconInterceptor.png", 80, 80);
				
				// User Prompts and Alerts
				invalidPlacment = new JOptionPane();

		// Board Logic
		if (isLoad == false) {
			this.useComplex = useComplex;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					grid[i][j] = new Cell(i, j);
				}
			}
			
			guiSetup();

		} else { // TODO
			try {
				File humanPlayerFile = new File(this.playerName + "HumanPlayer.txt");
				Scanner humanScanner = new Scanner(humanPlayerFile);
				
				for (int i = 0; i < 5; i++) {
					String positions = "";
					positions = humanScanner.nextLine();
					String[] coordinates = positions.split(" - ");

					int length = coordinates.length - 1;
					int[][] shipPositions = toCoords(coordinates);

					Ship ship = new Ship(shipPositions, length);
					ship.setHitCount(Integer.parseInt(humanScanner.nextLine()));
					ship.evaluateSunk();

					humanPlayer.getShips().add(ship);
				}

				File computerPlayerFile = new File(this.playerName + "ComputerPlayer.txt");
				Scanner computerScanner = new Scanner(computerPlayerFile);

				for (int i = 0; i < 5; i++) {
					String positions = "";
					positions = computerScanner.nextLine();
					String[] coordinates = positions.split(" - ");

					int length = coordinates.length - 1;
					int[][] shipPositions = toCoords(coordinates);

					Ship ship = new Ship(shipPositions, length);
					ship.setHitCount(Integer.parseInt(computerScanner.nextLine()));
					ship.evaluateSunk();

					computerPlayer.getShips().add(ship);
				}

				File boardFile = new File(this.playerName + "BoardGrid.txt");
				Scanner boardScanner = new Scanner(boardFile);

				guesses = Integer.parseInt(boardScanner.nextLine());
				useComplex = Boolean.parseBoolean(boardScanner.nextLine());
				
				hit = Integer.parseInt(boardScanner.nextLine());
				miss = Integer.parseInt(boardScanner.nextLine());
				sunk = Integer.parseInt(boardScanner.nextLine());

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

				humanScanner.close();
				computerScanner.close();
				boardScanner.close();

				shipsPlaced = 5;
				guiSetup();
				guiChange();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"ERROR NO SAVE FILE EXISTS FOR USER " + playerName + ", OPENING NEW EASY MODE GAME INSTEAD",
						"NO SAVE FOUND", JOptionPane.ERROR_MESSAGE);
				@SuppressWarnings("unused")
				Board newBoard = new Board(playerName, false, false);
				// This causes an error message to pop up in console but the error does not
				// effect anything
				frame.dispose();
			}
		}
		
		humanPlayer.setName(playerName);
		
	}

	

	public int[][] toCoords(String[] coordinates) {
		// Take the String[] of a ship's positions and create an int[][] based on the
		// String
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
	 * Generates a selection, then runs evaluateShipPlacement() to determine if the
	 * ship placement is valid or not If valid, initializes a ship If not valid,
	 * will generate another selection and evaluate again
	 * 
	 * Repeat until all 5 ships are placed
	 * 
	 * Occurs in a for loop to track ship placement
	 */
	public void setShips() {
		Random randgen = new Random();
		int[] selection = new int[2];
		int[][] evaluatedPositions = new int[10][2];
		boolean first = true;
		// i variable serves to record size/length of ship being placed
		for (int i = 2; i < 6; i++) {
			// Reset initial evaluated to invalid result for each ship
			evaluatedPositions = new int[10][2];
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

			// evaluateShipPlacement already marks the grid cells if a valid selection is
			// made
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
	 * If so, it will create an int[][] storing all grids the ship takes, set those
	 * cells so the appropriate player's ship is marked as present and then return
	 * 
	 * If not, it will return the invalid result
	 * 
	 * @param selection
	 * @param length
	 * @param isHuman
	 * @return
	 */
	public int[][] evaluateShipPlacement(int[] selection, int length, boolean isHuman) { // TODO
		boolean canPlace = true;
		int x = selection[0];
		int y = selection[1];

		// Evaluates whether or not the squares are valid
		// Try catch for out of bounds errors (selection is at the edge of the grid)
		try {
			for (int i = 0; i < length; i++) {
				Cell thisCell = grid[x][y];
				// If one of the cells the ship is supposed to be on already has a placement,
				// placement invalid
				boolean isPresent = false;
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
					x++;
				} else {
					y++;
				}
			}
			// If it goes off the grid
		} catch (Exception e) {
			canPlace = false;
		}
		// If invalid selection is made by player, popup error message
		if (isHuman == true && canPlace == false) {

			JOptionPane.showMessageDialog(null, "Sir our ship can not be here", "INVALID", JOptionPane.WARNING_MESSAGE);
			/*
			 * GUI note:
			 * 
			 * Popup error message "Invalid placement"
			 */
		}

		// Depending on if the ship can be placed, generate the int[][] array and set
		// the values of the cells
		// to reflect the placement, otherwise generate the invalid int[][] array and
		// return
		// Go over all grids evaluated again, record them in an int[][], and set
		// corresponding ship present boolean to true
		int[][] placeCoords = new int[length][2];
		if (canPlace == true) {
			for (int i = 0; i < length; i++) {
				// Copy cell's grid coordinates into placeCoords
				// Must be copying ints because int[]s act as pass by reference
				placeCoords[i][0] = selection[0];
				placeCoords[i][1] = selection[1];
				// Change the cell's values to reflect the placement
				if (isHuman == true) {
					grid[selection[0]][selection[1]].setPlayerShipPresent(true);
				} else {
					grid[selection[0]][selection[1]].setComputerShipPresent(true);
				}
				// Move to next valid cell
				if (useHorizontal == true) {
					selection[0] += 1;
				} else {
					selection[1] += 1;
				}
			}
		} else {
			// The invalid selection, signal no ship placement is to be made
			placeCoords[0][0] = -1;
		}
		System.out.println("Thingy: " + placeCoords[0][0]); // TODO
		return placeCoords;
	}

	/**
	 * This is where most of the game will be run
	 * 
	 * Button presses on different buttons will determine what will happen in game
	 * 
	 * The main buttons in board are the computer's grid (where the human player
	 * guesses), the player's grid (where the player clicks to place ships, and
	 * which cannot be clicked during the actual game) and the save button
	 * 
	 * The check box to toggle placement being horizontal or not is also present
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean isPlace = false;

		// Check if the button clicked is one of the player's grid
		if (e.getSource() instanceof JButton) {// AI couldent figure it out
			JButton clickedButton = (JButton) e.getSource();

			if (clickedButton.getClientProperty("row") != null) {

				int row = (int) clickedButton.getClientProperty("row");
				int col = (int) clickedButton.getClientProperty("col");

				System.out.println("Y: " + row + " X: " + col);
				System.out.println("Grid button pressed");

			}
		}

		// Check if the button pressed is in the player's board
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (playerCellButtons[i][j] == e.getSource()) {
					isPlace = true;
					break;
				}
			}
			if (isPlace == true) {
				break;
			}
		}
		
		// Based on selection, different things will happen
		// If the player pressed one of the buttons on their own cells (to place a ship)
		if (isPlace == true) {
			int[] selected = getGrid((JButton) e.getSource());
			// By how many ships are placed, the length is determined (smallest ships placed
			// first)
			int length = shipsPlaced + 2;
			if (length >= 4) {
				length--;
			}
			// Will return an int[][] of grid coordinates and mark on grid[][] cells if
			// placement valid
			int[][] place = evaluateShipPlacement(selected, length, true);
			// If a ship was actually placed, increment shipsPlaced and create a ship for
			// humanPlayer
			if (place[0][0] != -1) {
				shipsPlaced++;
				humanPlayer.getShips().add(new Ship(place, length));
				// Update grid so placement is seen
				updateGrids();
				if (shipsPlaced == 5) {
					System.out.println("All player ships placed! Setting up computer board...");

					// 1. Let the AI place its ships
					setShips();

					// 2. Clear out placement components / update GUI states
					guiChange();
					// (e.g., make toggleHorizontal invisible, enable computer grid buttons)
					toggleHorizontal.setVisible(false);

					// 3. Refresh grids to show the game layout
					updateGrids();

					// 50% chance computer goes first
					Random randgen = new Random();
					if (randgen.nextInt(0, 2) == 0) {
						computerGuess();
					}
				}
			}
			// If the button pressed is the save button
		} else if (saveButton == e.getSource()) {
			// TODO Save stuff
			try {
				// Save humanPlayer info
				PrintWriter writer = new PrintWriter(playerName + "HumanPlayer.txt");
				// Save ship positions, in (#,#) - (#,#) - (#,#)... format - one row per ship
				// for positions
				// The save the hitCount right under the same ship's positions - all other
				// variables can be recalculated later
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

					// At this point we should have a list of coordinates in the above mentioned
					// format
					// Save, then save hitCounter for the ship
					writer.println(positions);
					writer.println(humanPlayer.getShips().get(i).getHitCount());
				}
				writer.close();

				// Save computerPlayer info
				writer = new PrintWriter(playerName + "ComputerPlayer.txt");
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

					// At this point we should have a list of coordinates in the above mentioned
					// format
					// Save, then save hitCounter for the ship
					writer.println(positions);
					writer.println(computerPlayer.getShips().get(i).getHitCount());
				}
				writer.close();

				writer = new PrintWriter(playerName + "BoardGrid.txt");
				// First save guesses and useComplex
				writer.println(guesses);
				writer.println(useComplex);
				// Save hits, misses and sunk
				writer.println(hit);
				writer.println(miss);
				writer.println(sunk);
				// Save all grid boolean values in the boolean,boolean,boolean,boolean, format
				// in the order they are listed in the Cell class
				// Each grid should take only 1 row and each row stores the values for all 4
				// booleans
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
				JOptionPane.showMessageDialog(null, "Sir we already checked this area", "INVALID", JOptionPane.WARNING_MESSAGE);
				// Player has not already guessed the grid, so increment hitCount of ship hit
				// and check if sunk
			} else {
				// Set the grid as guessed, and increment guesses
				grid[guess[0]][guess[1]].setPlayerGuessed(true);
				guesses++;
				// If computer ship is hit, increment hitCount and display message if sunk
				if (grid[guess[0]][guess[1]].isComputerShipPresent()) {
					// Determine which ship is hit, increment hitCount and display message if sunk
					// (guaranteed not invalid result as we already determined computerPlayer has a
					// ship there)
					hit++;
					Ship ship = findShip(guess, false);
					
					
					ship.incrementHitCount();
					ship.evaluateSunk();
					// If the player's guess sinks computer's ship
					if (ship.getSunk()) {
						sunk++;
						
						String internalName = ship.getName().toUpperCase();
						String displayedName = internalName; // Default fallback if no match is found
					
						System.out.println(ship.getName());
						System.out.println(internalName);
						if (internalName.equals("VENATOR")) { 
							displayedName = "THE MALEVOLENCE";
						} else if (internalName.equals("ACCLIMATOR")) {
							displayedName = "PROVIDENCE-CLASS DREADNOUGHT";
						} else if (internalName.equals("ARQUINTIS")) {
							displayedName = "RECUSANT-CLASS LIGHT DESTROYER";
						} else if (internalName.equals("INTERCEPTOR")) {
							displayedName = "HYENA-CLASS BOMBER";
						}

						JOptionPane.showMessageDialog(null, "YOU DESTROYED A " + displayedName, "CONGRATULATIONS",
								JOptionPane.INFORMATION_MESSAGE, iconShipBoom);
					}
				} else {
					miss++;

				}

				// Update the grids after the guess, check to see if player won
				updateGrids();
				checkWin();

				// After player's guess, computer will guess (this makes up one turn)
				computerGuess();
				
			}
		}
	}

	public void computerGuess() {
		int[] guess = { -1, -1 };

		/*
		 * TODO it's kind of stupid to have board determine if it's valid or not can't
		 * it be written into the two classes? (especially complex the logic needs to
		 * work around invalids)
		 */

		// Have the computer generate a valid guess - if not valid, it keeps guessing
		// until a valid one is found
		ComplexAI complex = new ComplexAI();
		boolean valid = false;
		while (valid == false) {
			// Generate a guess
			if (useComplex == true) {
				
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
				
				String enemyInternalName = ship.getName().toUpperCase();
				String enemyDisplayedName = enemyInternalName;
				if(useComplex) {
					complex.sunkList.add(enemyInternalName);
				}
				if (enemyInternalName.equals("VENATOR")) {
					enemyDisplayedName = "VENATOR-CLASS STAR DESTROYER";
				} else if (enemyInternalName.equals("ACCLIMATOR")) { 
					enemyDisplayedName = "ACCLAMATOR-CLASS ASSAULT SHIP"; // Fixed spelling
				} else if (enemyInternalName.equals("ARQUINTIS")) { 
					enemyDisplayedName = "ARQUITENS-CLASS COMMAND CRUISER"; // Fixed spelling
				} else if (enemyInternalName.equals("INTERCEPTOR")) {
					enemyDisplayedName = "INTERCEPTOR-CLASS CORVETTE";
				}

				JOptionPane.showMessageDialog(null, "THE ENEMY DESTROYED A " + enemyDisplayedName, "OH NO",
					JOptionPane.INFORMATION_MESSAGE, iconShipBoom);
			}
		}

		// Update the grids after the guess
		updateGrids();
	}

	/**
	 * This method will check if one player has won, at which point it will display
	 * the appropriate win message
	 */
	public void checkWin() {
		// Update hasLost boolean for both players
		humanPlayer.evaluateHasLost();
		computerPlayer.evaluateHasLost();

		// If the player won, print victory message, add player to scoreboard and print
		// scoreboard
		boolean isOver = false;
		if (computerPlayer.getHasLost()) {
			isOver = true;
			
			// Print victory message
			JOptionPane.showMessageDialog(null, "GOOD JOB COMMANDER, WE DESTORYED ALL THE SEPARATIST SHIPS! THE SYSTEM IS SAVED!", "VICTORY",
					JOptionPane.INFORMATION_MESSAGE);

			/*
			 * TODO
			 * 
			 * Scoreboard fileio stuff here:
			 * 
			 * Get arraylist of fileio stuff, add player's score and name to it, sort, save
			 * new list and print
			 */
			/*
			 * Scoreboard displays by 3 columns of a bunch of rows (each row is 1 score saved)
			 * 
			 * Name  |  guesses
			 * 
			 * Scoreboard is ranked based on number of guesses (less goes higher on the board) - if
			 * there are two of the same number of guesses, the more recent one is placed below the
			 * less recent one
			 * 
			 * Printed in a score - name format:
			 * # - Name
			 * ## - Name 2
			 * ### - Name 3
			 */
			// If scoreboard already exists, read scoreboard file info, add player, sort, display and update
			String playerScore = (hit + miss) + " - " + playerName;
			try {
				File scoreboardFile = new File("scoreBoard.txt");
				Scanner scoreScanner = new Scanner(scoreboardFile);
				ArrayList<String> scores = new ArrayList<>();
				
				
				// Read all scoreboard values - they should be sorted already
				while (scoreScanner.hasNextLine()) {
					String line = scoreScanner.nextLine();
					scores.add(line);
				}
				scoreScanner.close();
				
				// Add player's score
				scores.add(playerScore);
				
				// Sort ArrayList appropriately, in ascending order (lower number of guesses at front)
				// Since ArrayList is already sorted, we just need to grab the back most value
				// and keep moving it forwards until we find where it is supposed to go
				String temp = scores.get(scores.size());
				for (int i = scores.size() - 1; i > -1; i--) {
					if (Integer.parseInt(scores.get(i).split(" - ")[0]) > Integer.parseInt(temp.split(" - ")[0])) {
						scores.set(i + 1, scores.get(i));
					} else {
						scores.set(i + 1, temp);
					}
				}
				
				/*
				 * GUI note:
				 * 
				 * Make a scorebord JFrame or something (I think if you don't set default close condition
				 * as exit on close it won't kill the program when just the scoreboard is closed
				 * 
				 * You use scores the ArrayList - it's all sorted with least # of guesses being at the front
				 * Elements are already strings in the # - name format
				 * ie "67 - Monkey"
				 * 
				 * Remember to add the title and the column headers
				 * ie number of guesses - name
				 */
				
				// Save new scores into scoreboard.txt
				PrintWriter writer = new PrintWriter("scoreboard.txt");
				for (int i = 0; i < scores.size(); i++) {
					writer.println(scores.get(i));
				}
				writer.close();
				
			// If scoreboard doesn't exist, display player and make new scoreboard file, add player
			} catch (Exception e) {
				/*
				 * GUI note:
				 * 
				 * Make a scorebord JFrame or something (I think if you don't set default close condition
				 * as exit on close it won't kill the program when just the scoreboard is closed)
				 * 
				 * You use scores the ArrayList - it's all sorted with least # of guesses being at the front
				 * Elements are already strings in the # - name format
				 * ie "67 - Monkey"
				 * 
				 * Remember to add the title and the column headers
				 * ie number of guesses - name
				 * 
				 * Just with playerScore this time because everything 
				 */
				
				// This try catch is just so that eclipse does not flag the PrintWriter initialization
				try {
					// Save the player's score in the scoreboard file
					PrintWriter writer = new PrintWriter("scoreboard.txt");
					writer.println(playerScore);
					writer.close();
				} catch (Exception e1) {
					/*
					 * At the ICS4U level where we don't expect to deal with restricted folders, invalid
					 * paths, etc this catch isn't going to catch anyways
					 * 
					 * As stated above this try catch is here to prevent eclipse from flagging the
					 * initialization as a source of FileNotFoundException
					 * 
					 * Realistically either case a: file exists and case b: file does not exist both 
					 * allow the PrintWriter to function properly (essentially it either overwrites the
					 * file completely or, if it can't find the file with the same name, makes a new one
					 * to the exact same effect)
					 */
				}
			}

		// If player lost, print loss message and do nothing else
		} else if (humanPlayer.getHasLost()) {
			isOver = true;
			
			JOptionPane.showMessageDialog(null, "SIR WE MUST RETREAT THE SEPARATISTS HAVE WON THIS SYSTEM", "DEFEAT",
					JOptionPane.INFORMATION_MESSAGE);
		}
		
		if (isOver == true) {
			/*
			 * GUI notes:
			 * 
			 * Set all buttons as nonclickable and maybe if you want after popup closed
			 * close window
			 * 
			 * Whatever you think would be best I think either is fine (or maybe a restart
			 * button that'll close this one after initializing another board, thereby
			 * playing again?)
			 */
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					eastGrid.add(computerCells[i][j]);
					playerCellButtons[i][j].setEnabled(false);
				}
			}
			new EndScreen();
		}
	}

	/**
	 * This method finds the ship that occupies a certain cell
	 * 
	 * Specify which player's board you are looking on, with isHuman referring to
	 * which board you are looking on (ie true looks on player's board, etc)
	 * 
	 * There is no need to account for already sunk ships because previously sunk
	 * ships have had their cells marked as guessed, and cannot be guessed again
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
					// If the cell the ship has matches with the cell hit, the ship is the one we
					// are looking for
					if (humanPlayer.getShips().get(i).getPositions()[j][0] == cell[0]
							&& humanPlayer.getShips().get(i).getPositions()[j][1] == cell[1]) {
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
					// If the cell the ship has matches with the cell hit, the ship is the one we
					// are looking for
					if (computerPlayer.getShips().get(i).getPositions()[j][0] == cell[0]
							&& computerPlayer.getShips().get(i).getPositions()[j][1] == cell[1]) {
						ship = computerPlayer.getShips().get(i);
						return ship;
					}
				}
			}
		}

		// If no ship found, return invalid case ship
		int[][] pos = { { 0, 0 }, { 0, 0 } };
		ship = new Ship(pos, -1);
		return ship;
	}

	/**
	 * This method finds the x and y values (coordinates) of a button pressed
	 * 
	 * This method does so by comparing the button passed in to each button in the 2
	 * grids and returning the [i,j] values if the button is located
	 * 
	 * This method only works if the button is a part of one of the grids
	 * 
	 * @param button
	 * @return
	 */
	public int[] getGrid(JButton button) {
		int[] location = { -1, -1 }; // Default is invalid return
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (button == playerCellButtons[i][j] || button == computerCells[i][j]) {
					location[0] = j;
					location[1] = i;
				}
			}
		}
		return location;
	}

	/**
	 * 
	 */
	public void updateGrids() {
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				computerCells[y][x].setBackground(Color.green);
				
				if (grid[x][y].isPlayerShipPresent() == false) {
					playerCellButtons[y][x].setBackground(Color.green);
					playerCellButtons[y][x].setIcon(null); 
				}
				
				if (grid[x][y].isPlayerShipPresent() == true) {
					playerCellButtons[y][x].setBackground(Color.orange);
					
					Ship ship = findShip(new int[]{x, y}, true);
					
					if (ship != null) {
						String name = ship.getName();
						
						if (name.equalsIgnoreCase("Venator")) {
							playerCellButtons[y][x].setIcon(cellVenator);
							playerCellButtons[y][x].setDisabledIcon(cellVenator);
						} else if (name.equalsIgnoreCase("Acclimator")) {
							playerCellButtons[y][x].setIcon(cellAcclamator);
							playerCellButtons[y][x].setDisabledIcon(cellAcclamator); 
						} else if (name.equalsIgnoreCase("Arquintis")) {
							playerCellButtons[y][x].setIcon(cellArquitens);
							playerCellButtons[y][x].setDisabledIcon(cellArquitens);  
						} else if (name.equalsIgnoreCase("Interceptor")) {
							playerCellButtons[y][x].setIcon(cellInterceptor);
							playerCellButtons[y][x].setDisabledIcon(cellInterceptor); 
						}
					}
				}
				
				if (grid[x][y].isPlayerGuessed() == true) {
					computerCells[y][x].setBackground(Color.black);
				}
				if (grid[x][y].isComputerGuessed() == true) {
					playerCellButtons[y][x].setBackground(Color.black);
				}
				
				if (grid[x][y].isComputerShipPresent() == true && grid[x][y].isPlayerGuessed() == true) {
					computerCells[y][x].setBackground(Color.red);
				}
				
				if (grid[x][y].isPlayerShipPresent() == true && grid[x][y].isComputerGuessed() == true) {
					playerCellButtons[y][x].setBackground(Color.red);
				}
				
				missLabel.setText("Miss:" + Integer.toString(miss));
				hitLabel.setText("Hit:" + Integer.toString(hit));
				sunkLabel.setText("Sunk:" + Integer.toString(sunk));
			}
		}
		this.revalidate();
		this.repaint();
	}

	// Ai made method to scale images
	public ImageIcon scaleImage(String filePath, int width, int height) {
		ImageIcon originalIcon = new ImageIcon(filePath);
		// Scale the image smoothly using Java's built-in image scaling algorithm
		java.awt.Image scaledImage = originalIcon.getImage().getScaledInstance(width, height,
				java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImage);
	}
	
	public void guiSetup() {
		java.awt.Font largeFont = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 24);
		java.awt.Font labelFont = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22);
		java.awt.Font shipFont = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18);

		this.setSize(2000, 1000);
		this.setDefaultCloseOperation(3);
		this.setResizable(false);

		background.setLayout(new GridLayout(1, 2));

		westPanel.setPreferredSize(new Dimension(1000, 1000));
		westPanel.setOpaque(false);
		westPanel.setLayout(new BorderLayout());

		westGrid.setPreferredSize(new Dimension(800, 800));
		westGrid.setOpaque(false);
		westGrid.setLayout(new GridLayout(10, 10));

		westWestPanel.setPreferredSize(new Dimension(50, 800));
		westWestPanel.setOpaque(false);
		westWestPanel.setLayout(new GridLayout(10, 1));

		northWestPanel.setPreferredSize(new Dimension(1000, 150));
		northWestPanel.setOpaque(false);
		northWestPanel.setLayout(new BorderLayout());

		namePanel.setOpaque(false);
		westBottomPanel.setOpaque(false);
		westNumbersGrid.setOpaque(false);

		westSpacer.setPreferredSize(new Dimension(50, 50));
		westSpacer.setOpaque(false);

		eastPanel.setPreferredSize(new Dimension(1000, 1000));
		eastPanel.setOpaque(false);
		eastPanel.setLayout(new BorderLayout());

		eastGrid.setPreferredSize(new Dimension(800, 800));
		eastGrid.setOpaque(false);
		eastGrid.setLayout(new GridLayout(1, 2));

		westEastGrid.setOpaque(false);
		westEastGrid.setLayout(new GridLayout(4, 1, 5, 5));

		eastEastGrid.setOpaque(false);
		eastEastGrid.setLayout(new GridLayout(4, 1, 5, 5));

		northEastPanel.setPreferredSize(new Dimension(1000, 150));
		northEastPanel.setOpaque(false);
		northEastPanel.setLayout(new BorderLayout());

		toggelPanel.setOpaque(false);
		eastEastPanel.setPreferredSize(new Dimension(100, 1000));
		eastEastPanel.setOpaque(false);

		toggleHorizontal.setPreferredSize(new Dimension(200, 50));
		toggleHorizontal.setFont(largeFont);
		toggleHorizontal.setBackground(Color.black);
		toggleHorizontal.setForeground(Color.yellow);
		toggleHorizontal.setText("Set Horizontal");
		toggleHorizontal.addActionListener(this);

		nameLabel.setText(playerName);
		nameLabel.setFont(largeFont);
		nameLabel.setPreferredSize(new Dimension(400, 50));
		nameLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		nameLabel.setBackground(Color.black);
		nameLabel.setForeground(Color.yellow);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		nameLabel.setOpaque(true);

		computerNamePlate.setText("SEPARATIST CIS");
		computerNamePlate.setFont(largeFont);
		computerNamePlate.setPreferredSize(new Dimension(200, 50));
		computerNamePlate.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		computerNamePlate.setBackground(Color.black);
		computerNamePlate.setForeground(Color.yellow);
		computerNamePlate.setHorizontalAlignment(JLabel.CENTER);
		computerNamePlate.setOpaque(true);

		hitLabel.setText("Hit:" + Integer.toString(hit));
		hitLabel.setFont(largeFont);
		hitLabel.setPreferredSize(new Dimension(200, 50));
		hitLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		hitLabel.setBackground(Color.black);
		hitLabel.setForeground(Color.yellow);
		hitLabel.setHorizontalAlignment(JLabel.CENTER);
		hitLabel.setOpaque(true);

		missLabel.setText("Miss:" + Integer.toString(miss));
		missLabel.setFont(largeFont);
		missLabel.setPreferredSize(new Dimension(200, 50));
		missLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		missLabel.setBackground(Color.black);
		missLabel.setForeground(Color.yellow);
		missLabel.setHorizontalAlignment(JLabel.CENTER);
		missLabel.setOpaque(true);

		sunkLabel.setText("Sunk:" + Integer.toString(sunk));
		sunkLabel.setFont(largeFont);
		sunkLabel.setPreferredSize(new Dimension(200, 50));
		sunkLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		sunkLabel.setBackground(Color.black);
		sunkLabel.setForeground(Color.yellow);
		sunkLabel.setHorizontalAlignment(JLabel.CENTER);
		sunkLabel.setOpaque(true);

		textVenator.setText("<html><center>Venator: 1<br>Length: 5</center></html>");
		textVenator.setFont(shipFont);
		textVenator.setHorizontalAlignment(JLabel.CENTER);
		textVenator.setBackground(Color.black);
		textVenator.setForeground(Color.yellow);
		textVenator.setOpaque(true);

		textAcclamator.setText("<html><center>Acclamator: 1<br>Length: 4</center></html>");
		textAcclamator.setFont(shipFont);
		textAcclamator.setHorizontalAlignment(JLabel.CENTER);
		textAcclamator.setBackground(Color.black);
		textAcclamator.setForeground(Color.yellow);
		textAcclamator.setOpaque(true);

		textArquitens.setText("<html><center>Arquitens: 2<br>Length: 3</center></html>");
		textArquitens.setFont(shipFont);
		textArquitens.setHorizontalAlignment(JLabel.CENTER);
		textArquitens.setBackground(Color.black);
		textArquitens.setForeground(Color.yellow);
		textArquitens.setOpaque(true);

		textInterceptor.setText("<html><center>Interceptor: 1<br>Length: 2</center></html>");
		textInterceptor.setFont(shipFont);
		textInterceptor.setHorizontalAlignment(JLabel.CENTER);
		textInterceptor.setBackground(Color.black);
		textInterceptor.setForeground(Color.yellow);
		textInterceptor.setOpaque(true);

		for (int i = 0; i < 10; i++) {
			JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
			label.setFont(labelFont);
			label.setForeground(Color.green);
			westWestPanel.add(label);
		}

		for (int i = 0; i < 10; i++) {
			JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
			label.setFont(labelFont);
			label.setForeground(Color.green);
			westNumbersGrid.add(label);
		}

		for (int y = 0; y < 4; y++) {
			westEastGrid.add(shipPlaceImage[y]);
			shipPlaceImage[y].setBackground(Color.black);
			shipPlaceImage[y].setOpaque(true);
		}

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				playerCellButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.cyan, 1));
				westGrid.add(playerCellButtons[i][j]);
			}
		}

		this.add(background);
		background.add(westPanel);
		background.add(eastPanel);

		westPanel.add(westGrid, BorderLayout.CENTER);
		westPanel.add(northWestPanel, BorderLayout.NORTH);
		westPanel.add(westWestPanel, BorderLayout.WEST);

		namePanel.add(nameLabel);
		northWestPanel.add(namePanel, BorderLayout.NORTH);
		westBottomPanel.add(westSpacer, BorderLayout.WEST);
		westBottomPanel.add(westNumbersGrid, BorderLayout.CENTER);
		northWestPanel.add(westBottomPanel, BorderLayout.SOUTH);

		eastPanel.add(eastGrid, BorderLayout.CENTER);
		eastPanel.add(eastEastPanel, BorderLayout.EAST);
		eastPanel.add(northEastPanel, BorderLayout.NORTH);

		eastGrid.add(westEastGrid);
		eastGrid.add(eastEastGrid);

		eastEastGrid.add(textVenator);
		eastEastGrid.add(textAcclamator);
		eastEastGrid.add(textArquitens);
		eastEastGrid.add(textInterceptor);

		toggelPanel.add(toggleHorizontal);
		northEastPanel.add(toggelPanel, BorderLayout.NORTH);

		this.setVisible(true);
		updateGrids();
	}

	public void guiChange() {
		eastPanel.removeAll();
		eastGrid.removeAll();
		eastEastPanel.removeAll();
		northEastPanel.removeAll();

		aiNamePanel.removeAll();
		eastNumbersGrid.removeAll();
		statsPanel.removeAll();

		saveButton.setText("SAVE");
		saveButton.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 24));
		saveButton.setPreferredSize(new Dimension(400, 50));
		saveButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		saveButton.setBackground(Color.black);
		saveButton.setForeground(Color.yellow);
		saveButton.setHorizontalAlignment(JLabel.CENTER);
		saveButton.addActionListener(this);

		eastGrid.setPreferredSize(new Dimension(800, 800));
		eastGrid.setLayout(new GridLayout(10, 10));

		eastEastPanel.setPreferredSize(new Dimension(50, 800));
		eastEastPanel.setLayout(new GridLayout(10, 1));
		for (int i = 0; i < 10; i++) {
			JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
			label.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22));
			label.setForeground(Color.green);
			eastEastPanel.add(label);
		}

		northEastPanel.setLayout(new BorderLayout());

		aiNamePanel.setOpaque(false);
		aiNamePanel.add(computerNamePlate);
		northEastPanel.add(aiNamePanel, BorderLayout.NORTH);
		northEastPanel.add(saveButton);

		eastNumbersGrid.setLayout(new GridLayout(1, 10));
		eastNumbersGrid.setOpaque(false);
		for (int i = 0; i < 10; i++) {
			JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
			label.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22));
			label.setForeground(Color.green);
			eastNumbersGrid.add(label);
		}

		eastSpacer.setPreferredSize(new Dimension(50, 50));
		eastSpacer.setOpaque(false);

		southEastPanel.setLayout(new BorderLayout());
		southEastPanel.setOpaque(false);
		southEastPanel.add(eastSpacer, BorderLayout.EAST);
		southEastPanel.add(eastNumbersGrid, BorderLayout.CENTER);

		northEastPanel.add(southEastPanel, BorderLayout.SOUTH);

		eastPanel.add(northEastPanel, BorderLayout.NORTH);
		eastPanel.add(eastEastPanel, BorderLayout.EAST);
		eastPanel.add(eastGrid, BorderLayout.CENTER);

		statsPanel.setLayout(new GridLayout(1, 3, 10, 0));
		statsPanel.setOpaque(false);
		statsPanel.add(hitLabel);
		statsPanel.add(missLabel);
		statsPanel.add(sunkLabel);

		northWestPanel.add(statsPanel, BorderLayout.CENTER);

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				eastGrid.add(computerCells[i][j]);
				playerCellButtons[i][j].setEnabled(false);
			}
		}

		eastPanel.revalidate();
		eastPanel.repaint();
	    
		northWestPanel.revalidate();
		northWestPanel.repaint();

		background.revalidate();
		background.repaint();
		this.revalidate();
		this.repaint();
	}

}