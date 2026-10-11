public class Main {
    static long fact(int n) { return n <= 1 ? 1 : n * fact(n - 1); }
    public static void main(String[] args) {
        for (int i = 15; i <= 25; i++) System.out.println(i + "! = " + fact(i));
        long h = 1125899906842597L;
        for (char c : "hash me please".toCharArray()) h = 31 * h + c;
        System.out.println(h);
        int ih = 17;
        for (int i = 0; i < 40; i++) ih = ih * 31 + i;
        System.out.println(ih);
        long fibA = 0, fibB = 1;
        for (int i = 0; i < 100; i++) { long t = fibA + fibB; fibA = fibB; fibB = t; }
        System.out.println(fibA + " " + fibB);
        long m = Long.MAX_VALUE;
        System.out.println(m + 1 + " " + (m + 1 == Long.MIN_VALUE) + " " + (m * 2) + " " + (-m - 2) + " " + Math.abs(Long.MIN_VALUE) + " " + (Long.MIN_VALUE / -1) + " " + (Long.MIN_VALUE % -1));
        int i1 = 1_000_000;
        long wrong = i1 * i1, right = (long) i1 * i1, alsoWrong = i1 * i1 * 1L;
        System.out.println(wrong + " " + right + " " + alsoWrong + " " + (1L * i1 * i1));
        System.out.println(1000000 * 1000000 + " " + 1000000L * 1000000 + " " + 46341 * 46341 + " " + 3037000500L * 3037000500L);
        int x = Integer.MAX_VALUE; x++; short s = Short.MAX_VALUE; s++; byte b = 127; b++; char c = 0; c--;
        System.out.println(x + " " + s + " " + b + " " + (int) c);
        long mod = 1_000_000_007L, p = 1;
        for (int i = 0; i < 100; i++) p = p * 31 % mod;
        System.out.println(p);
        long ms = 1700000000000L; int secs = (int) (ms / 1000);
        System.out.println(secs + " " + secs * 1000 + " " + secs * 1000L + " " + ms % 86400000L / 3600000);
        double d = Long.MAX_VALUE; long back = (long) d;
        System.out.println(d + " " + back + " " + (back == Long.MAX_VALUE) + " " + (long) (double) (Long.MAX_VALUE - 1000) + " " + (float) Integer.MAX_VALUE + " " + (int) (float) Integer.MAX_VALUE);
        System.out.println(Long.MAX_VALUE / 1000 * 1000 + " " + Long.MAX_VALUE % 1000 + " " + Long.divideUnsigned(-1L, 10) + " " + Long.remainderUnsigned(-1L, 10) + " " + Long.compareUnsigned(-1L, 1L));
        System.out.println(0.1 + 0.2 == 0.3);
        System.out.println((int) 1e10 + " " + (long) 1e19 + " " + (int) -1e10 + " " + (short) 1e10 + " " + (byte) 300.7 + " " + (char) 1e10 + 0);
    }
}
