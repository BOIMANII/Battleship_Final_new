

import java.util.Random;

public class SimpleAI {
//hi
	public static int[] Guess() {
		Random Rng = new Random();
		int[] Guess = { Rng.nextInt(0, 11), Rng.nextInt(0, 11) };
		return Guess;
	}
}
