public class Main {
    static int[][] grid;
    static int nodes = 0;

    static boolean ok(int r, int c, int v) {
        for (int i = 0; i < 4; i++) {
            if (grid[r][i] == v || grid[i][c] == v) return false;
        }
        int br = r / 2 * 2, bc = c / 2 * 2;
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                if (grid[br + i][bc + j] == v) return false;
        return true;
    }

    static boolean solve(int pos) {
        nodes++;
        if (pos == 16) return true;
        int r = pos / 4, c = pos % 4;
        if (grid[r][c] != 0) return solve(pos + 1);
        for (int v = 1; v <= 4; v++) {
            if (ok(r, c, v)) {
                grid[r][c] = v;
                if (solve(pos + 1)) return true;
                grid[r][c] = 0;
            }
        }
        return false;
    }

    static void show(String title) {
        System.out.println(title);
        for (int r = 0; r < 4; r++) {
            if (r == 2) System.out.println("----+----");
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < 4; c++) {
                if (c == 2) sb.append("|");
                sb.append(grid[r][c] == 0 ? " ." : " " + grid[r][c]);
            }
            System.out.println(sb);
        }
    }

    static int countSolutions(int pos) {
        if (pos == 16) return 1;
        int r = pos / 4, c = pos % 4;
        if (grid[r][c] != 0) return countSolutions(pos + 1);
        int total = 0;
        for (int v = 1; v <= 4; v++) {
            if (ok(r, c, v)) {
                grid[r][c] = v;
                total += countSolutions(pos + 1);
                grid[r][c] = 0;
            }
        }
        return total;
    }

    public static void main(String[] args) {
        grid = new int[][]{{1, 0, 0, 0}, {0, 0, 3, 0}, {0, 4, 0, 0}, {0, 0, 0, 2}};
        show("Puzzle:");
        System.out.println("Solutions: " + countSolutions(0));
        System.out.println(solve(0) ? "Solved in " + nodes + " nodes" : "No solution");
        show("Result:");
        grid = new int[4][4];
        System.out.println("Empty grid has " + countSolutions(0) + " solutions");
        grid = new int[][]{{1, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        nodes = 0;
        System.out.println("Invalid puzzle solvable? " + solve(0) + " (nodes " + nodes + ")");
        grid = new int[][]{{1, 2, 3, 0}, {3, 4, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        show("Partial:");
        System.out.println("Solutions: " + countSolutions(0));
    }
}
