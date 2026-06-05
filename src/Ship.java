
import java.util.*;

public class Ship {
	private int length;
	private int hitCount = 0;
	private boolean isSunk;
	private int[][] shipPosition;

	public Ship(int[][] pos, int length) {
		this.shipPosition = pos;
		this.length = length;
	}

	public void evaluateSunk() {
		isSunk = (length == hitCount);
	}

	public boolean getSunk() {
		return isSunk;
	}

	public int[][] getPositions() {
		return shipPosition;
	}

	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(int hits) {
		hitCount = hits;
	}

	public String getName() {
		if (length == 2) {
			return "Interceptor";
		} else if (length == 3) {
			return "Arquintis";
		} else if (length == 4) {
			return "Acclimator";
		} else {
			return "Venator";
		}
	}

	public void incrementHitCount() {
		hitCount++;
	}

}
