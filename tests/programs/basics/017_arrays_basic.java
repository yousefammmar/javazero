import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] a = new int[5];
        long[] l = new long[2];
        double[] d = new double[2];
        float[] f = new float[1];
        boolean[] b = new boolean[2];
        char[] c = new char[2];
        byte[] by = new byte[2];
        short[] sh = new short[2];
        String[] s = new String[3];
        Object[] o = new Object[1];
        Integer[] boxed = new Integer[2];
        System.out.println(Arrays.toString(a) + Arrays.toString(l) + Arrays.toString(d) + Arrays.toString(f));
        System.out.println(Arrays.toString(b) + Arrays.toString(by) + Arrays.toString(sh) + Arrays.toString(s) + Arrays.toString(o) + Arrays.toString(boxed));
        System.out.println((int) c[0] + " " + (c[1] == '\u0000'));
        int[] init = {5, 3, 9, 1, 7};
        System.out.println(init.length + " " + init[init.length - 1]);
        int[] sorted = init.clone();
        Arrays.sort(sorted);
        System.out.println(Arrays.toString(init) + " " + Arrays.toString(sorted));
        System.out.println(Arrays.binarySearch(sorted, 7) + " " + Arrays.binarySearch(sorted, 4) + " " + Arrays.binarySearch(sorted, 100));
        int[] cp = Arrays.copyOf(init, 8);
        System.out.println(Arrays.toString(cp));
        System.out.println(Arrays.toString(Arrays.copyOf(init, 2)));
        System.out.println(Arrays.toString(Arrays.copyOfRange(init, 1, 4)));
        System.out.println(Arrays.toString(Arrays.copyOfRange(init, 3, 8)));
        Arrays.fill(a, 7);
        System.out.println(Arrays.toString(a));
        Arrays.fill(a, 1, 3, 0);
        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.equals(init, init.clone()) + " " + (init == init.clone()) + " " + init.equals(init.clone()));
        System.out.println(Arrays.equals(new int[]{1}, new int[]{1, 2}));
        System.out.println(Arrays.hashCode(new int[]{1, 2, 3}) + " " + Arrays.hashCode(new int[0]));
        int[] alias = init;
        alias[0] = 99;
        System.out.println(init[0]);
        int[] copy = init.clone();
        copy[1] = -1;
        System.out.println(init[1] + " " + copy[1]);
        System.out.println(Arrays.stream(init).sum() + " " + Arrays.stream(init).max().getAsInt());
        String[] words = {"pear", "apple", "fig", "Banana", "cherry"};
        Arrays.sort(words);
        System.out.println(Arrays.toString(words));
        Arrays.sort(words, String.CASE_INSENSITIVE_ORDER);
        System.out.println(Arrays.toString(words));
        Arrays.sort(words, (x, y) -> x.length() - y.length());
        System.out.println(Arrays.toString(words));
        Integer[] bx = {3, 1, 2};
        Arrays.sort(bx, java.util.Collections.reverseOrder());
        System.out.println(Arrays.toString(bx));
        double[] ds = {3.5, -1.0, 2.25, 0.0};
        Arrays.sort(ds);
        System.out.println(Arrays.toString(ds));
        char[] cs = "hello".toCharArray();
        Arrays.sort(cs);
        System.out.println(new String(cs) + " " + Arrays.toString(cs));
        int[] part = {9, 8, 7, 6, 5, 4};
        Arrays.sort(part, 1, 4);
        System.out.println(Arrays.toString(part));
        System.out.println(Arrays.asList(1, 2, 3) + " " + Arrays.asList(words).size());
        try {
            System.out.println(a[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("AIOOBE " + e.getMessage());
        }
        try {
            System.out.println(a[-1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("AIOOBE " + e.getMessage());
        }
        try {
            int[] neg = new int[-1];
            System.out.println(neg.length);
        } catch (NegativeArraySizeException e) {
            System.out.println("NASE " + e.getMessage());
        }
        try {
            Object[] objs = new String[1];
            objs[0] = 1;
        } catch (ArrayStoreException e) {
            System.out.println("ASE " + e.getMessage());
        }
        int[] rev = {1, 2, 3, 4, 5};
        for (int i = 0, j = rev.length - 1; i < j; i++, j--) { int t = rev[i]; rev[i] = rev[j]; rev[j] = t; }
        System.out.println(Arrays.toString(rev));
        int[] fib = new int[10];
        fib[0] = 0; fib[1] = 1;
        for (int i = 2; i < fib.length; i++) fib[i] = fib[i - 1] + fib[i - 2];
        System.out.println(Arrays.toString(fib));
        long total = 0;
        for (int v : fib) total += v;
        System.out.println(total);
        byte[] bs = {(byte) 200, 127, -128};
        System.out.println(Arrays.toString(bs));
        System.out.println(new int[3].length + new int[]{1, 2}.length);
    }
}
