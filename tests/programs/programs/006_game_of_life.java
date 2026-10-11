public class Main {
    static final int N = 8;

    static int neighbors(boolean[][] g, int r, int c) {
        int n = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int rr = (r + dr + N) % N, cc = (c + dc + N) % N;
                if (g[rr][cc]) n++;
            }
        }
        return n;
    }

    static boolean[][] step(boolean[][] g) {
        boolean[][] next = new boolean[N][N];
        for (int r = 0; r < N; r++)
            for (int c = 0; c < N; c++) {
                int n = neighbors(g, r, c);
                next[r][c] = g[r][c] ? (n == 2 || n == 3) : n == 3;
            }
        return next;
    }

    static int count(boolean[][] g) {
        int k = 0;
        for (boolean[] row : g) for (boolean b : row) if (b) k++;
        return k;
    }

    static void show(boolean[][] g, int gen) {
        System.out.println("Generation " + gen + " (alive=" + count(g) + ")");
        for (boolean[] row : g) {
            StringBuilder sb = new StringBuilder();
            for (boolean b : row) sb.append(b ? '#' : '.');
            System.out.println(sb);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        boolean[][] g = new boolean[N][N];
        // glider
        g[0][1] = true; g[1][2] = true; g[2][0] = true; g[2][1] = true; g[2][2] = true;
        // blinker
        g[5][5] = true; g[5][6] = true; g[5][7] = true;
        for (int gen = 0; gen <= 6; gen++) {
            show(g, gen);
            g = step(g);
        }
        boolean[][] block = new boolean[N][N];
        block[3][3] = block[3][4] = block[4][3] = block[4][4] = true;
        boolean[][] after = step(block);
        boolean same = true;
        for (int r = 0; r < N; r++) for (int c = 0; c < N; c++) if (block[r][c] != after[r][c]) same = false;
        System.out.println("Block is still life: " + same);
    }
}
