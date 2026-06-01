

public class Cell {
	private boolean playerShipPresent = false;
	private boolean computerShipPresent = false;
	private boolean playerGuessed = false;
	private boolean computerGuessed = false;
	private static int indexX;
	private static int indexY;
	
	public Cell(int indexX, int indexY) {
		this.indexX = indexX;
		this.indexY = indexY;
	}
	
	public int getX() {return indexX;}
	public int getY() {return indexY;}
	
	public boolean isPlayerShipPresent() {
		return playerShipPresent;
	}
	public void setPlayerShipPresent(boolean playerShipPresent) {
		this.playerShipPresent = playerShipPresent;
	}
	public boolean isComputerShipPresent() {
		return computerShipPresent;
	}
	public void setComputerShipPresent(boolean computerShipPresent) {
		this.computerShipPresent = computerShipPresent;
	}
	public boolean isPlayerGuessed() {
		return playerGuessed;
	}
	public void setPlayerGuessed(boolean playerGuessed) {
		this.playerGuessed = playerGuessed;
	}
	public boolean isComputerGuessed() {
		return computerGuessed;
	}
	public void setComputerGuessed(boolean computerGuessed) {
		this.computerGuessed = computerGuessed;
	}
	

}
