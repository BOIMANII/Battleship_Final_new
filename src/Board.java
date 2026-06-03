
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
import java.util.Random;
import java.util.Scanner;

public class Board extends JFrame implements ActionListener {

	Cell[][] grid = new Cell[10][10];

	boolean useHorizontal = false;
	boolean useComplex;
	boolean computerFirst;
	int shipsPlaced = 0;
	HumanPlayer humanPlayer = new HumanPlayer();
	ComputerPlayer computerPlayer = new ComputerPlayer();
	int guesses = 0;
	String playerName;
	int hit;
	int miss;
	int sunk;

	// Gui components
	// Note for naming convention alot of the oroginal code and names were for a
	// borderLayout but something got chagned to grid
	JFrame frame;
	JButton[][] playerCellButtons;
	JButton[][] computerCells;
	JLabel[] shipPlaceImage;
	JButton saveButton;
	JButton loadButton;
	JCheckBox toggleHorizontal; // Checking this box means horizontal is used
	JLabel background;
	ImageIcon backgroundImage;
	JPanel westPanel;
	JPanel eastPanel;
	JPanel westGrid;
	JPanel eastGrid;
	JPanel eastEastGrid;// East of the east grid
	JPanel westEastGrid;// west of the east grid
	JPanel northEastPanel;
	JPanel northWestPanel;
	JPanel westWestPanel;
	JPanel eastEastPanel;
	JLabel nameLabel;
	JOptionPane invalidPlacment;
	JLabel computerNamePlate;
	JLabel hitLabel;
	JLabel missLabel;
	JLabel sunkLabel;

	ImageIcon iconShipBoom;
	ImageIcon iconVenator; // Cruiser 5 length
	ImageIcon iconAcclamator; // Battleship 4 Length
	ImageIcon iconArquitens; // Destoryer 3 Length
	ImageIcon iconInterceptor; // Patrol 2 length

	JLabel textVenator;
	JLabel textAcclamator;
	JLabel textArquitens;
	JLabel textInterceptor;

	JPanel toggelPanel;
	JPanel westBottomPanel;
	JPanel westSpacer;
	JPanel westNumbersGrid;
	JPanel namePanel;

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
			throws InterruptedException, FileNotFoundException {
		this.playerName = playerName;
		// GUI Components
		backgroundImage = new ImageIcon("backgroundBoard.jpg");
		background = new JLabel(backgroundImage);
		westPanel = new JPanel();
		eastPanel = new JPanel();
		westGrid = new JPanel();
		eastGrid = new JPanel();
		playerCellButtons = new JButton[10][10];
		shipPlaceImage = new JLabel[4];
		computerCells = new JButton[10][10];
		westEastGrid = new JPanel();
		eastEastGrid = new JPanel();
		nameLabel = new JLabel();
		northEastPanel = new JPanel();
		northWestPanel = new JPanel();
		westWestPanel = new JPanel();
		eastEastPanel = new JPanel();
		toggleHorizontal = new JCheckBox();
		computerCells = new JButton[10][10];
		invalidPlacment = new JOptionPane();
		computerNamePlate = new JLabel();
		toggelPanel = new JPanel();
		westBottomPanel = new JPanel(new BorderLayout());
		westSpacer = new JPanel();
		westNumbersGrid = new JPanel(new GridLayout(1, 10));
		namePanel = new JPanel();
		hitLabel = new JLabel();
		missLabel = new JLabel();
		sunkLabel = new JLabel();

		textVenator = new JLabel();
		textAcclamator = new JLabel();
		textArquitens = new JLabel();
		textInterceptor = new JLabel();

		iconShipBoom = new ImageIcon("iconShipBoom.png");
		iconVenator = scaleImage("iconVenator.png", 250, 100);// Couldnet get images to scale to i asked ai
		iconAcclamator = scaleImage("iconAcclamator.png", 250, 100);
		iconArquitens = scaleImage("iconArquitens.png", 250, 100);
		iconInterceptor = scaleImage("iconInterceptor.png", 250, 100);

		shipPlaceImage[0] = new JLabel(iconVenator);
		shipPlaceImage[1] = new JLabel(iconAcclamator);
		shipPlaceImage[2] = new JLabel(iconArquitens);
		shipPlaceImage[3] = new JLabel(iconInterceptor);

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

		// Board Logic
		if (isLoad == false) {
			this.useComplex = useComplex;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					grid[i][j] = new Cell(i, j);
				}
			}
			/*
			 * GUI note:
			 * 
			 * Display the ships and stuff on the right here
			 * 
			 * Also there should be 2 length 3 ships
			 */
			guiSetup();
			humanPlayer.setName(playerName);

		} else {
			try {
				File humanPlayerFile = new File(playerName + "HumanPlayer.txt");
				Scanner humanScanner = new Scanner(humanPlayerFile);

				humanPlayer.setName(playerName);

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

				File computerPlayerFile = new File(playerName + "ComputerPlayer.txt");
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

				humanScanner.close();
				computerScanner.close();
				boardScanner.close();

				updateGrids();
			} catch (Exception e) {
				@SuppressWarnings("unused")
				Board newBoard = new Board(playerName, useComplex, false);
				frame.dispose();
			}
		}

	}

	public void guiSetup() {
<<<<<<< HEAD
	    background.setLayout(new GridLayout(1, 2));
	    
	    westGrid.setPreferredSize(new Dimension(800,800));
	    westGrid.setOpaque(false);
	    westGrid.setLayout(new GridLayout(10,10));
	    
	    eastGrid.setPreferredSize(new Dimension(800,800));
	    eastGrid.setOpaque(false);
	    eastGrid.setLayout(new GridLayout(1,2));
	    
	    westPanel.setPreferredSize(new Dimension(1000,1000));
	    westPanel.setOpaque(false);
	    westPanel.setLayout(new BorderLayout());
	    
	    northWestPanel.setPreferredSize(new Dimension(1000, 100)); 
	    northWestPanel.setOpaque(false);
	    northWestPanel.setLayout(new BorderLayout()); 
	    
	    northEastPanel.setPreferredSize(new Dimension(1000, 100));
	    northEastPanel.setOpaque(false);
	    
	    eastPanel.setPreferredSize(new Dimension(1000,1000));
	    eastPanel.setOpaque(false);
	    eastPanel.setLayout(new BorderLayout());
	            
	    westEastGrid.setOpaque(false);
	    westEastGrid.setLayout(new GridLayout(4,1, 5, 5));
	    
	    eastEastGrid.setOpaque(false);
	    eastEastGrid.setLayout(new GridLayout(4,1, 5, 5));
	    
	    eastEastPanel.setPreferredSize(new Dimension(100,1000));
	    eastEastPanel.setOpaque(false);
	    
	    toggleHorizontal.setPreferredSize(new Dimension(200,50));
	    toggleHorizontal.addActionListener(this);
	    toggleHorizontal.setBackground(Color.black);
	    toggleHorizontal.setText("Set Horizontal");
	    toggleHorizontal.setForeground(Color.yellow);
	    
	    java.awt.Font shipFont = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18);
	    
	    textVenator.setText("<html><center>Venator: 1<br>Length: 5</center></html>"); // HTML was ai acceleration 
	    textVenator.setFont(shipFont);
	    textVenator.setHorizontalAlignment(JLabel.CENTER);
	    textVenator.setBackground(Color.black);
	    textVenator.setForeground(Color.yellow);
	    textVenator.setOpaque(true);
	    
=======
>>>>>>> e347aafc4046a763bada23b5a9dd14ae7ccbb80a
		background.setLayout(new GridLayout(1, 2));

		westGrid.setPreferredSize(new Dimension(800, 800));
		westGrid.setOpaque(false);
		westGrid.setLayout(new GridLayout(10, 10));

		eastGrid.setPreferredSize(new Dimension(800, 800));
		eastGrid.setOpaque(false);
		eastGrid.setLayout(new GridLayout(1, 2));

		westPanel.setPreferredSize(new Dimension(1000, 1000));
		westPanel.setOpaque(false);
		westPanel.setLayout(new BorderLayout());

		northWestPanel.setPreferredSize(new Dimension(1000, 150));
		northWestPanel.setOpaque(false);
		northWestPanel.setLayout(new BorderLayout());

		northEastPanel.setPreferredSize(new Dimension(1000, 150));
		northEastPanel.setOpaque(false);

		eastPanel.setPreferredSize(new Dimension(1000, 1000));
		eastPanel.setOpaque(false);
		eastPanel.setLayout(new BorderLayout());

		westEastGrid.setOpaque(false);
		westEastGrid.setLayout(new GridLayout(4, 1, 5, 5));

		eastEastGrid.setOpaque(false);
		eastEastGrid.setLayout(new GridLayout(4, 1, 5, 5));

		eastEastPanel.setPreferredSize(new Dimension(100, 1000));
		eastEastPanel.setOpaque(false);
		
		
		hitLabel.setText("Hit:" + Integer.toString(hit));
		hitLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 24));
		hitLabel.setPreferredSize(new Dimension(200, 50));
		hitLabel.setMaximumSize(new Dimension(200, 50));
		hitLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		hitLabel.setBackground(Color.black);
		hitLabel.setForeground(Color.yellow);
		hitLabel.setHorizontalAlignment(JLabel.CENTER);
		hitLabel.setOpaque(true);
		
		missLabel.setText("Miss:" + Integer.toString(miss));
		missLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 24));
		missLabel.setPreferredSize(new Dimension(200, 50));
		missLabel.setMaximumSize(new Dimension(200, 50));
		missLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		missLabel.setBackground(Color.black);
		missLabel.setForeground(Color.yellow);
		missLabel.setHorizontalAlignment(JLabel.CENTER);
		missLabel.setOpaque(true);
		
		sunkLabel.setText("Sunk:" + Integer.toString(sunk));
		sunkLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 24));
		sunkLabel.setPreferredSize(new Dimension(200, 50));
		sunkLabel.setMaximumSize(new Dimension(200, 50));
		sunkLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		sunkLabel.setBackground(Color.black);
		sunkLabel.setForeground(Color.yellow);
		sunkLabel.setHorizontalAlignment(JLabel.CENTER);
		sunkLabel.setOpaque(true);

		toggleHorizontal.setPreferredSize(new Dimension(200, 50));
		toggleHorizontal.setBackground(Color.black);
		toggleHorizontal.setText("Set Horizontal");
		toggleHorizontal.setForeground(Color.yellow);

		shipFont = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18);

		textVenator.setText("<html><center>Venator: 1<br>Length: 5</center></html>"); // HTML was ai acceleration
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

		textArquitens.setText("<html><center>Arquitens: 1<br>Length: 3</center></html>");
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

		nameLabel.setText(playerName);
		nameLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 24));
		nameLabel.setPreferredSize(new Dimension(200, 50));
		nameLabel.setMaximumSize(new Dimension(200, 50));
		nameLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		nameLabel.setBackground(Color.black);
		nameLabel.setForeground(Color.yellow);
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		nameLabel.setOpaque(true);

		computerNamePlate.setText("SEPARATIST CIS");
		computerNamePlate.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 24));
		computerNamePlate.setPreferredSize(new Dimension(200, 50));
		computerNamePlate.setMaximumSize(new Dimension(200, 50));
		computerNamePlate.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		computerNamePlate.setBackground(Color.black);
		computerNamePlate.setForeground(Color.yellow);
		computerNamePlate.setHorizontalAlignment(JLabel.CENTER);
		computerNamePlate.setOpaque(true);

		westWestPanel.setPreferredSize(new Dimension(50, 800));
		westWestPanel.setOpaque(false);
		westWestPanel.setLayout(new GridLayout(10, 1));
		for (int i = 0; i < 10; i++) {
			JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
			label.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22));
			label.setForeground(Color.green);
			westWestPanel.add(label);
		}

		namePanel.setOpaque(false);
		namePanel.add(nameLabel);
		northWestPanel.add(namePanel, BorderLayout.NORTH);

		westNumbersGrid.setOpaque(false);
		for (int i = 0; i < 10; i++) {
			JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
			label.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22));
			label.setForeground(Color.green);
			westNumbersGrid.add(label);
		}

		westSpacer.setPreferredSize(new Dimension(50, 50));
		westSpacer.setOpaque(false);

		westBottomPanel.setOpaque(false);
		westBottomPanel.add(westSpacer, BorderLayout.WEST);
		westBottomPanel.add(westNumbersGrid, BorderLayout.CENTER);

		northWestPanel.add(westBottomPanel, BorderLayout.SOUTH);

		this.setSize(2000, 1000);
		this.setDefaultCloseOperation(3);
		this.setResizable(false);

		this.add(background);

		background.add(westPanel);
		background.add(eastPanel);

		westPanel.add(westGrid, BorderLayout.CENTER);
		westPanel.add(northWestPanel, BorderLayout.NORTH);
		westPanel.add(westWestPanel, BorderLayout.WEST);

		eastPanel.add(eastEastPanel, BorderLayout.EAST);
		eastPanel.add(northEastPanel, BorderLayout.NORTH);

		eastPanel.add(eastGrid, BorderLayout.CENTER);
		eastGrid.add(westEastGrid);
		eastGrid.add(eastEastGrid);

		eastEastGrid.add(textVenator);
		eastEastGrid.add(textAcclamator);
		eastEastGrid.add(textArquitens);
		eastEastGrid.add(textInterceptor);

		toggelPanel.setOpaque(false);
		toggelPanel.add(toggleHorizontal);
		northEastPanel.setLayout(new BorderLayout());
		northEastPanel.add(toggelPanel, BorderLayout.NORTH);

		for (int y = 0; y < 4; y++) {
			westEastGrid.add(shipPlaceImage[y]);
			shipPlaceImage[y].setBackground(Color.black);
			shipPlaceImage[y].setOpaque(true);
		}

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				westGrid.add(playerCellButtons[i][j]);
			}
		}

		this.setVisible(true);
		updateGrids();
	}

	public void guiChange() {
	    eastPanel.removeAll();
	    eastGrid.removeAll();
	    eastEastPanel.removeAll();
	    northEastPanel.removeAll();

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

	    JPanel compNameWrapper = new JPanel();
	    compNameWrapper.setOpaque(false);
	    compNameWrapper.add(computerNamePlate);
	    northEastPanel.add(compNameWrapper, BorderLayout.NORTH);

	    JPanel eastNumbersGrid = new JPanel(new GridLayout(1, 10));
	    eastNumbersGrid.setOpaque(false);
	    for (int i = 0; i < 10; i++) {
	        JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
	        label.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 22));
	        label.setForeground(Color.green);
	        eastNumbersGrid.add(label);
	    }

	    JPanel eastSpacer = new JPanel();
	    eastSpacer.setPreferredSize(new Dimension(50, 50));
	    eastSpacer.setOpaque(false);

	    JPanel eastBottomWrapper = new JPanel(new BorderLayout());
	    eastBottomWrapper.setOpaque(false);
	    eastBottomWrapper.add(eastSpacer, BorderLayout.EAST);
	    eastBottomWrapper.add(eastNumbersGrid, BorderLayout.CENTER);

	    northEastPanel.add(eastBottomWrapper, BorderLayout.SOUTH);

	    eastPanel.add(northEastPanel, BorderLayout.NORTH);
	    eastPanel.add(eastEastPanel, BorderLayout.EAST);
	    eastPanel.add(eastGrid, BorderLayout.CENTER);
	    
	    JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0)); 
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

			JOptionPane.showMessageDialog(null, "Invalid Placment", "ERROR", JOptionPane.WARNING_MESSAGE);
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
<<<<<<< HEAD
		
=======

>>>>>>> e347aafc4046a763bada23b5a9dd14ae7ccbb80a
		// Based on selection, different things will happen
		// If the player pressed one of the buttons on their own cells (to place a ship)
		if (isPlace == true) {
			int[] selected = getGrid((JButton) e.getSource());
			// By how many ships are placed, the length is determined (smallest ships placed
			// first)
			int length = shipsPlaced + 2;
			if (length == 4 || length == 5) {
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
				PrintWriter writer = new PrintWriter(humanPlayer.getName() + "HumanPlayer.txt");
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

					// At this point we should have a list of coordinates in the above mentioned
					// format
					// Save, then save hitCounter for the ship
					writer.println(positions);
					writer.println(computerPlayer.getShips().get(i).getHitCount());
				}
				writer.close();

				writer = new PrintWriter(humanPlayer.getName() + "BoardGrid.txt");
				// First save guesses and useComplex
				writer.println(guesses);
				writer.println(useComplex);
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
				JOptionPane.showMessageDialog(null, "Already Guessed", "ERROR", JOptionPane.WARNING_MESSAGE);

				/*
				 * GUI notes:
				 * 
				 * Make error popup appear saying guess is invalid (already guessed), nothing
				 * else happens
				 */
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
						JOptionPane.showMessageDialog(null, "YOU SUNK A " + ship.getName(), "CONGRATULATIONS",
								JOptionPane.INFORMATION_MESSAGE, iconShipBoom);

						/*
						 * GUI notes:
						 * 
						 * Popup info thingy that will say "You have sunk computer's ___"
						 * 
						 * I'd recommend using ship.getName() and maybe an explosion image to go with
						 * the popup
						 * 
						 * If you want a list of ships each player still has, you adjust the ship count
						 * here
						 */
					}
				} else {
					miss++;

				}

				// Update the grids after the guess, check to see if player won
				updateGrids();
				checkWin();

				// After player's guess, computer will guess (this makes up one turn)
				computerGuess();
				checkWin();
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
				JOptionPane.showMessageDialog(null, "THE ENEMY SUNK A " + ship.getName(), "BOOO",
						JOptionPane.INFORMATION_MESSAGE, iconShipBoom);
				/*
				 * GUI notes:
				 * 
				 * Popup info thingy that will say "Computer has sunk your ___"
				 * 
				 * I'd recommend using ship.getName() and maybe an explosion image to go with
				 * the popup
				 * 
				 * If you want a list of ships each player still has, you adjust the ship count
				 * here
				 */
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

			/*
			 * TODO
			 * 
			 * Scoreboard fileio stuff here:
			 * 
			 * Get arraylist of fileio stuff, add player's score and name to it, sort, save
			 * new list and print
			 */
			
			JOptionPane.showMessageDialog(null, "YOU SUNCK ALL THE CIS SHIPS", "VICTORY",
					JOptionPane.INFORMATION_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "YOU LOST AND GOT BLOWN UP", "BOOO",
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
		/*
		 * GUI note:
		 * 
		 * This method updates the color of the 2 grids to correctly reflect what is
		 * going on
		 * 
		 * ie orange or something for a left side player ship not hit yet, red for hit
		 * grids (both sides), blue for places not guessed yet and green for misses
		 * (both sides)
		 * 
		 * Cell has a bunch of boolean values for this very reason
		 */
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				computerCells[y][x].setBackground(Color.green);
				if (grid[x][y].isPlayerShipPresent() == false) {
					playerCellButtons[y][x].setBackground(Color.green);
				}
				if (grid[x][y].isPlayerShipPresent() == true) {
					playerCellButtons[y][x].setBackground(Color.orange);
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

}