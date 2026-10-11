import java.util.Arrays;

public class Main {
    static void selection(int[] a) { for (int i = 0; i < a.length; i++) { int m = i; for (int j = i + 1; j < a.length; j++) if (a[j] < a[m]) m = j; int t = a[i]; a[i] = a[m]; a[m] = t; } }
    static void insertion(int[] a) { for (int i = 1; i < a.length; i++) { int k = a[i], j = i - 1; while (j >= 0 && a[j] > k) { a[j + 1] = a[j]; j--; } a[j + 1] = k; } }
    static int[] merge(int[] a) {
        if (a.length < 2) return a;
        int[] l = merge(Arrays.copyOfRange(a, 0, a.length / 2)), r = merge(Arrays.copyOfRange(a, a.length / 2, a.length));
        int[] o = new int[a.length];
        int i = 0, j = 0, k = 0;
        while (i < l.length && j < r.length) o[k++] = l[i] <= r[j] ? l[i++] : r[j++];
        while (i < l.length) o[k++] = l[i++];
        while (j < r.length) o[k++] = r[j++];
        return o;
    }
    static void quick(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int p = a[(lo + hi) >>> 1], i = lo, j = hi;
        while (i <= j) { while (a[i] < p) i++; while (a[j] > p) j--; if (i <= j) { int t = a[i]; a[i] = a[j]; a[j] = t; i++; j--; } }
        quick(a, lo, j); quick(a, i, hi);
    }
    static int[] counting(int[] a, int max) { int[] c = new int[max + 1]; for (int v : a) c[v]++; int[] o = new int[a.length]; int k = 0; for (int v = 0; v <= max; v++) while (c[v]-- > 0) o[k++] = v; return o; }
    static boolean sorted(int[] a) { for (int i = 1; i < a.length; i++) if (a[i - 1] > a[i]) return false; return true; }
    static int lcg = 12345;
    static int rnd(int n) { lcg = lcg * 1103515245 + 12345; return (lcg >>> 16) % n; }
    static int bsearch(int[] a, int k) { int lo = 0, hi = a.length - 1; while (lo <= hi) { int m = (lo + hi) >>> 1; if (a[m] < k) lo = m + 1; else if (a[m] > k) hi = m - 1; else return m; } return -(lo + 1); }
    public static void main(String[] args) {
        int[] base = {5, 2, 8, 1, 9, 3, 7, 3, 0, -4, 6};
        int[] a = base.clone(); selection(a); System.out.println("selection " + Arrays.toString(a));
        a = base.clone(); insertion(a); System.out.println("insertion " + Arrays.toString(a));
        System.out.println("merge     " + Arrays.toString(merge(base)));
        a = base.clone(); quick(a, 0, a.length - 1); System.out.println("quick     " + Arrays.toString(a));
        System.out.println("counting  " + Arrays.toString(counting(new int[]{4, 1, 3, 1, 0, 4}, 4)));
        System.out.println(Arrays.toString(base));
        int[] big = new int[200];
        for (int i = 0; i < big.length; i++) big[i] = rnd(1000);
        System.out.println(Arrays.toString(Arrays.copyOf(big, 10)));
        int[] b1 = big.clone(), b2 = big.clone(), b3 = big.clone();
        selection(b1); insertion(b2); quick(b3, 0, b3.length - 1);
        int[] b4 = merge(big), b5 = big.clone();
        Arrays.sort(b5);
        System.out.println(Arrays.equals(b1, b5) + " " + Arrays.equals(b2, b5) + " " + Arrays.equals(b3, b5) + " " + Arrays.equals(b4, b5) + " " + sorted(b5) + " " + sorted(big));
        System.out.println(Arrays.toString(Arrays.copyOfRange(b5, 0, 8)) + " " + Arrays.toString(Arrays.copyOfRange(b5, 192, 200)));
        long sum = 0;
        for (int v : big) sum += v;
        System.out.println(sum + " " + b5[100] + " " + Arrays.stream(big).max().getAsInt());
        System.out.println(bsearch(b5, b5[57]) >= 0 ? "found" : "missing");
        System.out.println(bsearch(new int[]{1, 3, 5}, 4) + " " + bsearch(new int[]{1, 3, 5}, 0) + " " + bsearch(new int[]{1, 3, 5}, 9) + " " + bsearch(new int[]{1, 3, 5}, 3));
        String[] w = {"delta", "Alpha", "charlie", "bravo", "echo", "alpha"};
        String[] w2 = w.clone();
        Arrays.sort(w2);
        System.out.println(Arrays.toString(w2));
        for (int i = 1; i < w.length; i++) { String k = w[i]; int j = i - 1; while (j >= 0 && w[j].compareToIgnoreCase(k) > 0) { w[j + 1] = w[j]; j--; } w[j + 1] = k; }
        System.out.println(Arrays.toString(w));
        double[] dd = {3.3, -1.1, 2.2, 0.0, -0.0, 1e10, -1e-10};
        Arrays.sort(dd);
        System.out.println(Arrays.toString(dd));
        long[] ll = {Long.MAX_VALUE, 0, Long.MIN_VALUE, -1, 1};
        Arrays.sort(ll);
        System.out.println(Arrays.toString(ll));
        char[] cc = "sorting".toCharArray();
        Arrays.sort(cc);
        System.out.println(String.valueOf(cc));
        Integer[] boxed = {3, 1, 2};
        Arrays.sort(boxed, (x, y) -> y - x);
        System.out.println(Arrays.toString(boxed));
    }
}
