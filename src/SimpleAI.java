
import java.util.Random;

public class SimpleAI {
//hi
	public int[] guess() {
		Random Rng = new Random();
		int[] Guess = { Rng.nextInt(0, 10), Rng.nextInt(0, 10) };
		return Guess;
	}
}
