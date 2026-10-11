import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[][] g = new int[3][4];
        System.out.println(Arrays.deepToString(g));
        g[1][2] = 5;
        g[2][3] = g[1][2] * 2;
        System.out.println(Arrays.deepToString(g));
        System.out.println(g.length + " " + g[0].length);
        int[][] jag = new int[4][];
        System.out.println(Arrays.toString(jag));
        for (int i = 0; i < jag.length; i++) {
            jag[i] = new int[i + 1];
            for (int j = 0; j <= i; j++) jag[i][j] = (i == 0 || j == 0 || j == i) ? 1 : jag[i - 1][j - 1] + jag[i - 1][j];
        }
        System.out.println(Arrays.deepToString(jag));
        for (int[] row : jag) System.out.println(Arrays.toString(row) + " len=" + row.length);
        int[][] lit = {{1, 2, 3}, {4, 5}, {}, {6}};
        System.out.println(Arrays.deepToString(lit));
        int[][] sh = lit.clone();
        sh[0][0] = 100;
        sh[1] = new int[]{-1};
        System.out.println(Arrays.deepToString(lit) + " " + Arrays.deepToString(sh));
        int[][] deep = new int[lit.length][];
        for (int i = 0; i < lit.length; i++) deep[i] = lit[i].clone();
        deep[0][1] = -2;
        System.out.println(lit[0][1] + " " + deep[0][1]);
        System.out.println(Arrays.equals(lit, deep) + " " + Arrays.deepEquals(lit, lit.clone()) + " " + Arrays.deepEquals(new int[][]{{1}}, new int[][]{{1}}) + " " + Arrays.equals(new int[][]{{1}}, new int[][]{{1}}));
        int[][][] cube = new int[2][3][2];
        cube[1][2][1] = 9;
        System.out.println(Arrays.deepToString(cube));
        System.out.println(cube.length + " " + cube[0].length + " " + cube[0][0].length);
        int[][][] partial = new int[2][2][];
        System.out.println(Arrays.deepToString(partial));
        String[][] names = new String[2][2];
        System.out.println(Arrays.deepToString(names));
        names[0] = new String[]{"a", "b"};
        names[1][0] = "z";
        System.out.println(Arrays.deepToString(names));
        double[][] dm = {{1.5, 2}, {3, 4.25}};
        System.out.println(Arrays.deepToString(dm));
        char[][] board = new char[3][3];
        for (char[] r : board) Arrays.fill(r, '.');
        board[1][1] = 'X'; board[0][2] = 'O';
        for (char[] r : board) System.out.println(new String(r));
        boolean[][] bb = new boolean[2][2];
        System.out.println(Arrays.deepToString(bb));
        long[][] ll = new long[1][1];
        System.out.println(Arrays.deepToString(ll));
        int[][] a = {{1, 2}, {3, 4}}, b = {{5, 6}, {7, 8}};
        int[][] prod = new int[2][2];
        for (int i = 0; i < 2; i++) for (int j = 0; j < 2; j++) for (int k = 0; k < 2; k++) prod[i][j] += a[i][k] * b[k][j];
        System.out.println(Arrays.deepToString(prod));
        int[][] tr = new int[2][2];
        for (int i = 0; i < 2; i++) for (int j = 0; j < 2; j++) tr[j][i] = a[i][j];
        System.out.println(Arrays.deepToString(tr));
        int[][] mult = new int[5][5];
        for (int i = 1; i < 5; i++) for (int j = 1; j < 5; j++) mult[i][j] = i * j;
        for (int[] r : mult) { StringBuilder sb = new StringBuilder(); for (int v : r) sb.append(String.format("%3d", v)); System.out.println(sb); }
        Object[] objs = {1, "s", new int[]{1, 2}, new String[]{"q"}, null};
        System.out.println(Arrays.deepToString(objs));
        Object[] self = new Object[2];
        self[0] = "x";
        System.out.println(Arrays.deepToString(self));
        int[][] sorted = {{3, 1}, {1, 2}, {2, 0}, {1, 1}};
        Arrays.sort(sorted, (p, q) -> p[0] != q[0] ? Integer.compare(p[0], q[0]) : Integer.compare(p[1], q[1]));
        System.out.println(Arrays.deepToString(sorted));
        int[][] fillRows = new int[2][3];
        for (int[] r : fillRows) Arrays.fill(r, 4);
        System.out.println(Arrays.deepToString(fillRows));
        try {
            System.out.println(jag[1][5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("AIOOBE " + e.getMessage());
        }
        try {
            int[][] nul = new int[2][];
            System.out.println(nul[0][0]);
        } catch (NullPointerException e) {
            System.out.println("NPE");
        }
        int[] one = {1, 2, 3};
        int[][] wrap = {one, one};
        wrap[0][0] = 50;
        System.out.println(Arrays.deepToString(wrap));
    }
}
