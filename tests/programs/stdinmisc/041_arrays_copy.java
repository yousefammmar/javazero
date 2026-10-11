import java.util.*;
public class Main {
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] b = new int[10];
        System.arraycopy(a, 2, b, 1, 4);
        System.out.println(Arrays.toString(b));
        System.arraycopy(a, 0, a, 2, 5);
        System.out.println(Arrays.toString(a));
        System.arraycopy(a, 3, a, 0, 5);
        System.out.println(Arrays.toString(a));
        int[] c = a.clone();
        c[0] = 99;
        System.out.println(a[0] + " " + c[0] + " " + (a == c) + " " + Arrays.equals(a, c));
        int[][] g = {{1, 2}, {3, 4}};
        int[][] g2 = g.clone();
        g2[0][0] = 100;
        g2[1] = new int[]{7};
        System.out.println(Arrays.deepToString(g) + " " + Arrays.deepToString(g2));
        System.out.println(Arrays.toString(Arrays.copyOf(a, 3)) + Arrays.toString(Arrays.copyOf(a, 10)) + Arrays.toString(Arrays.copyOfRange(a, 2, 5)) + Arrays.toString(Arrays.copyOfRange(a, 6, 10)));
        String[] ss = new String[3];
        System.out.println(Arrays.toString(ss) + " " + Arrays.toString(new boolean[2]) + Arrays.toString(new double[2]) + Arrays.toString(new char[]{'a', 'b'}) + Arrays.toString((int[]) null));
        Arrays.fill(b, 7);
        Arrays.fill(b, 2, 4, 0);
        System.out.println(Arrays.toString(b));
        int[] u = {5, 3, 9, 1, 7, 3};
        Arrays.sort(u);
        System.out.println(Arrays.toString(u) + " " + Arrays.binarySearch(u, 7) + " " + Arrays.binarySearch(u, 4) + " " + Arrays.binarySearch(u, 100));
        Integer[] boxed = {5, 3, 9, 1};
        Arrays.sort(boxed, Collections.reverseOrder());
        System.out.println(Arrays.toString(boxed) + " " + Arrays.asList(boxed) + " " + Arrays.stream(u).sum() + " " + Arrays.stream(u).max().getAsInt());
        try { System.arraycopy(a, 0, b, 5, 8); } catch (ArrayIndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        try { System.arraycopy(a, 0, new long[8], 0, 2); } catch (ArrayStoreException e) { System.out.println(e.getMessage()); }
        try { Object[] o = new String[1]; o[0] = 1; } catch (ArrayStoreException e) { System.out.println(e.getMessage()); }
        try { int[] z = new int[-1]; } catch (NegativeArraySizeException e) { System.out.println(e.getMessage()); }
        try { System.out.println(a[8]); } catch (ArrayIndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        char[] cc = "hello".toCharArray();
        Arrays.sort(cc);
        System.out.println(new String(cc) + " " + cc.length);
        System.out.println(Arrays.compare(new int[]{1, 2}, new int[]{1, 3}) + " " + Arrays.mismatch(new int[]{1, 2}, new int[]{1, 3}));
    }
}
