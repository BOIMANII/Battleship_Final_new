
/**
 * @author Max
 * @date 2026-05-26
 * Description: The abstract parent class that HumanPlayer and ComputerPlayer inherit from
 */

import java.util.ArrayList;

public abstract class Player {

	// Preset name, only used by the computer player's display - when the game is
	// launched, the player's name is overwritten with what is in the text box
	String name = "Computer";
	private ArrayList<Ship> ships = new ArrayList<>();
	private boolean hasLost;

	public String getName() {
		return name;
	}

	public boolean getHasLost() {
		return hasLost;
	}

	public ArrayList<Ship> getShips() {
		return ships;
	}

	/**
	 * Evaluates whether or not the player has lost (if all ships are sunk) and
	 * automatically sets hasLost
	 */
	public void evaluateHasLost() {
		boolean lost = true;
		for (Ship ship : ships) {
			if (ship.getSunk() == false) {
				lost = false;
			}
		}
		hasLost = lost;
	}

}
