public class Main {
    static final int ROWS = 6, COLS = 7;
    static char[][] b = new char[ROWS][COLS];

    static void init() { for (char[] r : b) java.util.Arrays.fill(r, '.'); }

    static int drop(int col, char p) {
        for (int r = ROWS - 1; r >= 0; r--) {
            if (b[r][col] == '.') { b[r][col] = p; return r; }
        }
        return -1;
    }

    static void print() {
        for (char[] r : b) System.out.println(new String(r).replace("", " ").trim());
        System.out.println("0 1 2 3 4 5 6");
    }

    static boolean wins(int r, int c) {
        char p = b[r][c];
        int[][] dirs = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] d : dirs) {
            int n = 1;
            for (int s = -1; s <= 1; s += 2) {
                int rr = r + s * d[0], cc = c + s * d[1];
                while (rr >= 0 && rr < ROWS && cc >= 0 && cc < COLS && b[rr][cc] == p) {
                    n++;
                    rr += s * d[0];
                    cc += s * d[1];
                }
            }
            if (n >= 4) return true;
        }
        return false;
    }

    static void game(int[] moves) {
        init();
        char p = 'R';
        for (int i = 0; i < moves.length; i++) {
            int r = drop(moves[i], p);
            if (r < 0) { System.out.println("Column " + moves[i] + " full!"); continue; }
            if (wins(r, moves[i])) {
                print();
                System.out.println(p + " wins on move " + (i + 1));
                return;
            }
            p = p == 'R' ? 'Y' : 'R';
        }
        print();
        System.out.println("No winner yet");
    }

    public static void main(String[] args) {
        game(new int[]{3, 3, 4, 4, 5, 5, 6});
        System.out.println();
        game(new int[]{0, 1, 1, 2, 2, 3, 2, 3, 3, 6, 3});
        System.out.println();
        game(new int[]{0, 0, 0, 0, 0, 0, 0, 1});
    }
}
