import java.util.ArrayList;
import java.util.List;

public class Main {
    static long gcd(long a, long b) { while (b != 0) { long t = a % b; a = b; b = t; } return Math.abs(a); }
    static long lcm(long a, long b) { return a / gcd(a, b) * b; }
    static long modPow(long b, long e, long m) { long r = 1; b %= m; while (e > 0) { if ((e & 1) == 1) r = r * b % m; b = b * b % m; e >>= 1; } return r; }
    static List<Integer> factors(int n) { List<Integer> f = new ArrayList<>(); for (int p = 2; (long) p * p <= n; p++) while (n % p == 0) { f.add(p); n /= p; } if (n > 1) f.add(n); return f; }
    static int digitalRoot(int n) { while (n >= 10) { int s = 0; while (n > 0) { s += n % 10; n /= 10; } n = s; } return n; }
    static String roman(int n) {
        int[] v = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] s = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length; i++) while (n >= v[i]) { sb.append(s[i]); n -= v[i]; }
        return sb.toString();
    }
    static boolean overflowsAdd(int a, int b) { int r = a + b; return ((a ^ r) & (b ^ r)) < 0; }
    static int isqrt(long n) { long x = (long) Math.sqrt(n); while (x * x > n) x--; while ((x + 1) * (x + 1) <= n) x++; return (int) x; }
    static long binom(int n, int k) { long r = 1; for (int i = 1; i <= k; i++) r = r * (n - k + i) / i; return r; }
    public static void main(String[] args) {
        System.out.println(gcd(1071, 462) + " " + gcd(-12, 18) + " " + gcd(0, 5) + " " + lcm(21, 6) + " " + lcm(1L << 20, 3L << 10));
        System.out.println(modPow(2, 10, 1000) + " " + modPow(3, 200, 13) + " " + modPow(7, 1_000_000_006L, 1_000_000_007L) + " " + modPow(2, 62, Long.MAX_VALUE));
        for (int n : new int[]{12, 97, 360, 1001, 65536, 2147483646, 999999937}) System.out.println(n + " = " + factors(n));
        boolean[] comp = new boolean[100];
        StringBuilder pr = new StringBuilder();
        for (int i = 2; i < 100; i++) { if (!comp[i]) { pr.append(i).append(' '); for (int j = i * i; j < 100; j += i) comp[j] = true; } }
        System.out.println(pr.toString().trim());
        System.out.println(digitalRoot(987654321) + " " + digitalRoot(0) + " " + digitalRoot(38));
        for (int n : new int[]{1994, 2024, 3999, 4, 49, 888}) System.out.print(roman(n) + " ");
        System.out.println();
        System.out.println(overflowsAdd(Integer.MAX_VALUE, 1) + " " + overflowsAdd(5, 6) + " " + overflowsAdd(Integer.MIN_VALUE, -1) + " " + overflowsAdd(-5, 3));
        System.out.println(isqrt(99) + " " + isqrt(100) + " " + isqrt(1_000_000_000_000_000_000L) + " " + isqrt(0));
        System.out.println(binom(5, 2) + " " + binom(10, 5) + " " + binom(40, 20) + " " + binom(60, 30));
        int steps = 0;
        long n = 837799;
        long peak = n;
        while (n != 1) { n = n % 2 == 0 ? n / 2 : 3 * n + 1; peak = Math.max(peak, n); steps++; }
        System.out.println(steps + " " + peak);
        int perfect = 0;
        StringBuilder pf = new StringBuilder();
        for (int i = 2; i < 10000; i++) { int s = 1; for (int d = 2; d * d <= i; d++) if (i % d == 0) { s += d; if (d != i / d) s += i / d; } if (s == i) pf.append(i).append(' '); }
        System.out.println(pf.toString().trim());
        long h = 7;
        for (char c : "hash me".toCharArray()) h = h * 31 + c;
        int hi = 7;
        for (char c : "hash me".toCharArray()) hi = hi * 31 + c;
        System.out.println(h + " " + hi + " " + (int) h + " " + "hash me".hashCode());
        int x = 0b1011_0110;
        int ones = 0;
        for (int t = x; t != 0; t &= t - 1) ones++;
        System.out.println(ones + " " + Integer.bitCount(x) + " " + ((x & (x - 1)) == 0) + " " + ((64 & 63) == 0));
        long tri = 0, sq = 0, cube = 0;
        for (int i = 1; i <= 1000; i++) { tri += i; sq += (long) i * i; cube += (long) i * i * i; }
        System.out.println(tri + " " + sq + " " + cube + " " + (tri * tri == cube));
        int isum = 0;
        for (int i = 1; i <= 100000; i++) isum += i * i;
        System.out.println(isum);
        System.out.println(Long.MAX_VALUE % 1000 + " " + Long.MIN_VALUE % 1000 + " " + Long.MAX_VALUE / 1000 + " " + (Long.MAX_VALUE - 1) % 7);
        System.out.println(Integer.parseInt("123") * 2 + Integer.valueOf("7") + " " + Integer.toString(12345).length() + " " + String.valueOf(-5).length() + " " + (12345 + "").charAt(2));
    }
}
