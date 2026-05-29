

public class ComplexAI {
	private boolean hit;
	private int[] previous;
	private int hitDist = 1;;
	private int dir = 0;
	private int[] origin;
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
			if (dir != 0) {
				if (dir == 1) {
					if (guess[1] == 0) {
						dir++;
					}
					else guess[1] = origin[1] + 1;
				}
				else if (dir == 2) {
					
				}
			}
			else {
				dir+=1;
				origin = previous;
				guess[0] = previous[0] + 1;
			}
			
			return guess;
		}
	}
	public void setHit(boolean e) {
		
		this.hit = hit;
		
	}
}
