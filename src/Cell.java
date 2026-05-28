

public class Cell {
	private boolean playerShipPresent;
	private boolean computerShipPresent;
	private boolean playerGuessed;
	private boolean computerGuessed;
	private static int indexX;
	private static int indexY;
	public Cell(int indexX, int indexY) {
		this.indexX = indexX;
		this.indexY = indexY;
		this.playerGuessed = playerGuessed;
		this.computerGuessed = computerGuessed;
		this.playerShipPresent = playerShipPresent;
		this.computerShipPresent = computerShipPresent;
	}
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
