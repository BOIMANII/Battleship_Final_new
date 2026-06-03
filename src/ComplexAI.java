import java.util.ArrayList;

public class ComplexAI {

    private boolean hit;              // last shot result
    private int[] previous = {0,0};   // last shot coords
    private int hitDist = 1;          // dynamic hunt spacing
    private int dir = 0;              // direction index
    private int[] origin = null;      // first hit
    public boolean dirSet = false;    // first hit found
    public int hitCount = 0;          // consecutive misses in target mode
    public ArrayList<String> sunkList = new ArrayList<>(); // sunk ships

    // direction vectors: RIGHT, LEFT, DOWN, UP
    private final int[][] DIRS = {
        {0, 1},
        {0, -1},
        {1, 0},
        {-1, 0}
    };

    public int[] guess() {

        // update hitDist based on sunk ships
    	if (!sunkList.isEmpty()) {
    	    String last = sunkList.get(sunkList.size() - 1);

    	    int length = switch (last) {
    	        case "INTERCEPTOR" -> 2;
    	        case "ARQUINTIS"   -> 3;   
    	        case "ACCLIMATOR"  -> 4;
    	        case "VENATOR"     -> 5;
    	        default -> 1;
    	    };

    	    hitDist = length+1;   // gap = shipLength - 1
    	} else {
    	    hitDist = 2;
    	}


        int[] g = new int[2];
        if (!hit) {
            dirSet = false;
            dir = 0;
            hitCount = 0;

            int nx = previous[0] + hitDist;
            int ny = previous[1];

//            if (ny > 9) {
//                ny = 0;
//                
//            }
            if (nx > 9) {
            	ny += 1;
            	
            }

            g[0] = nx%10;
            g[1] = ny%10;

            previous = g;
            return g;
        }


        if (!dirSet) {
            dirSet = true;
            origin = previous.clone();
            dir = 0;
        }

        if (dir < 4) {
            int dx = DIRS[dir][0];
            int dy = DIRS[dir][1];

            g[0] = origin[0] + dx;
            g[1] = origin[1] + dy;

            dir++;
            previous = g;
            return g;
        }


        int lockedDir = dir - 1; // last direction tried

        int fx = previous[0] + DIRS[lockedDir][0];
        int fy = previous[1] + DIRS[lockedDir][1];

        if (inBounds(fx, fy)) {
            g[0] = fx;
            g[1] = fy;
            previous = g;
            return g;
        }

        int rx = origin[0] - DIRS[lockedDir][0];
        int ry = origin[1] - DIRS[lockedDir][1];

        if (inBounds(rx, ry)) {
            g[0] = rx;
            g[1] = ry;
            previous = g;
            return g;
        }

        reset();
        return guess();
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    private void reset() {
        dirSet = false;
        dir = 0;
        hitCount = 0;
        origin = null;
    }

    public void setHit(boolean h) {
        this.hit = h;
    }
}
