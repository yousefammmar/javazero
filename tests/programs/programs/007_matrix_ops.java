import java.util.*;

public class Main {
    static int[][] multiply(int[][] a, int[][] b) {
        int n = a.length, m = b[0].length, k = b.length;
        int[][] r = new int[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                for (int x = 0; x < k; x++)
                    r[i][j] += a[i][x] * b[x][j];
        return r;
    }

    static int[][] transpose(int[][] a) {
        int[][] t = new int[a[0].length][a.length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++)
                t[j][i] = a[i][j];
        return t;
    }

    static long det(int[][] a) {
        int n = a.length;
        if (n == 1) return a[0][0];
        if (n == 2) return (long) a[0][0] * a[1][1] - (long) a[0][1] * a[1][0];
        long d = 0;
        for (int c = 0; c < n; c++) {
            int[][] minor = new int[n - 1][n - 1];
            for (int i = 1; i < n; i++) {
                int cc = 0;
                for (int j = 0; j < n; j++) {
                    if (j == c) continue;
                    minor[i - 1][cc++] = a[i][j];
                }
            }
            d += (c % 2 == 0 ? 1 : -1) * (long) a[0][c] * det(minor);
        }
        return d;
    }

    static List<Integer> spiral(int[][] a) {
        List<Integer> out = new ArrayList<>();
        int top = 0, bot = a.length - 1, left = 0, right = a[0].length - 1;
        while (top <= bot && left <= right) {
            for (int j = left; j <= right; j++) out.add(a[top][j]);
            top++;
            for (int i = top; i <= bot; i++) out.add(a[i][right]);
            right--;
            if (top <= bot) { for (int j = right; j >= left; j--) out.add(a[bot][j]); bot--; }
            if (left <= right) { for (int i = bot; i >= top; i--) out.add(a[i][left]); left++; }
        }
        return out;
    }

    static void print(String name, int[][] m) {
        System.out.println(name + ":");
        for (int[] row : m) {
            StringBuilder sb = new StringBuilder();
            for (int v : row) sb.append(String.format("%5d", v));
            System.out.println(sb);
        }
    }

    public static void main(String[] args) {
        int[][] a = {{1, 2, 3}, {4, 5, 6}};
        int[][] b = {{7, 8}, {9, 10}, {11, 12}};
        print("A", a);
        print("B", b);
        print("A*B", multiply(a, b));
        print("B*A", multiply(b, a));
        print("A^T", transpose(a));
        int[][] sq = {{2, -1, 0, 3}, {1, 4, 2, -2}, {0, 5, -3, 1}, {7, 0, 1, 2}};
        print("Sq", sq);
        System.out.println("det(Sq) = " + det(sq));
        System.out.println("det(I3) = " + det(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
        int[][] g = new int[4][5];
        int k = 1;
        for (int i = 0; i < 4; i++) for (int j = 0; j < 5; j++) g[i][j] = k++;
        print("Grid", g);
        System.out.println("Spiral: " + spiral(g));
        System.out.println(Arrays.deepToString(transpose(g)));
        int trace = 0;
        for (int i = 0; i < sq.length; i++) trace += sq[i][i];
        System.out.println("trace = " + trace);
    }
}
