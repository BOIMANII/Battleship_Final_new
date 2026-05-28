

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
	JButton toggleHorizontal;
	boolean useHorizontal;
	boolean useComplex;
	boolean computerFirst;
	HumanPlayer humanPlayer = new HumanPlayer();
	ComputerPlayer computerPlayer = new ComputerPlayer();
	
	/**
	 * Constructor for board
	 * This will set up humanPlayer's name, set whether or not
	 * the game is played in hard mode and initialize all cells in grid
	 * 
	 * It will then call call setShips twice to conduct the ship placement 
	 * process, for both the player and the computer
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
	public Board(String playerName, boolean useComplex, boolean isLoad) {
		
		
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
