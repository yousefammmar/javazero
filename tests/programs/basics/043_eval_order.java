public class Main {
    static StringBuilder log = new StringBuilder();
    static int v(String n, int val) { log.append(n).append(' '); return val; }
    static int[] arr(String n) { log.append(n).append(' '); return new int[]{0, 0, 0, 0}; }
    static String flush() { String s = log.toString().trim(); log.setLength(0); return s; }
    static int a = 1;
    static int bump() { a *= 2; return a; }
    static int sideA() { a += 10; return a; }
    public static void main(String[] args) {
        int r = v("a", 1) + v("b", 2) * v("c", 3);
        System.out.println(r + " : " + flush());
        r = v("a", 10) - v("b", 3) - v("c", 2);
        System.out.println(r + " : " + flush());
        r = v("a", 2) * (v("b", 3) + v("c", 4));
        System.out.println(r + " : " + flush());
        r = v("a", 1) < v("b", 2) ? v("c", 3) : v("d", 4);
        System.out.println(r + " : " + flush());
        boolean bl = v("a", 1) > 5 && v("b", 2) > 1 || v("c", 3) > 2;
        System.out.println(bl + " : " + flush());
        int[] x = arr("arr");
        x[v("idx", 1)] = v("val", 7);
        System.out.println(java.util.Arrays.toString(x) + " : " + flush());
        arr("arr2")[v("idx2", 2)] += v("rhs", 5);
        System.out.println(flush());
        String s = "" + v("p", 1) + v("q", 2) + (v("r", 3) + v("s", 4));
        System.out.println(s + " : " + flush());
        System.out.println(v("m", 5) + v("n", 6) + " : " + flush());
        System.out.println(bump() + a + " " + a);
        a = 1;
        System.out.println(a + bump() + " " + a);
        a = 1;
        System.out.println(a + a++ + a + " " + a);
        a = 5;
        System.out.println(a++ + a++ * a++ + " " + a);
        a = 5;
        a = a++ + sideA();
        System.out.println(a);
        a = 3;
        int y = a + (a = 10) + a;
        System.out.println(y + " " + a);
        a = 3;
        y = (a = 10) + a;
        System.out.println(y);
        int i = 0;
        int[] arr = new int[4];
        arr[i] = i = 2;
        System.out.println(java.util.Arrays.toString(arr) + " " + i);
        i = 0;
        arr[i++] = arr[i++] + 5;
        System.out.println(java.util.Arrays.toString(arr) + " " + i);
        i = 1;
        arr[i] = arr[i++] + i;
        System.out.println(java.util.Arrays.toString(arr) + " " + i);
        i = 1;
        i += i++ + ++i;
        System.out.println(i);
        i = 5;
        i = i-- - i--;
        System.out.println(i);
        int k = 2;
        k *= k + 1;
        System.out.println(k);
        k = 2;
        k = k * (k = 3) + k;
        System.out.println(k);
        try {
            int[] n = null;
            n[v("idx", 0)] = v("val", 1);
        } catch (NullPointerException e) {
            System.out.println("NPE : " + flush());
        }
        try {
            int[] n2 = new int[2];
            n2[v("idx", 5)] = v("val", 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("AIOOBE : " + flush());
        }
        try {
            int z = v("num", 1) / v("zero", 0) + v("never", 1);
            System.out.println(z);
        } catch (ArithmeticException e) {
            System.out.println("AE : " + flush());
        }
        String t = null;
        try {
            System.out.println(t.concat("" + v("arg", 1)));
        } catch (NullPointerException e) {
            System.out.println("NPE2 : " + flush());
        }
        System.out.println(v("x", 1) + "" + v("y", 2) + " " + (v("z", 3) + v("w", 4) + "") + " : " + flush());
        StringBuilder sb = new StringBuilder();
        sb.append(v("f1", 1)).append(v("f2", 2)).append(v("f3", 3));
        System.out.println(sb + " : " + flush());
        System.out.println(Math.max(v("m1", 1), v("m2", 9)) + " : " + flush());
        long big = v("L1", 1) * 1_000_000_000 * v("L2", 10);
        System.out.println(big + " : " + flush());
    }
}
