import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int r = sc.nextInt(), c = sc.nextInt();
        int[][] m = new int[r][c];
        for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) m[i][j] = sc.nextInt();
        for (int[] row : m) {
            StringBuilder sb = new StringBuilder();
            for (int v : row) sb.append(String.format("%4d", v));
            System.out.println(sb);
        }
        int[][] t = new int[c][r];
        for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) t[j][i] = m[i][j];
        System.out.println(java.util.Arrays.deepToString(t));
        int tr = 0;
        for (int i = 0; i < Math.min(r, c); i++) tr += m[i][i];
        System.out.println("trace=" + tr);
    }
}
