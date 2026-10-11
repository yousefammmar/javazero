public class Main {
    static String t(Object o) { return o.getClass().getSimpleName(); }
    public static void main(String[] args) {
        byte b1 = 100, b2 = 100;
        short s1 = 30000, s2 = 30000;
        char c1 = 'a', c2 = 'b';
        int i = 2_000_000_000;
        long l = 1L << 40;
        float f = 1.5f;
        double d = 2.5;
        System.out.println(b1 + b2 + " " + t(b1 + b2) + " " + (byte) (b1 + b2) + " " + (b1 * b2) + " " + (b1 << 1) + " " + (byte) (b1 << 1));
        System.out.println(s1 + s2 + " " + t(s1 + s2) + " " + (short) (s1 + s2) + " " + (s1 * s2));
        System.out.println(c1 + c2 + " " + t(c1 + c2) + " " + (char) (c1 + c2 - 'a') + " " + t(c1 + 1) + " " + t(c1 + 1L) + " " + t(c1 + 1f) + " " + t((char) (c1 + 1)));
        System.out.println(t(b1 + s1) + " " + t(b1 + c1) + " " + t(s1 + c1) + " " + t(i + l) + " " + t(i + f) + " " + t(l + f) + " " + t(l + d) + " " + t(f + d) + " " + t(i / 2) + " " + t(i / 2.0) + " " + t(l * 2) + " " + t(f * 2));
        System.out.println(t(-b1) + " " + t(~s1) + " " + t(+c1) + " " + t(-l) + " " + t(-f) + " " + t(b1 << 2L) + " " + t(l << 2) + " " + t(1 << l) + " " + t(c1 >> 1) + " " + t(b1 & s1) + " " + t(b1 & l) + " " + t(true & false));
        System.out.println(i + i + " " + (i + i + l) + " " + (l + i + i) + " " + ((long) i + i) + " " + (i + (long) i) + " " + (long) (i + i));
        System.out.println(i * 2 / 2 + " " + i * 2L / 2 + " " + i / 2 * 2 + " " + 1.0 * i * 2 / 2 + " " + (i * 2.0) + " " + (i * 2f) + " " + (float) i + " " + (i + 1f));
        System.out.println(l * l + " " + l * l * l + " " + (double) l * l + " " + (float) l * l);
        System.out.println(f + d + " " + (f + 1) + " " + (f * 2.1) + " " + (f * 2.1f) + " " + (d - f) + " " + (d % f) + " " + (f % d));
        int x = 10;
        double r = x / 4;
        double r2 = x / 4.0;
        double r3 = (double) x / 4;
        double r4 = (double) (x / 4);
        System.out.println(r + " " + r2 + " " + r3 + " " + r4);
        long m = 1000 * 1000 * 1000 * 10;
        long m2 = 1000L * 1000 * 1000 * 10;
        long m3 = 1000 * 1000 * 1000 * 10L;
        System.out.println(m + " " + m2 + " " + m3);
        long secs = 60 * 60 * 24 * 365 * 100;
        long secs2 = 60L * 60 * 24 * 365 * 100;
        System.out.println(secs + " " + secs2);
        int pct = 7 * 100 / 9;
        int pct2 = 7 / 9 * 100;
        double pct3 = 7.0 / 9 * 100;
        System.out.println(pct + " " + pct2 + " " + pct3);
        char c = 'a';
        c += 1;
        c++;
        c = (char) (c + 1);
        int ci = c + 1;
        System.out.println(c + " " + ci + " " + (char) ci + " " + (c > 'b') + " " + (c + c) + " " + (char) (c + c - 'a'));
        byte shifted = (byte) 0b1000_0001;
        System.out.println(shifted + " " + (shifted >> 1) + " " + (shifted >>> 1) + " " + ((shifted & 0xFF) >>> 1) + " " + (byte) (shifted >>> 1) + " " + (byte) ((shifted & 0xFF) >>> 1));
        System.out.println(1 / 2 + 1 / 2 + " " + 1 / 2.0 + 1 / 2 + " " + (1 + 1) / 2 + " " + 1 + 1 / 2);
        System.out.println(Integer.MAX_VALUE + 1 == Integer.MIN_VALUE);
        System.out.println(Integer.MAX_VALUE + 1L == Integer.MIN_VALUE);
        System.out.println((byte) 127 + (byte) 1 + " " + (byte) (127 + 1));
        System.out.println((short) 40000 + " " + (char) 40000 + 0 + " " + (int) (char) (short) 40000);
        System.out.println(5 & 3 | 8 ^ 2 + 1);
        System.out.println(0.1f + 0.1f + 0.1f == 0.3f);
        System.out.println(0.1 + 0.1 + 0.1 == 0.3);
        System.out.println(1e308 * 10 + " " + -1e308 * 10 + " " + 1e308 * 10 / (1e308 * 10) + " " + 1e-323 / 10 + " " + 1e-323 / 100);
        System.out.println('a' + 'b' + 'c' + " " + (char) ('a' + 'b' - 'a') + " " + ('a' + "b" + 'c') + " " + (char) 98 + 'c');
    }
}
