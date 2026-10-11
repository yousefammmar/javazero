import java.util.*;
public class Main {
    public static void main(String[] args) {
        int[] a = {9, 4, 7, 1, 8, 2, 6, 3, 5, 0};
        int[] b = a.clone();
        Arrays.sort(b);
        System.out.println(Arrays.toString(a) + " -> " + Arrays.toString(b));
        int[] c = a.clone();
        Arrays.sort(c, 2, 7);
        System.out.println("range sort 2..7: " + Arrays.toString(c));
        System.out.println("binarySearch 6: " + Arrays.binarySearch(b, 6) + " 11: " + Arrays.binarySearch(b, 11) + " -1: " + Arrays.binarySearch(b, -1));
        System.out.println("binarySearch range: " + Arrays.binarySearch(b, 2, 5, 9));
        int[] f = new int[6];
        Arrays.fill(f, 7);
        Arrays.fill(f, 1, 3, -1);
        System.out.println(Arrays.toString(f));
        System.out.println(Arrays.toString(Arrays.copyOf(b, 4)) + " " + Arrays.toString(Arrays.copyOf(b, 13)));
        System.out.println(Arrays.toString(Arrays.copyOfRange(b, 3, 6)) + " " + Arrays.toString(Arrays.copyOfRange(b, 8, 14)));
        String[] s = {"x", "y"};
        System.out.println(Arrays.toString(Arrays.copyOf(s, 4)));
        boolean[] bo = Arrays.copyOf(new boolean[]{true}, 3);
        System.out.println(Arrays.toString(bo));
        double[] d = Arrays.copyOf(new double[]{1.5}, 3);
        System.out.println(Arrays.toString(d));
        System.out.println(Arrays.equals(a, a.clone()) + " " + Arrays.equals(a, b) + " " + (a == a.clone()) + " " + a.equals(a.clone()));
        System.out.println(Arrays.hashCode(new int[]{1, 2, 3}) + " " + Arrays.hashCode(new String[]{"a", "b"}) + " " + Arrays.hashCode((int[]) null));
        System.out.println(Arrays.stream(a).sum() + " " + Arrays.stream(a).max().getAsInt() + " " + Arrays.stream(a, 2, 5).sum());
        int[][] grid = {{1, 2, 3}, {4, 5}, {}};
        System.out.println(Arrays.deepToString(grid) + " " + Arrays.toString(grid[1]));
        int[][] g2 = new int[3][4];
        for (int[] row : g2) Arrays.fill(row, 2);
        g2[1][2] = 9;
        System.out.println(Arrays.deepToString(g2));
        System.out.println(Arrays.deepEquals(new int[][]{{1}, {2}}, new int[][]{{1}, {2}}) + " " + Arrays.equals(new int[][]{{1}}, new int[][]{{1}}));
        char[] cs = "hello world".toCharArray();
        Arrays.sort(cs);
        System.out.println(new String(cs).trim() + "|" + Arrays.toString(cs));
        System.out.println(Arrays.mismatch(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        System.out.println(Arrays.toString(new String[0]) + Arrays.toString((int[]) null));
        long[] ls = {5L, 1L, 3L};
        Arrays.sort(ls);
        System.out.println(Arrays.toString(ls));
        double[] dd = {3.5, -1.0, 2.25, 0.0};
        Arrays.sort(dd);
        System.out.println(Arrays.toString(dd));
    }
}
