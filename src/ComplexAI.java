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
    private int orientation = -1;     // 0 = horizontal, 1 = vertical, -1 = unknown

    private boolean posDone = false;  // RIGHT or DOWN side M found
    private boolean negDone = false;  // LEFT or UP side M found

    private boolean orientationTriedBoth = false;

    private int hitDist = 2;
    private int huntIndex = 0;        // true hitDist grid index

    public ArrayList<String> sunkList = new ArrayList<>();

    // RIGHT, LEFT, DOWN, UP
    private final int[][] DIRS = {
        {0, 1}, {0, -1}, {1, 0}, {-1, 0}
    };

    public void setHit(boolean h) {
        lastHit = h;
    }

    public boolean getHit() {
        return lastHit;
    }

    public int[] guess(boolean[][] visited) {
        updateHitDist();

        // -------------------------
        // MISS
        // -------------------------
        if (!lastHit) {

            if (origin != null && orientation != -1) {
                debug("MISS while targeting → marking side done");

                int dx = previous[0] - origin[0];
                int dy = previous[1] - origin[1];

                if (orientation == 0) { // horizontal
                    if (dy > 0) posDone = true;
                    if (dy < 0) negDone = true;
                } else { // vertical
                    if (dx > 0) posDone = true;
                    if (dx < 0) negDone = true;
                }

                // IMPORTANT: don't keep sweeping from the M cell
                previous = origin.clone();

                // Both sides M
                if (posDone && negDone) {

                    // Try other orientation first
                    if (!orientationTriedBoth) {
                        debug("FALLBACK → switching orientation");
                        orientation = (orientation == 0) ? 1 : 0;
                        posDone = false;
                        negDone = false;
                        orientationTriedBoth = true;
                        previous = origin.clone();
                        return target(visited);
                    }

                    // WALK‑SHIP: use last hit as new origin
                    debug("WALK SHIP → new origin = last hit");
                    origin = previous.clone();
                    orientation = -1;
                    posDone = false;
                    negDone = false;
                    orientationTriedBoth = false;
                    return target(visited);
                }

                return target(visited);
            }

            return hunt(visited);
        }

        // -------------------------
        // HIT
        // -------------------------
        if (origin == null) {
            origin = previous.clone();
            orientation = -1;
            posDone = false;
            negDone = false;
            orientationTriedBoth = false;
            debug("First HIT → origin set at (" + origin[0] + "," + origin[1] + ")");
        }

        return target(visited);
    }

    // ---------------------------------------------------------
    // HUNT MODE — TRUE hitDist grid, no recursion, no overflow
    // ---------------------------------------------------------
    private int[] hunt(boolean[][] visited) {

        for (int attempts = 0; attempts < 300; attempts++) {

            // If we've looped the board → shift parity
            if (attempts == 200) {
                debug("HUNT SHIFT → shifting parity down by 1 row");
                huntIndex += 10; // skip a row
            }

            int row = (huntIndex / 10) % 10;
            int col = huntIndex % 10;

            // hitDist spacing rule
            if ((col - (row % 2)) % hitDist != 0) {
                huntIndex++;
                continue;
            }

            if (!visited[col][row]) {
                previous = new int[]{col, row};
                debug("HUNT → firing at (" + col + "," + row + ")");
                huntIndex++;
                return previous;
            }

            debug("HUNT → (" + col + "," + row + ") already guessed");
            huntIndex++;
        }

        debug("HUNT → fallback to (0,0)");
        return new int[]{0, 0};
    }

    // ---------------------------------------------------------
    // TARGET MODE
    // ---------------------------------------------------------
    private int[] target(boolean[][] visited) {

        // Step 1: find orientation
        if (orientation == -1) {
            for (int d = 0; d < 4; d++) {
                int tx = origin[0] + DIRS[d][0];
                int ty = origin[1] + DIRS[d][1];

                if (!inBounds(tx, ty) || visited[tx][ty]) continue;

                previous = new int[]{tx, ty};
                debug("TARGET (find orientation) → probing " + dirName(d) +
                      " at (" + tx + "," + ty + ")");

                orientation = (d <= 1) ? 0 : 1;
                posDone = false;
                negDone = false;
                orientationTriedBoth = false;
                return previous;
            }

            debug("TARGET → no valid probe → reset + hunt");
            previous = origin.clone();
            resetTargeting();
            return hunt(visited);
        }

        // Step 2: sweep positive side
        if (!posDone) {
            int dirIndex = (orientation == 0) ? 0 : 2;
            int nx = previous[0] + DIRS[dirIndex][0];
            int ny = previous[1] + DIRS[dirIndex][1];

            if (isM(nx, ny, visited)) {
                debug("TARGET → positive side M at (" + nx + "," + ny + ")");
                posDone = true;
            } else {
                previous = new int[]{nx, ny};
                debug("TARGET → sweeping positive side to (" + nx + "," + ny + ")");
                return previous;
            }
        }

        // Step 3: sweep negative side
        if (!negDone) {
            int dirIndex = (orientation == 0) ? 1 : 3;
            int nx = origin[0] + DIRS[dirIndex][0];
            int ny = origin[1] + DIRS[dirIndex][1];

            if (isM(nx, ny, visited)) {
                debug("TARGET → negative side M at (" + nx + "," + ny + ")");
                negDone = true;
            } else {
                previous = new int[]{nx, ny};
                debug("TARGET → sweeping negative side to (" + nx + "," + ny + ")");
                return previous;
            }
        }

        // Step 4: both sides M
        if (posDone && negDone) {

            if (!orientationTriedBoth) {
                debug("FALLBACK → switching orientation");
                orientation = (orientation == 0) ? 1 : 0;
                posDone = false;
                negDone = false;
                orientationTriedBoth = true;
                previous = origin.clone();
                return target(visited);
            }

            debug("WALK SHIP → new origin = last hit");
            origin = previous.clone();
            orientation = -1;
            posDone = false;
            negDone = false;
            orientationTriedBoth = false;
            return target(visited);
        }

        debug("TARGET → fallback to hunt");
        previous = origin.clone();
        resetTargeting();
        return hunt(visited);
    }

    // ---------------------------------------------------------
    // HELPERS
    // ---------------------------------------------------------
    private boolean isM(int x, int y, boolean[][] visited) {
        return !inBounds(x, y) || visited[x][y];
    }

    private void updateHitDist() {

        int arquintisCount = 0;
        for (String s : sunkList) {
            if (s.equals("ARQUINTIS")) arquintisCount++;
        }

        int smallestRemaining = 5;

        if (arquintisCount < 2) {
            smallestRemaining = 3;
        } else {
            boolean has2 = !sunkList.contains("INTERCEPTOR");
            boolean has4 = !sunkList.contains("ACCLIMATOR");

            if (has2) smallestRemaining = 2;
            else if (has4) smallestRemaining = 4;
            else smallestRemaining = 5;
        }

        hitDist = smallestRemaining - 1;
        debug("hitDist set to " + hitDist + " (smallest remaining ship = " + smallestRemaining + ")");
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

    private String dirName(int d) {
        return switch (d) {
            case 0 -> "RIGHT";
            case 1 -> "LEFT";
            case 2 -> "DOWN";
            case 3 -> "UP";
            default -> "UNKNOWN";
        };
    }

    private void debug(String msg) {
    }
}
