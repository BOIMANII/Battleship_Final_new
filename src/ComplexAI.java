import java.util.ArrayList;
/**
 * @author Andy
 * @teacher Mr. Smintich
 * @date 2026-05-29
 *
 * Description: A complex Battleship AI featuring:
 * - Hit-distribution skipping
 * - Parity shifting
 * - Walk-ship logic
 * - Fallback orientation
 * - No recursion or stack overflow
 */
public class ComplexAI {

    private boolean lastHit = false;
    private int[] previous = {0, 0};

    private int[] origin = null;
    private int orientation = -1;

    private boolean posDone = false;
    private boolean negDone = false;

    private boolean orientationTriedBoth = false;

    private int hitDist = 2;
    private int huntIndex = 0;

    public ArrayList<String> sunkList = new ArrayList<>();

    // Directions: right, left, down, up
    private final int[][] DIRS = {
        {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    public void setHit(boolean h) { lastHit = h; }
    public boolean getHit() { return lastHit; }

    public int[] guess(boolean[][] visited) {
        try {
            return safeGuess(visited);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return new int[]{0, 0};
        }
    }

    private int[] safeGuess(boolean[][] visited) {

        updateHitDist();

        // MISS → target mode cleanup
        if (!lastHit) {

            if (origin != null && orientation != -1) {

                int dx = previous[0] - origin[0];
                int dy = previous[1] - origin[1];

                // Mark which side failed
                if (orientation == 0) {
                    if (dx > 0) posDone = true;
                    if (dx < 0) negDone = true;
                } else {
                    if (dy > 0) posDone = true;
                    if (dy < 0) negDone = true;
                }

                previous = origin.clone();

                // Both sides failed
                if (posDone && negDone) {

                    // Try the other orientation once
                    if (!orientationTriedBoth) {
                        orientation = (orientation == 0) ? 1 : 0;
                        posDone = false;
                        negDone = false;
                        orientationTriedBoth = true;
                        previous = origin.clone();
                        return target(visited);
                    }

                    // Fully give up → hunt
                    resetTargeting();
                    return hunt(visited);
                }

                return target(visited);
            }

            return hunt(visited);
        }

        // HIT → set origin
        if (origin == null) {
            origin = previous.clone();
            orientation = -1;
            posDone = false;
            negDone = false;
            orientationTriedBoth = false;
        }

        return target(visited);
    }

    // HUNT MODE: use hitDist normally, but if visited → increment by 1 until empty
    private int[] hunt(boolean[][] visited) {

        System.out.println("HUNT MODE START");

        for (int attempts = 0; attempts < 500; attempts++) {

            int row = huntIndex / 10;
            int col = huntIndex % 10;

            System.out.println("Checking (" + col + "," + row + ")");

            // If empty AND matches hitDist → fire
            if (!visited[col][row] &&
                ((col - (row % 2)) % hitDist == 0)) {

                System.out.println("Firing (" + col + "," + row + ")");
                previous = new int[]{col, row};
                huntIndex++;
                return previous;
            }

            // If visited OR spacing mismatch → increment by 1 until empty
            System.out.println("Visited or spacing mismatch → incrementing");

            int startIndex = huntIndex;

            while (true) {
                huntIndex++;

                // Wrap to 0,0
                if (huntIndex >= 100) {
                    System.out.println("Wrapped to (0,0)");
                    huntIndex = 0;
                }

                int ny = huntIndex / 10;
                int nx = huntIndex % 10;

                System.out.println("Checking (" + nx + "," + ny + ")");

                if (!visited[nx][ny]) {
                    System.out.println("Firing (" + nx + "," + ny + ")");
                    previous = new int[]{nx, ny};
                    huntIndex++;
                    return previous;
                }

                // Safety: if we looped entire board, break
                if (huntIndex == startIndex) {
                    System.out.println("Full board loop → fallback");
                    return new int[]{0, 0};
                }
            }
        }

        System.out.println("HUNT FALLBACK → (0,0)");
        return new int[]{0, 0};
    }

    // TARGET MODE: sweep along orientation until both sides fail
    private int[] target(boolean[][] visited) {

        while (true) {

            // No orientation yet → probe all 4 directions
            if (orientation == -1) {
                boolean found = false;

                for (int d = 0; d < 4; d++) {
                    int tx = origin[0] + DIRS[d][0];
                    int ty = origin[1] + DIRS[d][1];

                    if (!inBounds(tx, ty) || visited[tx][ty]) continue;

                    System.out.println("Target probing (" + tx + "," + ty + ")");
                    previous = new int[]{tx, ty};
                    orientation = (d <= 1) ? 0 : 1;
                    posDone = false;
                    negDone = false;
                    orientationTriedBoth = false;
                    found = true;
                    break;
                }

                if (found) return previous;

                System.out.println("Target failed → hunt");
                resetTargeting();
                return hunt(visited);
            }

            // Sweep positive direction
            if (!posDone) {
                int dirIndex = (orientation == 0) ? 0 : 2;
                int nx = previous[0] + DIRS[dirIndex][0];
                int ny = previous[1] + DIRS[dirIndex][1];

                if (isM(nx, ny, visited)) {
                    System.out.println("Positive side M at (" + nx + "," + ny + ")");
                    posDone = true;
                } else {
                    System.out.println("Sweeping positive (" + nx + "," + ny + ")");
                    previous = new int[]{nx, ny};
                    return previous;
                }
            }

            // Sweep negative direction
            if (!negDone) {
                int dirIndex = (orientation == 0) ? 1 : 3;
                int nx = origin[0] + DIRS[dirIndex][0];
                int ny = origin[1] + DIRS[dirIndex][1];

                if (isM(nx, ny, visited)) {
                    System.out.println("Negative side M at (" + nx + "," + ny + ")");
                    negDone = true;
                } else {
                    System.out.println("Sweeping negative (" + nx + "," + ny + ")");
                    previous = new int[]{nx, ny};
                    return previous;
                }
            }

            // Both sides failed
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

                System.out.println("Both sides dead → hunt");
                resetTargeting();
                return hunt(visited);
            }

            System.out.println("Target fallback → hunt");
            resetTargeting();
            return hunt(visited);
        }
    }

    private boolean isM(int x, int y, boolean[][] visited) {
        if (!inBounds(x, y)) return true;
        return visited[x][y];
    }

    private void updateHitDist() {

        int arquintisCount = 0;
        for (String s : sunkList)
            if (s.equals("ARQUINTIS")) arquintisCount++;

        int smallestRemaining = 5;

        if (arquintisCount < 2) smallestRemaining = 3;
        else {
            boolean has2 = !sunkList.contains("INTERCEPTOR");
            boolean has4 = !sunkList.contains("ACCLIMATOR");

            if (has2) smallestRemaining = 2;
            else if (has4) smallestRemaining = 4;
            else smallestRemaining = 5;
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
