
import java.util.*;

public class Ship {
	private static int length;
	private int hitCount;
	private boolean isSunk;
	private int[][] shipPosition;
	public Ship(int[][] pos) {
		this.shipPosition = pos;
		this.length = length;
		this.hitCount = hitCount;
		this.isSunk = isSunk;
	}
	public boolean evaluateSunk() {
		return (length == hitCount);
	}
	public boolean getSunk() {
		return isSunk;
	}
	public void setShipPositions() {
		this.shipPosition = shipPosition;
	}
	

}
