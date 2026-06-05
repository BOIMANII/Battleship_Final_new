
/**
 * @author Max
 * @date 2026-05-26
 * Description: The object class of the computer's profile
 */

import java.util.Random;

public class ComputerPlayer extends Player {

	/**
	 * @return int[] selection
	 * 
	 *         This method generates a single grid which will be the computer's
	 *         selection for a ship placement
	 * 
	 *         Evaluation for whether or not the selection is valid, and setting
	 *         ship placement is done in a different object class (Board.java)
	 */
	public int[] generateShipPlacement() {
		Random randgen = new Random();
		int[] selection = new int[2];

		for (int i = 0; i < 2; i++) {
			selection[i] = randgen.nextInt(0, 10); // generate in index format
		}

		return selection;
	}

}
