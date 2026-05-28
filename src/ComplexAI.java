

public class ComplexAI {
	private boolean hit;
	private int[] previous;
	private int hitDist = 1;;
	
	public int [] guess() {
		int[] guess = new int[2];
		
		if (!hit) {
			if (previous[0] + hitDist > 9 && previous[1] != 9) {
				guess[1] +=1;
			}
			else if (previous[0] + hitDist > 9) {
				guess[1] = 0;
			}
			guess[0] = (previous[0] + hitDist)%10;
			return guess;
		}
		else {
			guess[1]+=1;
			guess[0] = previous[0];
			return guess;
		}
	}
	public void setHit(boolean e) {
		this.hit = hit;
	}
}
