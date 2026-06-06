import java.util.ArrayList;

/**
 * @author Andy
 * @teacher Mr. Smintich
 * @date 2026-05-29
 *
 *       Description: A complex Battleship AI featuring: - Hit-distribution
 *       skipping - Parity shifting - Walk-ship logic - Fallback orientation -
 *       No recursion or stack overflow
 */
public class ComplexAI {

	private boolean lastHit = false;
	private int[] previous = { 0, 0 };

	private int[] origin = null;
	private int orientation = -1;

	private boolean posDone = false;
	private boolean negDone = false;

	private boolean orientationTriedBoth = false;

	private int hitDist = 2;
	private int huntIndex = 0;

	public ArrayList<String> sunkList = new ArrayList<>();

	private final int[][] DIRS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

	public void setHit(boolean h) {
		lastHit = h;
	}

	public boolean getHit() {
		return lastHit;
	}

	public int[] guess(boolean[][] visited) {
		try {
			return safeGuess(visited);
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();
			return new int[] { 0, 0 };
		}
	}

	private int[] safeGuess(boolean[][] visited) {

		updateHitDist();

		if (!lastHit) {

			if (origin != null && orientation != -1) {

				int dx = previous[0] - origin[0];
				int dy = previous[1] - origin[1];

				if (orientation == 0) {
					if (dx > 0)
						posDone = true;
					if (dx < 0)
						negDone = true;
				} else {
					if (dy > 0)
						posDone = true;
					if (dy < 0)
						negDone = true;
				}

				previous = origin.clone();

				if (posDone && negDone) {

					if (!orientationTriedBoth) {
						orientation = (orientation == 0) ? 1 : 0;
						posDone = false;
						negDone = false;
						orientationTriedBoth = true;
						previous = origin.clone();
						return target(visited);
					}

					resetTargeting();
					return hunt(visited);
				}

				return target(visited);
			}

			return hunt(visited);
		}

		if (origin == null) {
			origin = previous.clone();
			orientation = -1;
			posDone = false;
			negDone = false;
			orientationTriedBoth = false;
		}

		return target(visited);
	}

	private int[] hunt(boolean[][] visited) {

		System.out.println("HUNT MODE START");

		for (int attempts = 0; attempts < 500; attempts++) {

			int row = huntIndex / 10;
			int col = huntIndex % 10;

			System.out.println("Checking (" + col + "," + row + ")");

			if (inBounds(col, row) && !visited[col][row]) {
				System.out.println("Firing (" + col + "," + row + ")");
				previous = new int[] { col, row };
				huntIndex++;
				return previous;
			}

			System.out.println("Already guessed (" + col + "," + row + ") → forward scan");

			int fx = col;
			int fy = row;

			while (fy < 10) {

				fx++;

				if (fx >= 10) {
					fx = 0;
					fy++;
					if (fy < 10)
						System.out.println("Row wrap → now at row " + fy);
				}

				if (fy >= 10)
					break;

				System.out.println("Forward scan checking (" + fx + "," + fy + ")");

				if (!visited[fx][fy]) {
					System.out.println("Forward scan found empty (" + fx + "," + fy + ")");
					previous = new int[] { fx, fy };
					huntIndex = fy * 10 + fx + 1;
					return previous;
				}
			}

			huntIndex++;
		}

		System.out.println("HUNT FALLBACK → (0,0)");
		return new int[] { 0, 0 };
	}

	private int[] target(boolean[][] visited) {

		while (true) {

			if (orientation == -1) {
				boolean found = false;
				for (int d = 0; d < 4; d++) {
					int tx = origin[0] + DIRS[d][0];
					int ty = origin[1] + DIRS[d][1];

					if (!inBounds(tx, ty) || visited[tx][ty])
						continue;

					System.out.println("Target probing (" + tx + "," + ty + ")");
					previous = new int[] { tx, ty };
					orientation = (d <= 1) ? 0 : 1;
					posDone = false;
					negDone = false;
					orientationTriedBoth = false;
					found = true;
					break;
				}

				if (found)
					return previous;

				System.out.println("Target failed → back to hunt");
				previous = origin.clone();
				resetTargeting();
				return hunt(visited);
			}

			if (!posDone) {
				int dirIndex = (orientation == 0) ? 0 : 2;
				int nx = previous[0] + DIRS[dirIndex][0];
				int ny = previous[1] + DIRS[dirIndex][1];

				if (isM(nx, ny, visited)) {
					System.out.println("Positive side M at (" + nx + "," + ny + ")");
					posDone = true;
				} else {
					System.out.println("Sweeping positive (" + nx + "," + ny + ")");
					previous = new int[] { nx, ny };
					return previous;
				}
			}

			if (!negDone) {
				int dirIndex = (orientation == 0) ? 1 : 3;
				int nx = origin[0] + DIRS[dirIndex][0];
				int ny = origin[1] + DIRS[dirIndex][1];

				if (isM(nx, ny, visited)) {
					System.out.println("Negative side M at (" + nx + "," + ny + ")");
					negDone = true;
				} else {
					System.out.println("Sweeping negative (" + nx + "," + ny + ")");
					previous = new int[] { nx, ny };
					return previous;
				}
			}

			if (posDone && negDone) {

				if (!orientationTriedBoth) {
					System.out.println("Switching orientation");
					orientation = (orientation == 0) ? 1 : 0;
					posDone = false;
					negDone = false;
					orientationTriedBoth = true;
					previous = origin.clone();
					continue;
				}

				System.out.println("Both sides dead → back to hunt");
				resetTargeting();
				return hunt(visited);
			}


			System.out.println("Target fallback → hunt");
			previous = origin.clone();
			resetTargeting();
			return hunt(visited);
		}
	}

	private boolean isM(int x, int y, boolean[][] visited) {
		if (!inBounds(x, y))
			return true;
		return visited[x][y];
	}

	private void updateHitDist() {

		int arquintisCount = 0;
		for (String s : sunkList)
			if (s.equals("ARQUINTIS"))
				arquintisCount++;

		int smallestRemaining = 5;

		if (arquintisCount < 2)
			smallestRemaining = 3;
		else {
			boolean has2 = !sunkList.contains("INTERCEPTOR");
			boolean has4 = !sunkList.contains("ACCLIMATOR");

			if (has2)
				smallestRemaining = 2;
			else if (has4)
				smallestRemaining = 4;
			else
				smallestRemaining = 5;
		}

		hitDist = smallestRemaining - 1;
	}

	private boolean inBounds(int x, int y) {
		return x >= 0 && x < 10 && y >= 0 && y < 10;
	}

	private void resetTargeting() {
		origin = null;
		orientation = -1;
		posDone = false;
		negDone = false;
		orientationTriedBoth = false;
	}

}
