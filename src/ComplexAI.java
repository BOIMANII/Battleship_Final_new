import java.util.ArrayList;

public class ComplexAI {

    private boolean lastHit = false;
    private int[] previous = {0, 0};

    private int[] origin = null;      // first hit of current ship
    private int orientation = -1;     // 0 = horizontal, 1 = vertical, -1 = unknown

    private boolean posDone = false;  // RIGHT or DOWN side M found
    private boolean negDone = false;  // LEFT or UP side M found

    private int hitDist = 2;
    public ArrayList<String> sunkList = new ArrayList<>();

    // RIGHT, LEFT, DOWN, UP
    private final int[][] DIRS = {
        {0, 1}, {0, -1}, {1, 0}, {-1, 0}
    };

    // -------------------------
    // PUBLIC API
    // -------------------------
    public void setHit(boolean h) {
        lastHit = h;
    }

    public boolean getHit() {
        return lastHit;
    }

    public int[] guess(boolean[][] visited) {
        updateHitDist();

        // -------------------------
        // CASE 1: MISS
        // -------------------------
        if (!lastHit) {

            // If we were targeting a ship, this MISS belongs to one side
            if (origin != null && orientation != -1) {
                debug("MISS while targeting → marking side done");

                int dx = previous[0] - origin[0];
                int dy = previous[1] - origin[1];

                if (orientation == 0) { // horizontal
                    if (dy > 0) posDone = true;   // RIGHT side M
                    if (dy < 0) negDone = true;   // LEFT side M
                } else { // vertical
                    if (dx > 0) posDone = true;   // DOWN side M
                    if (dx < 0) negDone = true;   // UP side M
                }

                // Only when BOTH sides are M do we revert to hunt
                if (posDone && negDone) {
                    debug("BOTH sides M → revert to hitDist hunting FROM ORIGIN");
                    previous = origin.clone();   // hunt starts from origin
                    resetTargeting();
                    return hunt(visited);
                }

                // Otherwise continue sweeping
                return target(visited);
            }

            // Not targeting → normal hunt
            return hunt(visited);
        }

        // -------------------------
        // CASE 2: HIT
        // -------------------------
        if (origin == null) {
            origin = previous.clone();
            orientation = -1;
            posDone = false;
            negDone = false;
            debug("First HIT → origin set at (" + origin[0] + "," + origin[1] + ")");
        }

        return target(visited);
    }

    // -------------------------
    // HUNT MODE
    // -------------------------
    private int[] hunt(boolean[][] visited) {
        int row = previous[1];
        int col = previous[0];

        int offset = row % 2; // parity shift

        col += hitDist;

        if (col > 9) {
            row++;
            if (row > 9) row = 0;
            col = offset;
        }

        int[] g = {col, row};

        // Skip visited squares
        if (visited[g[0]][g[1]]) {
            debug("HUNT → (" + g[0] + "," + g[1] + ") already guessed, advancing");
            previous = g;
            return hunt(visited);
        }

        previous = g;
        debug("HUNT → firing at (" + g[0] + "," + g[1] + ") with offset " + offset);
        return g;
    }

    // -------------------------
    // TARGET MODE
    // -------------------------
    private int[] target(boolean[][] visited) {

        // -------------------------
        // STEP 1: Find orientation
        // -------------------------
        if (orientation == -1) {
            for (int d = 0; d < 4; d++) {
                int tx = origin[0] + DIRS[d][0];
                int ty = origin[1] + DIRS[d][1];

                if (!inBounds(tx, ty) || visited[tx][ty]) continue;

                previous = new int[]{tx, ty};
                debug("TARGET (find orientation) → probing " + dirName(d) +
                      " at (" + tx + "," + ty + ")");

                orientation = (d <= 1) ? 0 : 1; // horizontal or vertical
                posDone = false;
                negDone = false;
                return previous;
            }

            // No valid probe
            debug("TARGET → no valid probe around origin → reset + hunt");
            previous = origin.clone();
            resetTargeting();
            return hunt(visited);
        }

        // -------------------------
        // STEP 2: Sweep POSITIVE side
        // -------------------------
        if (!posDone) {
            int dirIndex = (orientation == 0) ? 0 : 2; // RIGHT or DOWN
            int nx = previous[0] + DIRS[dirIndex][0];
            int ny = previous[1] + DIRS[dirIndex][1];

            if (isM(nx, ny, visited)) {
                debug("TARGET → positive side M/OOB at (" + nx + "," + ny + ")");
                posDone = true;
            } else {
                previous = new int[]{nx, ny};
                debug("TARGET → sweeping positive side to (" + nx + "," + ny + ")");
                return previous;
            }
        }

        // -------------------------
        // STEP 3: Sweep NEGATIVE side
        // -------------------------
        if (!negDone) {
            int dirIndex = (orientation == 0) ? 1 : 3; // LEFT or UP
            int nx = origin[0] + DIRS[dirIndex][0];
            int ny = origin[1] + DIRS[dirIndex][1];

            if (isM(nx, ny, visited)) {
                debug("TARGET → negative side M/OOB at (" + nx + "," + ny + ")");
                negDone = true;
            } else {
                previous = new int[]{nx, ny};
                debug("TARGET → sweeping negative side to (" + nx + "," + ny + ")");
                return previous;
            }
        }

        // -------------------------
        // STEP 4: BOTH sides M → revert to hunt
        // -------------------------
        if (posDone && negDone) {
            debug("TARGET → BOTH M found → revert to hitDist hunting FROM ORIGIN");
            previous = origin.clone();
            resetTargeting();
            return hunt(visited);
        }

        // Should not reach here
        debug("TARGET → fallback to hunt");
        previous = origin.clone();
        resetTargeting();
        return hunt(visited);
    }

    // -------------------------
    // HELPERS
    // -------------------------
    private boolean isM(int x, int y, boolean[][] visited) {
        return !inBounds(x, y) || visited[x][y];
    }

    private void updateHitDist() {
        if (sunkList.isEmpty()) {
            hitDist = 2;
            return;
        }

        String last = sunkList.get(sunkList.size() - 1);
        int length = switch (last) {
            case "INTERCEPTOR" -> 2;
            case "ARQUINTIS" -> 3;
            case "ACCLIMATOR" -> 4;
            case "VENATOR" -> 5;
            default -> 2;
        };

        hitDist = Math.max(1, length - 1);
        debug("hitDist set to " + hitDist + " from " + last);
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    private void resetTargeting() {
        origin = null;
        orientation = -1;
        posDone = false;
        negDone = false;
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
        System.out.println("[AI DEBUG] " + msg);
    }
}
