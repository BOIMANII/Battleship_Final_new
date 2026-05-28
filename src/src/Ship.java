package src;
import java.util.*;

public class Ship {
	private int size;
	private int hits;
	private boolean status;
	private ArrayList<int[]> shipPosition;
	private ArrayList<int[]> shipHitPosition;
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public ArrayList<int[]> getShipPosition() {
		return shipPosition;
	}
	public void setShipPosition(ArrayList<int[]> shipPosition) {
		this.shipPosition = shipPosition;
	}
	public ArrayList<int[]> getShipHitPosition() {
		return shipHitPosition;
	}
	public void setShipHitPosition(ArrayList<int[]> shipHitPosition) {
		this.shipHitPosition = shipHitPosition;
	}
	
	
	
}